package com.plus.go.smart.hr.server.model.po;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "function_po")
@Builder
@Data
@EntityListeners(AuditingEntityListener.class)
public class Function extends TraceInfo {
    @Id
    @Column(name = "id", updatable = false)
    private String id;

    private String name;
}
