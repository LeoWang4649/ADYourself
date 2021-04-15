package com.example.adyourself_fb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.LinkedHashSet;
import java.util.List;

public class QueryEboard extends AppCompatActivity
{
  private String A_usernow;
  private ImageButton btn_ad_tomainpage;
  private Button btn_ad_toStarteBoardQuery;
  private Spinner spinner, spinner1;
  private DatabaseReference mDatabase;
  private ImageView AreaImg;
  private String eBoardNum; //電子看板編號
  private String eBoardArea; //電子看板地區(城市)
  private String eBoardName; //電子看板名稱
  private String AreaImgUrl; //電子看板圖片URL
  private TextView MaxPeopleFlowView;
  private String UserID; //登入email帳號

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
    setContentView(R.layout.query_eboard);

    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("Eboard");

    //mDatabase = FirebaseDatabase.getInstance().getReference("Eboard").child("001");
    mDatabase = FirebaseDatabase.getInstance().getReference("Eboard");
    spinner = (Spinner)findViewById(R.id.spinner_area);
    spinner1 = (Spinner)findViewById(R.id.spinner_BoardName);
    AreaImg = (ImageView) findViewById(R.id.AreaImageView);
    MaxPeopleFlowView = (TextView) findViewById(R.id.MaxPeopleFlow);

    Query query = mDatabase.orderByChild("Area");
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(final DataSnapshot dataSnapshot) {
        //宣告3個List用來存放來自Firebase Table "Eboard"的資料
        final List<String> AreaList = new ArrayList<String>();
        //final List<String> NumList = new ArrayList<String>();
        final List<String> BoardNameList = new ArrayList<String>();
        final List<String> eBoardDataList = new ArrayList<String>();
        //讀取Firebase Table "Eboard"的資料並放入List中
        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
          String AreaName = dataSnapshot1.child("Area").getValue(String.class);
          //AreaList.add(AreaName + dataSnapshot1.getKey().toString());
          AreaList.add(AreaName);
          //將Area的上層資料儲存在這個ArrayList上，之後讀取圖片URL(Photo)時需要用到。
          //NumList.add(dataSnapshot1.getKey().toString());
          String BoardNum = dataSnapshot1.getKey().toString();
          String BoardName = dataSnapshot1.child("BoardName").getValue(String.class);
          eBoardDataList.add(AreaName + ":" + BoardName + ":" + BoardNum);
          //Toast.makeText(QueryEboard.this,
          // "您選擇"+AreaName + ":" + BoardName + ":" + BoardNum, Toast.LENGTH_LONG).show();
        }
        //因為一個Area(城市)可能會有多個BoardName(看板名稱)，故一個Area(城市)可能會有多筆資料
        //要移除重複
        LinkedHashSet<String> hashSet = new LinkedHashSet<>(AreaList);
        ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);

        //建立一個ArrayAdapter物件以放置下拉選單的內容，內容來自Area List
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(QueryEboard.this,
          android.R.layout.simple_spinner_item, listWithoutDuplicates);
        //設定下拉選單的樣式
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //將下拉選單的內容設定為ArrayAdapter物件
        spinner.setAdapter(arrayAdapter);

        //設定Area項目被選取之後的動作
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
            //---- 測試Begin：下拉式選單選取某個項目後會出現提示文字，使用Toast。
            //Toast.makeText(QueryEboard.this,
            // "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            //String EboardNum = dataSnapshot.getKey(); //Return Value = Eboard
            //String EboardNum = String.valueOf(query2.hashCode());
            //Log.v("Get",EboardNum);
            //DatabaseReference EboardNum = dataSnapshot.child("Hsinchu").getRef();
            //String EboardNum = dataSnapshot.child(adapterView.getSelectedItem().toString()).
            //                                      getKey();
            //String ImageUrl = dataSnapshot.child(EboardNum).child("Photo").getValue() + "";
            //---- 測試End。
            eBoardArea = adapterView.getSelectedItem().toString(); //選取到的Area
            Log.v("Get","eBoardArea: "+eBoardArea);
            //依據選取到的eBoardArea(城市名稱)到eBoardDataList找出對應的BoardName(看板名稱)
            BoardNameList.clear(); //清空Board Name List內容
            for (String value : eBoardDataList) {
              //Log.v("Get","BoardName: "+value);
              String[] split_line = value.split(":");
              String Area_v = split_line[0];
              //Log.v("Get","Area_v: "+Area_v);
              if(eBoardArea.equals(Area_v)) {
                BoardNameList.add(split_line[1]); //Area名稱相同時，將BoardName存放到List中
                //Log.v("Get","BoardName: "+split_line[1]);
              }
            }
            //下一個Spinner的ArrayAdapter必須放在此處，否則下一個Spinner無法運作
            //建立一個ArrayAdapter物件以放置下拉選單的內容，內容來自BoardName List
            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(QueryEboard.this,
                    android.R.layout.simple_spinner_item, BoardNameList);
            //設定下拉選單的樣式
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //將下拉選單的內容設定為ArrayAdapter物件
            spinner1.setAdapter(arrayAdapter1);
          }
          @Override
          public void onNothingSelected(AdapterView arg0) {
            Toast.makeText(QueryEboard.this, "您沒有選擇任何項目",
              Toast.LENGTH_LONG).show();
          }
        });

        //設定BoardName項目被選取之後的動作
        spinner1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
            //---- 測試Begin：下拉式選單選取某個項目後會出現提示文字，使用Toast。
            //Toast.makeText(QueryEboard.this, "您選擇："+Area, Toast.LENGTH_LONG).show();
            //---- 測試End。
            eBoardName = adapterView.getSelectedItem().toString(); //選取到的Board Name
            Log.v("Get","eBoardName: "+eBoardName);
            //依據選取到的eBoardArea(城市名稱)及BoardName(看板名稱)找出對應的Num並取出Photo內容(URL)
            for (String value : eBoardDataList) {
              String[] split_line = value.split(":");
              String Area_v = split_line[0];
              String BoardName_v = split_line[1];
              //Log.v("Get","(Area_v,BoardName_v): "+Area_v+","+BoardName_v);
              if(eBoardArea.equals(Area_v) && eBoardName.equals(BoardName_v)) {
                //Area及BoardName相同時，將Num存放到List中
                eBoardNum = split_line[2];
                Log.v("Get","eBoardNum: "+eBoardNum);
                AreaImgUrl = (dataSnapshot.child(split_line[2]).child("Photo").getValue() + "");
                //Toast.makeText(QueryEboard.this, "URL="+AreaImgUrl, Toast.LENGTH_LONG).show();
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
                //.execute範例: .execute("https://firebasestorage.googleapis.com/v0/b/"+
                //                       "adyourself-13ceb.appspot.com/o/FlowHsinchu.png?"+
                //                       "alt=media&token=50853b5d-1342-4c08-a12e-098e50816362")

                //顯示這個電子看板編號的最高人流數量
                mDatabase = FirebaseDatabase.getInstance().getReference("Flow");
                //query1 = mDatabase.orderByChild("PeopleFlow").endAt(eBoardNum);
                Query query1 = mDatabase.orderByChild("PeopleFlow");
                query1.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(final DataSnapshot dataSnapshot) {
                    //讀取Firebase Table "Flow"的資料並顯示在畫面上
                    long MaxPeopleFlow = 0L;
                    //Log.v("Get","MaxeBoardNum: "+eBoardNum);
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                      String Board_v = dataSnapshot1.child("Board").getValue(String.class);
                      long PeopleFlow_v = (long) dataSnapshot1.child("PeopleFlow").getValue();
                      //Log.v("Get","(eBoardNum,Board_v,PeopleFlow_v): "+eBoardNum+","+Board_v+","+PeopleFlow_v);
                      if(eBoardNum != null && !eBoardNum.isEmpty()) {
                        if(eBoardNum.equals(Board_v)) {
                          MaxPeopleFlow = PeopleFlow_v;
                          //Log.v("Get", "(Board_v,PeopleFlow_v): " + Board_v + "," + PeopleFlow_v);
                        }
                      }
                    }
                    MaxPeopleFlowView.setText("此電子看板的最高人流數：" + MaxPeopleFlow);
                  }
                  @Override
                  public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(QueryEboard.this,databaseError.getMessage(),
                            Toast.LENGTH_LONG).show();
                  }
                });
              }
            }
          }
          @Override
          public void onNothingSelected(AdapterView arg0) {
            Toast.makeText(QueryEboard.this, "您沒有選擇任何項目",
                    Toast.LENGTH_LONG).show();
          }
        });
      }
      @Override
      public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(QueryEboard.this,databaseError.getMessage(),
          Toast.LENGTH_LONG).show();
      }
    });

    //接收登入or註冊頁面傳來的使用者資訊
    Bundle bundle = this.getIntent().getExtras();
    if (bundle != null) {
          A_usernow = bundle.getString("nowuser");
    }
    UserID = A_usernow;
    String[] split_line = A_usernow.split("@");
    final String AD_Usernow = split_line[0];

    //跳轉回主頁面
    btn_ad_tomainpage = (ImageButton) findViewById(R.id.btn_ad_tomainpage);
    btn_ad_tomainpage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent_ad_tomainpage=new Intent(QueryEboard.this,MainPage.class);
        Bundle bundle = new Bundle();
        bundle.putString("nowuser", AD_Usernow);
        intent_ad_tomainpage.putExtras(bundle);
        startActivity(intent_ad_tomainpage);
      }
    });

    //跳轉到開始查詢頁面
    btn_ad_toStarteBoardQuery = (Button) findViewById(R.id.btn_ad_toStarteBoardQuery);
    btn_ad_toStarteBoardQuery.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
      Intent intent_to_StarteBoardQuery=new Intent(QueryEboard.this,
        StarteBoardQuery.class);
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
