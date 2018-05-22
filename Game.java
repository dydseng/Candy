import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class Game implements ActionListener {
    private JFrame frame;
    private JPanel candies;
    private int width;
    private int height;
    
    /* what is a priority queue? */
    private ArrayList<Candy> matched;
    private Map<Location, Candy> candyScreen;
    private Map<Location, JButton> buttonScreen;
    private static final int BUTT_SIZE = 52;
    
    private Location selected;
    private Set<Location> crushed;
    
    public Game() {
        width = 10;
        height = 10;
        buttonScreen = new TreeMap<Location, JButton>();
        candyScreen = new TreeMap<Location, Candy>();
        selected = null;
        /* put in extra graphical stuff later. */
    }
    
    public void moveMade(ActionEvent e) {
        if(!matched.isEmpty()) {
            crushCandy();
        }
        else {
            Scanner in = new Scanner(e.getActionCommand());
            int row = in.nextInt();
            int col = in.nextInt();
            
            if(selected == null) {
                selected = new Location(row, col);
            }
            else {
                Location other = new Location(row, col);
                if(other.isAdjacentTo(selected)) {
                    /* WRITE THIS METHOD
                     * 
                     * 
                     * 
                     */
                    change(other, selected);
                    selected = null;
                }
                else {
                    selected = other;
                }
            }
        }
    }
    
    public void addToMatched (Candy candy) {
        if(candy != null)
            matched.add(candy);
    }
    
    public void removeFromMatched() {
        for(int i = matched.size() - 1; i >= 0; i--)
            matched.remove(i);
        }
      
    public void initialize() {
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                Location loc = new Location(row, col);
                int color = (int)(Math.random() * Candy.NUM_COLORS);
                Candy candy = new Candy(color, loc, candyScreen, this);
                candyScreen.remove(loc);
                candyScreen.put(loc, candy);
                updateButton(loc);
            }
        }
        searchMatch();
    }
    
    public void makeCandies() {
        for (int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                Location loc = new Location(row, col);
                int color = (int)(Math.random() * Candy.NUM_COLORS);
                Candy candy = new Candy(color, loc, candyScreen, this);
                candyScreen.put(loc, candy);
                JButton button = new JButton();
                button.setIcon(candy.getImage());
                button.setBackground(Color.WHITE);
                buttonScreen.put(loc, button);
                /* not done here */
            }
        }
    }
    public void searchMatch() {
        
    }
    
    /* do we need append() or addToSet()?? */
    
    
    
    public void crushCandy() {
        
    }
}
