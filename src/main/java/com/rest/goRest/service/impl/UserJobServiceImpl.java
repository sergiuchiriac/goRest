package com.rest.goRest.service.impl;

import com.rest.goRest.dao.repository.UserRepository;
import com.rest.goRest.exception.InvalidDataException;
import com.rest.goRest.mapper.UserMapper;
import com.rest.goRest.rest.GoRestApi;
import com.rest.goRest.rest.dto.PostDto;
import com.rest.goRest.rest.dto.UserDto;
import com.rest.goRest.rest.response.JobStatus;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
@Slf4j
public class UserJobServiceImpl {

    private final GoRestApi goRestApi;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static Instant jobStartTime;
    private static Instant jobEndTime;
    private static JobStatus.Status jobStatus;
    private static volatile boolean stopRequested;


    public ResponseEntity<Instant> startFetching() {
        if (Objects.isNull(jobStartTime)) {
            jobStartTime = Instant.now();
            fetchUsers();
        }
        return ResponseEntity.ok(Instant.now());
    }

    private void fetchUsers() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            fetchUsersWithPosts();
            jobStartTime = null;
        });
    }

    private void fetchUsersWithPosts() {
        jobStatus = JobStatus.Status.IN_PROGRESS;
        var userList = fetchUsersFromApi();

        for (UserDto user : userList) {
            var posts = fetchPostFromApi(user);

            saveToData(user, posts);

            if (stopRequested()) {
                jobStatus = JobStatus.Status.FINISHED;
                jobEndTime = Instant.now();
                stopRequested = false;
                return;
            }

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                jobStatus = JobStatus.Status.ERROR;
                e.printStackTrace();
            }
        }
        jobStatus = JobStatus.Status.FINISHED;
        jobEndTime = Instant.now();
        stopRequested = false;
    }

    protected List<UserDto> fetchUsersFromApi() {
        List<UserDto> users;
        try {
            users = goRestApi.users();
        } catch (FeignException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return users;
    }

    protected List<PostDto> fetchPostFromApi(UserDto user) {
        List<PostDto> posts;
        try {
            posts = goRestApi.posts(user.getId());
        } catch (FeignException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return posts;
    }

    protected void saveToData(UserDto user, List<PostDto> posts) {
        if (Objects.nonNull(posts) && posts.size() > 0) {
            user.setPosts(posts);
        }
        var userEntity = userMapper.map(user);
        if (Objects.nonNull(userEntity.getPosts()) && userEntity.getPosts().size() > 0) {
            userEntity.getPosts().forEach(post -> post.setUser(userEntity));
        }
        userRepository.save(userEntity);
    }


    public JobStatus retrieveJobStatus() {
        if (Objects.isNull(jobStartTime)) {
            return new JobStatus(JobStatus.Status.FINISHED, null, null);
        }
        return new JobStatus(jobStatus, jobStartTime, jobEndTime);
    }

    public static synchronized void requestStop() {
        stopRequested = true;
    }

    public static synchronized boolean stopRequested() {
        return stopRequested;
    }
}
