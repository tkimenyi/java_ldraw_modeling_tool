package brick.tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import brick.*;

import org.junit.*;

public class SpecTest {
	private PartFactory fact;
	
	@Before
	public void setup() throws FileNotFoundException, PartNotFoundException {
		fact = new PartFactory("/Applications/LDRAW");
		fact.getPart("3004.dat");
		fact.getPart("3001.dat");
	}
	
	public String getTestStringFor(String partName) throws PartNotFoundException {
		return fact.getPart(partName).toString();
	}

	@Test
	public void test1() throws PartNotFoundException {
		assertEquals("0 Brick 1 x 2 \n0 Name: 3004.dat \n0 Author: James Jessiman \n0 Original LDraw Part \n0 LDRAW_ORG Part UPDATE 2002-03 \n0 BFC CERTIFY CCW \n0 2002-05-07 KJM BFC Certification \n1 16 0.0 4.0 0.0 1.0 0.0 0.0 0.0 -5.0 0.0 0.0 0.0 1.0 stud3.dat\n0 BFC INVERTNEXT \n1 16 0.0 24.0 0.0 16.0 0.0 0.0 0.0 -20.0 0.0 0.0 0.0 6.0 box5.dat\n4 16 20.0 24.0 10.0 16.0 24.0 6.0 -16.0 24.0 6.0 -20.0 24.0 10.0\n4 16 -20.0 24.0 10.0 -16.0 24.0 6.0 -16.0 24.0 -6.0 -20.0 24.0 -10.0\n4 16 -20.0 24.0 -10.0 -16.0 24.0 -6.0 16.0 24.0 -6.0 20.0 24.0 -10.0\n4 16 20.0 24.0 -10.0 16.0 24.0 -6.0 16.0 24.0 6.0 20.0 24.0 10.0\n1 16 0.0 24.0 0.0 20.0 0.0 0.0 0.0 -24.0 0.0 0.0 0.0 10.0 box5.dat\n1 16 10.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n1 16 -10.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n0 \n", 
				getTestStringFor("3004.dat"));
	}

	@Test 
	public void test2() throws PartNotFoundException {
		assertEquals("0 Cylinder 1.0 \n0 Name: 4-4cyli.dat \n0 Author: James Jessiman \n0 Original LDraw Primitive \n0 LDRAW_ORG Primitive UPDATE 2005-01 \n0 BFC CERTIFY CCW \n0 // 2002-03-23 SEB Added BFC statement; merged headers from \n0 // files in distributions LDraw 0.27 and Complete. \n0 // 2004-12-14 GuyVivan BFC CCW \n4 16 1.0 1.0 0.0 0.924 1.0 0.383 0.924 0.0 0.383 1.0 0.0 0.0\n5 24 1.0 0.0 0.0 1.0 1.0 0.0 0.924 0.0 0.383 0.924 0.0 -0.383\n4 16 0.924 1.0 0.383 0.707 1.0 0.707 0.707 0.0 0.707 0.924 0.0 0.383\n5 24 0.924 0.0 0.383 0.924 1.0 0.383 0.707 0.0 0.707 1.0 0.0 0.0\n4 16 0.707 1.0 0.707 0.383 1.0 0.924 0.383 0.0 0.924 0.707 0.0 0.707\n5 24 0.707 0.0 0.707 0.707 1.0 0.707 0.383 0.0 0.924 0.924 0.0 0.383\n4 16 0.383 1.0 0.924 0.0 1.0 1.0 0.0 0.0 1.0 0.383 0.0 0.924\n5 24 0.383 0.0 0.924 0.383 1.0 0.924 0.0 0.0 1.0 0.707 0.0 0.707\n4 16 0.0 1.0 1.0 -0.383 1.0 0.924 -0.383 0.0 0.924 0.0 0.0 1.0\n5 24 0.0 0.0 1.0 0.0 1.0 1.0 -0.383 0.0 0.924 0.383 0.0 0.924\n4 16 -0.383 1.0 0.924 -0.707 1.0 0.707 -0.707 0.0 0.707 -0.383 0.0 0.924\n5 24 -0.383 0.0 0.924 -0.383 1.0 0.924 -0.707 0.0 0.707 0.0 0.0 1.0\n4 16 -0.707 1.0 0.707 -0.924 1.0 0.383 -0.924 0.0 0.383 -0.707 0.0 0.707\n5 24 -0.707 0.0 0.707 -0.707 1.0 0.707 -0.924 0.0 0.383 -0.383 0.0 0.924\n4 16 -0.924 1.0 0.383 -1.0 1.0 0.0 -1.0 0.0 0.0 -0.924 0.0 0.383\n5 24 -0.924 0.0 0.383 -0.924 1.0 0.383 -1.0 0.0 0.0 -0.707 0.0 0.707\n4 16 -1.0 1.0 0.0 -0.924 1.0 -0.383 -0.924 0.0 -0.383 -1.0 0.0 0.0\n5 24 -1.0 0.0 0.0 -1.0 1.0 0.0 -0.924 0.0 -0.383 -0.924 0.0 0.383\n4 16 -0.924 1.0 -0.383 -0.707 1.0 -0.707 -0.707 0.0 -0.707 -0.924 0.0 -0.383\n5 24 -0.924 0.0 -0.383 -0.924 1.0 -0.383 -0.707 0.0 -0.707 -1.0 0.0 0.0\n4 16 -0.707 1.0 -0.707 -0.383 1.0 -0.924 -0.383 0.0 -0.924 -0.707 0.0 -0.707\n5 24 -0.707 0.0 -0.707 -0.707 1.0 -0.707 -0.383 0.0 -0.924 -0.924 0.0 -0.383\n4 16 -0.383 1.0 -0.924 0.0 1.0 -1.0 0.0 0.0 -1.0 -0.383 0.0 -0.924\n5 24 -0.383 0.0 -0.924 -0.383 1.0 -0.924 0.0 0.0 -1.0 -0.707 0.0 -0.707\n4 16 0.0 1.0 -1.0 0.383 1.0 -0.924 0.383 0.0 -0.924 0.0 0.0 -1.0\n5 24 0.0 0.0 -1.0 0.0 1.0 -1.0 0.383 0.0 -0.924 -0.383 0.0 -0.924\n4 16 0.383 1.0 -0.924 0.707 1.0 -0.707 0.707 0.0 -0.707 0.383 0.0 -0.924\n5 24 0.383 0.0 -0.924 0.383 1.0 -0.924 0.707 0.0 -0.707 0.0 0.0 -1.0\n4 16 0.707 1.0 -0.707 0.924 1.0 -0.383 0.924 0.0 -0.383 0.707 0.0 -0.707\n5 24 0.707 0.0 -0.707 0.707 1.0 -0.707 0.924 0.0 -0.383 0.383 0.0 -0.924\n4 16 0.924 1.0 -0.383 1.0 1.0 0.0 1.0 0.0 0.0 0.924 0.0 -0.383\n5 24 0.924 0.0 -0.383 0.924 1.0 -0.383 1.0 0.0 0.0 0.707 0.0 -0.707\n0 \n",
				getTestStringFor("4-4cyli.dat"));		
	}
	
	@Test
	public void test3() throws PartNotFoundException {
		assertEquals("0 Disc 1.0 \n0 Name: 4-4disc.dat \n0 Author: James Jessiman \n0 Original LDraw Primitive \n0 LDRAW_ORG Primitive UPDATE 2002-02 \n0 BFC CERTIFY CCW \n0 2002-03-23 SEB Added BFC statement \n3 16 0.0 0.0 0.0 1.0 0.0 0.0 0.924 0.0 0.383\n3 16 0.0 0.0 0.0 0.924 0.0 0.383 0.707 0.0 0.707\n3 16 0.0 0.0 0.0 0.707 0.0 0.707 0.383 0.0 0.924\n3 16 0.0 0.0 0.0 0.383 0.0 0.924 0.0 0.0 1.0\n3 16 0.0 0.0 0.0 0.0 0.0 1.0 -0.383 0.0 0.924\n3 16 0.0 0.0 0.0 -0.383 0.0 0.924 -0.707 0.0 0.707\n3 16 0.0 0.0 0.0 -0.707 0.0 0.707 -0.924 0.0 0.383\n3 16 0.0 0.0 0.0 -0.924 0.0 0.383 -1.0 0.0 0.0\n3 16 0.0 0.0 0.0 -1.0 0.0 0.0 -0.924 0.0 -0.383\n3 16 0.0 0.0 0.0 -0.924 0.0 -0.383 -0.707 0.0 -0.707\n3 16 0.0 0.0 0.0 -0.707 0.0 -0.707 -0.383 0.0 -0.924\n3 16 0.0 0.0 0.0 -0.383 0.0 -0.924 0.0 0.0 -1.0\n3 16 0.0 0.0 0.0 0.0 0.0 -1.0 0.383 0.0 -0.924\n3 16 0.0 0.0 0.0 0.383 0.0 -0.924 0.707 0.0 -0.707\n3 16 0.0 0.0 0.0 0.707 0.0 -0.707 0.924 0.0 -0.383\n3 16 0.0 0.0 0.0 0.924 0.0 -0.383 1.0 0.0 0.0\n0 \n",
				getTestStringFor("4-4disc.dat"));
	}
	
	@Test
	public void test4() throws PartNotFoundException {
		assertEquals("0 Circle 1.0 \n0 Name: 4-4edge.dat \n0 Author: James Jessiman \n0 Original LDraw Primitive \n0 LDRAW_ORG Primitive UPDATE 2005-01 \n2 24 1.0 0.0 0.0 0.924 0.0 0.383\n2 24 0.924 0.0 0.383 0.707 0.0 0.707\n2 24 0.707 0.0 0.707 0.383 0.0 0.924\n2 24 0.383 0.0 0.924 0.0 0.0 1.0\n2 24 0.0 0.0 1.0 -0.383 0.0 0.924\n2 24 -0.383 0.0 0.924 -0.707 0.0 0.707\n2 24 -0.707 0.0 0.707 -0.924 0.0 0.383\n2 24 -0.924 0.0 0.383 -1.0 0.0 0.0\n2 24 -1.0 0.0 0.0 -0.924 0.0 -0.383\n2 24 -0.924 0.0 -0.383 -0.707 0.0 -0.707\n2 24 -0.707 0.0 -0.707 -0.383 0.0 -0.924\n2 24 -0.383 0.0 -0.924 0.0 0.0 -1.0\n2 24 0.0 0.0 -1.0 0.383 0.0 -0.924\n2 24 0.383 0.0 -0.924 0.707 0.0 -0.707\n2 24 0.707 0.0 -0.707 0.924 0.0 -0.383\n2 24 0.924 0.0 -0.383 1.0 0.0 0.0\n0 \n", 
				getTestStringFor("4-4edge.dat"));
	}
	
	@Test
	public void test5() throws PartNotFoundException {
		assertEquals("0 Box 5 (five faces) \n0 Name: box5.dat \n0 Author: James Jessiman \n0 Original LDraw Primitive \n0 LDRAW_ORG Primitive UPDATE 2002-02 \n0 BFC CERTIFY CW \n0 2002-04-03 SEB Modified for BFC compliance \n2 24 1.0 1.0 1.0 -1.0 1.0 1.0\n2 24 -1.0 1.0 1.0 -1.0 1.0 -1.0\n2 24 -1.0 1.0 -1.0 1.0 1.0 -1.0\n2 24 1.0 1.0 -1.0 1.0 1.0 1.0\n2 24 1.0 0.0 1.0 -1.0 0.0 1.0\n2 24 -1.0 0.0 1.0 -1.0 0.0 -1.0\n2 24 -1.0 0.0 -1.0 1.0 0.0 -1.0\n2 24 1.0 0.0 -1.0 1.0 0.0 1.0\n2 24 1.0 0.0 1.0 1.0 1.0 1.0\n2 24 -1.0 0.0 1.0 -1.0 1.0 1.0\n2 24 1.0 0.0 -1.0 1.0 1.0 -1.0\n2 24 -1.0 0.0 -1.0 -1.0 1.0 -1.0\n4 16 -1.0 1.0 -1.0 1.0 1.0 -1.0 1.0 1.0 1.0 -1.0 1.0 1.0\n4 16 1.0 1.0 1.0 1.0 0.0 1.0 -1.0 0.0 1.0 -1.0 1.0 1.0\n4 16 -1.0 1.0 1.0 -1.0 0.0 1.0 -1.0 0.0 -1.0 -1.0 1.0 -1.0\n4 16 -1.0 1.0 -1.0 -1.0 0.0 -1.0 1.0 0.0 -1.0 1.0 1.0 -1.0\n4 16 1.0 1.0 -1.0 1.0 0.0 -1.0 1.0 0.0 1.0 1.0 1.0 1.0\n0 \n",
				getTestStringFor("box5.dat"));
	}
	
	@Test
	public void test6() throws PartNotFoundException {
		assertEquals("0 Stud \n0 Name: stud.dat \n0 Author: James Jessiman \n0 Original LDraw Primitive \n0 LDRAW_ORG Primitive UPDATE 2002-02 \n0 BFC CERTIFY CW \n0 2002-04-04 SEB Modified for BFC compliance \n1 16 0.0 0.0 0.0 6.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 6.0 4-4edge.dat\n1 16 0.0 -4.0 0.0 6.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 6.0 4-4edge.dat\n1 16 0.0 0.0 0.0 6.0 0.0 0.0 0.0 -4.0 0.0 0.0 0.0 6.0 4-4cyli.dat\n1 16 0.0 -4.0 0.0 6.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 6.0 4-4disc.dat\n0 \n", 
				getTestStringFor("stud.dat"));
	}
	
	@Test
	public void test7() throws PartNotFoundException {
		assertEquals("0 Stud Tube Solid \n0 Name: stud3.dat \n0 Author: James Jessiman \n0 Original LDraw Primitive \n0 LDRAW_ORG Primitive UPDATE 2002-02 \n0 BFC CERTIFY CW \n0 2002-04-04 SEB Modified for BFC compliance \n1 16 0.0 -4.0 0.0 4.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 4.0 4-4edge.dat\n1 16 0.0 0.0 0.0 4.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 4.0 4-4edge.dat\n1 16 0.0 -4.0 0.0 4.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 4.0 4-4disc.dat\n1 16 0.0 -4.0 0.0 4.0 0.0 0.0 0.0 4.0 0.0 0.0 0.0 4.0 4-4cyli.dat\n0 \n", 
				getTestStringFor("stud3.dat"));
	}
	
	@Test
	public void test8() throws PartNotFoundException {
		assertEquals("0 Brick 2 x 4 \n0 Name: 3001.dat \n0 Author: James Jessiman \n0 Original LDraw Part \n0 LDRAW_ORG Part UPDATE 2004-03 \n0 BFC CERTIFY CCW \n0 // 2002-05-07 KJM BFC Certification \n0 // 2004-02-08 Steffen used s\\3001s01.dat \n1 16 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 s\\3001s01.dat\n4 16 -40.0 0.0 -20.0 -40.0 24.0 -20.0 40.0 24.0 -20.0 40.0 0.0 -20.0\n4 16 40.0 0.0 20.0 40.0 24.0 20.0 -40.0 24.0 20.0 -40.0 0.0 20.0\n0 \n",
				getTestStringFor("3001.dat"));
	}
	
	@Test
	public void test9() throws PartNotFoundException {
		assertEquals("0 Box, 3 faces drawn, 2 edges left out \n0 Name: box3u2p.dat \n0 Author: Niels Karsdorp \n0 LDRAW_ORG Primitive UPDATE 2003-01 \n0 BFC CERTIFY CW \n2 24 1.0 1.0 1.0 -1.0 1.0 1.0\n2 24 -1.0 1.0 -1.0 1.0 1.0 -1.0\n2 24 1.0 0.0 1.0 -1.0 0.0 1.0\n2 24 -1.0 0.0 -1.0 1.0 0.0 -1.0\n2 24 1.0 1.0 1.0 1.0 1.0 -1.0\n2 24 -1.0 1.0 1.0 -1.0 1.0 -1.0\n2 24 1.0 1.0 1.0 1.0 0.0 1.0\n2 24 1.0 1.0 -1.0 1.0 0.0 -1.0\n2 24 -1.0 1.0 1.0 -1.0 0.0 1.0\n2 24 -1.0 1.0 -1.0 -1.0 0.0 -1.0\n4 16 1.0 1.0 -1.0 1.0 1.0 1.0 -1.0 1.0 1.0 -1.0 1.0 -1.0\n4 16 1.0 1.0 1.0 1.0 0.0 1.0 -1.0 0.0 1.0 -1.0 1.0 1.0\n4 16 -1.0 1.0 -1.0 -1.0 0.0 -1.0 1.0 0.0 -1.0 1.0 1.0 -1.0\n0 \n", 
				getTestStringFor("box3u2p.dat"));
	}
	
	@Test
	public void test10() throws PartNotFoundException {
		assertEquals("0 Ring 3 x 1.0 \n0 Name: ring3.dat \n0 Author: James Jessiman \n0 Original LDraw Primitive \n0 LDRAW_ORG Primitive UPDATE 2002-02 \n0 BFC CERTIFY CW \n0 2002-4-5: TH: Added BFC statement \n4 16 0.0 0.0 3.0 -1.148 0.0 2.772 -1.531 0.0 3.696 0.0 0.0 4.0\n4 16 -1.148 0.0 2.772 -2.121 0.0 2.121 -2.828 0.0 2.828 -1.531 0.0 3.696\n4 16 -2.121 0.0 2.121 -2.772 0.0 1.148 -3.696 0.0 1.531 -2.828 0.0 2.828\n4 16 -2.772 0.0 1.148 -3.0 0.0 0.0 -4.0 0.0 0.0 -3.696 0.0 1.531\n4 16 -3.0 0.0 0.0 -2.772 0.0 -1.148 -3.696 0.0 -1.531 -4.0 0.0 0.0\n4 16 -2.772 0.0 -1.148 -2.121 0.0 -2.121 -2.828 0.0 -2.828 -3.696 0.0 -1.531\n4 16 -2.121 0.0 -2.121 -1.148 0.0 -2.772 -1.531 0.0 -3.696 -2.828 0.0 -2.828\n4 16 -1.148 0.0 -2.772 0.0 0.0 -3.0 0.0 0.0 -4.0 -1.531 0.0 -3.696\n4 16 0.0 0.0 -3.0 1.148 0.0 -2.772 1.531 0.0 -3.696 0.0 0.0 -4.0\n4 16 1.148 0.0 -2.772 2.121 0.0 -2.121 2.828 0.0 -2.828 1.531 0.0 -3.696\n4 16 2.121 0.0 -2.121 2.772 0.0 -1.148 3.696 0.0 -1.531 2.828 0.0 -2.828\n4 16 2.772 0.0 -1.148 3.0 0.0 0.0 4.0 0.0 0.0 3.696 0.0 -1.531\n4 16 3.0 0.0 0.0 2.772 0.0 1.148 3.696 0.0 1.531 4.0 0.0 0.0\n4 16 2.772 0.0 1.148 2.121 0.0 2.121 2.828 0.0 2.828 3.696 0.0 1.531\n4 16 2.121 0.0 2.121 1.148 0.0 2.772 1.531 0.0 3.696 2.828 0.0 2.828\n4 16 1.148 0.0 2.772 0.0 0.0 3.0 0.0 0.0 4.0 1.531 0.0 3.696\n0 \n",
				getTestStringFor("ring3.dat"));
	}
	
	@Test
	public void test11() throws PartNotFoundException {
		assertEquals("0 ~Brick 2 x 4 without Front and Back Faces \n0 Name: s\\3001s01.dat \n0 Author: James Jessiman \n0 LDRAW_ORG Subpart UPDATE 2004-03 \n0 BFC CERTIFY CCW \n0 // 2002-05-07 KJM BFC Certification \n0 // 2002-05-07 AJW \n0 // Uncomment for front and back faces: \n0 // 4 16 40 0 -20 -40 0 -20 -40 24 -20 40 24 -20 \n0 // 4 16 40 0 20 -40 0 20 -40 24 20 40 24 20 \n1 16 20.0 4.0 0.0 1.0 0.0 0.0 0.0 -5.0 0.0 0.0 0.0 1.0 stud4.dat\n1 16 0.0 4.0 0.0 1.0 0.0 0.0 0.0 -5.0 0.0 0.0 0.0 1.0 stud4.dat\n1 16 -20.0 4.0 0.0 1.0 0.0 0.0 0.0 -5.0 0.0 0.0 0.0 1.0 stud4.dat\n0 BFC INVERTNEXT \n1 16 0.0 24.0 0.0 36.0 0.0 0.0 0.0 -20.0 0.0 0.0 0.0 16.0 box5.dat\n4 16 40.0 24.0 20.0 36.0 24.0 16.0 -36.0 24.0 16.0 -40.0 24.0 20.0\n4 16 -40.0 24.0 20.0 -36.0 24.0 16.0 -36.0 24.0 -16.0 -40.0 24.0 -20.0\n4 16 -40.0 24.0 -20.0 -36.0 24.0 -16.0 36.0 24.0 -16.0 40.0 24.0 -20.0\n4 16 40.0 24.0 -20.0 36.0 24.0 -16.0 36.0 24.0 16.0 40.0 24.0 20.0\n1 16 0.0 24.0 0.0 0.0 0.0 40.0 0.0 -24.0 0.0 -20.0 0.0 0.0 box3u2p.dat\n2 24 -40.0 24.0 -20.0 40.0 24.0 -20.0\n2 24 -40.0 24.0 20.0 40.0 24.0 20.0\n1 16 30.0 0.0 10.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n1 16 10.0 0.0 10.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n1 16 -10.0 0.0 10.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n1 16 -30.0 0.0 10.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n1 16 30.0 0.0 -10.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n1 16 10.0 0.0 -10.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n1 16 -10.0 0.0 -10.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n1 16 -30.0 0.0 -10.0 1.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 1.0 stud.dat\n0 \n",
				getTestStringFor("s\\3001s01.dat"));
	}
	
	@Test
	public void test12() throws PartNotFoundException {
		assertEquals("0 Stud Tube Open \n0 Name: stud4.dat \n0 Author: James Jessiman \n0 Author: Chris Dee \n0 Original LDraw Primitive \n0 LDRAW_ORG Primitive UPDATE 2002-02 \n0 BFC CERTIFY CW \n0 2002-04-04 SEB Modified for BFC compliance \n1 16 0.0 -4.0 0.0 6.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 6.0 4-4edge.dat\n1 16 0.0 -4.0 0.0 8.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 8.0 4-4edge.dat\n1 16 0.0 0.0 0.0 6.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 6.0 4-4edge.dat\n1 16 0.0 0.0 0.0 8.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 8.0 4-4edge.dat\n0 BFC INVERTNEXT \n1 16 0.0 -4.0 0.0 6.0 0.0 0.0 0.0 4.0 0.0 0.0 0.0 6.0 4-4cyli.dat\n1 16 0.0 -4.0 0.0 8.0 0.0 0.0 0.0 4.0 0.0 0.0 0.0 8.0 4-4cyli.dat\n1 16 0.0 -4.0 0.0 2.0 0.0 0.0 0.0 1.0 0.0 0.0 0.0 2.0 ring3.dat\n0 \n", 
				getTestStringFor("stud4.dat"));
	}
}
