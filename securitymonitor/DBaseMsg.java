package com.sssdo.securitymonitor;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DBaseMsg {

	Context c;
    SQLiteDatabase mydb;
    private   static String DBNAME = "Sherry.db";
    private static String TABLE = "Messenger";
	
	public DBaseMsg(Context a) {
		c=a;
		createTable();
		// TODO Auto-generated constructor stub
	}
	

    // CREATE TABLE IF NOT EXISTS
    public void createTable() {
        try {
            mydb = c.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            mydb.execSQL("CREATE TABLE IF  NOT EXISTS " + TABLE
                    + " (ID INTEGER PRIMARY KEY, toN TEXT, fromN TEXT, msg TEXT);");
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(c, "Error in creating table", Toast.LENGTH_LONG).show();
        }
    }
	
 
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void insertIntoTable(String to, String from, String msg) {
        try {
            mydb = c.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            mydb.execSQL("INSERT INTO " + TABLE
                    + "(toN, fromN, msg) VALUES('"+to+"','"+from+"','"+msg+"');");
            mydb.close();
//            Toast.makeText(c, "Inserted", Toast.LENGTH_LONG).show();
 
        	} catch (Exception e) {
            Toast.makeText(c, "Error in inserting into table", Toast.LENGTH_LONG).show();
        }
    }
    
    
    // THIS FUNCTION SHOWS DATA FROM THE DATABASE
    public ArrayList<String> getNames(String name) {
 
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            mydb = c.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("SELECT DISTINCT fromN FROM " + TABLE+" where fromN = '"+name+"' OR toN = '"+name+"' order by ID", null);
            System.out.println("COUNT : " + allrows.getCount());
 
            if (allrows.moveToFirst()) {
                do {
 
                    String tname = allrows.getString(0);
//                    if(name.equals(userId))
//                    	continue;
                    
                    my_array.add(tname);
 
                } while (allrows.moveToNext());
            }
            
            allrows.close();
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(c, "Error encountered.\n"+e.toString(), Toast.LENGTH_LONG).show();
        }
        
        
        return my_array;
    }

    public ArrayList<String> getMsg(String name, String userId) {
    	 
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            mydb = c.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("SELECT msg FROM " + TABLE+" where (fromN = '"+name+"' OR toN = '"+name+"') AND (fromN='"+userId+"' OR toN='"+userId+"') order by ID", null);
            System.out.println("COUNT : " + allrows.getCount());
 
            if (allrows.moveToFirst()) {
                do {
 
                    String msg = allrows.getString(0);
                    my_array.add(msg);
 
                } while (allrows.moveToNext());
            }
            allrows.close();
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(c, "Error encountered.\n"+e.toString(), Toast.LENGTH_LONG).show();
        }
        return my_array;
    }

	
	

}
