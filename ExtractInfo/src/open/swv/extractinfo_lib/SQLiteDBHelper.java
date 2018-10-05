package open.swv.extractinfo_lib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

public class SQLiteDBHelper {
	private String dbName = "";
	private Connection conn = null;

	public SQLiteDBHelper(String dbname)
	{
		this.dbName = dbname;
	}

	private Connection getConnection(){
		if (conn != null)
			return conn;

		boolean retValue = initDB();
		System.out.println( "init db: "+ retValue );
		return conn;
	}


	private boolean initDB(){
		try {
			// #1. JDBC driver loading
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);

		}
		catch (Exception ex) {
			return false;
		}
		return true;
	}


	public boolean init(){
		Connection conn = getConnection();

		// #1. Table µî·Ï
		Statement stat;
		try {
			stat = conn.createStatement();
			stat.executeUpdate("drop table if exists IgnoredWords;");
			stat.executeUpdate("create table IgnoredWords"
					+ " (word TEXT,blank1 TEXT);");
			stat.executeUpdate("drop table if exists DefaultView;");
			stat.executeUpdate("create table DefaultView"
					+ " (filename TEXT,blank1 TEXT);");
			stat.executeUpdate("drop table if exists SecondView;");
			stat.executeUpdate("create table SecondView"
					+ " (filename TEXT,blank1 TEXT);");
			stat.executeUpdate("drop table if exists CommonBlocks;");
			stat.executeUpdate("create table CommonBlocks"
					+ " (name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,blank2 TEXT,blank3 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists CommonValue;");
			stat.executeUpdate("create table CommonValue"
					+ " (common_block TEXT,name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,blank2 TEXT,blank3 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists LocalVariables;");
			stat.executeUpdate("create table LocalVariables"
					+ " (function TEXT,variable_name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,blank2 TEXT,blank3 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Remarks;");
			stat.executeUpdate("create table Remarks"
					+ " (filename TEXT,position TEXT,class TEXT,method_or_function TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Subroutines;");
			stat.executeUpdate("create table Subroutines"
					+ " (name TEXT,position TEXT,filename TEXT,attributes TEXT,blank1 TEXT,blank2 TEXT,blank3 TEXT,comment TEXT);");

			stat.executeUpdate("drop table if exists ReferredBy;");
			stat.executeUpdate("create table ReferredBy"
					+ " (ref_class TEXT, ref_symbol_name TEXT,ref_type TEXT,class TEXT,symbol TEXT,type TEXT,access TEXT,position TEXT, filename TEXT,caller_argument_types TEXT,ref_argument_types TEXT);");
			stat.executeUpdate("drop table if exists Classes;");
			stat.executeUpdate("create table Classes"
					+"(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,class_template TEXT,blank2 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Constants;");
			stat.executeUpdate("create table Constants"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,dec_type TEXT,blank1 TEXT,blank2 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Enumerations;");
			stat.executeUpdate("create table Enumerations"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,blank2 TEXT,blank3 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists EnumConstants;");
			stat.executeUpdate("create table EnumConstants"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,type TEXT,blank1 TEXT,blank2 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists ProjectFiles;");
			stat.executeUpdate("create table ProjectFiles"
					+ "(highlight_file TEXT,group_name TEXT,parsing_time TEXT);");
			stat.executeUpdate("drop table if exists FunctionDefinitions;");
			stat.executeUpdate("create table FunctionDefinitions"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,ret_type TEXT,arg_types TEXT,arg_names TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists SymbolsOfFiles;");
			stat.executeUpdate("create table SymbolsOfFiles"
					+ "(filename TEXT,start_position TEXT,class TEXT,identifier TEXT,type TEXT,end_position TEXT,high_start_pos TEXT,high_end_pos TEXT,arg_types TEXT);");
			stat.executeUpdate("drop table if exists Friends;");
			stat.executeUpdate("create table Friends"
					+ "(class TEXT, name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,ret_type TEXT,arg_types TEXT,arg_names TEXT, comment  TEXT);");
			stat.executeUpdate("drop table if exists Functions;");
			stat.executeUpdate("create table Functions"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,ret_type TEXT,arg_types TEXT,arg_names TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Variables;");
			stat.executeUpdate("create table Variables"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,type TEXT,template TEXT,parameter TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Inheritances;");
			stat.executeUpdate("create table Inheritances"
					+ "(class TEXT,base_class TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,class_template TEXT,inheritance_template TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Include;");
			stat.executeUpdate("create table Include"
					+ "(included_file TEXT,start_position TEXT,include_from_file TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,blank2 TEXT,blank3 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists IncludeList;");
			stat.executeUpdate("create table IncludeList"
					+ "(included_file TEXT,directory TEXT,blank1 TEXT);");
			stat.executeUpdate("drop table if exists InstanceVariables;");
			stat.executeUpdate("create table InstanceVariables"
					+ "(class TEXT,variable_name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,type TEXT,blank1 TEXT,blank2 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Macros;");
			stat.executeUpdate("create table Macros"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,blank2 TEXT,blank3 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists MethodDefinitions;");
			stat.executeUpdate("create table MethodDefinitions"
					+ "(class TEXT,name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,ret_type TEXT,arg_types TEXT,arg_names TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists MethodImplementations;");
			stat.executeUpdate("create table MethodImplementations"
					+ "(class TEXT,name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,ret_type TEXT,arg_types TEXT,arg_names TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists Typedefs;");
			stat.executeUpdate("create table Typedefs"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,original TEXT,blank1 TEXT,blank2 TEXT,comment TEXT);");
			stat.executeUpdate("drop table if exists RefersTo;");
			stat.executeUpdate("create table RefersTo"
					+ "(class TEXT,symbol_name TEXT,type TEXT,ref_class TEXT,ref_symbol TEXT,ref_type TEXT,access TEXT,position TEXT,filename TEXT,caller_argument_types TEXT,ref_argument_types TEXT);");
			stat.executeUpdate("drop table if exists Unions;");
			stat.executeUpdate("create table Unions"
					+ "(name TEXT,start_position TEXT,filename TEXT,end_position TEXT,attributes TEXT,blank1 TEXT,blank2 TEXT,blank3 TEXT,comment TEXT);");

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public boolean close() {
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public String MakeSQLInsertStatement(String tableName, Collection<String> dataList)
	{
		String sqlPrepareStatement = "insert or IGNORE into "+tableName+" values (";
		int i=0;
		for(String data : dataList)
		{
			sqlPrepareStatement += "'"+data+"'";
			if( i != (dataList.size() - 1) ) //last data
				sqlPrepareStatement += ", ";
			i++;
		}
		sqlPrepareStatement +=	");";
		return sqlPrepareStatement;
	}

	public String MakeSQLPreparedInsertStatement(int maxSize, String tableName, Collection<String> dataList)
	{
		String sqlPrepareStatement = "insert or IGNORE into "+tableName+" values (";

		for(int i=0; i < maxSize; i++)
		{
			sqlPrepareStatement += "?";
			if( i != (maxSize - 1) ) //last data
				sqlPrepareStatement += ", ";
		}
		sqlPrepareStatement +=	");";
		return sqlPrepareStatement;
	}

	private final int step = 1000;
	public boolean insertDB(String preparedStatemet,Collection<InsertDBItem> insertDBList)
	{
		Connection conn = getConnection();
		try {
			conn.setAutoCommit(false);
			int stepIdx = 0;
			PreparedStatement prep = conn.prepareStatement( preparedStatemet );

			for(InsertDBItem dbItem : insertDBList)
			{
				int i=0;
				for(String data: dbItem.getDataList()){
					i++;
					prep.setString(i, data);
				}
				prep.addBatch();
				stepIdx++;

				if ((stepIdx % step) == 0) {
					prep.executeBatch();
					conn.commit();
					prep.clearBatch();
					System.out.print(".");
				}

			}

			prep.executeBatch();
			conn.commit();
			prep.clearBatch();

			prep.close();
			conn.setAutoCommit(true);
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	private String[] getSeperateDot(String orgValue)
	{
		int pos = orgValue.lastIndexOf(".");
		String []ret = new String[2];
		ret[0] = orgValue.substring(0, pos);
		ret[1] = orgValue.substring(pos+1);
		return ret;
	}
	private String getType(String line, String instanceName)
	{
		line = line.replaceAll("\t", "");
		line = line.replaceAll(";", "");
		String[] seperateLine = line.split(" ");
		int findIndex = 0;
		for(int i=0; i < seperateLine.length; i++)
		{
			if( seperateLine[i].trim().equals(instanceName.trim()) )
			{
				findIndex = i-1;
				break;
			}
		}
		return seperateLine[findIndex];
	}

	private void insertInstanceVariablesType(String type, ResultSet rs)
	{
		String table = "InstanceVariables";

		Connection conn = getConnection();
        try {
        	String class_name = rs.getString("class");
    		String variable_name = rs.getString("variable_name");
    		String start_position = rs.getString("start_position");
    		String end_position = rs.getString("end_position");
    		String filename = rs.getString("filename");
    		String attributes = rs.getString("attributes");

    		String sql = "UPDATE "+table+" SET type=? WHERE ";
    		sql += "class='"+class_name+"'";
    		sql += " and ";
    		sql += "variable_name='"+variable_name+"'";
    		sql += " and ";
    		sql += "start_position='"+start_position+"'";
    		sql += " and ";
    		sql += "end_position='"+end_position+"'";
    		sql += " and ";
    		sql += "filename='"+filename+"'";
    		sql += " and ";
    		sql += "attributes='"+attributes+"'";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, type);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

	}

	public void selectAllInstanceVariables(){
		String table = "InstanceVariables";
		Connection conn = getConnection();
		String sql = "SELECT * FROM "+table;
        try
        {
        	Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            while(rs.next())
			{
            	if(rs.getString("type").length() != 0)
            		continue;

            	String filename = rs.getString("filename");
            	String[] start_position = getSeperateDot(rs.getString("start_position"));
            	String[] end_position = getSeperateDot(rs.getString("end_position"));
            	//System.out.println(start_position[0]+"," +start_position[1]);

            	int line = Integer.parseInt( start_position[0] );
            	int start_pos = Integer.parseInt( start_position[1] );
            	int end_pos = Integer.parseInt( end_position[1] );

            	//filename = filename.replaceFirst("/opt/workspace2.edu/Exampe1", "D:/eclipse/aaa");
            	//if(rs.getString("filename").equals("/opt/workspace2.edu/Exampe1/src/main/java/egovframework/example/sample/service/SampleVO.java"))

        		File file = new File(filename);
        		if( file.isFile() )
        		{
        			FileInputStream fstream = null;
					try {
						fstream = new FileInputStream(file);
						BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
						String strLine;
						for (int i=1; (strLine = br.readLine()) != null ; i++)   {
						  // Print the content on the console
							if(i == line)
							{
								//System.out.println (strLine);
								//System.out.println (getType(strLine, strLine.substring(start_pos, end_pos)));
								//System.out.println (strLine.substring(start_pos, end_pos));
								String type = getType(strLine, strLine.substring(start_pos, end_pos));
								insertInstanceVariablesType(type, rs);
							}
						}
						//Close the input stream
            			br.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
			}
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
