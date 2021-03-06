package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PVector;

public class Game {

	private Track track;
	private ArrayList<Racer> racers;
	private HashMap<Racer, Integer> scores;
	private PApplet parent;
	private int startFrame;
	boolean active;

	public Game(PApplet parent, Track track) {
		this.track = track;
		this.parent = parent;
		racers = new ArrayList<Racer>();
		active = false;
		scores = new HashMap<Racer, Integer>();
	}

	public void update() {
		track.display();
		int time = parent.frameCount - startFrame;
		if (active) {
			ArrayList<Racer> finished = new ArrayList<Racer>();
			for (Racer r : racers) {
				r.update();
				if (Arrays.equals(r.getPosition(), track.getEnd())) {
					System.out.println(r.getName() + " finished at " + time);
					scores.put(r, time);
					finished.add(r);
				}
			}
			for (Racer r : finished) {
				racers.remove(r);
			}
			removeCrashedRacers();
		}
	}

	public HashMap<Racer, Integer> getScore() {
		if (racers.size() == 0)
			return scores;
		return null;
	}

	public boolean addRacer(Racer r) {
		if (!racers.contains(r)) {
			racers.add(r);
			return true;
		}
		return false;
	}

	public Track getTrack() {
		return track;
	}

	public void removeCrashedRacers() {
		int time = parent.frameCount - startFrame;
		ArrayList<Racer> dead = new ArrayList<Racer>();
		for (Racer r : racers) {
			PVector pos = r.getPositionVector();
			if (!track.openAt((int) (pos.x + 0.5), (int) (pos.y + 0.5)))
				dead.add(r);
		}
		for (Racer r : dead) {
			System.out.println(r.getName() + " crashed at " + time);
			scores.put(r, -1);
			racers.remove(r);
		}
	}

	public void activate() {
		active = true;
		startFrame = parent.frameCount;
		for (Racer r : racers) {
			r.setTrack(track);
			r.start();
		}
	}

}
