/*
 * Class handles binary constraints for sudoku
 * Frank Obioma
 * 10/11/2021
 */
import java.util.LinkedList;

public class Constraint {
	private String name;
	private String type;
	private LinkedList<Variable> vars;
	
	public Constraint(String nam, String typ)
	{
		name = nam;
		type = typ;
		vars = new LinkedList<>();
	}
	public String toString()
	{
		return name;
	}
	
	public void revise_allDiff()
	{
		Variable var = vars.get(0);
		Variable var2 = vars.get(1);
		
		
		for(int dex = 0; dex < var.sizeDomain(); dex++)
		{
			if(!var2.hasSupport(var.getDomainElement(dex)))
			{
				var.removeFromDomain(dex);
			}
		}
		
		/*if(var.hasSupport(var2))
		{
			System.out.print(var + " " + var2 + " | support\n");
		}else
		{
			System.out.print(var + " " + var2+"\n");
		}*/
	}
	
	public void addVariable(Variable var)
	{
		getVars().add(var);
	}
	
	public Variable getVariable(int dex)
	{
		return getVars().get(dex);
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public LinkedList<Variable> getVars() {
		return vars;
	}
}