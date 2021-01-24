package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button CreateAccountButton;
    private EditText InputName, InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        CreateAccountButton=findViewById(R.id.register_btn);
        InputName=findViewById(R.id.register_name_input);
        InputPhoneNumber=findViewById(R.id.register_phone_number_input);
        InputPassword=findViewById(R.id.register_password_input);
        loadingBar=new ProgressDialog(this);


        CreateAccountButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
            CreateAccount();
        }});
    }
    private void CreateAccount(){
        String name=InputName.getText().toString();
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();
        if(TextUtils.isEmpty(name))
            Toast.makeText(this,"Name is required * ",Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(phone))
            Toast.makeText(this,"Phone number is required * ",Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(password))
            Toast.makeText(this,"Password is required * ",Toast.LENGTH_SHORT).show();
        else{
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait while we save and check your credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidatePhoneNumber(name,phone,password);

        }
    }
    private void ValidatePhoneNumber(final String name,final String phone, final String password){

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);

                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Congratulations. Your, account created Successfully. Please Login",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent= new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this,"Network Error",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                    }
                                }
                            });
                }
                else
                {   String a="This "+ phone +" already exists.";
                    Toast.makeText(RegisterActivity.this,a,Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent= new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}