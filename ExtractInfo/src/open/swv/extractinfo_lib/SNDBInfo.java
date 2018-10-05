package open.swv.extractinfo_lib;

public class SNDBInfo {
	public static String getAccessMeanForSNDB(String input) {
		if(input.equals("r"))
			return "read";
		else if(input.equals("w"))
			return "written";
		else if(input.equals("p"))
			return "passed";
		else if(input.equals("u"))
			return "unused";
		return "unknwon";
	}

	public static int getMaxDataSizeForSNDB(String input) {
		if(input.equals("0"))
			return 2;
		else if(input.equals("1"))
			return 2;
		else if(input.equals("2"))
			return 2;
		else if(input.equals("by"))
			return 11;
		else if(input.equals("cl"))
			return 9;
		else if(input.equals("com"))
			return 9;
		else if(input.equals("con"))
			return 9;
		else if(input.equals("cov"))
			return 10;
		else if(input.equals("e"))
			return 9;
		else if(input.equals("ec"))
			return 9;
		else if(input.equals("f"))
			return 3;
		else if(input.equals("fd"))
			return 9;
		else if(input.equals("fil"))
			return 9;
		else if(input.equals("fr"))
			return 10;
		else if(input.equals("fu"))
			return 9;
		else if(input.equals("gv"))
			return 9;
		else if(input.equals("in"))
			return 10;
		else if(input.equals("iu"))
			return 9;
		else if(input.equals("icl"))
			return 3;
		else if(input.equals("iv"))
			return 10;
		else if(input.equals("lv"))
			return 10;
		else if(input.equals("ma"))
			return 9;
		else if(input.equals("md"))
			return 10;
		else if(input.equals("mi"))
			return 10;
		else if(input.equals("rem"))
			return 5;
		else if(input.equals("su"))
			return 8;
		else if(input.equals("t"))
			return 9;
		else if(input.equals("to"))
			return 11;
		else if(input.equals("un"))
			return 9;
		return 0;
	}

	public static String getTypeMeanForSNDB(String input) {
		if(input.equals("0"))
			return "IgnoredWords";
		else if(input.equals("1"))
			return "DefaultView";
		else if(input.equals("2"))
			return "SecondView";
		else if(input.equals("by"))
			return "ReferredBy";
		else if(input.equals("cl"))
			return "Classes";
		else if(input.equals("com"))
			return "CommonBlocks";
		else if(input.equals("con"))
			return "Constants";
		else if(input.equals("cov"))
			return "CommonValue";
		else if(input.equals("e"))
			return "Enumerations";
		else if(input.equals("ec"))
			return "EnumConstants";
		else if(input.equals("f"))
			return "ProjectFiles";
		else if(input.equals("fd"))
			return "FunctionDefinitions";
		else if(input.equals("fil"))
			return "SymbolsOfFiles";
		else if(input.equals("fr"))
			return "Friends";
		else if(input.equals("fu"))
			return "Functions";
		else if(input.equals("gv"))
			return "Variables";
		else if(input.equals("in"))
			return "Inheritances";
		else if(input.equals("iu"))
			return "Include";
		else if(input.equals("icl"))
			return "IncludeList";
		else if(input.equals("iv"))
			return "InstanceVariables";
		else if(input.equals("lv"))
			return "LocalVariables";
		else if(input.equals("ma"))
			return "Macros";
		else if(input.equals("md"))
			return "MethodDefinitions";
		else if(input.equals("mi"))
			return "MethodImplementations";
		else if(input.equals("rem"))
			return "Remarks";
		else if(input.equals("su"))
			return "Subroutines";
		else if(input.equals("t"))
			return "Typedefs";
		else if(input.equals("to"))
			return "RefersTo";
		else if(input.equals("un"))
			return "Unions";
		return "unknwon";
	}
}
