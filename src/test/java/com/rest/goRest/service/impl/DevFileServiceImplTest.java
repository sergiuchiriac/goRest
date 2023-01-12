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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DevFileServiceImplTest {

    private static final Long LONG_ID = 1L;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private FileMetadataMapper fileMapper;

    @InjectMocks
    private DevFileServiceImpl fileService;


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
