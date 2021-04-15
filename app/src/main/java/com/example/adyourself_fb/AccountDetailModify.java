package com.example.adyourself_fb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AccountDetailModify extends AppCompatActivity {

    private String ADM_Usernow;
    private String ADM_userphone;
    private String ADM_usercred;

    private Button btn_adm_modify;
    private Button btn_adm_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail_modify);

        //接收登入or註冊頁面傳來的使用者資訊
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            ADM_Usernow = bundle.getString("AD_Usernow");
            ADM_userphone = bundle.getString("AD_userphone");
            ADM_usercred = bundle.getString("AD_usercred");
        }



        //設定顯示成接收到的會員資料
        TextView tv_adm_useracc = findViewById(R.id.tv_adm_useracc);
        final EditText tv_adm_userphone = findViewById(R.id.ed_adm_userphone);
        final EditText tv_adm_usercred = findViewById(R.id.ed_adm_usercred);

        tv_adm_useracc.setText(ADM_Usernow+"@gmail.com");
        tv_adm_userphone.setText(ADM_userphone);
        tv_adm_usercred.setText(ADM_usercred);





        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("User");

        //確定修改，修改完後跳轉回會員資料頁面
        btn_adm_modify = (Button) findViewById(R.id.btn_adm_modify);
        btn_adm_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_modifyaccdetail = new Intent(AccountDetailModify.this,AccountDetail.class);

                String tmp_userphone = tv_adm_userphone.getText().toString();;
                String tmp_usercred = tv_adm_usercred.getText().toString();;

                myRef.child(ADM_Usernow).child("Phone").setValue(tmp_userphone);
                myRef.child(ADM_Usernow).child("Creditcard").setValue(tmp_usercred);
                Toast.makeText(AccountDetailModify.this, "修改會員資料成功", Toast.LENGTH_SHORT).show();

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", ADM_Usernow);
                intent_modifyaccdetail.putExtras(bundle);

                startActivity(intent_modifyaccdetail);

            }
        });


        //取消修改，跳轉回會員資料頁面
        btn_adm_cancel = (Button) findViewById(R.id.btn_adm_cancel);
        btn_adm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_backto_accountdetail = new Intent(AccountDetailModify.this,AccountDetail.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", ADM_Usernow);
                intent_backto_accountdetail.putExtras(bundle);

                startActivity(intent_backto_accountdetail);
            }
        });


        
    }
}
