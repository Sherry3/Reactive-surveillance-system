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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class DeleteTask extends AsyncTask<String, Integer, String>{

	Context c;
	String script="";
	ArrayList<String> columns;
	ArrayList<String> values;
	ProgressDialog dialog;

	public DeleteTask(Context a) {
		c=a;
		// TODO Auto-generated constructor stub
	}

	public void setParams(String s, ArrayList<String> a, ArrayList<String> b)
	{
		script=s;
		columns=a;
		values=b;
	}
	
	public void setDialog(ProgressDialog pd)
	{
		dialog=pd;
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
	            dialog.setProgress(10);
	            ResponseHandler<String> responsehandler = new BasicResponseHandler();
	            response = httpclient.execute(httppost,responsehandler);
		        dialog.setProgress(40);
		        
		}catch(Exception e){
				Log.i("Sourabh", e.getMessage());
//				Toast.makeText(c, "PHP script error", Toast.LENGTH_SHORT).show();
				System.out.println("Error:"+e.getStackTrace());
		}
		 
		return response;
		 
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog.show();
	}
	
	@Override
	protected void onPostExecute(String is) {
		// TODO Auto-generated method stub

		super.onPostExecute(is);
		
        dialog.setProgress(60);
        
		Toast.makeText(c, is.toString(), Toast.LENGTH_LONG).show();
		dialog.dismiss();
		
	}		
}


