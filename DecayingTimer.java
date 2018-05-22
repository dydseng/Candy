//Casey McNamara
//4/27/2009; modified 5/14/2009 for Jewels
//Extends JPanel to better paint images in arbitrary places
//This specific implementation has a background image that occupies a specified percentage
//of the width of the panel

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class DecayingTimer extends JPanel implements ActionListener{

  	private Image background;					//The background image
  	private double percentExpired;
  	private double width, height;
  	private javax.swing.Timer decay;
  	private Jewels universe;
  	
	//Constructs a new ImagePanel with the background image specified by the file path given
  	public DecayingTimer(String img, Jewels j) 
  	{
  		this(new ImageIcon(img).getImage(), j);	
  			//The easiest way to make images from file paths in Swing
  	}

	//Constructs a new ImagePanel with the background image given
  	public DecayingTimer(Image img, Jewels j)
  	{
    	background = img;
    	universe = j;
    	width = img.getWidth(null);
    	height = img.getHeight(null);
    	Dimension size = new Dimension((int)width, (int)height);	
    		//Get the size of the image
    	//Thoroughly make the size of the panel equal to the size of the image
    	//(Various layout managers will try to mess with the size of things to fit everything)
    	setPreferredSize(size);
    	setMinimumSize(size);
    	setMaximumSize(size);
    	setSize(size);
    	
    	decay = new javax.swing.Timer(1000, this);
  	}
  	
  	public void startCounting()
  	{
  		decay.start();
  	}
	
	public void pause()
	{
		decay.stop();
	}
	
	public void reset()
	{
		decay.restart();
		percentExpired = 0;
	}
	
	public void resume()
	{
		decay.restart();
	}
	
	public void setSize(Dimension size)
	{
		width = size.getWidth();
		height = size.getHeight();
		setPreferredSize(size);
    	setMinimumSize(size);
    	setMaximumSize(size);
    	super.setSize(size);
	}

	//This is called whenever the computer decides to repaint the window
	//It's a method in JPanel that I've overwritten to paint the background
  	public void paintComponent(Graphics g) 
  	{
  		//Paint the background with its upper left corner at the upper left corner of the panel
    	g.drawImage(background, 0, 0, null);
    	int startPosition = (int)(width - percentExpired * width);
    	g.clearRect(startPosition, 0, (int)(percentExpired * width), (int)(height));
    		//x, y, width, height
  	}
  	
  	public void addTime(double addedPercent)
  	{
  		double time = percentExpired - addedPercent;
  		if(time < 0)
  			time = 0;
  		setPercentExpired(time);
  	}
  	
  	//Revises the percent of the time that has expired
  	public void setPercentExpired(double newPercent)
  	{
  		percentExpired = newPercent;
  		if(percentExpired > 1)
  		{
  			universe.die();
  			reset();
  		}
  		repaint();	//This repaints stuff... you don't need to know how it works
  	}
  	
  	public void actionPerformed(ActionEvent e)
  	{
  		setPercentExpired(percentExpired + .05);
  	}
}
