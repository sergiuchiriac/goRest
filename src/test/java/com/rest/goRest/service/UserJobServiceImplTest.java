package com.rest.goRest.service;

import com.rest.goRest.dao.entity.UserEntity;
import com.rest.goRest.dao.repository.UserRepository;
import com.rest.goRest.mapper.UserMapper;
import com.rest.goRest.rest.GoRestApi;
import com.rest.goRest.rest.dto.PostDto;
import com.rest.goRest.rest.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserJobServiceImplTest {

    @Mock
    private GoRestApi goRestApi;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserJobServiceImpl userJobService;


    @Test
    void shouldFetchUsersFromApi(@Mock UserDto userDto) {
        when(goRestApi.users()).thenReturn(List.of(userDto));
        var result = userJobService.fetchUsersFromApi();
        assertNotNull(result);
        assertSame(userDto, result.get(0));
    }

    @Test
    void shouldFetchPostFromApi(@Mock UserDto userDto, @Mock PostDto postDto) {
        when(goRestApi.posts(anyLong())).thenReturn(List.of(postDto));

        var result = userJobService.fetchPostFromApi(userDto);
        assertNotNull(result);
        assertSame(postDto, result.get(0));
    }
    @Test
    void shouldSaveToData(@Mock UserDto userDto, @Mock PostDto postDto, @Mock UserEntity userEntity) {
        when(userMapper.map(any(UserDto.class))).thenReturn(userEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        userJobService.saveToData(userDto, List.of(postDto));
    }

    @Test
    void shouldThrowNullPointerExceptionWhenFetchPost() {
        Assertions.assertThrows(NullPointerException.class, () -> userJobService.fetchPostFromApi(null));
    }
}
