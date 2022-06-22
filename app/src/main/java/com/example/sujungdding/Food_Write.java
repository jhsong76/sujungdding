package com.example.sujungdding;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.Calendar;

public class Food_Write extends AppCompatActivity {
    Button btnSelectDate;
    DatePickerDialog datePickerDialog;
    Spinner spinner2;
    //View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_write);
        spinner2 = findViewById(R.id.spinner2); //food spinner
        btnSelectDate = (Button) findViewById(R.id.date);

        //툴바
        Toolbar toolbar = findViewById(R.id.board_toolbar);
        setSupportActionBar(toolbar);

        //이동
        Button finish_btn = (Button) findViewById(R.id.finish);
        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Food_Board.class);
                startActivity(intent);
            }
        });

        //food spinner 기능코드

        ArrayAdapter foodAdapter = ArrayAdapter.createFromResource(this, R.array.food, android.R.layout.simple_spinner_dropdown_item);
        //R.array.people 모집인원수 파일/ android.R.layout.simple_spinner_dropdown_item은 기본으로 제공해주는 형식입니다.
        foodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(foodAdapter); //어댑터에 연결해줍니다.
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.
            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {
        if (v == btnSelectDate) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            btnSelectDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toolbar_mypage) {
            Intent intent = new Intent(this, Mypage.class);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.toolbar_chatroom){  //chatroom
            Intent intent = new Intent(this, Chatroom.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

