package com.example.itDa.domain.article.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;


@Service
public class MapService {
    private static String GEOCODE_URL = "http://dapi.kakao.com/v2/local/search/address.json?query=";
    private static String GEOCODE_USER_INFO = "KakaoAK Rest api key키";

    public void mapSearch() {

        //URL 인스턴스 생성
        URL obj;

        try {
            // 인코딩한 String을 보내줘야 원하는 데이터를 받을 수 있다.
            String address = URLEncoder.encode("대구광역시 중구 동성로2가 동성로2길 81", "UTF-8");
            // URL 객채 생성 부분
            obj = new URL(GEOCODE_URL + address);

            // URL 을 통해 서버 통신하는 JAVA 프로그램을 개발하기 위해 사용하며 URLConnection객체를 받는 부분
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //get 으로 받아오면 된다. 자세한 사항은 카카오개발자센터에 나와있다.
            con.setRequestMethod("GET"); //요청 메소드
            con.setRequestProperty("Authorization", GEOCODE_USER_INFO); // Key-value 쌍으 지정된 일반 요청 속성 설정
            con.setRequestProperty("content-type", "application/json");
            con.setDoOutput(true); //URL 커넥션 서버 데이터를 보내는데 사용할수 있는지 여부 (기본은 false)
            con.setUseCaches(false); // 연결이 캐시를 사용하는지 ( 기본은 true)
            con.setDefaultUseCaches(false); // URL 커넥션 기본적으로 캐시를 사용하는지에 대한 여부 ( 기본은 true)

        /*
        실제 내용을 읽으려면 연결에서 InputSeream의 read() 메서드를 사용해야하며,
        read는 데이터를 바이트의 배열로 읽는 로우-레빌 메서드이다.
        문자 데이터를 읽기 위해서는 InputStream을 InputStreamReader에서 보다 편하게 조작하기 위해 래핑하거나,
        데이터를 문자열로 읽기 위해 BufferedReader로 래핑
         */

            Charset charset = Charset.forName("UTF-8");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //response 객체를 출력해보자
            System.out.println(response.toString());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}