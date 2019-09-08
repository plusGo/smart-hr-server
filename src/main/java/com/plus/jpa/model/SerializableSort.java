package com.plus.jpa.model;

import lombok.Data;
import org.springframework.data.domain.Sort;

/**
 * @author Allen
 */
@Data
public class SerializableSort extends Sort {

    private boolean sorted;
    private boolean unsorted;

    public SerializableSort() {
        super(Direction.ASC, "nothing");
    }
}
