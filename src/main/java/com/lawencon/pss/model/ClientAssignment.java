package com.lawencon.pss.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_r_client_assignments")
public class ClientAssignment extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "ps_id")
    private User ps;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private User client;

}
