import java.awt.*;
import java.awt.image.BufferedImage;

public class SpaceShip extends GameObject {

    private double velx;
    private double vely;

    private int life = 5;
    private int hp = 70;



    private int score = 0;

    private int up, down, left, right, shoot, respawn;

    private BufferedImage spaceImg;

    public SpaceShip(double x, double y, BufferedImage spaceImg, int up, int down, int left, int right, int shoot, int respawn) {
        super(x, y);
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.shoot = shoot;
        this.respawn = respawn;
        this.spaceImg = spaceImg;
    }
    public void tick(){


       x += velx;
       y += vely;


       if(x < 0){
           x = 0;
       }
       else if(x > 950){
           x = 950;
       }
       else if(y < 0){
           y = 0;
       }
       else if(y > 690){
           y = 690;
       }



    }
    public void render(Graphics g) {

        g.setColor(Color.red);                              //health bars
        g.fillRect((int) x, (int) y + 80, 70, 20);

        g.setColor(Color.green);
        g.fillRect((int) x, (int) y + 80, hp, 20);




        g.drawImage(spaceImg, (int) x, (int) y, null);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
    public double getVelx() {
        return velx;
    }

    public void setVelx(double velx) {
        this.velx = velx;
    }

    public double getVely() {
        return vely;
    }

    public void setVely(double vely) {
        this.vely = vely;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getShoot() {
        return shoot;
    }

    public void setShoot(int shoot) {
        this.shoot = shoot;
    }

    public int getRespawn() {
        return respawn;
    }

    public void setRespawn(int respawn) {
        this.respawn = respawn;
    }

    public BufferedImage getSpaceImg() {
        return spaceImg;
    }

    public void setSpaceImg(BufferedImage spaceImg) {
        this.spaceImg = spaceImg;
    }

}
