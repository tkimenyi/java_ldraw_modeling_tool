package brick.tests;

import java.io.FileNotFoundException;

import brick.*;

public class PartTester {
	private final static String myPath = "/Users/gabriel/books/robotics";
	private final static String ldrawPath = "/Applications/LDRAW";
	
	public static void main(String[] args) throws PartNotFoundException, FileNotFoundException, MalformedLDrawException {
		PartFactory fact = new PartFactory(ldrawPath);
		fact.addPath(myPath);
		fact.activateFailureErrors();
		
		fact.getPart("BasicRobot.ldr");
		for (PartSpec p: fact.getAllParts()) {
			System.out.println(p.getName());
			System.out.println(Util.javafy(p.toString()));
		}
	}
}
