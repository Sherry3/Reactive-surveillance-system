package com.sssdo.securitymonitor;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Messages extends Activity {
 
	String userId;
	String p1;
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
        p1=bundle.getString("p1");
		
        ArrayList<String> my_array = new ArrayList<String>();
        my_array = dbase.getMsg(p1, userId);
        
        ListView list = (ListView) findViewById(R.id.LV_M_NamesList);
        ArrayAdapter<String> my_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, my_array);
        list.setAdapter(my_Adapter);
    }

    
    public void newMsg(View v)
    {
		Intent i=new Intent(Messages.this, SendMsg.class);
		i.putExtra("userId", userId);
		Toast.makeText(getApplicationContext(), "Under construction..", Toast.LENGTH_SHORT).show();
		startActivity(i);
    }    

 
}