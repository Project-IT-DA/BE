package com.example.itDa.domain.model;

public enum UserSocialEnum {
    KAKAO(Social.KAKAO),
    GOOGLE(Social.GOOGLE)
    ;

    private final String social;
    UserSocialEnum(String social){this.social=social;}
    public static class Social{
        public static final String KAKAO = "SOCIAL_KAKAO";
        public static final String GOOGLE="SOCIAL_GOOGLE";

    }
}
