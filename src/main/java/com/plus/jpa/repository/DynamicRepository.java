package com.plus.jpa.repository;

import com.plus.jpa.model.QueryParameters;
import com.plus.jpa.model.QueryService;
import com.plus.jpa.model.SerializablePage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Allen
 */
@NoRepositoryBean
public interface DynamicRepository<T, TIdType extends Serializable> extends
        JpaRepository<T, TIdType>,
        JpaSpecificationExecutor<T>,
        QueryService<T, DynamicRepository<T, TIdType>> {

    /**
     * 根据参数查询数据
     *
     * @param queryParameters 查询参数
     * @return 查询结果
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    default Page<T> findPage(QueryParameters queryParameters) {
        Sort sort = QueryUtils.createSort(queryParameters);
        Page<T> result;
        if (queryParameters.getPageIndex() < 0) {

            List<T> resultList = this.findAll((Specification<T>) (root, query, criteriaBuilder) -> QueryUtils
                    .setQueryParameters(queryParameters, root, criteriaBuilder, query), sort);

            result = new SerializablePage<>();
            ((SerializablePage<T>) result).setContent(resultList);
        } else if (queryParameters.getPageSize() == 1) {
            Optional<T> resultOne = this.findOne((Specification<T>) (root, query, criteriaBuilder) -> QueryUtils
                    .setQueryParameters(queryParameters, root, criteriaBuilder, query));
            result = new SerializablePage<T>();
            ArrayList<T> resultList = new ArrayList<>();
            resultOne.ifPresent(resultList::add);
            ((SerializablePage<T>) result).setContent(resultList);
        } else {
            PageRequest pageable = PageRequest.of(queryParameters.getPageIndex(), queryParameters.getPageSize(), sort);

            result = this.findAll((Specification<T>) (root, query, criteriaBuilder) -> QueryUtils
                    .setQueryParameters(queryParameters, root, criteriaBuilder, query), pageable);
        }

        return result;
    }

    /**
     * 查询数量
     *
     * @param queryParameters 查询参数
     * @return 数量
     */
    @Override
    default long countQuery(QueryParameters queryParameters) {
        return this.count((Specification<T>) (root, query, criteriaBuilder) -> QueryUtils
                .setQueryParameters(queryParameters, root, criteriaBuilder, query));
    }

}
