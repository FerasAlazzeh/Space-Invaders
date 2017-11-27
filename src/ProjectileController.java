import java.awt.*;
import java.util.LinkedList;

public class ProjectileController {


    private LinkedList<Projectile> bang = new LinkedList<>();

    Projectile tempProjectile;

    GameWorld game;

    public ProjectileController(GameWorld game) {

        this.game = game;
    }


    public void tick() {

        for (int i = 0; i < bang.size(); i++) {
            tempProjectile = bang.get(i);
            tempProjectile.tick();
        }


        for(int i = 0; i < bang.size(); i++){

            if(tempProjectile.x < 0){
                removeProjectile(tempProjectile);           //delete projectiles
            }
            else if(tempProjectile.x  > 980){
                removeProjectile(tempProjectile);
            }
            else if(tempProjectile.y < 0){
                removeProjectile(tempProjectile);
            }
            else if(tempProjectile.y > 690){
                removeProjectile(tempProjectile);
            }
        }


    }


    public void render(Graphics g) {
        for (int i = 0; i < bang.size(); i++) {
            tempProjectile = bang.get(i);
            tempProjectile.render(g);
        }
    }

    public void addProjectile(Projectile missle) {
        bang.add(missle);
    }

    public void removeProjectile(Projectile missle) {
        bang.remove(missle);
    }

    public LinkedList<Projectile> checkBulletCollsion() {
        return bang;
    }



}
