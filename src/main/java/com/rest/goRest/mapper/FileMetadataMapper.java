package com.rest.goRest.mapper;

import com.rest.goRest.dao.entity.FileMetadataEntity;
import com.rest.goRest.rest.response.FileMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface FileMetadataMapper {

    @Mapping(target = "name", source = "fileMetadataEntity.filename")
    FileMetadata map(FileMetadataEntity fileMetadataEntity);
}
