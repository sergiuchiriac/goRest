package com.rest.goRest.dao.repository;

import com.rest.goRest.dao.entity.FileMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FileRepository extends JpaRepository<FileMetadataEntity, Long> {

    @Query("SELECT fme FROM FileMetadataEntity fme WHERE fme.userId =:userId")
    List<FileMetadataEntity> findFileMetadataByUserId(@Param("userId") Long userId);

    @Query("SELECT fme FROM FileMetadataEntity fme WHERE fme.userId =:userId and fme.id =:fileId")
    Optional<FileMetadataEntity> findFileMetadataByUserAndFileId(@Param("userId") Long userId, @Param("fileId") Long fileId);
}
