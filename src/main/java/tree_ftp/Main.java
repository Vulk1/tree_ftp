package tree_ftp;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		
		assert(args.length > 0);
		boolean authentificationMode = false;
		boolean depthLimited = false;
		
		if(args.length == 3)
			authentificationMode = true;
		
		if(args.length == 4)
			depthLimited = true;
		
		TreeFtp tree = new TreeFtp(args[0]);
		
		try {
			if(authentificationMode)
				tree.connect(args[1], args[2]);
			else
				tree.connectAnonymously();
			
			if(depthLimited)
				tree.get(Integer.parseInt(args[3]));
			else
				tree.get(-1);
			
			tree.closeClient();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
			
	}
}
