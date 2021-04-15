package com.example.adyourself_fb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoveEboard extends AppCompatActivity {
    private RecyclerView LoveEboard_recyclerView;
    private RecyclerView.Adapter LoveEboard_recyclerAdapter;
    private LoveEboard_data[] LoveEboard_Dataset;
    FirebaseDatabase mdatabase;
    DatabaseReference mRef;

    int datacount=0;
    String[] tmp_BoardID;//電子看板ID
    String[] tmp_Address;//電子看板地址
    String[] tmp_Area;//電子看板地區
    String[] tmp_BoardName;
    String[] tmp_Photo;
    String[] tmp_User = {"X"};

    private String U_usernow;

    private ImageButton btn_le_tomainpage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_eboard);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference("User");

        //接收登入or註冊頁面傳來的使用者資訊
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            U_usernow = bundle.getString("nowuser");
        }

        final String[] split_line = U_usernow.split("@");

        final String LE_Usernow = split_line[0];

        //跳轉回主頁面
        btn_le_tomainpage = (ImageButton) findViewById(R.id.btn_le_tomainpage);
        btn_le_tomainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_le_tomainpage=new Intent(LoveEboard.this,MainPage.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", LE_Usernow);

                intent_le_tomainpage.putExtras(bundle);

                startActivity(intent_le_tomainpage);
            }
        });


        LoveEboard_recyclerView = (RecyclerView)findViewById(R.id.loveeboard_recyclerView);
        ValueEventListener valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datacount=(int) dataSnapshot.child("" + LE_Usernow).child("LoveEboard").getChildrenCount();//獲得資料庫中我的最愛看板數量;

                tmp_BoardID= new String[datacount];
                for(int i=1;i<=dataSnapshot.child("" + LE_Usernow).child("LoveEboard").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + LE_Usernow).child("LoveEboard").child("" + i).child("BoardID").getValue() + "");
                    tmp_BoardID[i-1]=gametmp;
                }

                tmp_Area= new String[datacount];
                for(int i=1;i<=dataSnapshot.child("" + LE_Usernow).child("LoveEboard").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + LE_Usernow).child("LoveEboard").child("" + i).child("Area").getValue() + "");
                    tmp_Area[i-1]=gametmp;
                }

                tmp_Photo= new String[datacount];
                for(int i=1;i<=dataSnapshot.child("" + LE_Usernow).child("LoveEboard").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + LE_Usernow).child("LoveEboard").child("" + i).child("Photo").getValue() + "");
                    tmp_Photo[i-1]=gametmp;
                }

                tmp_User[0]=LE_Usernow;


                setRecyclerView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setRecyclerView(){
        LoveEboard_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //設置格線
        LoveEboard_recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(new MDGridRvDividerDecoration(getActivity()));
        setData();

        //設置格線
        //recyclerView.addItemDecoration(new MDGridRvDividerDecoration(this));

        LoveEboard_recyclerAdapter = new MyAdapterLoveEboard(LoveEboard_Dataset);
        LoveEboard_recyclerView.setAdapter(LoveEboard_recyclerAdapter);

    }
    public void setData(){
        LoveEboard_Dataset = new LoveEboard_data[datacount];
        int j=0;
        for (int i = datacount-1 ; i >=0 ;i--){

            LoveEboard_Dataset[j] = new LoveEboard_data(tmp_BoardID[i],tmp_Area[i],tmp_Photo[i],tmp_User[0]);
            j++;

        }
    }
}