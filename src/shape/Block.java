package shape;

import java.awt.Color;
import java.awt.Rectangle;

/**
 * 
 * @author Benzolamps
 *
 */
public class Block extends Rectangle {

	private static final long serialVersionUID = 1207728129959144395L;

	public int currentDestination = 0;
	public int currentDestinationExtra = 0;
	private Color color;
	private int m, n;

	public Block(int m, int n, Color color) {
		this.height = this.width = 15;
		this.m = m;
		this.n = n;
		x = m * 15;
		y = n * 15;
		this.color = color;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
		x = m * 15;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
		y = n * 15;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
