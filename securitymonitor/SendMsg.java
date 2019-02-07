package com.sssdo.securitymonitor;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class SendMsg extends Activity {

	String id;
	DBaseMsg dbase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messenger_newmsg);
		
		Intent i=getIntent();
		id=i.getExtras().getString("userId");
		EditText et2=(EditText) findViewById(R.id.ET_M_RId);
		et2.setText(id);
		
		dbase=new DBaseMsg(getApplicationContext());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void send(View v)
	{
		MessengerTask s=new MessengerTask(getApplicationContext());
		ArrayList<String> columns=new ArrayList<String>();
		ArrayList<String> values=new ArrayList<String>();
		
		EditText et1=(EditText) findViewById(R.id.ET_M_RId);
		String rid=et1.getText().toString();
		EditText et2=(EditText) findViewById(R.id.ET_M_Msg);
		String msg=et2.getText().toString();

		columns.add("SUserId");
		columns.add("RUserId");
		columns.add("Msg");
		
		values.add(id);
		values.add(rid);
		values.add(msg);
		
		s.setParams("sendMsg.php", columns, values);
		s.setDBase(dbase);
		s.execute("");
		
	}
	
}
