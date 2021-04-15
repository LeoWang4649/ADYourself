package com.example.adyourself_fb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class QRCode extends AppCompatActivity {

    private String QR_usernow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code);

        SurfaceView surfaceView;
        final TextView textView;
        final CameraSource cameraSource;
        BarcodeDetector barcodeDetector;

        final String tmpQRBoardID[] = {"X"};//電子看板編號暫存
        final String tmpQRArea[] = {"Taipei"};//電子看板區域暫存
        final String tmpQRPhoto[] = {"https://firebasestorage.googleapis.com/v0/b/adyourself-13ceb.appspot.com/o/FlowTaipei.png?alt=media&token=e50d38e0-4df5-4ec9-852a-1a9d056e61e4"};//電子看板照片暫存

        final Button btn_to_EBoard;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();



        //接收登入or註冊頁面傳來的使用者資訊
        final Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            QR_usernow = bundle.getString("nowuser");
        }

        String[] split_line = QR_usernow.split("@");
        final String QRRR_Usernow = split_line[0];



        //QR
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        textView=(TextView)findViewById(R.id.tv_showqr);
        btn_to_EBoard = (Button)findViewById(R.id.btn_to_EBoard);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE).build();
        /*cameraSource=new CameraSource.Builder(this,barcodeDetector)
                .setRequestedPreviewSize(300,300).build();*/
        cameraSource = new CameraSource.Builder(this,barcodeDetector).setAutoFocusEnabled(true).build();

        //掃描器顯示
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback(){
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED)
                    return;
                try{
                    cameraSource.start(holder);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        //條碼判斷
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrCodes=detections.getDetectedItems();
                if(qrCodes.size()!=0){
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            //顯示電子看板編號
                            textView.setText(qrCodes.valueAt(0).displayValue);

                            //取得電子看板編號
                            tmpQRBoardID[0] = textView.getText().toString();
                        }
                    });
                }
            }
        });




        //前往嘎看板資訊頁面
        btn_to_EBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tmpQRBoardID[0] == "X"){
                    Toast.makeText(QRCode.this, "請先掃描電子看板上的QRCode！", Toast.LENGTH_SHORT).show();
                }else{

                    Intent intent_to_StarteBoardQuery=new Intent(QRCode.this,StarteBoardQuery.class);

                    ValueEventListener valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //取得所需資訊
                            tmpQRArea[0] = (dataSnapshot.child("Eboard").child(tmpQRBoardID[0]).child("Area").getValue()+"");
                            tmpQRPhoto[0] = (dataSnapshot.child("Eboard").child(tmpQRBoardID[0]).child("Photo").getValue()+"");
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //Bundle放入需要的資料
                    final Bundle bundle = new Bundle();
                    bundle.putString("ad_eBoardNum", tmpQRBoardID[0]);
                    bundle.putString("ad_eBoardArea", tmpQRArea[0]);
                    bundle.putString("ad_AreaImgUrl", tmpQRPhoto[0]);
                    bundle.putString("ad_UserID", QRRR_Usernow);
                    intent_to_StarteBoardQuery.putExtras(bundle);

                    startActivity(intent_to_StarteBoardQuery);

                }



            }
        });

    }

    private  void QRtest(final String account, final String pwd){

    }
}
