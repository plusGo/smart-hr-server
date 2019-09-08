package com.plus.jpa.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen
 */
@Data
public class FilterGroup {

    private String type = "and";
    private List<FilterGroup> childGroups = new ArrayList<>();
    private List<Filter> filters = new ArrayList<>();

    public FilterGroup where(final String field, final String compareType, final Object value) {
        Filter filter = new Filter();
        filter.setField(field);
        filter.setCompareType(compareType);
        filter.setValue(value);
        this.filters.add(filter);
        return this;
    }

    public FilterGroup where(final String field, final Object value) {
        return this.where(field, "=", value);
    }

    public FilterGroup newChildGroup(String type, FilterGroupCreator creator) {
        FilterGroup group = new FilterGroup();
        group.setType(type);
        creator.create(group);
        this.childGroups.add(group);
        return group;
    }

}
