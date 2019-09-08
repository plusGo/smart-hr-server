package com.plus.jpa.repository;

import com.plus.jpa.model.FilterGroup;
import com.plus.jpa.model.QueryParameters;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class QueryUtils {

    static <T> Predicate setQueryParameters(QueryParameters queryParameters, Root<T> root,
                                            CriteriaBuilder criteriaBuilder, CriteriaQuery<?> query) {
        List<From<?, ?>> fetches = new ArrayList<>();
        if (!query.getResultType().equals(Long.class)) {
            fetches = setIncludes(queryParameters, root);
        }
        return setWheres(queryParameters, root, criteriaBuilder, fetches);
    }

    private static <T> List<From<?, ?>> setIncludes(QueryParameters queryParameters, Root<T> root) {
        final List<From<?, ?>> result = new ArrayList<From<?, ?>>();

        final List<String> includes = queryParameters.getIncludes();
        for (String include : includes) {
            String[] fieldNames = include.split("\\.");
            Fetch fetch = root.fetch(fieldNames[0], JoinType.LEFT);
            result.add((From<?, ?>) fetch);
            for (Integer i = 1; i < fieldNames.length; i++) {
                fetch = fetch.fetch(fieldNames[i], JoinType.LEFT);
                result.add((From<?, ?>) fetch);
            }
        }
        return result;
    }

    private static <T> Predicate setWheres(QueryParameters queryParameters, Root<T> root,
                                           CriteriaBuilder criteriaBuilder, List<From<?, ?>> fetches) {
        FilterGroup filterGroup = queryParameters.getFilterGroup();
        return PredicateUtils.createPredicate(filterGroup, root, criteriaBuilder, fetches);
    }

    static Sort createSort(QueryParameters queryParameters) {
        List<Sort.Order> orders = queryParameters.getSorts().stream()
                .map(sort -> new Sort.Order(
                        "DESC".equalsIgnoreCase(sort.getOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sort.getField()))
                .collect(Collectors.toList());

        return Sort.by(orders);
    }

}
