package com.github.v3rc3tti.timeguard.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Task {

    private Long id;
    @NotBlank
    private String description;
    @NotNull
    private Boolean done;
    private Long userId;
}
