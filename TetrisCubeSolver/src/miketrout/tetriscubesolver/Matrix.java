package miketrout.tetriscubesolver;

public class Matrix {

	public static final Matrix Rx = new Matrix(new int[][] { { 1, 0, 0 }, { 0, 0, -1 }, { 0, 1, 0 } });
	public static final Matrix Ry = new Matrix(new int[][] { { 0, 0, 1 }, { 0, 1, 0 }, { -1, 0, 0 } });
	public static final Matrix Rz = new Matrix(new int[][] { { 0, -1, 0 }, { 1, 0, 0 }, { 0, 0, 1 } });

	protected final int[][] data; // M-by-N array
	protected final int M; // Number of rows
	protected final int N; // Number of columns

	// Create M-by-N matrix of 0's
	public Matrix(int M, int N) {
		this.M = M;
		this.N = N;
		data = new int[M][N];
	}

	// Create matrix based on 2d array
	public Matrix(int[][] data) {
		M = data.length;
		N = data[0].length;
		this.data = new int[M][N];
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				this.data[i][j] = data[i][j];
	}

	// Return A == B
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Matrix))
			return false;
		Matrix A = this;
		Matrix B = (Matrix) object;
		if (B.M != A.M || B.N != A.N)
			return false;
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				if (A.data[i][j] != B.data[i][j])
					return false;
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				result = 31 * result + data[i][j];
			}
		}
		return result;
	}

	// Return C = A - B
	public Matrix minus(Matrix B) {
		Matrix A = this;
		if (B.M != A.M || B.N != A.N)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.data[i][j] = A.data[i][j] - B.data[i][j];
		return C;
	}

	// Return C = A * B
	public Matrix multiply(Matrix B) {
		Matrix A = this;
		if (A.N != B.M)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(A.M, B.N);
		for (int i = 0; i < C.M; i++)
			for (int j = 0; j < C.N; j++)
				for (int k = 0; k < A.N; k++)
					C.data[i][j] += (A.data[i][k] * B.data[k][j]);
		return C;
	}

	// Return C = A + B
	public Matrix plus(Matrix B) {
		Matrix A = this;
		if (B.M != A.M || B.N != A.N)
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(M, N);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < N; j++)
				C.data[i][j] = A.data[i][j] + B.data[i][j];
		return C;
	}

	@Override
	// Return a string representation of the matrix
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int m = 0; m < M; m++) {
			sb.append("[ ");
			for (int n = 0; n < N; n++) {
				sb.append(data[m][n] + " ");
			}
			sb.append("]\n");
		}
		return sb.toString();
	}

}
