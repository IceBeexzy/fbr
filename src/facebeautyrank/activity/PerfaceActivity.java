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

/*
 * Classname: PerfaceActivity
 * Show the main menu
 */
public class PerfaceActivity extends Activity {
	private Button enterButton;
	private Button helpButton;
	private Button quitButton;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfaceactivitylayout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        enterButton = (Button)findViewById(R.id.EnterButton);
        helpButton = (Button)findViewById(R.id.HelpButton);
        quitButton = (Button)findViewById(R.id.QuitButton);
        
        enterButton.setOnClickListener(new Button.OnClickListener()
        {
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		Intent toSelect = new Intent();
        		toSelect.setClass(PerfaceActivity.this, SelectActivity.class);
        		startActivity(toSelect);        		
        	}
        });
        
        helpButton.setOnClickListener(new Button.OnClickListener()
        {
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		Intent toHelp = new Intent();
        		toHelp.setClass(PerfaceActivity.this, HelpActivity.class);
        		startActivity(toHelp);        		
        	}
        });
        
        quitButton.setOnClickListener(new Button.OnClickListener()
        {
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		PerfaceActivity.this.finish();        		
        	}
        });
    }
}