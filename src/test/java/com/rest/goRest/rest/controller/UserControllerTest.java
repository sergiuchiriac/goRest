package com.rest.goRest.rest.controller;

import com.rest.goRest.rest.request.UserRequest;
import com.rest.goRest.rest.response.FileMetadata;
import com.rest.goRest.rest.response.JobStatus;
import com.rest.goRest.rest.response.PostResponse;
import com.rest.goRest.rest.response.UserResponse;
import com.rest.goRest.service.FileServiceImpl;
import com.rest.goRest.service.UserCrudServiceImpl;
import com.rest.goRest.service.UserJobServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final String TOKEN = "access_token";
    private static final Boolean ACTIVE = true;
    private static final Long LONG_ID = 1L;
    private static final String TEXT = "text";

    @Mock
    private UserCrudServiceImpl userCrudService;
    @Mock
    private FileServiceImpl fileService;
    @Mock
    private UserJobServiceImpl userJobService;

    @InjectMocks
    private UserController userController;

    @Test
    void shouldStartJob() {
        ResponseEntity<Instant> expectedResponse = ResponseEntity.ok(Instant.now());
        when(userJobService.startFetching()).thenReturn(expectedResponse);
        var result = userController.startJob();
        assertNotNull(result);
        assertSame(expectedResponse, result);
    }

    @Test
    void shouldGetJobStatus(@Mock JobStatus jobStatus) {
        when(userJobService.retrieveJobStatus()).thenReturn(jobStatus);
        var result = userController.getJobStatus();
        assertNotNull(result);
        assertSame(jobStatus, result);
    }

    @Test
    void shouldCreateUser(@Mock UserRequest userRequest, @Mock UserResponse userResponse) {
        when(userCrudService.createUser(any(UserRequest.class), anyString())).thenReturn(userResponse);
        var result = userController.createUser(userRequest, TOKEN);
        assertNotNull(result);
        assertSame(userResponse, result);
    }

    @Test
    void shouldGetUsers(@Mock Pageable pageable, @Mock UserResponse userResponse) {
        Page<UserResponse> userResponsePage = createOneItemPage(userResponse);
        when(userCrudService.fetchUsers(anyBoolean(), any(Pageable.class))).thenReturn(userResponsePage);
        var result = userController.getUsers(ACTIVE, pageable);
        assertNotNull(result);
        assertSame(userResponsePage, result);
    }

    @Test
    void shouldUpdateUser(@Mock UserRequest userRequest, @Mock UserResponse userResponse) {
        when(userCrudService.updateUser(anyLong(), any(UserRequest.class), anyString())).thenReturn(userResponse);
        var result = userController.updateUser(LONG_ID, userRequest, TOKEN);
        assertNotNull(result);
        assertSame(userResponse, result);
    }

    @Test
    void shouldAddFile(@Mock MultipartFile multipartFile, @Mock FileMetadata fileMetadata) throws IOException {
        when(fileService.saveFile(anyLong(), any(MultipartFile.class))).thenReturn(List.of(fileMetadata));
        var result = userController.addFile(LONG_ID, multipartFile);
        assertNotNull(result);
        assertSame(fileMetadata, result.get(0));
    }

    @Test
    void shouldGetFile(@Mock FileMetadata fileMetadata) {
        when(fileService.fetchFile(anyLong(), anyLong())).thenReturn(fileMetadata);
        var result = userController.getFile(LONG_ID, LONG_ID);
        assertNotNull(result);
        assertSame(fileMetadata, result);
    }

    @Test
    void shouldGetFiles(@Mock FileMetadata fileMetadata) {
        when(fileService.fetchFilesMetadata(anyLong())).thenReturn(List.of(fileMetadata));
        var result = userController.getFiles(LONG_ID);
        assertNotNull(result);
        assertSame(fileMetadata, result.get(0));
    }

    @Test
    void shouldGetPosts(@Mock PostResponse postResponse) {
        when(userCrudService.fetchPosts(anyString(), anyString(), anyString())).thenReturn(List.of(postResponse));
        var result = userController.getPosts(TEXT, TEXT, TEXT);
        assertNotNull(result);
        assertSame(postResponse, result.get(0));
    }


    private <T> Page<T> createOneItemPage(T item) {
        return new PageImpl<>(List.of(item));
    }

}
