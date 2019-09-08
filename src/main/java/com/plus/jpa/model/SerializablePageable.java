package com.plus.jpa.model;

import lombok.Data;
import org.springframework.data.domain.PageRequest;

/**
 * @author Allen
 */
@Data
public class SerializablePageable extends PageRequest {

    private SerializableSort sort;
    private long offset;
    private int pageNumber;
    private int pageSize;
    private boolean unpaged;
    private boolean paged;

    public SerializablePageable() {
        super(1, 10);
    }

}
