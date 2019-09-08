package com.plus.jpa.repository;

import com.plus.jpa.model.Filter;
import com.plus.jpa.model.FilterGroup;
import com.plus.jpa.util.TypeUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class PredicateUtils {

    private static final String COMBINE_TYPE_AND = "and";
    private static final String COMBINE_TYPE_OR = "or";

    static <T> Predicate createPredicate(FilterGroup filterGroup, Root<T> root,
                                         CriteriaBuilder criteriaBuilder, List<From<?, ?>> fetches) {
        List<Predicate> predicates = filterGroup.getChildGroups().stream()
                .map(group -> createPredicate(group, root, criteriaBuilder, fetches))
                .collect(Collectors.toList());
        predicates.addAll(Arrays.asList(createPredicates(filterGroup.getFilters(), root, criteriaBuilder, fetches)));
        return combine(predicates.toArray(new Predicate[0]), filterGroup.getType(), criteriaBuilder);
    }

    private static <T> Predicate[] createPredicates(List<Filter> filters, Root<T> root,
                                                    CriteriaBuilder criteriaBuilder, List<From<?, ?>> fetches) {
        return filters.stream().map(filter -> {
            Object value = filter.getValue();
            if (Objects.isNull(value)) {
                // 参数没有值，忽略
                return null;
            }

            String[] fieldNames = filter.getField().split("\\.");

            Path<?> path = root;
            Type fieldType = null;
            for (String fieldName : fieldNames) {
                fieldType = TypeUtils.getFieldType(path.getModel().getBindableJavaType(), fieldName);
                if (fieldType instanceof ParameterizedType
                        && ((ParameterizedType) fieldType).getRawType().equals(Set.class)) {
                    Type innerType = TypeUtils.getGenericInnerType((ParameterizedType) fieldType);
                    Optional<From<?, ?>> fetched = fetches.stream()
                            .filter(fetch -> fetch.getModel().getBindableJavaType().equals(innerType)).findFirst();
                    if (fetched.isPresent()) {
                        path = fetched.get();
                    } else {
                        if (path instanceof Join) {
                            path = ((Join<?, ?>) path).join(fieldName, JoinType.LEFT);
                        } else {
                            path = root.join(fieldName, JoinType.LEFT);
                        }
                    }
                } else {
                    path = path.get(fieldName);
                }
            }

            String compareType = filter.getCompareType();
            return createPredicate(criteriaBuilder, compareType, path, value);
        })
                .filter(predicate -> !ObjectUtils.isEmpty(predicate))
                .toArray(Predicate[]::new);
    }

    @SuppressWarnings("unchecked")
    private static Predicate createPredicate(CriteriaBuilder criteriaBuilder, String compareType, Path path,
                                             Object value) {
        Predicate predicate;
        switch (compareType) {
            case "=":
                predicate = criteriaBuilder.equal(path, value);
                break;
            case "!=":
                predicate = criteriaBuilder.notEqual(path, value);
                break;
            case "<":
                predicate = criteriaBuilder.lessThan(path, (Comparable) value);
                break;
            case "<=":
                predicate = criteriaBuilder.lessThanOrEqualTo(path, (Comparable) value);
                break;
            case ">":
                predicate = criteriaBuilder.greaterThan(path, (Comparable) value);
                break;
            case ">=":
                predicate = criteriaBuilder.greaterThanOrEqualTo(path, (Comparable) value);
                break;
            case "like":
                predicate = criteriaBuilder.like(path, (String) value);
                break;
            case "is not null":
                predicate = criteriaBuilder.isNotNull(path);
                break;
            case "is null":
                predicate = criteriaBuilder.isNull(path);
                break;
            case "in":
                if (!(value instanceof Collection)) {
                    throw new InvalidParameterException("In查询需要Collection类型条件");
                }
                Collection collection = (Collection<?>) value;
                predicate = path.in(collection);
                break;
            default:
                throw new InvalidParameterException("不支持的比较表达式：" + compareType);
        }
        return predicate;
    }


    private static Predicate combine(Predicate[] predicates, String combineType, CriteriaBuilder criteriaBuilder) {
        if (COMBINE_TYPE_AND.equalsIgnoreCase(combineType)) {
            return criteriaBuilder.and(predicates);
        } else if (COMBINE_TYPE_OR.equalsIgnoreCase(combineType)) {
            return criteriaBuilder.or(predicates);
        } else {
            throw new RuntimeException("不支持 and 、or 之外的其他类型：" + combineType);
        }
    }
}
