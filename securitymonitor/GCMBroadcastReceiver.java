package com.sssdo.securitymonitor;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	try{
	        // Explicitly specify that GcmIntentService will handle the intent.
	        ComponentName comp = new ComponentName(context.getPackageName(),
	                GcmIntentService.class.getName());
	        // Start the service, keeping the device awake while it is launching.
	        startWakefulService(context, (intent.setComponent(comp)));
	        setResultCode(Activity.RESULT_OK);
    	}catch (Exception e) {
    		Toast.makeText(context, "Service error : "+e.getMessage(), Toast.LENGTH_LONG).show();
            	
			// TODO: handle exception
		}
    }
}
