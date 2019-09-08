package com.plus.jpa.model;

import lombok.Data;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Allen
 */
@Data
public class SerializablePage<T> extends PageImpl<T> {

    private List<T> content;
    private SerializablePageable pageable;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private SerializableSort sort;
    private boolean first;
    private boolean last;
    private int numberOfElements;

    public SerializablePage() {
        super(new ArrayList<>(), PageRequest.of(1, 10), 0);
    }
}
