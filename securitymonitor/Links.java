package com.sssdo.securitymonitor;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Links extends Activity {
 
	String userId;
	DBaseLinks dbase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.links);
 
        dbase=new DBaseLinks(getApplicationContext());
        
        Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		userId=bundle.getString("userId");
        
        load();
    }
    
    public void load()
    {
    	ArrayList<String> my_array = new ArrayList<String>();
        my_array = dbase.getLinks(userId);
        
        ListView list = (ListView) findViewById(R.id.LV_L_Links);
        ArrayAdapter<String> my_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, my_array);
        list.setAdapter(my_Adapter);
        
        
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
									
				new DownloadVideoTask(getApplicationContext()).execute(arg0.getItemAtPosition(arg2).toString());
				
			}
        });
        
        
    }
 
}