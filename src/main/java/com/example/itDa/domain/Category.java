package com.example.itDa.domain;

public enum Category {
    PC("데스크탑/노트북"), ACC("PC 주변기기/모니터/마우스"), PARTS("PC 주요부품/저장장치"), ETC("제품코드,도서,기타");

    private String title;
    Category(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

}
