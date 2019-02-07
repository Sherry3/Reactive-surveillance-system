package com.sssdo.securitymonitor;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Device extends Activity{

//	String userId;
//	int userType;
//	String zoneId;
	String deviceId;
	ProgressDialog prgDialog;
//	ListView list;
	String port="7650";
	EditText et1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device);

		Intent intent=getIntent();
//		list=(ListView) findViewById(R.id.L_D_Ip);

		et1=(EditText) findViewById(R.id.TV_D_IP);
		deviceId=intent.getExtras().getString("deviceId");
//		Toast.makeText(getApplicationContext(), deviceId, Toast.LENGTH_SHORT).show();
			
//		userType=Integer.parseInt(intent.getExtras().getString("userType"));
//		zoneId=intent.getExtras().getString("deviceId");
	
		createDialog();
		
		try{
		getIpAndStatus();
		}catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Cant fetch IP address\n"+e.toString(), Toast.LENGTH_SHORT).show();
			// TODO: handle exception
		}
		
//		list.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				// TODO Auto-generated method stub
//				
//				String ipaddr=arg0.getItemAtPosition(arg2).toString();
//				try{
//					Intent i = new Intent(Intent.ACTION_VIEW);
//					i.setPackage("org.videolan.vlc.betav7neon");
//					EditText et1=(EditText) findViewById(R.id.editText1);
////					EditText et2=(EditText) findViewById(R.id.editText2);
//					
//					i.setDataAndType(Uri.parse("http://"+ipaddr+":"+port), "video/MJPG");
//					startActivity(i);
//					}catch(Exception e)
//					{
//						Toast.makeText(getApplicationContext(), "Streaming error", Toast.LENGTH_SHORT).show();
//					}
//			}
			
//		});

		
	}
	
	public void startVideo(View v)
	{
		try{
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setPackage("org.videolan.vlc.betav7neon");
//		EditText et2=(EditText) findViewById(R.id.editText2);
		
			i.setDataAndType(Uri.parse("http://"+et1.getText().toString()+":"+port), "video/MJPG");
//			
//			Intent i = new Intent(Device.this, VideoPlayer.class);
//			i.putExtra("url", "http://"+et1.getText().toString()+":"+port);
			
			startActivity(i);
		}catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), "Streaming error", Toast.LENGTH_SHORT).show();
		}
	}
	
	void getIpAndStatus()
	{
		Button b=(Button) findViewById(R.id.B_D_Status);
		Button bv=(Button) findViewById(R.id.B_D_Video);
		
		DeviceTask s=new DeviceTask(getApplicationContext(), Device.this, b, bv);
		
		s.setParams("loadIpAddressByDeviceId.php", deviceId, et1);
		s.setDialog(prgDialog);
		s.execute("");
	}
	

	public void createDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setIndeterminate(false);
		prgDialog.setMax(100);
		prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		prgDialog.setCancelable(false);
	}
	
	
	public void toggleStatus(View v)
	{
		Button b1=(Button) findViewById(R.id.B_D_Status);
		Button b2=(Button) findViewById(R.id.B_D_Video);
		TextView tv=(TextView) findViewById(R.id.TV_D_IP);
		
		ToggleTask s=new ToggleTask(getApplicationContext(), b1, b2, tv);
		
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();

		String status=b1.getText().toString();

		columns.add("DeviceId");
		columns.add("Status");
		values.add(deviceId);
//		Toast.makeText(getApplicationContext(), md5(password), Toast.LENGTH_LONG).show();

		if(status.equals("Deactivate"))
			values.add("off");
		else
			values.add("on");
		
		s.setParams("changeDeviceStatus.php", columns, values);
		
		s.setDialog(prgDialog);
		s.execute("");
	}
		
}