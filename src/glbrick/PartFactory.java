package glbrick;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PartFactory
{
	private TreeMap<String, PartSpec> parts;
	private ArrayList<String> paths;
	private ColorBase colors;
	private boolean printFailures;

	public PartFactory(String ldrawPath) throws FileNotFoundException
	{
		paths = new ArrayList<String>();
		parts = new TreeMap<String, PartSpec>();
		colors = new ColorBase(ldrawPath);
		printFailures = false;

		String partsPath = ldrawPath + File.separator + "parts";
		String pPath = ldrawPath + File.separator + "p";

		addPath(partsPath);
		addPath(pPath);
		addPath(ldrawPath + File.separator + "models");
		addPath(partsPath + File.separator + "S");
		addPath(pPath + File.separator + "48");
	}

	public void activateFailureErrors()
	{
		printFailures = true;
	}

	public Collection<PartSpec> getAllParts()
	{
		return parts.values();
	}

	public void addPath(String path)
	{
		paths.add(path);
	}

	public PartSpec getPart(String partName) throws PartNotFoundException
	{
		partName = partName.toLowerCase();
		if (parts.containsKey(partName))
		{
		//	return parts.get(partName);
		//} else
		}
			for (String path : paths)
			{
				try
				{
					PartSpec result = tryFile(path, partName);
					parts.put(partName, result);
					return result;
				} catch (FileNotFoundException e)
				{
					if (printFailures)
					{
						System.out.println(e.getMessage());
					}
				}
			}
			throw new PartNotFoundException(partName);
		
	}

	public String mkString(String[] strArr)
	{

		String printstr = "";
		for (String s : strArr)
		{
			printstr += s + " ";
		}

		return printstr;
	}

	private PartSpec tryFile(String path, String partName) throws FileNotFoundException, PartNotFoundException
	{
		String fileName = path + File.separator + brickUtilities.fixPath(partName);
		FileReader fr = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(fr);
		String snl = "";
		PartSpec result = new PartSpec(partName, colors);
		try
		{
			while (reader.ready())
			{
				snl = reader.readLine();
				snl = snl.trim();
				//System.out.println(snl);
				String[] lineParts = snl.split("\\s+");
				//System.out.println(mkString(lineParts));
				if (lineParts.length > 0 && lineParts[0].length() > 0)
				{
					int code = Integer.parseInt(lineParts[0]);
					//System.out.println(mkString(lineParts));
					if (code == 0)
					{
						result.addLine(new CommentSpec(lineParts));
					} else if (code == 1)
					{
						result.addLine(new SubpartSpec(lineParts, this));
					} else if (code == 2)
					{
						result.addLine(new LineSpec(lineParts, colors));
					} else if (code == 3)
					{
						result.addLine(new TriangleSpec(lineParts, colors));
					} else if (code == 4)
					{
						try
						{
							result.addLine(new QuadSpec(lineParts, colors));
						} catch (ArrayIndexOutOfBoundsException e)
						{
							System.out.println("Bad stuff happening");
							System.out.println(e);
							System.out.println("Array:" + lineParts);
							System.out.println("Source string: '" + snl + "'");
							//System.out.println("next line: " + s.hasNextLine());
							//System.out.println("next line: " + s.nextLine());
						}

					} else if (code == 5)
					{
						result.addLine(new OptionalLineSpec(lineParts, colors));
					}
				}
			}
			reader.close();
		} catch (NumberFormatException e)
		{
			System.out.println("numberFormatException, line 116 partsfactory.java.");
			System.out.println("snl: " + snl);
			result.debugging();
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			System.out.println("IOexception, line 121 partsfactory.java.");
			System.out.println("snl: " + snl);
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
}
