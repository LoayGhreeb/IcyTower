import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    private final Player player;

    public KeyHandler(Player player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            player.isMovingLeft = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            player.isMovingRight = true;
        } else if (key == KeyEvent.VK_SPACE && !player.isJumping && !player.isFalling) {
            player.isJumping = true;
            player.jumpClip.start();
            player.jumpY = Math.max(player.y - player.jumpHeight, -GamePanel.currentY - player.height);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            player.isMovingLeft = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            player.isMovingRight = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}