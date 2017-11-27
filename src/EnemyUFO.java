import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;


public class EnemyUFO extends GameObject {
   private int speedx = 2;
   private int speedy = 2;
   BufferedImage ufoImage;
   BufferedImage projectileImg;
   ProjectileController ufoShooter;
   Random rand;
   int randomNumber;
   boolean dead = false;

    public EnemyUFO(double x, double y, BufferedImage ufoImage, BufferedImage projectileImg, GameWorld game) {
        super(x, y);
        this.ufoImage = ufoImage;
        this.projectileImg = projectileImg;
        ufoShooter = new ProjectileController(game);
        rand = new Random();
    }

    public void tick(){

     if(!dead) {
         x += speedx;
         y += speedy;

         randomNumber = rand.nextInt(4600); // 0 to 999999999
         if (randomNumber < 100) {
             ufoShooter.addProjectile(new Projectile((int) x + 30, (int) y + 20, projectileImg, 0, 5));
         } else {
         }
         if (x < 0) {
             speedx = -speedx;
         } else if (x > 950) {
             speedx = -speedx;
         }
         if (y > 600) {
             speedy = -speedy;
         }
         if (y < 0) {
             speedy = -speedy;
         }
     }
        ufoShooter.tick();
    }

    public void render(Graphics g){
       if(!dead) {
           g.drawImage(ufoImage, (int) x, (int) y, null);
       }
        ufoShooter.render(g);
    }


}
