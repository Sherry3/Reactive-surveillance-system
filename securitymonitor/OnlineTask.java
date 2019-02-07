package com.sssdo.securitymonitor;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class OnlineTask extends AsyncTask<String, Integer, String>{

	Context c;
	String text="";
	String script="";
	ArrayList<String> columns;
	ArrayList<String> values;
	ArrayList<String> outputColumns;
	ListView out=null;
	int flag=0;      //0-Login : 1-ListView
	ProgressDialog dialog;
	Activity mainScreen;
	String res;
	
	GoogleCloudMessaging gcm;
    String gcmregid;
    String gcmPROJECT_NUMBER = "913568432817";
	
	public OnlineTask(Context a, Activity m) {
		c=a;
		mainScreen=m;
		// TODO Auto-generated constructor stub
	}

	public void setParams(String s, ArrayList<String> a, ArrayList<String> b, ArrayList<String> c, ListView d)
	{
		script=s;
		columns=a;
		values=b;
		outputColumns=c;
		out=d;
	}
	
	public void setFlag(int f)
	{
		flag=f;
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
		        
		        if(flag==0)
		        {
		        	nameValuePairList.add(new BasicNameValuePair("RegistrationId",getRegId()));
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
        	if(flag==0)
        	{
        		Toast.makeText(c, "Invalid username or password", Toast.LENGTH_LONG).show();
            	dialog.dismiss();
        		return;
        	}
        	else if(flag==1)
        	{
        		Toast.makeText(c, "Empty", Toast.LENGTH_LONG).show();
        		result=new ArrayList<String>();
        	}
        	else if(flag==2)
        	{
        		Toast.makeText(c, "Can't insert data", Toast.LENGTH_LONG).show();
            	dialog.dismiss();
        		return;
        	}
        }
		dialog.setProgress(60);
        
		if(flag==0)
		{
			//Login
			String s=result.get(0);
			String d[]=s.split(":");
			d[1]=d[1].replaceAll(" ", "");
			d[2]=d[2].replaceAll(" ", "");
			login(d[0],d[1],d[2]);
//			Toast.makeText(c, result.get(0), Toast.LENGTH_LONG).show();
		}
		else if(flag==1)
		{
			String temp[]=new String[result.size()];
			for(int i=0;i<result.size();i++)
				temp[i]=result.get(i);
			
			CustomAdapter adapter=new CustomAdapter(mainScreen, temp);
			dialog.setProgress(80);
			out.setAdapter(adapter);
			dialog.dismiss();	
		}
		else if(flag==2)
		{
			try{
				Toast.makeText(c, "Region created\nRegion Id : "+result.get(0), Toast.LENGTH_LONG).show();
				dialog.dismiss();
				Intent in=new Intent(c, AddUser.class);
				in.putExtra("regionId", result.get(0));
				in.putExtra("userType", "1");
				in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				c.startActivity(in);
			}catch (Exception e) {
				Toast.makeText(c, e.getStackTrace().toString(), Toast.LENGTH_LONG).show();
			}
		}
	}	
	
	public ArrayList<String> parse(String r)
	{
		ArrayList<String> results=new ArrayList<String>();
		
		if(flag==2)
		{
			results.add(r);
			return results;
		}
				 
		//parse json data
		try{
				if(r==null)
				{
					return null;
				}
				
		        JSONArray jArray = new JSONArray(r);
		        for(int i=0;i<jArray.length();i++)
		        {
	                JSONObject json_data = jArray.getJSONObject(i);
	                
	                String sd="";
	                for (int j = 0; j < outputColumns.size(); j++) 
	                {
	                	if(j==outputColumns.size()-1)
	                		results.add(sd+json_data.getString(outputColumns.get(j)));
	                	else
	                		sd+=json_data.getString(outputColumns.get(j))+" : ";
	                }
	                    
		        }
		        return results;
		        
		}catch(JSONException e){
	        System.out.println("202"+e.toString());
		}
		
		results=null;
		return results;

	}

	
	
	public void login(String userName, String userId, String Rights)
	{
		try{
			//Information of user
			Intent i=new Intent(c, MainScreen.class);
			i.putExtra("userType", ""+Rights);
			i.putExtra("userId", ""+userId);
			i.putExtra("userName", ""+userName);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			DBase dbase=new DBase(c);
			dbase.insertCredentials(""+userId, ""+userName, ""+Rights);
			
			dialog.dismiss();
			c.startActivity(i);
			(mainScreen).finish();
			
//			Toast.makeText(c, userName+"\n"+userId+"\n"+Rights, Toast.LENGTH_LONG).show();
			}catch(Exception x)
			{
				x.getMessage();
				Log.i("Sourabh", x.getMessage());
				Toast.makeText(c, x.getMessage(), Toast.LENGTH_LONG).show();
			}
	}
	
	
    public String getRegId()
    {
        String gcmregid = "";
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(c);
            }
            gcmregid = gcm.register(gcmPROJECT_NUMBER);
            Log.i("GCM",  gcmregid);

        } catch (IOException ex) {
        	gcmregid = "Error :" + ex.getMessage();

        }
        return gcmregid;
     }
	
}


