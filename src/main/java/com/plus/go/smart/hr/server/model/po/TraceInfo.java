package com.plus.go.smart.hr.server.model.po;

import lombok.Builder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalTime;

@MappedSuperclass
public class TraceInfo {
    @CreatedBy
    private String creator;

    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private LocalTime createTime;

    @LastModifiedBy
    private String modifier;

    @Column(name = "modify_time")
    @LastModifiedDate
    private LocalTime modifyTime;

    @Builder.Default
    private String deleted = "0";
}
