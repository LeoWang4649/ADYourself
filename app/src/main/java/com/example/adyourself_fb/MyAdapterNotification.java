package com.example.adyourself_fb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class MyAdapterNotification extends RecyclerView.Adapter<MyAdapterNotification.ViewHolder> {

    //private String[] Dataset;

    private Notification_data[] Notification_Dataset;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tmp_Notification;
        public ViewHolder(View v) {
            super(v);

            tmp_Notification = (TextView) v.findViewById(R.id.Ntmp_Notification);
        }

    }

    public MyAdapterNotification(Notification_data[] myDataset) {
        Notification_Dataset = myDataset;
    }
    @Override
    public MyAdapterNotification.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclefor_notification, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        String ttmp_Notificationr=Notification_Dataset[position].gettmp_Notification();

        holder.tmp_Notification.setText(ttmp_Notificationr);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
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
        });*/

    }
    @Override
    public int getItemCount() {
        return Notification_Dataset.length;
    }
}
