//Casey McNamara
//5/15/2009
//A really little class to hold a row and column

import java.util.*;

public class Spot implements Comparable
{
	private int row;
	private int column;
	
	public Spot(int r, int c)
	{
		row = r;
		column = c;
	}
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return column;
	}
	
	public ArrayList<Spot> getSpotsAbove()
	{
		ArrayList<Spot> answer = new ArrayList<Spot>();
		for(int r = row; r >= 0; r --)
		{
			answer.add(new Spot(r, column));
		}
		return answer;
	}
	
	public Spot getRight()
	{
		return new Spot(row, column + 1);
	}
	
	public Spot getLeft()
	{
		return new Spot(row, column - 1);
	}
	
	public Spot getUp()
	{
		return new Spot(row - 1, column);
	}
	
	public Spot getDown()
	{
		return new Spot(row + 1, column);
	}
	
	public boolean isAdjacentTo(Spot other)
	{
		return (getRight().equals(other) || getLeft().equals(other) || getUp().equals(other) || getDown().equals(other));
	}
	
	public Spot transform(int[] transform)
	{
		return new Spot(row + transform[0], column + transform[1]);
	}
	
	public int[] getTransformTo(Spot other)
	{
		int[] stuff = {other.getRow() - getRow(), other.getCol() - getCol()};
		return stuff;
	}
	
	public static int[] getInverse(int[] transform)
	{
		int[] stuff = {-1 * transform[0], -1 * transform[1]};
		return stuff;
	}
	
	public ArrayList<Spot> getAdjacentSpots()
	{
		ArrayList<Spot> answer = new ArrayList<Spot>();
		answer.add(getRight());
		answer.add(getLeft());
		answer.add(getUp());
		answer.add(getDown());
		return answer;
	}
	
	public boolean inBounds(int height, int width)
	{
		return (row >= 0 && row < height && column >= 0 && column < width);
	}
	
	public int compareTo(Object other)
	{
		Spot test = (Spot)other;
		if(test.row == row)
			return -(column - test.column);
		return row - test.row;
	}
	
	public boolean equals(Object other)
	{
		Spot test = (Spot)other;
		return (row == test.row && column == test.column);
	}
	
	public String toString()
	{
		return row + " " + column;
	}
}
