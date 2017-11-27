import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class GameWorld extends Canvas implements Runnable {

    private boolean running = false;
    private Thread thread;
    private final String TITLE = "Space Invaders";
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static final int SCALE = 1;

    private boolean isShooting = false;
    private boolean init = false;
    private boolean isDead;

    private Random rand;

    private SpaceShip spaceShip;
    private ProjectileController spaceShipLaser;
    private MovingBackground movingbackground;
    LinkedList<EnemyUFO> enemyUFO = new LinkedList<>();
    LinkedList<EnemyBoss> enemyBOSS = new LinkedList<>();
    LinkedList<EnemyFastShip> enemyFASTSHIP = new LinkedList<>();

    private int speed;
    private int highScore = 0;

    private MusicPlayer music;

    private static BufferedImage background, spaceShipImg, laserImg, ufo, ufoProjectile, heart, boss, bossBullet, gameOver, fastShip, fastShipBullet;


    private synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }


    private synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    public void run() {
        Long lasTime = System.nanoTime();
        final double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;              //game keeps freezing think its gamme loop
        double delta = 0; //calculated time passed
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lasTime) / ns;
            lasTime = now;
            if (delta >= 1) {
                tick();
                updates++;
                delta--;

            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;                                                  //counting ticks and frames
                System.out.println(updates + " Ticks, Fps " + frames);
                updates = 0;
                frames = 0;
            }

        }

        stop();
    }


    private void tick() {

        this.setFocusable(true);

        if (init && !isDead) {
            movingbackground.tick();
            spaceShip.tick();
            spaceShipLaser.tick();
            collision();
            isAlive();
            updateEnemyStatus();

            if (enemyBOSS.isEmpty()) {              //while score is 1000 spawn enemy ufo's
                addEnemyUFO();
                addEnemyFastShip();
            }


            if (spaceShip.getScore() % 300 == 0 && enemyBOSS.isEmpty() && spaceShip.getScore() != 0) {
                addBoss();
            }

            if (!enemyUFO.isEmpty()) {
                for (int i = 0; i < enemyUFO.size(); i++) {
                    enemyUFO.get(i).tick();
                }
            }
            if (!enemyBOSS.isEmpty()) {
                enemyBOSS.getFirst().tick();
            }
            if (!enemyFASTSHIP.isEmpty()) {
                for (int i = 0; i < enemyFASTSHIP.size(); i++) {
                    enemyFASTSHIP.get(i).tick();
                }
            }
        }

    }


    public void render() {

        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();     //draw stuff under this

        if (!init) {
            init();
        }

        if (!isDead) {
            movingbackground.render(g);
            spaceShip.render(g);
            spaceShipLaser.render(g);

            if (!enemyUFO.isEmpty()) {
                for (int i = 0; i < enemyUFO.size(); i++) {
                    enemyUFO.get(i).render(g);
                }
            }
            if (!enemyFASTSHIP.isEmpty()) {
                for (int i = 0; i < enemyFASTSHIP.size(); i++) {
                    enemyFASTSHIP.get(i).render(g);
                }
            }

            if (!enemyBOSS.isEmpty()) {
                enemyBOSS.getFirst().render(g);
            }


            //draw hearts
            switch (spaceShip.getLife()) {
                case 5:
                    g.drawImage(heart, (int) spaceShip.getX(), (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 15, (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 30, (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 45, (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 60, (int) spaceShip.getY() + 110, this);
                case 4:
                    g.drawImage(heart, (int) spaceShip.getX(), (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 15, (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 30, (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 45, (int) spaceShip.getY() + 110, this);
                    break;
                case 3:
                    g.drawImage(heart, (int) spaceShip.getX(), (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 15, (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 30, (int) spaceShip.getY() + 110, this);
                    break;
                case 2:
                    g.drawImage(heart, (int) spaceShip.getX(), (int) spaceShip.getY() + 110, this);
                    g.drawImage(heart, (int) spaceShip.getX() + 15, (int) spaceShip.getY() + 110, this);
                    break;
                case 1:
                    g.drawImage(heart, (int) spaceShip.getX(), (int) spaceShip.getY() + 110, this);
                    break;
                case 0:
                    isDead = true;
                    break;

            }

            g.setFont(new Font("default", Font.BOLD, 30));
            g.drawString("SCORE  " + spaceShip.getScore(), 10, 760);
        } else {                   //end game

            if (spaceShip.getScore() > highScore) {
                highScore = spaceShip.getScore();
            }


            g.drawImage(gameOver, 0, 0, this);

            g.setColor(Color.green);
            g.setFont(new Font("default", Font.BOLD, 30));
            g.drawString("SCORE  " + spaceShip.getScore(), 10, 760);

            g.setColor(Color.red);
            g.setFont(new Font("default", Font.BOLD, 50));
            g.drawString("HIGH SCORE  " + highScore, 350, 600);

            g.setColor(Color.red);
            g.setFont(new Font("default", Font.BOLD, 80));
            g.drawString("PRESS R TO PLAY AGAIN", 10, 100);

        }

        g.dispose();    //used to release system recources
        bs.show();      //used to show the current buffers
    }

    private void addEnemyFastShip() {
        int randomNumber = rand.nextInt(4000); // 0 to 4999

        if (randomNumber < 10) {
            enemyFASTSHIP.add(new EnemyFastShip(512, 10, fastShip, fastShipBullet, this));
        } else if (randomNumber < 20) {
            enemyFASTSHIP.add(new EnemyFastShip(40, 10, fastShip, fastShipBullet, this));
        } else if (randomNumber < 30) {
            enemyFASTSHIP.add(new EnemyFastShip(200, 10, fastShip, fastShipBullet, this));
        } else if (randomNumber < 40) {
            enemyFASTSHIP.add(new EnemyFastShip(300, 10, fastShip, fastShipBullet, this));
        } else if (randomNumber < 50) {
            enemyFASTSHIP.add(new EnemyFastShip(400, 10, fastShip, fastShipBullet, this));
        }
    }


    private void addEnemyUFO() {

        int randomNumber = rand.nextInt(2000); // 0 to 4999

        if (randomNumber < 10) {
            enemyUFO.add(new EnemyUFO(512, 10, ufo, ufoProjectile, this));
        } else if (randomNumber < 20) {
            enemyUFO.add(new EnemyUFO(40, 10, ufo, ufoProjectile, this));
        } else if (randomNumber < 30) {
            enemyUFO.add(new EnemyUFO(200, 10, ufo, ufoProjectile, this));
        } else if (randomNumber < 40) {
            enemyUFO.add(new EnemyUFO(300, 10, ufo, ufoProjectile, this));
        } else if (randomNumber < 50) {
            enemyUFO.add(new EnemyUFO(400, 10, ufo, ufoProjectile, this));
        }
    }

    private void updateEnemyStatus() {

        if (!enemyUFO.isEmpty()) {
            for (int i = 0; i < enemyUFO.size(); i++) {
                if (enemyUFO.get(i).dead && enemyUFO.get(i).ufoShooter.checkBulletCollsion().isEmpty()) {
                    enemyUFO.remove(i);
                }
            }
        }

        if (!enemyBOSS.isEmpty()) {
            if (enemyBOSS.getFirst().getHealth() <= 0) {
                spaceShip.setScore(spaceShip.getScore() + 100);
                enemyBOSS.removeFirst();
            }
        }
        if(!enemyFASTSHIP.isEmpty()){
            for (int i = 0; i < enemyFASTSHIP.size(); i++) {
                if (enemyFASTSHIP.get(i).dead && enemyFASTSHIP.get(i).Shooter.checkBulletCollsion().isEmpty()) {
                    enemyFASTSHIP.remove(i);
                }
            }
        }
    }

    public void addBoss() {
        enemyBOSS.add(new EnemyBoss(512, 40, boss, bossBullet, this));
    }


    public void isAlive() {

        if (spaceShip.getHp() <= 0) {

            spaceShip.setLife(spaceShip.getLife() - 1);

            if (spaceShip.getLife() <= 0) {
                spaceShip.setVelx(0);
                spaceShip.setVely(0);
                isDead = true;
            } else {
                spaceShip.setHp(70);
                spaceShip.y = 600;
                spaceShip.x = 512;
            }

        }

    }

    public void init() {
        isDead = false;
        rand = new Random();
        this.addKeyListener(new KeyInput(this));
        loadImages();
        speed = 5;
        spaceShip = new SpaceShip(512, 600, spaceShipImg, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE, KeyEvent.VK_R);
        spaceShipLaser = new ProjectileController(this);
        movingbackground = new MovingBackground(0, -700, background, this);
        music = new MusicPlayer();

        init = true;
    }


    public void loadImages() {
        try {
            background = ImageIO.read(new File("background.jpeg"));
            spaceShipImg = ImageIO.read(new File("spaceShipimg.png"));
            laserImg = ImageIO.read(new File("laser.png"));
            ufo = ImageIO.read(new File("ufo.png"));
            ufoProjectile = ImageIO.read(new File("ufoBullet.png"));
            heart = ImageIO.read(new File("hearts.png"));
            boss = ImageIO.read(new File("boss.png"));
            bossBullet = ImageIO.read(new File("bossBullet.png"));
            gameOver = ImageIO.read(new File("gameOver.jpg"));
            fastShip = ImageIO.read(new File("fastShip.png"));
            fastShipBullet = ImageIO.read(new File("fastBullet.png"));
        } catch (IOException e) {
            System.out.println("Error Loading Recources");
        }

    }


    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!isDead) {
            if (key == spaceShip.getUp()) {
                spaceShip.setVely(-speed);
            } else if (key == spaceShip.getLeft()) {                 //[up,left,right,down]
                spaceShip.setVelx(-speed);
            } else if (key == spaceShip.getRight()) {
                spaceShip.setVelx(speed);
            } else if (key == spaceShip.getDown()) {
                spaceShip.setVely(speed);
            } else if (key == spaceShip.getShoot() && !isShooting) {
                isShooting = true;
                spaceShipLaser.addProjectile(new Projectile(spaceShip.getX() + 30, spaceShip.getY() + 10, laserImg, 0, -10));
            }


        }
        if (key == spaceShip.getRespawn() && isDead) {
            spaceShip.setHp(70);
            spaceShip.setLife(5);
            spaceShip.x = 512;
            spaceShip.y = 600;
            spaceShip.setScore(0);
            enemyBOSS.clear();
            enemyUFO.clear();
            enemyFASTSHIP.clear();
            isDead = false;
        }

    }

    public void keyTyped(KeyEvent e) {
        int key = e.getKeyCode();
        if (!isDead) {
            if (key == spaceShip.getUp()) {
                spaceShip.setVely(-speed);
            } else if (key == spaceShip.getLeft()) {                 //[up,left,right,down]
                spaceShip.setVelx(-speed);
            } else if (key == spaceShip.getRight()) {
                spaceShip.setVelx(speed);
            } else if (key == spaceShip.getDown()) {
                spaceShip.setVely(speed);
            } else if (key == spaceShip.getShoot() && !isShooting) {
                isShooting = true;
                spaceShipLaser.addProjectile(new Projectile(spaceShip.getX() + 30, spaceShip.getY() + 10, laserImg, 0, -10));
            }

        }
        if (key == spaceShip.getRespawn() && isDead) {
            spaceShip.setHp(70);
            spaceShip.setLife(5);
            spaceShip.x = 512;
            spaceShip.y = 600;
            enemyBOSS.clear();
            enemyUFO.clear();
            enemyFASTSHIP.clear();
            isDead = false;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (!isDead) {
            if (key == spaceShip.getUp()) {
                spaceShip.setVely(0);
            } else if (key == spaceShip.getLeft()) {
                spaceShip.setVelx(0);
            } else if (key == spaceShip.getRight()) {
                spaceShip.setVelx(0);
            } else if (key == spaceShip.getDown()) {
                spaceShip.setVely(0);
            } else if (key == spaceShip.getShoot()) {
                isShooting = false;
            }
        }

    }


    public void collision() {
        if (!enemyUFO.isEmpty()) {
            for (int i = 0; i < enemyUFO.size(); i++) {                  //spaceship vs ufo
                if (spaceShip.getBounds(40, 40).intersects(enemyUFO.get(i).getBounds(40, 40)) && !enemyUFO.get(i).dead) {
                    enemyUFO.get(i).dead = true;
                    spaceShip.setHp(spaceShip.getHp() - 20);
                    spaceShip.setScore(spaceShip.getScore() + 10);
                }
            }
        }

        if (!spaceShipLaser.checkBulletCollsion().isEmpty() && !enemyUFO.isEmpty()) {                  //space ship laser vs ufo
            for (int j = 0; j < spaceShipLaser.checkBulletCollsion().size(); j++) {
                for (int i = 0; i < enemyUFO.size(); i++) {
                    if (spaceShipLaser.checkBulletCollsion().get(j).getBounds(40, 40).intersects(enemyUFO.get(i).getBounds(40, 40)) && !enemyUFO.get(i).dead) {
                        spaceShipLaser.checkBulletCollsion().remove(j);
                        enemyUFO.get(i).dead = true;
                        spaceShip.setScore(spaceShip.getScore() + 10);
                        break;
                    }
                }
            }
        }

        if (!enemyUFO.isEmpty()) {
            for (int i = 0; i < enemyUFO.size(); i++) {                             //spaceship vs ufo lasers
                if (!enemyUFO.get(i).ufoShooter.checkBulletCollsion().isEmpty()) {
                    for (int j = 0; j < enemyUFO.get(i).ufoShooter.checkBulletCollsion().size(); j++) {
                        if (spaceShip.getBounds(50, 50).intersects(enemyUFO.get(i).ufoShooter.checkBulletCollsion().get(j).getBounds(40, 40))) {
                            spaceShip.setHp(spaceShip.getHp() - 10);
                            enemyUFO.get(i).ufoShooter.checkBulletCollsion().remove(j);
                        }
                    }
                }
            }
        }
        //boss bullets vs spaceship
        if (!enemyBOSS.isEmpty() && !enemyBOSS.getFirst().bossShooter.checkBulletCollsion().isEmpty()) {
            for (int i = 0; i < enemyBOSS.getFirst().bossShooter.checkBulletCollsion().size(); i++) {
                if (spaceShip.getBounds(30, 30).intersects(enemyBOSS.getFirst().bossShooter.checkBulletCollsion().get(i).getBounds(30, 30))) {
                    enemyBOSS.getFirst().bossShooter.checkBulletCollsion().remove(i);
                    spaceShip.setHp(spaceShip.getHp() - 30);
                }
            }
        }
        if (!spaceShipLaser.checkBulletCollsion().isEmpty() && !enemyBOSS.isEmpty()) {          //spaseship laser vs boss
            for (int i = 0; i < spaceShipLaser.checkBulletCollsion().size(); i++) {
                if (spaceShipLaser.checkBulletCollsion().get(i).getBounds(40, 40).intersects(enemyBOSS.getFirst().getBounds(200, 200))) {
                    spaceShipLaser.checkBulletCollsion().remove(i);
                    enemyBOSS.getFirst().setHealth(enemyBOSS.getFirst().getHealth() - 3);
                }
            }
        }

        if (!enemyFASTSHIP.isEmpty()) {
            for (int i = 0; i < enemyFASTSHIP.size(); i++) {                             //spaceship vs fast ship bullets
                if (!enemyFASTSHIP.get(i).Shooter.checkBulletCollsion().isEmpty()) {
                    for (int j = 0; j < enemyFASTSHIP.get(i).Shooter.checkBulletCollsion().size(); j++) {
                        if (spaceShip.getBounds(50, 50).intersects(enemyFASTSHIP.get(i).Shooter.checkBulletCollsion().get(j).getBounds(40, 40))) {
                            spaceShip.setHp(spaceShip.getHp() - 10);
                            enemyFASTSHIP.get(i).Shooter.checkBulletCollsion().remove(j);
                        }
                    }
                }
            }
        }


        if (!spaceShipLaser.checkBulletCollsion().isEmpty() && !enemyFASTSHIP.isEmpty()) {                  //space ship laser vs fast ship
            for (int j = 0; j < spaceShipLaser.checkBulletCollsion().size(); j++) {
                for (int i = 0; i < enemyFASTSHIP.size(); i++) {
                    if (spaceShipLaser.checkBulletCollsion().get(j).getBounds(40, 40).intersects(enemyFASTSHIP.get(i).getBounds(90, 90)) && !enemyFASTSHIP.get(i).dead) {
                        spaceShipLaser.checkBulletCollsion().remove(j);
                        enemyFASTSHIP.get(i).hit();
                        spaceShip.setScore(spaceShip.getScore() + 10);
                        break;
                    }
                }
            }
        }

        if (!enemyFASTSHIP.isEmpty()) {
            for (int i = 0; i < enemyFASTSHIP.size(); i++) {                  //spaceship vs Enemy Fast Ship
                if (spaceShip.getBounds(40, 40).intersects(enemyFASTSHIP.get(i).getBounds(90, 90)) && !enemyFASTSHIP.get(i).dead) {
                    enemyFASTSHIP.get(i).dead = true;
                    spaceShip.setHp(spaceShip.getHp() - 40);
                    spaceShip.setScore(spaceShip.getScore() + 10);
                }
            }
        }

        if(!enemyBOSS.isEmpty()){           //space ship vs boss
            if(enemyBOSS.getFirst().getBounds(200,220).intersects(spaceShip.getBounds(50,50))){
                spaceShip.setHp(0);
            }
        }
    }


    public static void main(String args[]) {
        GameWorld game = new GameWorld();
        game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        JFrame frame = new JFrame(game.TITLE);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setFocusable(true);
        game.start();
    }


}
