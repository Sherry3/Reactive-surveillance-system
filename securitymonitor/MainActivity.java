package com.sssdo.securitymonitor;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	DBase db;
	ProgressDialog prgDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
//		hi();

		if(!isOnline())
		{
			Toast.makeText(getApplicationContext(), "No internet connection found", Toast.LENGTH_LONG).show();
//			Button b=(Button) findViewById(R.id.B_MA_Login);
//			b.setClickable(false);
			finish();
		}
		
		db=new DBase(getApplicationContext());
		createDialog();
		
		try{
			slideImages();
		}catch (Exception e) {
			// TODO: handle exception
		}

		String a[]=db.readFile();
		if(!a[0].equals(""))
		{
			try{
				//Information of user
				Intent i=new Intent(MainActivity.this, MainScreen.class);
				i.putExtra("userType", ""+a[2]);
				i.putExtra("userId", ""+a[0]);
				i.putExtra("userName", ""+a[1]);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
				
//				Toast.makeText(c, userName+"\n"+userId+"\n"+Rights, Toast.LENGTH_LONG).show();
				}catch(Exception x)
				{
					x.getMessage();
					Toast.makeText(getApplicationContext(), x.getMessage(), Toast.LENGTH_LONG).show();
				}
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		
//		createDialog();
//		Button b=(Button) findViewById(R.id.B_MA_Login);
//		b.setClickable(true);
//		
//		if(!isOnline())
//		{
//			Toast.makeText(getApplicationContext(), "No internet connection found", Toast.LENGTH_LONG).show();
//			b.setClickable(false);
//		}
//	}
	

	public void createDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setIndeterminate(false);
		prgDialog.setMax(100);
		prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		prgDialog.setCancelable(false);
		prgDialog.setProgress(50);
	}
	
	public void slideImages()
	{
		try
		{
			ImageView iv=(ImageView) findViewById(R.id.IV_MA_Image);
			SliderTask s=new SliderTask(getApplicationContext(), iv);
			s.execute();
		}catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Slider not supported", Toast.LENGTH_LONG).show();
			// TODO: handle exception
		}
	}

	public void login(View v)
	{
		
		OnlineTask s=new OnlineTask(getApplicationContext(), MainActivity.this);
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		ArrayList<String> outputColumns=new ArrayList<String>();
		
		EditText et1=(EditText) findViewById(R.id.E_MA_Username);
		String id=et1.getText().toString();
		EditText et2=(EditText) findViewById(R.id.E_MA_Password);
		String password=et2.getText().toString();

		columns.add("UserId");
		columns.add("Password");
		values.add(id);
		values.add(md5(password));
//		Toast.makeText(getApplicationContext(), md5(password), Toast.LENGTH_LONG).show();
		outputColumns.add("Name");
		outputColumns.add("UserId");
		outputColumns.add("Rights");	
		
		s.setParams("Login.php", columns, values, outputColumns, null);
		s.setFlag(0);
		s.setDialog(prgDialog);
		s.execute("");
	}
	
	public void logout(View v)
	{
		Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		startService(unregIntent);
		Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
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
	
	public boolean isOnline() 
	{
	   boolean connected=false;;	
       try{
    	ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
        return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }
	
	
	public class SliderTask extends AsyncTask<String, String, String>{

		Context c;
		ImageView iv;
		int count=0;
		CountDownTimer ct;
		int image[]={R.drawable.sliderimage01, R.drawable.sliderimage02, R.drawable.sliderimage03, R.drawable.sliderimage04};
		Animation animateImage;
		
		public SliderTask(Context a, ImageView i) {
			c=a;
			iv=i;
			
			animateImage = AnimationUtils.loadAnimation(c, R.anim.fadein);
	        
			ct=new CountDownTimer(20000000,5000) {
				
				@Override
				public void onTick(long arg0) {
					// TODO Auto-generated method stub
					changeBackground();
					
				}
				
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					start();
				}
			};
			
			// TODO Auto-generated constructor stub
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub		 
			
			ct.start();
			
			return "";
		}
		
		void changeBackground()
		{
			if(count>=4)
				count=0;
			
			iv.setBackground(c.getResources().getDrawable(image[count]));
			iv.startAnimation(animateImage);
			
			count++;
		}
		
				
	}

}
