package com.danielvargas.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

public class Tiempo {

    @JsonSerialize
    private LocalDateTime localDateTime;

    public Tiempo() {
        localDateTime = LocalDateTime.now();
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
