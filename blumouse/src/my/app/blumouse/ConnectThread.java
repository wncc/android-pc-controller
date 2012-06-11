package my.app.blumouse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


class ConnectThread extends Thread{
	private final UUID MY_UUID = UUID.fromString("94f39d29-7d6d-437d-973b-fba39e49d4ee");
	private final BluetoothSocket mmSocket;
	private final BluetoothDevice mmDevice;

	public ConnectThread(BluetoothDevice device) {
		// Use a temporary object that is later assigned to mmSocket,
		// because mmSocket is final
		
		BluetoothSocket tmp = null;
		mmDevice = device;

		// Get a BluetoothSocket to connect with the given BluetoothDevice
		/*try {
			// MY_UUID is the app's UUID string, also used by the server code
			tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
			
		} catch (IOException e) {Log.i("Socket operation","socket's not up! :("); } */
		
		BluetoothDevice hxm = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.getAddress());
		
		Method m;
		try{
		m = hxm.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
			Log.i("TRY!!","This try happened");
			tmp = (BluetoothSocket)m.invoke(hxm, Integer.valueOf(1));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		Log.i("Socket operation","socket's up!!!!");
		Log.i("socket info", tmp.toString());
		
		mmSocket = tmp;
		
	}

	public void run() {
		Log.i("socket operation" , "successfully started");
		// Cancel discovery because it will slow down the connection
		BluetoothAdapter btadapter = BluetoothAdapter.getDefaultAdapter();
		btadapter.cancelDiscovery();

		try {
			// Connect the device through the socket. This will block
			// until it succeeds or throws an exception
			
			mmSocket.connect();
			Log.i("socket operation" , "trying to connect");
			//BlumouseActivity.GiveConnectedSocket(mmSocket);
		} catch (IOException connectException) {
			// Unable to connect; close the socket and get out
			Log.i("socket operation" , "caught ioexception");
			Log.i("Message", connectException.getMessage());
			Log.i("LMessage", connectException.getLocalizedMessage());
			try {
				mmSocket.close();
			} catch (IOException closeException) { }
			return;
		}

		// Do work to manage the connection (in a separate thread)
		//manageConnectedSocket(mmSocket);
		Log.i("socket operation" , "completed connect thread");
		
		
		ConnectedThread thr1 = new ConnectedThread(mmSocket);
		MoveThread Comm = new MoveThread(thr1);
		Comm.start();
		/*thr1.start();
		byte[] bytearray = new byte[]{121,111,92,109,47};
		thr1.write(bytearray);*/
		
	}

	 //Will cancel an in-progress connection, and close the socket 
	public void cancel() {
		try {Log.i("socket operation" , "closing socket");
			mmSocket.close();
		} catch (IOException e) { }
	}
	
	public  BluetoothSocket GiveSocket(){
		return mmSocket;
	}
}
 


/*BluetoothDevice hxm = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(device.getAddress());
Method m;
m = hxm.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
socket = (BluetoothSocket)m.invoke(hxm, Integer.valueOf(1)); */
 
