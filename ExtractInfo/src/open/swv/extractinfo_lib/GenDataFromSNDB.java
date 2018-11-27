package open.swv.extractinfo_lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;


public class GenDataFromSNDB {

	private SQLiteDBHelper db = null;

	public GenDataFromSNDB(String dbname) {
		db = new SQLiteDBHelper(dbname);
		db.init();
	}
	public void close()
	{
		db.close();
	}
	public void extractInfo(String dir, String cmd) {
		File dirFile = new File(dir);
		if (! dirFile.exists()) {
			System.out.println(dir + " is not exists");
			return;
		}
		for(File f: dirFile.listFiles()) {			
				parseSANDB(cmd, f);
		}
		
	}

	private String getFileExt(String fileName) {
		int index = fileName.lastIndexOf(".");
        String fileExtension = fileName.substring(index + 1);
        return fileExtension;
	}

	private void parseSANDB(String cmd,File f)
	{
		String cmdLine = "" + cmd + " " + f.getAbsolutePath() +"";
		System.out.println(cmdLine);
		try {
			Process p = Runtime.getRuntime().exec(cmdLine);
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line;
			Vector<InsertDBItem>	inserDBList = new Vector<InsertDBItem>();
			int index = 0;
			String prepardStatement = null;
			while ((line = input.readLine()) != null) {
				//{} extract
				Vector<String> dataList = new Vector<String>();
				String[] itemAttributeList = line.split("\\{");
				String[] itemList = itemAttributeList[0].split(" |;");
				for(int i=0; i < itemList.length; i++)
				{
					dataList.add(itemList[i].trim());
				}
				//input to last one
				for(int i=1; i < itemAttributeList.length; i++)
				{
					itemAttributeList[i] = itemAttributeList[i].replaceAll("\\}", "");
					dataList.add(itemAttributeList[i].trim());
				}
				if(index == 0)
				{
					prepardStatement = db.MakeSQLPreparedInsertStatement(
							SNDBInfo.getMaxDataSizeForSNDB( getFileExt(f.getAbsolutePath()) ),
							SNDBInfo.getTypeMeanForSNDB( getFileExt(f.getAbsolutePath()) ),
							dataList);
				}

				inserDBList.add(new InsertDBItem(dataList));
				index++;
			}

			if(prepardStatement != null)
			{
				System.out.println("Insert DB : "+ getFileExt(f.getAbsolutePath()) +" file" );
				boolean outFlag = db.insertDB(prepardStatement, inserDBList);
				if(outFlag)
					System.out.println("Complete : "+ getFileExt(f.getAbsolutePath()) +" file" );
			}
			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
