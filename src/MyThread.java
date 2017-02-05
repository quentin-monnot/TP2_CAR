import java.util.HashMap;

public class MyThread extends Thread{
	
	private App myApp;
	private String ligne;
	
	public MyThread(String ligne){
		this.myApp = new App();
		this.ligne = ligne;
	}
	
	public void run(){
		myApp.countWords(ligne);
	}
	
	public HashMap<String, Integer> getMap(){
		return myApp.map;
	}
	
	public App getApp(){
		return myApp;
	}
		
}
