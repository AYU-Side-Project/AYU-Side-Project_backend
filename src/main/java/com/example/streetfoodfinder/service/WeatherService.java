package com.example.streetfoodfinder.service;

import com.example.streetfoodfinder.domain.entity.Review;
import com.example.streetfoodfinder.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WeatherService {
    private final ReviewRepository reviewRepository;

    @Value("${weather.url}")
    private String apiUrl;
    @Value("${weather.key}")
    private String serviceKey;

    //날씨를 받아오는 함수
    public String lookUpWeather(Integer x, Integer y) throws IOException, ParseException {//위도 경도 받아옴
        LocalDateTime now = LocalDateTime.now();
        int minute = now.getMinute();
        LocalDateTime rounded;

        if (minute < 30) {
            rounded = now.minusHours(1).withMinute(30).withSecond(0).withNano(0);
        } else {
            rounded = now.withMinute(30).withSecond(0).withNano(0);
        }//매시 30분에 날씨 업데이트 됨 -> 가장 최근 30분 된 시점으로 설정

        String nowDate = rounded.format(DateTimeFormatter.ofPattern("yyyyMMdd")); //조회 날짜
        String nowTime = rounded.format(DateTimeFormatter.ofPattern("HHmm")); //조회 시간
        String urlBuilder = apiUrl +
                "?" + URLEncoder.encode("ServiceKey", StandardCharsets.UTF_8) + "=" + serviceKey +
                "&" + URLEncoder.encode("dataType", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("JSON", StandardCharsets.UTF_8) +    /* 타입 (JSON, XML)*/
                "&" + URLEncoder.encode("pageNo", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1", StandardCharsets.UTF_8) +    /* 페이지 번호 */
                "&" + URLEncoder.encode("numOfRows", StandardCharsets.UTF_8) + "=" + URLEncoder.encode("1000", StandardCharsets.UTF_8) +    /* 페이지 당 열 개수 최대 1000*/
                "&" + URLEncoder.encode("base_date", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(nowDate, StandardCharsets.UTF_8) + /* 조회 날짜 */
                "&" + URLEncoder.encode("base_time", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(nowTime, StandardCharsets.UTF_8) + /* 조회 시간 */
                "&" + URLEncoder.encode("nx", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(x.toString(), StandardCharsets.UTF_8) + //경도
                "&" + URLEncoder.encode("ny", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(y.toString(), StandardCharsets.UTF_8); //위도

        URL url = new URL(urlBuilder);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        InputStreamReader isr;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            isr = new InputStreamReader(conn.getInputStream());
        } else {
            isr = new InputStreamReader(conn.getErrorStream());
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObj = (JSONObject) jsonParser.parse(isr);

        isr.close();
        conn.disconnect();

        String sky = "";
        String pty = "";
        JSONObject response = (JSONObject) jsonObj.get("response");
        JSONObject body = (JSONObject) response.get("body");
        if (body != null) {
            JSONObject items = (JSONObject) body.get("items");
            JSONArray jsonArray = (JSONArray) items.get("item");
            for (int i = 0; i < jsonArray.toArray().length; i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                String fcstValue = (String) item.get("fcstValue");
                String category = (String) item.get("category");

                if (category.equals("PTY")) {
                    switch (fcstValue) {
                        case "0":
                            pty = "없음";
                            break;
                        case "1": case "2": case "4": case "5": case "6":
                            pty = "비";
                            break;
                        case "3": case "7":
                            pty = "눈";
                            break;
                    }
                }// 강수형태(PTY) 코드 : (초단기) 없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7)
                if (category.equals("SKY")) {
                    switch (fcstValue) {
                        case "1":
                            sky = "맑음";
                            break;
                        case "3": case "4":
                            sky = "흐림";
                            break;
                    }
                }//하늘상태(SKY) 코드 : 맑음(1), 구름많음(3), 흐림(4)
            }
        } else {
            JSONObject header = (JSONObject) response.get("header");
            return (String) header.get("resultMsg");
        }

        if (pty.equals("없음")) {
            return sky;
        } else {
            return pty;
        }
        //비, 눈, 맑음, 흐림 중 하나 return
    }

    //리뷰에서 날씨로 찾기
    public List<Review> weatherLikeToday(Integer x, Integer y) throws IOException, ParseException {
        return reviewRepository.findByWeather(lookUpWeather(x, y));
    }

}
