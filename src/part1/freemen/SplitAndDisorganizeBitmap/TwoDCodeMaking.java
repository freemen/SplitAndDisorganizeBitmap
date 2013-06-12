package part1.freemen.SplitAndDisorganizeBitmap;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class TwoDCodeMaking {
	private final static int SMALLIFOR = 15;
	private final static int MIDDLEIFOR = 31;
	private final static int MUCHIFOR = 63;
	
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
		int xNum ;
		int yNum = xNum = SMALLIFOR;
		if (byteMsg.length*8 > usefulDataAmount(SMALLIFOR)){		//expend the part of 2d code
			if (byteMsg.length*8 > usefulDataAmount(MIDDLEIFOR)){
				xNum = yNum = MUCHIFOR;
			}else{
				xNum = yNum = MIDDLEIFOR;
			}
		}
		float dx = (float) CODEWIDTH /xNum;
		float dy = (float) CODEHEIGHT/yNum;				//10x10 code
		float left = 0, right, top = 0, bottom;
		
		//DRAW the POSITION MARK
		paint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, MARKSIZE*dx, MARKSIZE*dy, paint);
		canvas.drawRect(0, CODEHEIGHT-MARKSIZE*dy, MARKSIZE*dx, CODEHEIGHT, paint);
		canvas.drawRect(CODEWIDTH-MARKSIZE*dx, 0, CODEWIDTH, MARKSIZE*dy, paint);
		paint.setColor(Color.RED);
		canvas.drawRect(0+dx, 0+dy , MARKSIZE*dx-dx, MARKSIZE*dy-dy, paint);
		canvas.drawRect(0+dx, CODEHEIGHT-MARKSIZE*dy+dy, MARKSIZE*dx-dx, CODEHEIGHT-dy, paint);
		canvas.drawRect(CODEWIDTH-MARKSIZE*dx+dx, 0+dy, CODEWIDTH-dx, MARKSIZE*dy-dy, paint);
		paint.setColor(Color.BLUE);
		for (int i=MARKSIZE+1; i<yNum-MARKSIZE; i+=2){
			canvas.drawRect(i*dx, 0, i*dx+dx, dy*MARKSIZE, paint);
			canvas.drawRect(0, i*dy, dx*MARKSIZE, i*dy+dy, paint);
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
						|| i<MARKSIZE || j<MARKSIZE || i>=(xNum-MARKSIZE) || j>=(yNum-MARKSIZE)
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
		return (size-2*MARKSIZE)*(size-2*MARKSIZE);
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
	public String bitmap2String(Bitmap bm){
		int picW = bm.getWidth();
		int picH = bm.getHeight();
		changedBm = bm.copy(bm.getConfig(), true);
		int[] pixel = new int[picW*picH];
		Log.v("2dMaking", "w:"+picW+" h:"+picH);
		bm.getPixels(pixel, 0, picW, 0, 0, picW, picH);
		
		//change the picture's pixel
		//test:get the opposite color of the picture
		for(int y=0; y<picH; y++){
			for (int x=0; x<picW; x++){
				int index = y*picW + x;
				pixel[index] = 0xff000000 | (pixel[index] ^ 0xffffff);
			}
		}
		if (changedBm == null){
			Log.e("2dMaking", "changedBm is empty!");
		}
		changedBm.setPixels(pixel, 0, picW, 0, 0, picW, picH);
		return "hello";
	}
	public Bitmap showChangedBm(){
		return changedBm;
	}
}
