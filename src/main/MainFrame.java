package main;

import javax.swing.JFrame;

/**
 * 
 * @author Benzolamps
 *
 */
public class MainFrame extends JFrame {

    private static final long serialVersionUID = -3249573337883474642L;

    private DrawPanel draw = new DrawPanel();

    public MainFrame() {
        super("贪吃蛇");
        setResizable(false);
        setSize(1004, 640);
        addKeyListener(draw);
        getContentPane().add(draw);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainFrame.setDefaultLookAndFeelDecorated(true);
        new MainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
