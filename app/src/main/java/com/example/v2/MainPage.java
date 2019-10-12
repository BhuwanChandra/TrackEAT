package com.example.v2;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {

    TextView textView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        textView = findViewById(R.id.textView);
        String myValue = getIntent().getStringExtra("name");
        textView.setText(myValue);

    }
}
