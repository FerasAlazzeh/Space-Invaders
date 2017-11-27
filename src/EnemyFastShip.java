import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EnemyFastShip extends GameObject {

    private int speedx = -7;
    private int speedy = 5;
    BufferedImage Image;
    BufferedImage projectileImg;
    ProjectileController Shooter;
    Random rand;
    int randomNumber;
    boolean dead = false;
    private int hittoKill = 2;

    public EnemyFastShip(double x, double y, BufferedImage fastShip, BufferedImage projectileImg, GameWorld game) {
        super(x, y);
        this.Image = fastShip;
        this.projectileImg = projectileImg;
        Shooter = new ProjectileController(game);
        rand = new Random();
    }


    public void hit(){              //2 kills to kill fast ship
        hittoKill -= 1;
        if(hittoKill <= 0){
            dead = true;
        }
    }


    public void tick(){

        if(!dead) {
            x += speedx;
            y += speedy;

            randomNumber = rand.nextInt(4000); // 0 to 999999999
            if (randomNumber < 100) {
                Shooter.addProjectile(new Projectile((int) x + 30, (int) y + 20, projectileImg, 0, 5));
            } else {
            }
            if (x < 0) {
                speedx = -speedx;
            } else if (x > 950) {
                speedx = -speedx;
            }
            if (y > 200) {
                speedy = -speedy;
            }
            if (y < 0) {
                speedy = -speedy;
            }
        }
        Shooter.tick();
    }

    public void render(Graphics g){
        if(!dead) {
            g.drawImage(Image, (int) x, (int) y, null);
        }
        Shooter.render(g);
    }


}
