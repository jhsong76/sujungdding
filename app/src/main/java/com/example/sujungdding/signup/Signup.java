package com.example.sujungdding.signup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.sujungdding.R;
import com.example.sujungdding.SendMail;
import com.example.sujungdding.login.Login;

import org.json.JSONException;
import org.json.JSONObject;

public class Signup extends AppCompatActivity {
    //입력 값
    private EditText signup_id, signup_nickname, signup_pwd, signup_pwdcheck, signup_codeCheck;
    private Button signup_btn, signup_emailvf, signup_nicknamedb, signup_codeCheckBtn;
    private AlertDialog dialog;
    private boolean validate = false;
    private boolean validatenn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        //툴바
        Toolbar toolbar = findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);

        //입력 칸 가져오기
        signup_id = findViewById( R.id.signup_id);
        signup_nickname = findViewById( R.id.signup_nickname );
        signup_pwd = findViewById( R.id.signup_pwd );
        signup_pwdcheck = findViewById(R.id.signup_pwdcheck);

        // 아이디 코드 체크
        signup_codeCheck = findViewById(R.id.signup_codeCheck);
        signup_codeCheckBtn = (Button) findViewById(R.id.signup_codeCheckBtn);

        /***********아이디 중복 체크*************/
        signup_emailvf = findViewById(R.id.signup_emailvf);
        signup_emailvf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력 값 가져오기
                String userId = signup_id.getText().toString();
                if (validate) {
                    return; //중복 확인 완료
                }
                if (userId.equals("")) { //빈 칸이면 입력하십시오 출력
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    dialog = builder.setMessage("아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                } else if(userId.length() != 8){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    dialog = builder.setMessage("올바른 아이디를 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        받아오는 값 확인
                        String result = response;
                        System.out.println(result);
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {   //DB에 없는 새로운 아이디면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                dialog = builder.setTitle("이메일 인증").setMessage(userId + "@sungshin.ac.kr로 이메일을 전송합니다.").setPositiveButton("확인", null).create();
                                dialog.show();

                                /***** 코드만들어서 메일 전송****/
                                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitDiskReads().permitDiskWrites().permitNetwork().build());
                                SendMail mailServer = new SendMail();
                                String recvr = userId.concat("@sungshin.ac.kr");
                                System.out.println(recvr);
                                //인증코드
                                String[] str = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                                String newCode = new String();
                                for (int x = 0; x < 8; x++) {
                                    int random = (int) (Math.random() * str.length);
                                    newCode += str[random];
                                }
                                mailServer.sendSecurityCode(getApplicationContext(), newCode, recvr);
                                /***********이메일 인증 코드 확인**********/
                                String finalNewCode = newCode;
                                signup_codeCheckBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String inputCode = signup_codeCheck.getText().toString();
                                        System.out.println("코드: " + finalNewCode + "입력 값: " + inputCode);
                                        if(inputCode.equals(finalNewCode)){
                                            signup_id.setEnabled(false); //아이디값 고정
                                            validate = true; //검증 완료
                                            signup_emailvf.setBackgroundColor(getResources().getColor(R.color.white));
                                            signup_codeCheckBtn.setBackgroundColor(getResources().getColor(R.color.white));
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                            dialog = builder.setTitle("이메일 인증").setMessage("이메일 인증에 성공했습니다.").setNegativeButton("확인", null).create();
                                            dialog.show();
                                        }else{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                            dialog = builder.setTitle("이메일 인증").setMessage("이메일 인증에 실패했습니다. 인증 코드를 다시 입력하십시오").setNegativeButton("확인", null).create();
                                            dialog.show();
                                            return;
                                        }
                                    }
                                });
                            }
                            else {  //DB에 이미 있는 중복된 아이디면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                dialog = builder.setTitle("이메일 인증 실패").setMessage("이미 존재하는 아이디입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                SignupIdCkRequest idCkRequest = new SignupIdCkRequest(userId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Signup.this);
                queue.add(idCkRequest);
            }
        });


        /***********별명 중복 체크*************/
        signup_nicknamedb = findViewById(R.id.signup_nicknamedb);
        signup_nicknamedb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력 값 가져오기
                String userNname = signup_nickname.getText().toString();
                if (validatenn) {
                    return; //중복 확인 완료
                }
                if (userNname.equals("")) { //빈 칸이면 입력하십시오 출력
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    dialog = builder.setMessage("별명을 입력하세요.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                } else if(userNname.length() > 10){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    dialog = builder.setMessage("별명이 너무 깁니다.").setPositiveButton("확인", null).create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
//                        받아오는 값 확인
//                        String result = response;
//                        System.out.println(result);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {   //DB에 없는 새로운 아이디면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                dialog = builder.setTitle("중복 인증").setMessage("사용 가능한 별명입니다.").setPositiveButton("확인", null).create();
                                dialog.show();
                                signup_nickname.setEnabled(false); //아이디값 고정
                                validatenn = true; //검증 완료
                                signup_nicknamedb.setBackgroundColor(getResources().getColor(R.color.white));
                            }
                            else {  //DB에 이미 있는 중복된 아이디면
                                AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                dialog = builder.setTitle("중복 인증").setMessage("이미 존재하는 별명입니다.").setNegativeButton("확인", null).create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                SignupNnameCkRequest nnameCkRequest = new SignupNnameCkRequest(userNname, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Signup.this);
                queue.add(nnameCkRequest);
            }
        });

        /***************회원가입***************/
        signup_btn = findViewById(R.id.signup_signup);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //입력 값 가져오기
                String userId = signup_id.getText().toString();
                String userNname = signup_nickname.getText().toString();
                String userPwd = signup_pwd.getText().toString();
                String userPwdck = signup_pwdcheck.getText().toString();
                if (validate) {
                    if (!userPwd.equals(userPwdck)) { //비밀번호 두 개 일치 확인
                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                        dialog = builder.setTitle("회원가입 실패").setMessage("비밀번호가 일치하지 않습니다.").setPositiveButton("확인", null).create();
                        dialog.show();
                        return;
                    } else if (userPwd.length() > 15) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                        dialog = builder.setMessage("비밀번호가 너무 깁니다.").setPositiveButton("확인", null).create();
                        dialog.show();
                        return;
                    }
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
    //                        받아오는 값 확인
    //                        String result = response;
    //                        System.out.println(result);
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {   //회원가입 성공

                                    Toast.makeText(getApplicationContext(), "회원가입이 완료됐습니다. 로그인 해주세요.", Toast.LENGTH_LONG).show();
                                    //로그인 페이지로 이동
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {  //회원가입 실패
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                    dialog = builder.setTitle("회원가입 실패").setMessage("회원가입에 실패했습니다. 이미 가입했는지 확인해 주십시오.").setNegativeButton("확인", null).create();
                                    dialog.show();
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    SignupRequest signupRequest = new SignupRequest(userId, userNname, userPwd, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Signup.this);
                    queue.add(signupRequest);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    dialog = builder.setTitle("이메일 인증").setMessage("이메일 인증을 해주십시오").setNegativeButton("확인", null).create();
                    dialog.show();
                    return;
                }
            }
        });
    }

    /***************툴바****************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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