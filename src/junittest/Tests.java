package junittest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import glbrick.PartNotFoundException;

import org.junit.Test;
import org.lwjgl.LWJGLException;

public class Tests
{

	public void test() throws brick.PartNotFoundException
	{
		try
		{
			GLWindowTest.main(new String[]{});
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PartNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}



	public void allPartTest() throws IOException
	{
		
		ArrayList<String> files = getFiles();
		String finaloutPut = "";
		System.out.println("test?");
		int i = 0;
		ldrawParseTest ldp = new ldrawParseTest();
		for(String s : files){
			finaloutPut = finaloutPut + s +"\n";
			try{
				assertTrue(ldp.lDrawTest(s));
			}
			catch (PartNotFoundException e){
			}
			finaloutPut = finaloutPut + i++ +"\n";
		}
		System.out.println(finaloutPut);
	}

	@Test
	public void someParts() throws IOException
	{
		//test succeeds if it doesn't crash
		ArrayList<String> files = new ArrayList<String>(1);
		files.add("3003.dat");
		String finaloutPut = "";
		System.out.println("test?");
		int i = 0;
		ldrawParseTest ldp = new ldrawParseTest();
		for(String s : files){
			finaloutPut = finaloutPut + s +"\n";
			try{
				assertTrue(ldp.lDrawSingleTest(s));

			}
			catch (PartNotFoundException e){
			}
			finaloutPut = finaloutPut + i++ +"\n";
		}
	}




	public ArrayList<String> getFiles() throws IOException{
		System.out.println("getfiles()");
		String path = "/home/john/workspace/java-ldraw-cad/ldraw/parts.txt";
		String fileName = path;
		ArrayList<String> allFiles = new ArrayList<String>(7118);


		FileReader fr = new FileReader(fileName);
		BufferedReader reader = new BufferedReader(fr);


		String str = reader.readLine();
		while (reader.ready()){
			allFiles.add(str);
			str = reader.readLine();

		}

		reader.close();

		return allFiles;

	}


	/**
	 * @throws FileNotFoundException
	 */
	public void test2() throws FileNotFoundException
	{

		ldrawParseTest ldp = new ldrawParseTest();


		try{
			System.out.println("test");
			assertTrue(ldp.lDrawTest("s/2902s01.dat"));
			System.out.println("test2");
		}
		catch (PartNotFoundException e){
		}
	}


	public void test3() throws FileNotFoundException
	{

		ldrawParseTest ldp = new ldrawParseTest();
		try{
			System.out.println("test");
			assertTrue(ldp.lDrawTest("s/2902s01.dat"));
			System.out.println("test2");

		}
		catch (PartNotFoundException e){
			System.out.println(e);
		}
	}

}

