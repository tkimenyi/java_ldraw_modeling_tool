package junittest;

import static org.junit.Assert.*;
import glbrick.GLTest;

import org.junit.Test;

public class Tests
{


	@Test
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
			
		

}
