package game;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import racers.*;

public class LineRacerDaemon extends PApplet {

	private Track t;
	private boolean editMode = true;
	private int brushSize = 3;
	private ArrayList<Integer> keysPressed = new ArrayList<Integer>();
	private Game g;

	public static void main(String[] args) {
		PApplet.main("game.LineRacerDaemon");
	}

	public void settings() {
		size(640, 480);
	}

	public void setup() {
		System.out.println("Click and drag to draw the racetrack.\nHold x to add walls back in.");
		System.out.println("Press '[' and ']' to increase or decrease the brush size.");
		System.out.println("Press 'r' to reset the course to all walls.");
		System.out.println("Press 'i' to invert the course.");
		System.out.println("When you are ready to begin the race, press ENTER.");
		t = new Track(this, 64, 48);
		g = new Game(this, t);
		// Add racers to the game here.
		for (int i = 0; i < 100; i++) {
			g.addRacer(new DemoRacer(this, "Demo_" + i, (float) (255 * Math.random()), (float) (255 * Math.random()),
					(float) (255 * Math.random())));
		}
	}

	public void draw() {
		background(255);
		g.update();
		HashMap<Racer, Integer> score = g.getScore();
		if (score != null) {
			exit();
		}
	}

	public void mousePressed() {
		int x = mouseX / (width / t.nCols());
		int y = mouseY / (height / t.nRows());
		if (editMode) {
			if (keysPressed.contains(83)) {
				t.setStart(x, y);
			} else if (keysPressed.contains(69)) {
				t.setEnd(x, y);
			} else {
				t.setCell(x, y, !keysPressed.contains(88), brushSize);
			}
		}
	}

	public void mouseDragged() {
		int x = mouseX / (width / t.nCols());
		int y = mouseY / (height / t.nRows());
		if (editMode) {
			t.setCell(x, y, !keysPressed.contains(88), brushSize);
		}
	}

	public void keyPressed() {
		if (!keysPressed.contains(keyCode))
			keysPressed.add(keyCode);
		if (key == '[')
			brushSize = brushSize > 1 ? brushSize - 1 : 1;
		else if (key == ']')
			brushSize++;
		else if (key == 'r')
			t.reset();
		else if (key == 'i')
			t.invert();
		else if (keyCode == ENTER) {
			if (t.raceReady()) {
				System.out.println("Ready to race!\n-----");
				editMode = false;
				g.activate();
			} else
				System.out.println("Track incomplete!");
			// TODO: Trigger the start of the race.
		}
	}

	public void keyReleased() {
		if (keysPressed.contains(keyCode)) {
			keysPressed.remove(Integer.valueOf(keyCode));
		}
	}

}
