package com.example.dividend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    // 회사 이름으로 배당금 검색
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName){
        return null;
    }


}
