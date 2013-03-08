package junittest;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import glbrick.GLTest;
import glbrick.PartNotFoundException;
import glbrick.ldrawParseTest;

import org.junit.Test;

public class Tests
{


	//put test back!!!!
	public void test()
	{
		try
		{
			GLTest.main(new String[]{});
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

	}

	@Test
	public void allPartTest() throws FileNotFoundException
	{
		//test succeeds if it doesn't crash
		

		for (int i = 1; i <3000; i++){
			ldrawParseTest ldp = new ldrawParseTest();
			String temp = "";
			temp += i + ".dat";
			System.out.println(temp);
			try{
				assertTrue(ldp.lDrawTest(temp));
			}
			catch (PartNotFoundException e){
			}
		}

	}

	public void test2() throws FileNotFoundException
	{
		//test succeeds if it doesn't crash
		ldrawParseTest ldp = new ldrawParseTest();


		try{
			assertTrue(ldp.lDrawTest("2902.dat"));
		}
		catch (PartNotFoundException e){
		}
	}

}

