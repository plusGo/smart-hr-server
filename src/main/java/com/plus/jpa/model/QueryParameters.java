package com.plus.jpa.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Allen
 */
@Data
public class QueryParameters {
    private int pageIndex;
    private int pageSize = 10;
    private List<Sort> sorts = new LinkedList<>();
    private FilterGroup filterGroup = new FilterGroup();
    private List<String> includes = new LinkedList<>();

    public FilterGroup getFilterGroup() {
        return filterGroup;
    }

    public QueryParameters where(final String field, final String compareType, final Object value) {
        this.filterGroup.where(field, compareType, value);
        return this;
    }

    public QueryParameters where(final String field, final Object value) {
        return this.where(field, "=", value);
    }

    public QueryParameters orderBy(final String field, final String order) {
        Sort sort = new Sort();
        sort.setField(field);
        sort.setOrder(order);
        this.sorts.add(sort);
        return this;
    }

    public QueryParameters orderBy(final String field) {
        return this.orderBy(field, "ASC");
    }

    public QueryParameters include(String field) {
        this.includes.add(field);
        return this;
    }

    private static ThreadLocal<QueryParameters> queryParametersThreadLocal = new ThreadLocal<>();

    public static QueryParameters instance() {
        QueryParameters queryParameters = queryParametersThreadLocal.get();
        if (Objects.isNull(queryParameters)) {
            queryParameters = new QueryParameters();
            queryParametersThreadLocal.set(queryParameters);
        }
        return queryParameters;
    }

    public static void clean() {
        queryParametersThreadLocal.remove();
    }
}
