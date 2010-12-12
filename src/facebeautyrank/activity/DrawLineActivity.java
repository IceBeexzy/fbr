/*
 * Project:			FaceBeautyRank
 * Author:			Chiubun,XuZhiyang
 * Version:			1.2
 * Created Date:	2010-12-5
 * CopyRight@2010 Guangzhou Sysu IceBee
 * 
 * History:
 * -----------------------------------------
 * Date			Author		Description
 * 2010-12-5	Chiubun		Set up
 * 2010-12-8	Chiubun		Modify after code review 
 * 2010-12-9    Chiubun     Modify image source justification
 * 2010-12-9	XuZhiyang	Modify after code review 
 * 2010-12-10	XuZhiyang	Modify UI design and data setting
 */
package facebeautyrank.activity;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Classname: DrawLineActivity
 * Allow user to draw lines which are needed by analyze system
 */
public class DrawLineActivity extends Activity {
	
	private ImageView photoImageView;
	private TextView displayDistanceTestView;
	private Button cancelButton;
	private Button nextStepButton;
	private Button confirmButton;
	private double lastPositionX=0;
	private double lastPositionY = 0;
	private double currentPositionX= 0;
	private double currentPositionY= 0;
	private double distanceBetweenTwoPoint=0;
	//UI控件SlidingDrawer，隐藏式抽屉显示提示窗口
	private SlidingDrawer tipsSlidingDrawer;
	private ImageView slidingDrawerHandle;
	private ImageView tipsImageView;
	private TextView tipsContent;
	
	private Analyse analyse=new Analyse();
	private int stepcount=1;
	private int starLevel =0;
	private double result = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.drawlineactivitylayout);
		//设置界面中的所有组件
		setComponent();        
	}
	private void setComponent() {
        setSlidingDrawer();
        setCancelButton();
        SetNextStepButton();
        setConfirmButton();
		photoImageView = (ImageView)findViewById(R.id.PhotoImageView);		
		setImageView();
        displayDistanceTestView = (TextView)findViewById(R.id.DisplayDistanceTestView);
		
	}
	private void setCancelButton() {
		//清空所有坐标信息
		cancelButton = (Button)findViewById(R.id.CancelButton);
		cancelButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				currentPositionX=0;
				currentPositionY=0;
				lastPositionX=0;
				lastPositionY=0;
				distanceBetweenTwoPoint=0;
				displayDistanceTestView.setText("x1:"+currentPositionX+
						" y1:"+currentPositionY+" distance:"+(int)distanceBetweenTwoPoint);
			}
		});
	}
	
	private void SetNextStepButton() {
		nextStepButton = (Button)findViewById(R.id.NextStepButton);
		nextStepButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(stepcount != 8){
					analyse.SetData(distanceBetweenTwoPoint);
					distanceBetweenTwoPoint = 0;
					stepcount++;
					tipsSlidingDrawer.open();
				}
			}
		});
	}
	
	private void setConfirmButton() {
		confirmButton = (Button)findViewById(R.id.ConfirmButton);
	       
		confirmButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				analyse.calculate();
				setStarLevelAccordingToAnalyse();
				Bundle sendDistanceBetweenTwoPoint = new Bundle();
				sendDistanceBetweenTwoPoint.putInt("starLevel", starLevel);
				sendDistanceBetweenTwoPoint.putDouble("calResult", result);
				
				Intent intent = new Intent();
				intent.setClass(DrawLineActivity.this, ResultPageActivity.class);
				intent.putExtras(sendDistanceBetweenTwoPoint);
				startActivity(intent);
			}
		});
	}
	
	private void setSlidingDrawer() {
		tipsSlidingDrawer = (SlidingDrawer)findViewById(R.id.TipsSlidingDrawer);
        slidingDrawerHandle = (ImageView)findViewById(R.id.SlidingDrawerButton);
        tipsImageView = (ImageView)findViewById(R.id.TipsImageView);
        tipsContent = (TextView)findViewById(R.id.TipsContent);
        tipsSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
			public void onDrawerOpened() {
				slidingDrawerHandle.setImageResource(R.drawable.open);				
				tipsImageView.setImageResource(R.drawable.op1);
				hideView();
			}
		});
        tipsSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			public void onDrawerClosed() {
				slidingDrawerHandle.setImageResource(R.drawable.close);
				showView();
			}
		});
		
	}
	private void setImageView() {
		Bundle imageInfoBundle = this.getIntent().getExtras();
		
		String imageSource = imageInfoBundle.getString("source");
		if(imageSource.equals("fromCamera"))
		{
			String photoPath = imageInfoBundle.getString("imagePath");
			
			File photofile = new File(photoPath);
			if(photofile.exists())
			{
				Bitmap photoBitmap = BitmapFactory.decodeFile(photoPath);
				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				Bitmap newPhoto = Bitmap.createBitmap(photoBitmap,0,0,
						photoBitmap.getWidth(),photoBitmap.getHeight(),matrix,true);
				
				photoImageView.setImageBitmap(newPhoto);
			}
			
			Toast.makeText(DrawLineActivity.this, imageInfoBundle.getString("imagePath"), Toast.LENGTH_LONG).show();
		}
		else if(imageSource.equals("fromGallery"))
		{
			
		}
		
	}
	private void setStarLevelAccordingToAnalyse(){
    	starLevel = analyse.getStarLevel();
    	result = analyse.getAnalyticResult();
    }
    public void hideView(){
    	LinearLayout hide = (LinearLayout)findViewById(R.id.parent);
    	for(int i =0;i<hide.getChildCount();i++){
    		View v = hide.getChildAt(i);
    		if(v!=tipsSlidingDrawer){
    			v.setVisibility(View.GONE);
    		}
    	}
    }
    public void showView(){
    	LinearLayout show = (LinearLayout)findViewById(R.id.parent);
    	for(int i=0;i<show.getChildCount();i++){
    		View v = show.getChildAt(i);
    		v.setVisibility(View.VISIBLE);
    	}
    }
    public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			StoreLastPosition();
			break;
		case MotionEvent.ACTION_UP:
			getNewPostionAndDistance(event);
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
    }
    private void StoreLastPosition() {
		lastPositionX = currentPositionX;
		lastPositionY= currentPositionY;
	}
	private void getNewPostionAndDistance(MotionEvent event) {
		int pointerIndex = event.findPointerIndex(0);
		currentPositionX = event.getX(pointerIndex);
		currentPositionY = event.getY(pointerIndex);
		distanceBetweenTwoPoint = Math.sqrt(
				(currentPositionX-lastPositionX)*(currentPositionX-lastPositionX)
				+(currentPositionY-lastPositionY)*(currentPositionY-lastPositionY));
		displayDistanceTestView.setText("x1:"+currentPositionX
				+" y1:"+currentPositionY
				+" x2:"+lastPositionX
				+" y2:"+lastPositionY
				+" distance:"+(int)distanceBetweenTwoPoint);
	}
}
