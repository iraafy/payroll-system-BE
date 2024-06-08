package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tb_m_user_roles", uniqueConstraints = {
		@UniqueConstraint(columnNames= {"roleCode"})
})
public class Role extends BaseModel {

    @Column(name = "role_code")
	private String roleCode;
	
	@Column(name = "role_name")
	private String roleName;

}
