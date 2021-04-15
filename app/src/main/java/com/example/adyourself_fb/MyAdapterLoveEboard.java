package com.example.adyourself_fb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyAdapterLoveEboard extends RecyclerView.Adapter<MyAdapterLoveEboard.ViewHolder> {

    //private String[] Dataset;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User");



    private LoveEboard_data[] LoveEboard_Dataset;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tmp_BoardID,tmp_Area;
        //public ImageView Gamepic;
        public ViewHolder(View v) {
            super(v);

            tmp_BoardID =(TextView) v.findViewById(R.id.LEtmp_BoardID);
            tmp_Area =(TextView) v.findViewById(R.id.LEtmp_Area);
        }

    }


    public MyAdapterLoveEboard(LoveEboard_data[] myDataset) {
        LoveEboard_Dataset = myDataset;
    }
    @Override
    public MyAdapterLoveEboard.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclefor_loveeboard, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        String ttmp_BoardID=LoveEboard_Dataset[position].gettmp_BoardID();
        String ttmp_Area=LoveEboard_Dataset[position].gettmp_Area();
        String ttmp_Photo=LoveEboard_Dataset[position].gettmp_Photo();
        String ttmp_User=LoveEboard_Dataset[position].gettmp_User();

        holder.tmp_BoardID.setText("電子看板編號:" + ttmp_BoardID);
        holder.tmp_Area.setText("電子看板所在地區:" + ttmp_Area);

        //前往該看板資訊頁面
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String t_BoardID = LoveEboard_Dataset[position].gettmp_BoardID();
                String t_Area = LoveEboard_Dataset[position].gettmp_Area();
                String t_Photo = LoveEboard_Dataset[position].gettmp_Photo();
                String t_User = LoveEboard_Dataset[position].gettmp_User();

                Context context = view.getContext();
                Intent intent = new Intent();
                intent.setClass(context,StarteBoardQuery.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("ad_eBoardNum",t_BoardID);
                bundle.putString("ad_eBoardArea",t_Area);
                bundle.putString("ad_AreaImgUrl",t_Photo);
                bundle.putString("ad_UserID",t_User);

                //將Bundle物件assign給intent
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return LoveEboard_Dataset.length;
    }
}
