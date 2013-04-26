package junittest;

import glbrick.PartFactory;
import glbrick.PartNotFoundException;
import glbrick.PartSpec;

import java.io.FileNotFoundException;

public class ldrawParseTest {
	public final static String ldrawPath = "ldraw";
	
	
	public ldrawParseTest(){
		/*PartFactory fact = new PartFactory(ldrawPath);
		PartSpec model = fact.getPart(pn);
		//model.debugging();
		System.out.println("no problems");*/
		
	}
	
	public boolean lDrawTest(String pn) throws FileNotFoundException, PartNotFoundException{
		PartFactory fact = new PartFactory(ldrawPath);
		PartSpec model = fact.getPart(pn);
		if (model != null){
		return true;
		}
		else{
			return false; 
			
		}
	}
	
	
	public boolean lDrawSingleTest(String pn) throws FileNotFoundException, PartNotFoundException{
		PartFactory fact = new PartFactory(ldrawPath);
		PartSpec model = fact.getPart(pn);
		model.treeTester();
		return true;
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException, PartNotFoundException
	{
		new ldrawParseTest();
		
		
	}
	
}