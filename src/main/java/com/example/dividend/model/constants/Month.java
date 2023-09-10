package com.example.dividend.model.constants;

public enum Month {

    JAN("Jan", 1),
    FEB("Feb", 2),
    MAR("Mar", 3),
    APR("Apr", 4),
    MAY("May", 5),
    JUN("Jun", 6),
    JUL("Jul", 7),
    AUG("Aug", 8),
    SEP("Sep", 9),
    OCT("Oct", 10),
    NOV("Nov", 11),
    DEC("Dec", 12);

    private String s;
    private int n;

    Month(String s, int n) {
        this.s = s;
        this.n = n;
    }

    // string 을 입력하면 int 로 반환하는 method
    public static int strToNum(String string){
        for(var v : Month.values()){
            if(v.s.equals(string)){
                return v.n; //number 를 반환
            }
        }return -1; // 못찾으면 -1 반환
    }
}
