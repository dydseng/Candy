public class Candy{
    private String color;
    private ImageIcon image;
    private Location loc;
    private boolean chosen;
    private Map<Location,Candy>candyScreen;
    private Game game;
    private String[] colors = {"red", "blue", "green", "orange", "yellow"};
    public static final COLOR_NUM = 5;
    
    
    public Candy(String color, Location loc, Map<Location,Candy>candyScreen , Game game){
        this.color = color;
        this.loc = loc;
        this.candyScreen = candyScreen;
        this.game = game;
        image = new ImageIcon(color + ".jpg");
        chosen = false;
    }
    
    public String getColor(){
        return color; 
    }
    
    public ImageIcon getImage(){
        return image;
    }
    
    public void setImage(ImageIcon image){
        this.image = image;
    }
    
    public void choose(){
        chosen = true;
    }
    
    public Location getLoc(){
        return loc;
    }
    
    public void moveDown(){
        game.remove(loc);
        candyScreen.updateButton(loc);
        loc = loc.getDown();
        game.put(loc, this);
        candyScreen.updateButton(loc);
    }
  
    public void crushed(){
        chosen = false;
        candyScreen.remove(loc);
        game.updateButton(loc);
        for(Location above: getLoc().getAllAbove()){
            if(!above.equals(getLoc())){
                game.get(above).moveDown(); 
            }
        }
        Location l = new Location(0, loc.getCol());
        game.put(l, new Candy((int)(Math.random()*COLOR_NUM), l, game, candyScreen));
        candyScreen.updateButton(l);
    }
    
    public int compareTo(Candy c){
        return -1;
    }
}