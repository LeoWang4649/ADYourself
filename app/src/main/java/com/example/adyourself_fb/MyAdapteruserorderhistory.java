package com.example.adyourself_fb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class MyAdapteruserorderhistory extends RecyclerView.Adapter<MyAdapteruserorderhistory.ViewHolder> {

    //private String[] Dataset;

    private UserOrderHistory_data[] UserOrderHistory_Dataset;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tmp_AD,tmp_BoardID,tmp_Date,tmp_Completion,tmp_Price,tmp_Time,tmp_User,tmp_OrderID;
        //public ImageView Gamepic;
        public ViewHolder(View v) {
            super(v);

            tmp_OrderID = (TextView) v.findViewById(R.id.Utmp_OrderID);
            tmp_Completion =(TextView) v.findViewById(R.id.Utmp_Completion);
            tmp_Date =(TextView) v.findViewById(R.id.Utmp_Date);
            tmp_Time =(TextView) v.findViewById(R.id.Utmp_Time);

            /*tmp_User =(TextView) v.findViewById(R.id.Detail_User);
            tmp_AD =(TextView) v.findViewById(R.id.Detail_AD);
            tmp_BoardID =(TextView) v.findViewById(R.id.Detail_Board);
            tmp_Price =(TextView) v.findViewById(R.id.Detail_Price);*/
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

    public MyAdapteruserorderhistory(UserOrderHistory_data[] myDataset) {
        UserOrderHistory_Dataset = myDataset;
    }
    @Override
    public MyAdapteruserorderhistory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclefor_userorderhistory, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        String ttmp_User=UserOrderHistory_Dataset[position].gettmp_User();
        String ttmp_AD=UserOrderHistory_Dataset[position].gettmp_AD();
        String ttmp_Completion=UserOrderHistory_Dataset[position].gettmp_Completion();
        String ttmp_BoardID=UserOrderHistory_Dataset[position].gettmp_BoardID();
        String ttmp_Date = UserOrderHistory_Dataset[position].gettmp_Date();
        String ttmp_Time = UserOrderHistory_Dataset[position].gettmp_Time();
        String ttmp_Price = UserOrderHistory_Dataset[position].gettmp_Price();
        String ttmp_OrderID = UserOrderHistory_Dataset[position].gettmp_OrderID();

        holder.tmp_OrderID.setText("訂單編號:" + ttmp_OrderID);
        holder.tmp_Completion.setText("訂單完成狀態:" + ttmp_Completion);
        holder.tmp_Date.setText("廣告日期:"+ttmp_Date);
        holder.tmp_Time.setText("廣告時段:"+ttmp_Time);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t_AD = UserOrderHistory_Dataset[position].gettmp_AD();
                String t_BoardID = UserOrderHistory_Dataset[position].gettmp_BoardID();
                String t_Date = UserOrderHistory_Dataset[position].gettmp_Date();
                String t_Completion = UserOrderHistory_Dataset[position].gettmp_Completion();
                String t_Price = UserOrderHistory_Dataset[position].gettmp_Price();
                String t_Time = UserOrderHistory_Dataset[position].gettmp_Time();
                String t_User = UserOrderHistory_Dataset[position].gettmp_User();
                String t_OrderID = UserOrderHistory_Dataset[position].gettmp_OrderID();

                Context context = view.getContext();
                Intent intent = new Intent();
                intent.setClass(context,UserOrderHistoryDetail.class);
                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("t_AD",t_AD);
                bundle.putString("t_BoardID",t_BoardID);
                bundle.putString("t_Date",t_Date);
                bundle.putString("t_Completion",t_Completion);
                bundle.putString("t_Price",t_Price);
                bundle.putString("t_Time",t_Time);
                bundle.putString("t_User",t_User);
                bundle.putString("t_OrderID",t_OrderID);

                //將Bundle物件assign給intent
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }
    @Override
    public int getItemCount() {
        return UserOrderHistory_Dataset.length;
    }
}
