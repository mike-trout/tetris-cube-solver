package miketrout.tetriscubesolver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class TetrisCube extends JFrame {

	private class SolveCubeTask extends SwingWorker<Stack<Piece>, Stack<Piece>> {

		private final Stack<Piece> cubePieces;
		private final ArrayBlockingQueue<Piece> failed;
		private final TetrisCubePanel panel;
		private final Collection<Piece> pieces;
		private final ArrayBlockingQueue<Piece> remaining;
		private int tries = 0;

		public SolveCubeTask(Collection<Piece> pieces, TetrisCubePanel panel) {
			this.pieces = pieces;
			this.panel = panel;
			cubePieces = new Stack<Piece>();
			remaining = new ArrayBlockingQueue<Piece>(12);
			failed = new ArrayBlockingQueue<Piece>(12);
			remaining.addAll(pieces);
		}

		@Override
		protected Stack<Piece> doInBackground() throws Exception {
			pushPieces();
			while (failed.size() > 0) {
				popPieces();
				failed.drainTo(remaining);
				pushPieces();
			}
			return cubePieces;
		}

		@Override
		protected void done() {
			try {
				panel.setPieces(get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		private void popPieces() {
			Piece topPermutation = cubePieces.pop();
			Piece topPiece = null;
			for (Piece piece : pieces) {
				if (piece.getName().equals(topPermutation.getName())) {
					topPiece = piece;
					break;
				}
			}
			if (topPiece == null)
				return;
			Boolean fits = false;
			while (topPiece.hasNextPermutation()) {
				Piece permutation = topPiece.nextPermutation();
				if (tryPiece(permutation)) {
					fits = true;
					cubePieces.push(permutation);
					break;
				}
			}
			if (!fits) {
				topPiece.resetPermutations();
				try {
					failed.put(topPiece);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				popPieces();
			}
		}

		@Override
		protected void process(List<Stack<Piece>> chunks) {
			panel.setPieces(chunks.get(chunks.size() - 1));
		}

		private void pushPieces() {
			try {
				Boolean fits;
				while (remaining.size() > 0) {
					Piece piece = remaining.take();
					fits = false;
					while (piece.hasNextPermutation()) {
						Piece permutation = piece.nextPermutation();
						if (tryPiece(permutation)) {
							fits = true;
							cubePieces.push(permutation);
							break;
						}
					}
					if (!fits) {
						piece.resetPermutations();
						failed.put(piece);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		private boolean tryPiece(Piece piece) {
			tries++;
			if (tries > 1000000) {
				publish(cubePieces);
				tries = 0;
			}
			for (Piece cubePiece : cubePieces) {
				if (cubePiece.intersects(piece))
					return false;
			}
			for (int zIndex = 0; zIndex < 4; zIndex++) {
				boolean[][] zPlane = new boolean[4][4];
				for (Piece cubePiece : cubePieces) {
					for (Vector vector : cubePiece.getVectors()) {
						if (vector.getData(2) == zIndex)
							zPlane[vector.getData(0)][vector.getData(1)] = true;
					}
				}
				boolean zPlaneFull = true;
				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 4; j++) {
						if (zPlane[i][j] == false)
							zPlaneFull = false;
					}
				}
				if (zPlaneFull == false) {
					for (Vector vector : piece.getVectors()) {
						if (vector.getData(2) == zIndex)
							return true;
					}
					return false;
				}
			}
			return false;
		}

	}

	private class TetrisCubePanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private Stack<Piece> cubePieces;

		public TetrisCubePanel() {
			this.cubePieces = new Stack<Piece>();
			setBorder(BorderFactory.createLineBorder(Color.black));
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(195, 200);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for (Piece piece : this.cubePieces)
				piece.paintPiece(g);

			g.setColor(Color.BLACK);
			g.drawRect(10, 10, 80, 80);
			g.drawString("z = 0", 10, 100);

			g.drawRect(105, 10, 80, 80);
			g.drawString("z = 1", 105, 100);

			g.drawRect(10, 105, 80, 80);
			g.drawString("z = 2", 10, 195);

			g.drawRect(105, 105, 80, 80);
			g.drawString("z = 3", 105, 195);
		}

		public void setPieces(Stack<Piece> cubePieces) {
			this.cubePieces.setSize(cubePieces.size());
			Collections.copy(this.cubePieces, cubePieces);
			repaint();
		}

	}

	private static final long serialVersionUID = 1L;

	private static void createAndShowGUI() {
		new TetrisCube();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				createAndShowGUI();
			}

		});
	}

	private final TetrisCubePanel panel;
	private final Collection<Piece> pieces;
	private SwingWorker<Stack<Piece>, Stack<Piece>> solveCubeTask;

	public TetrisCube() {
		super("Tetris Cube");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pieces = new ArrayList<Piece>();
		this.pieces.add(Piece.BLUE1);
		this.pieces.add(Piece.RED1);
		this.pieces.add(Piece.YELLOW1);
		this.pieces.add(Piece.BLUE2);
		this.pieces.add(Piece.RED2);
		this.pieces.add(Piece.YELLOW2);
		this.pieces.add(Piece.BLUE3);
		this.pieces.add(Piece.RED3);
		this.pieces.add(Piece.YELLOW3);
		this.pieces.add(Piece.BLUE4);
		this.pieces.add(Piece.RED4);
		this.pieces.add(Piece.YELLOW4);
		this.panel = new TetrisCubePanel();
		this.solveCubeTask = new SolveCubeTask(pieces, panel);
		this.panel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (solveCubeTask.isDone()) {
					solveCubeTask = new SolveCubeTask(pieces, panel);
					solveCubeTask.execute();
				}
			}

		});
		add(this.panel);
		pack();
		setResizable(false);
		setVisible(true);
		this.solveCubeTask.execute();
	}

}
