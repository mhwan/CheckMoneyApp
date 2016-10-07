package com.app.checkmoney.NetworkUtil;

import com.app.checkmoney.Items.RoomUserItem;
import com.app.checkmoney.Items.UserItem;
import com.app.checkmoney.Util.DevelopeLog;

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
import java.util.ArrayList;
import java.util.Map;

/**
 * 미사용
 *
 * HttpUrlConnection 네트워킹 클래스
 * Created by Mhwan on 2016. 9. 1..
 */
public class NetworkUtility {
    private static NetworkUtility instance;
    public static final int TIMEOUT = 10000;

    private NetworkUtility() {
    }

    public static NetworkUtility getInstance() {
        if (instance == null)
            instance = new NetworkUtility();

        return instance;
    }

    public BasicStatus loginForApp(String email, String pw) throws IOException, JSONException{
        JSONObject params = new JSONObject();
        params.put("userEmail", email);
        params.put("userPassWord", pw);

        HttpURLConnection httpURLConnection = getHttpURLConnection(UrlList.LOGIN_URL);
        httpURLConnection.setRequestMethod("POST");
        //httpURLConnection.setConnectTimeout(TIMEOUT);
        //httpURLConnection.setReadTimeout(TIMEOUT);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");

        httpURLConnection.getOutputStream().write(params.toString().getBytes());
        httpURLConnection.getOutputStream().flush();

        if (checkResultData(httpURLConnection.getResponseCode())) {
            String result = getResultCode(httpURLConnection.getInputStream());
            DevelopeLog.d("Result !! "+result);
            JSONObject resultObject = new JSONObject(result);
            if (resultObject.getString("resultCode").equals("OK"))
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
        requestObject.put("userEmail", email);
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

    public BasicStatus writeRoomData(String title, String money, String date, String roomId){
        return BasicStatus.OK;
    }
    /**
     * 임시적으로 선택된 아이템에 연락처와 이름을 받아와 강제로 미가입 상태로 만들어서 리턴
     * @param roomId
     * @param userlist
     * @return
     */
    public ArrayList<RoomUserItem> inviteRoom(String roomId, ArrayList<UserItem> userlist){
        ArrayList<RoomUserItem> result_list = null;

        if (userlist != null){
            result_list = new ArrayList<>();
            for (UserItem item : userlist) {
                RoomUserItem result_item = new RoomUserItem(item.getName(), item.getPhoneNumber(), RoomUserItem.ManageType.NO_MANAGER, RoomUserItem.ExchangeType.GIVER);
                result_item.setRegister(false);
                result_list.add(result_item);
            }
        }

        return result_list;
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
