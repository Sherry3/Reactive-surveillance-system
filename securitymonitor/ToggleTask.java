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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ToggleTask extends AsyncTask<String, String, String>{
	
	ArrayList<String> columns;
	ArrayList<String> values;
	ProgressDialog dialog;
	Context c;
	String script;
	Button b1;
	Button b2;
	TextView tv;
	
	public ToggleTask(Context a, Button d, Button e,TextView t) {
		c=a;
		b1=d;
		b2=e;
		tv=t;
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
	        response="success";
	        
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
		
	    dialog.setProgress(80);
	
	    if(values.get(1).equals("on"))
	    	values.set(1, "Activate");
	    else
	    	values.set(1, "Dectivate");
	    
		if(is.equals("success"))
			{
				Toast.makeText(c, values.get(1)+"d", Toast.LENGTH_SHORT).show();
				if(values.get(1).equals("Activate"))
				{
					b1.setText("Deactivate");
					b2.setEnabled(true);
					tv.setEnabled(true);
				}
				else
				{
					b1.setText("Activate");
					b2.setEnabled(false);
					tv.setEnabled(false);
				}
			}
		else
			Toast.makeText(c, "not "+values.get(1)+"d", Toast.LENGTH_SHORT).show();
		
		dialog.cancel();
	}
	
}