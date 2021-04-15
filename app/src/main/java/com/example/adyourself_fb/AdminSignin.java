package com.example.adyourself_fb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminSignin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_adminsignin;
    private EditText admin_account;
    private EditText admin_pwd;

    private String user_acc;

    //登入頁面(APP剛開啟)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_signin);

        btn_adminsignin=(Button) findViewById(R.id.btn_adminsignin);
        admin_account=(EditText) findViewById(R.id.admin_account);
        admin_pwd=(EditText) findViewById(R.id.admin_pwd);


        //登入按鈕
        mAuth = FirebaseAuth.getInstance();

        btn_adminsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_acc = admin_account.getText().toString();
                    validate(admin_account.getText().toString(),admin_pwd.getText().toString());
            }
        });




    }

    //登入
    private  void validate(String account,String pwd){
        if (account.isEmpty()){
            Toast.makeText(AdminSignin.this, "請輸入帳號(信箱)", Toast.LENGTH_SHORT).show();
        }else if(pwd.isEmpty()){
            Toast.makeText(AdminSignin.this, "請輸入密碼", Toast.LENGTH_SHORT).show();
        }else if(account.isEmpty() && pwd.isEmpty()){
            Toast.makeText(AdminSignin.this, "請輸入帳號(信箱)", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.signInWithEmailAndPassword(account, pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithEmail:success");
                                Toast.makeText(AdminSignin.this,"管理者登入成功",Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent_tomainpage=new Intent(AdminSignin.this,AdminMainPage.class);

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
                                Toast.makeText(AdminSignin.this,"登入失敗",Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }

    }
}