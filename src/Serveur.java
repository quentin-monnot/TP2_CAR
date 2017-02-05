import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;

public class Serveur {
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) throws NumberFormatException, InterruptedException{
		try{
			ServerSocket sv = new ServerSocket(4000);
	        while (true){
	        	Socket s=sv.accept();
	        	File file =new File("fichierATraiter.txt");
	        	DataOutputStream sortieMsg = new DataOutputStream(s.getOutputStream());
	        	receptionFichier(s,file);
	            String msg = process(file,Integer.parseInt(args[0]));
	            System.out.println(msg);
	            sortieMsg.writeBytes(msg+"\n");
	            file.delete();
	            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	            if(in.readLine() != null){
	            	System.out.println("ARRET DU SERVEUR !");
	            	sv.close();
	            	s.close();
	            	break;
	            }
	        }
	    }catch(IOException e){
	    	System.out.println("Une erreur est survenue ----> " + e);
	    }
	}
	
	public static void receptionFichier(Socket s,File file){
		try {
			 ObjectInputStream in = new ObjectInputStream(s.getInputStream());
			 byte[] content = (byte[]) in.readObject();
			 Files.write(file.toPath(), content);
			 
		} catch (IOException e) {
			System.out.println("ServeurFichier : Erreur de réception du fichier ----> " + e);
		} catch (ClassNotFoundException e) {
			System.out.println("Une erreur est survenue avec le fichier ----> " + e);
		}
	}
	
	public static String process(File f, int nbThread) throws FileNotFoundException, InterruptedException{
		
		StringBuilder[] lineText = new StringBuilder[nbThread];
		MyThread[] tabThread = new MyThread[nbThread];
		
		for(int i=0;i<nbThread;i++){
			lineText[i]=new StringBuilder();
		}
		
		sc = new Scanner(f);
		int j=0;
		
		while(sc.hasNextLine()){
			lineText[j]=lineText[j].append(sc.nextLine());
			j++;
			j%=nbThread;
		}
		
		for(int i=0;i<nbThread;i++){
			tabThread[i] = new MyThread(lineText[i].toString());
			tabThread[i].start();
		}
		
		for(int i=0;i<nbThread;i++){
			tabThread[i].join();
		}
	
		if(nbThread > 1){
			for(int i=1;i<nbThread;i++){
				fusion(tabThread[0].getMap(),tabThread[i].getMap());
			}
		}
		
		String mot = tabThread[0].getApp().findMax().getKey();
		int valeur = tabThread[0].getApp().findMax().getValue();
		
		return "Le mots le plus récurent est " + mot + " avec " + valeur + " apparition dans le texte.";
	}
	
	public static void fusion(HashMap<String, Integer> mainMap,HashMap<String, Integer> mergeMap){
		for (String key : mergeMap.keySet()) {
		    if(mainMap.containsKey(key)){
		    	mainMap.put(key, mainMap.get(key)+mergeMap.get(key));
		    }else{
		    	mainMap.put(key, mergeMap.get(key));
		    }
		}
	}
}
