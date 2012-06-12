package my.app.blumouse;

import java.util.Set;
import my.app.blumouse.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.bluetooth.*;


public class BlumouseActivity extends Activity {
	/** Called when the activity is first created. */

	final int REQUEST_ENABLE_BT = 120;
	static BluetoothSocket ConnectedSocket=null;
	BluetoothDevice extcomputer ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		BluetoothAdapter btadapter = BluetoothAdapter.getDefaultAdapter();

		if(btadapter == null)
		{ 
			System.out.println ("****no bluetooth adapter*****"); 
			Log.i("BlumouseActivity", "****no bluetooth adapter*****");

		}
		else
		{
			System.out.println ("****THERE IS A bluetooth adapter*****");
			Log.i("BlumouseActivity", "****THERE IS A bluetooth adapter*****");



			if (!btadapter.isEnabled()) {
				Log.i("BlumouseActivity","Bluetooth not enabled");
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
			}

		}
		
		Set<BluetoothDevice> pairedDevices = btadapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
			Log.i("aaa","there are paired devices!!");
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		   extcomputer = device ;
		   String addr = extcomputer.getAddress();
		   Log.i("aaa","paired devices address: " + addr);
		   }
			
		}
		
		TextView tv = (TextView)findViewById(R.id.touch_view);
		Button b1 = (Button)findViewById(R.id.leftbutton);
		Button b2 = (Button)findViewById(R.id.rightbutton);
		
		
		ConnectThread socket_operation = new ConnectThread(extcomputer,tv,b1,b2);
		
		socket_operation.start();
		
		
		
		
		
	}
	
	
	
	




	@Override
	protected void onActivityResult(int requestcode,int resultcode,Intent intent) {
		switch(requestcode){
		case REQUEST_ENABLE_BT:
			switch(resultcode){
			case RESULT_OK:
				Log.i("onActivityResult","Result ok :)");
				break;
			case RESULT_CANCELED:
				Log.i("onActivityResult","Result cancelled :(");
				break;
			}		
			break;	
		}
	}



}




