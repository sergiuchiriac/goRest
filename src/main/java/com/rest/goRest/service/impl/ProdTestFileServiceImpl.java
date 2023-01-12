package com.rest.goRest.service.impl;

import com.rest.goRest.dao.entity.FileMetadataEntity;
import com.rest.goRest.dao.repository.FileRepository;
import com.rest.goRest.exception.RecordNotFoundException;
import com.rest.goRest.mapper.FileMetadataMapper;
import com.rest.goRest.rest.response.FileMetadata;
import com.rest.goRest.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@Profile({"prod", "test"})
@AllArgsConstructor
public class ProdTestFileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMetadataMapper fileMapper;

    public List<FileMetadata> saveFile(Long userId, MultipartFile file) throws IOException {
        saveFileInDatabase(userId, file);
        return fetchFilesMetadata(userId);
    }

    @Transactional
    public void saveFileInDatabase(Long userId, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String type = file.getContentType();
        long size = file.getSize();
        byte[] data = file.getBytes();

        FileMetadataEntity metadata = new FileMetadataEntity();
        metadata.setUserId(userId);
        metadata.setFilename(filename);
        metadata.setType(type);
        metadata.setSize(size);
        metadata.setData(data);

        fileRepository.save(metadata);
    }

    @Transactional(readOnly = true)
    public List<FileMetadata> fetchFilesMetadata(Long userId) {
        return fileRepository.findFileMetadataByUserId(userId).stream().map(fileMapper::map).toList();
    }

    @Transactional(readOnly = true)
    public FileMetadata fetchFile(Long userId, Long fileId) {
        return fileMapper.map(fileRepository.findFileMetadataByUserAndFileId(userId, fileId).orElseThrow(
                () -> new RecordNotFoundException("File with not found " + fileId)));
    }
}
