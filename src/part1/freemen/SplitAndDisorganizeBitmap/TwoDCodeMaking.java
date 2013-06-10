package part1.freemen.SplitAndDisorganizeBitmap;

import java.nio.charset.Charset;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class TwoDCodeMaking {
	private final static int CODEHEIGHT = 300;
	private final static int CODEWIGHT = 300;
	public String twoDString = null;
	public Bitmap twoDBitmap = null;
	
	public TwoDCodeMaking(){
		
	}
	
	public Bitmap string2Bitmap(String msg){
		twoDBitmap = Bitmap.createBitmap(CODEWIGHT, CODEHEIGHT, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(twoDBitmap);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, CODEWIGHT, CODEHEIGHT, paint);
		
		byte[] byteMsg = msg.getBytes();
		String byte2StringMsg = "";
		for(int i=0; i<byteMsg.length; i++){
			byte2StringMsg+=Integer.toBinaryString(byteMsg[i]);
		}
		
		Log.i("TwoDCodeMaking","len:"+byteMsg.length+"byteMsg:"+byte2StringMsg
				+"\nbyteString:"+byte2StringMsg.length());
		paint.setColor(Color.BLACK);
		float dx = CODEWIGHT/10;
		float dy = CODEHEIGHT/10;
		float left = 0, right, top = 0, bottom;
		for (int i=0; i<10; i++){
			bottom = top + dy;
			for (int j = 0; j<10; j++){
				right = left + dx;
				canvas.drawRect(left, top, right, bottom, paint);
//				Log.i("TwoDCodeMaking", "l:"+String.valueOf(left)
//						+" t:"+String.valueOf(top)
//						+" r:"+String.valueOf(right)
//						+" b:"+String.valueOf(bottom));
				left = right + dx;
			}
			top = bottom + dy;
			left = 0;
		}
		
		return twoDBitmap;
	}
	
	public String bitmap2String(Bitmap bm){
		
		return null;
	}
	
}
