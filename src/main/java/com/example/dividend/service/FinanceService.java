package com.example.dividend.service;

import com.example.dividend.model.Company;
import com.example.dividend.model.Dividend;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.persist.entity.CompanyEntity;
import com.example.dividend.persist.entity.DividendEntity;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {

    private final DividendRepository dividendRepository;
    private final CompanyRepository companyRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {

        //1. 입력값 (회사 이름) 으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회사입니다."));

        //2. 조회된 회사 ID 로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        //3. 결과 조합 후 반환

        List<Dividend> dividendList = dividendEntities.stream()
                .map(e -> Dividend.builder()
                        .localDateTime(e.getDate())
                        .dividend(e.getDividend())
                        .build())
                .collect(Collectors.toList());


        return new ScrapedResult(Company.builder()
                .ticker(company.getTicker())
                .name(company.getName())
                .build(), dividendList);

    }
}
