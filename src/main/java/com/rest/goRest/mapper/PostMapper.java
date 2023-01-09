package com.rest.goRest.mapper;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.goRest.dao.entity.PostEntity;
import com.rest.goRest.rest.dto.PostDto;
import com.rest.goRest.rest.response.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface PostMapper {
    ObjectMapper objectMapper = new ObjectMapper();


    @Mapping(target = "data", expression = "java(objectMapper.convertValue(postDto, Object.class))")
    PostEntity map(PostDto postDto);

    @Mapping(target = "userId", source = "postEntity.user.id")
    @Mapping(target = "userName", source = "postEntity.user.name")
    @Mapping(target = "title", source = "postEntity.data", qualifiedByName = "jsonbTitle")
    @Mapping(target = "body", source = "postEntity.data", qualifiedByName = "jsonbBody")
    PostResponse map(PostEntity postEntity);

    @Named("jsonbTitle")
    default String jsonbTitle(Object jsonbObject) {
        if (jsonbObject == null) {
            return null;
        }
        try {
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            String json = objectMapper.writeValueAsString(jsonbObject);
            return objectMapper.readValue(json, PostDto.class).getTitle();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Named("jsonbBody")
    default String jsonbBody(Object jsonbObject) {
        if (jsonbObject == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            String json = objectMapper.writeValueAsString(jsonbObject);
            return objectMapper.readValue(json, PostDto.class).getBody();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
