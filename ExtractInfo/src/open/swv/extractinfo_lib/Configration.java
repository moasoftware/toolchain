package open.swv.extractinfo_lib;

import java.io.File;

public class Configration {

	private String sndumpPath = "";
	private String sndbPath = "";
	private String dbName = "";
	private static Configration	configObject = new Configration();

	private Configration()
	{
	}
	
	private static void printUsage()
	{
		System.out.println("-----------------------------------");
		System.out.println("The usage of this program is below.");
		System.out.println("-----------------------------------");
		System.out.println("$ java -jar ExtractInfo.jar -sndump path -sndb path -db name");
		System.out.println("ex) $ java -jar ExtractInfo.jar -sndump ./SN-NG4.5/bin/dbdump -sndb ./SN-NG4.5/bin/SNDB4 -db recovery.db");
		System.out.println("ex) $ java -jar ExtractInfo.jar -sndump ./SN-NG4.5/bin/dbdump -sndb ./SN-NG4.5/bin/SNDB4");
	}

	public static boolean parseParmeter(String[] args)
	{
		if(args.length != 4 && args.length != 6)
		{
			printUsage();
			return false;
		}
		
		Configration configObject = getInstance();
		
		for(int i=0; i < args.length; i+=2)
		{
			if(args[i].equals("-sndump"))
			{
				configObject.setSNDumpPath(args[i+1]);
			}
			else if(args[i].equals("-sndb"))
			{
				configObject.setSndbPath(args[i+1]);
			}
			else if(args[i].equals("-db"))
			{
				configObject.setDbName(args[i+1]);
			}
		}
		
		if(configObject.getSNDumpPath().length() == 0)
		{
			printUsage();
			return false;
		}
		else
		{
			File check = new File(configObject.getSNDumpPath());
			if( !check.exists() )
			{
				System.out.println("ERROR: "+configObject.getSNDumpPath() + " is not exists" );
				printUsage();
				return false;
			}
		
		}
		if(configObject.getSNDBPath().length() == 0)
		{
			printUsage();
			return false;
		}
		else
		{
			File check = new File(configObject.getSNDBPath());
			if( !check.isDirectory() )
			{
				System.out.println("ERROR: "+configObject.getSNDBPath() + " is not exists or not directory" );
				printUsage();
				return false;
			}
		}
		
		if(configObject.getDBName().length() == 0)
		{
			configObject.setDbName("recoveryDB.db");
			System.out.println("db name is default : "+configObject.getDBName());
		}
		return true;
	}

	public static Configration	getInstance()
	{
		return configObject;
	}

	public String getSNDumpPath()
	{
		return sndumpPath;
	}

	public String getSNDBPath()
	{
		return sndbPath;
	}

	public String getDBName()
	{
		return dbName;
	}

	public void setSNDumpPath(String sndumpPath) {
		this.sndumpPath = sndumpPath;
	}

	public void setSndbPath(String sndbPath) {
		this.sndbPath = sndbPath;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
}
