package com.rest.goRest.mapper;


import com.rest.goRest.dao.entity.FileMetadataEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class FileMetadataMapperTest {

    private static final Long LONG_ID = 1L;
    private static final Long LONG_SIZE = 1L;
    private static final String NAME = "Name";
    private static final String TYPE = "Name";


    @InjectMocks
    private FileMetadataMapperImpl mapper;

    @Test
    void shouldMapEntityToResponse() {
        var actual = mapper.map(createFileMetadataEntity());
        assertAll(
                () -> assertEquals(LONG_ID, actual.getId()),
                () -> assertEquals(NAME, actual.getName()),
                () -> assertEquals(LONG_SIZE, actual.getSize()),
                () -> assertEquals(NAME, actual.getType())
        );
    }

    private FileMetadataEntity createFileMetadataEntity() {
        FileMetadataEntity fileMetadataEntity = new FileMetadataEntity();
        fileMetadataEntity.setId(LONG_ID);
        fileMetadataEntity.setUserId(LONG_ID);
        fileMetadataEntity.setSize(LONG_SIZE);
        fileMetadataEntity.setFilename(NAME);
        fileMetadataEntity.setType(TYPE);
        return fileMetadataEntity;
    }

}
