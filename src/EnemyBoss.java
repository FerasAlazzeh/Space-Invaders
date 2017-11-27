import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class EnemyBoss extends GameObject {

    private int health = 170;

    private int speedx = 4;

    BufferedImage boss;
    BufferedImage bossBullet;
    ProjectileController bossShooter;
    Random rand;
    int randomNumber;

    public EnemyBoss(double x, double y, BufferedImage bossImage, BufferedImage bossBullet, GameWorld game) {
        super(x, y);
        this.boss = bossImage;
        this.bossBullet = bossBullet;
        bossShooter = new ProjectileController(game);
        rand = new Random();
    }

    public void tick(){

        x += speedx;

        randomNumber = rand.nextInt(5000); // 0 to 999999999

        if(randomNumber < 100){
            bossShooter.addProjectile(new Projectile((int) x + 75, (int)y + 210, bossBullet, 0,5));
            bossShooter.addProjectile(new Projectile((int) x + 75, (int)y + 210, bossBullet, 3, 3));
            bossShooter.addProjectile(new Projectile((int) x + 75, (int)y + 210, bossBullet, -3, 3));
        }
        else{
        }

        if(x < 0){
            speedx = -speedx;
        }
        else if(x > 850){
            speedx = -speedx;
        }

        bossShooter.tick();
    }


    public void render(Graphics g){

        g.setColor(Color.red);                              //health barsdwwdw

        g.fillRect((int) x, (int) y - 20, 170, 20);

        g.setColor(Color.green);
        g.fillRect((int) x, (int) y - 20, health, 20);


        g.drawImage(boss, (int) x, (int) y,  null);


        bossShooter.render(g);
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
