package com.rest.goRest.service.impl;

import com.rest.goRest.dao.entity.PostEntity;
import com.rest.goRest.dao.entity.UserEntity;
import com.rest.goRest.dao.repository.PostRepository;
import com.rest.goRest.dao.repository.UserRepository;
import com.rest.goRest.dao.specification.PostSpecification;
import com.rest.goRest.exception.RecordNotFoundException;
import com.rest.goRest.mapper.PostMapper;
import com.rest.goRest.mapper.UserMapper;
import com.rest.goRest.rest.GoRestApi;
import com.rest.goRest.rest.dto.UserDto;
import com.rest.goRest.rest.request.UserRequest;
import com.rest.goRest.rest.response.PostResponse;
import com.rest.goRest.rest.response.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserCrudServiceImplTest {

    private static final String TOKEN = "access_token";

    private static final String TEXT = "text";
    private static final Boolean ACTIVE = true;
    private static final Long USER_ID = 1L;

    @Mock
    private GoRestApi goRestApi;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private UserCrudServiceImpl userCrudService;

    @Test
    void shouldCreateUser(@Mock UserDto userDto,
                          @Mock UserRequest userRequest,
                          @Mock UserEntity userEntity,
                          @Mock UserResponse userResponse) {
        when(goRestApi.createUser(anyString(), any(UserRequest.class)))
                .thenReturn(userDto);
        when(userMapper.map(any(UserDto.class)))
                .thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.map(any(UserEntity.class)))
                .thenReturn(userResponse);
        var result = userCrudService.createUser(userRequest, TOKEN);

        assertNotNull(result);
        assertSame(userResponse, result);
    }

    @Test
    void shouldFetchUsers(@Mock Pageable pageable,
                          @Mock UserEntity userEntity,
                          @Mock UserResponse userResponse) {
        when(pageable.getPageSize()).thenReturn(10);
        Page<UserEntity> usersEntityPage = createOneItemPage(userEntity);

        when(userRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(usersEntityPage);
        when(userMapper.map(any(UserEntity.class)))
                .thenReturn(userResponse);
        var result = userCrudService.fetchUsers(ACTIVE, pageable);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getSize());
    }

    @Test
    void shouldUpdateUser(@Mock UserDto userDto,
                          @Mock UserRequest userRequest,
                          @Mock UserEntity userEntity,
                          @Mock UserResponse userResponse) {
        when(goRestApi.updateUser(anyString(), anyLong(), any(UserRequest.class)))
                .thenReturn(userDto);
        when(userMapper.map(any(UserDto.class)))
                .thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userMapper.map(any(UserEntity.class)))
                .thenReturn(userResponse);
        var result = userCrudService.updateUser(USER_ID, userRequest, TOKEN);

        assertNotNull(result);
        assertSame(userResponse, result);
    }

    @Test
    void shouldDeleteUser(@Mock UserEntity userEntity) {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(userEntity));
        userCrudService.deleteUser(USER_ID, TOKEN);
        verify(userRepository).save(userEntity);
    }

    @Test
    void shouldFetchPosts(@Mock PostEntity postEntity, @Mock PostResponse postResponse) {
        when(postRepository.findAll(any(PostSpecification.class))).thenReturn(List.of(postEntity));
        when(postMapper.map(any(PostEntity.class))).thenReturn(postResponse);

        var result = userCrudService.fetchPosts(TEXT, TEXT, TEXT);

        assertNotNull(result);
        assertSame(postResponse, result.get(0));

    }

    @Test
    void shouldThrowRecordNotFoundExceptionWhenDeleteUser() {
        Assertions.assertThrows(RecordNotFoundException.class, () -> userCrudService.deleteUser(null, TOKEN));
    }

    private <T> Page<T> createOneItemPage(T item) {
        return new PageImpl<>(List.of(item));
    }
}
