package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_m_companies", uniqueConstraints= {
		@UniqueConstraint(columnNames= {"company_name"}),
		@UniqueConstraint(columnNames= {"phone"}),
})
public class Company extends BaseModel {

    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @Column(name = "default_payment_day", nullable = false)
    private Byte defaultPaymentDay;
    
    @Column(name = "address")
    private String address;
    
    @Column(name = "phone", nullable = false)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "logo_id")
    private File logoId;
    
}
