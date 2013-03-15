package junittest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import glbrick.GLTest;
import glbrick.PartNotFoundException;
import glbrick.PartSpec;
import glbrick.brickUtilities;
import glbrick.ldrawParseTest;

import org.junit.Test;

public class Tests
{


	//put test back!!!!
	@Test
	public void test()
	{
		try
		{
			GLTest.main(new String[]{});
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PartNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	
	public void allPartTest() throws IOException
	{
		//test succeeds if it doesn't crash

		/*
		for (int i = 1; i <9000; i++){
			ldrawParseTest ldp = new ldrawParseTest();
			String temp = "";
			temp += i + ".dat";*/
		//System.out.println(temp);
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
		System.out.println(finaloutPut);
	}
	
	


	public ArrayList<String> getFiles() throws IOException{
		System.out.println("getfiles()");
		//String path = "/export/home/f09/dyerjw/eclipse-work/java-ldraw-cad/ldraw/parts.txt";
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



		/*Scanner s = new Scanner(new File(filename));
	int i = 0;
	while (s.hasNextLine()) {
		allFiles.add(s.nextLine());

	}*/
		return allFiles;

	}


	public void test2() throws FileNotFoundException
	{
		//test succeeds if it doesn't crash
		ldrawParseTest ldp = new ldrawParseTest();


		try{
			//assertTrue(ldp.lDrawTest("s/.dat"));
			System.out.println("test");
			assertTrue(ldp.lDrawTest("s/2902s01.dat"));
			System.out.println("test2");
			//assertTrue(ldp.lDrawTest("s/.dat"));

		}
		catch (PartNotFoundException e){
		}
	}


	public void test3() throws FileNotFoundException
	{
		//test succeeds if it doesn't crash
		ldrawParseTest ldp = new ldrawParseTest();


		try{
			//assertTrue(ldp.lDrawTest("s/.dat"));
			System.out.println("test");
			assertTrue(ldp.lDrawTest("s/2902s01.dat"));
			System.out.println("test2");
			//assertTrue(ldp.lDrawTest("s/.dat"));

		}
		catch (PartNotFoundException e){
			System.out.println(e);
		}
	}

}

