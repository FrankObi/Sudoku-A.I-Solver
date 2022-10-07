/*
 * Class hold information about all variable in the sudoku puzzle
 * Frank Obioma
 * 10/11/2021
 */
import java.util.LinkedList;

public class Variable {
	private int assignment;
	private LinkedList<Integer> domain;
	private String name;
	
	public Variable(int initValue, int startRange, int endRange, String nam)
	{
		name = nam;
		setAssignment(initValue);
		domain = new LinkedList<Integer>();
		
		for(int rang = startRange; rang <= endRange; rang++)
		{
			domain.add(rang);
		}
	}
	
	public void removeFromDomain(int dex)
	{
		domain.remove(dex);
	}
	
	public String toString()
	{
		String stuff = getName() + ": (" + getAssignment() + ") domain: ";
		for(int rang = 0; rang < domain.size(); rang++)
		{
			stuff += domain.get(rang)+ " ";
		}
		stuff += ",";
		return stuff;
	}
	
	public int sizeDomain()
	{
		return domain.size();
	}

	public boolean hasSupport(int val)
	{	
		for(int dex = 0; dex < this.sizeDomain(); dex++)
		{
			if(this.getDomainElement(dex) != val)
				return true;
		}
		
		return false;
	}
	
	public void setAssignment(int assignment) {
		this.assignment = assignment;
	}
	
	public int getDomainElement(int dex)
	{
		return domain.get(dex);
	}

	public int getAssignment() {
		return assignment;
	}

	public String getName() {
		return name;
	}
}
