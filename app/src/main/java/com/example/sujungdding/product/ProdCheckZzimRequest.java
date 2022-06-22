package com.example.sujungdding.product;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProdCheckZzimRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://192.168.219.102/prodCheckIfZzimed.php";
    private Map<String, String> map;

    public ProdCheckZzimRequest(String userId, String ppostNo, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userId", userId);
        map.put("ppost_no", ppostNo);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}