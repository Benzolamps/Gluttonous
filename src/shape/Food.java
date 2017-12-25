package shape;

import java.awt.Color;
import java.util.Random;

/**
 * 
 * @author Benzolamps
 *
 */
public class Food extends Block {
	private static final long serialVersionUID = -7299247196930660433L;

	private Food original = (Food) clone();
	private Snake snake;
	private static Random r = new Random();

	public Food(int m, int n, Color color, Snake snake) {
		super(m, n, color);
		this.snake = snake;
		if (getColor() == Color.PINK || getColor() == Color.BLUE)
			currentDestination = 0;
		if (getColor() == Color.ORANGE)
			currentDestination = (r.nextInt(2) + 1) * (r.nextInt(2) == 0 ? 1 : -1);
		if (getColor() == Color.YELLOW) {
			currentDestination = (r.nextInt(2) == 0 ? 1 : -1);
			currentDestinationExtra = 2 * (r.nextInt(2) == 0 ? 1 : -1);
		}
		if (getColor() == Color.CYAN) {
			original.setM((getM() >= (49 / 2)) ? (49 - getM()) : getM());
			original.setN((getN() >= (39 / 2)) ? (39 - getN()) : getN());
			if (r.nextInt(2) == 0)
				original.currentDestination = Snake.DOWN;
			else
				original.currentDestination = Snake.RIGHT;
		}
	}

	public static Food produceOneFood(Snake snake) {
		int t = r.nextInt(10);
		outer: while (true) {
			int m = r.nextInt(48) + 1;
			int n = r.nextInt(38) + 1;
			for (int i = 0; i < snake.size(); i++) {
				if (snake.get(i).getM() == m && snake.get(i).getN() == n)
					continue outer;
			}

			if (snake.size() <= 1)
				return new Food(m, n, Color.PINK, snake);
			if (snake.size() >= 1600)
				return new Food(m, n, Color.BLUE, snake);
			switch (t) {
			case 0:
				return new Food(m, n, Color.BLUE, snake);
			case 1:
				return new Food(m, n, Color.ORANGE, snake);
			case 2:
				return new Food(m, n, Color.YELLOW, snake);
			case 3:
				return new Food(m, n, Color.RED, snake);
			case 4:
				if (m > 23 && 49 - m > 23 || n > 18 && 39 - n > 18)
					continue outer;
				return new Food(m, n, Color.CYAN, snake);
			default:
				return new Food(m, n, Color.PINK, snake);
			}
		}
	}

	private boolean eatabled() {
		int m = Math.abs(snake.get(snake.size() - 1).getM() - getM());
		int n = Math.abs(snake.get(snake.size() - 1).getN() - getN());
		if (m == 0 && n == 0)
			return true;
		if (getColor() == Color.ORANGE || getColor() == Color.YELLOW || getColor() == Color.CYAN) {
			int d = Math.abs(snake.getCurrentDestination()) - Math.abs(currentDestination);
			if (d != 0)
				return false;
			if (m == 0 && n == 1 && Math.abs(currentDestination) == Snake.UP)
				return true;
			if (m == 1 && n == 0 && Math.abs(currentDestination) == Snake.LEFT)
				return true;
		}
		return false;
	}

	public void moveCyan() {

		for (int i = 0; i < snake.size(); i++) {
			if (equals(snake.get(i))) {
				currentDestination = -currentDestination;
				if (original.currentDestination == Snake.DOWN)
					original.currentDestination = Snake.RIGHT;
				else if (original.currentDestination == Snake.RIGHT)
					original.currentDestination = Snake.DOWN;
			}
		}
		int m = original.getM();
		int n = original.getN();
		if (getM() == m && getN() == n)
			currentDestination = original.currentDestination;
		if (getM() == m && getN() == 39 - n) {
			if (original.currentDestination == Snake.DOWN)
				currentDestination = Snake.RIGHT;
			else if (original.currentDestination == Snake.RIGHT)
				currentDestination = Snake.UP;
		}

		if (getM() == 49 - m && getN() == 39 - n) {
			if (original.currentDestination == Snake.DOWN)
				currentDestination = Snake.UP;
			else if (original.currentDestination == Snake.RIGHT)
				currentDestination = Snake.LEFT;
		}

		if (getM() == 49 - m && getN() == n) {
			if (original.currentDestination == Snake.DOWN)
				currentDestination = Snake.LEFT;
			else if (original.currentDestination == Snake.RIGHT)
				currentDestination = Snake.DOWN;
		}

		switch (currentDestination) {
		case Snake.LEFT:
			setM(getM() - 1);
			break;
		case Snake.RIGHT:
			setM(getM() + 1);
			break;
		case Snake.DOWN:
			setN(getN() + 1);
			break;
		case Snake.UP:
			setN(getN() - 1);
			break;
		}

	}

	public boolean move() {
		if (eatabled()) {
			snake.eat(this);
			return true;
		}
		if (getColor() == Color.CYAN) {
			moveCyan();
			return false;
		}

		for (int i = 0; i < snake.size(); i++) {
			if (equals(snake.get(i))) {
				if (getColor() == Color.YELLOW) {
					try {
						// int m0 = snake.get(i).getM();
						// int n0 = snake.get(i).getN();
						int m2 = snake.get(i + 1).getM();
						int n2 = snake.get(i + 1).getN();
						int m1 = snake.get(i - 1).getM();
						int n1 = snake.get(i - 1).getN();

						if (m1 != m2 && n1 != n2) {
							int d1 = snake.get(i + 1).currentDestination * snake.get(i - 1).currentDestination;
							int d2 = currentDestination * currentDestinationExtra;
							if (d1 == d2) {
								currentDestination = -currentDestination;
								currentDestinationExtra = -currentDestinationExtra;
							}
						} else if (m1 == m2)
							currentDestinationExtra = -currentDestinationExtra;
						else if (n1 == n2)
							currentDestination = -currentDestination;
					} catch (ArrayIndexOutOfBoundsException e) {
						if (Math.abs(snake.get(i).currentDestination) == Snake.UP)
							currentDestination = -currentDestination;
						else if (Math.abs(snake.get(i).currentDestination) == Snake.LEFT)
							currentDestinationExtra = -currentDestinationExtra;
					}
				} else
					currentDestination = -currentDestination;
			}
		}
		if (getM() == 49 || getM() == 0 || getN() == 39 || getN() == 0) {
			if (getColor() == Color.ORANGE)
				currentDestination = -currentDestination;
			if (getColor() == Color.YELLOW) {
				if (getM() == 0 || getM() == 49)
					currentDestinationExtra = -currentDestinationExtra;
				if (getN() == 0 || getN() == 39)
					currentDestination = -currentDestination;
			}
		}
		switch (currentDestination) {
		case Snake.LEFT:
			setM(getM() - 1);
			break;
		case Snake.RIGHT:
			setM(getM() + 1);
			break;
		case Snake.DOWN:
			setN(getN() - 1);
			break;
		case Snake.UP:
			setN(getN() + 1);
			break;
		}
		switch (currentDestinationExtra) {
		case Snake.LEFT:
			setM(getM() - 1);
			break;
		case Snake.RIGHT:
			setM(getM() + 1);
			break;
		case Snake.DOWN:
			setN(getN() - 1);
			break;
		case Snake.UP:
			setN(getN() + 1);
			break;
		}
		return false;
	}
}
