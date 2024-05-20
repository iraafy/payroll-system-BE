package com.lawencon.pss.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_m_files")
@Getter
@Setter
public class File extends BaseModel {
    
    @Column(name = "stored_path")
    private String storedPath;
    
    @Column(name = "file_content")
    private String fileContent;
    
    @Column(name = "file_ext")
    private String fileExt;
    
    @Column(name = "file_name")
    private String fileName;
}
