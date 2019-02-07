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
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends Activity{

	String userId;
	ProgressDialog prgDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
	
		try
		{
			createDialog();
			Intent intent=getIntent();
			Bundle bundle=intent.getExtras();
			userId=bundle.getString("userId");
		}catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
		}
		
		loadProfile();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void loadProfile()
	{
		try{
			SettingsTask task=new SettingsTask(getApplicationContext());
			ArrayList<String> columns=new ArrayList<String>();
			ArrayList<String> values=new ArrayList<String>();
			
			columns.add("UserId");
			values.add(userId);
			
			task.setParams(columns, values);
			
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
	
	public class SettingsTask extends AsyncTask<String, String, String>{
		
		ArrayList<String> columns;
		ArrayList<String> values;
		ArrayList<String> outputColumns;
		Context c;
		String script;

		public SettingsTask(Context a) {
			c=a;
			script="loadUserProfileByUserId.php";
			// TODO Auto-generated constructor stub
		}
		
		public void setParams(ArrayList<String> a, ArrayList<String> b)
		{
			columns=a;
			values=b;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			prgDialog.show();
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
	            prgDialog.setProgress(10);
	            ResponseHandler<String> responsehandler = new BasicResponseHandler();
	            response = httpclient.execute(httppost,responsehandler);
	            prgDialog.setProgress(40);
		        
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
					
					((TextView)findViewById(R.id.TV_UP_Name)).setText("Name : "+res[0].toString());		
					((TextView)findViewById(R.id.TV_UP_Id)).setText("User Id : "+res[1].toString());		
					((TextView)findViewById(R.id.TV_UP_Address)).setText("Address : "+res[2].toString());		
					((TextView)findViewById(R.id.TV_UP_Phone)).setText("Phone : "+res[3].toString());		
					((TextView)findViewById(R.id.TV_UP_Email)).setText("Email : "+res[4].toString());		
			        
					int itemp=Integer.parseInt(res[5].toString());
					if(itemp==0)
					{
						((TextView)findViewById(R.id.TV_UP_Rights)).setText("Role : Super Admin");
						((TextView)findViewById(R.id.TV_UP_SysReg)).setText("System Id : "+res[6].toString());
					}
					else if(itemp==1)
					{
						((TextView)findViewById(R.id.TV_UP_Rights)).setText("Role : Admin");
						((TextView)findViewById(R.id.TV_UP_SysReg)).setText("Region Id : "+res[6].toString());
					}
					if(itemp==2)
					{
						((TextView)findViewById(R.id.TV_UP_Rights)).setText("Role : Substitute");

						String temp="Zone Id :";
						for(int i=6;i<res.length;i++)
							temp+=" "+res[i].toString();
						
							((TextView)findViewById(R.id.TV_UP_SysReg)).setText(temp);
					}
			        			        
				}catch(Exception e){
					System.out.println("202"+e.toString());
			}
	
			prgDialog.cancel();
		}
			
	}

}
