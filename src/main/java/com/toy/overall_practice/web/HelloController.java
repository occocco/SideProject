package com.toy.overall_practice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    @GetMapping("/")
    public String indexForm() {
        return "index";
    }
    @GetMapping("/api/hello")
    @ResponseBody
    public ResponseEntity<String> apiHello() {
        return ResponseEntity.ok().body("hello");
    }
}
