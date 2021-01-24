package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(this);
        button=findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener(){
                                      @Override
                                      public void onClick(View v) {
                                          Toast.makeText(HomeActivity.this,"Thank Your for using our app. Bye",Toast.LENGTH_SHORT).show();
                                          Intent intent= new Intent(HomeActivity.this,MainActivity.class);
                                          startActivity(intent);
                                          Paper.book().destroy();
                                      }
                                  }
        );
    }
}