package shape;

import java.awt.Color;
import java.util.*;

public class Snake extends Vector<Block> {
	private static final long serialVersionUID = -4291321031204316836L;

	public static final int LEFT = 2, RIGHT = -2, UP = 1, DOWN = -1;
	public int currentDestination = 0;

	public Snake() {
		Random r = new Random();
		add(new Block(r.nextInt(48) + 1, r.nextInt(38) + 1, Color.GREEN));
	}

	public boolean move() {
		int dx = 0, dy = 0;
		switch (currentDestination) {
		case LEFT:
			dx = -1;
			break;
		case RIGHT:
			dx = 1;
			break;
		case UP:
			dy = -1;
			break;
		case DOWN:
			dy = 1;
			break;
		case 0:
			return true;
		}
		int m = get(size() - 1).getM() + dx;
		int n = get(size() - 1).getN() + dy;
		if (m > 0 && m < 49 && n > 0 && n < 39) {
			for (Block b : this) {
				Block temp = new Block(m, n, Color.GREEN);
				if (temp.equals(b))
					return false;
			}
			remove(0);
			Block b = new Block(m, n, Color.GREEN);
			b.currentDestination = currentDestination;
			add(b);
			return true;
		}
		return false;
	}

	public void eat(Food food) {
		if (food.getColor() == Color.BLUE)
			remove(get(0));
		else {
			add(0, (Block) get(0).clone());
		}
	}

	public void setCurrentDestination(int destination) {
		if (Math.abs(currentDestination) != Math.abs(destination))
			currentDestination = destination;
	}

	public int getCurrentDestination() {
		return currentDestination;
	}
}
