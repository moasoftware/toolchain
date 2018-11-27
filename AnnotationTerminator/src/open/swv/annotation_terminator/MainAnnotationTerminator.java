package open.swv.annotation_terminator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

public class MainAnnotationTerminator {

	private static String intputDirectory = "";
	private static String outputDirectory = "";

	private static boolean parseCommandLine(String[] args)
	{
		for(int i=0; i < args.length; i+=2)
		{
			if(args[i].equals("-input"))
			{
				intputDirectory = args[i+1];
			}
			else if(args[i].equals("-output"))
			{
				outputDirectory = args[i+1];
			}
		}
		if(intputDirectory.length() == 0 && outputDirectory.length() == 0)
			return false;

		return true;
	}

	private static void printUseage()
	{
		System.out.println("-----------------------------------");
		System.out.println("The usage of this program is below.");
		System.out.println("-----------------------------------");
		System.out.println("$ java -jar AnnotationTerminator.jar -input path -output path");
		System.out.println("ex) $ java -jar AnnotationTerminator.jar -input ./project/src ./output");
	}

	private static String readBufferedReader(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try
        {
        	BufferedReader br = new BufferedReader(new FileReader(filePath));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

	public static void main(String[] args) throws Exception {

		if(args.length != 4)
		{
			printUseage();
			System.exit(0);
		}
		else if(! parseCommandLine(args))
		{
			printUseage();
			System.exit(0);;
		}

		String input = intputDirectory;

		String output = outputDirectory;

		GetFileDir gfd = new GetFileDir();
		File f = new File(input);
		gfd.fileDir(f);

		for (String path : gfd.getFilePath()) {
			String outputPath = output;

			FileInputStream fis = new FileInputStream(path);

			CompilationUnit cu = JavaParser.parse(fis);

			AnnotationParsing ap = new AnnotationParsing();

			input = input.replaceAll("\\\\", "/");
			outputPath += path.replace(input, "");

			File file = new File(outputPath);
			//System.out.println(file.getParent());

			File directory = new File(file.getParent());
			if(!directory.isDirectory())
				directory.mkdirs();

			System.out.println(outputPath);
			FileWriter writer = new FileWriter(file, false);

			readBufferedReader(path);

			writer.write(ap.parsingClass(cu));
			writer.flush();
			writer.close();
		}
	}
}
