package com.sssdo.securitymonitor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public class DownloadVideoTask  extends AsyncTask<String, Void, String> {
	
	Context c;
	int i=0;
	
	public DownloadVideoTask(Context a) {
		c=a;
		// TODO Auto-generated constructor stub
	}
	
    protected String doInBackground(String... urls) {

    	File ex=Environment.getExternalStorageDirectory();
    	
		File folder=new File(ex.getAbsolutePath()+"/SecurityMonitor/Videos");
		if(!folder.exists())
			folder.mkdir();
		
    	String a[]=urls[0].split("/");
		File f=new File(ex.getAbsolutePath()+"/SecurityMonitor/Videos/"+a[a.length-1]);
		
		while(f.exists())
		{
			f=new File(ex.getAbsolutePath()+"/SecurityMonitor/Videos/"+"S"+i+""+a[a.length-1]);
			i++;
		}
		
//		Log.i("Sourabh", f.getAbsolutePath());
//		Toast.makeText(c, f.getAbsolutePath(), Toast.LENGTH_LONG).show();
		
    	connnectingwithFTP("sssdo.host56.com", "a6879791", "M@jor2K15", urls[0], f);
    	
        return "";
    }


/*    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        showProgressDialog();
    }*/

    protected void onPostExecute(String result) {
        //pDlg.dismiss();
//    	Log.i("Sourabh", "ho gya");
		Toast.makeText(c, "File downloaded", Toast.LENGTH_LONG).show();
    }
    
    
    public void connnectingwithFTP(String ip, String userName, String pass, String link, File f) {  
        boolean status = false;
        FTPClient mFtpClient=null;
        try {  
             mFtpClient = new FTPClient();  
             mFtpClient.setDataTimeout(60 * 1000);  
             mFtpClient.connect(InetAddress.getByName(ip));  
             status = mFtpClient.login(userName, pass);
//             Toast.makeText(c, ""+status, Toast.LENGTH_LONG).show();
     		
             Log.i("isFTPConnected", String.valueOf(status));  
//             if (FTPReply.isPositiveCompletion(mFtpClient.getReplyCode())) {  
                  mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);  
                  mFtpClient.enterLocalPassiveMode();  
//                  FTPFile[] mFileArray = mFtpClient.listFiles();  
//                  Log.e("Size", String.valueOf(mFileArray.length));
                  
                  Log.i("Output", ""+downloadSingleFile(mFtpClient, link, f));
                  
//             }  
        } catch (SocketException e) {  
             e.printStackTrace();  
        } catch (UnknownHostException e) {  
             e.printStackTrace();  
        } catch (IOException e) {  
             e.printStackTrace();  
        }
//        finally {
//            if (mFtpClient != null) {
//                mFtpClient.logout();
//                mFtpClient.disconnect();
//            }
//        }
  
   }  
    
    
    public boolean downloadSingleFile(FTPClient ftpClient, String remoteFilePath, File downloadFile) {  
       File parentDir = downloadFile.getParentFile();  
       if (!parentDir.exists())  
            parentDir.mkdir();  
       OutputStream outputStream = null;  
       try {  
    	   
            outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile));  
            
//            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            
//            Log.i("Sourabh", "came here");
//            Toast.makeText(c, "go here", Toast.LENGTH_LONG).show();
    		
            return ftpClient.retrieveFile(remoteFilePath, outputStream);  
       } catch (Exception ex) {  
            ex.printStackTrace();  
       } finally {  
            if (outputStream != null) {  
                 try {  
                      outputStream.close();  
                 } catch (IOException e) {  
                      e.printStackTrace();  
                 }  
            }  
       }  
       return false;  
  }   

    
}
