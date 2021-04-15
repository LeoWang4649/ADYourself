package com.example.adyourself_fb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_signin;
    private Button btn_to_signup;
    private Button btn_to_adminsignin;
    private EditText account;
    private EditText pwd;

    private String user_acc;

    //登入頁面(APP剛開啟)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_signin=(Button) findViewById(R.id.btn_signin);
        btn_to_signup=(Button) findViewById(R.id.btn_to_signup);
        btn_to_adminsignin=(Button) findViewById(R.id.btn_to_adminsignin);
        account=(EditText) findViewById(R.id.Account_input);
        pwd=(EditText) findViewById(R.id.Password_input);

        //跳轉到註冊頁面
        btn_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_tosignup=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent_tosignup);
            }
        });

        //跳轉到管理者登入頁面
        btn_to_adminsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_toadminsignin=new Intent(MainActivity.this,AdminSignin.class);
                startActivity(intent_toadminsignin);
            }
        });


        //登入按鈕
        mAuth = FirebaseAuth.getInstance();

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_acc = account.getText().toString();
                validate(account.getText().toString(),pwd.getText().toString());
            }
        });




    }

    //登入
    private  void validate(String account,String pwd){
        if (account.isEmpty()){
            Toast.makeText(MainActivity.this, "請輸入帳號(信箱)", Toast.LENGTH_SHORT).show();
        }else if(pwd.isEmpty()){
            Toast.makeText(MainActivity.this, "請輸入密碼", Toast.LENGTH_SHORT).show();
        }else if(account.isEmpty() && pwd.isEmpty()){
            Toast.makeText(MainActivity.this, "請輸入帳號(信箱)", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(account, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(MainActivity.this,"登入成功",Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent_tomainpage=new Intent(MainActivity.this,MainPage.class);

                                //放入現在使用者的帳號(信箱)
                                Bundle bundle = new Bundle();
                                bundle.putString("nowuser", user_acc);
                                intent_tomainpage.putExtras(bundle);
                                startActivity(intent_tomainpage);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                //        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                                Toast.makeText(MainActivity.this,"登入失敗",Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }
}
