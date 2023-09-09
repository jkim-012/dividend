package com.example.dividend.controller;

import com.example.dividend.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/finance")
public class FinanceController {

    private FinanceService financeService;

    /**
     * 배당금 정보 검색 - 저장된 배당금 리스트 (DividendEntity) 가져오기
     * @param companyName
     * @return
     */
    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable String companyName){
        var result = this.financeService.getDividendByCompanyName(companyName);
        return ResponseEntity.ok(result);
    }


}
