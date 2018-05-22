//Casey McNamara
//5/15/2009
//I decided to take advantage of object-orientation

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Jewel implements Comparable
{
	private String color;
	private ImageIcon image;
	private Spot location;
	private boolean marked;
	private Map<Spot, Jewel> world;
	private Jewels universe;
	private String[] colors = {"red", "blue", "green", "purple", "orange", "yellow", "white"};
	public static final int NUM_COLORS = 7;
	
	public Jewel(int c, Spot s, Map<Spot, Jewel> w, Jewels j)
	{
		color = colors[c];
		location = s;
		world = w;
		universe = j;
		image = new ImageIcon(color + ".jpg");
	}
	
	public String getColor()
	{
		return color;
	}
	
	public ImageIcon getIcon()
	{
		return image;
	}
	
	public void mark()
	{
		marked = true;
	}
	
	public boolean isMarked()
	{
		return marked;
	}
	
	public void setColor(String c)
	{
		color = c;
		image = new ImageIcon(color + ".jpg");
	}
	
	public Spot getSpot()
	{
		return location;
	}
	
	public void fall()
	{
		world.remove(location);
		universe.updateButton(location);
		location = location.getDown();
		world.put(location, this);
		universe.updateButton(location);
	}
	
	public void die()
	{
		marked = false;
		world.remove(location);
		universe.updateButton(location);
		for(Spot above: getSpot().getSpotsAbove())
		{
			if(!above.equals(getSpot()))
			{
				world.get(above).fall();
			}	
		}
		Spot s = new Spot(0, location.getCol());
		world.put(s, new Jewel((int)(Math.random() * NUM_COLORS), s, world, universe));
		universe.updateButton(s);
	}
	
	public int compareTo(Object j)
	{
		return -1 *(getSpot().compareTo(((Jewel)j).getSpot()));
	}
	
}
