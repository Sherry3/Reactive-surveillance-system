package com.sssdo.securitymonitor;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddUser extends Activity{

	ProgressDialog prgDialog;
	int userType;
	String regionId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_user);
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		userType=Integer.parseInt(bundle.getString("userType"));
		
		if(userType==1)
		{
			regionId=bundle.getString("regionId");
		}
		else if(userType==2)
		{
			Spinner s=(Spinner) findViewById(R.id.S_AU_Zone);
			s.setVisibility(Spinner.VISIBLE);
			ArrayList<String> list=new ArrayList<String>();
			int count=Integer.parseInt(bundle.getString("count"));
			
			for(int i=0;i<count;i++)
			{
				list.add(bundle.getString("zoneId"+i));
			}
			
			ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list); 
			s.setAdapter(adapter1);
		}
		
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
	
	public String md5(String s) 
	{ 
		try {
			    byte[] hash;

			    try {
			        hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));
			    } catch (NoSuchAlgorithmException e) {
			        throw new RuntimeException("Huh, MD5 should be supported?", e);
			    } catch (UnsupportedEncodingException e) {
			        throw new RuntimeException("Huh, UTF-8 should be supported?", e);
			    }

			    StringBuilder hex = new StringBuilder(hash.length * 2);

			    for (byte b : hash) {
			        int i = (b & 0xFF);
			        if (i < 0x10) hex.append('0');
			        hex.append(Integer.toHexString(i));
			    }

			    return hex.toString();
			
	     } catch (Exception e) {
	         e.printStackTrace();
	     }
	     return "";
	}

	
	public void add(View v)
	{
		
		//Add user
		EditText et=(EditText) findViewById(R.id.E_AU_Name);
		String name=et.getText().toString();
		et=(EditText) findViewById(R.id.E_AU_Password);
		String password=et.getText().toString();
		et=(EditText) findViewById(R.id.E_AU_Email);
		String email=et.getText().toString();
		et=(EditText) findViewById(R.id.E_AU_Phone);
		String phone=et.getText().toString();
		et=(EditText) findViewById(R.id.E_AU_Address);
		String address=et.getText().toString();
		int rights=userType;
		
		String must="";
		String tm="";
		
		if(address.equals(""))
		{	
			must="Address";
		}

		if(phone.equals(""))
		{	
			tm=must;
			must="Phone number, "+must;
		}

		if(email.equals(""))
		{	
			tm=must;
			must="Email, "+must;
		}

		if(password.equals(""))
		{	
			tm=must;
			must="Password, "+must;
		
		}

		if(name.equals(""))
		{	
			tm=must;
			must="Name, "+must;
		}
		
		tm="";
		
		if(!must.equals(""))
		{
			Toast.makeText(getApplicationContext(), tm+must+" must not be empty", Toast.LENGTH_SHORT).show();
			return;
		}
	
		AddUserTask s=new AddUserTask();
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		
		columns.add("UserName");
		values.add(name);
		columns.add("Password");
		values.add(md5(password));
		columns.add("Email");
		values.add(email);
		columns.add("Phone");
		values.add(phone);
		columns.add("Address");
		values.add(address);
		columns.add("Rights");
		values.add(""+rights);

		if(userType==1)
		{
			columns.add("RegionId");
			values.add(regionId);
		}
		else if(userType==2)
		{
			columns.add("ZoneId");
			Spinner sp=(Spinner) findViewById(R.id.S_AU_Zone);
			String zoneId=sp.getSelectedItem().toString();
			values.add(zoneId);
		}
		
		if(userType==1)
			s.setParams("addAdmin.php", columns, values);	
		if(userType==2)
			s.setParams("addSubstitute.php", columns, values);	
		
		s.execute("");
	}
	
	
	public class AddUserTask extends AsyncTask<String, Integer, String>{

		String script;

		ArrayList<String> columns;
		ArrayList<String> values;
		String userId;
	
		public void setParams(String s, ArrayList<String> a, ArrayList<String> b)
		{
			columns=a;
			values=b;
			script=s;
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
		            
		            userId=response;
		            
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
            
			if(is.startsWith("u"))
				Toast.makeText(getApplicationContext(), "User Created\nUser ID : "+is, Toast.LENGTH_LONG).show();
			else
				Toast.makeText(getApplicationContext(), "Error\nUser not created", Toast.LENGTH_LONG).show();
			
			prgDialog.dismiss();
//			finish();
		}
			
	}

}
