package com.rest.goRest.rest;

import com.rest.goRest.rest.dto.PostDto;
import com.rest.goRest.rest.dto.UserDto;
import com.rest.goRest.rest.request.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(value = "goRest-api", url = "${services.url.goRest}")
public interface GoRestApi {

    String AUTH_TOKEN = "Authorization";

    @GetMapping("/users")
    List<UserDto> users();

    @GetMapping("/posts")
    List<PostDto> posts(@RequestParam("user_id") Long user_id);


    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserDto createUser(@RequestHeader(AUTH_TOKEN) String token,
                       @RequestBody UserRequest user);

    @PutMapping(value = "/users/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    UserDto updateUser(@RequestHeader(AUTH_TOKEN) String token,
                       @PathVariable("userId") Long userId,
                       @RequestBody UserRequest user);

    @DeleteMapping("/users/{userId}")
    void deleteUser(@PathVariable("userId") Long userId,
                    @RequestHeader(AUTH_TOKEN) String token);
}
