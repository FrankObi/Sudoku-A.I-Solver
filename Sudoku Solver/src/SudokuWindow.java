/*
 * Class that contains the housing for the Sudoku Puzzle
 * Frank Obioma
 * 9/21/2021
 */


import javax.swing.*;

public class SudokuWindow extends JFrame{
	private SudokuPuzzle puzzle;
	private SudokuDisplay display;
	private int win_height = 600;
	private int win_width = 600;
	
	
	public SudokuWindow(String fileName)
	{
		this.setTitle("My Puzzle");
		this.setSize(getWin_height(), getWin_width());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		puzzle = new SudokuPuzzle(fileName);
		display = new SudokuDisplay(puzzle);
		//Ask about this
		puzzle.setDisplay(display);
		puzzle.setWin(this); 
		
		this.add(display);
		this.setVisible(true);
	}

	public static void main(String[] args)
	{
		SudokuWindow myWin = new SudokuWindow("9X9_expert.txt");
	}
	//ask instructor about this
	public int getWin_height() {
		return win_height;
	}

	public int getWin_width() {
		return win_width;
	}
}
