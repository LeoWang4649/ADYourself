package com.example.adyourself_fb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Notification extends AppCompatActivity {
    private RecyclerView Notification_recyclerView;
    private RecyclerView.Adapter Notification_recyclerAdapter;
    private Notification_data[] Notification_Dataset;
    FirebaseDatabase mdatabase;
    DatabaseReference mRef;

    int datacount=0;
    private String[] tmp_Notification;//廣告內容

    private String U_usernow;

    private ImageButton btn_noti_tomainpage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference("User");

        //接收登入or註冊頁面傳來的使用者資訊
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            U_usernow = bundle.getString("nowuser");
        }

        final String[] split_line = U_usernow.split("@");

        final TextView UUU = findViewById(R.id.textViewUUU);
        final String NOT_Usernow = split_line[0];
        //UUU.setText(UOH_Usernow+"的訂單紀錄");

        //跳轉回主頁面
        btn_noti_tomainpage = (ImageButton) findViewById(R.id.btn_noti_tomainpage);
        btn_noti_tomainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_noti_tomainpage=new Intent(Notification.this,MainPage.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", NOT_Usernow);

                intent_noti_tomainpage.putExtras(bundle);

                startActivity(intent_noti_tomainpage);
            }
        });



        Notification_recyclerView = (RecyclerView)findViewById(R.id.notification_recyclerView);
        ValueEventListener valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datacount=(int) dataSnapshot.child(NOT_Usernow+"").child("Notification").getChildrenCount();//獲得資料庫中訂單的數量;

                tmp_Notification= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(NOT_Usernow+"").child("Notification").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(NOT_Usernow+"").child("Notification").child("" + i).getValue() + "");
                    tmp_Notification[i-1]=gametmp;
                }

                setRecyclerView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void setRecyclerView(){
        Notification_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //設置格線
        Notification_recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(new MDGridRvDividerDecoration(getActivity()));
        setData();

        //設置格線
        //recyclerView.addItemDecoration(new MDGridRvDividerDecoration(this));

        Notification_recyclerAdapter = new MyAdapterNotification(Notification_Dataset);
        Notification_recyclerView.setAdapter(Notification_recyclerAdapter);

    }
    public void setData(){
        Notification_Dataset = new Notification_data[datacount];
        int j=0;
        for (int i = datacount-1 ; i >=0 ;i--){

            Notification_Dataset[j] = new Notification_data(tmp_Notification[i]);
            j++;

        }
    }
}
