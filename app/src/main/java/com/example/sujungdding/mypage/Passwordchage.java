package com.example.sujungdding.mypage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.sujungdding.Home;
import com.example.sujungdding.R;
import com.example.sujungdding.User;
import com.example.sujungdding.login.Login;
import com.example.sujungdding.login.LoginRequest;
import com.example.sujungdding.signup.Signup;

import org.json.JSONException;
import org.json.JSONObject;

public class Passwordchage extends AppCompatActivity {
    private User user;
    private boolean change = false;
    private TextView original_pwd, change_pwd1, change_pwd2;
    private Button pwdchange_btn;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwordchage);

        //사용자 데이터 가져오기
        Intent preIntent = getIntent();
        user = (User) preIntent.getParcelableExtra("user");

        /****툴바****/
        Toolbar toolbar = findViewById(R.id.passwordchage_toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);

        /******************비밀번호 변경********************/
        original_pwd = findViewById( R.id.password1 );
        change_pwd1 = findViewById( R.id.password2 );
        change_pwd2 = findViewById( R.id.password3 );
        pwdchange_btn = (Button) findViewById(R.id.pwdchangebtn);
        pwdchange_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String originalPwd = original_pwd.getText().toString();
                String changePwd1 = change_pwd1.getText().toString();
                String changePwd2 = change_pwd2.getText().toString();

                if (change) {
                    return; //변경 확인 완료
                }
                else if (originalPwd.equals("")) { //빈 칸이면 입력하십시오 출력
                    AlertDialog.Builder builder = new AlertDialog.Builder(Passwordchage.this);
                    dialog = builder.setMessage("기존 비밀번호를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                } else if (!changePwd1.equals(changePwd2)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Passwordchage.this);
                    dialog = builder.setMessage("변경할 비밀번호가 일치하지 않습니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                } else if(changePwd1.length() > 15){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Passwordchage.this);
                    dialog = builder.setMessage("비밀번호가 너무 깁니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        받아오는 값 확인
                        String result = response;
                        System.out.println(result);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {//비밀번호 변경 성공시
                                Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Passwordchage.this, Mypage.class);
                                user.setPwd(changePwd2);
                                intent.putExtra("user", user);
                                finish();
                                startActivity(intent);
                            } else {//로그인 실패시
                                Toast.makeText(getApplicationContext(), "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                PasswordChangeRequest passchRequest = new PasswordChangeRequest(user.getId(), originalPwd, changePwd2, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Passwordchage.this);
                queue.add(passchRequest);
            }
        });
    }

    /******************툴바**********************/
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mypage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /******************뒤로가기********************/
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}