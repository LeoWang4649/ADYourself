package com.example.adyourself_fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUp extends AppCompatActivity {

    //資料庫
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    //Firebase內建登入
    private FirebaseAuth mAuth;

    private Button btn_signup;
    private EditText edi_account;
    private EditText edi_pwd;
    private EditText edi_phone;
    private EditText edi_creditcard;

    private String user_acc;
    private String user_pwd;
    private String user_phone;
    private String user_creditcard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btn_signup=(Button) findViewById(R.id.btn_signup);
        edi_account=(EditText) findViewById(R.id.Account_edi);
        edi_pwd=(EditText) findViewById(R.id.Password_edi);
        edi_phone=(EditText) findViewById(R.id.Phone_edi);
        edi_creditcard=(EditText) findViewById(R.id.CreditCard_edi);

        //FB
        mAuth = FirebaseAuth.getInstance();

        //註冊按鈕
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_acc = edi_account.getText().toString();
                user_pwd = edi_pwd.getText().toString();
                user_phone = edi_phone.getText().toString();
                user_creditcard = edi_creditcard.getText().toString();

                //註冊驗證
                validate2(user_acc,user_pwd);

                //將使用者註冊帳號以"@"分隔(資料好像不能有"@")
                String[] split_line = user_acc.split("@");

                //寫入使用者資料
                myRef.child("User").child(split_line[0]).child("Phone").setValue(user_phone);
                myRef.child("User").child(split_line[0]).child("Creditcard").setValue(user_creditcard);
                myRef.child("User").child(split_line[0]).child("Notification").child("1").setValue("歡迎使用AD YourSelf，" + user_acc + "。");

            }
        });




    }

    //註冊
    private  void validate2(final String account, final String pwd){
        if (account.isEmpty()){
            Toast.makeText(SignUp.this, "請輸入帳號(信箱)", Toast.LENGTH_SHORT).show();
        }else if(pwd.isEmpty()){
            Toast.makeText(SignUp.this, "請輸入密碼", Toast.LENGTH_SHORT).show();
        }else if(account.isEmpty() && pwd.isEmpty()){
            Toast.makeText(SignUp.this, "請輸入帳號(信箱)", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.createUserWithEmailAndPassword(account, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "createUserWithEmail:success");
                                Toast.makeText(SignUp.this, "註冊成功", Toast.LENGTH_SHORT).show();
                                Intent intent_signupsuccess = new Intent(SignUp.this, MainPage.class);

                                //放入現在使用者的帳號(信箱)
                                Bundle bundle = new Bundle();
                                bundle.putString("nowuser", user_acc);
                                intent_signupsuccess.putExtras(bundle);

                                //跳轉到主頁
                                startActivity(intent_signupsuccess);
                                FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                //        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                Toast.makeText(SignUp.this, "註冊失敗", Toast.LENGTH_SHORT).show();
                            }
                            // ...
                        }
                    });
        }

    }
}
