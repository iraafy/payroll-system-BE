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
    
	@Column(name = "file_content")
	private String fileContent;

	@Column(name = "file_ext")
	private String fileExt;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "stored_path")
    private String storedPath;
    
<<<<<<< HEAD
    @Column(name = "file_content")
    private String fileContent;
    
    @Column(name = "file_ext")
    private String fileExt;
    
    @Column(name = "file_name")
    private String fileName;
=======
>>>>>>> 0c4bdafe158490c41005741211504167960755fe
}
