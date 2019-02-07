package com.sssdo.securitymonitor;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Messenger extends Activity {
 
	String userId;
	int type=0;  //0:name  1:msg
	DBaseMsg dbase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
 
        dbase=new DBaseMsg(getApplicationContext());
        
        Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		userId=bundle.getString("userId");
        
        loadNames();
    }
    
    public void loadNames()
    {
    	ArrayList<String> my_array = new ArrayList<String>();
        my_array = dbase.getNames(userId);
        
        ListView list = (ListView) findViewById(R.id.LV_M_NamesList);
        ArrayAdapter<String> my_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, my_array);
        list.setAdapter(my_Adapter);
        list.setOnItemClickListener(click1);
    }

    
    public void newMsg(View v)
    {
		Intent i=new Intent(Messenger.this, SendMsg.class);
		i.putExtra("userId", userId);
		Toast.makeText(getApplicationContext(), "Under construction..", Toast.LENGTH_SHORT).show();
		startActivity(i);
		loadNames();
    }    
    
    OnItemClickListener click1=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
			String p1=arg0.getItemAtPosition(arg2).toString();
			Intent i= new Intent(Messenger.this, Messages.class);
			i.putExtra("userId", userId);
			i.putExtra("p1", p1);
			startActivity(i);
		}
    	
	};

 
}