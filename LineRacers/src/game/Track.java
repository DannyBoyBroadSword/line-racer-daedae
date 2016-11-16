package game;

import java.util.Arrays;
import java.util.LinkedList;

import processing.core.PApplet;

public class Track {

	private boolean[][] grid;
	private int[] start;
	private int[] end;
	private PApplet parent;
	private static int[][] dirs = new int[][] { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

	public Track(PApplet parent, int width, int height) {
		grid = new boolean[width][height];
		start = null;
		end = null;
		this.parent = parent;
	}

	public boolean openAt(int x, int y) {
		if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
			return false;
		}
		return grid[x][y];
	}

	public void display() {
		float colWidth = 1f * parent.width / grid.length;
		float rowHeight = 1f * parent.height / grid[0].length;
		parent.noStroke();
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				if (start != null && x == start[0] && y == start[1]) {
					parent.fill(0, 255, 0);
				} else if (end != null && x == end[0] && y == end[1]) {
					parent.fill(255, 0, 0);
				} else if (!grid[x][y]) {
					parent.fill(0);
				} else {
					parent.fill(255);
				}
				parent.rect(x * colWidth, y * rowHeight, colWidth, rowHeight);
			}
		}
	}

	public boolean[][] getGrid() {
		boolean[][] out = new boolean[grid.length][];
		for (int i = 0; i < out.length; i++) {
			out[i] = Arrays.copyOf(grid[i], grid[i].length);
		}
		return out;
	}

	public int nRows() {
		return grid[0].length;
	}

	public int nCols() {
		return grid.length;
	}

	public int[] getStart() {
		return Arrays.copyOf(start, start.length);
	}

	public int[] getEnd() {
		return Arrays.copyOf(end, end.length);
	}

	public void setCell(int x, int y, boolean wall, int dist) {
		if (dist <= 0 || x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
			return;
		}
		grid[x][y] = wall;
		int[] loc = new int[] { x, y };
		if (Arrays.equals(loc, start))
			start = null;
		if (Arrays.equals(loc, end))
			end = null;
		for (int[] dir : dirs) {
			setCell(x + dir[0], y + dir[1], wall, dist - 1);
		}
	}

	public boolean setStart(int x, int y) {
		if (openAt(x, y)) {
			start = new int[] { x, y };
			return true;
		}
		return false;
	}

	public boolean setEnd(int x, int y) {
		if (openAt(x, y)) {
			end = new int[] { x, y };
			return true;
		}
		return false;
	}

	public boolean raceReady() {
		if (start == null || end == null)
			return false;
		LinkedList<int[]> visited = new LinkedList<int[]>();
		LinkedList<int[]> frontier = new LinkedList<int[]>();
		frontier.add(start);
		while (frontier.size() > 0) {
			int[] curr = frontier.removeLast();
			if (Arrays.equals(curr, end))
				return true;
			visited.add(curr);
			for (int[] dir : dirs) {
				int[] c = new int[] { curr[0] + dir[0], curr[1] + dir[1] };
				if (c[0] < 0 || c[0] >= grid.length || c[1] < 0 || c[1] >= grid[0].length || !grid[c[0]][c[1]])
					continue;
				boolean good = true;
				for (int[] x : visited) {
					if (Arrays.equals(c, x)) {
						good = false;
						break;
					}
				}
				for (int[] x : frontier) {
					if (Arrays.equals(c, x)) {
						good = false;
						break;
					}
				}
				if (good)
					frontier.add(c);
			}
		}
		return false;
	}

	public void reset() {
		start = null;
		end = null;
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				grid[x][y] = false;
			}
		}
	}
	
	public void invert() {
		start = null;
		end = null;
		for (int x = 0; x < grid.length; x++) {
			for (int y = 0; y < grid[0].length; y++) {
				grid[x][y] = !grid[x][y];
			}
		}
	}

}
