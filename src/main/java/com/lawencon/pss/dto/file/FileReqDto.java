package com.lawencon.pss.dto.file;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileReqDto {
    private String fileContent;
    private String fileExt;
    private String fileName;
}
