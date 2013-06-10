package part1.freemen.SplitAndDisorganizeBitmap;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	private ImageView imageView;
	private String path;
	private BitmapHandler bHandler = new BitmapHandler();
	private Bitmap showBitmap = null;
	private TwoDCodeMaking twoDCodeMaking = new TwoDCodeMaking();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView1);
        
        ((Button)(findViewById(R.id.button1))).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				path = "/sdcard/Test/"
						+((TextView)(findViewById(R.id.editText1))).getText().toString()
						+".jpg";
				showBitmap = bHandler.getBitmap(path);
				if (showBitmap != null){
					Log.v("MainActivity","read it");
				}else{
					Log.i("MainActivity", "no");
				}
				imageView.setImageBitmap(showBitmap);
			}
		});
        
        ((Button)(findViewById(R.id.buttonSave))).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (showBitmap != null){
					bHandler.saveBitmap(showBitmap);
					Log.v("MainActivity", "Bitmap Saved~");
				}else{
					Toast.makeText(getApplicationContext(), "no photo to save", Toast.LENGTH_SHORT).show();
					Log.e("MainActivity", "showBitmap is empty!");
				}
			}
		});
        
        ((Button)(findViewById(R.id.buttonTest))).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String text = ((TextView)findViewById(R.id.editText1)).getText().toString();
				showBitmap = twoDCodeMaking.string2Bitmap(text);
				imageView.setImageBitmap(showBitmap);
				
			}
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
