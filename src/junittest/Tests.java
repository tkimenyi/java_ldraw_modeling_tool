package junittest;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import glbrick.BrickSpec;
import glbrick.ColorBase;
import glbrick.DrawnObject;
import glbrick.GuInterface;
import glbrick.LineSpec;
import glbrick.Matrix;
import glbrick.PartNotFoundException;
import glbrick.QuadSpec;
import glbrick.TriangleSpec;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lwjgl.LWJGLException;

public class Tests
{
	private DrawnObject obj;
	
	@Before
	public void initialize(){
		obj = new DrawnObject();
	}
	
	@Test
	public void test() throws IOException, LWJGLException, 
					InterruptedException, PartNotFoundException{
	    	    GLWindowTest.main(new String[]{});
	}
	
	@Ignore //this is used by allPartsTest
	public ArrayList<String> getFiles() throws IOException{
        String path = "/export/home/f09/dyerjw/eclipse-work/java-ldraw-cad/ldraw/parts.txt";
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

    @Ignore // It takes too long to run, check testSomeParts() for a shorter version
    @Test
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
    public void testSomeParts() throws IOException, PartNotFoundException{
        //test succeeds if it doesn't crash
        ArrayList<String> files = new ArrayList<String>(1);
        files.add("3003.dat");
        String finaloutPut = "";
        System.out.println("test?");
        int i = 0;
        ldrawParseTest ldp = new ldrawParseTest();
        for(String s : files){
        	finaloutPut = finaloutPut + s +"\n";
        	assertTrue(ldp.lDrawSingleTest(s));
            finaloutPut = finaloutPut + i++ +"\n";
        }
    }

    
    
    @Test
    public void test2() throws FileNotFoundException, PartNotFoundException
    {
        ldrawParseTest ldp = new ldrawParseTest();
		System.out.println("test");
		assertTrue(ldp.lDrawTest("s/2902s01.dat"));
		System.out.println("test2");
       
    }

    @Test   
    public void test3() throws FileNotFoundException, PartNotFoundException
    {
        //test succeeds if it doesn't crash
        ldrawParseTest ldp = new ldrawParseTest();
	    System.out.println("test");
	    assertTrue(ldp.lDrawTest("s/2902s01.dat"));
	    System.out.println("test2");
    }
    
    @Test
    public void quadTest(){
	    String[] lp = new String[]{"4.0","16.0","17.125","18.078","-10.0","17.125","32.0","-10.0","19.0","32.0","-10.0","19.0","29.0","-10.0"};
	    ColorBase cb;
	    try {
            cb = new ColorBase("ldraw");
            QuadSpec t = new QuadSpec(lp, cb);
            specTest(lp,t);
	    } catch (FileNotFoundException e) {
	        fail("quadtest failed, file was not found");
	    }
    }

    @Test
    public void triTest(){ 
	    String[] lp = new String[]{"4.0","16.0","17.125","18.078","-10.0","17.125","32.0","-10.0","19.0","32.0","-10.0"};
	    ColorBase cb;
	    try {
            cb = new ColorBase("ldraw");
            TriangleSpec t = new TriangleSpec(lp, cb);
            specTest(lp,t);
	    } catch (FileNotFoundException e) {
	    	fail("tritest failed, file was not found");
	    }

    }

    @Test
    public void lineTest(){
        String[] lp = new String[]{"4.0","16.0","17.125","18.078","-10.0","17.125","32.0","-10.0"};
        ColorBase cb;
        try {
            cb = new ColorBase("ldraw");
            LineSpec t = new LineSpec(lp, cb);
            specTest(lp,t);
        } catch (FileNotFoundException e) {
        	fail("linetest failed, file was not found");
        }

    }
    
    public void specTest(String[] lp, BrickSpec b){
        assertFalse(b.isCommment());
        ArrayList<String> al = new ArrayList<String>();
        for (String s: lp){
                al.add(s);
        }
        for (double[] d: b.toDrawnObject().getVertices()){
                for (double d2: d){
                        al.remove(String.valueOf(d2));
                }
        }
        assertTrue(al.remove("4.0") && al.remove("16.0") && al.isEmpty());
    }
    
    public void printMatrix(double [] p){
    	for(int i=0;i<p.length;i++){
    		System.out.println(p[i]);
    	}
    }
    
    public void print2DMatrix(double [][] p){
    	for(int i=0;i<p.length;i++){
    		for(int j=0;j<p[i].length;j++){
    			System.out.print(p[i][j]+"\t");
    			
    		}
    		System.out.println("\n");
    	}
    	System.out.println();
    }
    
    @Test
    public void testMatrixMultiplication(){
    	double [][] p = {{1,2,3},{3,4,7},{10,3,3}};
    	double [] q = {1,4,5};
    	double [][] m = {{-1,-3,3},{2,-4,5},{1,-3,3}};
    	
    	print2DMatrix(p);
    	print2DMatrix(m);
    	System.out.println();
    	
    	double [] resp = Matrix.matrixMult(p, q);
    	printMatrix(resp);
    	double [] result = {24.0,54.0,37.0};
    	assertTrue(Arrays.equals(resp,result));
    	
    	double [][] resp2 = Matrix.matrixMult2(p, m);
    	print2DMatrix(resp2);
    	double [][] result2 = {{6.0,-20.0,22.0},{12.0,-46.0,50.0},{-1.0,-51.0,54.0}};
    	assertTrue(Arrays.equals(resp2[0], result2[0]));
    	assertTrue(Arrays.equals(resp2[1], result2[1]));
    	assertTrue(Arrays.equals(resp2[2], result2[2]));
    }
    
    @Test
    public void testMatrixSubtraction(){
    	double [] v= {1,2,3};
    	double [] w= {-1,5,-6};
    	double result []= {2,-3,9};
    	assertTrue(Arrays.equals(Matrix.subtract(v, w),result));
    }
    
    @Test
    public void testMatrixMagnitude(){
    	double [] r= {-1,5,-6};
    	double result =Math.sqrt(62.0);
    	assertEquals((int)Matrix.magnitude(r),(int)result);
    	System.out.println("magnitude: "+ (int)Matrix.magnitude(r));
    }
    
    @Test
    public void testMatrixNormalize(){
    	double [] r= {-2,4,-6};
    	double magnitude =Matrix.magnitude(r);
    	double [] normalR = {r[0]/magnitude , r[1]/magnitude, r[2]/magnitude};
    	double [] otherNormalR = Matrix.normalize(r);
    	assertTrue(Arrays.equals(normalR, otherNormalR));
    	
    }
    
    @Test
    public void testMatrixCrossProduct(){
    	double [] t= {10,2,3};
    	double [] r = {12,4,5};
    
    	double [] res = new double[t.length];
    	res[0] = t[1] * r[2] - t[2] * r[1];
		res[1] = t[2] * r[0] - t[0] * r[2];
		res[2] = t[0] * r[1] - t[1] * r[0];
		
		double[] crossP = Matrix.cross_product(t, r);
		assertTrue(Arrays.equals(res, crossP));
 
    }
    
    @Test
    public void testDrawnObjectLocation(){
    	double[] loc = {10.0, 20.0, -15.0};
    	double[] to = {5,4,5};
    	double[] at = {15.0,24.0,-10.0};
    	obj.setLocation(loc);
    	obj.move(to);
    	assertFalse(Arrays.equals(obj.getLocation(), loc));
    	assertTrue(Arrays.equals(obj.getLocation(),at));
    	assertTrue(obj.getx() == at[0]);
    	assertTrue(obj.gety() == at[1]);
    	assertTrue(obj.getz() == at[2]);
    }
    
    @Test
    public void testDrawnObjectTransformation(){
    	double [][] trans = {{-1,-3,3,0},{2,-4,5,0},{1,-3,3,0},{0,0,0,1}};
    	double [][] t = obj.getTransformation();
    	double [][] dt = obj.twoDArrayCopy(t);
    	
    	obj.setTransformation(trans);
    	double [][] newTrans = obj.getTransformation();
    	
    	print2DMatrix(newTrans);
    	
    	assertTrue(Arrays.equals(trans[0], newTrans[0]));
    	assertTrue(Arrays.equals(trans[1], newTrans[1]));
    	assertTrue(Arrays.equals(trans[2], newTrans[2]));
    }
    
   

}


