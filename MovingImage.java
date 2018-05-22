//Casey McNamara
//4/27/2009
//A little class to store an image and where it is

import java.awt.*;
import javax.swing.*;

public class MovingImage
{
	private Image image;		//The picture
	private double x;			//X position
	private double y;			//Y position
	private double vx;			//X velocity (pixels/step)
	private double vy;			//Y velocity (pixels/step)
	
	//Construct a new Moving Image with image, x position, and y position given
	public MovingImage(Image img, double xPos, double yPos)
	{
		image = img;
		x = xPos;
		y = yPos;
		vx = vy = 0;
	}
	
	//Construct a new Moving Image with image (from file path), x position, and y position given
	public MovingImage(String path, double xPos, double yPos)
	{
		this(new ImageIcon(path).getImage(), xPos, yPos);	
			//easiest way to make an image from a file path in Swing
	}
	
	//They are set methods.  I don't feel like commenting them.
	public void setPosition(double xPos, double yPos)
	{
		x = xPos;
		y = yPos;
	}
	
	public void setImage(Image img)
	{
		image = img;
	}
	
	public void setVelocity(double newVx, double newVy)
	{
		vx = newVx;
		vy = newVy;
	}
	
	//Move one step by adding velocity to position
	public void incrementPosition()
	{
		x += vx;
		y += vy;
	}
	
	//Reflect off the side (if true) or the top or bottom (if false)
	//Makes the appropriate velocity component point in the opposite direction 
	//(conservation of energy!  yay)
	public void reflect(boolean side)
	{
		if(side)
			vx = -1 * vx;
		else
			vy = -1 * vy;
	}
	
	//Get methods which I'm also not commenting
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public Image getImage()
	{
		return image;
	}
}