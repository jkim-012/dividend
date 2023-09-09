package com.example.dividend.controller;

import com.example.dividend.model.Company;
import com.example.dividend.persist.entity.CompanyEntity;
import com.example.dividend.service.CompanyService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;


    // 배당금 검색시 자동 완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String keyword) {
        List<String> result = this.companyService.getCompanyNamesByKeywords(keyword);
        return ResponseEntity.ok(result);
    }


    /**
     * 회사 검색 - 저장된 회사 리스트 (CompanyEntity) 가져오기
     * @return
     */
    @GetMapping
    public ResponseEntity<?> searchCompany(final Pageable pageable){
        Page<CompanyEntity> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }


    /**
     * 관리자 기능 - 회사 추가하기
     * @param request
     * @return
     */
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
        //keyword 에 회사명 추가 - 회사를 저장할때마다 trie 에 저장이 됨
        this.companyService.addAutoCompleteKeyword(company.getName());

        return ResponseEntity.ok(company);
    }

    //관리자 기능 - 회사 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany(){
        return null;
    }

}
