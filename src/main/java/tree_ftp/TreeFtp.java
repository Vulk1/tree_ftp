package tree_ftp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TreeFtp {

	private Directory rootDirectory;
	private FtpClient client;
	private String serverName;
	
	public TreeFtp(String serverName) {
		this.rootDirectory = new Directory("/");
		this.serverName = serverName;
		this.client = new FtpClient(serverName);
		
	}
	
	public void connect(String user, String password) throws InterruptedException {
		client.connect(user, password);
	}
	
	public void connectAnonymously() throws InterruptedException {
		client.connectAnonymously();
	}
	
	public void closeClient() {
		client.closeClient();
	}
	
	protected List<Directory> fromStringExtractDirectories(String message) {
		List<Directory> directories = new ArrayList<>();
		
		if (message.isBlank() || message == null) return null;
		
		String[] directoriesLines = message.split("\n");
		
		for(int i =0; i < directoriesLines.length; i++) {
			if (directoriesLines[i].startsWith("d")) {
				int delimit = directoriesLines[i].lastIndexOf(" ");
				if(delimit >= 0) {
					String directoryName = directoriesLines[i].substring(delimit+1, directoriesLines[i].length());
					System.out.println("On a extrait : " + directoryName);
					directories.add(new Directory(directoryName));
				}
			}
			
		}
		
		return directories;
	}
	
	protected List<String> fromStringExtractFiles(String message) {
		List<String> files = new ArrayList<>();
		
		String[] filesLines = message.split("\n");
		
		for(int i =0; i < filesLines.length; i++) {
			if (filesLines[i].startsWith("-")) {
				int delimit = filesLines[i].lastIndexOf(" ");
				if(delimit >= 0) {
					String fileName = filesLines[i].substring(delimit+1, filesLines[i].length());
					files.add(fileName);
				}
			}
		}
		
		return files;
	}
	
	public void get() throws IOException {
		
		doIt(rootDirectory);
		this.displayTree(rootDirectory, 1);
	}
	
	public Directory getRootDirectory() {
		return rootDirectory;
	}
	
	
	
	protected void doIt(Directory parentDirectory) throws IOException {
		System.out.println("Traitement du dir " + parentDirectory.getName());
		client.setCurrentDirectory(parentDirectory.getName());
		String content = client.list("");
		List<Directory> subDirectories = new ArrayList<>();
		subDirectories.addAll(this.fromStringExtractDirectories(content));
		
		if(!subDirectories.isEmpty()) {
		
			for(int i =0; i < subDirectories.size(); i++) {
				parentDirectory.addSubDirectory(subDirectories.get(i));
				System.out.println("On a ajouté le dir :  " + subDirectories.get(i).getName());

				doIt(subDirectories.get(i));
			}
		}
		
		
	}
	
	public void displayTree(Directory dir, int degree) {
		List<Directory> e = rootDirectory.getSubDirectories();
		
		System.out.println("---" + dir.getName());

		for(int i=0; i < e.size(); i++) {
			System.out.println("\t" + e.get(i).getName());
			displayTree(e.get(i), degree++);

		}
		
	}
	
	
	
	
}
