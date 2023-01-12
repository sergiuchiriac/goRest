package com.rest.goRest.rest.controller;

import com.rest.goRest.rest.request.UserRequest;
import com.rest.goRest.rest.response.FileMetadata;
import com.rest.goRest.rest.response.JobStatus;
import com.rest.goRest.rest.response.PostResponse;
import com.rest.goRest.rest.response.UserResponse;
import com.rest.goRest.service.FileService;
import com.rest.goRest.service.impl.UserCrudServiceImpl;
import com.rest.goRest.service.impl.UserJobServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserJobServiceImpl userJobService;
    private final UserCrudServiceImpl userCrudService;
    private final FileService fileService;

    @PostMapping("/job/start")
    public ResponseEntity<Instant> startJob() {
        return userJobService.startFetching();
    }

    @GetMapping("/job/status")
    public JobStatus getJobStatus() {
        return userJobService.retrieveJobStatus();
    }

    @PostMapping("/job/stop")
    public void stopJob() {
        userJobService.requestStop();
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse createUser(@RequestBody UserRequest userRequest,
                                   @RequestHeader(name = "Authorization") String token) {
        return userCrudService.createUser(userRequest, token);
    }

    @GetMapping("/users")
    public Page<UserResponse> getUsers(
            @RequestParam(value = "active", required = false) Boolean active,
            Pageable pageable
    ) {
        return userCrudService.fetchUsers(active, pageable);
    }

    @PutMapping(value = "/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse updateUser(@PathVariable("userId") Long userId,
                                   @RequestBody UserRequest userRequest,
                                   @RequestHeader(name = "Authorization") String token) {
        return userCrudService.updateUser(userId, userRequest, token);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId,
                                           @RequestHeader(name = "Authorization") String token) {
        userCrudService.deleteUser(userId, token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/users/{userId}/files")
    public List<FileMetadata> addFile(@PathVariable("userId") Long userId,
                                      @RequestParam("file") MultipartFile file) throws IOException {
        return fileService.saveFile(userId, file);
    }

    @GetMapping("/users/{userId}/files/{fileId}")
    public FileMetadata getFile(@PathVariable("userId") Long userId,
                                @PathVariable("fileId") Long fileId) {
        return fileService.fetchFile(userId, fileId);
    }

    @GetMapping("/users/{userId}/files")
    public List<FileMetadata> getFiles(@PathVariable("userId") Long userId) {
        return fileService.fetchFilesMetadata(userId);
    }

    @GetMapping("/users/posts")
    public List<PostResponse> getPosts(
            @RequestParam(value = "bodyText", required = false) String bodyText,
            @RequestParam(value = "titleText", required = false) String titleText,
            @RequestParam(value = "name", required = false) String userName
    ) {
        return userCrudService.fetchPosts(bodyText, titleText, userName);
    }


}
