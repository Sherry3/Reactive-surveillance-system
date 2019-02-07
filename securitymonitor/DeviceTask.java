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
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class DeviceTask extends AsyncTask<String, Integer, String>{

	Context c;
	String text="";
	String script="";
	String deviceId;
	ListView out=null;
	EditText et;
	ProgressDialog dialog;
	Activity mainScreen;
	String res;
	Button button;
	Button video;
	
	GoogleCloudMessaging gcm;
    String gcmregid;
    String gcmPROJECT_NUMBER = "913568432817";
	
	public DeviceTask(Context a, Activity m, Button b, Button e) {
		c=a;
		mainScreen=m;
		button=b;
		video=e;
		// TODO Auto-generated constructor stub
	}

	public void setParams(String s, String b, EditText d)
	{
		script=s;
		deviceId=b;
		et=d;
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
		        
	        	nameValuePairList.add(new BasicNameValuePair("DeviceId",deviceId));
		                    
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList));
	            dialog.setProgress(10);
	            ResponseHandler<String> responsehandler = new BasicResponseHandler();
	            response = httpclient.execute(httppost,responsehandler);
		        dialog.setProgress(40);
		        
		}catch(Exception e){
				Toast.makeText(c, "PHP script error", Toast.LENGTH_SHORT).show();
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
		
		if(is==null)
		{
	        Toast.makeText(c, "Error Input Stream null in OnlineTask", Toast.LENGTH_LONG).show();
			dialog.dismiss();
	    	return;
		}
		
		dialog.setProgress(50);
		
		ArrayList<String> result=parse(is);
        
        if(result==null)
        {
        		Toast.makeText(c, "Error", Toast.LENGTH_LONG).show();
        		result=new ArrayList<String>();	
        }
        
		dialog.setProgress(60);
			
		et.setText(""+result.get(result.size()-1));
		
		if(result.get(0).equals("on"))
		{
			button.setText("Deactivate");
			et.setEnabled(true);
			video.setEnabled(true);
		}
		else
		{
			button.setText("Activate");
			et.setEnabled(false);
			video.setEnabled(false);
		}
		
//		ArrayAdapter<String> adapter=new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, result); 
		dialog.setProgress(80);
//		out.setAdapter(adapter);
		dialog.dismiss();	
	}
	
	
	public ArrayList<String> parse(String r)
	{
		ArrayList<String> results=new ArrayList<String>();
				 
		//parse json data
		try{
				if(r==null)
				{
					return null;
				}
				
		        r=r.substring(1, r.length()-2);
		        r=r.replace("\"", "");
				String s[]=r.split(",");
				
				for(int i=0;i<s.length;i++)
				{
					results.add(s[i]);
				}
		        
		        return results;
		        
		}catch(Exception e){
			Toast.makeText(c, "202 error\n"+r.toString(), Toast.LENGTH_SHORT).show();
			
	        System.out.println("202"+e.toString());
		}
		
		results=null;
		return results;

	}
	
}


