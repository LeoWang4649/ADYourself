package com.example.adyourself_fb;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationEboard extends AppCompatActivity
{
  private String A_usernow;
  private ImageButton btn_ad_tomainpage;
  private ImageButton btn_ad_toStarteBoardQuery;
  private Button btn_ad_toCheckOut;
  private Spinner spinner, spinner1;
  private DatabaseReference mDatabase,mDatabase1;
  private VideoView UploadVideo;
  private ImageView UploadImg;
  private String eBoardNum; //??????????????????
  private String eBoardArea; //??????????????????(??????)
  private String eBoardName; //??????????????????
  private String AreaImgUrl; //??????????????????URL
  private TextView MaxPeopleFlowView;
  private DatePickerDialog picker; //??????????????????Calendar
  private EditText eText; //??????User???????????????
  private String DateBySelected; //??????User???????????????????????????YYYYMMDD
  private String TimeBySelected; //??????User???????????????????????????PM10-12
  private Button btnGetDate; //??????User???????????????
  private Button btnChooseFile; //????????????
  private Button btnUploadFile; //????????????
  private String UserID; //??????email??????
  private int MaxDataCount; //??????Order Table???????????????
  private String UploadVideoURL; //???????????????Firebase Storage???URL
  private MediaController mediaControls;
  private String videoFileName;
  private long flowPrice;
  private TextView flowPriceView;
  private TextView TitleText;

  FirebaseDatabase database = FirebaseDatabase.getInstance();
  DatabaseReference myRef = database.getReference();

  // Uri indicates, where the image will be picked from
  private Uri filePath;

  // request code
  private final int PICK_IMAGE_REQUEST = 22;

  // instance for firebase storage and StorageReference
  FirebaseStorage storage;
  StorageReference storageReference;

  //?????????????????????Method????????????Bitmap
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

  // Select Image method
  private void SelectImage() {
    // Defining Implicit Intent to mobile gallery
    Intent intent = new Intent();
    intent.setType("video/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(
            Intent.createChooser(
                    intent,"Select Video from here..."), PICK_IMAGE_REQUEST);
  }

  // Override onActivityResult method
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    // checking request code and result code
    // if request code is PICK_IMAGE_REQUEST and resultCode is RESULT_OK
    // then set image in the image view
    if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
        && data.getData() != null) {
      // Get the Uri of data
      filePath = data.getData();
      Log.v("Get","filePath:"+filePath);
      if (mediaControls == null) {
        // create an object of media controller class
        mediaControls = new MediaController(ReservationEboard.this);
        mediaControls.setAnchorView(UploadVideo);
      }
      // set the media controller for video view
      UploadVideo.setMediaController(mediaControls);
      // set the uri for the video view
      //UploadVideo.setVideoURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/" +
      //  "adyourself-13ceb.appspot.com/o/" +
      //  "153655.mp4?alt=media&token=a4fd190b-b94a-4133-a332-3b09b1f2eb56"));

      //String fileName = "153655.mp4";
      //String completePath = Environment.getExternalStorageDirectory() + "/" + fileName;
      //File file = new File(completePath);
      //Uri videoUri = Uri.fromFile(file);
      //UploadVideo.setVideoURI(Uri.parse(String.valueOf(videoUri)));

      //UploadVideo.setVideoURI(Uri.parse("file:///sdcard/Download/153655.mp4"));
      //UploadVideo.setVideoURI(Uri.parse("file:///storage/emulated/0/Download/153655.mp4"));

      //????????????????????????Android Emulator??????????????????App?????????Storage???Permission???
      //Video File????????????Android Studio???Device File Explorer???????????????/sdcard/Download/????????????
      String filePath_str1 = String.valueOf(filePath);
      String filePath_str2 = filePath_str1.replace("content://com.android.providers." +
              "downloads.documents/document/raw%3A%2F","file:///");
      String VideoPath = filePath_str2.replace("%2F","/");
      //ex. VideoPath = file:///storage/Femulated/0/Download/153655.mp4
      int last_slash = VideoPath.lastIndexOf('/');
      videoFileName = VideoPath.substring(last_slash + 1); //ex. 153655.mp4
      Log.v("Get","videoFileName:"+videoFileName);

      UploadVideo.setVideoURI(Uri.parse(VideoPath));
      // Start to play the video
      UploadVideo.start();

      // implement on completion listener on video view
      UploadVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
          Toast.makeText(getApplicationContext(), "????????????",
                  Toast.LENGTH_LONG).show(); // display a toast when an video is completed
        }
      });
      UploadVideo.setOnErrorListener(new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
          Toast.makeText(getApplicationContext(), "?????????????????????",
                  Toast.LENGTH_LONG).show(); // display a toast when an error is occured while
                                             // playing an video
          return false;
        }
      });
    }
  }

  // UploadImage method
  private void uploadImage() {
    if (filePath != null) {
      // Code for showing progressDialog while uploading
      final ProgressDialog progressDialog = new ProgressDialog(this);
      progressDialog.setTitle("Uploading...");
      progressDialog.show();

      // Defining the child of storageReference
      //StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
      StorageReference ref = storageReference.child(videoFileName);
      UploadVideoURL = "https://firebasestorage.googleapis.com/v0/b/adyourself-13ceb.appspot.com/o/"
                       + videoFileName;
      // adding listeners on upload or failure of image
      ref.putFile(filePath)
         .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
             //Image uploaded successfully
             //Dismiss dialog
             progressDialog.dismiss();
             Toast.makeText(ReservationEboard.this,"?????????????????????",
                            Toast.LENGTH_SHORT).show();
           }
         })

         .addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
             // Error, Image not uploaded
             progressDialog.dismiss();
             Toast.makeText(ReservationEboard.this,"Failed " + e.getMessage(),
                             Toast.LENGTH_SHORT).show();
           }
         })
         .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
           //Progress Listener for loading
           //percentage on the dialog box
           @Override
           public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
             double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount());
             progressDialog.setMessage("Uploaded " + (int)progress + "%");
           }
         });
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.reservation_eboard);
    TitleText = (TextView) findViewById(R.id.textView8);

    //??????[????????????]??????(StartBoardQuery)?????????eBoardArea, eBoardNum, AreaImgUrl
    Bundle bundle = this.getIntent().getExtras();
    if (bundle != null) {
      eBoardNum = bundle.getString("ad_eBoardNum");
      eBoardArea= bundle.getString("ad_eBoardArea");
      eBoardName= bundle.getString("ad_eBoardName");
      AreaImgUrl= bundle.getString("ad_AreaImgUrl");
      UserID= bundle.getString("ad_UserID");
    }
    TitleText.setText("?????? (" + eBoardArea + ":" + eBoardName + ")");
    //FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("Order");

    //mDatabase = FirebaseDatabase.getInstance().getReference("Order").child("001");
    mDatabase = FirebaseDatabase.getInstance().getReference("Flow");
    spinner = (Spinner)findViewById(R.id.spinner_FlowTime);
    MaxPeopleFlowView = (TextView) findViewById(R.id.MaxPeopleFlow);
    btnGetDate=(Button)findViewById(R.id.button_GetDate);
    eText=(EditText) findViewById(R.id.editText1);
    eText.setInputType(InputType.TYPE_NULL);
    btnChooseFile=(Button)findViewById(R.id.button_ChooseFile);
    btnUploadFile=(Button)findViewById(R.id.button_UploadFile);
    UploadVideo = (VideoView) findViewById(R.id.UploadFileVideoView);
    flowPriceView = (TextView) findViewById(R.id.textView_flow_price);

    // get the Firebase  storage reference
    storage = FirebaseStorage.getInstance();
    storageReference = storage.getReference();

    // on pressing btnSelect SelectImage() is called
    btnChooseFile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        SelectImage();
      }
    });

    // on pressing btnUpload uploadImage() is called
    btnUploadFile.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        uploadImage();
      }
    });

    Query query = mDatabase.orderByChild("Time");
    query.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(final DataSnapshot dataSnapshot) {
        final List<String> TimeList = new ArrayList<String>(); //?????????????????????(eBoardNum)???????????????
        final List<String> NotBookTimeList = new ArrayList<String>(); //????????????????????????????????????
        final List<String> BookedTimeList = new ArrayList<String>(); //?????????????????????????????????
        int count1 = 0;
        //?????????????????????(eBoardNum)???????????????(In Table Flow)
        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
          String BoardValue = dataSnapshot1.child("Board").getValue(String.class);
          if(BoardValue.equals(eBoardNum)) {
            //???Board?????????????????????(Index)?????????????????????Flow, Time??????????????????????????????
            String IndexOfFlow = dataSnapshot1.getKey().toString();
            String TimeValue = dataSnapshot.child(IndexOfFlow).child("Time").getValue() + "";
            TimeList.add(TimeValue);
            Log.v("Get","count1:"+count1+" "+BoardValue+" "+TimeValue);
            count1++;
          }
          //---- ??????Begin???
          //Toast.makeText(StarteBoardQuery.this,"?????????: " + count + " " +
          //               BoardValue + " " + TimeValue + " " + PeopleFlowValue,
          //               Toast.LENGTH_LONG).show();
          //Log.v("Get",count+" "+BoardValue+" "+TimeValue+" "+PeopleFlowValue);
          //---- ??????End???
        }
        //User???Calendar???????????????
        eText.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(ReservationEboard.this,
                     new DatePickerDialog.OnDateSetListener() {
                       @Override
                       public void onDateSet(DatePicker view, int year, int monthOfYear,
                                            int dayOfMonth) {
                         eText.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                      }
                    }, year, month, day);
            picker.show();
          }
        });
        //??????User??????????????????
        btnGetDate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            DateBySelected = eText.getText().toString();
            Log.v("Get", "DateBySelected:"+DateBySelected);
            //????????????Table(Order)???????????????????????????????????????,??????????????????????????????????????????????????????List???,??????
            //??????????????????????????????????????????????????????????????????????????????
            mDatabase = FirebaseDatabase.getInstance().getReference("Order");
            //query1 = mDatabase.orderByChild("PeopleFlow").endAt(eBoardNum);
            Query query1 = mDatabase.orderByChild("BoardID");
            query1.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(final DataSnapshot dataSnapshot) {
                int count2 = 0;
                MaxDataCount = 0;
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                  String BoardValue = dataSnapshot1.child("BoardID").getValue(String.class);
                  if(BoardValue != null && !BoardValue.isEmpty()) {
                    if (BoardValue.equals(eBoardNum)) {
                      //???BoardID?????????????????????(Index)????????????????????????Date, Time??????????????????????????????
                      String IndexOfOrder = dataSnapshot1.getKey().toString();
                      String TimeValue = dataSnapshot.child(IndexOfOrder).child("Time").getValue() + "";
                      String DateValue = dataSnapshot.child(IndexOfOrder).child("Date").getValue() + "";
                      Log.v("Get", "BoardValue(1):" + BoardValue + " " + DateBySelected);
                      if (DateValue.equals(DateBySelected)) {
                        BookedTimeList.add(TimeValue);
                        Log.v("Get", "count2:" + count2 + " " + DateValue + " " + TimeValue);
                        count2++;
                      }
                    }
                  }
                  MaxDataCount++;
                }
                //?????????????????????(eBoardNum)?????????????????????????????????????????????????????????????????????
                int count3 = 0;
                for (String Time_v : TimeList) {
                  if (BookedTimeList.contains(Time_v)) { //?????????????????????????????????????????????
                  }
                  else { //????????????????????????
                    NotBookTimeList.add(Time_v);
                    Log.v("Get","count3:"+count3+" "+Time_v);
                    count3++;
                  }
                }
                //????????????ArrayAdapter???????????????????????????????????????????????????BoardName List
                ArrayAdapter<String> arrayAdapter1 =
                        new ArrayAdapter<String>(ReservationEboard.this,
                                android.R.layout.simple_spinner_item, NotBookTimeList);
                //???????????????????????????
                arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //?????????????????????????????????ArrayAdapter??????
                spinner.setAdapter(arrayAdapter1);
                //??????[????????????]????????????????????????
                spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                  @Override
                  public void onItemSelected(AdapterView adapterView, View view, int position,
                                             long id) {
                    TimeBySelected = adapterView.getSelectedItem().toString(); //????????????Time
                    Log.v("Get","TimeBySelected: " + TimeBySelected);
                    //---- ??????Begin?????????????????????????????????????????????????????????????????????Toast???
                    //Toast.makeText(ReservationEboard.this,
                    //        "??????????????????" + TimeBySelected, Toast.LENGTH_LONG).show();
                    //---- ??????End???
                    //???????????????????????????
                    mDatabase1 = FirebaseDatabase.getInstance().getReference("Flow");
                    //query1 = mDatabase.orderByChild("PeopleFlow").endAt(eBoardNum);
                    Query query1 = mDatabase1.orderByChild("Board");
                    query1.addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(final DataSnapshot dataSnapshot) {
                        //??????Firebase Table "Flow"??????????????????????????????
                        flowPrice = 0;
                        //Log.v("Get","MaxeBoardNum: "+eBoardNum);
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                          String Board_v = dataSnapshot1.child("Board").getValue(String.class);
                          long flowPrice_v = (long) dataSnapshot1.child("Price").getValue();
                          String Time_v = dataSnapshot1.child("Time").getValue(String.class);
                          Log.v("Get","(Board_v,flowPrice_v,Time_v):"+Board_v+","+
                                flowPrice_v+","+Time_v);
                          if(eBoardNum != null && !eBoardNum.isEmpty()) {
                            if(eBoardNum.equals(Board_v) && TimeBySelected.equals(Time_v)) {
                              flowPrice = flowPrice_v;
                              //Log.v("Get", "(Board_v,PeopleFlow_v): " + Board_v + "," + PeopleFlow_v);
                              flowPriceView.setText("" + flowPrice);
                            }
                          }
                        }
                      }
                      @Override
                      public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(ReservationEboard.this,databaseError.getMessage(),
                                Toast.LENGTH_LONG).show();
                      }
                    });
                  }
                  @Override
                  public void onNothingSelected(AdapterView arg0) {
                    Toast.makeText(ReservationEboard.this, "???????????????????????????",
                            Toast.LENGTH_LONG).show();
                  }
                });
              }
              @Override
              public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ReservationEboard.this,databaseError.getMessage(),
                        Toast.LENGTH_LONG).show();
              }
            });
          }
        });
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(ReservationEboard.this,databaseError.getMessage(),
          Toast.LENGTH_LONG).show();
      }
    });

    //????????????????????????
    btn_ad_toStarteBoardQuery = (ImageButton) findViewById(R.id.btn_ad_toStarteBoardQuery);
    btn_ad_toStarteBoardQuery.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
        /*
        Intent intent_ad_toStarteBoardQueryPage=new Intent(ReservationEboard.this,
                StarteBoardQuery.class);
        Bundle bundle = new Bundle();
        bundle.putString("eBoardNum", eBoardNum);
        bundle.putString("eBoardArea", eBoardArea);
        intent_ad_toStarteBoardQueryPage.putExtras(bundle);
        startActivity(intent_ad_toStarteBoardQueryPage);
        */
      }
    });





    //?????????????????????????????????????????????????????????
    final int[] userdatacheck = {0};
    final String[] split_line = UserID.split("@");

    ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        int datacount=(int) dataSnapshot.child("User").child("" + split_line[0]).child("Notification").getChildrenCount();//??????????????????????????????????????????;

        //????????????????????????
        userdatacheck[0] = datacount + 1;


      }
      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });

    //?????????????????????????????????????????????????????????
    final int[] userorderdatacheck = {0};

    ValueEventListener valueEventListener2 = myRef.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        int datacount2=(int) dataSnapshot.child("User").child("" + split_line[0]).child("Order").getChildrenCount();//??????????????????????????????????????????;

        //????????????????????????
        userorderdatacheck[0] = datacount2 + 1;


      }
      @Override
      public void onCancelled(DatabaseError databaseError) {

      }
    });


    //???????????????????????????
    btn_ad_toCheckOut = (Button) findViewById(R.id.btn_ad_toCheckOut);
    btn_ad_toCheckOut.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //???????????? Table(Order)
        Log.v("Get","MaxDataCount: " + MaxDataCount + " " + TimeBySelected + " "
                  + UserID);
        MaxDataCount++;
        String IndexID = String.valueOf(MaxDataCount);
        mDatabase.child(IndexID).child("AD").setValue(UploadVideoURL);
        mDatabase.child(IndexID).child("BoardID").setValue(eBoardNum);
        mDatabase.child(IndexID).child("Completion").setValue("No");
        mDatabase.child(IndexID).child("Date").setValue(DateBySelected);
        mDatabase.child(IndexID).child("OrderID").setValue("OID" + IndexID);
        mDatabase.child(IndexID).child("Pending").setValue("No");
        mDatabase.child(IndexID).child("Price").setValue(flowPrice);
        mDatabase.child(IndexID).child("Time").setValue(TimeBySelected);
        mDatabase.child(IndexID).child("User").setValue(split_line[0]);


        //????????????&?????????????????????
        myRef.child("User").child(split_line[0]).child("Notification").child(userdatacheck[0] + "").setValue("??????????????????:OID" + IndexID + "????????????????????????????????????");
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("AD").setValue(UploadVideoURL);
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("BoardID").setValue(eBoardNum);
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("Completion").setValue("No");
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("Date").setValue(DateBySelected);
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("OrderID").setValue("OID" + IndexID);
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("Pending").setValue("No");
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("Price").setValue(flowPrice);
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("Time").setValue(TimeBySelected);
        myRef.child("User").child(split_line[0]).child("Order").child(userorderdatacheck[0]+ "").child("User").setValue(split_line[0]);

        Toast.makeText(ReservationEboard.this,
                "??????????????????:OID" + IndexID+ "????????????????????????????????????",Toast.LENGTH_LONG).show();


        //???????????????
        Intent intent_ck_backtomainpage=new Intent(ReservationEboard.this,
          MainPage.class);
        //??????????????????????????????(??????)
        Bundle bundle = new Bundle();
        bundle.putString("nowuser", UserID);
        intent_ck_backtomainpage.putExtras(bundle);

        intent_ck_backtomainpage.putExtras(bundle);
        startActivity(intent_ck_backtomainpage);

      }
    });
  }
}
