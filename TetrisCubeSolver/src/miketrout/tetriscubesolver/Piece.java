package miketrout.tetriscubesolver;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Piece implements Comparable<Piece> {

	public static final Piece BLUE1 = new Piece(new Vector[] { new Vector(new int[] { 0, 0, 0 }),
			new Vector(new int[] { 1, 0, 0 }), new Vector(new int[] { 1, 0, 1 }), new Vector(new int[] { 2, 0, 0 }),
			new Vector(new int[] { 0, 1, 0 }), new Vector(new int[] { 0, 2, 0 }) }, "BLUE1");
	public static final Piece BLUE2 = new Piece(new Vector[] { new Vector(new int[] { 1, 0, 0 }),
			new Vector(new int[] { 2, 0, 0 }), new Vector(new int[] { 3, 0, 0 }), new Vector(new int[] { 0, 1, 0 }),
			new Vector(new int[] { 1, 1, 0 }) }, "BLUE2");
	public static final Piece BLUE3 = new Piece(new Vector[] { new Vector(new int[] { 1, 0, 0 }),
			new Vector(new int[] { 2, 0, 0 }), new Vector(new int[] { 0, 1, 0 }), new Vector(new int[] { 0, 1, 1 }),
			new Vector(new int[] { 1, 1, 0 }) }, "BLUE3");
	public static final Piece BLUE4 = new Piece(new Vector[] { new Vector(new int[] { 0, 0, 0 }),
			new Vector(new int[] { 1, 0, 0 }), new Vector(new int[] { 2, 0, 0 }), new Vector(new int[] { 2, 0, 1 }),
			new Vector(new int[] { 0, 1, 0 }), new Vector(new int[] { 0, 2, 0 }) }, "BLUE4");
	public static final Piece RED1 = new Piece(new Vector[] { new Vector(new int[] { 0, 0, 0 }),
			new Vector(new int[] { 1, 0, 0 }), new Vector(new int[] { 1, 1, 0 }), new Vector(new int[] { 0, 1, 0 }),
			new Vector(new int[] { 0, 0, 1 }) }, "RED1");
	public static final Piece RED2 = new Piece(new Vector[] { new Vector(new int[] { 0, 0, 0 }),
			new Vector(new int[] { 1, 0, 0 }), new Vector(new int[] { 2, 0, 0 }), new Vector(new int[] { 0, 1, 0 }),
			new Vector(new int[] { 0, 2, 0 }) }, "RED2");
	public static final Piece RED3 = new Piece(new Vector[] { new Vector(new int[] { 1, 0, 0 }),
			new Vector(new int[] { 0, 1, 0 }), new Vector(new int[] { 1, 1, 0 }), new Vector(new int[] { 2, 1, 0 }),
			new Vector(new int[] { 0, 2, 0 }), new Vector(new int[] { 0, 2, 1 }) }, "RED3");
	public static final Piece RED4 = new Piece(new Vector[] { new Vector(new int[] { 0, 0, 0 }),
			new Vector(new int[] { 1, 0, 0 }), new Vector(new int[] { 1, 1, 0 }), new Vector(new int[] { 2, 0, 0 }),
			new Vector(new int[] { 3, 0, 0 }) }, "RED4");
	public static final Piece YELLOW1 = new Piece(new Vector[] { new Vector(new int[] { 0, 1, 0 }),
			new Vector(new int[] { 1, 1, 0 }), new Vector(new int[] { 1, 0, 1 }), new Vector(new int[] { 1, 1, 1 }),
			new Vector(new int[] { 1, 2, 1 }), new Vector(new int[] { 2, 1, 1 }) }, "YELLOW1");
	public static final Piece YELLOW2 = new Piece(new Vector[] { new Vector(new int[] { 0, 0, 0 }),
			new Vector(new int[] { 1, 0, 0 }), new Vector(new int[] { 1, 0, 1 }), new Vector(new int[] { 1, 1, 1 }),
			new Vector(new int[] { 2, 1, 1 }) }, "YELLOW2");
	public static final Piece YELLOW3 = new Piece(new Vector[] { new Vector(new int[] { 0, 0, 0 }),
			new Vector(new int[] { 0, 1, 0 }), new Vector(new int[] { 0, 1, 1 }), new Vector(new int[] { 1, 1, 1 }),
			new Vector(new int[] { 2, 1, 1 }) }, "YELLOW3");
	public static final Piece YELLOW4 = new Piece(new Vector[] { new Vector(new int[] { 0, 0, 0 }),
			new Vector(new int[] { 1, 0, 0 }), new Vector(new int[] { 2, 0, 0 }), new Vector(new int[] { 1, 1, 0 }),
			new Vector(new int[] { 1, 1, 1 }) }, "YELLOW4");

	private final String name;
	private Iterator<Piece> permutationIterator;
	private List<Piece> permutations;
	private final Vector[] vectors;

	public Piece(Vector[] vectors, String name) {
		this.vectors = vectors;
		this.name = name;
	}

	@Override
	public int compareTo(Piece anotherPiece) {
		int zWeightingThis = 0;
		int zWeightingOther = 0;
		for (Vector vector : this.vectors) {
			zWeightingThis += vector.getData(2);
		}
		for (Vector vector : anotherPiece.vectors) {
			zWeightingOther += vector.getData(2);
		}
		return zWeightingThis - zWeightingOther;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Piece))
			return false;
		Piece piece = (Piece) obj;
		if (!this.name.equals(piece.name))
			return false;
		if (this.vectors.length != piece.vectors.length)
			return false;
		for (int i = 0; i < this.vectors.length; i++) {
			if (!this.vectors[i].equals(piece.vectors[i]))
				return false;
		}
		return true;
	}

	private boolean fits() {
		for (Vector vector : vectors) {
			if (vector.getData(0) > 3 || vector.getData(1) > 3 || vector.getData(2) > 3)
				return false;
		}
		return true;
	}

	public String getName() {
		return name;
	}

	private void getPermutations() {
		Set<Piece> permutationSet = new HashSet<Piece>();
		permutationSet.add(new Piece(vectors, name));
		Set<Piece> newCombinations = new HashSet<Piece>();
		for (Piece piece : permutationSet) {
			for (int i = 0; i < 4; i++) {
				piece = piece.rotateX();
				newCombinations.add(piece);
			}
		}
		permutationSet.addAll(newCombinations);
		newCombinations = new HashSet<Piece>();
		for (Piece piece : permutationSet) {
			for (int i = 0; i < 4; i++) {
				piece = piece.rotateY();
				newCombinations.add(piece);
			}
		}
		permutationSet.addAll(newCombinations);
		newCombinations = new HashSet<Piece>();
		for (Piece piece : permutationSet) {
			for (int i = 0; i < 4; i++) {
				piece = piece.rotateZ();
				newCombinations.add(piece);
			}
		}
		permutationSet.addAll(newCombinations);
		newCombinations = new HashSet<Piece>();
		for (Piece piece : permutationSet) {
			while ((piece = piece.shiftX()).fits()) {
				newCombinations.add(piece);
			}
		}
		permutationSet.addAll(newCombinations);
		newCombinations = new HashSet<Piece>();
		for (Piece piece : permutationSet) {
			while ((piece = piece.shiftY()).fits()) {
				newCombinations.add(piece);
			}
		}
		permutationSet.addAll(newCombinations);
		newCombinations = new HashSet<Piece>();
		for (Piece piece : permutationSet) {
			while ((piece = piece.shiftZ()).fits()) {
				newCombinations.add(piece);
			}
		}
		permutationSet.addAll(newCombinations);
		permutations = new ArrayList<Piece>(permutationSet);
		Collections.sort(permutations);
		permutationIterator = permutations.iterator();
	}

	public Vector[] getVectors() {
		return vectors;
	}

	@Override
	public int hashCode() {
		int result = 17;
		for (Vector vector : vectors) {
			result = 31 * result + vector.hashCode();
		}
		return result;
	}

	public boolean hasNextPermutation() {
		if (permutations == null || permutationIterator == null)
			getPermutations();
		return permutationIterator.hasNext();
	}

	public boolean intersects(Piece piece) {
		for (Vector v1 : this.vectors)
			for (Vector v2 : piece.vectors)
				if (v1.equals(v2))
					return true;
		return false;
	}

	public Piece nextPermutation() {
		if (permutations == null || permutationIterator == null)
			getPermutations();
		return permutationIterator.next();
	}

	private void normalise() {
		int offsetX = 0;
		int offsetY = 0;
		int offsetZ = 0;
		for (Vector vector : vectors) {
			if (0 - vector.getData(0) > offsetX) {
				offsetX = 0 - vector.getData(0);
			}
			if (0 - vector.getData(1) > offsetY) {
				offsetY = 0 - vector.getData(1);
			}
			if (0 - vector.getData(2) > offsetZ) {
				offsetZ = 0 - vector.getData(2);
			}
		}
		for (int i = 0; i < vectors.length; i++) {
			vectors[i] = new Vector(new int[] { vectors[i].getData(0) + offsetX, vectors[i].getData(1) + offsetY,
					vectors[i].getData(2) + offsetZ });
		}
	}

	public void paintPiece(Graphics g) {
		Color numberColor = Color.WHITE;
		String numberString = "0";
		Color pieceColor = Color.BLACK;
		if (this.name.equals("BLUE1") || this.name.equals("BLUE2") || this.name.equals("BLUE3")
				|| this.name.equals("BLUE4")) {
			pieceColor = Color.BLUE;
			numberColor = Color.WHITE;
		} else if (this.name.equals("RED1") || this.name.equals("RED2") || this.name.equals("RED3")
				|| this.name.equals("RED4")) {
			pieceColor = Color.RED;
			numberColor = Color.BLACK;
		} else if (this.name.equals("YELLOW1") || this.name.equals("YELLOW2") || this.name.equals("YELLOW3")
				|| this.name.equals("YELLOW4")) {
			pieceColor = Color.YELLOW;
			numberColor = Color.BLACK;
		}
		if (this.name.equals("BLUE1") || this.name.equals("RED1") || this.name.equals("YELLOW1")) {
			numberString = "1";
		} else if (this.name.equals("BLUE2") || this.name.equals("RED2") || this.name.equals("YELLOW2")) {
			numberString = "2";
		} else if (this.name.equals("BLUE3") || this.name.equals("RED3") || this.name.equals("YELLOW3")) {
			numberString = "3";
		} else if (this.name.equals("BLUE4") || this.name.equals("RED4") || this.name.equals("YELLOW4")) {
			numberString = "4";
		}
		for (Vector vector : vectors) {
			int xOffset, yOffset;
			switch (vector.getData(2)) {
			case 0:
				xOffset = 10;
				yOffset = 70;
				break;
			case 1:
				xOffset = 105;
				yOffset = 70;
				break;
			case 2:
				xOffset = 10;
				yOffset = 165;
				break;
			case 3:
				xOffset = 105;
				yOffset = 165;
				break;
			default:
				xOffset = 0;
				yOffset = 0;
				break;
			}
			xOffset += vector.getData(0) * 20;
			yOffset -= vector.getData(1) * 20;
			g.setColor(pieceColor);
			g.fillRect(xOffset, yOffset, 20, 20);
			g.setColor(numberColor);
			g.drawString(numberString, xOffset + 6, yOffset + 14);
		}
	}

	public void resetPermutations() {
		permutationIterator = permutations.iterator();
	}

	public Piece rotateX() {
		Vector[] newVectors = new Vector[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			newVectors[i] = Vector.valueOf(Matrix.Rx.multiply(vectors[i]));
		}
		Piece newPiece = new Piece(newVectors, this.name);
		newPiece.normalise();
		return newPiece;
	}

	public Piece rotateY() {
		Vector[] newVectors = new Vector[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			newVectors[i] = Vector.valueOf(Matrix.Ry.multiply(vectors[i]));
		}
		Piece newPiece = new Piece(newVectors, this.name);
		newPiece.normalise();
		return newPiece;
	}

	public Piece rotateZ() {
		Vector[] newVectors = new Vector[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			newVectors[i] = Vector.valueOf(Matrix.Rz.multiply(vectors[i]));
		}
		Piece newPiece = new Piece(newVectors, this.name);
		newPiece.normalise();
		return newPiece;
	}

	public Piece shiftX() {
		Vector[] newVectors = new Vector[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			newVectors[i] = vectors[i].plus(Vector.i);
		}
		return new Piece(newVectors, this.name);
	}

	public Piece shiftY() {
		Vector[] newVectors = new Vector[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			newVectors[i] = vectors[i].plus(Vector.j);
		}
		return new Piece(newVectors, this.name);
	}

	public Piece shiftZ() {
		Vector[] newVectors = new Vector[vectors.length];
		for (int i = 0; i < vectors.length; i++) {
			newVectors[i] = vectors[i].plus(Vector.k);
		}
		return new Piece(newVectors, this.name);
	}

	@Override
	public String toString() {
		return name;
	}

}
