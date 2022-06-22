package com.example.sujungdding.product;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.sujungdding.TransferDate;
import com.example.sujungdding.User;
import com.example.sujungdding.food.CommentItem;
import com.example.sujungdding.food.CommentView;
import com.example.sujungdding.mypage.Mypage;
import com.example.sujungdding.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Product_Read extends AppCompatActivity {
    private User user;
    String postNo, postWriter;
    private Toast toast;
    TextView post_title, post_due, post_cont, post_minppl, comment;
    String writer, title, content, minppl, clas, finaldate, finaldue;
    Button zzimOrDelete, cmtBtn;
    private ListView cmtList;
    CmtListSingerAdapter cmtAdapter;
    CheckBox secret;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_read);

        //사용자 데이터 가져오기
        Intent preIntent = getIntent();
        user = (User) preIntent.getParcelableExtra("user");
        //게시글 정보
        postNo = preIntent.getStringExtra("postNo");
        postWriter = preIntent.getStringExtra("postWriter");

//      /******게시글 내용 불러오기*******/
        post_title = (TextView)findViewById(R.id.post_title);
        post_due = (TextView)findViewById(R.id.post_due);
        post_cont = (TextView)findViewById(R.id.post_cont);
        post_minppl = (TextView)findViewById(R.id.post_minppl);
        printPost();
        printComment();

        /*****댓글******/
        cmtList = (ListView)findViewById(R.id.food_cmtList);
        cmtBtn = (Button)findViewById(R.id.comment_btn);
        comment = (TextView)findViewById(R.id.comment);
        secret = (CheckBox)findViewById(R.id.cmt_secretCheck);

        /****툴바****/
        Toolbar toolbar = findViewById(R.id.pboardread_toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);

        zzimOrDelete = (Button) findViewById(R.id.zzim);
        if(postWriter.equals(user.getNname())){ //내가 쓴 게시글이면 삭제
            zzimOrDelete.setText("삭제");
            zzimOrDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zzimOrDelete.getText().toString().equals("삭제")) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //받아오는 값 확인
                                String result = response;
                                System.out.println(result);
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "게시글을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        ProdPostDeleteRequest prodPostDeleteRequest = new ProdPostDeleteRequest(user.getId(), postNo, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
                        queue.add(prodPostDeleteRequest);
                    }
                }
            });

        } else{ // 남이 쓴 게시글이면 찜
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                        받아오는 값 확인
                    String result = response;
                    System.out.println(result);
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {   //이미 찜을 했으면 별
                            zzimOrDelete.setText("★");
                        }
                        else {  //찜 안했으면 찜
                            zzimOrDelete.setText("찜");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ProdCheckZzimRequest poodCheckZzimRequest = new ProdCheckZzimRequest(user.getId(), postNo, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
            queue.add(poodCheckZzimRequest);

            zzimOrDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (zzimOrDelete.getText().toString().equals("찜")) { // 찜일 때 클릭하면 찜해
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                          받아오는 값 확인
                                String result = response;
                                System.out.println(result);
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {   //찜 저장 성공
                                        toast.makeText(getApplicationContext(), "게시글을 찜했습니다.", Toast.LENGTH_SHORT).show();
                                        zzimOrDelete.setText("★");
                                    }
                                    else {  //찜 저장 실패
                                        toast.makeText(getApplicationContext(), "게시글 찜에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        ProdZzimInsertRequest prodZzimInsertRequest = new ProdZzimInsertRequest(user.getId(), postNo, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
                        queue.add(prodZzimInsertRequest);
                    } else if (zzimOrDelete.getText().toString().equals("★")){ // 별일 때 클릭하면 찜 취소
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                        받아오는 값 확인
                                String result = response;
                                System.out.println(result);
                                try{
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {   //찜 저장 성공
                                        toast.makeText(getApplicationContext(), "게시글 찜을 취소했습니다.", Toast.LENGTH_SHORT).show();
                                        zzimOrDelete.setText("찜");
                                    }
                                    else {  //찜 저장 실패
                                        toast.makeText(getApplicationContext(), "게시글 찜 취소에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        ProdZzimDeleteRequest prodZzimDeleteRequest = new ProdZzimDeleteRequest(user.getId(), postNo, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
                        queue.add(prodZzimDeleteRequest);
                    }
                }
            });
        }



        /*************댓글 입력**************/
        cmtBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentInput = comment.getText().toString();
                if (commentInput.equals("")){
                    Toast.makeText(getApplicationContext(), "댓글을 입력하십시오.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (commentInput.length() > 50){
                    comment.setText("");
                    Toast.makeText(getApplicationContext(), "댓글이 너무 깁니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                      받아오는 값 확인
                        String result = response;
                        System.out.println(result);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {//키워드 입력 성공시
                                Toast.makeText(getApplicationContext(), "댓글을 입력했습니다.", Toast.LENGTH_SHORT).show();
                                printComment();
                                comment.setText("");
                            } else {//키워드 입력 실패시
                                Toast.makeText(getApplicationContext(), "댓글 입력에 실패했습니다", Toast.LENGTH_SHORT).show();
                                comment.setText("");
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                if(secret.isChecked()) {    //비밀 댓글 체크 돼있으면
                    ProdCommentInputRequest prodCommentInputRequest = new ProdCommentInputRequest(user.getId(), postNo, "1", commentInput, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
                    queue.add(prodCommentInputRequest);
                }else{
                    ProdCommentInputRequest prodCommentInputRequest = new ProdCommentInputRequest(user.getId(), postNo, "0", commentInput, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
                    queue.add(prodCommentInputRequest);
                }
            }
        });
    }

    /*******게시글 내용 가져오기********/
    private void printPost(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response;
                System.out.println(result);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("결과");
                    if (jsonArray.length() == 0) {
                        //게시글 못 불러옴
                    } else {
                        JSONObject item = jsonArray.getJSONObject(0);
                        writer = item.getString("post_writer");
                        title = item.getString("post_title");
                        content = item.getString("post_content");
                        minppl = item.getString("post_minppl");
                        String due = item.getString("post_due");
                        clas = item.getString("post_class");
                        String date = item.getString("post_date");
                        //날짜 변환
                        TransferDate td = new TransferDate();
                        finaldate = td.TransferedDate(date, "date");
                        finaldue = td.TransferedDate(due, "due");

                        post_title.setText(title);
                        post_cont.setText(content);
                        post_due.setText(finaldue);
                        post_minppl.setText(minppl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PRRequest prRequest = new PRRequest(postNo, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
        queue.add(prRequest);
    }

    /******************댓글 가져오기***************/
    private void printComment(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result = response;
                System.out.println(result);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("댓글");
                    if (jsonArray.length() == 0) {
                        //댓글 없으므로 할 거 없음
                    } else {
                        cmtAdapter = new CmtListSingerAdapter();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String number = item.getString("cmt_no");
                            String writer = item.getString("cmt_writer");
                            String secret = item.getString("cmt_secret");
                            String content;
                            if(secret.equals("1")){
                                content = "비밀 댓글입니다.";
                                if(postWriter.equals(user.getNname()) || writer.equals(user.getNname())){ //secret이 1이면=> userNname이 postWriter랑 uesrNname이랑 댓글 작성자가 다르면 "비밀댓글입니다."
                                    content = item.getString("cmt_content");
                                }
                            } else{
                                content = item.getString("cmt_content");
                            }
                            String date = item.getString("cmt_time");
                            //날짜 변환
                            TransferDate td = new TransferDate();
                            String finaldate = td.TransferedDate(date, "date");
                            cmtAdapter.addItem(new CommentItem(number, writer, content, secret, finaldate));
                        }
                        cmtList.setAdapter(cmtAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProdCommentShowRequest prodCommentShowRequest = new ProdCommentShowRequest(postNo, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
        queue.add(prodCommentShowRequest);
    }

    /*****************댓글 리스트 뷰 어댑터****************/
    class CmtListSingerAdapter extends BaseAdapter {
        ArrayList<CommentItem> items = new ArrayList<CommentItem>();
        @Override
        public int getCount(){
            return items.size();
        }
        public void addItem(CommentItem item){ items.add(item); }
        @Override
        public CommentItem getItem(int position){ return items.get(position); }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            CommentView commentView = null;
            if(convertView == null){
                commentView = new CommentView(getApplicationContext());
            } else {
                commentView = (CommentView)convertView;
            }
            CommentItem item = items.get(position);
            commentView.setCmt_writer(item.getCmt_writer());
            commentView.setCmt_content(item.getCmt_content());

//          게시글 작성자랑 댓글 작성자가 같으면 "작성자"로 바꿔
            if (item.getCmt_writer().equals(postWriter))
                commentView.setCmt_writer("작성자");

            //게시글 작성자가 로그인 한 상태고 댓글이 비밀 댓글이면 (그래야 작성자가 비밀 댓글이란 걸 알 수 있으니까)
//            if(postWriter.equals(user.getNname()) && item.getCmt_secret().equals("1"))
//                commentView.setCmt_secret("비밀 댓글입니다.");

//            + 댓글 작성자가 로그인 한 상태고 댓글이 비댓이면 (내가 비댓을 달았을때)
            if(item.getCmt_secret().equals("1"))
                commentView.setCmt_secret("비밀 댓글 입니다");

            commentView.setCmt_date(item.getCmt_date());

            //댓글 작성자가 로그인 한 상태라면 삭제 보이기
            if(user.getNname().equals(item.getCmt_writer())) {
                commentView.setCmt_del();
                /***************댓글 삭제*****************/
                commentView.cmt_del.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
////                              받아오는 값 확인
                                String result = response;
                                System.out.println(result);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {//댓글 삭제 성공시
                                        Toast.makeText(getApplicationContext(), "댓글을 삭제했습니다", Toast.LENGTH_SHORT).show();
                                        printComment();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        ProdCommentDeleteRequest prodCommentDeleteRequest = new ProdCommentDeleteRequest(item.getCmt_no(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Product_Read.this);
                        queue.add(prodCommentDeleteRequest);
                    }
                });
            }
            return commentView;
        }
    }

    /*****툴바******/
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
