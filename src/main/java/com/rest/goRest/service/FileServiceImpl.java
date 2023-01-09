package com.rest.goRest.service;

import com.rest.goRest.dao.entity.FileMetadataEntity;
import com.rest.goRest.dao.repository.FileRepository;
import com.rest.goRest.exception.FileStorageException;
import com.rest.goRest.exception.RecordNotFoundException;
import com.rest.goRest.mapper.FileMetadataMapper;
import com.rest.goRest.rest.response.FileMetadata;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class FileServiceImpl {

    private final FileRepository fileRepository;
    private final FileMetadataMapper fileMapper;
    private Environment environment;

    public List<FileMetadata> saveFile(Long userId, MultipartFile file) throws IOException {
        if (isProd()) {
            saveFileInDatabase(userId, file);
            return fetchFilesMetadata(userId);
        } else {
            saveFileOnDisc(userId, file);
            return fetchFileMetadataFromDisc(userId);
        }
    }

    public boolean isProd() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if (profile.equals("prod")) {
                return true;
            }
        }
        return false;
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

    public void saveFileOnDisc(Long userId, MultipartFile file) {
        final String filename = file.getOriginalFilename();
        final String directory = "files/" + userId;
        Path filePath = Paths.get(directory, filename);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new FileStorageException("Error saving file on disc: " + e.getMessage());
        }
    }

    public List<FileMetadata> fetchFileMetadataFromDisc(Long userId) {
        final String directory = "files/" + userId;
        final Path dirPath = Paths.get(directory);
        List<FileMetadata> metadataList = new ArrayList<>();
        try(var fileList = Files.list(dirPath)) {
            fileList.forEach(filePath -> {
                try {
                    BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
                    String filename = filePath.getFileName().toString();
                    String type = Files.probeContentType(filePath);
                    long size = attr.size();

                    FileMetadata metadata = new FileMetadata();
                    metadata.setName(filename);
                    metadata.setType(type);
                    metadata.setSize(size);
                    metadataList.add(metadata);
                } catch (IOException e) {
                    throw new FileStorageException("Error getting file from disc: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            throw new FileStorageException(e.getMessage());
        }
        return metadataList;
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
