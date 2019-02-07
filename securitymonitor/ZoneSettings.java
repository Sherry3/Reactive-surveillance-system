package com.sssdo.securitymonitor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ZoneSettings extends Activity{

	String zoneId;
	String userId;
	int userType;
	ProgressDialog prgDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zone_settings);
	
		createDialog();
		
		try{
			SettingsTask task=new SettingsTask(getApplicationContext());
			
			Intent intent=getIntent();
			Bundle bundle=intent.getExtras();
			userId=bundle.getString("userId");
			zoneId=bundle.getString("zoneId");
			userType=Integer.parseInt(bundle.getString("userType"));
		    
			ArrayList<String> columns=new ArrayList<String>();
			ArrayList<String> values=new ArrayList<String>();
			
			columns.add("UserId");
			columns.add("ZoneId");
			values.add(userId);
			values.add(zoneId);
			
			task.setParams(columns, values);
			
			task.setDialog(prgDialog);
			task.execute("");
			
		}catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
			// TODO: handle exception
		}
				
	}

	
	public void createDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setIndeterminate(false);
		prgDialog.setMax(100);
		prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		prgDialog.setCancelable(false);
	}
	
	public void toggleNotify(View v)
	{
		ToggleButton tb=(ToggleButton)findViewById(R.id.B_ZS_Notification);
		ToggleStatusZone task=new ToggleStatusZone(getApplicationContext());
		
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		
		columns.add("UserId");
		columns.add("ZoneId");
		columns.add("NotifyMe");
		
		values.add(userId);
		values.add(zoneId);
		
		if(tb.isChecked()==false)
			values.add("false");
		else
			values.add("true");
		
		task.setParams("changeZoneNotifiactionStatus.php", columns, values);	
		task.setDialog(prgDialog);
		
		task.execute("");
	}
	
	public void toggleLevel(View v)
	{
		ToggleButton tb=(ToggleButton)findViewById(R.id.B_ZS_SecurityLevel);
		ToggleStatusZone task=new ToggleStatusZone(getApplicationContext());
		
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		
		columns.add("ZoneId");
		columns.add("Security");
		
		values.add(zoneId);
		
		if(tb.isChecked()==false)
			values.add("0");
		else
			values.add("1");
		
		task.setParams("changeZoneSecurity.php", columns, values);	
		task.setDialog(prgDialog);
		
		task.execute("");
	}
	
	public void toggleStatus(View v)
	{
		ToggleButton tb=(ToggleButton)findViewById(R.id.B_ZS_Status);
		ToggleStatusZone task=new ToggleStatusZone(getApplicationContext());
		
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		
		columns.add("ZoneId");
		columns.add("Status");
		
		values.add(zoneId);
		
		if(tb.isChecked()==false)
			values.add("off");
		else
			values.add("on");
		
		task.setParams("changeZoneStatus.php", columns, values);	
		task.setDialog(prgDialog);
		
		task.execute("");
	}
	
	
	
	
	
	public class SettingsTask extends AsyncTask<String, String, String>{
		
		ArrayList<String> columns;
		ArrayList<String> values;
		ArrayList<String> outputColumns;
		ProgressDialog dialog;
		Context c;
		String script;

		public SettingsTask(Context a) {
			c=a;
			script="loadZoneSettingsByZoneId.php";
			// TODO Auto-generated constructor stub
		}
		
		public void setParams(ArrayList<String> a, ArrayList<String> b)
		{
			columns=a;
			values=b;
		}
		
		public void setDialog(ProgressDialog pd)
		{
			dialog=pd;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			String response="";

			try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://sssdo.host56.com/phpFiles/"+script);
		        ArrayList<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		        
		        for(int i=0;i<columns.size();i++)
	        	{
		        	nameValuePairList.add(new BasicNameValuePair(columns.get(i),values.get(i)));
	        	}
	            
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
	            dialog.setProgress(10);
	            ResponseHandler<String> responsehandler = new BasicResponseHandler();
	            response = httpclient.execute(httppost,responsehandler);
		        dialog.setProgress(40);
		        
			}catch(Exception e){
					Log.i("Sourabh", e.getMessage());
					Toast.makeText(c, "PHP script error", Toast.LENGTH_SHORT).show();
					System.out.println("Error:"+e.getStackTrace());
			}
			
			// TODO Auto-generated method stub
			return response;
		}
		
		
		@Override
		protected void onPostExecute(String is) {
		// TODO Auto-generated method stub

			super.onPostExecute(is);
			
			//parse json data
			try{
					if(is==null)
					{
						return;
					}
					
					String res[]=is.split(",");
					
					((TextView)findViewById(R.id.TV_ZS_ZoneId)).setText(res[0].toString());
					((TextView)findViewById(R.id.TV_ZS_ZoneName)).setText(res[1].toString());
					
					if(userType==0 || userType==1)
					{
						((LinearLayout)findViewById(R.id.LL_ZS_SecurityLevel)).setVisibility(LinearLayout.VISIBLE);
						((LinearLayout)findViewById(R.id.LL_ZS_Status)).setVisibility(LinearLayout.VISIBLE);
						
						if(res[2].equals("0"))
						{
							((ToggleButton)findViewById(R.id.B_ZS_SecurityLevel)).setChecked(false);
						}
						else
						{
							((ToggleButton)findViewById(R.id.B_ZS_SecurityLevel)).setChecked(true);
						}
						
						if(res[3].equals("on"))
						{
							((ToggleButton)findViewById(R.id.B_ZS_Status)).setChecked(true);
						}
						else
						{
							((ToggleButton)findViewById(R.id.B_ZS_Status)).setChecked(false);
						}
					}
					else if(userType==2)
					{
						if(res.length==5)
						{
							((LinearLayout)findViewById(R.id.LL_ZS_Notification)).setVisibility(LinearLayout.VISIBLE);
						
							if(res[4].equals("true"))
							{
								((ToggleButton)findViewById(R.id.B_ZS_Notification)).setChecked(true);
							}
							else
							{
								((ToggleButton)findViewById(R.id.B_ZS_Notification)).setChecked(false);
							}
						}
					}
					
			        			        
				}catch(Exception e){
					System.out.println("202"+e.toString());
			}
	
			dialog.cancel();
		}
			
	}
	
	
}
