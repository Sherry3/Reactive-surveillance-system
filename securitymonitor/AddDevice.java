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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AddDevice extends Activity{

	ProgressDialog prgDialog;
	String zoneId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_device);
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		zoneId=bundle.getString("zoneId");
		
		Spinner s1=(Spinner) findViewById(R.id.S_AD_Type);
		String array1[]={"Camera","PIR Sensor","Alarm"};
		ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array1); 
		s1.setAdapter(adapter1);
		
		Spinner s2=(Spinner) findViewById(R.id.S_AD_Status);
		String array2[]={"on","off"};
		ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array2); 
		s2.setAdapter(adapter2);
		
		createDialog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void createDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setIndeterminate(false);
		prgDialog.setMax(100);
		prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		prgDialog.setCancelable(false);
	}
	
	public void add(View v)
	{
		try{
			//Add device
			Spinner s1=(Spinner) findViewById(R.id.S_AD_Type);
			String type=s1.getSelectedItem().toString();
			Spinner s2=(Spinner) findViewById(R.id.S_AD_Status);
			String status=s2.getSelectedItem().toString();
	
			AddDeviceTask s=new AddDeviceTask();
			ArrayList<String> columns=new ArrayList<String>();
			ArrayList<String> values=new ArrayList<String>();
			
			columns.add("ZoneId");
			values.add(zoneId);
			columns.add("Type");
			values.add(type);
			columns.add("Status");
			values.add(status);
			
			s.setParams(columns, values);	
			
			s.execute("");
			
		}catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getStackTrace().toString(), Toast.LENGTH_LONG).show();
			// TODO: handle exception
		}
	
	}
	
	
	public class AddDeviceTask extends AsyncTask<String, Integer, String>{

		String script="addDevice.php";

		ArrayList<String> columns;
		ArrayList<String> values;
		String deviceId;
		
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
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub		
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
		            
		            deviceId=response;
		            
			}catch(Exception e){
					Toast.makeText(getApplicationContext(), "PHP script error", Toast.LENGTH_SHORT).show();
					System.out.println("Error:"+e.getStackTrace());
			}
			 
			return response;
			 
		}
			
		@Override
		protected void onPostExecute(String is) {
			// TODO Auto-generated method stub

			super.onPostExecute(is);
		
			prgDialog.setProgress(80);
            
			if(is.startsWith("d"))
				Toast.makeText(getApplicationContext(), "Device Created\nDevice ID : "+is, Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getApplicationContext(), "Error\nDevice not created", Toast.LENGTH_LONG).show();
			
			prgDialog.dismiss();
//			finish();
		}
			
	}
	
	
	

}
