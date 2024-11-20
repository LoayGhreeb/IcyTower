package org.example;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class Game extends JFrame {

    public Game() {
        initGame();
    }

    public void initGame() {
        add(new GamePanel());
        setTitle("Icy Tower");
        setIconImage(new ImageIcon(getClass().getResource("/images/Idle2.png")).getImage());
        setResizable(false);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        Game game = new Game();
        game.setVisible(true);
    }
}
