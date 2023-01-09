package com.rest.goRest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.goRest.rest.request.UserRequest;
import com.rest.goRest.rest.response.FileMetadata;
import com.rest.goRest.rest.response.PostResponse;
import com.rest.goRest.rest.response.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserIT {

    private static final String BODY = "Umerus conventus demitto.";
    private static final String TITLE = "Tibi termes contra utique cerno apto auditor sortitus tandem fuga dolorem atrox coniecto.";
    @Value("${authorization.token}")
    private String TOKEN;
    @Autowired
    ObjectMapper objectMapper;
    @LocalServerPort
    private String port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldCreateUser() {
        var randomChar = getRandomChars();
        var request = UserRequest
                .builder()
                .name(randomChar)
                .email(randomChar + "@email.com")
                .gender("male")
                .status("active")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", TOKEN);
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<UserResponse> response = restTemplate.postForEntity(createURLWithPort("/v1/users "), requestEntity, UserResponse.class);

        UserResponse userResponse = objectMapper.convertValue(response.getBody(), new TypeReference<>() {
        });


        Assertions.assertEquals(randomChar, userResponse.getName());
        Assertions.assertEquals(randomChar + "@email.com", userResponse.getEmail());
        Assertions.assertEquals("male", userResponse.getGender());
        Assertions.assertEquals("active", userResponse.getStatus());

    }


    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetUsers() throws JsonProcessingException {

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users"),
                HttpMethod.GET,
                null,
                String.class
        );

        Page<UserResponse> page = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

        var userResponse = page.get().toList();

        Assertions.assertEquals(4, page.getTotalElements());
        Assertions.assertEquals("Sweta Asan", userResponse.get(0).getName());
        Assertions.assertEquals("female", userResponse.get(0).getGender());
        Assertions.assertEquals("asan_sweta@stanton-okuneva.com", userResponse.get(0).getEmail());
        Assertions.assertEquals("active", userResponse.get(0).getStatus());

    }

    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetUsersWithStatusActive() throws JsonProcessingException {

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users?active=true"),
                HttpMethod.GET,
                null,
                String.class
        );

        Page<UserResponse> page = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

        var userResponse = page.get().toList();

        Assertions.assertEquals(1, page.getTotalElements());
        Assertions.assertEquals("Sweta Asan", userResponse.get(0).getName());
        Assertions.assertEquals("female", userResponse.get(0).getGender());
        Assertions.assertEquals("asan_sweta@stanton-okuneva.com", userResponse.get(0).getEmail());
        Assertions.assertEquals("active", userResponse.get(0).getStatus());

    }


    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldUpdateUser() throws JsonProcessingException {

        Map<String, String> params = new HashMap<>();
        params.put("userId", "33092");
        var request = UserRequest
                .builder()
                .name("Testing Name234")
                .email("TestingName234@test.com")
                .gender("male")
                .status("inactive")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", TOKEN);
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users/{userId}"),
                HttpMethod.PUT,
                requestEntity,
                String.class,
                params
        );

        UserResponse userResponse = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });


        Assertions.assertEquals("Testing Name234", userResponse.getName());
        Assertions.assertEquals("TestingName234@test.com", userResponse.getEmail());
        Assertions.assertEquals("male", userResponse.getGender());
        Assertions.assertEquals("inactive", userResponse.getStatus());

    }


    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldDeleteUser() {

        Map<String, String> params = new HashMap<>();
        params.put("userId", "4255");
        var request = UserRequest
                .builder()
                .name("Amb. Aditeya Deshpande")
                .email("amb_aditeya_deshpande@dicki-kub.info")
                .gender("female")
                .status("inactive")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", TOKEN);
        HttpEntity<UserRequest> requestEntity = new HttpEntity<>(request, headers);

        restTemplate.exchange(
                createURLWithPort("/v1/users/{userId}"),
                HttpMethod.DELETE,
                requestEntity,
                String.class,
                params
        );
    }

    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldAddFile() throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, String> params = new HashMap<>();
        params.put("userId", "4255");

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new ClassPathResource("files/file.png"));

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users/{userId}/files"),
                HttpMethod.POST,
                new HttpEntity<>(map, headers),
                String.class,
                params
        );
        List<FileMetadata> fileObject = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(1, fileObject.size());
        Assertions.assertEquals("file.png", fileObject.get(0).getName());
        Assertions.assertEquals("image/png", fileObject.get(0).getType());
        Assertions.assertEquals(1299, fileObject.get(0).getSize());
    }


    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetFile() throws JsonProcessingException {

        Map<String, String> params = new HashMap<>();
        params.put("userId", "1");
        params.put("fileId", "1");
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users/{userId}/files/{fileId}"),
                HttpMethod.GET,
                null,
                String.class,
                params
        );

        FileMetadata fileObject = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(1, fileObject.getId());
        Assertions.assertEquals("icon", fileObject.getName());
        Assertions.assertEquals("png", fileObject.getType());
        Assertions.assertEquals(100, fileObject.getSize());

    }

    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetFiles() throws JsonProcessingException {

        Map<String, String> params = new HashMap<>();
        params.put("userId", "1");
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users/{userId}/files"),
                HttpMethod.GET,
                null,
                String.class,
                params
        );

        List<FileMetadata> fileObject = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(1, fileObject.get(0).getId());
        Assertions.assertEquals("icon", fileObject.get(0).getName());
        Assertions.assertEquals("png", fileObject.get(0).getType());
        Assertions.assertEquals(100, fileObject.get(0).getSize());

    }


    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetPostsByName() throws JsonProcessingException {

        Map<String, String> params = new HashMap<>();
        params.put("name", "Sweta Asan");
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users/posts"),
                HttpMethod.GET,
                null,
                String.class,
                params
        );

        List<PostResponse> fileObject = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(1, fileObject.get(0).getId());
        Assertions.assertEquals(1, fileObject.get(0).getUserId());
        Assertions.assertEquals("Sweta Asan", fileObject.get(0).getUserName());
        Assertions.assertEquals(TITLE, fileObject.get(0).getTitle());
        Assertions.assertEquals(BODY, fileObject.get(0).getBody());

    }

    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetPostsByTitle() throws JsonProcessingException {

        Map<String, String> params = new HashMap<>();
        params.put("title", "Tibi");
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users/posts"),
                HttpMethod.GET,
                null,
                String.class,
                params
        );

        List<PostResponse> fileObject = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(1, fileObject.get(0).getId());
        Assertions.assertEquals(1, fileObject.get(0).getUserId());
        Assertions.assertEquals("Sweta Asan", fileObject.get(0).getUserName());
        Assertions.assertEquals(TITLE, fileObject.get(0).getTitle());
        Assertions.assertEquals(BODY, fileObject.get(0).getBody());

    }


    @Test
    @Sql(scripts = "/db/insert-initial-entities.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/db/delete-all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void shouldGetPostsByBody() throws JsonProcessingException {

        Map<String, String> params = new HashMap<>();
        params.put("body", "Umerus");
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/v1/users/posts"),
                HttpMethod.GET,
                null,
                String.class,
                params
        );

        List<PostResponse> fileObject = objectMapper.readValue(response.getBody(), new TypeReference<>() {
        });

        Assertions.assertEquals(1, fileObject.get(0).getId());
        Assertions.assertEquals(1, fileObject.get(0).getUserId());
        Assertions.assertEquals("Sweta Asan", fileObject.get(0).getUserName());
        Assertions.assertEquals(TITLE, fileObject.get(0).getTitle());
        Assertions.assertEquals(BODY, fileObject.get(0).getBody());

    }

    private static String getRandomChars() {
        StringBuilder randomStr = new StringBuilder();
        String possibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 5; i++)
            randomStr.append(possibleChars.charAt((int) Math.floor(Math.random() * possibleChars.length())));
        return randomStr.toString();
    }

    protected String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
