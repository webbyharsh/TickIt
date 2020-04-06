package com.example.tickit;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MutedNotificationsAdapter extends RecyclerView.Adapter<MutedNotificationsAdapter.MyViewHolder>{
    Context context;
    ArrayList<SimpleNotificationMaker> myNots;

    public MutedNotificationsAdapter(Context context, ArrayList<SimpleNotificationMaker> myNots) {
        this.context = context;
        this.myNots = myNots;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_muted_notification,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final PackageManager pm = context.getApplicationContext().getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(myNots.get(position).getPackageName(), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        holder.AppName.setText(applicationName);

        holder.tickerText.setText(myNots.get(position).getTickerText());
        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(myNots.get(position).getPackageName());
            holder.notIcon.setImageDrawable(icon);
        }
        catch(PackageManager.NameNotFoundException e){

        }
    }

    @Override
    public int getItemCount() {
        return myNots.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView AppName,tickerText;
        ImageView notIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            AppName = (TextView) itemView.findViewById(R.id.muted_notification_title);
            tickerText=(TextView) itemView.findViewById(R.id.muted_notification_ticker);
            notIcon=(ImageView)itemView.findViewById(R.id.muted_notification_icon);
        }
    }
}
