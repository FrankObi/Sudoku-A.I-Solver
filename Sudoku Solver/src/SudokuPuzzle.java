/*
 * Class that contains the logic for Sudoku
 * Frank Obioma
 * 9/21/2021
 */

import java.io.*;
import java.math.*;
import java.util.*;
import javax.swing.*;

public class SudokuPuzzle {
	private int size;
	private int groupSize;
	private Variable[][] grid;
	private SudokuDisplay display;
	private SudokuWindow win;
	private HashMap<String, Constraint> constraints;
	private Variable[] assignment;
	private int[] domainElement;
	
	public SudokuPuzzle(String fName)
	{
		loadPuzzleFromFile(fName);
		constraints = new HashMap<>();
		initializeConstraints();
		assignment = new Variable[getSize()*getSize()];
		domainElement = new int[getSize()*getSize()];
	}
	
	public void initializeConstraints()
	{
		int startRow;
		int startCol;
		Variable curVariable = null;
		for(int row = 0; row < getSize(); row++)
		{
			for(int col = 0; col < getSize(); col++)
			{
				curVariable = getGridPosition(row, col);
				
				for(int rowCon = 0; rowCon < getSize(); rowCon++)
				{
					Variable newVar = getGrid()[rowCon][col];
					if(newVar == curVariable) continue;
					Constraint con = new Constraint(curVariable.getName(), null);
					con.addVariable(curVariable);
					con.addVariable(newVar);
					constraints.put(curVariable.getName() + ":" + newVar.getName(), con);
				}
				
				for(int colCon = 0; colCon < getSize(); colCon++)
				{
					Variable newVar = getGrid()[row][colCon];
					if(newVar == curVariable) continue;			
					Constraint con = new Constraint(curVariable.getName(), null);
					con.addVariable(curVariable);
					con.addVariable(newVar);
					constraints.put(curVariable.getName() + ":" + newVar.getName(), con);
				}
				
				startRow = row / getGroupSize() * getGroupSize();
				startCol = col / getGroupSize() * getGroupSize();
				
				for(int groupRow = startRow; groupRow < startRow + getGroupSize(); groupRow++)
				{
					for(int groupCol = startCol; groupCol < startCol + getGroupSize(); groupCol++)
					{
						Variable newVar = getGrid()[groupRow][groupCol];
						if(newVar == curVariable) continue;
						Constraint con = new Constraint(curVariable.getName(), null);
						con.addVariable(curVariable);
						con.addVariable(newVar);
						constraints.put(curVariable.getName() + ":" + newVar.getName(), con);
					}
				}
			}
		}
	}

	public void loadPuzzleFromFile(String fName)
	{
		File inFile = new File(fName);
		
		try{
			Scanner inScan = new Scanner(inFile);
			
			size = inScan.nextInt();
			groupSize = inScan.nextInt();
			grid = new Variable[getSize()][getSize()];
			
			for(int row = 0; row < getSize(); row++)
			{
				for(int col = 0; col < getSize(); col++)
				{
					int newVal = inScan.nextInt();
					Variable newVar;
					String nam = "" + row + "," + col;
					if(newVal == 0)
					{
						newVar = new Variable(-1,1,size, nam);
					}else
					{
						newVar = new Variable(newVal, newVal, newVal, nam);
					}
					grid[row][col] = newVar;
				}
			}
			inScan.close();
			
		}catch(IOException ioe)
		{
			String errMess = "An IOException was thrown";
			JOptionPane.showMessageDialog(null, errMess , "Warning", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Variable getGridPosition(int row, int col)
	{
		return getGrid()[row][col];
	}

	public void checkForSingleton()
	{
		Variable curVariable = null;
		for(int row = 0;row < getSize(); row++)
		{
			for(int col = 0;col < getSize(); col++)
			{
				curVariable = getGridPosition(row, col);
				for(int dex = 0; dex < getSize(); dex++)
				{
					if(curVariable.sizeDomain() == 1 && curVariable.getAssignment() < 0)
						curVariable.setAssignment(curVariable.getDomainElement(0));
				}
			}
		}
	}
	
	public void arcConsistenct_1Round()
	{
		int startRow;
		int startCol;
		Variable curVariable = null;
		for(int row = 0; row < getSize(); row++)
		{
			for(int col = 0; col < getSize(); col++)
			{
				curVariable = getGridPosition(row, col);
				
				for(int colCon = 0; colCon < getSize(); colCon++)
				{
					if(curVariable.sizeDomain() == 1)break;
					Variable newVar = getGrid()[row][colCon];
					if(newVar == curVariable) continue;
					Constraint con = constraints.get(curVariable.getName() + ":" + newVar.getName());
					con.revise_allDiff();
					System.out.print(curVariable + ", " + newVar + "\n");
				}
				
				for(int rowCon = 0; rowCon < getSize(); rowCon++)
				{
					if(curVariable.sizeDomain() == 1)break;
					Variable newVar = getGrid()[rowCon][col];
					if(newVar == curVariable) continue;
					Constraint con = constraints.get(curVariable.getName() + ":" + newVar.getName());
					con.revise_allDiff();
					System.out.print(curVariable + ", " + newVar + "\n");
				}
				
				startRow = row / getGroupSize() * getGroupSize();
				startCol = col / getGroupSize() * getGroupSize();
				
				for(int groupRow = startRow; groupRow < startRow + getGroupSize(); groupRow++)
				{
					if(curVariable.sizeDomain() == 1)break;
					for(int groupCol = startCol; groupCol < startCol + getGroupSize(); groupCol++)
					{
						Variable newVar = getGrid()[groupRow][groupCol];
						if(newVar == curVariable) continue;
						Constraint con = constraints.get(curVariable.getName() + ":" + newVar.getName());
						con.revise_allDiff();
						System.out.print(curVariable + ", " + newVar + "\n");
					}
				}
				
				
			}
		}
	}
	
	public void backTrackSearch()
	{
		int numVars = 0;
		int min = 0;
		int iter = 0;
		
		//add single domain Variables
		for(int row = 0; row < getSize(); row++)
		{
			for(int col = 0; col < getSize(); col++)
			{
				Variable tempVar = getGrid()[row][col];
				if(tempVar.sizeDomain() == 1)
				{
					assignment[numVars] = tempVar;
					//don't need this
					domainElement[numVars] = tempVar.getDomainElement(0);
					numVars++;
				}
			}
		}

		//add multi-domain Variables
		for(int row = 0; row < getSize(); row++)
		{
			for(int col = 0; col < getSize(); col++)
			{
				Variable tempVar = getGrid()[row][col];
				if(tempVar.sizeDomain() > 1)
				{
					assignment[numVars] = tempVar;
					domainElement[numVars] = tempVar.getDomainElement(0);
					numVars++;
				}
			}
		}
		
		//get first unassign variable
		for(int dex = 0; dex < numVars; dex++)
		{
			if(assignment[dex].getAssignment() == -1)
			{
				min = dex;
				break;
			}
		}
		
		iter = min;
		
		while(iter >= min && iter < numVars)
		{
			Variable curVariable = assignment[iter];
			
			if(findSuccessfulAssignment(curVariable, iter))
			{
				iter++;
			}else
			{
				domainElement[iter] = curVariable.getDomainElement(0);
				iter--;
				assignment[iter].setAssignment(-1);
				domainElement[iter]++;
			}
			
			getDisplay().paintImmediately(0,0, getWin().getWin_width(),getWin().getWin_height() );
		}
	}

	private boolean isConsistantAssignment(Variable var, int index)
	{	
		int startRow;
		int startCol;
		Variable curVariable = null;
		for(int row = 0; row < getSize(); row++)
		{
			for(int col = 0; col < getSize(); col++)
			{
				curVariable = getGrid()[row][col];
				if(curVariable != var) continue;
				
				for(int colCon = 0; colCon < getSize(); colCon++)
				{
					Variable newVar = getGrid()[row][colCon];
					if(var == newVar || newVar.getAssignment() == -1) continue;
					if(domainElement[index] == newVar.getAssignment()) 
						return false;
					
				}
				
				for(int rowCon = 0; rowCon < getSize(); rowCon++)
				{
					Variable newVar = getGrid()[rowCon][col];
					if(var == newVar || newVar.getAssignment() == -1) continue;
					if(domainElement[index] == newVar.getAssignment()) 
						return false;
				}
				
				startRow = row / getGroupSize() * getGroupSize();
				startCol = col / getGroupSize() * getGroupSize();
				
				for(int groupRow = startRow; groupRow < startRow + getGroupSize(); groupRow++)
				{
					for(int groupCol = startCol; groupCol < startCol + getGroupSize(); groupCol++)
					{
						Variable newVar = getGrid()[groupRow][groupCol];
						if(var == newVar || newVar.getAssignment() == -1) continue;
						if(domainElement[index] == newVar.getAssignment()) 
							return false;
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	private boolean findSuccessfulAssignment(Variable var, int index)
	{
		while(domainElement[index] <= var.sizeDomain())
		{
			if(isConsistantAssignment(var, index))
			{
				var.setAssignment(domainElement[index]);
				return true;
			}
			domainElement[index]++;
		}
		
		return false;
	}
	
	public int getSize() {
		return size;
	}

	public int getGroupSize() {
		return groupSize;
	}

	public Variable[][] getGrid() {
		return grid;
	}

	public SudokuDisplay getDisplay() {
		return display;
	}

	public void setDisplay(SudokuDisplay display) {
		this.display = display;
	}

	public BigInteger sizeOfSearchSpace()
	{
		BigInteger searchSpace = new BigInteger("1");
		for(int row = 0; row < getSize(); row++)
		{
			for(int col = 0; col < getSize(); col++)
			{
				searchSpace = searchSpace.multiply(BigInteger.valueOf(getGridPosition(row,col).sizeDomain()));
			}
		}
		
		return searchSpace;
	}

	public SudokuWindow getWin() {
		return win;
	}

	public void setWin(SudokuWindow win) {
		this.win = win;
	}
}
