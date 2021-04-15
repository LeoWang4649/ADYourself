package com.example.adyourself_fb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserOrderHistoryDetail extends AppCompatActivity {

    private ImageButton btn_uohd_tomainpage;

    private Button btn_checkAD;

    //資料庫
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history_detail);

        //取從待審核訂單獲得的資料
        Bundle bundleclass =this.getIntent().getExtras();
        final String D_AD = bundleclass.getString("t_AD");
        String D_BoardID = bundleclass.getString("t_BoardID");
        String D_Date = bundleclass.getString("t_Date");
        String D_Completion = bundleclass.getString("t_Completion");
        String D_Price = bundleclass.getString("t_Price");
        String D_Time = bundleclass.getString("t_Time");
        final String D_User = bundleclass.getString("t_User");
        String D_OrderID = bundleclass.getString("t_OrderID");

        //顯示在頁面上
        Button btn_checkAD = findViewById(R.id.btn_checkAD2);
        //TextView TV_AD = findViewById(R.id.UDetail_AD);
        TextView TV_BoardID = findViewById(R.id.UDetail_Board);
        TextView TV_Date = findViewById(R.id.UDetail_Date);
        TextView TV_Completion = findViewById(R.id.UDetail_Pending);
        TextView TV_Price = findViewById(R.id.UDetail_Price);
        TextView TV_Time = findViewById(R.id.UDetail_Time);
        TextView TV_User = findViewById(R.id.UDetail_User);
        TextView TV_OrderID = findViewById(R.id.UDetail_OrderID);

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
        TV_Completion.setText(""+D_Completion);
        TV_Price.setText(""+D_Price);
        TV_Time.setText(""+D_Time);
        TV_User.setText(""+D_User);
        TV_OrderID.setText("訂單編號:"+D_OrderID);

        //跳轉回主頁面
        btn_uohd_tomainpage = (ImageButton) findViewById(R.id.btn_uohd_tomainpage);
        btn_uohd_tomainpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_uohd_tomainpage=new Intent(UserOrderHistoryDetail.this,MainPage.class);

                Bundle bundle = new Bundle();
                bundle.putString("nowuser", D_User);

                intent_uohd_tomainpage.putExtras(bundle);

                startActivity(intent_uohd_tomainpage);
            }
        });
    }
}
