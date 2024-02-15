package com.dependent.guardian.service.function;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@Slf4j
public class NaverService {
    public String naverLoginAPI(HttpServletRequest request) throws UnsupportedEncodingException {
        String id = null;

        StringBuffer apiURL = getApiURL(request);
        //apiURL 받아옴
        String access_token;

        try {
            //accees_token 발급하는 과정
            access_token = getAccess_token(apiURL);
            //파싱한 값을 따로 저장

            //access_token을 이용해 사용자 정보를 받아 오는 과정
            if (access_token != null) { // access_token을 잘 받아왔다면

                id = getId(access_token);
                //access_token으로 id값을 받아오고 파싱까지
                log.info("네이버에서 {}의 회원인증", id);


            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return id;
    }

    private StringBuffer getApiURL(HttpServletRequest req) throws UnsupportedEncodingException {
        String clientId = "C8B0Vo2iQLoeoT_aaVZG";//애플리케이션 클라이언트 아이디값;
        String clientSecret = "_BoWT9I7_W";//애플리케이션 클라이언트 시크릿값;
        String code = req.getParameter("code");//access_token을 발급하기 전 유효한코드 받음
        String state = req.getParameter("state");//그냥 랜덤 문자열
        /* state가 프론트에서 줘야하던가..?
         * 어 맞아 카카오 code랑 똑같은듯 */
//        String redirectURI = URLEncoder.encode("https://guardian.paas-ta.org/index/naver", "UTF-8");
        String redirectURI = URLEncoder.encode("http://localhost:8080/index/naver", "UTF-8");

        StringBuffer apiURL = new StringBuffer();


        apiURL.append("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code");
        apiURL.append("&client_id=").append(clientId);
        apiURL.append("&client_secret=").append(clientSecret);
        apiURL.append("&redirect_uri=").append(redirectURI);
        apiURL.append("&code=").append(code);
        apiURL.append("&state=").append(state);

        return apiURL;
    }

    private JSONObject getJsonObj(StringBuffer res) throws ParseException {
        JSONParser parsing = new JSONParser();
        Object obj = parsing.parse(res.toString());

        return (JSONObject) obj;
    }

    private String getAccess_token(StringBuffer apiURL) throws IOException, ParseException {
        URL url = new URL(apiURL.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        BufferedReader br;
        if (responseCode == 200) { // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {  // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }
        String inputLine;
        StringBuffer res = new StringBuffer();
        //response 값 받아오는 과정, 실패해도 성공해도 값을 전달해 주기 때문에
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }
        br.close();

        //String to Json
        JSONObject jsonObj = this.getJsonObj(res);
        return (String) jsonObj.get("access_token");
    }

    private String getId(String access_token) throws IOException, ParseException {
        String inputLine;
        BufferedReader br;

        //사용자 정보를 전달해주는 주소
        String apiurl = "https://openapi.naver.com/v1/nid/me";
        URL url = new URL(apiurl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        String tmp = "Bearer " + access_token;
        con.setRequestProperty("Authorization", tmp);
        int responseCode = con.getResponseCode();

        //사용자 정보 호출 시 정상인지 판별

        if (responseCode == 200) { // 정상 호출
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {  // 에러 발생
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }


        //정상 호출일 경우 response값을 파싱함
        StringBuffer res = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            res.append(inputLine);
        }

        //String to Json
        JSONObject jsonObj = this.getJsonObj(res);

        //Json에서 response에 해당하는 값(우리가 필요한 값들) 가져옴
        JSONObject resObj = (JSONObject) jsonObj.get("response");

        br.close();
//bSNlN4eXkdneX9Aaf9tYzESGdWnS8vTpwSBF1YlsXJE access 토큰
        return (String) resObj.get("id");
    }


}
