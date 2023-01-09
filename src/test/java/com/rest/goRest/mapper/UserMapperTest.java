package com.rest.goRest.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.rest.goRest.dao.entity.PostEntity;
import com.rest.goRest.dao.entity.UserEntity;
import com.rest.goRest.rest.dto.PostDto;
import com.rest.goRest.rest.dto.UserDto;
import com.rest.goRest.rest.response.PostResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    private static final Long LONG_ID = 1L;
    private static final String NAME = "Name";
    private static final String EMAIL = "test@email.com";
    private static final String GENDER = "Male";
    private static final String STATUS = "active";
    private static final String BODY = "body text";
    private static final String TITLE = "title text";
    private static final JsonNode DATA = BooleanNode.TRUE;

    @Mock
    private PostMapper postMapper;
    @InjectMocks
    private UserMapperImpl mapper;

    @Test
    void shouldMapEntityToResponse() {
        when(postMapper.map(any(PostEntity.class))).thenReturn(createPostResponse());
        var actual = mapper.map(createUserEntity());
        assertAll(
                () -> assertEquals(LONG_ID, actual.getId()),
                () -> assertEquals(NAME, actual.getName()),
                () -> assertEquals(EMAIL, actual.getEmail()),
                () -> assertEquals(GENDER, actual.getGender()),
                () -> assertEquals(STATUS, actual.getStatus()),
                () -> assertEquals(LONG_ID, ((PostResponse) actual.getPosts().get(0)).getUserId()),
                () -> assertEquals(NAME, ((PostResponse) actual.getPosts().get(0)).getUserName()),
                () -> assertEquals(TITLE, ((PostResponse) actual.getPosts().get(0)).getTitle()),
                () -> assertEquals(BODY, ((PostResponse) actual.getPosts().get(0)).getBody())
        );
    }

    @Test
    void shouldMapDtoToEntity() {
        when(postMapper.map(any(PostDto.class))).thenReturn(createPostEntity());
        var actual = mapper.map(createUserDto());
        assertAll(
                () -> assertEquals(NAME, actual.getName()),
                () -> assertEquals(EMAIL, actual.getEmail()),
                () -> assertEquals(GENDER, actual.getGender()),
                () -> assertEquals(STATUS, actual.getStatus()),
                () -> assertEquals(LONG_ID, actual.getPosts().get(0).getId()),
                () -> assertEquals(DATA, actual.getPosts().get(0).getData())


        );
    }

    private UserDto createUserDto() {
        return UserDto
                .builder()
                .id(LONG_ID)
                .name(NAME)
                .email(EMAIL)
                .gender(GENDER)
                .status(STATUS)
                .posts(createPostsDto())
                .build();
    }

    private List<PostDto> createPostsDto() {
        return List.of(PostDto
                .builder()
                .body(BODY)
                .title(TITLE)
                .build());
    }

    private PostResponse createPostResponse() {
        return PostResponse
                .builder()
                .userId(LONG_ID)
                .userName(NAME)
                .title(TITLE)
                .body(BODY)
                .build();
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(LONG_ID);
        userEntity.setName(NAME);
        userEntity.setEmail(EMAIL);
        userEntity.setStatus(STATUS);
        userEntity.setGender(GENDER);
        userEntity.setPosts(createPostsEntity());
        return userEntity;
    }

    private List<PostEntity> createPostsEntity() {
        return List.of(createPostEntity());
    }

    private PostEntity createPostEntity() {
        PostEntity postEntity = new PostEntity();
        postEntity.setId(LONG_ID);
        postEntity.setData(DATA);
        return postEntity;
    }
}
