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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.sujungdding.R;
import com.example.sujungdding.TransferDate;
import com.example.sujungdding.User;
import com.example.sujungdding.food.Food_Read;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Mypost extends AppCompatActivity {
    private ListView myPostList;
    private User user;
    MypostSingerAdapter mypostAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        //사용자 정보 불러오기
        Intent preIntent = getIntent();
        user = (User) preIntent.getParcelableExtra("user");
        /********내가쓴 글 리스트 뷰*******/
        myPostList = (ListView) findViewById(R.id.my_list);
        printMy();
        /****툴바****/
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);
    }

    /******************내 글 가져오기***************/
    private void printMy(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response;
                System.out.println(result);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("My_post");
                    if (jsonArray.length() == 0) {
                        //게시글 없으므로 할 거 없음
                    } else {
                        mypostAdapter = new MypostSingerAdapter();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String postNo = item.getString("myPost_no");
                            String POrF = item.getString("myPost_PorF");
                            String title = item.getString("myPost_title");
                            String zclass = item.getString("myPost_class");
                            String due = item.getString("myPost_due");
                            String date = item.getString("myPost_date");
                            //날짜 변환
                            TransferDate td = new TransferDate();
                            String finaldate = td.TransferedDate(date, "date");
                            String finaldue = td.TransferedDate(due, "due");

                            mypostAdapter.addItem(new ZzimItem(postNo, POrF, user.getNname(), title, zclass, finaldue, finaldate));
                        }
                        myPostList.setAdapter(mypostAdapter);
                        myPostList.setOnItemClickListener(listener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        MypostRequest mypostRequest = new MypostRequest(user.getId(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypost.this);
        queue.add(mypostRequest);
    }

    /*****************내 글 리스트 뷰 어댑터 => 찜 아이템 재사용****************/
    class MypostSingerAdapter extends BaseAdapter {
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
            MypostView mypostView = null;
            if(convertView == null){
                mypostView = new MypostView(getApplicationContext());
            } else {
                mypostView = (MypostView)convertView;
            }
            ZzimItem item = items.get(position);
            mypostView.setMypost_writer(item.getZzim_writer());
            mypostView.setMypost_title(item.getZzim_title());
            mypostView.setMypost_class(item.getZzim_class());
            mypostView.setMypost_due(item.getZzim_due());
            mypostView.setMypost_date(item.getZzim_date());
            mypostView.setMypost_class_bkg(item.getZzim_class());
            return mypostView;
        }
    }


    ListView.OnItemClickListener listener= new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            final ZzimItem item = (ZzimItem) mypostAdapter.getItem(position);
            if (item.getZzim_prodOrFood().equals("물품")){
                //prod_read로
//                Intent intent = new Intent(getApplicationContext(), Product_Read.class);
//                intent.putExtra("user", user);
//                intent.putExtra("postNo", item.getZzim_postNo());
//                startActivity(intent);
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

    /*******툴바*******/
    @Override
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