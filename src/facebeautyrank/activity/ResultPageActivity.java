/*
 * Project:			FaceBeautyRank
 * Author:			XuZhiyang
 * Version:			1.1
 * Created Date:	2010-12-8
 * CopyRight@2010 GuangZhou Sysu IceBee
 * 
 * History:
 * -----------------------------------------
 * Date			Author		Description
 * 2010-12-8	XuZhiyang	Set up
 * 2010-12-9	XuZhiyang	Modify after code review 
 * 2010-12-10   XuZhiyang   Modify component setting
 */
package facebeautyrank.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/*
 * Classname:ResultPageActivity
 * Show the result after analyzing the data which are getting form user 
 */
public class ResultPageActivity extends Activity{
	private Button uploadResultButton;
	private Button backToMenuButton;
	private ImageView photoImage;
	private ImageView resultStarImageView;
	private TextView testStarResult;
	private TextView commentTextView;
	private int starLevel;
	private double result;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resultpageactivity);
		setComponent();
	}
	private void setComponent() {
		setUploadResultButton();
		setBackToMenuButton();
		setPhotoImage();
		setCommentTextView();
		displayAnalyticResult();
		
	}
	private void setUploadResultButton() {
		uploadResultButton = (Button)findViewById(R.id.UploadResultButton);
		
	}
	private void setBackToMenuButton() {
		backToMenuButton = (Button)findViewById(R.id.BackToMenuButton);
		backToMenuButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ResultPageActivity.this, SelectActivity.class);
				startActivity(intent);
				ResultPageActivity.this.finish();
			}
		});
		
	}
	private void setPhotoImage() {
		photoImage = (ImageView)findViewById(R.id.PhotoImage);
		photoImage.setImageResource(R.drawable.op1);
		
	}
	private void setCommentTextView() {
		commentTextView = (TextView)findViewById(R.id.CommentTextView);	
		
	}
	private void displayAnalyticResult() {
		resultStarImageView = (ImageView)findViewById(R.id.ResultStarImageView);
		testStarResult = (TextView)findViewById(R.id.testStarResult);
		Bundle getResult = this.getIntent().getExtras();
		starLevel = getResult.getInt("starLevel");
		result = getResult.getDouble("calResult");
		switch(starLevel){
		case 1:
			setStarImage(R.drawable.onestar);
			testStarResult.setText("1"+String.valueOf(result));
			break;
		case 2:
			setStarImage(R.drawable.twostars);
			testStarResult.setText("2"+String.valueOf(result));
			break;
		case 3:
			setStarImage(R.drawable.threestars);
			testStarResult.setText("3"+String.valueOf(result));
			break;
		case 4:
			setStarImage(R.drawable.fourstars);
			testStarResult.setText("4"+String.valueOf(result));
			break;
		case 5:
			setStarImage(R.drawable.fivestars);
			testStarResult.setText("5"+String.valueOf(result));
			break;
		default:
			setStarImage(R.drawable.fivestars);
			testStarResult.setText("other:"+String.valueOf(result));
			break;
		}
	}
	private void setStarImage(int resId) {
		resultStarImageView.setImageResource(resId);
		
	}

}
