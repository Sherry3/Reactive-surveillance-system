package com.sssdo.securitymonitor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String>{
	  private final Activity context;
	  private final String[] names;
	  String path="http://sssdo.host56.com/phpFiles/Pictures/";
	  
	  static class ViewHolder {
	    public TextView text;
	    public ImageView image;
	  }

	  public CustomAdapter(Activity context, String[] names) {
	    super(context, R.layout.row, names);
	    this.context = context;
	    this.names = names;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View rowView = convertView;
	    // reuse views
	    if (rowView == null) {
	      LayoutInflater inflater = context.getLayoutInflater();
	      rowView = inflater.inflate(R.layout.row, null);
	      // configure view holder
	      ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) rowView.findViewById(R.id.row_tv);
	      viewHolder.image = (ImageView) rowView
	          .findViewById(R.id.row_img);
	      rowView.setTag(viewHolder);
	    }

	    // fill data
	    ViewHolder holder = (ViewHolder) rowView.getTag();
	    String s = names[position];
	    holder.text.setText(s);
	    
//	    try{
//			String temp=path+parse(s, " : ", 1)+".jpg";	
//			new DownloadImageTask((ImageView)  holder.image).execute(temp);
//		}catch(Exception e)
//	    {
//	    	Toast.makeText(context.getApplicationContext(), "Error in loading photo\n"+e.getMessage(), Toast.LENGTH_SHORT).show();
//	    }
	    
	    return rowView;
	  }

	  public String parse(String str, String seperator, int pos)
	  {
		String out[]=str.split(seperator);
		return out[pos];
	  }
	  
} 



