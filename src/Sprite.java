import javax.swing.*;
import java.awt.*;

public class Sprite {
    protected int x, y;
    protected int width, height;
    public int left, right, top, bottom;


    public Sprite(){}
    public Sprite(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    protected Image loadImage(String path){
        return (new ImageIcon(getClass().getResource(path)).getImage());
    }
}
