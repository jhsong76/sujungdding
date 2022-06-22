package com.example.sujungdding.mypage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.sujungdding.R;
import com.example.sujungdding.TransferDate;
import com.example.sujungdding.User;
import com.example.sujungdding.food.Food_Read;
import com.example.sujungdding.product.Product_Read;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class Setting extends AppCompatActivity {
    private User user;
    private ListView keywdList, zzimList;
    private EditText keyword;
    private Button keywordbtn, keywordDelete;
    KwdSingerAdapter kwdadapter;
    ZzimSingerAdapter zzimadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //사용자 데이터 가져오기
        Intent preIntent = getIntent();
        user = (User) preIntent.getParcelableExtra("user");
        /********키워드 리스트 뷰********/
        keyword = findViewById(R.id.keyword);
        keywordbtn = (Button) findViewById(R.id.keywdbutton);
        keywdList = (ListView) findViewById(R.id.keywordlist);
        /********찜 리스트 뷰*******/
        zzimList = (ListView) findViewById(R.id.zzimList);

        /****툴바****/
        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);

        //키워드 정보 가져오기
        printKeyword();
        //찜 가져오기
        printZzim();

        /*************키워드 입력**************/
        keywordbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keywordInput = keyword.getText().toString();
                if (keywordInput.equals("")){
                    keyword.setText("");
                    Toast.makeText(getApplicationContext(), "키워드를 입력하십시오.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (keywordInput.length() > 10){
                    keyword.setText("");
                    Toast.makeText(getApplicationContext(), "키워드가 너무 깁니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                      받아오는 값 확인
//                        String result = response;
//                        System.out.println(result);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {//키워드 입력 성공시
                                Toast.makeText(getApplicationContext(), "키워드를 등록했습니다.", Toast.LENGTH_SHORT).show();
                                printKeyword();
                                keyword.setText("");
                            } else {//키워드 입력 실패시
                                Toast.makeText(getApplicationContext(), "이미 입력된 키워드 입니다.", Toast.LENGTH_SHORT).show();
                                keyword.setText("");
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                KeywdInputRequest keywdInputRequest = new KeywdInputRequest(user.getId(), keywordInput, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Setting.this);
                queue.add(keywdInputRequest);
            }
        });

    }

    /******************찜 가져오기***************/
    private void printZzim(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response;
                System.out.println(result);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("찜");
                    if (jsonArray.length() == 0) {
                        //찜 없으므로 할 거 없음
                    } else {
                        zzimadapter = new ZzimSingerAdapter();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String postNo = item.getString("zzim_postNo");
                            String POrF = item.getString("zzim_PorF");
                            String writer = item.getString("zzim_writer");
                            String title = item.getString("zzim_title");
                            String zclass = item.getString("zzim_class");
                            String due = item.getString("zzim_due");
                            String date = item.getString("zzim_date");
                            //날짜 변환
                            TransferDate td = new TransferDate();
                            String finaldate = td.TransferedDate(date, "date");
                            String finaldue = td.TransferedDate(due, "due");

                            zzimadapter.addItem(new ZzimItem(postNo, POrF, writer, title, zclass, finaldue, finaldate));
                        }
                        zzimList.setAdapter(zzimadapter);
                        zzimList.setOnItemClickListener(listener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ZzimRequest zzimShow = new ZzimRequest(user.getId(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Setting.this);
        queue.add(zzimShow);
    }

    ListView.OnItemClickListener listener= new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            final ZzimItem item = (ZzimItem) zzimadapter.getItem(position);
            if (item.getZzim_prodOrFood().equals("물품")){
//                prod_read로
                Intent intent = new Intent(getApplicationContext(), Product_Read.class);
                intent.putExtra("user", user);
                intent.putExtra("postNo", item.getZzim_postNo());
                intent.putExtra("postWriter", item.getZzim_writer());
                startActivity(intent);
            } else if(item.getZzim_prodOrFood().equals("음식")){
                //food_read로
                Intent intent = new Intent(getApplicationContext(), Food_Read.class);
                intent.putExtra("user", user);
                intent.putExtra("postNo", item.getZzim_postNo());
                intent.putExtra("postWriter", item.getZzim_writer());
                startActivity(intent);
            }
        }
    };

    /***************키워드 정보 가져오기**************/
    private void printKeyword() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                받아오는 값 확인
//                String result = response;
//                System.out.println(result);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("결과");
                    if (jsonArray.length() == 0) {
                        //키워드가 없으므로 할 거 없음
                    } else {
                        kwdadapter = new KwdSingerAdapter();
                        for (int i = 1; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            kwdadapter.addItem(String.format("# %s", item.getString("키워드")));
                        }
                        keywdList.setAdapter(kwdadapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        KeywdShowRequest keywdShow = new KeywdShowRequest(user.getId(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Setting.this);
        queue.add(keywdShow);
    }

    /*****************찜 리스트 뷰 어댑터****************/
    class ZzimSingerAdapter extends BaseAdapter {
        ArrayList<ZzimItem> items = new ArrayList<ZzimItem>();
        @Override
        public int getCount(){
            return items.size();
        }
        public void addItem(ZzimItem item){ items.add(item); }
        @Override
        public ZzimItem getItem(int position){ return items.get(position); }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ZzimView zzimView = null;
            if(convertView == null){
                zzimView = new ZzimView(getApplicationContext());
            } else {
                zzimView = (ZzimView)convertView;
            }
            ZzimItem item = items.get(position);
            zzimView.setZzim_writer(item.getZzim_writer());
            zzimView.setZzim_title(item.getZzim_title());
            System.out.println(item.getZzim_class());
            zzimView.setZzim_class(item.getZzim_class());
            zzimView.setZzim_due(item.getZzim_due());
            zzimView.setZzim_date(item.getZzim_date());
            zzimView.setZzim_class_bkg(item.getZzim_class());
            return zzimView;
        }
    }

    /***************키워드 리스트 뷰 어댑터**************/
    class KwdSingerAdapter extends BaseAdapter {
        ArrayList<String> items = new ArrayList<String>();
        @Override
        public int getCount(){
            return items.size();
        }
        public void addItem(String item){
            items.add(item);
        }
        @Override
        public String getItem(int position){
            return items.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            KeywordView keywordView = null;
            if(convertView == null){
                keywordView = new KeywordView(getApplicationContext());
            } else {
                keywordView = (KeywordView)convertView;
            }
            String item = items.get(position);
            keywordView.setKeyword(item);

            Button keywordDelete = (Button) keywordView.findViewById(R.id.keywdDelete);
            TextView keywd = keywordView.findViewById(R.id.keyword);
            String keywordtoDelete = keywd.getText().toString().substring(2);
            /***************키워드 삭제*****************/
            keywordDelete.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
////                              받아오는 값 확인
//                            String result = response;
//                            System.out.println(result);
                           try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {//키워드 삭제 성공시
                                    Toast.makeText(getApplicationContext(), "키워드를 삭제했습니다", Toast.LENGTH_SHORT).show();
                                    printKeyword();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    KeywdDeleteRequest keywdDeleteRequest = new KeywdDeleteRequest(user.getId(), keywordtoDelete, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Setting.this);
                    queue.add(keywdDeleteRequest);
                }
            });
            return keywordView;
        }
    }


    /************툴바***************/
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