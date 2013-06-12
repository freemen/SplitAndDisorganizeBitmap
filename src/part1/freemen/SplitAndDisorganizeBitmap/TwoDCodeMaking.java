package part1.freemen.SplitAndDisorganizeBitmap;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class TwoDCodeMaking {
	private final static int SMALLIFOR = 21;
	private final static int MIDDLEIFOR = 25;
	private final static int MUCHIFOR = 31;
	private int infoNum;
	
	private final static int MARKSIZE = 3; 
	
	private final static int CODEHEIGHT = 300;
	private final static int CODEWIDTH = 300;
	public String twoDString = null;
	public Bitmap twoDBitmap = null;
	
	public TwoDCodeMaking(){
		
	}
	
	public Bitmap bytes2Bitmap(byte[] byteMsg){
		twoDBitmap = Bitmap.createBitmap(CODEWIDTH, CODEHEIGHT, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(twoDBitmap);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, CODEWIDTH, CODEHEIGHT, paint);
		//encode the byte to 2d code
		byteMsg = encode2dbyte(byteMsg);
		//just show some information of the byte
		String byte2StringMsg = "";
		for(int i=0; i<byteMsg.length; i++){
			byte2StringMsg+=(Integer.toBinaryString(byteMsg[i])+'|');
		}
		Log.i("TwoDCodeMaking","len:"+byteMsg.length+"byteMsg:"+byte2StringMsg
				+"\nbyteString:"+byte2StringMsg.length());
		
		//begin to draw the 2d code picture
		int xNum, yNum;
		infoNum = yNum = xNum = SMALLIFOR;
		if (byteMsg.length*8 > usefulDataAmount(SMALLIFOR)){		//expend the part of 2d code
			if (byteMsg.length*8 > usefulDataAmount(MIDDLEIFOR)){
				infoNum = xNum = yNum = MUCHIFOR;
			}else{
				infoNum = xNum = yNum = MIDDLEIFOR;
			}
		}
		float dx = (float) CODEWIDTH /xNum;
		float dy = (float) CODEHEIGHT/yNum;				//10x10 code
		float left = 0, right, top = 0, bottom;
		
		//DRAW the POSITION MARK
		paint.setColor(Color.rgb(220, 220, 220));
		canvas.drawRect(0, 0, MARKSIZE*dx, MARKSIZE*dy, paint);
		canvas.drawRect(0, CODEHEIGHT-MARKSIZE*dy, MARKSIZE*dx, CODEHEIGHT, paint);
		canvas.drawRect(CODEWIDTH-MARKSIZE*dx, 0, CODEWIDTH, MARKSIZE*dy, paint);
		paint.setColor(Color.WHITE);
		canvas.drawRect(0+dx, 0+dy , MARKSIZE*dx-dx, MARKSIZE*dy-dy, paint);
		canvas.drawRect(0+dx, CODEHEIGHT-MARKSIZE*dy+dy, MARKSIZE*dx-dx, CODEHEIGHT-dy, paint);
		canvas.drawRect(CODEWIDTH-MARKSIZE*dx+dx, 0+dy, CODEWIDTH-dx, MARKSIZE*dy-dy, paint);
		paint.setColor(Color.BLACK);
		for (int i=MARKSIZE+1; i<yNum-MARKSIZE; i+=2){
			canvas.drawRect(i*dx, dy, i*dx+dx, MARKSIZE*dy-dy, paint);
			canvas.drawRect(dx, i*dy, MARKSIZE*dx-dx, i*dy+dy, paint);
		}
		paint.setColor(Color.BLACK);
		
		//DRAW DATA
		int bitNum = 0;
		for (int i=0; i<yNum; i++){
			bottom = top + dy;
			for (int j = 0; j<xNum; j++){
				right = left + dx;
				
				//jump over the mark
				if (i==0 || j==0 
						|| i<MARKSIZE || j<MARKSIZE 
//						|| i>=(xNum-MARKSIZE) || j>=(yNum-MARKSIZE)
						
//						|| (i<MARKSIZE && j<MARKSIZE)
//						|| (i<MARKSIZE && j>=(yNum-MARKSIZE))
//						|| (j<MARKSIZE && i>=(xNum-MARKSIZE))
						){		//LINK1:will affect the usefulDataAmount
					//do nothing
				}else{
					int byteN = bitNum / 8 ;
//					Log.i("TwoDCodeMaking", "byteN:"+byteN+" bitNum:"+bitNum);
					if (byteN < byteMsg.length ){
						if ((0x80 & byteMsg[byteN]) == 0x80){	//the highest bit is 1
							canvas.drawRect(left, top, right, bottom, paint);
						}
						byteMsg[byteN] = (byte) (byteMsg[byteN] << 1);
						bitNum++;
					}else{
						break;
					}
	//				Log.i("TwoDCodeMaking", "l:"+String.valueOf(left)
	//						+" t:"+String.valueOf(top)
	//						+" r:"+String.valueOf(right)
	//						+" b:"+String.valueOf(bottom));
					
				}
				
				left = right;
			}
			top = bottom;
			left = 0;
		}
		
		return twoDBitmap;
	}
	private byte[] encode2dbyte(byte[] originalBytes){
		byte[] lenBytes = new byte[2];
		lenBytes[0] = (byte)(originalBytes.length & 0xff);
		lenBytes[1] = '|';
		byte[] newBytes = new byte[originalBytes.length+lenBytes.length+1];
		System.arraycopy(lenBytes, 0, newBytes, 0, lenBytes.length);
		System.arraycopy(originalBytes, 0, newBytes, lenBytes.length, originalBytes.length);
		newBytes[newBytes.length-1] = '|';
		Log.i("2dMaking", "newByte:"+String.valueOf(newBytes));
		return newBytes;
	}
	
	private int usefulDataAmount(int size){
		//LINK1:depend on which place to be display
//		return (size-2*MARKSIZE)*(size-2*MARKSIZE);
		return (size-MARKSIZE)*(size-MARKSIZE);
//		return size*size - MARKSIZE*MARKSIZE*3 - 2*size + 4*MARKSIZE;
	}
	
	public Bitmap array2Bitmap(ArrayList<Integer> array){
		
		return null;
	}
	
	public Bitmap string2Bitmap(String msg){
		byte[] byteMsg = msg.getBytes();
		return bytes2Bitmap(byteMsg);
	}
	
	private Bitmap changedBm = null;
	
	public String bitmap2String(Bitmap pic){
		Bitmap bm = findThePic(pic);
		
		int picW = bm.getWidth();
		int picH = bm.getHeight();
//		changedBm = bm.copy(bm.getConfig(), true);
		int[] pixel = new int[picW*picH];
		Log.v("2dMaking", "w:"+picW+" h:"+picH);
		bm.getPixels(pixel, 0, picW, 0, 0, picW, picH);
		
		//change the picture's pixel
		//test:get the opposite color of the picture
//		for(int y=0; y<picH; y++){
//			for (int x=0; x<picW; x++){
//				int index = y*picW + x;
//				pixel[index] = 0xff000000 | (pixel[index] ^ 0xffffff);
//			}
//		}
		//
		
//		changedBm.setPixels(pixel, 0, picW, 0, 0, picW, picH);
		return "hello";
	}
	public Bitmap showChangedBm(){
		return changedBm;
	}
	
	private final static byte OUTCORNER = 0x1;
	private final static byte INCORNER = 0x2;
	private final static byte EDGE = 0x3;
	private final static byte INSIDE = 0x4;
	private int thepicW, thepicH;
	
	public Bitmap findThePic(Bitmap bm){
		int picW = thepicW = bm.getWidth();
		int picH = thepicH = bm.getHeight();
		
		changedBm = bm.copy(bm.getConfig(), true);
		int[] pixel = new int[picW*picH];
		int[] anotherp = new int[picW*picH];
		byte[][] isEdgeTable = new byte[picW][picH];
		bm.getPixels(pixel, 0, picW, 0, 0, picW, picH);
		
		//find the black one of the picture for the square
		int diffNum;
		for(int y=0; y<picH; y++){
			for (int x=0; x<picW; x++){
				int index = y*picW + x;
				if (isSimmularColor(pixel[index], 0xffffffff)){		//is black
					diffNum = 0;
					diffNum += isSimmularColor(pixel, index, x, y-1)?1:0;		//u
					diffNum += isSimmularColor(pixel, index, x+1, y-1)?1:0;		//ur
					diffNum += isSimmularColor(pixel, index, x+1, y)?1:0;		//r
					diffNum += isSimmularColor(pixel, index, x+1, y+1)?1:0;		//rb
					diffNum += isSimmularColor(pixel, index, x, y+1)?1:0;		//b
					diffNum += isSimmularColor(pixel, index, x-1, y+1)?1:0;		//lb
					diffNum += isSimmularColor(pixel, index, x-1, y)?1:0;		//l
					diffNum += isSimmularColor(pixel, index, x-1, y-1)?1:0;		//ul	
					
					if(diffNum <=1){
						anotherp[index] = 0xff000000;
						isEdgeTable[x][y] = 0;
					}else if (diffNum <=3){
						anotherp[index] = 0xffff0000;
						isEdgeTable[x][y] = OUTCORNER;
					}else if (diffNum <=5){
						anotherp[index] = 0xff00ff00;
						isEdgeTable[x][y] = EDGE;
					}else if (diffNum <=7){
						anotherp[index] = 0xff0000ff;
						isEdgeTable[x][y] = INCORNER;
					}else{
						anotherp[index] = 0xffffffff;
						isEdgeTable[x][y] = INSIDE;
					}
				}else{
					anotherp[index] = 0xff000000;
					isEdgeTable[x][y] = 0;
				}
			}
		}
		
//		//find the edge of the picture for the square
//		for(int y=0; y<picH; y++){
//			for (int x=0; x<picW; x++){
//				int index = y*picW + x;
//				if (y == 0){
//					if (isSimmularColor(pixel[index], pixel[index+1])){
//						
//						pixel[index] = 0xff000000 | (pixel[index] ^ 0xffffff);
//					}
//				}else if (y == picH-1){
//					
//				}
//			}
//		}
		Log.i("2dMaking", "pixelsize£º"+pixel.length);
		
		
		infoNum = MIDDLEIFOR;
		if (changedBm == null){
			Log.e("2dMaking", "changedBm is empty!");
		}
		changedBm.setPixels(anotherp, 0, picW, 0, 0, picW, picH);
		return changedBm;
	}
	private Boolean isSimmularColor(int[] pixels, int index, int x, int y){
		if (x<0 || x>= thepicW || y<0 || y>=thepicH){
			return false;
		}
		return isSimmularColor(pixels[index], pixels[y*thepicW+x]);
	}
	private Boolean isSimmularColor(int pixel1, int pixel2){
		int r1 = (pixel1 >> 16) & 0xff;
		int g1 = (pixel1 >> 8) & 0xff;
		int b1 = pixel1 & 0xff;
		
		int r2 = (pixel2 >> 16) & 0xff;
		int g2 = (pixel2 >> 8) & 0xff;
		int b2 = pixel2 & 0xff;
		
		if((Math.abs(r1-r2)+Math.abs(b1-b2)+Math.abs(g1-g2)) < 0x10){
			return true;
		}
		return false;
	}
}
