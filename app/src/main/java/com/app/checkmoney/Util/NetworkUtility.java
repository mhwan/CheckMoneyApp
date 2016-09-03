package com.app.checkmoney.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mhwan on 2016. 9. 1..
 */
public class NetworkUtility {
    private static NetworkUtility instance;
    public static final int TIMEOUT = 1000;

    private NetworkUtility() {
    }

    public static NetworkUtility getInstance() {
        if (instance == null)
            instance = new NetworkUtility();

        return instance;
    }

    public BasicStatus loginForApp(String email, String pw) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", email);
        params.put("userPassWord", pw);

        HttpURLConnection httpURLConnection = getHttpURLConnection(UrlList.LOGIN_URL);
        byte[] postdata = makeParamsToBytes(params);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(postdata.length));

        httpURLConnection.getOutputStream().write(postdata);
        httpURLConnection.getOutputStream().flush();

        if (checkResultData(httpURLConnection.getResponseCode())) {
            String result = getResultCode(httpURLConnection.getInputStream());
            if (result.equals("OK"))
                return BasicStatus.OK;
        }

        return BasicStatus.FAIL;
    }
    public RegisterStatus registerForApp(String email, long kakaoId, String name, String pw, String phnum) throws IOException, JSONException {
        /*
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", email);
        params.put("kakaoId", kakaoId);
        params.put("userName", name);
        params.put("userPassWord", pw);
        params.put("userPhoneNumber", phnum);*/
        JSONObject requestObject = new JSONObject();
        requestObject.put("userId", email);
        requestObject.put("kakaoId", kakaoId);
        requestObject.put("userName", name);
        requestObject.put("userPassWord", pw);
        requestObject.put("userPhoneNumber", phnum);

        HttpURLConnection httpURLConnection = getHttpURLConnection(UrlList.REGISTER_URL);
        //byte[] postData = makeParamsToBytes(params);
        httpURLConnection.setRequestMethod("POST");
        //httpURLConnection.setConnectTimeout(TIMEOUT);
        //httpURLConnection.setReadTimeout(TIMEOUT);
        httpURLConnection.setDoOutput(true); //outputstream으로 post 데이터를 넘겨주겠다.
        httpURLConnection.setDoInput(true); //inputstream으로 서버로 부터 응답을 받는다.
        //httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        //httpURLConnection.setRequestProperty("Content-Length", String.valueOf(postData.length));

        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.getOutputStream().write(requestObject.toString().getBytes());
        httpURLConnection.getOutputStream().flush();

        if (checkResultData(httpURLConnection.getResponseCode())){
            String result = getResultCode(httpURLConnection.getInputStream());
            DevelopeLog.d("result :: "+result);
            if (result.equals("EXIST"))
                return RegisterStatus.EXIST;
            else if (result.equals("OK"))
                return RegisterStatus.OK;
            else
                return RegisterStatus.FAIL;
        } else {
            DevelopeLog.i("RESPONSE NOT OK");
        }

        return RegisterStatus.FAIL;
    }


    public boolean checkResultData(int resoponse_code) {
        return resoponse_code == HttpURLConnection.HTTP_OK;
    }

    private String getResultCode(InputStream inputStream) throws IOException {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(inputStream);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        } catch (Exception e) {
            DevelopeLog.e("Error reading InputStream");
            result = null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    DevelopeLog.e("Error closing InputStream");
                }
            }
        }

        return result;
    }

    public HttpURLConnection getHttpURLConnection(String string_url) throws IOException {
        URL url = new URL(string_url);
        return (HttpURLConnection) url.openConnection();
    }


    public byte[] makeParamsToBytes(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        return postData.toString().getBytes("UTF-8");
    }

    public enum RegisterStatus {OK, FAIL, EXIST}

    public enum BasicStatus {OK, FAIL}
}
