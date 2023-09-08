package com.example.dividend.controller;

import com.example.dividend.model.Company;
import com.example.dividend.service.CompanyService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

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
    public ResponseEntity<?> addCompany(@RequestBody Company request){
        //입력값 Company object 로 ticker 구하기
        String ticker = request.getTicker().trim();

        //ticker 가 없는 경우 exception
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("ticker is empty");
        }
        // ticker 가 있으면 save method 호출해서 저장해주기
        Company company = this.companyService.save(ticker);

        return ResponseEntity.ok(company);
    }

    //관리자 기능 - 회사 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }

}
