import javax.swing.*;
import java.awt.*;

public class CandyRunner {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    
    public static void main(String args[])
    {
        /* Create a JFrame that will be visible on the screen. */
        JFrame frame = new JFrame( "Game" );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //make the red close button work
        frame.setLocation( 0, 0 ); //place the frame in the upper left corner
        Game game = new Game(Driver.WIDTH, Driver.HEIGHT); //create a Game object with width = 1500, height = 1000
        frame.getContentPane().add(game); //add game to the frame so it will be on the screen
        frame.pack();
        frame.setVisible(true);
        game.playGame();
    }
}