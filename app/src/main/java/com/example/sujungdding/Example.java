package com.example.sujungdding;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Example extends AppCompatActivity {
    private android.widget.TextView textView;
    private String jsonString;
    ArrayList<Tree> treeArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example);

        textView = (TextView) findViewById(R.id.textView);
        JsonParse jsonParse = new JsonParse();
        jsonParse.execute("http://192.168.219.103/nicknametest.php");
        //http://115.140.175.200/nicknametest.php로 하면 다른 화면으로 전환돼서 다른 서버 페이지로 확인필요
    }

    public class JsonParse extends AsyncTask<String, Void, String> {
        String TAG = "JsonParseTest";
        @Override
        protected String doInBackground(String... strings){
            String url = strings[0];
            try {
                URL serverURL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)serverURL.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();

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
                Log.d(TAG, sb.toString().trim());

                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                String errorString = e.toString();
                textView.setText(errorString);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String fromdoInBackgroundString) {
            super.onPostExecute(fromdoInBackgroundString);

            if (fromdoInBackgroundString == null)
                textView.setText("error from function name: onPostexecute");
            else {
                jsonString = fromdoInBackgroundString;
                textView.setText(jsonString);

                //에러 났던 부분 -> 서버에 데이터베이스 따로 두면 잘 될 거 같아
               /* treeArrayList = doParse();
                Log.d(TAG, treeArrayList.get(0).getNickname());
                textView.setText(treeArrayList.get(0).getNickname());*/
            }
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values){
            super.onProgressUpdate(values);
        }

        private ArrayList<User> doParse() {
            ArrayList<User> tmpUserArray = new ArrayList<User>();
            try{
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("회원");

                for(int i = 0; i < jsonArray.length(); i++) {
                    User tmpUser = new User();
                    JSONObject item = jsonArray.getJSONObject(i);
                    tmpUser.setId(item.getString("아이디"));
                    tmpUser.setPwd(item.getString("비밀번호"));
                    tmpUser.setNname(item.getString("별명"));
                    tmpUser.setZzim(item.getString("찜"));
                    tmpUser.setKeywd(item.getString("키워드"));
                    tmpUserArray.add(tmpUser);
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            return tmpUserArray;
        }
    }
}
