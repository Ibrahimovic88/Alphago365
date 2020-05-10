package com.alphago365.octopus.model;

import com.alphago365.octopus.model.audit.DateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "providers")
@Data
@NoArgsConstructor
public class Provider extends DateAudit {
    @Id
    private Integer id;

    private String name;

    public Provider(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toString() {
        return name;
    }

}
