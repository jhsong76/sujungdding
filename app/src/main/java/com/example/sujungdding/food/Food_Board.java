package com.example.sujungdding.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.sujungdding.TransferDate;
import com.example.sujungdding.User;
import com.example.sujungdding.mypage.Mypage;
import com.example.sujungdding.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Food_Board extends AppCompatActivity {
    private static String TAG = "phptest_Foodboard";
    private static final String TAG_NO = "postNo";
    private static final String TAG_JSON = "food_board";
    private static final String TAG_CLAS = "clas";
    private static final String TAG_DATE = "date";
    private static final String TAG_TITLE = "title";
    private static final String TAG_WRITER = "writer";
    private static final String TAG_CONTENT = "content";
    private Button searchbtn;

    ArrayList<HashMap<String, String>> fArrayList;
    ListView flistView;
    String fJsonString;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Spinner spinner;
    User user = new User();
    TransferDate td = new TransferDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_board);

        //사용자 데이터 가져오기
        Intent preIntent = getIntent();
        user = (User) preIntent.getParcelableExtra("user");

        flistView = (ListView) findViewById(R.id.food_list);
        fArrayList = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://192.168.219.102/foodlist.php");

        ////////검색
        searchbtn = (Button) findViewById(R.id.food_searchbtn);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        /****툴바****/
        Toolbar toolbar = findViewById(R.id.food_toolbar);
        setSupportActionBar(toolbar);
        //뒤로가기
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);

        ImageButton write_btn = (ImageButton) findViewById(R.id.food_add);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Food_Write.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        String[] items = {"전체", "한식", "양식", "중식", "일식", "분식", "패스트푸드", "기타"};
        spinner = findViewById(R.id.food_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fArrayList.clear();
                showResult("분류", items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fArrayList.clear();
                EditText search = (EditText) findViewById(R.id.food_searchview);
                String searchKeywd = search.getText().toString();
//                Toast.makeText(getApplicationContext(), searchKeywd, Toast.LENGTH_SHORT).show();
                showResult("검색", searchKeywd);
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Food_Board.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);
            if (result == null) {

            } else {
                fJsonString = result;
                showResult("분류", "전체");
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    private void classificationResult(String classification) {
        try {
            JSONObject jsonObject = new JSONObject(fJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            if (classification.equals("전체")) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String number = Integer.toString(item.getInt(TAG_NO));
                    String clas = item.getString(TAG_CLAS);
                    String title = item.getString(TAG_TITLE);
                    String writer = item.getString(TAG_WRITER);
                    String date = item.getString(TAG_DATE);
                    String content = item.getString(TAG_CONTENT);
                    String final_date = td.TransferedDate(date, "date");
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(TAG_NO, number);
                    hashMap.put(TAG_CLAS, clas);
                    hashMap.put(TAG_TITLE, title);
                    hashMap.put(TAG_WRITER, writer);
                    hashMap.put(TAG_DATE, final_date);
                    hashMap.put(TAG_CONTENT, content);
                    fArrayList.add(hashMap);
                }
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    String clas = item.getString(TAG_CLAS);
                    if (clas.equals(classification)) {
                        String number = Integer.toString(item.getInt(TAG_NO));
                        String title = item.getString(TAG_TITLE);
                        String writer = item.getString(TAG_WRITER);
                        String date = item.getString(TAG_DATE);
                        String final_date = td.TransferedDate(date, "date");
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(TAG_NO, number);
                        hashMap.put(TAG_CLAS, clas);
                        hashMap.put(TAG_TITLE, title);
                        hashMap.put(TAG_WRITER, writer);
                        hashMap.put(TAG_DATE, final_date);
                        fArrayList.add(hashMap);
                    }
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void searchResult(String searchkwd) {
        try {
            JSONObject jsonObject = new JSONObject(fJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                String content = item.getString(TAG_CONTENT);
                String title = item.getString(TAG_TITLE);
                if (content.contains(searchkwd) || title.contains(searchkwd)) {
                    String number = Integer.toString(item.getInt(TAG_NO));
                    String clas = item.getString(TAG_CLAS);
                    String writer = item.getString(TAG_WRITER);
                    String date = item.getString(TAG_DATE);

                    String final_date = td.TransferedDate(date, "date");
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(TAG_NO, number);
                    hashMap.put(TAG_CLAS, clas);
                    hashMap.put(TAG_TITLE, title);
                    hashMap.put(TAG_WRITER, writer);
                    hashMap.put(TAG_DATE, final_date);
                    hashMap.put(TAG_CONTENT, content);
                    fArrayList.add(hashMap);
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showResult(String searchOrClass, String kwd) {
        try {
            if (searchOrClass.equals("분류")) {
                classificationResult(kwd);
            } else if (searchOrClass.equals("검색")) {
                searchResult(kwd);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Food_Board.this, fArrayList, R.layout.food_listitem,
                    new String[]{TAG_CLAS, TAG_TITLE, TAG_WRITER, TAG_DATE},
                    new int[]{R.id.foodlist_class, R.id.foodlist_title, R.id.foodlist_writer, R.id.foodlist_date}
            );

            flistView.setAdapter(adapter);
            flistView.setOnItemClickListener(listener);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mSwipeRefreshLayout.setRefreshing(true);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fArrayList.clear();
                            GetData task = new GetData();
                            task.execute("http://192.168.219.103/foodlist.php");
                            flistView.setAdapter(adapter);
                            flistView.setOnItemClickListener(listener);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }, 2000);
                }
            });

        } catch (Exception e) {
            Log.d(TAG, "showResult : ", e);
        }

    }
    /**********************/
    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getApplicationContext(), Food_Read.class);
            intent.putExtra("postNo", fArrayList.get(position).get(TAG_NO));
            intent.putExtra("postWriter", fArrayList.get(position).get(TAG_WRITER));
            intent.putExtra("user",user);
            startActivity(intent);
        }
    };

    /***************툴바***************/
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


    /******************뒤로가기 종료********************/
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