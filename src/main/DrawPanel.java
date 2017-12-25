package main;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import shape.*;

public class DrawPanel extends JPanel implements KeyListener, Runnable {
	private static final long serialVersionUID = -1790872881032236015L;

	private Block[][] blockBorder = new Block[50][40];
	private Block[][] blockScore = new Block[16][8];
	private Block[][] blockBlood = new Block[16][8];
	private Block[][] blockLength = new Block[16][8];
	private Block[][] blockExtra = new Block[16][16];
	private Snake snake = new Snake();
	private Food food = Food.produceOneFood(snake);
	private Food lastFood = new Food(snake.get(0).getM(), snake.get(0).getN(), snake.get(0).getColor(), snake);
	private boolean gameOver = false;
	private boolean paused = false;
	private boolean needSleep = false;
	private int value = 0;
	private long score = 0;
	private int scoreAddition = 0;
	private int blood = 30;

	public DrawPanel() {
		setLayout(null);
		initial();
		Thread t = new Thread(this);
		t.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setPaint(Color.WHITE);
		g2D.setFont(new Font("Consolas", Font.BOLD, 24));
		g2D.fillRect(0, 0, getWidth(), getHeight());
		drawOneBlock(g2D, blockBorder);
		drawOneBlock(g2D, blockBlood);
		drawOneBlock(g2D, blockLength);
		drawOneBlock(g2D, blockScore);
		drawOneBlock(g2D, blockExtra);
		drawSnake(g2D);
		drawFood(g2D);
		drawBlood(g2D);
		drawLength(g2D);
		drawScore(g2D);
		if (gameOver || paused) {
			String str = null;
			if (paused)
				str = "PAUSED";
			if (gameOver)
				str = "GAME OVER";
			g2D.setPaint(new Color(255, 0, 128));
			g2D.drawString(str, 20, 385);
		}
	}

	private void drawFood(Graphics2D g2D) {
		g2D.setPaint(food.getColor());
		g2D.fillRoundRect(food.x, food.y, food.width, food.height, food.width, food.width);
	}

	private void drawSnake(Graphics2D g2D) {
		g2D.setPaint(snake.get(0).getColor());
		for (Block b : snake)
			g2D.fillRoundRect(b.x, b.y, b.width, b.height, b.width, b.width);
	}

	private void drawBlood(Graphics2D g2D) {
		g2D.setPaint(Color.RED);
		g2D.drawString("BLOOD", 770, 32);
		g2D.drawString(String.format("%12d", blood / 3), 805, 100);
	}

	private void drawLength(Graphics2D g2D) {
		g2D.setPaint(Color.GREEN);
		g2D.drawString("CURRENT LENGTH", 770, 155);
		g2D.drawString(String.format("%12d", snake.size()), 805, 223);
	}

	private void drawScore(Graphics2D g2D) {
		g2D.setPaint(Color.MAGENTA);
		g2D.drawString("SCORE    " + (scoreAddition == 0 ? "" : ("+" + scoreAddition)), 770, 274);
		g2D.drawString(String.format("%12d", score), 805, 342);
	}

	private void drawOneBlock(Graphics2D g2D, Block[][] block) {
		for (int i = 0; i < block.length; i++) {
			for (int j = 0; j < block[0].length; j++) {
				Color color = block[i][j].getColor();
				if (!color.equals(Color.WHITE)) {
					g2D.setPaint(block[i][j].getColor());
					int x = block[i][j].x;
					int y = block[i][j].y;
					int width = block[i][j].width;
					int height = block[i][j].height;
					g2D.fillRoundRect(x, y, width, height, width, height);
				}
			}
		}
	}

	private void initial() {
		initOneBlock(blockBorder, 0, 0);
		initOneBlock(blockBlood, 50, 0);
		initOneBlock(blockLength, 50, 8);
		initOneBlock(blockScore, 50, 16);
		initOneBlock(blockExtra, 50, 24);
	}

	private void initOneBlock(Block[][] block, int x, int y) {
		for (int i = x; i < x + block.length; i++) {
			for (int j = y; j < y + block[i - x].length; j++) {
				block[i - x][j - y] = new Block(i, j, Color.WHITE);
			}
		}
		for (int i = 0; i < block.length; i++) {
			block[i][0].setColor(Color.BLACK);
			block[i][block[0].length - 1].setColor(Color.BLACK);
		}
		for (int i = 0; i < block[0].length; i++) {
			block[0][i].setColor(Color.BLACK);
			block[block.length - 1][i].setColor(Color.BLACK);
		}
	}

	private int calculateScore() {
		if (food.getColor() == Color.RED && blood < 30)
			return 0;
		int dm = Math.abs(food.getM() - lastFood.getM());
		int dn = Math.abs(food.getN() - lastFood.getN());
		int m = value - dm - dn;
		;
		int n = 0;
		if (food.getColor() == Color.YELLOW)
			n = 15;
		else if (food.getColor() == Color.CYAN)
			n = 10;
		else if (food.getColor() == Color.ORANGE)
			n = 5;
		if (m > 90)
			return n + 1;
		else
			return n + 10 - m / 10;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_P:
			pause();
			break;
		case KeyEvent.VK_R:
			restart();
			break;
		}
		if (paused || gameOver || needSleep)
			return;
		switch (arg0.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			snake.setCurrentDestination(Snake.UP);
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			snake.setCurrentDestination(Snake.DOWN);
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			snake.setCurrentDestination(Snake.LEFT);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			snake.setCurrentDestination(Snake.RIGHT);
			break;
		}
		needSleep = true;
	}

	private void restart() {
		paused = gameOver = false;
		value = 0;
		score = 0;
		scoreAddition = 0;
		blood = 30;
		snake = new Snake();
		food = Food.produceOneFood(snake);
		lastFood = new Food(snake.get(0).getM(), snake.get(0).getN(), snake.get(0).getColor(), snake);
		repaint();
	}

	private void pause() {
		paused = !paused;
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (paused || gameOver)
				continue;
			if (snake.getCurrentDestination() != 0)
				value++;
			if (!snake.move()) {
				if (blood > 0)
					--blood;
				else
					gameOver = true;
			}
			needSleep = false;
			if (food.move()) {
				scoreAddition = calculateScore();
				score += scoreAddition;
				if (food.getColor() == Color.RED) {
					if (blood < 30)
						blood += 3;
				}
				lastFood = (Food) food.clone();
				food = Food.produceOneFood(snake);
				value = 0;
			}
			repaint();
		}
	}
}