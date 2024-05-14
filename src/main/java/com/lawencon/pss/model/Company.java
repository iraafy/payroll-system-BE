package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_m_companies")
public class Company extends BaseModel {

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "logo_id")
    private File logoId;
    
    
}
