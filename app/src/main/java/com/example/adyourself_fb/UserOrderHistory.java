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

public class UserOrderHistory extends AppCompatActivity {
    private RecyclerView UserOrderHistory_recyclerView;
    private RecyclerView.Adapter UserOrderHistory_recyclerAdapter;
    private UserOrderHistory_data[] UserOrderHistory_Dataset;
    FirebaseDatabase mdatabase;
    DatabaseReference mRef;

    int datacount=0;
    private String[] tmp_AD;//廣告內容
    private String[] tmp_BoardID;//放廣告的電子看板ID
    private String[] tmp_Date;//預約日期
    //String[] tmp_Pending;//廣告審核狀態
    private String[] tmp_Price;//此訂單價格
    private String[] tmp_Time;//預約時段
    private String[] tmp_User;//訂單下訂之使用者
    private String[] tmp_OrderID;//訂單編號

    private String[] tmp_Completion;//訂單完成狀態

    private String U_usernow;

    private ImageButton btn_UOH_backtomainpage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history);

        mdatabase = FirebaseDatabase.getInstance();
        mRef = mdatabase.getReference("User");

        //接收登入or註冊頁面傳來的使用者資訊
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            U_usernow = bundle.getString("nowuser");
        }

        final String[] split_line = U_usernow.split("@");

        final TextView UUU = findViewById(R.id.textViewUUU);
        final String UOH_Usernow = split_line[0];
        //UUU.setText(UOH_Usernow+"的訂單紀錄");

        //返回主頁面
        btn_UOH_backtomainpage = (ImageButton) findViewById(R.id.btn_UOH_backtomainpage);
        btn_UOH_backtomainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_UOHbckto_MainPage=new Intent(UserOrderHistory.this,MainPage.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", U_usernow);
                intent_UOHbckto_MainPage.putExtras(bundle);

                startActivity(intent_UOHbckto_MainPage);
            }
        });


        UserOrderHistory_recyclerView = (RecyclerView)findViewById(R.id.userorderhistory_recyclerView);
        ValueEventListener valueEventListener = mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                datacount=(int) dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount();//獲得資料庫中訂單的數量;

                //最上方顯示該使用者有幾筆訂單紀錄
                if (datacount == 0){
                    UUU.setText(UOH_Usernow+"，您目前還沒有任何訂單紀錄。");
                }else{
                    UUU.setText(UOH_Usernow+"，您目前有"+datacount+"筆訂單紀錄。");
                }

                //重新定義訂單數量
                tmp_AD= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(UOH_Usernow+"").child("Order").child("" + i).child("AD").getValue() + "");
                    tmp_AD[i-1]=gametmp;
                }

                tmp_BoardID= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(UOH_Usernow+"").child("Order").child("" + i).child("BoardID").getValue() + "");
                    tmp_BoardID[i-1]=gametmp;
                }

                tmp_Date= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(UOH_Usernow+"").child("Order").child("" + i).child("Date").getValue() + "");
                    tmp_Date[i-1]=gametmp;
                }

                tmp_Completion= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(UOH_Usernow+"").child("Order").child("" + i).child("Completion").getValue() + "");
                    tmp_Completion[i-1]=(gametmp);
                }

                tmp_Price= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(UOH_Usernow+"").child("Order").child("" + i).child("Price").getValue() + "");
                    tmp_Price[i-1]=(gametmp);
                }

                tmp_Time= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(UOH_Usernow+"").child("Order").child("" + i).child("Time").getValue() + "");
                    tmp_Time[i-1]=gametmp;
                }

                tmp_User= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(UOH_Usernow+"").child("Order").child("" + i).child("User").getValue() + "");
                    tmp_User[i-1]=gametmp;
                }

                tmp_OrderID= new String[datacount];
                for(int i=1;i<=dataSnapshot.child(UOH_Usernow+"").child("Order").getChildrenCount(); i++){
                    String gametmp=(dataSnapshot.child(UOH_Usernow+"").child("Order").child("" + i).child("OrderID").getValue() + "");
                    tmp_OrderID[i-1]=gametmp;
                }

                setRecyclerView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void setRecyclerView(){
        UserOrderHistory_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //設置格線
        UserOrderHistory_recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(new MDGridRvDividerDecoration(getActivity()));
        setData();

        //設置格線
        //recyclerView.addItemDecoration(new MDGridRvDividerDecoration(this));

        UserOrderHistory_recyclerAdapter = new MyAdapteruserorderhistory(UserOrderHistory_Dataset);
        UserOrderHistory_recyclerView.setAdapter(UserOrderHistory_recyclerAdapter);

    }
    public void setData(){
        UserOrderHistory_Dataset = new UserOrderHistory_data[datacount];
        int j=0;
        for (int i = datacount-1 ; i >=0 ;i--){

            UserOrderHistory_Dataset[j] = new UserOrderHistory_data(tmp_AD[i],tmp_BoardID[i],tmp_Date[i],tmp_Completion[i],tmp_Price[i],tmp_Time[i],tmp_User[i],tmp_OrderID[i]);
            j++;

        }
    }
}
