package com.sssdo.securitymonitor;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {

	public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    Handler HN = new Handler(); 

    private class DisplayToast implements Runnable {

      String TM = "";

          public DisplayToast(String toast){
              TM = toast; 
          }

          public void run(){
             Toast.makeText(getApplicationContext(), TM, Toast.LENGTH_SHORT).show();
          }
    }
    
    
    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	try{
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        DBaseMsg dbase=new DBaseMsg(getApplicationContext());
        DBaseLinks dlinks=new DBaseLinks(getApplicationContext());
        
        if(extras.isEmpty())
        {
        	HN.post(new DisplayToast("Empty"));
        	return;
        }
    	
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendError("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendError("Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
   
//            	try {
//	            	Vibrator myVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//	        		myVib.vibrate(500);
//            	}catch (Exception e) {
//					// TODO: handle exception
//				}
            	
            	if(extras.getString("link").equals("flag"))
            	{
            		String rid=(new DBase(getApplicationContext()).readFile())[0];
            		String sid=extras.getString("UserId");
            		
            		sendNotification(sid+" "+rid+"" + extras.getString("msg"), ""+extras.getString("name"), "");
//            		HN.post(new DisplayToast(sid+" "+rid));
                    dbase.insertIntoTable(rid, sid, extras.getString("msg"));
                    
            	}
            	else
            	{
            		sendNotification("" + extras.getString("msg"), ""+extras.getString("link"));
            		if(extras.getString("link").contains("Motion"))
            			dlinks.insertIntoTable(""+extras.getString("link"), (new DBase(getApplicationContext()).readFile())[0]);
            	}
            	            	
            	NOTIFICATION_ID++;
            	
//            	HN.post(new DisplayToast("Received: " + extras.toString()));
             }
        }
        
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    	}catch (Exception e) {
    		HN.post(new DisplayToast("Service error: " + e.toString()));
			// TODO: handle exception
		}
    }

    private void sendNotification(String msg, String link) {
    	
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        
        Intent resultIntent = new Intent(getApplicationContext() , MainScreen.class);
        resultIntent.putExtra("userName", "hahahagotya");
        resultIntent.putExtra("link", ""+link);
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(msg+" : "+NOTIFICATION_ID)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("->"))
                        .setContentText("->"+link)
                        .setAutoCancel(true)
                        .setSound(alarmSound);
        
        
        
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }
    
    private void sendNotification(String msg, String sender, String dummy) {
	
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        
//        Intent resultIntent = new Intent(this , MainScreen.class);
//        
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
                
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(sender+" : "+NOTIFICATION_ID)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg+" "+sender))
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setSound(alarmSound);
        
        
//        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    } 

}

