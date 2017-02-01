import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class Serveur {
	
	private static ServerSocket ecoute;
	private static Socket service;

	public static void main(String[] args) throws NumberFormatException, InterruptedException{
		try{
            ecoute = new ServerSocket(4000);
            
            service = (Socket) null;
            while (true) {


                    service = ecoute.accept();
                    File file =new File("fichierATraiter.txt");
                    ObjectInputStream in=new ObjectInputStream(service.getInputStream());
					FileOutputStream out =new FileOutputStream(file);
                    byte buf[] = new byte[1024];
                    int n;
                    while((n=in.read(buf))!=-1){
                        out.write(buf,0,n);                                        
                    }
    				DataOutputStream sortie = new DataOutputStream(service.getOutputStream());
    				sortie.writeBytes("toto");
                    sortie.close();                    
                    service.close();
    				
            }
	    }catch(IOException e){
	            System.out.println("ServeurFichier : Erreur de réception du fichier");
	    }
	}
	
	public static String process(File f, int nbThread) throws FileNotFoundException, InterruptedException{
		
		StringBuilder[] lineText = new StringBuilder[nbThread];
		MyThread[] tabThread = new MyThread[nbThread];
		
		for(int i=0;i<nbThread;i++){
			lineText[i]=new StringBuilder();
		}
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(f);
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
