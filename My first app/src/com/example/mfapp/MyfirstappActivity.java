package com.example.mfapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

public class MyfirstappActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);}
    
    public void leftmousefn(View view) { 
    	Button lebutton = (Button) findViewById(R.id.leftbutton);
    	//function when left mouse is clicked
    	}
    
    public void rightmousefn(View view) { 
    	Button rtbutton = (Button) findViewById(R.id.rightbutton);
    	//function when rightmouse is clicked
    	}
}
