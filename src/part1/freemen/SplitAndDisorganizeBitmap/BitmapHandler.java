package part1.freemen.SplitAndDisorganizeBitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BitmapHandler extends Handler {
	public final static int DISPLAYWIDTH = 480;		//480
	public final static int DISPLAYHEIGHT = 800;	//800
	
	private String savedBitmapPath;
	private File temFile = null;
	
	public BitmapHandler(){
		savedBitmapPath = "/sdcard/Test/pic.jpg";
	}
	
	public void saveBitmap(Bitmap bitmap){
		temFile = new File(savedBitmapPath);
		if (temFile.exists()){
			temFile.delete();
			Log.i("bitmapHandler", "the defaultPicture is Deleted");
		}
		try {
			temFile.createNewFile();					//Create a new File
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(temFile);		//create a fileOutput Stream
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		try {
			fos.flush();								//try to save
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Bitmap getBitmap(String imagePath){
		temFile = new File(imagePath);
		if (!temFile.exists()){
			Log.i("BitmapHandler", "the "+imagePath+" isn't exist");
			imagePath = savedBitmapPath;
			temFile = new File(imagePath);
			if (!temFile.exists()){
				Log.e("BitmapHandler", "the defaultImage"+imagePath+" isn't exist!");
				return null;
			}
		}
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		Bitmap bitmap = null;
		bmpFactoryOptions.inJustDecodeBounds = true;		//get the informations
		bitmap = BitmapFactory.decodeFile(imagePath, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float)DISPLAYHEIGHT);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float)DISPLAYWIDTH);
		
		if (heightRatio>1 || widthRatio>1){
			if(heightRatio > widthRatio){
				bmpFactoryOptions.inSampleSize = heightRatio;
			}else{
				bmpFactoryOptions.inSampleSize = widthRatio;
			}
		}
		bmpFactoryOptions.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(imagePath, bmpFactoryOptions);
		
		return bitmap;
	}
	
	public void handleMessage(Message msg){
		super.handleMessage(msg);
	}

}
