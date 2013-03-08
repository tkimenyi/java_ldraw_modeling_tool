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

	
	public void allPartTest() throws FileNotFoundException
	{
		//test succeeds if it doesn't crash
		

		for (int i = 1; i <9000; i++){
			ldrawParseTest ldp = new ldrawParseTest();
			String temp = "";
			temp += i + ".dat";
			//System.out.println(temp);
			try{
				assertTrue(ldp.lDrawTest(temp));
			}
			catch (PartNotFoundException e){
			}
		}

	}
	@Test
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

}

