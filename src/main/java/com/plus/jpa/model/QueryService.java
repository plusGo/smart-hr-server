package com.plus.jpa.model;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author Allen
 */
public interface QueryService<T, TThis extends QueryService<T, TThis>> {

    /**
     * 设置查询参数
     *
     * @param field       字段
     * @param compareType 比较类型
     * @param value       比较值
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default TThis where(String field, String compareType, Object value) {
        QueryParameters.instance().where(field, compareType, value);
        return (TThis) this;
    }

    /**
     * 设置相等查询参数
     *
     * @param field 字段
     * @param value 比较值
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default TThis where(String field, Object value) {
        QueryParameters.instance().where(field, value);
        return (TThis) this;
    }

    /**
     * 设置分组查询参数
     *
     * @param type    分组类型
     * @param creator 内部构造方法
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default TThis whereGroup(String type, FilterGroupCreator creator) {
        QueryParameters.instance().getFilterGroup().setType(type);
        creator.create(QueryParameters.instance().getFilterGroup());
        return (TThis) this;
    }

    /**
     * 关联对象
     *
     * @param field 关联属性
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default TThis include(String field) {
        QueryParameters.instance().include(field);
        return (TThis) this;
    }

    /**
     * 设置排序
     *
     * @param field 排序字段
     * @param order 顺序
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default TThis orderBy(String field, String order) {
        QueryParameters.instance().orderBy(field, order);
        return (TThis) this;
    }

    /**
     * 设置正序排序
     *
     * @param field 排序字段
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default TThis orderBy(String field) {
        QueryParameters.instance().orderBy(field);
        return (TThis) this;
    }

    /**
     * 起始页，从0开始
     *
     * @param pageIndex 页号
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default TThis pageIndex(int pageIndex) {
        QueryParameters.instance().setPageIndex(pageIndex);
        return (TThis) this;
    }

    /**
     * 设置分页大小
     *
     * @param pageSize 分页大小
     * @return 查询对象
     */
    @SuppressWarnings("unchecked")
    default TThis pageSize(int pageSize) {
        QueryParameters.instance().setPageSize(pageSize);
        return (TThis) this;
    }

    /**
     * 根据查询条件查询
     *
     * @return 查询结果
     */
    default Page<T> findPage() {
        try {
            return findPage(QueryParameters.instance());
        } finally {
            QueryParameters.clean();
        }
    }

    /**
     * 分页查询数据
     *
     * @param queryParameters 查询参数
     * @return 查询结果
     */
    Page<T> findPage(QueryParameters queryParameters);

    /**
     * 根据参数查询数据
     *
     * @return 查询结果
     */
    default List<T> findList() {
        try {
            return findList(QueryParameters.instance());
        } finally {
            QueryParameters.clean();
        }
    }

    /**
     * 根据参数查询数据
     *
     * @param queryParameters 查询参数
     * @return 查询结果
     */
    default List<T> findList(QueryParameters queryParameters) {
        queryParameters.setPageIndex(-1);
        return findPage(queryParameters).getContent();
    }

    /**
     * 根据参数查询一个数据
     *
     * @return 查询结果
     */
    default Optional<T> findOne() {
        try {
            return findOne(QueryParameters.instance());
        } finally {
            QueryParameters.clean();
        }
    }

    /**
     * 根据参数查询一个数据
     *
     * @param queryParameters 查询参数
     * @return 查询结果
     */
    default Optional<T> findOne(QueryParameters queryParameters) {
        queryParameters.setPageSize(1);
        Page<T> resultPage = findPage(queryParameters);
        return Optional.ofNullable(resultPage.getContent().size() > 0 ? resultPage.getContent().get(0) : null);
    }

    /**
     * 查询数量
     *
     * @param queryParameters 查询参数
     * @return 数量
     */
    long countQuery(QueryParameters queryParameters);

    /**
     * 查询数量
     *
     * @return 数量
     */
    default long countQuery() {
        try {
            return countQuery(QueryParameters.instance());
        } finally {
            QueryParameters.clean();
        }
    }
}
