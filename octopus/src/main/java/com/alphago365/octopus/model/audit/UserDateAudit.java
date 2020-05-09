package com.alphago365.octopus.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
@JsonIgnoreProperties(
        value = {"createdBy", "updatedBy"},
        allowGetters = true
)
@Data
public class UserDateAudit extends DateAudit {

    @CreatedBy
    @Column(updatable = false)
    protected Long createdBy; // user date

    @LastModifiedBy
    protected Long updatedBy;
}
