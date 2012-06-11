package my.app.blumouse;

public class MoveThread extends Thread{
	final ConnectedThread Stream;
	
	public MoveThread(ConnectedThread tmp){
		Stream = tmp;
	}
	
	public void run(){

		Stream.start();
		byte[] bytearray = new byte[]{121,111,92,109,47};
		while(true){
		Stream.write(bytearray);}
	}
}
