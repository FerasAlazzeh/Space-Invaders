import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;



public class KeyInput extends KeyAdapter {


    GameWorld game;

    public KeyInput(GameWorld game) {
        this.game = game;
    }

    public void keyPressed(KeyEvent e) {
       game.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        game.keyReleased(e);
    }

    public void keyTyped(KeyEvent e) {
        game.keyTyped(e);
    }
}
