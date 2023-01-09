package com.rest.goRest.rest.response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Data
public class JobStatus {
    private Status status;
    private Instant startTime;
    private Instant endTime;

    public JobStatus(Status status, Instant startTime, Instant endTime) {
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public enum Status {
        IN_PROGRESS, FINISHED, ERROR
    }
}
