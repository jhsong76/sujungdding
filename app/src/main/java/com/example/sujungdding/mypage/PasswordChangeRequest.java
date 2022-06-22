package com.example.sujungdding.mypage;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PasswordChangeRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://192.168.219.102/passwdCh.php";
    private Map<String, String> map;

    public PasswordChangeRequest(String userId, String userPwd, String userPwdCh, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userId", userId);
        map.put("userPwd", userPwd);
        map.put("userPwdCh", userPwdCh);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
