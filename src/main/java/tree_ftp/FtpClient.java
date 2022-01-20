package tree_ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class FtpClient {

	private Socket commande_socket;
	private Socket data_socket;
	    
	private BufferedReader commande_buffReader;
	private PrintWriter commande_printer;
	private BufferedReader data_buffReader;
	private PrintWriter data_printer;
	
	byte[] commandeResponseCache;
	byte[] dataResponseCache;
	
	private String serverInerAdress;
	private String user;
	private String password;
	
	
	FtpClient(String serverInetAdresse) {
		
		this.serverInerAdress = serverInetAdresse;
		commandeResponseCache = new byte[1000];
		dataResponseCache = new byte[1000];

		
		try {
			this.commande_socket = new Socket(serverInetAdresse, 21);
			
			this.commande_printer = new PrintWriter(commande_socket.getOutputStream(), true);
			this.commande_buffReader = new BufferedReader(new InputStreamReader(commande_socket.getInputStream()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	public void connect(String user, String password) throws InterruptedException {
		String message;

		try {
			
			message = this.getCommandResponse();
			//System.out.println("message : " + message);

			this.sendCommand("USER " + user);

			message = this.getCommandResponse();
			//System.out.println("message : " + message);
			
			this.sendCommand("PASS " + password);
			message = this.getCommandResponse();
			//System.out.println("message : " + message);

			/*this.sendCommand("PASV");
			message = this.getCommandResponse();
			System.out.println("message : " + message);


			this.data_socket = this.getDataSocketFromString(message);
			this.data_printer = new PrintWriter(data_socket.getOutputStream(), true);
			this.data_buffReader = new BufferedReader(new InputStreamReader(data_socket.getInputStream()));*/
			
		} catch (IOException e) {


			/*if (!message.startsWith("227 ")) {
			      throw new IOException("SimpleFTP could not request passive mode: "
			          + message);
			    }*/

			/*this.data_socket = this.getDataSocketFromString(message);
			this.data_printer = new PrintWriter(data_socket.getOutputStream(), true);
			this.data_buffReader = new BufferedReader(new InputStreamReader(data_socket.getInputStream()));*/

		}

		
		
	}
	
	public void connectAnonymously() throws InterruptedException {
		
		this.connect("anonymous", "anonymous");
		
	}
	
	public void sendCommand(String message) throws IOException {
		if(commande_socket== null)  {
		      throw new IOException("FTP Server is not connected.");
		} else {
			
			commande_printer.println(message);
		}
	}
	
	public  String getDataResponse() throws IOException {
		int length = data_socket.getInputStream().read(dataResponseCache, 0, 1000); // autres cas ? exception
		String responseStr = new String(dataResponseCache, 0, 1000);
		resetCache(dataResponseCache);

		return responseStr;
	}
	
	public  String getCommandResponse() throws IOException {
		int length = commande_socket.getInputStream().read(commandeResponseCache, 0, 1000); // autres cas ? exception
		String responseStr = new String(commandeResponseCache, 0, 1000);
		resetCache(commandeResponseCache);

		return responseStr;
	}
	
	public String list(String entity) throws IOException {
		this.sendCommand("PASV");
		String message = this.getCommandResponse();

		this.data_socket = this.getDataSocketFromString(message);
		this.data_printer = new PrintWriter(data_socket.getOutputStream(), true);
		this.data_buffReader = new BufferedReader(new InputStreamReader(data_socket.getInputStream()));
		
		
		this.sendCommand("LIST " + entity);
		
		this.getCommandResponse();
		this.getCommandResponse();

		String response = this.getDataResponse();


		return response;
	}
	
	public String setCurrentDirectory(String directoryName) throws IOException {
		this.sendCommand("CWD " + directoryName);

		return this.getCommandResponse();

	}
	
	public String getCurrentDirectory() throws IOException {
		this.sendCommand("PWD");

		return this.getCommandResponse();

	}
	
	public static void resetCache(byte[] cache) {
		for(int i=0; i < cache.length; i++)
		{
			cache[i] = 0;
		}
	}
	
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
