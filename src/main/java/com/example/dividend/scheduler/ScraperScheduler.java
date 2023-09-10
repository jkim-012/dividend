package com.example.dividend.scheduler;

import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.model.constants.CacheKey;
import com.example.dividend.persist.entity.CompanyEntity;
import com.example.dividend.persist.entity.DividendEntity;
import com.example.dividend.repository.CompanyRepository;
import com.example.dividend.repository.DividendRepository;
import com.example.dividend.sraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class ScraperScheduler {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;
    private final Scraper yahooFinanceScraper;

    @Scheduled(cron ="${scheduler.scrap.yahoo}")
    @CacheEvict(value = CacheKey.KEY_FINANCE, allEntries = true) //scheduling 이 실행될때마다 캐시 삭제
    public void yahooFinanceScheduling() {
        // 로깅
        log.info("scraping scheduler is started");

        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();

        // 회사마다 배당금 정보를 새로 스크래핑
        for (CompanyEntity company : companies) {
            ScrapedResult scrapedResult = this.yahooFinanceScraper.scrap(new Company(company.getTicker(),company.getName()));

            // 스크래핑 한 배당금 정보가 db 에 없으면 저장
            scrapedResult.getDividendEntities().stream()
                    // dividend 모델 -> dividend entity 로 mapping
                    .map(e -> new DividendEntity(company.getId(),e))
                    // element 하나씩 dividend repository 에 저장 (존재하지않는 경우만)
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if(!exists){
                            this.dividendRepository.save(e);
                        }
                    });

            //연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지 -> 일시적으로 멈추고 다시 실행됨
            try {
                Thread.sleep(3000); //3 sec
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }


        }

    }
}
