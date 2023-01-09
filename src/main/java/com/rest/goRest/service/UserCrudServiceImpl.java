package com.rest.goRest.service;


import com.rest.goRest.dao.entity.UserEntity;
import com.rest.goRest.dao.repository.PostRepository;
import com.rest.goRest.dao.repository.UserRepository;
import com.rest.goRest.dao.specification.PostSpecification;
import com.rest.goRest.dao.specification.UserSpecification;
import com.rest.goRest.exception.InvalidDataException;
import com.rest.goRest.exception.RecordNotFoundException;
import com.rest.goRest.mapper.PostMapper;
import com.rest.goRest.mapper.UserMapper;
import com.rest.goRest.rest.GoRestApi;
import com.rest.goRest.rest.dto.UserDto;
import com.rest.goRest.rest.request.UserRequest;
import com.rest.goRest.rest.response.PostResponse;
import com.rest.goRest.rest.response.UserResponse;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class UserCrudServiceImpl {
    private static final Integer DEFAULT_PAGE_SIZE = 100;
    private final GoRestApi goRestApi;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserMapper userMapper;
    private final PostMapper postMapper;


    @Transactional
    public UserResponse createUser(UserRequest userRequest, String token) {
        var userDto = new UserDto();
        try {
            userDto = goRestApi.createUser(token, userRequest);
        } catch (FeignException e) {
            throw new InvalidDataException(e.getMessage());
        }

        return userMapper.map(userRepository.save(userMapper.map(userDto)));
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> fetchUsers(Boolean active, Pageable pageable) {
        Specification<UserEntity> specification = null;
        int pageSize = pageable.getPageSize();
        if (!Objects.isNull(active)) {
            specification = UserSpecification.isStatus(active);
        }
        if (pageable.getPageSize() >= DEFAULT_PAGE_SIZE) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        var page = PageRequest.of(pageable.getPageNumber(), pageSize);
        return userRepository.findAll(specification, page).map(userMapper::map);
    }

    @Transactional
    public UserResponse updateUser(Long userId, UserRequest userRequest, String token) {
        var userDto = new UserDto();
        try {
            userDto = goRestApi.updateUser(token, userId, userRequest);
        } catch (FeignException e) {
            throw new InvalidDataException(e.getMessage());
        }
        return userMapper.map(userRepository.save(userMapper.map(userDto)));
    }

    @Transactional
    public void deleteUser(Long userId, String token) {
        try {
            goRestApi.deleteUser(userId, token);
        } catch (FeignException e) {
            throw new RecordNotFoundException("Resource not found");
        }
        deleteUserInDatabase(userId);
    }

    private void deleteUserInDatabase(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User not found"));
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> fetchPosts(String bodyText, String titleText, String userName) {
        PostSpecification spec = new PostSpecification(bodyText, titleText, userName);

        return postRepository.findAll(spec).stream().map(postMapper::map).collect(toList());
    }
}
