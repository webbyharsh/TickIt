package com.example.tickit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MuteNotificationsScreen extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
Button fromTime,endTime,setAlarm;
Calendar c=Calendar.getInstance();
Calendar d=Calendar.getInstance();
int startHour,startMinute;
public static final String SHARED_PREFS="sharedPrefs";
public static final String NOTIFICATIONS_MUTED="NOTIFICATIONS_MUTED";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mute_notifications_screen);
        fromTime=(Button)findViewById(R.id.timefrom);
        setAlarm=(Button)findViewById(R.id.notification_mute_btn);

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker=new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"Start time picker");
            }
        });


        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent=new Intent(MuteNotificationsScreen.this,AlertReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(MuteNotificationsScreen.this,1,intent,0);
                alarmManager.setExact(AlarmManager.RTC,c.getTimeInMillis(),pendingIntent);
                Toast.makeText(MuteNotificationsScreen.this,"Notifications muted ",Toast.LENGTH_LONG).show();
                mute();
                displayMuteTimeText();
                showMuteButton();
            }
        });
        Button b=(Button)findViewById(R.id.notification_unmute_btn);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent=new Intent(MuteNotificationsScreen.this,AlertReceiver.class);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(MuteNotificationsScreen.this,1,intent,0);
                alarmManager.cancel(pendingIntent);
                Toast.makeText(MuteNotificationsScreen.this,"Notifications unmuted ",Toast.LENGTH_LONG).show();
                unmute();
                HideMuteTimeText();
                hideMuteButton();
            }
        });
        Button mutedNot=(Button)findViewById(R.id.show_muted_notifications);
        mutedNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MuteNotificationsScreen.this,MutedNotificationsActivity.class);
                startActivity(i);
            }
        });
        Button f=(Button)findViewById(R.id.mute_btn_cancel);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MuteNotificationsScreen.this,NotificationAnalytics.class);
                startActivity(i);
            }
        });


    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startHour=hourOfDay;
            startMinute=minute;
            c.set(Calendar.HOUR_OF_DAY,startHour);
            c.set(Calendar.MINUTE,startMinute);
            c.set(Calendar.SECOND,0);
            saveMuteTime(startHour,startMinute);
            displayMuteTimeText();
    }

    public void mute(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(NOTIFICATIONS_MUTED,true);
        editor.apply();
    }
    public void unmute(){
        SharedPreferences sharedPreferences=getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(NOTIFICATIONS_MUTED,false);
        editor.putInt("mutedHour",0);
        editor.putInt("mutedMinute",0);
        editor.apply();
    }
    public boolean checkifAlreadyMuted(){
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        boolean c=sharedPreferences.getBoolean("NOTIFICATIONS_MUTED",false);
        return c;
    }
    public void displayMuteTimeText(){
        TextView t=(TextView) findViewById(R.id.muteTimeShow);
        t.setVisibility(View.VISIBLE);
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        String h=Integer.toString(sharedPreferences.getInt("mutedHour",0));
        String m=Integer.toString(sharedPreferences.getInt("mutedMinute",0));
        t.setText("Notifications muted until "+convert12(Integer.parseInt(h),Integer.parseInt(m)));
    }
    public void HideMuteTimeText(){
        TextView t=(TextView) findViewById(R.id.muteTimeShow);
        t.setVisibility(View.GONE);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(checkifAlreadyMuted()){
            displayMuteTimeText();
            showMuteButton();

        }else{
            Button b=(Button)findViewById(R.id.notification_unmute_btn);
            b.setVisibility(View.GONE);
            HideMuteTimeText();
            hideMuteButton();
        }
    }
    public void saveMuteTime(int Hour, int minute){
        SharedPreferences sharedPreferences=getSharedPreferences("sharedPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("mutedHour",Hour);
        editor.putInt("mutedMinute",minute);
        editor.apply();
    }
    public void showMuteButton(){
        Button b=(Button)findViewById(R.id.notification_unmute_btn);
        b.setVisibility(View.VISIBLE);

    }
    public void hideMuteButton(){
        Button b=(Button)findViewById(R.id.notification_unmute_btn);
        b.setVisibility(View.GONE);

    }
    public String convert12(int hour,int minute) {
// Get Hours
        String finalString,h,m;
        h="";m="";
        String mer="";
       if(hour>=0 &&hour<=12){
           h=Integer.toString(hour);
           mer=" AM";
       }
       else if(hour>12){
           h=Integer.toString(hour-12);
           mer=" PM";
       }
       if(minute<10){
         m="0"+Integer.toString(minute);
       }else{
       m=Integer.toString(minute);}
       return h+":"+m+mer;
    }

}
