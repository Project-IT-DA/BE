package com.example.itDa.domain.model.article;

public enum Status {
    SELL("거래가능"),
    SOLD_OUT("거래완료");

    private String title;
    Status(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}
