package com.example.dividend.sraper;

import com.example.dividend.model.Company;
import com.example.dividend.model.ScrapedResult;

public interface Scraper {
    public ScrapedResult scrap(Company company);
    public Company scrapCompanyByTicker(String ticker);
}
