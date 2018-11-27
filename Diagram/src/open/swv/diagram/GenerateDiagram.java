package open.swv.diagram;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GenerateDiagram {
	private Connection conn;
	String classDiagram = "";
	String couplingDiagram = "";

	public Connection getConnection(String path) {
		Connection conn = null;
		try {
			Class.forName("org.sqlite.JDBC").newInstance();
			conn = DriverManager.getConnection("jdbc:sqlite:" + path);
		} catch (Exception ex) {}

		return conn;
	}

	public void setConnection(String path) {
		this.conn = getConnection(path);
	}

	public void addTable() throws SQLException {
		Statement stat = conn.createStatement();

		stat.executeUpdate("drop table if exists Coupling;");
		stat.executeUpdate("drop table if exists Association;");
		stat.executeUpdate("drop table if exists Dependency;");
		stat.executeUpdate("drop table if exists Generalization;");
		stat.executeUpdate("create table Coupling ("
				+ "Caller TEXT, Callee TEXT, Data TEXT, Stamp TEXT, Control TEXT, External TEXT, Common TEXT, Content TEXT"
				+ ");");
		stat.executeUpdate("create table Association ("
				+ "Start TEXT, End TEXT"
				+ ");");
		stat.executeUpdate("create table Dependency ("
				+ "Start TEXT, End TEXT"
				+ ");");
		stat.executeUpdate("create table Generalization ("
				+ "Start TEXT, End TEXT"
				+ ");");
	}

	public void makeContents(String path, String graph) throws Exception {
		String contents = "digraph xx {\n\tnode[shape=record, style=filled, fillcolor=lightyellow];\n";
		contents += form();
		contents += association();
		contents += dependency();
		contents += generalization();
		contents += "}";
		//System.out.println(contents);
		genDot(path, contents, graph + File.separator +"ClassDiagram.png");
		classDiagram = graph + File.separator + "ClassDiagram.png";

		contents = "digraph xx {\n\tnode[shape=record, style=filled, fillcolor=lightyellow];\n";
		contents += callGraph();
		contents += "}";

		genDot(path, contents, graph + File.separator + "CouplingDiagram.png");
		couplingDiagram = graph + File.separator + "CouplingDiagram.png";
	}

	public String form() throws SQLException {
		String contents = "";
		Statement stat = conn.createStatement();
		Statement cStat = conn.createStatement();
		Statement iStat = conn.createStatement();
		Statement mStat = conn.createStatement();
		ArrayList<String> classList = new ArrayList<>();

		ResultSet rs = stat.executeQuery("select name from Classes");
		while (rs.next()) {
			String name = rs.getString("name");
			classList.add(name);
		}

		for (String className : classList) {
			contents += "\t" + className + " [label=<{";

			rs = cStat.executeQuery("select name, attributes from Classes where name='" + className + "'");
			while (rs.next()) {
				String checkClass = checkClass(rs.getString("name"), rs.getString("attributes"));
				contents += checkClass;
			}
			contents += "|";

			rs = iStat.executeQuery("select variable_name, attributes, type from InstanceVariables where class='" + className + "'");
			boolean next = rs.next();
			while (next) {
				String checkVar = checkVariable(rs.getString("variable_name"), rs.getString("attributes"), rs.getString("type"));
				contents += checkVar;
				next = rs.next();
				if (next) {
					contents += "<BR/>";
				}
			}
			contents += "|";

			rs = mStat.executeQuery("select name, attributes, ret_type, arg_types, arg_names from MethodImplementations where class='" + className + "'");
			next = rs.next();
			while (next) {
				String checkMethod = checkMethod(rs.getString("name"), rs.getString("attributes"), rs.getString("ret_type"), rs.getString("arg_types"), rs.getString("arg_names"));
				contents += checkMethod;
				next = rs.next();
				if (next) {
					contents += "<BR/>";
				}
			}

			contents += "}>]\n";
		}
		rs.close();
		stat.close();
		iStat.close();
		mStat.close();

		return contents;
	}

	private String checkClass(String name, String attributes) {
		String contents = "";
		switch(attributes) {
		case "0x4":
			contents = "<FONT FACE=\"boldfontname\">" + name + "</FONT>";
			break;
		case "0x401":
		case "0x404":
			contents = "&lt;&lt;interface&gt;&gt;<BR/><FONT FACE=\"boldfontname\">" + name + "</FONT>";
			break;
		case "0x11":
		case "0x14":
			contents = "<I><FONT FACE=\"boldfontname\">" + name + "</FONT></I>";
			break;
		}

		return contents;
	}

	private String checkVariable(String name, String attributes, String type) {
		String contents = "";
		switch(attributes) {
		case "0x1":
		case "0x21":
			contents = "-" + name + ": " + type;
			break;
		case "0x2":
		case "0x22":
			contents = "#" + name + ": " + type;
			break;
		case "0x4":
		case "0x24":
			contents = "+" + name + ": " + type;
			break;
		case "0x9":
		case "0x29":
			contents = "<u>-" + name + " : " + type + "</u>";
			break;
		case "0xa":
		case "0x2a":
			contents = "<u>#" + name + " : " + type + "</u>";
			break;
		case "0xc":
		case "0x2c":
			contents = "<u>+" + name + " : " + type + "</u>";
			break;
		}

		return contents;
	}

	private String checkMethod(String name, String attributes, String ret_type, String arg_types, String arg_names) {
		String contents = "";

		String[] argTypeList = arg_types.split(",");
		String[] argNameList = arg_names.split(",");
		String param = "";
		for (int i = 0; i < argTypeList.length; i++) {
			param += argNameList[i] + ": " + argTypeList[i];

			if (i != argTypeList.length - 1) {
				param += ", ";
			}

		}
		if (param.equals(": ")) {
			param = name + "():" + ret_type;
		} else {
			param = name + "(" + param + "): " + ret_type;
		}

		switch(attributes) {
		case "0x1":
		case "0x21":
			contents = "-" + param;
			break;
		case "0x2":
		case "0x22":
			contents = "#" + param;
			break;
		case "0x4":
		case "0x24":
			contents = "+" + param;
			break;
		case "0x9":
		case "0x29":
			contents = "<u>-" + param + "</u>";
			break;
		case "0xa":
		case "0x2a":
			contents = "<u>#" + param + "</u>";
			break;
		case "0xc":
		case "0x2c":
			contents = "<u>+" + param + "</u>";
			break;
		}

		return contents;
	}

	public String association() throws SQLException {
		String contents = "";
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select distinct type, class from InstanceVariables where type in (select name from Classes)");
		while (rs.next()) {
			PreparedStatement preStat = conn.prepareStatement("insert into Association values (?, ?)");
			String name = rs.getString("class");
			String type = rs.getString("type");
			preStat.setString(1, name);
			preStat.setString(2, type);
			preStat.execute();
			preStat.close();
			contents += "\t\"" + rs.getString("class") + "\"->\"" + rs.getString("type") + "\" [style=solid, arrowhead=vee];\n";
		}
		rs.close();
		stat.close();

		return contents;
	}

	public String dependency() throws SQLException {
		String contents = "";
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select distinct ref_class, class from RefersTo where class!=ref_class and ref_class in (select name from Classes)");
		while (rs.next()) {
			PreparedStatement preStat = conn.prepareStatement("insert into Dependency values (?, ?)");
			String name = rs.getString("class");
			String refName = rs.getString("ref_class");
			preStat.setString(1, name);
			preStat.setString(2, refName);
			preStat.execute();
			preStat.close();
			contents += "\t\"" + rs.getString("class") + "\"->\"" + rs.getString("ref_class") + "\" [style=dotted, arrowhead=vee];\n";
		}
		rs.close();
		stat.close();

		return contents;
	}

	public String generalization() throws SQLException {
		String contents = "";
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select class, base_class from Inheritances where base_class in (select name from Classes)");
		while (rs.next()) {
			PreparedStatement preStat = conn.prepareStatement("insert into Generalization values (?, ?)");
			String superClass = rs.getString("base_class");
			String subClass = rs.getString("class");
			preStat.setString(1, subClass);
			preStat.setString(2, superClass);
			preStat.execute();
			preStat.close();
			contents += "\t\"" + subClass + "\"->\"" + superClass + "\" [style=solid, arrowhead=onormal];\n";
		}
		rs.close();
		stat.close();

		return contents;
	}

	public String callGraph() throws SQLException {
		String contents = "";
		//HashMap<String, Integer> couplingValue = Coupling();

		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select name from Classes");
		while (rs.next()) {
			contents += "\t\"" + rs.getString("name") + "\"\n";
		}
		rs = stat.executeQuery("select * from Coupling");
		while (rs.next()) {
			String refer = rs.getString("Caller");
			String referred = rs.getString("Callee");
			String data = rs.getString("Data");
			String stamp = rs.getString("Stamp");
			String control = rs.getString("Control");
			String external = rs.getString("External");
			String common = rs.getString("Common");
			String content = rs.getString("Content");

			if (!refer.equals("")) {
				contents += "\t\"" + refer + "\"->\"" + referred + "\" [style=solid, arrowhead=vee, label=\"";
				if (!data.equals("0")) {
					contents += " DS:" + data;
				}
				if (!stamp.equals("0")) {
					contents += " ST:" + stamp;
				}
				if (!control.equals("0")) {
					contents += " CR:" + control;
				}
				if (!external.equals("0")) {
					contents += " EX:" + external;
				}
				if (!common.equals("0")) {
					contents += " CM:" + common;
				}
				if (!content.equals("0")) {
					contents += " CT:" + content;
				}
				contents += " \"]\n";
			}
		}

		stat.close();
		rs.close();

		return contents;
	}

	public void xml(String path) throws IOException, SQLException {
		String cClass = "";
		String cMethod = "";
		String cMember = "";
		int cLine = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		long time = System.currentTimeMillis();
		String date = sdf.format(new Date(time));
		System.out.println(date);

		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery("select count(*) as CNT from Classes");
		while (rs.next()) {
			cClass = rs.getString("CNT");
		}
		stat = conn.createStatement();
		rs = stat.executeQuery("select count(*) as CNT from MethodImplementations");
		while (rs.next()) {
			cMethod = rs.getString("CNT");
		}
		stat = conn.createStatement();
		rs = stat.executeQuery("select count(*) as CNT from InstanceVariables");
		while (rs.next()) {
			cMember = rs.getString("CNT");
		}
		stat = conn.createStatement();
		rs = stat.executeQuery("select end_position from SymbolsOfFiles where type='cl'");
		while (rs.next()) {
			cLine += Integer.parseInt(String.valueOf(rs.getString("end_position")).split("\\.")[0]);
		}

		String xml = "";
		xml = "<?xml version=\"1.0\" encoding=\"ASCII\"?>\n" +
				"<archVis:ArchitectureVisualizationModel xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\">\n" +
				"\t<content countClass=\"" + cClass + "\" countMethod=\"" + cMethod + "\" countMember=\"" + cMember + "\" srcLine=\"" + cLine +
				"\" img=\"" + classDiagram + "\" img=\"" + couplingDiagram + "\" date=\"" + date + "\"/>\n" +
				"</archVis:ArchitectureVisualizationModel>";
		File file = new File(path + File.separator +"score.xml");
		FileWriter writer = new FileWriter(file);
		writer.write(xml);
		writer.flush();
		writer.close();
	}

	public HashMap<String, Integer> Coupling() throws SQLException {
		String caller = "";
		String callee = "";
		int data = 0;
		int stamp = 0;
		int control = 0;
		int external = 0;
		int common = 0;
		int content = 0;

		HashMap<String, Integer> couplingValue = new HashMap<>();
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(
				"select ref_class, ref_symbol, class, symbol_name, access"
						+ " from RefersTo"
						+ " where ref_class in (select name from Classes) and type = 'mi' and (ref_type = 'mi' or ref_type = 'iv') and class != ref_class"
						+ " order by class asc, ref_class asc");
		while (rs.next()) {
			int data_coupling = 0;
			int stamp_coupling = 0;
			int control_coupling = 0;
			int external_coupling = 0;
			int common_coupling = 0;
			int content_coupling = 0;

			String rdSymbol = rs.getString("ref_symbol");
			String rClass = rs.getString("class");
			String rdClass = rs.getString("ref_class");
			String access = rs.getString("access");

			if (access.equals("w")) {
				content_coupling++;
			}

			Statement stat2 = conn.createStatement();
			ResultSet rs2 = stat2.executeQuery(
					"select attributes, arg_types from MethodImplementations where name='" + rdSymbol + "'");
			while (rs2.next()) {
				if (rdSymbol.length() > 3 && rdSymbol.substring(0, 3).equals("set")) {
					content_coupling++;
					continue;
				}

				String mAttribute = rs2.getString("attributes");
				String argument = rs2.getString("arg_types");
				String arg = argument.replaceAll("[^[{]^[}]]", "");
				String argum[] = arg.split(",");

				if (argum.length == 0) {
					data_coupling++;
				} else {
					for (int i = 0; i < argum.length; i++) {
						if (argum[i].equals("int") || argum[i].equals("long") || argum[i].equals("char")
								|| argum[i].equals("short") || argum[i].equals("float") || argum[i].equals("double")
								|| argum[i].equals("byte")) {
							data_coupling++;
						}
						else if (argum[i].equals("boolean")) {
							control_coupling++;
						}
						else if (argum[i].equals("File") || argum[i].equals("FileDescriptor")
								|| argum[i].equals("InputStream") || argum[i].equals("OutputStream")
								|| argum[i].equals("DataInput") || argum[i].equals("CharBuffer")
								|| argum[i].equals("CharSequence") || argum[i].equals("Charset")
								|| argum[i].equals("CharsetDecoder") || argum[i].equals("Reader")
								|| argum[i].equals("Writer")) {
							external_coupling++;
						} else if (argum[i].equals("")) {
							if (mAttribute.equals("0xc")) {
								common_coupling++;
							} else {
								data_coupling++;
							}
						}
						else {
							stamp_coupling++;
						}
					}
				}
			}
			Statement stat3 = conn.createStatement();
			ResultSet rs3 = stat3.executeQuery("select attributes from InstanceVariables where variable_name='" + rdSymbol + "'");
			while (rs3.next()) {
				String vAttributes = rs3.getString("attributes");
				if (vAttributes.equals("0x9")) {
					common_coupling++;
				} else {
					data_coupling++;
				}
			}

			Statement stat4 = conn.createStatement();

			int allCoupling = data_coupling + stamp_coupling + control_coupling + external_coupling + common_coupling + content_coupling;
			if (couplingValue.containsKey(rClass + "->" + rdClass)) {
				int value = couplingValue.get(rClass + "->" + rdClass);
				value += allCoupling;
				couplingValue.put(rClass + "->" + rdClass, value);
				data += data_coupling;
				stamp += stamp_coupling;
				control += control_coupling;
				external += external_coupling;
				common += common_coupling;
				content += content_coupling;
			} else {
				stat4.executeUpdate("insert into Coupling values ('" + caller + "', '" + callee + "', '" + data + "', '" + stamp + "', '" + control + "', '" + external + "', '" + common + "', '" + content + "')");
				couplingValue.put(rClass + "->" + rdClass, allCoupling);
				caller = rClass;
				callee = rdClass;
				data = data_coupling;
				stamp = stamp_coupling;
				control = control_coupling;
				external = external_coupling;
				common = common_coupling;
				content = content_coupling;
			}
		}
		stat.executeUpdate("insert into Coupling values ('" + caller + "', '" + callee + "', '" + data + "', '" + stamp + "', '" + control + "', '" + external + "', '" + common + "', '" + content + "')");
		rs.close();
		stat.close();

		return couplingValue;
	}

	public void genDot(String graphvizPath, String dotScript, String fileName) {
		try {
			File temp = File.createTempFile("graph", ".dot");

			temp.deleteOnExit();
			BufferedWriter out = new BufferedWriter(new FileWriter(temp));
			out.write(dotScript);
			out.close();

			System.out.println("dotting");
			Process p = Runtime.getRuntime()
					.exec(graphvizPath + " -Tpng " + temp.getAbsolutePath() + " -o " + fileName);
			System.out.println(fileName);
			p.waitFor();
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
