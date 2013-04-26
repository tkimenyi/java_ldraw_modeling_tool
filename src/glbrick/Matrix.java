package glbrick;

public  class Matrix {
	
	public static double[] matrixMult(double[][] m, double[] v){
		double[] newv = new double[v.length];
		newv[0] = v[0] * m[0][0] + v[1] * m[0][1] + v[2] * m[0][2];
		newv[1] = v[0] * m[1][0] + v[1] * m[1][1] + v[2] * m[1][2];
		newv[2] = v[0] * m[2][0] + v[1] * m[2][1] + v[2] * m[2][2];
		return newv;
	}
	
	public static double [][] matrixMult2(double[][] p, double[][] q){
		
		double [][] newp  = new double [p.length][q.length];
		for(int i = 0; i < p.length; i++) {
			for(int j = 0; j < q.length; j++) {
				for(int k = 0; k < q.length; k++){
					newp[i][j] += p[i][k]*q[k][j];
				}
			}
		}
		return newp;
	}
	
	public static double[] subtract(double[] v1, double[] v2)
	{
		double[] result = new double[v1.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = v1[i] - v2[i];
		}
		return result;
	}
	
	public static double magnitude(double[] v)
	{
		double result = 0;
		for (double i : v)
		{
			result += i * i;
		}
		return Math.sqrt(result);
	}

	public static double[] normalize(double[] v)
	{
		double[] result = new double[v.length];
		result[0] = v[0] / magnitude(v);
		result[1] = v[1] / magnitude(v);
		result[2] = v[2] / magnitude(v);
		return result;
	}

	public static double[] cross_product(double[] v1, double[] v2)
	{
		double[] result = new double[v1.length];

		result[0] = v1[1] * v2[2] - v1[2] * v2[1];
		result[1] = v1[2] * v2[0] - v1[0] * v2[2];
		result[2] = v1[0] * v2[1] - v1[1] * v2[0];

		return result;

	}
}
