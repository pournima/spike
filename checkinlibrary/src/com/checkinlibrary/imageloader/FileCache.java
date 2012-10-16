package com.checkinlibrary.imageloader;

import java.io.File;

import android.content.Context;
import android.util.Log;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context){
		//Find the dir to save cached images

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			if(android.os.Environment.getExternalStorageDirectory().canWrite()){
				cacheDir=new File(android.os.Environment.getExternalStorageDirectory().toString(),"HeadsUp");
			}else{
				cacheDir=context.getCacheDir();
			}
		}else{
			cacheDir=context.getCacheDir();
		}
		if(!cacheDir.exists()) {
			boolean bMkDir = cacheDir.mkdirs();
			Log.i("FILE CACHE", String.valueOf(bMkDir)); 
		}

	}

	public File getFile(String url){
		//I identify images by hashcode. Not a perfect solution, good for the demo.
		String filename=String.valueOf(url.hashCode());
		//Another possible solution (thanks to grantland)
		//String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear(){
		File[] files=cacheDir.listFiles();
		if(files==null)
			return;
		for(File f:files)
			f.delete();
	}

}