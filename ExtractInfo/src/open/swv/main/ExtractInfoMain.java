package open.swv.main;

import open.swv.extractinfo_lib.Configration;
import open.swv.extractinfo_lib.GenDataFromSNDB;
import open.swv.extractinfo_lib.MakeInstanceVariables;

public class ExtractInfoMain {

	public static void ExtractInfomation() {
		GenDataFromSNDB p = new GenDataFromSNDB(Configration.getInstance().getDBName());
		p.extractInfo(Configration.getInstance().getSNDBPath(),
				Configration.getInstance().getSNDumpPath());
		p.close();

		MakeInstanceVariables mIv = new MakeInstanceVariables(Configration.getInstance().getDBName());
		mIv.make();
		mIv.close();
	}

	public static void main(String[] args) {

		if( !Configration.parseParmeter(args) )
			System.exit(0);

		ExtractInfomation();
	}

}
