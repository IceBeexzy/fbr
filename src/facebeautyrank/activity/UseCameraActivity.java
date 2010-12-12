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
 * 2010-12-6	Chiubun		Finish camera function
 * 2010-12-7	Chiubun		Add auto focus before take picture
 * 2010-12-8	Chiubun		Modify after code review 
 */
package facebeautyrank.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

/*
 * Classname:UseCameraActivity
 * Show the frame of camera and allow user to 
 * take picture and then confirm
 */
public class UseCameraActivity extends Activity implements SurfaceHolder.Callback{
	private Button sureButton;
	private Button takePhotoButton;
	private SurfaceView photoSurfaceView;
	private SurfaceHolder photoSurfaceHolder;
	private String photoStorePath = "/sdcard/FaceBeautyRank/images/";
	private boolean cameraPreviewMode = false;
	private Camera camera;
	private AutoFocusCallback mAutoFocusCallback = 
          new AutoFocusCallback(); 
	private String filename;	
	private boolean hasTakePhoto = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setWindowFormat();
		setContentView(R.layout.usecameraactivitylayout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		initSurfaceView();
		
		sureButton = (Button)findViewById(R.id.SureButton);
		takePhotoButton = (Button)findViewById(R.id.TakePhotoButton);	
		
		sureButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(hasTakePhoto)
				{
					Intent drawLineIntent = new Intent();
					Bundle imageInfo = new Bundle();
					imageInfo.putString("source", "fromCamera");
					imageInfo.putString("imagePath", photoStorePath + filename);
					drawLineIntent.putExtras(imageInfo);
					drawLineIntent.setClass(UseCameraActivity.this, DrawLineActivity.class);
					startActivity(drawLineIntent);					
				}
				else
				{
					Toast.makeText(UseCameraActivity.this, "Please take a photo of yourself.", Toast.LENGTH_SHORT).show();
				}			
			}
		});
		
		takePhotoButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cameraPreviewMode)
				{
					if(checkSDCard())
					{
						camera.autoFocus(mAutoFocusCallback);
					}
					else
					{
						Toast.makeText(UseCameraActivity.this, "No SDcard found. Can't take picture!",
								Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					resetCamera();
					initCamera();					
				}
			}
		});
		
		//Alert user no sdcard found
		if(!checkSDCard())
		{
			Toast.makeText(UseCameraActivity.this, "No SDcard Found!", 
					Toast.LENGTH_LONG).show();			
		}		
		
	}

	private void initSurfaceView() {
		photoSurfaceView = (SurfaceView)findViewById(R.id.PhotoSurfaceView);
		photoSurfaceHolder = photoSurfaceView.getHolder();
		photoSurfaceHolder.addCallback(UseCameraActivity.this);
		photoSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	private void setWindowFormat() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		resetCamera();
	}
	
	private void initCamera() {
		// TODO Auto-generated method stub
		if(cameraPreviewMode == false)
		{
			camera = Camera.open();				
		}
		if(camera != null && cameraPreviewMode == false)
		{
			try {
				//set camera parameters
				camera.setPreviewDisplay(photoSurfaceHolder);
				Camera.Parameters parameters = camera.getParameters();
				parameters.setPictureFormat(PixelFormat.JPEG);
				parameters.setPreviewSize(640, 480);
				parameters.setPictureSize(1024, 768);
				camera.setParameters(parameters);
				camera.setPreviewDisplay(photoSurfaceHolder);
				
				camera.startPreview();
				
				cameraPreviewMode = true;				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}	
	}
	
	private void resetCamera() {
		if(camera != null)
		{
			camera.stopPreview();
			camera.release();
			camera = null;
			cameraPreviewMode = false;
		}		
	}
	
	private void takePicture() {
		if(camera != null && cameraPreviewMode == true)
		{
			camera.takePicture(null, null, jpegCallback);
			cameraPreviewMode = false;
			hasTakePhoto = true;
		}		
	}
	
	private boolean checkSDCard() {
		// TODO Auto-generated method stub
		if(android.os.Environment.getExternalStorageState().equals
				(android.os.Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		else
		{
			return false;			
		}		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		initCamera();
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		resetCamera();		
	}
	
	private PictureCallback jpegCallback = new PictureCallback() {		
		@Override
		public void onPictureTaken(byte[] _data, Camera _camera) {
			// TODO Auto-generated method stub
			Bitmap photoBitMap = BitmapFactory.decodeByteArray(_data, 0, _data.length);
			
			//set appropriate directory and filename
			setDiretoryAndFileName();
			
			//initial file
			File photoFile = new File(photoStorePath, filename);
			
			try {
				//store picture
				BufferedOutputStream photoBufferStream = new BufferedOutputStream(
						(new FileOutputStream(photoFile)));
				photoBitMap.compress(Bitmap.CompressFormat.JPEG, 100, photoBufferStream);
				try {
					photoBufferStream.flush();
					photoBufferStream.close();					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

		private void setDiretoryAndFileName() {
			File photoDirectory = new File(photoStorePath);
			photoDirectory.mkdirs();			
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh_mm_ss");
			String date = sdf.format(new java.util.Date());
			filename = date + ".jpg";
		}
	};
	
	public final class AutoFocusCallback implements android.hardware.Camera.AutoFocusCallback 
	{ 
		public void onAutoFocus(boolean focused, Camera camera) 
		{ 
			/* ∂‘µΩΩπµ„≈ƒ’’ */
			if (focused) 
			{ 
				takePicture();
			} 
		} 
	};
}
