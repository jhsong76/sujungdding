package com.example.sujungdding.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.sujungdding.User;
import com.example.sujungdding.mypage.Mypage;
import com.example.sujungdding.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import java.util.List;

public class Food_Write extends AppCompatActivity {
    Button btnSelectDate;
    DatePickerDialog datePickerDialog;
    private EditText title, content, minppl, date;
    private Button finish;
    private Spinner pclass;

    //사용자 정보 가져오기
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_write);
        List<String> states = Arrays.asList("한식", "양식", "일식", "중식", "분식", "패스트푸드", "기타");

        //사용자 정보 가져오기
        Intent preIntent = getIntent();
        user = (User) preIntent.getParcelableExtra("user");

        title = findViewById(R.id.title);
        content= findViewById(R.id.content);
        minppl = findViewById(R.id.minppl);
        date = findViewById(R.id.date);
        pclass = findViewById(R.id.pclass);

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,states);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        pclass.setAdapter(adapter);
        finish = (Button)findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String post_title = title.getText().toString();
                String post_content = content.getText().toString();
                String post_minppl = minppl.getText().toString();
                String post_due = date.getText().toString();
                String post_class =pclass.getSelectedItem().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String result = response;
                        System.out.println(result);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) { // 등록에 성공한 경우
                                Toast.makeText(getApplicationContext(), "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else { // 등록에 실패한 경우
                                Toast.makeText(getApplicationContext(), "등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                FWRequest registerRequest = new FWRequest(user.getId(), post_title, post_content, post_minppl, post_due, post_class, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Food_Write.this);
                queue.add(registerRequest);
            }
        });


        /****툴바****/
        Toolbar toolbar = findViewById(R.id.board_toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.toolbar_mypage:
                intent = new Intent(this, Mypage.class);
                intent.putExtra("user", user);
                startActivity(intent);
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
