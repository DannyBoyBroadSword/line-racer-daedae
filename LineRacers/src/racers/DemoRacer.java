package racers;

import game.Racer;
import processing.core.PApplet;

public class DemoRacer extends Racer {

	public DemoRacer(PApplet parent, String name, float r, float g, float b) {
		super(parent, name, r, g, b);
	}

	public void start() {
		
	}

	@Override
	public int[] go() {
		double r = Math.random();
		if (r < 0.25)
			return new int[] { 1, 0 };
		else if (r < 0.5)
			return new int[] { -1, 0 };
		else if (r < 0.725)
			return new int[] { 0, 1 };
		else
			return new int[] { 0, -1 };
	}

}
