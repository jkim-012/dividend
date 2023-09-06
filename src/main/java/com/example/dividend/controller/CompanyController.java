package com.example.dividend.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    // 배당금 검색시 자동 완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword) {
        return null;
    }

    //회사 검색
    @GetMapping
    public ResponseEntity<?> searchCompany(){
        return null;
    }

    //관리자 기능 - 회사 추가
    @PostMapping
    public ResponseEntity<?> addCompany(){
        return null;
    }

    //관리자 기능 - 회사 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }

}
