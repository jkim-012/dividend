package com.example.dividend;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DividendApplication {

    public static void main(String[] args) {
//        SpringApplication.run(DividendApplication.class, args);

        //Jsoup - xml, html parsing library
        Connection connection = Jsoup.connect("https://finance.yahoo.com/quote/COKE/history?period1=99100800&period2=1693872000&interval=1mo&filter=history&frequency=1mo&includeAdjustedClose=true");
        try {
            Document document = connection.get();

            // element's' 인 이유는 이 속성을 가진 element 가 하나 이상일 수 있어서
            Elements elements = document.getElementsByAttributeValue("data-test", "historical-prices");
            // 이 중 제일 처음 element 가져오기
            Element element = elements.get(0);

            Element tbody = element.children().get(1);
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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
