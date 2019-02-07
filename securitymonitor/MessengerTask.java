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

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class MessengerTask extends AsyncTask<String, Integer, String>{

	Context c;
	String script="";

	ArrayList<String> columns;
	ArrayList<String> values;
	
    DBaseMsg dbase;
    
	public MessengerTask(Context a) {
		c=a;
		// TODO Auto-generated constructor stub
	}

	public void setParams(String s, ArrayList<String> a, ArrayList<String> b)
	{
		script=s;
		columns=a;
		values=b;
	}
	
	public void setDBase(DBaseMsg a)
	{
		dbase=a;
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
	            ResponseHandler<String> responsehandler = new BasicResponseHandler();
	            response = httpclient.execute(httppost,responsehandler);
		        
	            response="success";
	            
		}catch(Exception e){
				Toast.makeText(c, "PHP script error", Toast.LENGTH_SHORT).show();
				System.out.println("Error:"+e.getStackTrace());
		}
		 
		return response;
		 
	}
		
	@Override
	protected void onPostExecute(String is) {
		// TODO Auto-generated method stub

		super.onPostExecute(is);
	
		if(is.equals("success"))
		{
			Toast.makeText(c, "Sent", Toast.LENGTH_SHORT).show();
			dbase.insertIntoTable(values.get(1), values.get(0), values.get(2));
		}
		else
			Toast.makeText(c, "not sent", Toast.LENGTH_SHORT).show();
	}
		
}