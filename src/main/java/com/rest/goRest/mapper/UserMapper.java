package com.rest.goRest.mapper;

import com.rest.goRest.dao.entity.UserEntity;
import com.rest.goRest.rest.dto.UserDto;
import com.rest.goRest.rest.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {PostMapper.class})
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity map(UserDto userDto);


    UserResponse map(UserEntity userEntity);
}
