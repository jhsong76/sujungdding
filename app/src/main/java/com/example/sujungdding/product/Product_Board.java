package com.example.sujungdding.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

public class Product_Board extends AppCompatActivity {
    Spinner spinner;
    private User user;

    private static String TAG = "phptest_Productboard";
    private static final String TAG_NO = "postNo";
    private static final String TAG_JSON = "prod_board";
    private static final String TAG_CLAS = "clas";
    private static final String TAG_DATE = "date";
    private static final String TAG_TITLE = "title";
    private static final String TAG_WRITER = "writer";
    private static final String TAG_CONTENT = "content";
    private Button searchbtn;

    ArrayList<HashMap<String, String>> pArrayList;
    ListView plistView;
    String pJsonString;
//    SwipeRefreshLayout mSwipeRefreshLayout;

    TransferDate td = new TransferDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_board);

        //????????? ????????? ????????????
        Intent preIntent = getIntent();
        user = (User) preIntent.getParcelableExtra("user");

        plistView = (ListView) findViewById(R.id.prod_list);
        pArrayList = new ArrayList<>();

        Product_Board.GetData task = new Product_Board.GetData();
        task.execute("http://192.168.219.102/prodlist.php");

        ////////??????
        searchbtn = (Button) findViewById(R.id.prod_searchbtn);

        /****??????****/
        Toolbar toolbar = findViewById(R.id.product_toolbar);
        setSupportActionBar(toolbar);
        //????????????
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.abc_vector_test);

        ImageButton write_btn = (ImageButton) findViewById(R.id.prod_add);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Product_Write.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        String[] items = {"??????", "?????????", "??????", "????????????", "????????????", "?????????", "??????"};
        spinner = findViewById(R.id.prod_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pArrayList.clear();
                showResult("??????", items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pArrayList.clear();
                EditText search = (EditText) findViewById(R.id.prod_searchview);
                String searchKeywd = search.getText().toString();
//                Toast.makeText(getApplicationContext(), searchKeywd, Toast.LENGTH_SHORT).show();
                showResult("??????", searchKeywd);
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Product_Board.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response  - " + result);
            if (result == null) {

            } else {
                pJsonString = result;
                System.out.println(pJsonString);
                showResult("??????", "??????");
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
            JSONObject jsonObject = new JSONObject(pJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            if (classification.equals("??????")) {
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
                    pArrayList.add(hashMap);
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
                        pArrayList.add(hashMap);
                    }
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void searchResult(String searchkwd) {
        try {
            JSONObject jsonObject = new JSONObject(pJsonString);
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
                    pArrayList.add(hashMap);
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private void showResult(String searchOrClass, String kwd) {
        try {
            if (searchOrClass.equals("??????")) {
                classificationResult(kwd);
            } else if (searchOrClass.equals("??????")) {
                searchResult(kwd);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Product_Board.this, pArrayList, R.layout.food_listitem,
                    new String[]{TAG_CLAS, TAG_TITLE, TAG_WRITER, TAG_DATE},
                    new int[]{R.id.foodlist_class, R.id.foodlist_title, R.id.foodlist_writer, R.id.foodlist_date}
            );

            plistView.setAdapter(adapter);
            plistView.setOnItemClickListener(listener);

        } catch (Exception e) {
            Log.d(TAG, "showResult : ", e);
        }
    }


    AdapterView.OnItemClickListener listener= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(getApplicationContext(), Product_Read.class);
            intent.putExtra("postNo", pArrayList.get(position).get(TAG_NO));
            intent.putExtra("postWriter", pArrayList.get(position).get(TAG_WRITER));
            intent.putExtra("user",user);
            startActivity(intent);
        }
    };

    /******??????*******/
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

    /******************????????????********************/
    private long backKeyPressedTime = 0;
    // ??? ?????? ?????? ?????? ????????? ?????? ??? ??????
    private Toast toast;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // ?????? ?????? ?????? ????????? ????????? ?????? ?????? ?????? ?????? ?????? ??????

        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ?????? ?????? ????????? ?????? ???
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ???????????? Toast ??????
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "?????? ?????? ????????? ??? ??? ??? ???????????? ???????????????.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ?????? ?????? ????????? ?????? ???
        // ??????????????? ?????? ?????? ????????? ????????? ????????? 2.5?????? ????????? ???????????? ??????
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            toast.cancel();
            toast = Toast.makeText(this,"????????? ????????? ???????????????.",Toast.LENGTH_LONG);
            toast.show();
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}