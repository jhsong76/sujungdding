package com.example.sujungdding.food;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FoodCommentInputRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://192.168.219.102/foodCmtInput.php";
    private Map<String, String> map;

    public FoodCommentInputRequest(String userId, String fpost_no, String cmt_secret, String cmt_content, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userId", userId);
        map.put("fpost_no", fpost_no);
        map.put("cmt_secret", cmt_secret);
        map.put("cmt_content", cmt_content);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }
}
