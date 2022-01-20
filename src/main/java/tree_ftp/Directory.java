package tree_ftp;
import java.util.ArrayList;
import java.util.List;

public class Directory {

	
	private List<String> files;
	private List<Directory> subDirectories;
	private String directoryName;
	
	public Directory(String directoryName) {
		this.directoryName = directoryName;
		files = new ArrayList<>();
		subDirectories = new ArrayList<>();
	}
	
	public void addSubDirectory(Directory subDirectory) {
		this.subDirectories.add(subDirectory);
	}
	
	public void addFile(String file) {
		files.add(file);
	}
	public List<Directory> getSubDirectories() {
		return subDirectories;
	}
	public List<String> getFiles() {
		return files;
	}
	public String getName() {
		return directoryName;
	}
}
