package com.example.adyourself_fb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {

    private TextView showuser;
    private String usernow;
    private ImageButton btn_to_queryeboard;
    private ImageButton btn_to_accountdetail;
    private ImageButton btn_to_notification;
    private ImageButton btn_to_loveeboard;
    private ImageButton btn_to_qrcode;
    private ImageButton btn_to_userorderhistory;
    private Button btn_mp_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);



        //接收登入or註冊頁面傳來的使用者資訊
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            usernow = bundle.getString("nowuser");
        }

        String[] split_line = usernow.split("@");
        String MP_Usernow = split_line[0];


        //左上角顯示現在使用者
        showuser =(TextView) findViewById(R.id.text_showuser);
        showuser.setText("歡迎使用ADYourSelf，"+MP_Usernow+"@gmail.com。");


        btn_to_queryeboard = (ImageButton) findViewById(R.id.btn_to_queryeboard);
        btn_to_accountdetail = (ImageButton) findViewById(R.id.btn_to_accountdetail);
        btn_to_notification = (ImageButton) findViewById(R.id.btn_to_notification);
        btn_to_loveeboard = (ImageButton) findViewById(R.id.btn_to_loveeboard);
        btn_to_qrcode = (ImageButton) findViewById(R.id.btn_to_qrcode);
        btn_to_userorderhistory = (ImageButton) findViewById(R.id.btn_to_userorderhistory);
        btn_mp_signout = (Button) findViewById(R.id.btn_mp_signout);


        //登出
        btn_mp_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_accountdetail=new Intent(MainPage.this,MainActivity.class);
                startActivity(intent_to_accountdetail);
            }
        });


        //跳轉到查詢電子看板頁面
        /*btn_to_queryeboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

       //跳轉到查詢電子看板頁面 NEW-20200409: New Created
       btn_to_queryeboard.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
           Intent intent_to_queryeboard=new Intent(MainPage.this,QueryEboard.class);
           Bundle bundle = new Bundle();
           bundle.putString("nowuser", usernow);
           intent_to_queryeboard.putExtras(bundle);

           startActivity(intent_to_queryeboard);

          }
       });

        //跳轉到帳戶資料
        btn_to_accountdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_accountdetail=new Intent(MainPage.this,AccountDetail.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", usernow);
                intent_to_accountdetail.putExtras(bundle);

                startActivity(intent_to_accountdetail);
            }
        });


        //跳轉到訊息通知
        btn_to_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_notification=new Intent(MainPage.this,Notification.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", usernow);
                intent_to_notification.putExtras(bundle);

                startActivity(intent_to_notification);
            }
        });


        //跳轉到我的最愛看板
        btn_to_loveeboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_loveeboard=new Intent(MainPage.this,LoveEboard.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", usernow);
                intent_to_loveeboard.putExtras(bundle);

                startActivity(intent_to_loveeboard);
            }
        });


        //跳轉到QRCODE掃描頁面
        btn_to_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_qrcode=new Intent(MainPage.this,QRCode.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", usernow);
                intent_to_qrcode.putExtras(bundle);

                startActivity(intent_to_qrcode);
            }
        });



        //跳轉到訂單紀錄頁面
        btn_to_userorderhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_to_userorderhistory=new Intent(MainPage.this,UserOrderHistory.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", usernow);
                intent_to_userorderhistory.putExtras(bundle);

                startActivity(intent_to_userorderhistory);
            }
        });

    }
}
