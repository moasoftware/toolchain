package open.swv.annotation_terminator;
import java.io.File;
import java.util.ArrayList;

public class GetFileDir {
	ArrayList<String> filepath = new ArrayList<String>();

	public ArrayList<String> getFilePath() {
		return this.filepath;
	}

	public void fileDir(File file) {
		try {
			for (int i = 0; i < file.listFiles().length; i++) {
				try {
					if (file.isDirectory()) {
						fileDir(file.listFiles()[i]);
					}
				} catch (NullPointerException e) {
					String path = file.getPath() + "/" + file.list()[i];
					if (path.substring(path.length() - 4).equals("java")) {
						filepath.add(path.replace("\\", "/"));
					}
				}
			}
		} catch (NullPointerException e) {
			String path = file.getPath();
			if (path.substring(path.length() - 4).equals("java")) {
				filepath.add(path.replace("\\", "/"));
			}
		}
	}

	public void checkDir() {
		for (String path : filepath) {
			System.out.println(path);
		}
	}
}
