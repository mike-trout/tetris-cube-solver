package miketrout.tetriscubesolver;

public class Vector extends Matrix {

	public static final Vector i = new Vector(new int[] { 1, 0, 0 });
	public static final Vector j = new Vector(new int[] { 0, 1, 0 });
	public static final Vector k = new Vector(new int[] { 0, 0, 1 });

	private static int[][] extendDimensions(int[] data) {
		int[][] d = new int[data.length][1];
		for (int i = 0; i < data.length; i++)
			d[i][0] = data[i];
		return d;
	}

	public static Vector valueOf(Matrix matrix) {
		if (matrix.N != 1) {
			throw new IllegalArgumentException(
					"The " + matrix.M + " x " + matrix.N + " matrix cannot be cast as a vector.");
		}
		int[] d = new int[matrix.M];
		for (int i = 0; i < matrix.M; i++)
			d[i] = matrix.data[i][0];
		return new Vector(d);
	}

	public Vector(int[] data) {
		super(extendDimensions(data));
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Vector))
			return false;
		return super.equals(object);
	}

	public int getData(int i) {
		return super.data[i][0];
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public Vector minus(Matrix B) {
		return valueOf(super.minus(B));
	}

	@Override
	public Vector multiply(Matrix B) {
		return valueOf(super.multiply(B));
	}

	@Override
	public Vector plus(Matrix B) {
		return valueOf(super.plus(B));
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
