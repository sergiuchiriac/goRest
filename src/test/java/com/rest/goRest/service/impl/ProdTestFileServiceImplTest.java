package com.rest.goRest.service.impl;

import com.rest.goRest.dao.entity.FileMetadataEntity;
import com.rest.goRest.dao.repository.FileRepository;
import com.rest.goRest.exception.RecordNotFoundException;
import com.rest.goRest.mapper.FileMetadataMapper;
import com.rest.goRest.rest.response.FileMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProdTestFileServiceImplTest {

    private static final Long LONG_ID = 1L;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileMetadataMapper fileMapper;
    @InjectMocks
    private ProdTestFileServiceImpl fileService;

    @Test
    void shouldSaveFile(@Mock MultipartFile multipartFile,
                        @Mock FileMetadataEntity fileMetadataEntity,
                        @Mock FileMetadata fileMetadata) throws IOException {
        when(fileRepository.findFileMetadataByUserId(anyLong())).thenReturn(List.of(fileMetadataEntity));
        when(fileMapper.map(any(FileMetadataEntity.class))).thenReturn(fileMetadata);

        var result = fileService.saveFile(LONG_ID, multipartFile);

        assertSame(fileMetadata, result.get(0));

    }

    @Test
    void shouldFetchFilesMetadata(@Mock FileMetadataEntity fileMetadataEntity,
                                  @Mock FileMetadata fileMetadata) {
        when(fileRepository.findFileMetadataByUserId(anyLong())).thenReturn(List.of(fileMetadataEntity));
        when(fileMapper.map(any(FileMetadataEntity.class))).thenReturn(fileMetadata);

        var result = fileService.fetchFilesMetadata(LONG_ID);

        assertNotNull(result);
        assertSame(fileMetadata, result.get(0));

    }

    @Test
    void shouldFetchFile(@Mock FileMetadataEntity fileMetadataEntity,
                         @Mock FileMetadata fileMetadata) {
        when(fileRepository.findFileMetadataByUserAndFileId(anyLong(), anyLong())).thenReturn(Optional.ofNullable(fileMetadataEntity));
        when(fileMapper.map(any(FileMetadataEntity.class))).thenReturn(fileMetadata);

        var result = fileService.fetchFile(LONG_ID, LONG_ID);

        assertNotNull(result);
        assertSame(fileMetadata, result);

    }

    @Test
    void shouldThrowRecordNotFoundExceptionWhenFetchFile() {
        Assertions.assertThrows(RecordNotFoundException.class, () -> fileService.fetchFile(null, null));
    }
}
