package com.example.adyourself_fb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class MyAdapteradminorder extends RecyclerView.Adapter<MyAdapteradminorder.ViewHolder> {

    //private String[] Dataset;

    private Adminorder_data[] Adminorder_Dataset;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tmp_AD,tmp_BoardID,tmp_Date,tmp_Pending,tmp_Price,tmp_Time,tmp_User,tmp_OrderID;
        //public ImageView Gamepic;
        public ViewHolder(View v) {
            super(v);

            tmp_OrderID = (TextView) v.findViewById(R.id.Rtmp_OrderID);
            tmp_Pending =(TextView) v.findViewById(R.id.Rtmp_Pending);
            tmp_Date =(TextView) v.findViewById(R.id.Rtmp_Date);
            tmp_Time =(TextView) v.findViewById(R.id.Rtmp_Time);

            tmp_User =(TextView) v.findViewById(R.id.Detail_User);
            //tmp_AD =(TextView) v.findViewById(R.id.Detail_AD);
            tmp_BoardID =(TextView) v.findViewById(R.id.Detail_Board);
            tmp_Price =(TextView) v.findViewById(R.id.Detail_Price);
        }

    }

    /*public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView =(TextView) v.findViewById(R.id.recyclerTextView2);
        }
    }*/
    //public MyAdapterallgame(String[] myDataset) {Dataset = myDataset;}

    public MyAdapteradminorder(Adminorder_data[] myDataset) {
        Adminorder_Dataset = myDataset;
    }
    @Override
    public MyAdapteradminorder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclefor_adminorder, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        String ttmp_User=Adminorder_Dataset[position].gettmp_User();
        String ttmp_AD=Adminorder_Dataset[position].gettmp_AD();
        String ttmp_Pending=Adminorder_Dataset[position].gettmp_Pending();
        String ttmp_BoardID=Adminorder_Dataset[position].gettmp_BoardID();
        String ttmp_Date = Adminorder_Dataset[position].gettmp_Date();
        String ttmp_Time = Adminorder_Dataset[position].gettmp_Time();
        String ttmp_Price = Adminorder_Dataset[position].gettmp_Price();
        String ttmp_OrderID = Adminorder_Dataset[position].gettmp_OrderID();
        int ttmp_OrderNum = Adminorder_Dataset[position].gettmp_OrderNum();

        holder.tmp_OrderID.setText("訂單編號:" + ttmp_OrderID);
        holder.tmp_Pending.setText("審核狀態:" + ttmp_Pending);
        holder.tmp_Date.setText("廣告日期:"+ttmp_Date);
        holder.tmp_Time.setText("廣告時段:"+ttmp_Time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t_AD = Adminorder_Dataset[position].gettmp_AD();
                String t_BoardID = Adminorder_Dataset[position].gettmp_BoardID();
                String t_Date = Adminorder_Dataset[position].gettmp_Date();
                String t_Pending = Adminorder_Dataset[position].gettmp_Pending();
                String t_Price = Adminorder_Dataset[position].gettmp_Price();
                String t_Time = Adminorder_Dataset[position].gettmp_Time();
                String t_User = Adminorder_Dataset[position].gettmp_User();
                String t_OrderID = Adminorder_Dataset[position].gettmp_OrderID();
                int t_OrderNum = Adminorder_Dataset[position].gettmp_OrderNum();

                Context context = view.getContext();
                Intent intent = new Intent();
                intent.setClass(context,AdminorderDetail.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("t_AD",t_AD);
                bundle.putString("t_BoardID",t_BoardID);
                bundle.putString("t_Date",t_Date);
                bundle.putString("t_Pending",t_Pending);
                bundle.putString("t_Price",t_Price);
                bundle.putString("t_Time",t_Time);
                bundle.putString("t_User",t_User);
                bundle.putString("t_OrderID",t_OrderID);
                bundle.putInt("t_OrderNum",t_OrderNum);

                //將Bundle物件assign給intent
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return Adminorder_Dataset.length;
    }
}
