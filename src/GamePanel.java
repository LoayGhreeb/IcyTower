import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {
    private final static int panelWidth = 640, panelHeight = 640;
    private static Image background, rightBorder, leftBorder;
    private static Player player;
    private List<Image> platformTypes;
    private static List<Platform> gamePlatforms;
    public static int currentY, dy;
    private boolean inGame, scroll;

    private Audio greeting, fall, tryAgain, gameOver, great, good;
   private boolean goodFlag, greatFlag;
    public GamePanel() {
        initGame();
    }

    public void initGame() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setFocusable(true);
        currentY = 0;
        dy = 1;
        player = new Player();
        KeyHandler keyHandler = new KeyHandler(player);
        addKeyListener(keyHandler);
        inGame = true;
        platformTypes = new ArrayList<>();
        gamePlatforms = new ArrayList<>();
        loadImages();
        loadSound();
        generateRandomFloors();
    }

    private void loadImages() {
        background = new ImageIcon(getClass().getResource("Resources/Images/background.png")).getImage();
        rightBorder = new ImageIcon(getClass().getResource("Resources/Images/RightBorder.png")).getImage();
        leftBorder = new ImageIcon(getClass().getResource("Resources/Images/LeftBorder.png")).getImage();

        Image center, left, right;
        int numOfLevels = 2;
        for (int i = 1; i <= numOfLevels; i++) {
            left = new ImageIcon(getClass().getResource("Resources/Images/Level" + i + "Left.png")).getImage();
            center = new ImageIcon(getClass().getResource("Resources/Images/Level" + i + "Center.png")).getImage();
            right = new ImageIcon(getClass().getResource("Resources/Images/Level" + i + "Right.png")).getImage();
            createFloor(left, center, right);
        }
    }
    private void loadSound(){
        greeting = new Audio("Resources/Audio/greeting.wav");
        fall =new Audio("Resources/Audio/falling.wav");
        tryAgain =new Audio("Resources/Audio/tryAgain.wav");
        gameOver =new Audio("Resources/Audio/gameOver.wav");
        great =new Audio("Resources/Audio/great.wav");
        good =new Audio("Resources/Audio/good.wav");

        greeting.start();

    }
    private void createFloor(Image left, Image center, Image right) {

        BufferedImage img;
        //Every platform will have 5 different width : full width, 9, 7, 5, 3
        for (int i = 11; i >= 3; i -= 2) {
            int temp = i;
            //Full width
            if(i == 11)  temp = 36;

            BufferedImage middle = new BufferedImage(temp * center.getWidth(null), center.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics g = middle.getGraphics();
            int x = 0;
            while (temp > 0) {
                g.drawImage(center, x, 0, null);
                x += center.getWidth(null);
                temp--;
            }
            int w = middle.getWidth(null) + right.getWidth(null) + left.getWidth(null);
            int h = right.getHeight(null);
            img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            g = img.getGraphics();

            x = 0;
            g.drawImage(left, x, 0, null);
            x += left.getWidth(null);
            g.drawImage(middle, x, 0, null);
            x += middle.getWidth(null);
            g.drawImage(right, x, 0, null);

            platformTypes.add(img);
        }
    }

    private void generateRandomFloors() {

        SecureRandom random = new SecureRandom();

        int y = panelHeight - 2 * platformTypes.get(0).getHeight(null);
        player.y = y - player.height;
        int x, index;
        int maxDist = 96;
        //Generate 200 random floors : Floors 0 - 99: Stone , Floors 100 - 199: Ice
        for (int i = 0; i <= 200; i++) {
            // floor types [0 -> 4]  Stone, [5 -> 8] Ice

            //Full Width at : 0, 50, 100, 150
            if (i == 0 || i == 50) {
                index = 0;
                gamePlatforms.add(new Platform(platformTypes.get(index), 0, y, i));
            } else if (i == 100 || i == 150) {
                index = 5;
                gamePlatforms.add(new Platform(platformTypes.get(index), 0, y, i));
            } else {
                if (i < 100) index = random.nextInt(1, 5);
                else index = random.nextInt(6, 9);


                int xLowerBound = gamePlatforms.get(i-  1).left - maxDist - platformTypes.get(index).getWidth(null);
                if(xLowerBound < getBorderWidth())
                    xLowerBound = getBorderWidth();

                int xUpperBound = gamePlatforms.get(i - 1).right + maxDist;
                if(xUpperBound + platformTypes.get(index).getWidth(null) > panelWidth - getBorderWidth())
                    xUpperBound = panelWidth - getBorderWidth() - platformTypes.get(index).getWidth(null);

                x = random.nextInt(xLowerBound, xUpperBound + 1);
                gamePlatforms.add(new Platform(platformTypes.get(index), x, y, i));
            }
            y -= 100;
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        int fps = 60;
        double drawInterval = 1000000000.0 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (inGame) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1) {
                if(player.getScore() >= 40) scroll = true;
                player.move(scroll);
                if(scroll)
                    for (Platform platform : gamePlatforms)  platform.move();

                player.fallOfPlatform(gamePlatforms);
                repaint();
                if(player.getScore()/10 % 100 == 50 && player.getScore() != 0){
                    if(!goodFlag){
                        good.start();
                        goodFlag = true;
                    }
                }
                else if(player.getScore()/10 % 100 == 0 && player.getScore() != 0){
                    if(!greatFlag)  {
                        great.start();
                        greatFlag = true;
                    }
                }
                else{
                    goodFlag = false;
                    greatFlag = false;
                }

                if(player.getScore() == 2000){
                    inGame = false;
                    if(JOptionPane.showConfirmDialog(null, "You Won :) Your score is : " + player.getScore() + "\nDo you want to play again ? ",
                            "Game Over", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        resetGame();
                    }
                    else System.exit(0);
                }


                if(player.top >= panelHeight - currentY) {
                    inGame = false;
                    fall.start();
                    gameOver.start();
                    if(JOptionPane.showConfirmDialog(null, "Game Over :) Your score is : " + player.getScore() + "\nDo you want to play again ? ",
                            "Game Over", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        resetGame();
                    }
                    else System.exit(0);
                }
                delta--;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawObjects(g2d);
    }

    private void drawObjects(Graphics2D g2d) {
        if(scroll) {
            currentY += dy;
        }
        if(player.getScore() == 1000)
            dy=2;

        // Move the camera
        g2d.translate(0, currentY);
        //Draw background
        int y = panelHeight - background.getHeight(null);
        for (int i = 0; i < 14; i++) {
            g2d.drawImage(background, 0, y, null);
            y -= background.getHeight(null);
        }

        //Draw Platforms
        for (int i = gamePlatforms.size() - 1; i >= 0; i--) {
            Platform platform = gamePlatforms.get(i);
            if(platform.getY() > panelHeight  - currentY ) gamePlatforms.remove(i);
            platform.draw(g2d);
        }

        //Draw left & right border
        y = panelHeight - getBorderHeight();
        for (int i = 0; i < 70; i++) {
            g2d.drawImage(rightBorder, panelWidth - rightBorder.getWidth(null), y, null);
            g2d.drawImage(leftBorder, 0, y, null);
            y -= getBorderHeight();
        }

        //Draw Player
        player.draw(g2d);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("FORTE", Font.PLAIN, 45));
        g2d.drawString("Score : " + player.getScore() ,getBorderWidth(), (int) (panelHeight - currentY  - 10));
    }


    public void resetGame() {
        tryAgain.start();
        currentY = 0;
        dy= 1;
        scroll = false;
        player.reset();
        gamePlatforms.clear();
        generateRandomFloors();
        inGame = true;
    }
    public static int getPanelWidth() {
        return panelWidth;
    }

    public static int getPanelHeight() {
        return panelHeight;
    }

    public static int getBorderWidth() {
        return leftBorder.getWidth(null);
    }

    public static int getBorderHeight() {
        return leftBorder.getHeight(null);
    }
}
