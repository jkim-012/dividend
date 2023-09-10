package com.example.dividend.sraper;

import com.example.dividend.model.Company;
import com.example.dividend.model.Dividend;
import com.example.dividend.model.ScrapedResult;
import com.example.dividend.model.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper{

    private static final String STATISTIC_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400;

    //dividendEntities 가져오는 method
    @Override
    public ScrapedResult scrap(Company company) {

        var scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try {

            long now = System.currentTimeMillis() / 1000;

            //URL format
            String url = String.format(STATISTIC_URL, company.getTicker(), START_TIME, now);

            //Jsoup - xml, html parsing library
            Connection connection = Jsoup.connect(url);

            //web content 가 parse 되어 document 에 넣어짐
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
                int month = Month.strToNum(splits[0]); //월 , string 이라서 number 로 변환후 저장

                // 받아온 숫자 값이 -1 인 경우 exception 발생
                if (month < 0) {
                    throw new RuntimeException("unexpected value -> " + splits[0]);
                }

                int date = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]); //년도
                String dividend = splits[3]; //배당금


                // builder pattern 으로 dividend object 만들고 list 에 넣기
                dividendList.add(new Dividend(LocalDateTime.of(year, month, date, 0, 0), dividend));
            }

            scrapResult.setDividendEntities(dividendList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scrapResult;
    }

    @Override
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();
            Element titleElement = document.getElementsByTag("h1").get(0);
            String title = titleElement.text().split(" - ")[1].trim();

            return new Company(ticker, title);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
