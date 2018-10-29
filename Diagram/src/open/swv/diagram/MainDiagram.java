package open.swv.diagram;

import java.sql.SQLException;

public class MainDiagram {

	public static void main(String[] args) {
		GenerateDiagram cd = new GenerateDiagram();
		if(args.length != 4)
		{
			System.out.println("Parameter is not enough");
			System.out.println("[1] db path [2] graphViz(dot) path [3] out img path [4] xml out file path");
			return ;
		}
		
		cd.setConnection(args[0]);//DB 경로
		try {
			cd.addTable();
			cd.makeContents(args[1], args[2]);//Graphviz 경로
			cd.xml(args[3]);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
