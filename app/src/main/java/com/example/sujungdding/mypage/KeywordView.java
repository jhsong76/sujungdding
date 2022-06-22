package com.example.sujungdding.mypage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.sujungdding.R;

import org.json.JSONException;
import org.json.JSONObject;

public class KeywordView extends LinearLayout {
    TextView keyword;
    Button keywordDelete;
    public KeywordView(Context context) {
        super(context);
        init(context);
    }
    public KeywordView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.keyword,this, true);

        keyword = findViewById(R.id.keyword);
        keywordDelete = (Button)findViewById(R.id.keywdDelete);

//        /***************키워드 삭제*****************/
//
//        keywordDelete.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String keywd = keyword.getText().toString();
//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                          받아오는 값 확인
////                        String result = response;
////                        System.out.println(result);
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success");
//
//                            if (success) {//키워드 입력 성공시
//                                Toast.makeText(getContext(), "키워드를 삭제했습니다", Toast.LENGTH_SHORT).show();
//                                printKeyword();
//                                keyword.setText("");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                KeywdDeleteRequest keywdDeleteRequest = new KeywdDeleteRequest(user.getId(), keywd, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(Setting.this);
//                queue.add(keywdDeleteRequest);
//            }
//        });
//
    }
    public void setKeyword(String name){
        keyword.setText(name);
    }

}

