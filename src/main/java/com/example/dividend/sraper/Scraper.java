package com.example.dividend.sraper;

import com.example.dividend.model.Company;
import com.example.dividend.model.Dividend;
import com.example.dividend.model.ScrapedResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scraper {

    private static final String STATISTIC_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";

    //dividendEntities 가져오는 method
    public ScrapedResult scrap(Company company){

        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try{
            long start = 0;
            long end = 0;

            //URL format
            String url = String.format(STATISTIC_URL, company.getTicker(), start, end);

            //Jsoup - xml, html parsing library
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            //parsing 된 elements
            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");

            Element tableElement = parsingDivs.get(0);

            Element tbody = tableElement.children().get(1);

            List<Dividend> dividendList = new ArrayList<>();
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" "); //공백으로 문자 나누기
                String month = splits[0]; //월
                int date = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]); //년도
                String dividend = splits[3]; //배당금

            }

            scrapResult.setDividendEntities(dividendList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scrapResult;
    }

}
