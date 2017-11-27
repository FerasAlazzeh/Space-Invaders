import java.awt.*;
import java.awt.image.BufferedImage;

public class MovingBackground extends GameObject {
   private BufferedImage spaceImg;
   private GameWorld game;
    public MovingBackground(double x, double y, BufferedImage spaceImg, GameWorld game) {
        super(x, y);
        this.spaceImg = spaceImg;
        this.game = game;
    }

   public void tick(){
        y += .8;

        if (y > 0){
            y = -700;
        }
    }

    public void render(Graphics g){
        g.drawImage(spaceImg, (int)x ,(int) y , game);
    }
}
