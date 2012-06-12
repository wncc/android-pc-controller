package my.app.blumouse;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MoveThread extends Thread{
	final ConnectedThread Stream;
	final TextView touchview;
	final Button left_button;
	final Button right_button;
	
	
	public MoveThread(ConnectedThread tmp,TextView tv,Button b1,Button b2){
		Stream = tmp;
		touchview = tv;
		left_button = b1;
		right_button = b2;
		Stream.start();
		touchview.setOnTouchListener(new View.OnTouchListener() {
            //@Override
            public boolean onTouch(View v, MotionEvent event) {
            	byte[] temp;
                touchview.setText("Touch coordinates : " +  String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()) +"::" + String.valueOf(event.getAction()));
                String tstr = "(" + String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()) + ")" +":" + String.valueOf(event.getAction());
                
                temp= tstr.getBytes();
                Stream.write(temp);   
                return true;}}); 
		
		left_button.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
            	byte[] temp = new byte[] {'l'};
            	//left_button.setText("clicked");
            	Stream.write(temp);
            	
            }}); 
		
		right_button.setOnClickListener(new View.OnClickListener() {
            //@Override
            public void onClick(View v) {
            	byte[] temp = new byte[] {'r'};
            	//right_button.setText("clicked");
            	Stream.write(temp);
            }}); 
		
		
		
	}
	
	
	public void run(){

		
		String init = "Connection Started :)";
		
		byte[] bytearray = init.getBytes();
		Stream.write(bytearray);
	}
}
