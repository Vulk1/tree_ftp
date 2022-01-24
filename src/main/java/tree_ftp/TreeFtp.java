package tree_ftp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import displayers.Displayer;
import displayers.StandardDisplayer;

/*@author Le Guennec Yaakoub
 *@version 1.3
 *Permet de se connecter à un serveur ftp et d'en extraire l'arborescence des répertoires.
 * 
 */
public class TreeFtp {

	private String rootDirectory;
	private FtpClient client;
	private String serverName;
	private Displayer displayer;
	
	/*
	 * @param serverName L'adresse IP du server Ftp sur lequel se connecter (peut être un nom de domaine (résolution dns)
	 */
	public TreeFtp(String serverName) {
		this.rootDirectory = "/";
		this.serverName = serverName;
		this.client = new FtpClient(serverName);
		this.displayer = new StandardDisplayer();
		
	}
	
	/*
	 * Permet de s'authentifier à un server et d'y être connecté.
	 * 
	 * @param user Le nom d'utilisateur avec lequel se connecter au server
	 * @param password Le mot de passe avec lequel se connecter au server
	 */
	public void connect(String user, String password) throws InterruptedException {
		client.connect(user, password);
	}
	
	/*
	 * Permet de se connecter à un serveur avec une authentification anonyme
	 */
	public void connectAnonymously() throws InterruptedException {
		client.connectAnonymously();
	}
	
	/*
	 * Permet de proceder à l'exploration et l'affichage de l'arborescence des répertoires du server jusqu'à une profondeur maxDepth (-1 si on ne veut pas de limite)
	 * @param maxDepth Profondeur maximal à laquelle on explore l'arborescence du serveur.(égale à -1 si on ne se limite pas)
	 */
	public void get(int maxDepth) throws IOException {
		
		getTree(rootDirectory, 1, maxDepth);
	}
		
	/*Permet de proceder à l'exploration et l'affichage de l'arborescence des répertoires du server à partir du répertoire 'parentDirectory'.
	 * @param parentDirectory Le répertoire à partir duquel l'exploration de l'arborescence se fait.
	 * @param depth Indique la profondeur à laquelle le répertoire 'parentDirectory' se trouve.
	 * @param maxDepth La profondeur maximal à partir de 'parentDirectory' à laquelle on peut acceder.
	 * 
	 */
	protected void getTree(String parentDirectory, int depth, int maxDepth) throws IOException {
		
		if(maxDepth > 0 && depth > maxDepth)
			return;
		
		client.setCurrentDirectory(parentDirectory);

		List<String> content = client.list();
		
		List<String> subDirectories = new ArrayList<>(this.fromStringExtractDirectories(content));
		List<String> files = new ArrayList<>(this.fromStringExtractFiles(content));
		
		displayer.addDirectory(parentDirectory, depth);
		displayer.addFiles(files, depth+1);

		if(!subDirectories.isEmpty() && subDirectories != null) {
			// On ajoute tous les repéroires fils au repertoire parents
			for(int i =0; i < subDirectories.size(); i++) {
				
				getTree(subDirectories.get(i), depth+1, maxDepth);
				client.setAsParentDirectory();
			}
			
			
		}		
	}
	
	/*
	 * Permet d'extraire les noms des répertoires à partir d'une réponse de la commande list
	 * @param responses Liste d'informations des répertoires (droits, etc..)
	 * @return La liste des noms de répertoires
	 */
	protected List<String> fromStringExtractDirectories(List<String> responses) {
		List<String> directories = new ArrayList<>();

		if (responses.isEmpty() || responses == null) return null;
				
		for(int i =0; i < responses.size(); i++) {
			String directory = responses.get(i);
			
			if (directory.startsWith("d")) {
				int delimit = directory.lastIndexOf(" ");
				
				if(delimit >= 0) {
					String directoryName = directory.substring(delimit+1, directory.length());
					directories.add(directoryName);
				}
			}
			
		}
		return directories;
	}

	/*
	 * Permet d'extraire les noms des fichiers à partir d'une réponse de la commande list
	 * @param responses Liste d'informations des fichiers (droits, etc..)
	 * @return La liste des noms de fichiers
	 */
	protected List<String> fromStringExtractFiles(List<String> responses) {
		List<String> files = new ArrayList<>();
				
		for(int i =0; i < responses.size(); i++) {
			String file = responses.get(i);
			if (file.startsWith("-")) {
				int delimit = file.lastIndexOf(" ");
				if(delimit >= 0) {
					String fileName = file.substring(delimit+1, file.length());
					files.add(fileName);
				}
			}
		}
		
		return files;
	}
	
	/*
	 * @return Le répertoire parent absolue du serveur ftp.
	 */
	public String getRootDirectory() {
		return rootDirectory;
	}
	
	/*
	 * Ferme la connexion au serveur Ftp.
	 */
	public void closeClient() {
		client.closeClient();
	}
	
	

}
