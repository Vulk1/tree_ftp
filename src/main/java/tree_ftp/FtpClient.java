package tree_ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/*@author Le Guennec Yaakoub
 *@version 1.3
 *Permet de cr�er une instance de connexion � un serveur Ftp et de communiquer avec ce dernier.
 * 
 */
public class FtpClient {

	private Socket commande_socket;
	private Socket data_socket;
	    
	private BufferedReader commande_buffReader;
	private PrintWriter commande_printer;
	private BufferedReader data_buffReader;
	private PrintWriter data_printer;
	

	private String serverInetAdress;
	private String user;
	private String password;
	
	/*
	 * Cr�e une instance de connexion � un serveur ftp 
	 * @param serverInetAdress L'adresse IP du server Ftp sur lequel se connecter (peut �tre un nom de domaine (r�solution dns)
	 */
	FtpClient(String serverInetAdresse) {
		
		this.serverInetAdress = serverInetAdresse;
		
		try {
			this.commande_socket = new Socket(serverInetAdresse, 21);
			
			this.commande_printer = new PrintWriter(commande_socket.getOutputStream(), true);
			this.commande_buffReader = new BufferedReader(new InputStreamReader(commande_socket.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/*
	 * Permet de s'authentifier � un server et d'y �tre connect�.
	 * 
	 * @param user Le nom d'utilisateur avec lequel se connecter au server
	 * @param password Le mot de passe avec lequel se connecter au server
	 */
	public void connect(String user, String password) throws InterruptedException {
		String message;

		try {
			
			message = this.getCommandResponse();
			
			this.sendCommand("USER " + user);
			message = this.getCommandResponse();
			
			this.sendCommand("PASS " + password);
			message = this.getCommandResponse();
			
			System.out.println("Connected to server as " + user + " on " + this.serverInetAdress + "\n");
			
			
		} catch (IOException e) {

			e.printStackTrace();
		}

		
		
	}
	
	/*
	 * Permet de se connecter � un serveur avec une authentification anonyme
	 */
	public void connectAnonymously() throws InterruptedException {
		
		this.connect("anonymous", "anonymous");
		
	}
	
	/*
	 * Envoie la commande 'message' au server Ftp
	 * @param message Commande � envoyer au server ftp
	 */
	public void sendCommand(String message) throws IOException {
		if(commande_socket== null)  {
		      throw new IOException("FTP Server is not connected.");
		} else {
			
			commande_printer.println(message);
		}
	}
	
	/*
	 * Permet de r�cuperer la r�ponse du server sur le canal de donn�e.
	 * @return Liste des donn�es (par ligne) r�cup�r�es.
	 */
	public  List<String> getDataResponse() throws IOException {
		
		List<String> responses = new ArrayList<>();
		String line = "";
		
		boolean continueExtract = true;

		do {
			line = this.data_buffReader.readLine();
			if(line != null) {
				responses.add(line);
			} else {
				continueExtract = false;
			}
		}while(continueExtract);

		return responses;
	}
	
	/*
	 * Permet de r�cuperer la r�ponse du server sur le canal de commande.
	 * @return La r�ponse du server sur le canal de commande.
	 */
	public  String getCommandResponse() throws IOException {
		String line = "";
		String responseStr = "";
		
		line = this.commande_buffReader.readLine();
		responseStr+=line;
		
		return responseStr;
	}
	
	/*
	 * Permet de passer la communication avec le serveur en mode passif.
	 */
	public void passiveMode() throws IOException {
		this.sendCommand("PASV");
		String message = this.getCommandResponse();

		this.data_socket = this.getDataSocketFromString(message);
		this.data_printer = new PrintWriter(data_socket.getOutputStream(), true);
		this.data_buffReader = new BufferedReader(new InputStreamReader(data_socket.getInputStream()));
		

	}
	
	/*
	 * Envoie la commande trivial LIST au server et de r�cuperer le contenu du r�pertoire courant.
	 * @return La liste des dossiers et fichiers (avec leurs droits et autres informations).
	 */
	public List<String> list() throws IOException {
		
		this.passiveMode();
		this.sendCommand("LIST");
		
		this.getCommandResponse();
		this.getCommandResponse();

		return this.getDataResponse();
	}
	
	/*
	 * Permet de se placer dans le sous-r�pertoire 'directoryName'
	 * @param directoryName Le nom du sous-repertoire auquel on souhaite se d�placer.
	 * @return La r�ponse du serveur � la requete.
	 */
	public String setCurrentDirectory(String directoryName) throws IOException {
		this.sendCommand("CWD " + directoryName);

		return this.getCommandResponse();

	}
	
	/*
	 * Permet de r�cuperer le r�pertoire courant dans le serveur ftp.
	 * @return Le r�pertoire courant.
	 */
	public String getCurrentDirectory() throws IOException {
		this.sendCommand("PWD");

		return this.getCommandResponse();

	}
	
	/*
	 * Permet de se placer dans le r�pertoire parent.
	 * @return La r�ponse du serveur � la requete.
	 */
	public String setAsParentDirectory() throws IOException {
		this.sendCommand("CDUP");

		return this.getCommandResponse();

	}
	

	/*
	 * Ferme la connexion au serveur Ftp.
	 */
	public void closeClient()  {
		try {
			this.sendCommand("QUIT");
			
			if(data_socket != null)
				data_socket.close();
			
			commande_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 * Permet de cr�er un socket � partir du message du server suite � la requete du passage en mode passif.
	 * @param message Message de confirmation du serveur du passage en mode passif.
	 * @return Un socket sur l'adresse Ip et le port indiqu� par le serveur pour la communication en mode passif.
	 */
	public Socket getDataSocketFromString(String message) throws IOException {

		String ip = null;
	    int port = -1;
	    int opening = message.indexOf('(');
	    int closing = message.indexOf(')', opening + 1);
	   
	    if (closing > 0) {
	      String dataLink = message.substring(opening + 1, closing);
	      StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
	      
	      try {
	        ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
	            + tokenizer.nextToken() + "." + tokenizer.nextToken();
	        port = Integer.parseInt(tokenizer.nextToken()) * 256
	            + Integer.parseInt(tokenizer.nextToken());
	      } catch (Exception e) {
	        throw new IOException("FTP server received bad data link information: "
	            + message);
	        
	      }
	    }
	    
	    return new Socket(ip, port);
	}
		
}
