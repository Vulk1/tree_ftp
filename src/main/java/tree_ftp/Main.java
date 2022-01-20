package tree_ftp;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		/*FtpClient client = new FtpClient("ftp.ubuntu.com");
		
		
		try {
			client.connectAnonymously();
			//System.out.println("current dir : " + client.getCurrentDirectory().getCommandeResponse());
			String message = client.list("");
			System.out.println("contenu dir : " + message);
			
			String[] msgs = message.split("\n");
			for(int i =0; i < msgs.length; i++) {
				System.out.println(msgs[i]);
				if (msgs[i].startsWith("d")) {
					int delimit = msgs[i].lastIndexOf(" ");
					if(delimit >= 0) {
						System.out.println("valeur delimit : " + String.valueOf(delimit));
						String dirName = msgs[i].substring(delimit+1, msgs[i].length());
						System.out.println(dirName);
					}
				}
				
			}*/
			//System.out.println("reponse set dir : " + client.setCurrentDirectory("/ubuntu").getCommandeResponse());
			/*System.out.println("current dir : " + client.getCurrentDirectory());
			System.out.println("reponse set dir : " + client.setCurrentDirectory("/cdimage"));
			System.out.println("current dir : " + client.getCurrentDirectory());
			System.out.println("contenu dir : " + client.list(""));
			System.out.println("reponse set dir : " + client.setCurrentDirectory("/"));*/




			
			/*
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Fin du service");
		client.closeClient();*/
		TreeFtp tree = new TreeFtp("ftp.ubuntu.com");
		
		try {
			tree.connectAnonymously();
			tree.get();
	
			tree.closeClient();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
}
