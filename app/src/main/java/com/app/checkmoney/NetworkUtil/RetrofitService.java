package com.app.checkmoney.NetworkUtil;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Mhwan on 2016. 9. 11..
 */
public class RetrofitService {
    public interface RegisterService{
        @POST(UrlList.REGISTER_URL)
        Call<okhttp3.ResponseBody> registerForApp(@Body HashMap<String, Object> map);
    }

    public interface LoginService{
        @POST(UrlList.LOGIN_URL)
        Call<ResponseBody> loginForApp(@Body HashMap<String, Object> map);
    }


    public interface RegisterResponse {
        String SUCCESS = "OK";
        String FAIL = "FAIL";
        String EXIST = "EXIST";
    }
    public interface GeneralResponse{
        String SUCCESS = "OK";
        String FAIL = "FAIL";
    }
}
