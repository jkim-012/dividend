package com.example.dividend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapedResult {

    private Company company;

    //배당금의 정보, 한 회사는 여러개의 배당금 정보를 가지고 있음
    private List<Dividend> dividendEntities;

    public ScrapedResult(){
        this.dividendEntities = new ArrayList<>();
    }
}
