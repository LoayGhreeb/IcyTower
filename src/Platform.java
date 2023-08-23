import java.awt.*;

public class Platform extends Sprite {
    private final Image image;
    public int index;

    public Platform(Image image, int x, int y, int index) {
        super(x, y);
        this.image = image;
        this.index = index;
        initBlock();
    }

    private void initBlock() {
        width  = image.getWidth(null);
        height = image.getHeight(null);

        left = x;
        right = x + width;
        top = y;
        bottom = y + height;
    }

    public void move(){
        //Floor always fall down
        y++;

        //Update floor bounders
        top = y;
        bottom = y + height;
    }
    public boolean collide (Player player) {
        if (player.right >= left && player.left <= right && player.bottom == top) {
            player.isFalling = false; //stop falling
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, getX(), getY(), null);
    }
}