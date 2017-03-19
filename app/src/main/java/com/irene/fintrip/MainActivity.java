package com.irene.fintrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://api.fixer.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void StartDetail(View view) {
        Intent i = new Intent(this,DetailsActivity.class);
        startActivity(i);
    }
}
