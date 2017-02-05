import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class Client {
	
	static Socket s;
	static DataOutputStream out;
	
	public static void main(String[] args){
		try {
			s=new Socket("127.0.0.1",4000);
			File f = new File(args[0]);
			envoiFichier(s,f);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			String response = in.readLine();
			System.out.println(response);
			/* Le deuxieme parametre du jar definie l'arret du serveur */
			/* Si 1 alors le serveur continue sinon il s'arrete */
			/* Si aucun parametre n'est saisie alors on arrete le serveur */
			out = new DataOutputStream(s.getOutputStream());
			if(args.length != 2){
				out.writeBytes("0\n");
			}
			s.close();
		}catch(Exception e) {
			System.out.println("Une erreur est survenue ---> "+e);
		}
	}
	
	public static void envoiFichier(Socket s, File f){
		try {
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());             
	        byte content[] = Files.readAllBytes(f.toPath());
	        out.writeObject(content);
		}catch(Exception e){
			System.out.println("EnvoiFichier : Erreur lors de l'envoi du fichier "+e);
		}
	}
}

