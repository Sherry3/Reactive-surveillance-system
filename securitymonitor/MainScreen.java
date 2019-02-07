package com.sssdo.securitymonitor;

import java.util.ArrayList;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreen extends Activity implements AnimationListener{
	
	ProgressDialog prgDialog;
	int userType;
	int listType;   //1-Regions : 2-Zones : 3-Admins : 4-Subtitutes : 5-Devices : 6-Notifications
	String rId;
	String zId;
	String sId;
	String aId;
	int lastListType;
	String userId;
	ListView list;
	ListView menuList;
	LinearLayout LLMenuList;
	DBase dbase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
	
		list=(ListView) findViewById(R.id.L_MS_Lists);
		menuList=(ListView) findViewById(R.id.L_MS_MenuList);
		LLMenuList=(LinearLayout) findViewById(R.id.LL_MS_MenuList);

		sId="";
		rId="flag";
		zId="";
		aId="";
		lastListType=0;
			
		dbase=new DBase(getApplicationContext());
		
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		String userName=bundle.getString("userName");
		
		if(userName.equals("hahahagotya"))
		{
			String a[]=dbase.readFile();
			userName=a[1];
			userId=a[0];
			userType=Integer.parseInt("0"+a[2]);
		}
		else
		{
			String t=bundle.getString("userType");
			userId=bundle.getString("userId");
			userType=Integer.parseInt("0"+t);
		}
		
		TextView tv=(TextView)findViewById(R.id.tv1);
		tv.setText(""+userName+"\n"+userId+"  :  "+userType);
		
		try{
			String link=bundle.getString("link");
			if(link.contains("Motion"))
			{
				openVideoDialog(link);
				//Add link to local database
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		if(userType==0)
		{
			listType=1;
		}
		else if(userType==2 || userType==1)
		{
			listType=2;
		}
		
		createDialog();
		
		loadContentInList();		
		setCustomActionBar();
		
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				lastListType=listType;
				// TODO Auto-generated method stub
				if(listType==1)
				{
					listType=2;
//					Toast.makeText(getApplicationContext(), arg0.getItemAtPosition(arg2).toString(), Toast.LENGTH_SHORT).show();
					String temp=parse(arg0.getItemAtPosition(arg2).toString()," : ",1);
					loadZoneByRegionId(temp);
					rId=temp;
				}
				else if(listType==2)
				{
					listType=5;
					String temp=parse(arg0.getItemAtPosition(arg2).toString()," : ",1);
					loadDeviceByZoneId(temp);
					zId=temp;
				}
				else if(listType==3)
				{
					listType=2;
					String temp=parse(arg0.getItemAtPosition(arg2).toString()," : ",1);
					loadZoneByAdminId(temp);
					aId=temp;
				}
				else if(listType==5)
				{
					listType=5;
					
					if(parse(arg0.getItemAtPosition(arg2).toString()," : ",1).equals("Camera"))
					{
						String temp=parse(arg0.getItemAtPosition(arg2).toString()," : ",0);
						loadDevice(temp);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "PIR sensor under construction", Toast.LENGTH_SHORT).show();
					}
					
				}

				loadContentInMenuList();
			}
		
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
//	getMenuInflater().inflate(R.menu.mainmenu_superadmin, menu);
		
	return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(userType==0)
			{ 
//				if(!zId.equals(""))
//				{
//					zId="";
//					listType=2;
//					loadContentInList();
//					loadContentInMenuList();
//					return true;
//				}
				if(!rId.equals(""))
				{
					rId="";
					listType=1;
					loadContentInList();
					loadContentInMenuList();
					return true;
				}
				
			}
			else if(userType==1)
			{
				if(lastListType!=0)
				{
					listType=lastListType;
					lastListType=0;
					loadContentInList();
					loadContentInMenuList();
					return true;
				}
			}
			else if(userType==2)
			{
				if(lastListType!=0)
				{
					listType=lastListType;
					lastListType=0;
					loadContentInList();
					loadContentInMenuList();
					return true;
				}
			}
			
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		LLMenuList.setVisibility(View.GONE);
		list.setVisibility(View.VISIBLE);
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	

	
	public void createDialog() {
		prgDialog = new ProgressDialog(this);
		prgDialog.setMessage("Please wait...");
		prgDialog.setIndeterminate(false);
		prgDialog.setMax(100);
		prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		prgDialog.setCancelable(false);
	}
	
	public void setCustomActionBar()
	{
		ActionBar actionBar=getActionBar();
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.actionbar, null);

		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);
		
		loadContentInMenuList();
	}
	
	public void loadContentInList()
	{		
		if(listType==1)
		{
			//Show regions
			loadRegionByUserId();
		}
		else if(listType==2)
		{
			//Show zones
			loadZoneByUserId();
		}
		/*Confused*/
		else if(listType==3)
		{
			//Show admins
			loadAdminBySuperUserId();
		}
		/*Confused*/
		else if(listType==4)
		{
			//Show subtitutes
			
		}
		
	}
	
	
	
	
	public void showMenu(View v)
	{
		if(LLMenuList.getVisibility()==View.GONE)
		{
			LLMenuList.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
			Animation anim=AnimationUtils.loadAnimation(this,R.anim.ltor);
			LLMenuList.setAnimation(anim);
			
			Animation anim1=AnimationUtils.loadAnimation(this,R.anim.anticlockwise);
			ImageButton ib=(ImageButton)findViewById(R.id.imageButton1);
			ib.setAnimation(anim1);
			
			LLMenuList.startAnimation(anim);
			ib.startAnimation(anim1);
		}
		else
		{
			Animation anim=AnimationUtils.loadAnimation(this,R.anim.rtol);
			anim.setAnimationListener(this);
			Animation anim1=AnimationUtils.loadAnimation(this,R.anim.clockwise);
			ImageButton ib=(ImageButton)findViewById(R.id.imageButton1);
			LLMenuList.setAnimation(anim);
			LLMenuList.startAnimation(anim);
			ib.setAnimation(anim1);
			ib.startAnimation(anim1);
		}
	}
	
	
	
	
	public void loadContentInMenuList()
	{
		ArrayList<String> ar=new ArrayList<String>();
		
		if(userType==0)
		{
			ar.add("Profile");
			ar.add("Regions");
//			ar.add("Admins");
			
			if(listType==1)
				ar.add("New Region");
			else if(listType==2)
			{
				ar.add("Zones");
//				ar.add("Subtitutes");
				ar.add("New Zone");
				ar.add("New Subtitute");
				ar.add("Delete Region");
			}
			else if(listType==5)
			{
				ar.add("Zone Settings");
				ar.add("Delete Zone");
				ar.add("New Device");
			}
		}
		else if(userType==1)
		{
			ar.add("Profile");
			ar.add("Zones");
//			ar.add("Subtitutes");
	
			ar.add("New Zone");
			ar.add("New Subtitute");
			
			if(listType==5)
			{
				ar.add("Zone Settings");
				ar.add("Delete Zone");
				ar.add("New Device");
			}
		}
		else if(userType==2)
		{
			ar.add("Profile");
			ar.add("Zones");
			
			if(listType==5)
			{
				ar.add("Zone Settings");
			}
		}
		
		ar.add("Logout");
		
		ArrayAdapter<String> ad=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ar);
		menuList.setAdapter(ad);
		
		menuList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				String action=arg0.getItemAtPosition(arg2).toString();
				if(action.equals("Profile"))
				{
					Intent intent=new Intent(MainScreen.this, UserProfile.class);
					intent.putExtra("userId", userId);
					startActivity(intent);
				}
				
				else if(action.equals("Regions"))
				{
					if(listType==1)
						{
						showMenu(arg1);
						return;
						}
					
					listType=1;
					loadContentInList();
					loadContentInMenuList();
					showMenu(arg1);
				}

				else if(action.equals("Zones"))
				{
					if(listType==2)
						{
						showMenu(arg1);
						return;
						}
					
					listType=2;
					loadContentInList();
					loadContentInMenuList();
					showMenu(arg1);
				}
//				else if(action.equals("Admins"))
//				{
//					listType=3;
//					loadContentInList();
//					loadContentInMenuList();
//					showMenu(arg1);
//				}
//				else if(action.equals("Subtitutes"))
//				{
//					listType=4;
//					loadContentInList();
//					loadContentInMenuList();
//					showMenu(arg1);
//				}
				else if(action.equals("New Region"))
				{
					//Add new region
					final Dialog dialog=new Dialog(MainScreen.this);
					dialog.setContentView(R.layout.add_region);
					dialog.setTitle("Add Region");
					
					Button b=(Button) dialog.findViewById(R.id.B_MS_Add);
					b.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
						
							//Add Region
							try {
								//Add Region
								EditText et=(EditText) dialog.findViewById(R.id.E_MS_RegionName);
								String name=et.getText().toString();
								
								OnlineTask s=new OnlineTask(getApplicationContext(), MainScreen.this);
								ArrayList<String> columns=new ArrayList<String>();
								ArrayList<String> values=new ArrayList<String>();
								ArrayList<String> outputColumns=new ArrayList<String>();

								columns.add("UserId");
								columns.add("Region_Name");
								values.add(userId);
								values.add(name);
//								outputColumns.add("RegionId");
								
								s.setParams("addRegion.php", columns, values, outputColumns, null);
								s.setFlag(2);
								s.setDialog(prgDialog);
								s.execute("");
								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
							
						}
					});
					
					dialog.show();
				}
				else if(action.equals("New Zone"))
				{
					//Add new zone
					Intent intent=new Intent(MainScreen.this, AddZone.class);
					intent.putExtra("regionId", rId);
					startActivity(intent);
				}
				else if(action.equals("New Device"))
				{
					//Add new device
					Intent intent=new Intent(MainScreen.this, AddDevice.class);
					intent.putExtra("zoneId", zId);
					startActivity(intent);
				}
				else if(action.equals("New Subtitute"))
				{
					//Add new subtitute
					Intent intent=new Intent(MainScreen.this, AddUser.class);
					intent.putExtra("userType", "2");
					intent.putExtra("count", ""+list.getCount());
					
					for(int i=0;i<list.getCount();i++)
					{
						intent.putExtra("zoneId"+i, parse(list.getItemAtPosition(i).toString(), " : ", 1));
					}
					
					startActivity(intent);
				}
				else if(action.equals("Zone Settings"))
				{
					//Zone settings
					Intent intent=new Intent(MainScreen.this, ZoneSettings.class);
					intent.putExtra("userType", ""+userType);
					intent.putExtra("userId", userId);
					intent.putExtra("zoneId", zId);
					
					startActivity(intent);
				}
				else if(action.equals("Delete Region"))
				{
					deleteRegion();
				}
				else if(action.equals("Delete Zone"))
				{
					deleteZone();
				}
				else if(action.equals("Logout"))
				{
					Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
					unregIntent.putExtra("app", PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0));
					startService(unregIntent);
					Toast.makeText(getApplicationContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
				
					dbase.delete();
					Intent i=new Intent(MainScreen.this, MainActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(i);
					MainScreen.this.finish();
				}
				
			}
			
		});
		

		findViewById(R.id.FL_MS_MainScreen).setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				
				if(arg1.getX()>LLMenuList.getLayoutParams().width && arg1.getY()>LLMenuList.getLayoutParams().height)
				{
					if(LLMenuList.getVisibility()==View.VISIBLE)
						showMenu(arg0);	
				}
				return false;
			}
		});
		
	}
	
	public void openVideoDialog(String link)
	{
		final Dialog dialog=new Dialog(MainScreen.this);
		final String path=link;
		dialog.setContentView(R.layout.videodownload);
		dialog.setTitle("Download video");
		
		ImageView iv=(ImageView)dialog.findViewById(R.id.IV_MS_IntrusionImage);
		try{
			String temp=link.replaceAll(".avi", ".jpg");	
			new DownloadImageTask(getApplicationContext(), (ImageView)  iv).execute(temp);
		}catch(Exception e)
	    {
	    	Toast.makeText(dialog.getContext(), "Error in loading photo\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
	    }
		
		Button b=(Button) dialog.findViewById(R.id.B_MS_DownloadVideo);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			
				//Add Region
				try {
					
//			        Intent resultIntent = new Intent(Intent.ACTION_VIEW);
//			        resultIntent.setData(Uri.parse(path));
//			        startActivity(resultIntent);
					
					Toast.makeText(getApplicationContext(), "Downloading : "+path, Toast.LENGTH_LONG).show();
					new DownloadVideoTask(getApplicationContext()).execute(path);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
		
		dialog.show();

	}
	
	public void loadRegionByUserId()
	{
		if(userType==0)
		{
			OnlineTask s=new OnlineTask(getApplicationContext(), MainScreen.this);
			ArrayList<String> columns=new ArrayList<String>();
			ArrayList<String> values=new ArrayList<String>();
			ArrayList<String> outputColumns=new ArrayList<String>();
			
			columns.add("UserId");
			values.add(userId);
			outputColumns.add("Region_Name");
			outputColumns.add("RegionId");
			
			s.setParams("loadRegionBySuperUserId.php", columns, values, outputColumns, list);
			s.setFlag(1);
			s.setDialog(prgDialog);
			s.execute("");
		}
			
	}
	
	public void loadZoneByUserId()
	{
		OnlineTask s=new OnlineTask(getApplicationContext(), MainScreen.this);
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		ArrayList<String> outputColumns=new ArrayList<String>();
		
		columns.add("UserId");
		values.add(userId);
		outputColumns.add("Zone_Name");
		outputColumns.add("ZoneId");
		
		if(userType==2)
		{
			s.setParams("loadZoneByUserId.php", columns, values, outputColumns, list);	
		}
		else if(userType==1)
		{
			s.setParams("loadZoneByAdminId.php", columns, values, outputColumns, list);	
		}
		else if(userType==0)
		{
			//PHP code  not written
			s.setParams("loadZoneBySuperUserId.php", columns, values, outputColumns, list);	
		}
		
		s.setFlag(1);
		s.setDialog(prgDialog);
		s.execute("");
	}
	
	
	public void loadZoneByRegionId(String id)
	{	
		OnlineTask s=new OnlineTask(getApplicationContext(), MainScreen.this);
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		ArrayList<String> outputColumns=new ArrayList<String>();
		
		columns.add("RegionId");
		values.add(id);
		outputColumns.add("Zone_Name");
		outputColumns.add("ZoneId");
		
		s.setParams("loadZoneByRegionId.php", columns, values, outputColumns, list);	
		
		s.setFlag(1);
		s.setDialog(prgDialog);
		s.execute("");
	}
	
	public void loadZoneByAdminId(String id)
	{	
		OnlineTask s=new OnlineTask(getApplicationContext(), MainScreen.this);
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		ArrayList<String> outputColumns=new ArrayList<String>();
		
		columns.add("UserId");
		values.add(id);
		outputColumns.add("Zone_Name");
		outputColumns.add("ZoneId");
		
		s.setParams("loadZoneByAdminId.php", columns, values, outputColumns, list);	
		
		s.setFlag(1);
		s.setDialog(prgDialog);
		s.execute("");
	}
	
	public void loadDeviceByZoneId(String id)
	{
		OnlineTask s=new OnlineTask(getApplicationContext(), MainScreen.this);
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		ArrayList<String> outputColumns=new ArrayList<String>();
		
		columns.add("ZoneId");
		values.add(id);
		outputColumns.add("DeviceId");
		outputColumns.add("Type");
		
		s.setParams("loadDeviceByZoneId.php", columns, values, outputColumns, list);	
		
		s.setFlag(1);
		s.setDialog(prgDialog);
		s.execute("");
	}
	
	public void loadAdminBySuperUserId()
	{
		if(userType==0)
		{
			OnlineTask s=new OnlineTask(getApplicationContext(), MainScreen.this);
			ArrayList<String> columns=new ArrayList<String>();
			ArrayList<String> values=new ArrayList<String>();
			ArrayList<String> outputColumns=new ArrayList<String>();
			
			columns.add("UserId");
			values.add(userId);
			outputColumns.add("Name");
			outputColumns.add("UserId");
			
			s.setParams("loadAdminBySuperUserId.php", columns, values, outputColumns, list);
			s.setFlag(1);
			s.setDialog(prgDialog);
			s.execute("");
		}
			
	}
	
	public void loadDevice(String id)
	{
		Intent i=new Intent(MainScreen.this, Device.class);
		i.putExtra("deviceId", id);
		startActivity(i);
	}
	
	public String parse(String str, String seperator, int pos)
	{
		String out[]=str.split(seperator);
		return out[pos];
	}
	
	public void deleteRegion()
	{
		DeleteTask s=new DeleteTask(getApplicationContext());
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		
		columns.add("RegionId");
		values.add(rId);
		
		s.setParams("removeRegion.php", columns, values);
		s.setDialog(prgDialog);
		s.execute("");
	}
	
	public void deleteZone()
	{
		DeleteTask s=new DeleteTask(getApplicationContext());
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		
		columns.add("ZoneId");
		values.add(zId);
		
		s.setParams("removeZone.php", columns, values);
		s.setDialog(prgDialog);
		s.execute("");
	}
	
	public void showNotifications(View v)
	{
		try
		{
			Intent i=new Intent(MainScreen.this, Links.class);
			i.putExtra("userId", userId);
			Toast.makeText(getApplicationContext(), "Under construction..", Toast.LENGTH_SHORT).show();
			startActivity(i);
		}catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			// TODO: handle exception
		}
	}
	
	public void showMsg(View v)
	{
		try
		{
			Intent i=new Intent(MainScreen.this, Messenger.class);
			i.putExtra("userId", userId);
			Toast.makeText(getApplicationContext(), "Under construction..", Toast.LENGTH_SHORT).show();
			startActivity(i);
		}catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			// TODO: handle exception
		}
		
	}
		
	
		
}


