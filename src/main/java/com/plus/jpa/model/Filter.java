package com.plus.jpa.model;

import lombok.Data;

/**
 * @author Allen
 */
@Data
public class Filter {
    private String field;
    private String compareType;
    private Object value;
}
