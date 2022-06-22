package com.example.sujungdding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class Product_Board extends AppCompatActivity {
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_board);

        Toolbar toolbar = findViewById(R.id.product_toolbar);
        setSupportActionBar(toolbar);

        ImageButton write_btn = (ImageButton) findViewById(R.id.product_add);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Product_Write.class);
                startActivity(intent);
            }
        });

        spinner = findViewById(R.id.product_spinner);
        ArrayAdapter productsort = ArrayAdapter.createFromResource(this, R.array.product, android.R.layout.simple_spinner_dropdown_item);
        productsort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(productsort);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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