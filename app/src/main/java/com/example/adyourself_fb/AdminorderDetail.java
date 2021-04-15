package com.example.adyourself_fb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminorderDetail extends AppCompatActivity {

    private Button btn_penOK;
    private Button btn_penNOK;
    private Button btn_checkAD;

    //資料庫
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminorder_detail);

        //取從待審核訂單獲得的資料
        Bundle bundleclass =this.getIntent().getExtras();
        final String D_AD = bundleclass.getString("t_AD");
        String D_BoardID = bundleclass.getString("t_BoardID");
        String D_Date = bundleclass.getString("t_Date");
        final String D_Pending = bundleclass.getString("t_Pending");
        String D_Price = bundleclass.getString("t_Price");
        String D_Time = bundleclass.getString("t_Time");
        final String D_User = bundleclass.getString("t_User");
        final String D_OrderID = bundleclass.getString("t_OrderID");

        final int D_OrderNum = bundleclass.getInt("t_OrderNum");

        //顯示在頁面上
        Button btn_checkAD = findViewById(R.id.btn_checkAD);
        //TextView TV_AD = findViewById(R.id.Detail_AD);
        TextView TV_BoardID = findViewById(R.id.Detail_Board);
        TextView TV_Date = findViewById(R.id.Detail_Date);
        TextView TV_Pending = findViewById(R.id.Detail_Pending);
        TextView TV_Price = findViewById(R.id.Detail_Price);
        TextView TV_Time = findViewById(R.id.Detail_Time);
        TextView TV_User = findViewById(R.id.Detail_User);
        TextView TV_OrderID = findViewById(R.id.Detail_OrderID);


        //查看廣告內容
        btn_checkAD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Uri uri= Uri.parse(D_AD);
                Intent C_AD=new Intent(Intent.ACTION_VIEW,uri);
                startActivity(C_AD);
            }
        });

        //TV_AD.setText(""+D_AD);
        TV_BoardID.setText(""+D_BoardID);
        TV_Date.setText(""+D_Date);
        TV_Pending.setText(""+D_Pending);
        TV_Price.setText(""+D_Price);
        TV_Time.setText(""+D_Time);
        TV_User.setText(""+D_User);
        TV_OrderID.setText("訂單編號:"+D_OrderID);



        //確認要新增到哪個使用者的通知及通知數量
        final int[] userdatacheck = {0};

        ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int datacount=(int) dataSnapshot.child("User").child("" + D_User).child("Notification").getChildrenCount();//獲得資料庫中下訂者的通知數量;

                //要新增通知的位置
                userdatacheck[0] = datacount + 1;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //審核通過按鈕
        //將此筆訂單的Pending狀態改為Yes(此訂單通過審核)
        btn_penOK=(Button) findViewById(R.id.btn_Penging_OK);
        btn_penOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //修改Order下的Pending
                myRef.child("Order").child(D_OrderNum+"").child("Pending").setValue("Yes");
                Toast.makeText(AdminorderDetail.this, "此訂單審核通過！", Toast.LENGTH_SHORT).show();

                //新增通知
                myRef.child("User").child(D_User).child("Notification").child(userdatacheck[0] + "").setValue("您的訂單編號" + D_OrderID + "已通過審核。");

                Intent intent_pendingsuccess = new Intent(AdminorderDetail.this, AdminMainPage.class);
                startActivity(intent_pendingsuccess);
            }
        });

        //駁回訂單按鈕
        //將此筆訂單的Pending狀態改為Cancel(取消此筆訂單)
        btn_penNOK=(Button) findViewById(R.id.btn_Penging_NOK);
        btn_penNOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //修改Order下的Pending
                myRef.child("Order").child(D_OrderNum+"").child("Pending").setValue("Cancel");
                Toast.makeText(AdminorderDetail.this, "此訂單已被取消！", Toast.LENGTH_SHORT).show();

                //新增通知
                myRef.child("User").child(D_User).child("Notification").child(userdatacheck[0] + "").setValue("您的訂單編號" + D_OrderID + "未通過審核，此訂單已取消請重新下單");


                Intent intent_pendingsuccess = new Intent(AdminorderDetail.this, AdminMainPage.class);
                startActivity(intent_pendingsuccess);
            }
        });
    }
}
