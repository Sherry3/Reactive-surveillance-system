package com.sssdo.securitymonitor;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DBaseLinks {

	Context c;
    SQLiteDatabase mydb;
    private   static String DBNAME = "Sherry.db";
    private static String TABLE = "Links";
	
	public DBaseLinks(Context a) {
		c=a;
		createTable();
		// TODO Auto-generated constructor stub
	}
	

    // CREATE TABLE IF NOT EXISTS
    public void createTable() {
        try {
            mydb = c.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            mydb.execSQL("CREATE TABLE IF  NOT EXISTS " + TABLE
                    + " (ID INTEGER PRIMARY KEY, link TEXT, userid TEXT);");
            mydb.close();
        } catch (Exception e) {
            Toast.makeText(c, "Error in creating table", Toast.LENGTH_LONG).show();
        }
    }
	
 
 // THIS FUNCTION INSERTS DATA TO THE DATABASE
    public void insertIntoTable(String link, String userId) {
        try {
            mydb = c.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            mydb.execSQL("INSERT INTO " + TABLE
                    + "(link, userid) VALUES('"+link+"','"+userId+"');");
            mydb.close();
//            Toast.makeText(c, "Inserted", Toast.LENGTH_LONG).show();
 
        	} catch (Exception e) {
            Toast.makeText(c, "Error in inserting into table", Toast.LENGTH_LONG).show();
        }
    }
    
    
    public ArrayList<String> getLinks(String userId) {
    	 
        ArrayList<String> my_array = new ArrayList<String>();
        try {
            mydb = c.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE, null);
            Cursor allrows = mydb.rawQuery("SELECT link FROM " + TABLE+" where userid='"+userId+"'  order by ID desc", null);
            System.out.println("COUNT : " + allrows.getCount());
 
            if (allrows.moveToFirst()) {
                do {
 
                    String link = allrows.getString(0);
//                    String time = allrows.getString(1);
                    my_array.add(link);
 
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
