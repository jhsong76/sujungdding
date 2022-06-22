package com.example.sujungdding.food;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.sujungdding.R;
import com.example.sujungdding.mypage.KeywdDeleteRequest;
import com.example.sujungdding.mypage.Setting;

import org.json.JSONException;
import org.json.JSONObject;

public class CommentView extends LinearLayout {
    public TextView cmt_writer, cmt_content, cmt_secret, cmt_date, cmt_del;
    public CommentView(Context context){
        super(context);
        init(context);
    }
    public CommentView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment,this, true);

        cmt_writer = findViewById(R.id.cmt_writer);
        cmt_content = findViewById(R.id.cmt_content);
        cmt_date = findViewById(R.id.cmt_date);
        cmt_secret = findViewById(R.id.cmt_secret);
        cmt_del = findViewById(R.id.cmt_del);
    }
    public void setCmt_writer(String cmt_writer) {
            this.cmt_writer.setText(cmt_writer);
    }
    public void setCmt_content(String cmt_content) { this.cmt_content.setText(cmt_content); }
    public void setCmt_date(String cmt_date) {
        this.cmt_date.setText(cmt_date);
    }
    public void setCmt_secret(String cmt_secret) {this.cmt_secret.setText(cmt_secret);}
    public void setCmt_del() {
        this.cmt_del.setClickable(true);
        this.cmt_del.setText("삭제");
    }
}
