package com.rest.goRest.service;

import com.rest.goRest.rest.response.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileMetadata> saveFile(Long userId, MultipartFile file) throws IOException;

    List<FileMetadata> fetchFilesMetadata(Long userId);

    FileMetadata fetchFile(Long userId, Long fileId);
}
