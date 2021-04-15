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

public class AdminMainPage extends AppCompatActivity {
    private RecyclerView Adminorder_recyclerView;
    private RecyclerView.Adapter Adminorder_recyclerAdapter;
    private Adminorder_data[] Adminorder_Dataset;
    FirebaseDatabase mdatabase;
    DatabaseReference mRef;

    private ImageButton btn_adminlogout;

    int datacount=0;
    String[] tmp_AD;//廣告內容
    String[] tmp_BoardID;//放廣告的電子看板ID
    String[] tmp_Date;//預約日期
    String[] tmp_Pending;//廣告審核狀態
    String[] tmp_Price;//此訂單價格
    String[] tmp_Time;//預約時段
    String[] tmp_User;//訂單下訂之使用者
    String[] tmp_OrderID;//訂單編號

    int[] tmp_OrderNum; //訂單順序號碼(之後修改審核狀態需要用到這個)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference("Order");


        //管理者登出，跳轉回登入畫面
        btn_adminlogout = (ImageButton) findViewById(R.id.btn_adminlogout);
        btn_adminlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_bckto_MainAcitiviy=new Intent(AdminMainPage.this,MainActivity.class);
                startActivity(intent_bckto_MainAcitiviy);
            }
        });


        Adminorder_recyclerView = (RecyclerView)findViewById(R.id.adminorder_recyclerView);
        ValueEventListener valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datacount=(int) dataSnapshot.getChildrenCount();//獲得資料庫中訂單的數量;
                //重新定義訂單數量
                tmp_AD= new String[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + i).child("AD").getValue() + "");
                    tmp_AD[i-1]=gametmp;
                }

                tmp_BoardID= new String[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + i).child("BoardID").getValue() + "");
                    tmp_BoardID[i-1]=gametmp;
                }

                tmp_Date= new String[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + i).child("Date").getValue() + "");
                    tmp_Date[i-1]=gametmp;
                }

                tmp_Pending= new String[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + i).child("Pending").getValue() + "");
                    tmp_Pending[i-1]=(gametmp);
                }

                tmp_Price= new String[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + i).child("Price").getValue() + "");
                    tmp_Price[i-1]=(gametmp);
                }

                tmp_Time= new String[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + i).child("Time").getValue() + "");
                    tmp_Time[i-1]=gametmp;
                }

                tmp_User= new String[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + i).child("User").getValue() + "");
                    tmp_User[i-1]=gametmp;
                }

                tmp_OrderID= new String[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child("" + i).child("OrderID").getValue() + "");
                    tmp_OrderID[i-1]=gametmp;
                }

                tmp_OrderNum= new int[datacount];
                for(int i=1;i<=dataSnapshot.getChildrenCount(); i++){
                    tmp_OrderNum[i-1]=i;
                }

                setRecyclerView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void setRecyclerView(){
        Adminorder_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //設置格線
        Adminorder_recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(new MDGridRvDividerDecoration(getActivity()));
        setData();

        //設置格線
        //recyclerView.addItemDecoration(new MDGridRvDividerDecoration(this));

        Adminorder_recyclerAdapter = new MyAdapteradminorder(Adminorder_Dataset);
        Adminorder_recyclerView.setAdapter(Adminorder_recyclerAdapter);

    }
    public void setData(){
        Adminorder_Dataset = new Adminorder_data[datacount];
        int j=0;
        for (int i = datacount-1 ; i >=0 ;i--){

            Adminorder_Dataset[j] = new Adminorder_data(tmp_AD[i],tmp_BoardID[i],tmp_Date[i],tmp_Pending[i],tmp_Price[i],tmp_Time[i],tmp_User[i],tmp_OrderID[i],tmp_OrderNum[i]);
            j++;

        }
    }
}
