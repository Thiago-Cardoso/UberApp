package com.clone.tcardoso.uberclone.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.clone.tcardoso.uberclone.R;
import com.clone.tcardoso.uberclone.helper.UserFirebase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
    }

    public void openLogin(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void openRegister(View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        UserFirebase.redirectUserLogged(MainActivity.this);
    }
}
