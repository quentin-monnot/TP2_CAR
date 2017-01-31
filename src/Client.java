import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	public static void main(String[] args){
		try {
			// Création des flux d'envoi
	        Socket s = new Socket(InetAddress.getLocalHost(), 4000);
	        FileInputStream inf=new FileInputStream(new File("lorem-ipsum.txt"));
	        ObjectOutputStream out=new ObjectOutputStream(s.getOutputStream());               
	        byte buf[] = new byte[1024];
	        int n;                   
	        while((n=inf.read(buf))!=-1){
	        	out.write(buf,0,n);                   
	        }
	        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	        String response = in.readLine();
			System.out.println(response);
	        inf.close();
	        out.close();   
	        s.close();         
		}catch(Exception e) {
	        System.out.println("EnvoiFichier : Erreur lors de l'envoi du fichier "+e);
		}
	}
}
