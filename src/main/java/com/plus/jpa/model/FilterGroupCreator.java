package com.plus.jpa.model;

/**
 * @author Allen
 */
@FunctionalInterface
public interface FilterGroupCreator {

    /**
     * 新建下级分组
     *
     * @param group 下级分组对象
     */
    void create(FilterGroup group);

}
