import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {
            move();
            checkSymbols();
            checkCollisions();
        }
        repaint();
    }

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 5;
    int SymbolsEaten;
    int Symbol1X;
    int Symbol1Y;
    int Symbol2X;
    int Symbol2Y;
    int Symbol3X;
    int Symbol3Y;

    char direction = 'R';
    boolean running = false;
    private static boolean gameOn;
    Timer timer;
    Random random;

    GamePanel() {

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {

        newSymbol();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void pause() {
        GamePanel.gameOn = true;
        timer.stop();
    }

    public void resume() {
        GamePanel.gameOn = false;
        timer.start();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            g.setColor(Color.red); //Verb
            g.fillOval(Symbol1X, Symbol1Y, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.darkGray); //Noun
            g.fillOval(Symbol2X, Symbol2Y, UNIT_SIZE, UNIT_SIZE);

            g.setColor(Color.yellow);   //Adjective
            g.fillOval(Symbol3X, Symbol3Y, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++)
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(33, 95, 19));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
        } else {
            gameOver(g);
        }

        //Display Score
        g.setColor(Color.pink);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + SymbolsEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + SymbolsEaten)) / 2, g.getFont().getSize());
    }

    public void newSymbol() {

        Symbol1X = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        Symbol1Y = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        Symbol2X = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        Symbol2Y = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        Symbol3X = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        Symbol3Y = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkSymbols() {

        if ((x[0] == Symbol1X || x[0] == Symbol2X || x[0] == Symbol3X) && (y[0] == Symbol1Y || y[0] == Symbol2Y || y[0] == Symbol3Y)) {
            bodyParts++;
            SymbolsEaten++;
            newSymbol();
        }
    }

    public void checkCollisions() {

        //check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                System.out.println("Game Over");
            }
        }
        //check if head touches left border
        if (x[0] < 0) {
            running = false;

        }
        //check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;

        }
        //check if head touches top border
        if (y[0] < 0) {
            running = false;

        }
        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;

        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {

        //Display Score
        g.setColor(Color.pink);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + SymbolsEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + SymbolsEaten)) / 2, g.getFont().getSize());

        //GameOver Text
        g.setColor(Color.pink);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (!GamePanel.gameOn) {
                        if (direction != 'R') {
                            direction = 'L';
                        }
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!GamePanel.gameOn) {
                        if (direction != 'L') {
                            direction = 'R';
                        }
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (!GamePanel.gameOn) {
                        if (direction != 'D') {
                            direction = 'U';
                        }
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (!GamePanel.gameOn) {
                        if (direction != 'U') {
                            direction = 'D';
                        }
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (GamePanel.gameOn){
                        resume();
                    }
                    else {
                        pause();
                    }
                    break;
            }
        }
    }


}
