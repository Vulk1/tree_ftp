package displayers;

import java.util.List;


/*@author Le Guennec Yaakoub
 *@version 1.3
 *Permet d'afficher dans le bon format l'arborescence des r�pertoires.
 * 
 */

public interface Displayer {

	public void addDirectory(String element, int degree);
	public void addFiles(List<String> element, int degree);
}
