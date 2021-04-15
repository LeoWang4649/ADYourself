package com.example.adyourself_fb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StarteBoardQuery extends AppCompatActivity {
  private ImageButton btn_ad_toQueryEboardPage;
  private Button btn_ad_toReservationEboard;
  private Button btn_addMyFavorite;
  private String eBoardNum; //電子看板編號
  private String eBoardName; //電子看板名稱
  private String eBoardArea; //電子看板地區
  private String AreaImgUrl; //電子看板圖片URL
  private String UserID; //登入email帳號

  private DatabaseReference mDatabase,mDatabase1;
  private ImageView AreaImg; //頁面上方的圖片
  private TextView TitleText;
  private int MaxCount = 5; //Bar Chart只顯示Flow最高的5筆資料

  //讀取網路圖片的Method，型態為Bitmap
  private static Bitmap getBitmapFromURL(String imageUrl) {
    try {
      URL url = new URL(imageUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoInput(true);
      connection.connect();
      InputStream input = connection.getInputStream();
      Bitmap bitmap = BitmapFactory.decodeStream(input);
      return bitmap;
    }
    catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.start_eboard_query);
    AreaImg = (ImageView) findViewById(R.id.AreaImageView);
    TitleText = (TextView) findViewById(R.id.textView8);
    btn_addMyFavorite=(Button)findViewById(R.id.button_addMyFavorite);

    //接收查詢頁面(QueryEboard)傳來的eBoardArea, eBoardNum
    Bundle bundle = this.getIntent().getExtras();
    if (bundle != null) {
      eBoardNum = bundle.getString("ad_eBoardNum");
      eBoardName = bundle.getString("ad_eBoardName");
      eBoardArea= bundle.getString("ad_eBoardArea");
      AreaImgUrl= bundle.getString("ad_AreaImgUrl");
      UserID= bundle.getString("ad_UserID");
    }
    TitleText.setText("查詢結果 (" + eBoardArea + ":" + eBoardName + ")");
    //讀取網路圖片URL並顯示圖片: 建立一個AsyncTask多執行緒進行圖片讀取動作，並帶入圖片連結網址。
    //多執行緒目的: 在Android 4.0版本之後，為了防止在與網路串接溝通時造成程式整個停滯，卡住在網
    //路溝通裡而無法繼續動作，所以4.0之後版本在與網路進行溝通時必須要建立一個多執行緒來進行網路
    //讀取資料而不影響到程式其他的運作。
    new AsyncTask<String, Void, Bitmap>() {
      @Override
      protected Bitmap doInBackground(String... params) {
        String url = params[0];
        return getBitmapFromURL(url);
      }
      @Override
      protected void onPostExecute(Bitmap result) {
        AreaImg.setImageBitmap (result);
        super.onPostExecute(result);
      }
    }.execute(AreaImgUrl);

    //---讀取資料庫Firebase Table Flow的資料並顯示長條圖 Begin:
    mDatabase = FirebaseDatabase.getInstance().getReference("Flow");
    //Query query = mDatabase.orderByChild("PeopleFlow").equalTo(eBoardNum,"Board");
    //Firebase only supports Ascending
    //Query query = mDatabase.orderByChild("PeopleFlow").limitToLast(5);
    //Query query = mDatabase.orderByChild("PeopleFlow").equalTo(eBoardNum,"Board");
    Query query = mDatabase.orderByChild("PeopleFlow"); //Only Have The Ascending Sort
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(final DataSnapshot dataSnapshot) {
        //宣告List用來存放來自Firebase Table "Flow"的資料
        final List<String> BoardList = new ArrayList<String>();
        final List<Long> PeopleFlowList = new ArrayList<Long>();
        final List<String> TimeList = new ArrayList<String>();
        final ArrayList<BarEntry> NoOfFlow = new ArrayList(); //儲存長條圖的資料
        final List<String> xAxisLabel = new ArrayList<String>(); //X-Axis Labels
        final List<String> Time_PeopleFlow_List = new ArrayList<String>();

        //讀取Firebase Table "Flow"的資料並放入List中
        int count = 0;
        xAxisLabel.add("");//[0]為原點座標，Label是空白
        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
          String BoardValue = dataSnapshot1.child("Board").getValue(String.class);
          if(BoardValue.equals(eBoardNum)) {
            BoardList.add(BoardValue);
            //將Board欄位的上層資料(Index)暫存，之後讀取Flow, Time欄位內容時需要用到。
            String IndexOfFlow = dataSnapshot1.getKey().toString();
            String TimeValue = dataSnapshot.child(IndexOfFlow).child("Time").getValue() + "";
            TimeList.add(TimeValue);
            Long PeopleFlowValue = (long) dataSnapshot.child(IndexOfFlow).
                              child("PeopleFlow").getValue();
            PeopleFlowList.add(PeopleFlowValue);
            Log.v("Get",count+" "+BoardValue+" "+TimeValue+" "+PeopleFlowValue);
            count++;
          }
          //---- 測試Begin：
          //Toast.makeText(StarteBoardQuery.this,"您選擇: " + count + " " +
          //               BoardValue + " " + TimeValue + " " + PeopleFlowValue,
          //               Toast.LENGTH_LONG).show();
          //Log.v("Get",count+" "+BoardValue+" "+TimeValue+" "+PeopleFlowValue);
          //---- 測試End。
        }
        if(count != 0 ) {
          int TempCount = count-1; //ArrayList的Index從0開始
          count = 0; //用來記錄之後實際要顯示的資料筆數
          //從PeopleFlowList的後面取出資料(有最大的PeopleFlow值)並搭配TimeList內容放入List中
          for(int i = 1; i <= MaxCount; i++) {
            if(TempCount >= 0) {
              String PairValue = TimeList.get(TempCount) + ":" +
                                 PeopleFlowList.get(TempCount).toString();
              //Ex. PairValue = PM08-09:700, PM11-12:500
              Log.v("Get",PairValue);
              Time_PeopleFlow_List.add(PairValue);
              TempCount--;
              count++;
            }
            else { i = MaxCount + 1; }
          }
          //依據Time由小到大將Time_PeopleFlow_List內容排序，
          Collections.sort(Time_PeopleFlow_List, new Comparator<Object>(){
            public int compare( Object l1, Object l2 )
            {
              // 回傳值: -1 前者比後者小, 0 前者與後者相同, 1 前者比後者大
              return l1.toString().toLowerCase().compareTo(l2.toString().toLowerCase());
            }
          });
          for(int i = 1; i <= count; i++) {
            String Time_PeopleFlow_v = Time_PeopleFlow_List.get(i-1);
            String[] split_line = Time_PeopleFlow_v.split(":");
            String Time_v = split_line[0];
            int PeopleFlow_v = Integer.valueOf(split_line[1]);
            NoOfFlow.add(new BarEntry(i, PeopleFlow_v,Time_v));
            //NoOfFlow.add(new BarEntry(count, PeopleFlowValue));
            xAxisLabel.add(Time_v);
          }

          //---準備顯示長條圖(Bar Chart) Begin
          BarChart mChart = (BarChart) findViewById(R.id.barChart);
          BarDataSet bardataset = new BarDataSet(NoOfFlow, "Flow Count");

          //---設定X軸與Y軸的顯示參數 Begin
          Legend L = mChart.getLegend();
          Description desc = mChart.getDescription();
          desc.setText(""); //將Chart圖右下角的文字"Description"移除。
          L.setEnabled(true);

          YAxis leftAxis = mChart.getAxisLeft();
          YAxis rightAxis = mChart.getAxisRight();
          XAxis xAxis = mChart.getXAxis();

          xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
          xAxis.setTextSize(12f);
          xAxis.setDrawAxisLine(true);
          xAxis.setDrawGridLines(false);
          xAxis.setLabelCount(count); //設定X軸標籤數量

          leftAxis.setTextSize(13f);
          leftAxis.setDrawLabels(false);
          leftAxis.setDrawAxisLine(false);
          leftAxis.setDrawGridLines(false);

          rightAxis.setDrawAxisLine(false);
          rightAxis.setDrawGridLines(false);
          rightAxis.setDrawLabels(false);
          //---設定X軸與Y軸的顯示參數 End

          bardataset.setBarBorderWidth(0);
          int[] colors = {Color.rgb(153, 193, 12),
                  Color.rgb(179, 130, 76)}; //Bar Chart有2種顏色交替使用
          bardataset.setColors(colors);
          bardataset.setValueTextSize(13f);

          BarData data = new BarData(bardataset);
          mChart.setData(data); //在畫面上顯示長條圖
          mChart.setDescription(desc);

          //---將X軸上的數字換成Time時段 Begin
          //xAxis.setCenterAxisLabels(true); // center labels over groups
          // Set the value formatter
          xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel));

          /* 下面的方法會造成App Crash，因為ArrayList的Out of Index存取
          xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
              return NoOfFlow.get((int)value).getData().toString();
            }
          });
          */
          //保証Y軸左邊由 0 開始
          //YAxis leftYAxis = mChart.getAxisLeft();
          //leftYAxis.setAxisMinimum(0);
          //leftYAxis.setYOffset(5);

          //保証Y軸右邊由 0 開始
          //YAxis rightYAxis = mChart.getAxisRight();
          //rightYAxis.setAxisMinimum(0);

          //---將X軸上的數字換成Time時段 End
          mChart.invalidate(); //refresh
          //---準備顯示長條圖(Bar Chart) End
        }
        else {
          Toast.makeText(StarteBoardQuery.this,"找不到可用資料",
                         Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(StarteBoardQuery.this,databaseError.getMessage(),
                Toast.LENGTH_LONG).show();
      }
    });
    //---讀取資料庫Firebase Table Flow的資料並顯示長條圖 End

    //加入我的最愛
    btn_addMyFavorite.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String[] split_line = UserID.split("@");
        final String userName = split_line[0];
        mDatabase1 = FirebaseDatabase.getInstance().getReference("User");
        //query1 = mDatabase.orderByChild("PeopleFlow").endAt(eBoardNum);
        Query query1 = mDatabase1.child(userName).child("LoveEboard").orderByChild("BoardID");
        query1.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(final DataSnapshot dataSnapshot) {
            //讀取Firebase Table "User"的資料，如果這個電子看板號碼(eBoardNum)不存在，則進行Insert
            int FindBoardID = 0, totalCount=0;
            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
              String BoardID_v = dataSnapshot1.child("BoardID").getValue(String.class);
              Log.v("Get","BoardID_v:" + BoardID_v);
              if(eBoardNum != null && !eBoardNum.isEmpty()) {
                if(eBoardNum.equals(BoardID_v)) {
                  FindBoardID = 1;
                  Log.v("Get", "FindBoardID:" + FindBoardID);
                }
              }
              totalCount++;
            }
            if(FindBoardID == 0) {
              //將此電子看板號碼(eBoardNum)寫入到此User的LoveEboard Table
              totalCount++;
              mDatabase1.child(userName).child("LoveEboard").child(String.valueOf(totalCount)).
                         child("BoardID").setValue(eBoardNum);
              mDatabase1.child(userName).child("LoveEboard").child(String.valueOf(totalCount)).
                      child("Area").setValue(eBoardArea);
              mDatabase1.child(userName).child("LoveEboard").child(String.valueOf(totalCount)).
                      child("BoardName").setValue(eBoardName);
              mDatabase1.child(userName).child("LoveEboard").child(String.valueOf(totalCount)).
                      child("Photo").setValue(AreaImgUrl);
              Toast.makeText(StarteBoardQuery.this,
                      "已將此電子看板"+eBoardNum+"加到我的最愛看板中",
                      Toast.LENGTH_LONG).show();
            }
          }
          @Override
          public void onCancelled(DatabaseError databaseError) {
            Toast.makeText(StarteBoardQuery.this,databaseError.getMessage(),
                    Toast.LENGTH_LONG).show();
          }
        });
      }
    });

    //跳轉回查詢面
    btn_ad_toQueryEboardPage = (ImageButton) findViewById(R.id.btn_ad_toQueryEboardPage);
    btn_ad_toQueryEboardPage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent_ad_toQueryEboardPage=new Intent(StarteBoardQuery.this,
          QueryEboard.class);
        Bundle bundle = new Bundle();
        bundle.putString("eBoardNum", eBoardNum);
        bundle.putString("eBoardArea", eBoardArea);
        intent_ad_toQueryEboardPage.putExtras(bundle);
        startActivity(intent_ad_toQueryEboardPage);
      }
    });

    //跳轉到預約頁面
    btn_ad_toReservationEboard = (Button) findViewById(R.id.btn_ad_toReservationEboard);
    btn_ad_toReservationEboard.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent_to_StarteBoardQuery=new Intent(StarteBoardQuery.this,
                ReservationEboard.class);
        Bundle bundle = new Bundle();
        bundle.putString("ad_eBoardArea", eBoardArea);
        bundle.putString("ad_eBoardNum", eBoardNum);
        bundle.putString("ad_eBoardName", eBoardName);
        bundle.putString("ad_AreaImgUrl", AreaImgUrl);
        bundle.putString("ad_UserID", UserID);
        intent_to_StarteBoardQuery.putExtras(bundle);
        startActivity(intent_to_StarteBoardQuery);
      }
    });


  }
}