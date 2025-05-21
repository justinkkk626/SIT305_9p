package com.example.justin9p;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitle, tvDescription, tvDate, tvLocation, tvContact;
    Button btnRemove;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvDate = findViewById(R.id.tvDate);
        tvLocation = findViewById(R.id.tvLocation);
        tvContact = findViewById(R.id.tvContact);
        btnRemove = findViewById(R.id.btnRemove);

        db = new DatabaseHelper(this);

        int id = getIntent().getIntExtra("id", -1);
        tvTitle.setText(getIntent().getStringExtra("title"));
        tvDescription.setText(getIntent().getStringExtra("description"));
        tvDate.setText(getIntent().getStringExtra("date"));
        tvLocation.setText(getIntent().getStringExtra("location"));
        tvContact.setText(getIntent().getStringExtra("contact"));

        btnRemove.setOnClickListener(v -> {
            db.deleteItem(id);
            finish();
        });
    }
}
