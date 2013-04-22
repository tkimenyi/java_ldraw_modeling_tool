package glbrick;

public  class Matrix {
	
	public static double[] matrixMult(double[][] m, double[] v)
	{
		double[] newv = new double[v.length];
		newv[0] = v[0] * m[0][0] + v[1] * m[0][1] + v[2] * m[0][2];
		newv[1] = v[0] * m[1][0] + v[1] * m[1][1] + v[2] * m[1][2];
		newv[2] = v[0] * m[2][0] + v[1] * m[2][1] + v[2] * m[2][2];
		return newv;
	}
	
	
	public static double [][] matrixMult2(double[][] p, double[][] q){
		double [] newp0 = matrixMult(p,q[0]);
		double [] newp1 = matrixMult(p,q[1]);
		double [] newp2 = matrixMult(p,q[2]);
		
		double [][] newp = {newp0,newp1,newp2};
		return newp;
	}
}
