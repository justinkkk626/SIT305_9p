package com.example.justin9p;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnCreateAdvert, btnShowAllItems, btnShowMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreateAdvert = findViewById(R.id.btnCreateAdvert);
        btnShowAllItems = findViewById(R.id.btnShowAllItems);
        btnShowMap = findViewById(R.id.btnShowMap);

        btnCreateAdvert.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddItemActivity.class)));

        btnShowAllItems.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, DetailActivity.class))); // 可换为 ItemListActivity

        btnShowMap.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, MapActivity.class)));
    }
}
