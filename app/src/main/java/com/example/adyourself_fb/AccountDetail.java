package com.example.adyourself_fb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;


public class AccountDetail extends AppCompatActivity {

    private String A_usernow;
    private Button btn_ad_toaccountdetailmodify;
    private ImageButton btn_ad_tomainpage;

    private String[] tmp_foradbundle = {"A","A"};

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");


        //接收登入or註冊頁面傳來的使用者資訊
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            A_usernow = bundle.getString("nowuser");
        }

        String[] split_line = A_usernow.split("@");
        final String AD_Usernow = split_line[0];

        TextView tv_ad_useracc = findViewById(R.id.tv_ad_useracc);
        final TextView tv_ad_userphone = findViewById(R.id.tv_ad_userphone);
        final TextView tv_ad_usercred = findViewById(R.id.tv_ad_usercred);

        //抓會員資料並顯示
        tv_ad_useracc.setText(AD_Usernow+"@gmail.com");

        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tmp_userphone = (dataSnapshot.child(AD_Usernow).child("Phone").getValue() + "");
                String tmp_usercred = (dataSnapshot.child(AD_Usernow).child("Creditcard").getValue() + "");

                tv_ad_userphone.setText(tmp_userphone);
                tv_ad_usercred.setText(tmp_usercred);

                tmp_foradbundle[0] = tmp_userphone;
                tmp_foradbundle[1] = tmp_usercred;

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        //跳轉到修改會員資料頁面
        btn_ad_toaccountdetailmodify = (Button) findViewById(R.id.btn_ad_toaccountdetailmodify);
        btn_ad_toaccountdetailmodify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_accountdetailmodify=new Intent(AccountDetail.this,AccountDetailModify.class);

                Bundle bundle = new Bundle();
                bundle.putString("AD_Usernow", AD_Usernow);
                bundle.putString("AD_userphone", tmp_foradbundle[0]);
                bundle.putString("AD_usercred", tmp_foradbundle[1]);

                intent_to_accountdetailmodify.putExtras(bundle);

                startActivity(intent_to_accountdetailmodify);
            }
        });

        //跳轉回主頁面
        btn_ad_tomainpage = (ImageButton) findViewById(R.id.btn_ad_tomainpage);
        btn_ad_tomainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_ad_tomainpage=new Intent(AccountDetail.this,MainPage.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", AD_Usernow);

                intent_ad_tomainpage.putExtras(bundle);

                startActivity(intent_ad_tomainpage);
            }
        });
    }
}
