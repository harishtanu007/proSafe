package com.harish.prosafe.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.harish.prosafe.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.button).setOnClickListener(v -> {
            String message="Harish";
            Intent intent=new Intent();
            intent.putExtra("MESSAGE",message);
            setResult(174,intent);
            finish();
        });
    }
}