package com.dependent.guardian.service.function;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URL;

@Service
@Slf4j
public class KakaoService {

    public String kakaoLoginAPI(HttpServletRequest request) throws IOException {
        String id = null;

        //네이버와 다르게 카카오는 HttpsURLConnection에 최종적인 주소가 완성됨
        HttpsURLConnection conn = getApiURL(request);

        try {
            String access_token = getAccess_token(conn);
            //access_token을 받아옴

            //access_token을 이용해 사용자 정보를 받아 오는 과정
            if (access_token != null) { // access_token을 잘 받아왔다면

                id = getId(access_token);

                log.info("카카오에서 {}의 회원인증", id);

            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return id;
    }

    private HttpsURLConnection getApiURL(HttpServletRequest req) throws IOException {
        String code = req.getParameter("code");

        String endpoint = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(endpoint);

        String bodyData = "grant_type=authorization_code&";
        bodyData += "client_id=2bea2843fd1d93e3251f612666d0617a&";
//        bodyData += "redirect_uri=https://guardian.paas-ta.org/index/kakao&";
        bodyData += "redirect_uri=http://localhost:8080/index/kakao&";
        bodyData += "code=" + code;

        //Stream 연결
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        //http header 값 넣기
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setDoOutput(true);


        //https(주소 바디)에 쓰는 과정dddㅇ
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
        bw.write(bodyData);
        bw.flush();

        return conn;

    }

    private JSONObject getJsonObj(StringBuilder res) throws ParseException {
        JSONParser parsing = new JSONParser();
        Object obj = parsing.parse(res.toString());

        return (JSONObject) obj;
    }

    private String getAccess_token(HttpsURLConnection conn) throws IOException, ParseException {
        BufferedReader br;
        int respenseCode = conn.getResponseCode();
        if (respenseCode == 200) {
            //정상 호출
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            //에러 발생
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        String input = "";
        StringBuilder res = new StringBuilder();

        //response 값 받아오는 과정, 실패해도 성공해도 값을 전달해 주기 때문에
        while ((input = br.readLine()) != null) {
            res.append(input);
        }
        br.close();

        //String to Json
        JSONObject jsonObj = this.getJsonObj(res);

        //access_token을 받아서 반환
        return (String) jsonObj.get("access_token");
    }

    private String getId(String access_token) throws ParseException, IOException {
        //사용자 정보를 전달해주는 주소
        String endpoint = "https://kapi.kakao.com/v2/user/me";
        URL url = new URL(endpoint);

        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

        //header 값 넣기, access_token을 이용해서 받아옴
        conn.setRequestProperty("Authorization", "Bearer " + access_token);
        conn.setDoOutput(true);

        //request 하기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder res = new StringBuilder();

        //response 값 받아오는 과정, 실패해도 성공해도 값을 전달해 주기 때문에
        String input;
        while ((input = br.readLine()) != null) {
            res.append(input);
        }
        br.close();


        //String to Json
        JSONObject jsonObj = this.getJsonObj(res);

        return String.valueOf(jsonObj.get("id"));
    }


}
