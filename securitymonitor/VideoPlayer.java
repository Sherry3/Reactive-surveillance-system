package com.sssdo.securitymonitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class VideoPlayer extends Activity{
	
	String url;
	WebView wv;
	ProgressDialog prgDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoplayer);
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		url=bundle.getString("url");
	
		wv = (WebView) findViewById(R.id.WV_VP_Video);
		wv.setWebViewClient(new AWebViewClient());
		WebSettings ws=wv.getSettings();
		ws.setJavaScriptEnabled(true);
		
		createDialog();
		
		new LoadTask().execute("");
	}

	public void createDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setIndeterminate(false);
		prgDialog.setMax(100);
		prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		prgDialog.setCancelable(false);
	}
	
	
	public class LoadTask extends AsyncTask<String, Integer, String>{

		public void load()
		{
			wv.loadUrl(url);
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

			 try{
				 	load();
			}catch(Exception e){
					Toast.makeText(getApplicationContext(), "PHP script error", Toast.LENGTH_SHORT).show();
					System.out.println("Error:"+e.getStackTrace());
			}
			 
			 return "";
			 
		}
	
			
		@Override
		protected void onPostExecute(String is) {
			// TODO Auto-generated method stub

			super.onPostExecute(is);
		
			prgDialog.setProgress(80);
            
			prgDialog.dismiss();
		}
			
	}


}
