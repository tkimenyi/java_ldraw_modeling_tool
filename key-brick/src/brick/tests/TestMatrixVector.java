package brick.tests;

import brick.Util;

import com.threed.jpct.*;

public class TestMatrixVector {
	public static void main(String[] args) {
		Matrix m = new Matrix();
		m.setColumn(0, 1, 2, 3, 4);
		m.setColumn(1, 5, 6, 7, 8);
		m.setColumn(2, 9, 10, 11, 12);
		m.setColumn(3, 0, 0, 0, 1);
		SimpleVector v = new SimpleVector(1, 1, 1);
		v.matMul(m);
		System.out.println(v);
		// Expected result:
		//
		// 1 + 2 + 3 + 4 = 10
		// 5 + 6 + 7 + 8 = 26
		// 9 + 10 + 11 + 12 = 42
		
		Matrix m2 = new Matrix();
		m2.setColumn(0, 2, 1, 2, 1);
		m2.setColumn(1, 1, 2, 1, 2);
		m2.setColumn(2, 2, 1, 2, 1);
		m2.setColumn(3, 1, 2, 1, 2);
		
		// Expected result: m * m2
		//
		// 14 16 14 16
		// 38 40 38 40
		// 62 64 62 64
		// 1   2  1  2
		System.out.println(Util.matMul(m, m2));
		
		SimpleVector vLine = new SimpleVector(0, 1, 0);
		vLine.rotateX((float)Math.PI/2);
		System.out.println(vLine);
	}
}
