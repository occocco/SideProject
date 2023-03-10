package com.toy.overall_practice.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExResult {

    private String code;
    private String message;

}
