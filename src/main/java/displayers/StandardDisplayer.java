package displayers;

import java.util.List;

/*@author Le Guennec Yaakoub
 *@version 1.3
 *Permet d'afficher dans le bon format l'arborescence des r�pertoires.
 * 
 */

public class StandardDisplayer implements Displayer {

	/*
	 * Permet d'ajouter un r�pertoire � l'affichage de l'arborescence
	 * @param directory Le nom du r�pertoire � ajouter
	 * @param La profondeur � laquelle le r�pertoire 'directory' se trouve
	 */
	public void addDirectory(String directory, int depth) {
		if(depth > 1) {
			System.out.println("|  ".repeat(depth));
			System.out.print("|  ".repeat(depth-1));
			System.out.println("|__ " + directory);
		}
		else {
			System.out.println("__" + directory);
		}
	}
	
	/*
	 * Permet d'ajouter une liste de fichiers � l'affichage de l'arborescence
	 * @param files La liste des noms de fichiers � ajouter
	 * @param La profondeur � laquelle les fichier 'files' se trouvent.
	 */
	public void addFiles(List<String> files, int degree) {
		for(int j = 0; j < files.size(); j++) {
			if(degree > 1) {
				System.out.print("|  ".repeat(degree-1));
				//System.out.print("|   ".repeat(degree));
				System.out.println("|__ " + files.get(j));
			}
			else {
				System.out.println("__" + files.get(j));
			}
		}
		
	}
}
