package com.toy.overall_practice.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseResult {

        private int code;
        private String message;

}
