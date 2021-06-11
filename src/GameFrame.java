import javax.swing.JFrame;

public class GameFrame extends JFrame {

    GameFrame(){

        //GamePanel panel = new GamePanel();
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        //pack() method sizes the frame so that all its contents are at or above their preferred size
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}
