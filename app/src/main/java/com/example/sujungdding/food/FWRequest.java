package com.example.sujungdding.food;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FWRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://192.168.219.102/foodwrite.php";
    private Map<String, String> map;

    public FWRequest(String userId,String title, String content, String minppl, String date,String pclass, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userId", userId);
        map.put("title", title);
        map.put("content", content);
        map.put("minppl",minppl);
        map.put("date",date);
        map.put("class",pclass);

    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
