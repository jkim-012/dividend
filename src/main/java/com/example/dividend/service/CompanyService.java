package com.example.dividend.service;

import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.persist.entity.CompanyEntity;
import com.example.dividend.persist.entity.DividendEntity;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import com.example.dividend.sraper.Scraper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Scraper yahooFinanceScraper;
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    private final Trie trie;


    /**
     * scraping data 저장
     *
     * @param ticker
     * @return
     */
    public Company save(String ticker) {
        //먼저 ticker 가 db 에 있는지 확인 (T/F)
        boolean exist = this.companyRepository.existsByTicker(ticker);

        //이미 존재하면 exception
        if (exist) {
            throw new RuntimeException("ticker already exists");
        }
        //존재하지 않으면 아래 scraping 후 배당금과 회사 정보 store 하는 method 호출
        return this.storeCompanyAndDividend(ticker);
    }


    public Page<CompanyEntity> getAllCompany(Pageable pageable) {
        return this.companyRepository.findAll(pageable);
    }


    /**
     * 회사 찾아서 배당금 정보 scraping , 그 후 db 에 저장
     *
     * @param ticker
     * @return
     */

    private Company storeCompanyAndDividend(String ticker) {
        //ticker 를 기준으로 scarp
        Company company = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to scrap ticker -> " + ticker);
        }

        //회사가 존재할 경우, 회사의 배당금 정보를 scraping
        ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(company);

        //scarping 결과
        CompanyEntity companyEntity = this.companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult.getDividendEntities().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());

        //db 에 저장
        this.dividendRepository.saveAll(dividendEntities);

        return company;
    }

    public void addAutoCompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

    public List<String> autoComplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public void deleteAutoCompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    public List<String> getCompanyNamesByKeywords(String keyword) {
        //10 개로 limit
        Pageable limit = PageRequest.of(0,10);
        Page<CompanyEntity> companyEntities = this.companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
        return companyEntities.stream()
                .map(e -> e.getName())
                .collect(Collectors.toList());
    }


}
