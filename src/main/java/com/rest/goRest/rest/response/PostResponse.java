package com.rest.goRest.rest.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String title;
    private String body;
}
