package main;

import javax.swing.*;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -3249573337883474642L;
	DrawPanel draw = new DrawPanel();

	public MainFrame() {
		super("̰����");

		setResizable(false);
		setSize(1004, 640);
		addKeyListener(draw);
		getContentPane().add(draw);
		setVisible(true);
	}

	public static void main(String[] args) throws ClassCastException {
		// TODO Auto-generated method stub
		MainFrame.setDefaultLookAndFeelDecorated(true);
		new MainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}