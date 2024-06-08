package com.lawencon.pss.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_r_client_assignments", uniqueConstraints= {
		@UniqueConstraint(columnNames= {"client"})
})
public class ClientAssignment extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "ps_id", nullable = false)
    private User ps;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

}
