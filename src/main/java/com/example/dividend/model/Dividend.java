package com.example.dividend.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class Dividend {

    private LocalDateTime localDateTime;
    private String dividend;
}