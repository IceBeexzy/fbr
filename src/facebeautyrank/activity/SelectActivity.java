/*
 * Project:			FaceBeautyRank
 * Author:			Chiubun
 * Version:			1.0
 * Created Date:	2010-12-5
 * CopyRight@2010 Guangzhou Sysu IceBee
 * 
 * History:
 * -----------------------------------------
 * Date			Author		Description
 * 2010-12-5	Chiubun		Set up
 */
package facebeautyrank.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
 * Classname:SelectActivity
 * show two options to use the software
 */
public class SelectActivity extends Activity {
	private Button useCamera;
	private Button useGallary;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selectactivitylayout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		useCamera = (Button)findViewById(R.id.UseCameraButton);
		useGallary = (Button)findViewById(R.id.FromGallaryButton);
		
		useCamera.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent toCamera = new Intent();
				toCamera.setClass(SelectActivity.this, UseCameraActivity.class);
				startActivity(toCamera);				
			}
		});
		
		useGallary.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Not construct yet.
				Toast.makeText(SelectActivity.this, "up coming", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
