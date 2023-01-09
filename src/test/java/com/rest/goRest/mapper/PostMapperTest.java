package com.rest.goRest.mapper;

import com.rest.goRest.dao.entity.PostEntity;
import com.rest.goRest.dao.entity.UserEntity;
import com.rest.goRest.rest.dto.PostDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PostMapperTest {

    private static final Long LONG_ID = 1L;
    private static final String NAME = "Name";
    private static final String BODY = "body text";
    private static final String TITLE = "title text";
    private static final String EMAIL = "test@email.com";
    private static final String GENDER = "Male";
    private static final String STATUS = "active";


    @InjectMocks
    private PostMapperImpl mapper;

    @Test
    void shouldMapEntityToResponse() {
        var actual = mapper.map(createPostEntity());
        assertAll(
                () -> assertEquals(LONG_ID, actual.getId()),
                () -> assertEquals(NAME, actual.getUserName()),
                () -> assertEquals(BODY, actual.getBody()),
                () -> assertEquals(TITLE, actual.getTitle())

        );
    }

    @Test
    void shouldMapDtoToEntity() {
        Map<String, String> map = new HashMap<>();
        map.put("title", TITLE);
        map.put("body", BODY);
        var actual = mapper.map(createPostDto());
        assertAll(
                () -> assertEquals(map, actual.getData())
        );
    }


    private PostDto createPostDto() {
        return PostDto
                .builder()
                .body(BODY)
                .title(TITLE)
                .build();
    }

    private PostEntity createPostEntity() {
        PostEntity postEntity = new PostEntity();
        postEntity.setId(LONG_ID);
        postEntity.setUser(createUserEntity());
        postEntity.setData(createPostDto());
        return postEntity;
    }

    private UserEntity createUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(LONG_ID);
        userEntity.setName(NAME);
        userEntity.setEmail(EMAIL);
        userEntity.setStatus(STATUS);
        userEntity.setGender(GENDER);
        return userEntity;
    }
}
