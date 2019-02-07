package com.sssdo.securitymonitor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.os.Environment;

public class AFileManager {
	File sdDir;
	ArrayList<String> musicFiles;
	
	AFileManager(){
		musicFiles=new ArrayList<String>();
		sdDir = Environment.getExternalStorageDirectory();
		}
		
		AFileManager(String path){
			musicFiles=new ArrayList<String>();
			sdDir = new File(path);
			}
			
		public String nextWord(String words){
			String tmp="";
			for(int i=0;i<words.length();i++)
			{
				if(words.charAt(i)==' ')
					{
					tmp=words.substring(i+1, words.length());
					break;
					}
			}
			return tmp;
		}
		
		public void expand(File uadDir)
		{
			int i=0;
			File[] allFiles=uadDir.listFiles();
					
		
			while(i<allFiles.length)
	         {
			 	File f1=	new File(uadDir.getAbsolutePath()+ "/"+allFiles[i].getName());
			 	if(f1.isDirectory())
			 	{
			 		//a folders found
			 	}
			 		else
			 		{
			 		//a file found
			 		}
			    i++;	 
			}
		}
		
		public void searchFile(File Dir,String name)
		{
			int i=0;
			File[] allFiles=Dir.listFiles();
					
			while(i<allFiles.length)
	         {
			 	File f1=	new File(Dir.getAbsolutePath()+ "/"+allFiles[i].getName());
			 	if(f1.isDirectory())
			 	searchFile(f1,name);               //call itself to find within its folders
			 		
			 	else
			 	{
			 						//a file found
		 		}
			    i++;	 
			}
		}
		
		public String readFile(File f){
			
			if(f.isFile())
			{
			String text="";
			try {
				FileReader fr=new FileReader(f.getAbsolutePath());
				BufferedReader bf=new BufferedReader(fr);
				String line=bf.readLine();
			   
				while(line!=null)
				{
					text+=line;
					text+='\n';
					line =bf.readLine();
				}
				
				bf.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	 		catch (IOException e) {
				e.printStackTrace();
			}
	
		return text;
			}
			else
				return "Not a file";
		}
		

		public boolean appendInFile(File f,String text){

			if(f.isFile())
			{
			String previousText=readFile(f);
			try {
				FileOutputStream fos=new FileOutputStream(f.getAbsolutePath());
				PrintWriter pw=new PrintWriter(fos);
				pw.println(previousText+" "+text);
		        pw.close();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			}
			return false;
		}
		
		public boolean writeInFile(File f,String text){

			if(f.isFile())
			{
			try {
				FileOutputStream fos=new FileOutputStream(f.getAbsolutePath());
				PrintWriter pw=new PrintWriter(fos);
				pw.println(text);
				pw.close();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			}
			return false;
		}
		
		public boolean pasteFolder(File Dir,String path,String name)
		{
			int i=0;
			createFolder(name, path);
			path=path+"/"+name;
			File[] allFiles=Dir.listFiles();
					
			while(i<allFiles.length)
	         {
			 	if(allFiles[i].isDirectory())
			 	pasteFolder(allFiles[i],path,allFiles[i].getName());               //call itself to find within its folders
			 	else	
			 	//code to paste a file
			    i++;	 
			}
			return false;
		}
		
		public boolean createFile(String name,String path,String extension){

			File f1=new File(path);
			if(f1.isDirectory())
			{
			    File f2=new File(path+"/"+name+extension);
			    try {
					f2.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;
			}
			return false;
		}
		

		public boolean createFolder(String name,String path){

			File f1=new File(path);
			if(f1.isDirectory())
			{
			    File f2=new File(path+"/"+name);
			   	f2.mkdir();
				return true;
			}
			return false;
		}
		
		public boolean screateTxtFile(String name,String folder){
			
			try{
				createFile(name, folder,".txt");
			}catch(Exception e){
				return false;
			}
			return true;
		}
		
		public boolean deleteFile(File f){

			if(f.isFile())
			{
			     f.delete();
				return true;
			}
			return false;
		}
		
		public void deleteFolder(File Dir)
		{
			int i=0;
			File[] allFiles=Dir.listFiles();
					
		
			while(i<allFiles.length)
	         {
			 	if(allFiles[i].isDirectory())
			 	deleteFolder(allFiles[i]);               //call itself to find within its folders
			 		
			 	allFiles[i].delete();
			    i++;	 
			}
		}

		
		public String extension(File f){
			String s="";
			
			s=f.getName();
			String s1="";
			int temp=0;
			for(int i=0;i<s.length();i++){
				if(s.charAt(i)=='.' && temp==1)
					s1="";
					
				if(s.charAt(i)=='.'){
					temp=1;
					}
				else{}
				
				if(temp==1)
					s1+=s.charAt(i);
			
			}
			
			return s1;
		}
		
		public boolean pasteFile(String spath,String dpath)
		{
			try {
	            File dest_file = new File(dpath);
	            URL u = new URL("file://"+spath);
	            //dest_file.createNewFile();
	            DataInputStream stream = new DataInputStream(u.openStream());
	            URLConnection conn = u.openConnection();

	            byte[] buffer = new byte[conn.getContentLength()];
	            stream.readFully(buffer);
	            stream.close();

	            DataOutputStream fos = new DataOutputStream(new FileOutputStream(dest_file));
	            fos.write(buffer);
	            fos.flush();
	            fos.close();
	            return true;
	        }catch(Exception e){
	        	return false;
	        } 
		}
		
		public void pasteFolder(File source,String dest)
		{
			int i=0;
			File[] allFiles=source.listFiles();
		
			while(i<allFiles.length)
	         {
			 	if(allFiles[i].isDirectory())
			 	{	
			 		new File(dest).mkdir();
			 		pasteFolder(allFiles[i], dest+"/"+allFiles[i].getName());               //call itself to find within its folders
			 	}
			 	else	
			 	pasteFile(allFiles[i].getAbsolutePath(),dest+"/"+allFiles[i].getName());
			    i++;
			}
		}
		
		public ArrayList<String> searchMusicFiles(File Dir)
		{
			int i=0;
			File[] allFiles=Dir.listFiles();	
			while(i<allFiles.length)
	         {
			 	File f1=new File(Dir.getAbsolutePath()+ "/"+allFiles[i].getName());
			 	if(f1.isDirectory())
			 	searchMusicFiles(f1);               //call itself to find within its folders
			 		
			 	else
			 	{
			 				if(f1.getAbsolutePath().endsWith(".mp3") || f1.getAbsolutePath().endsWith(".MP3"))
			 				{
			 				musicFiles.add(f1.getAbsolutePath());	
			 				}
		 		}
			    i++;	 
			}
			return musicFiles;
		}
		
		public ArrayList<String> searchMusic()
		{
			musicFiles.clear();
			searchMusicFiles(sdDir);
			return musicFiles;
		}
}
