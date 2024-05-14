package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_m_files")
public class File extends BaseModel{

    @Column(name = "path", columnDefinition = "text")
    private String path;
}
