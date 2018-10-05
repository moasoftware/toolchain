package open.swv.extractinfo_lib;

public class MakeInstanceVariables {
	private SQLiteDBHelper db = null;

	public MakeInstanceVariables(String dbname) {
		db = new SQLiteDBHelper(dbname);
	}
	public void close()
	{
		db.close();
	}
	public void make() {
		db.selectAllInstanceVariables();
	}
}
