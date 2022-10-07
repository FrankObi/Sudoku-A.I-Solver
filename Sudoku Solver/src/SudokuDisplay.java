/*
 * Class contains graphic implementations
 * Frank Obioma
 * 9/21/2021
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class SudokuDisplay extends JPanel{
	private SudokuPuzzle puzzle;
	private int cellSize = 50;
	private int start_y = 50;
	private int start_x = 50;
	private int offset_x = 15;
	private int offset_y = 35;
	private Font numberFont = new Font("Arial", Font.PLAIN, 30);
	private Color digitalColor = Color.blue;
	
	public SudokuDisplay(SudokuPuzzle puz)
	{
		puzzle = puz;
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me)
			{
				puzzle.backTrackSearch();
				repaint();
			}
		});
	}
	
	public void nextMove()
	{
		puzzle.arcConsistenct_1Round();
		puzzle.checkForSingleton();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.setFont(numberFont);
		for(int row = 0; row < puzzle.getSize(); row++)
		{
			for(int col = 0; col < puzzle.getSize(); col++)
			{
				if(puzzle.getGridPosition(row, col).getAssignment() > 0)
				{
					if(puzzle.getGridPosition(row, col).sizeDomain() == 1)
					{
						g.setColor(Color.LIGHT_GRAY);
						g.fillRect(start_x + col * cellSize, 
								start_y + row * cellSize, cellSize, cellSize);
						
						g.setColor(Color.BLACK);
						g.drawString(""+puzzle.getGridPosition(row, col).getAssignment(), 
								start_x + col * cellSize + offset_x,
								start_y + row * cellSize + offset_y);
					}else
					{
						g.setColor(digitalColor);
						g.drawString(""+puzzle.getGridPosition(row, col).getAssignment(), 
								start_x + col * cellSize + offset_x,
								start_y + row * cellSize + offset_y);
					}
					
					g.setColor(Color.black);
				}
				
				g.drawRect(start_x + col * cellSize, 
						start_y + row * cellSize, 
						cellSize, cellSize);
			}	
		}
		
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(6));
		
		int block = puzzle.getGroupSize();
		for(int row = 0; row < puzzle.getSize(); row+=block)
		{
			for(int col = 0; col < puzzle.getSize(); col+=block)
			{
				g.drawRect(start_y + row * cellSize,
						start_x + col * cellSize,
						cellSize * block, cellSize * block);
			}
		}
		
		numberFont = new Font("Arial", Font.BOLD, 13);
		g.setFont(numberFont);
		numberFont = new Font("Arial", Font.PLAIN, 30);
		
		g.drawString("Seach Space: "+puzzle.sizeOfSearchSpace(), start_x, 
				start_y + (cellSize  * puzzle.getSize()) + cellSize);
	}
}
