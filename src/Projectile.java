import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Projectile extends GameObject {



    private double speedx;
    private double speedy;


    BufferedImage laserImg;




    public Projectile(double x, double y, BufferedImage laserimg, double speedx, double speedy) {
        super(x, y);                 //set tank x and y to bullet x and y initial
        this.laserImg = laserimg;
        this.speedx = speedx;
        this.speedy = speedy;
    }

    public void tick() {

        x += speedx;
        y += speedy;

        if(x < 10){
            speedx = -speedx;
        }
        else if(x > 930){
            speedx = -speedx;
        }

    }


    public void render(Graphics g) {

        g.drawImage(laserImg , (int) x, (int) y, null);
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


}
