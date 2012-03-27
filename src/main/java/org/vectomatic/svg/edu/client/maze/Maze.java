/**********************************************
 * Copyright (C) 2010 Lukas Laag
 * This file is part of lib-gwt-svg-edu.
 * 
 * libgwtsvg-edu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * libgwtsvg-edu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with libgwtsvg-edu.  If not, see http://www.gnu.org/licenses/
 **********************************************/
package org.vectomatic.svg.edu.client.maze;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import org.vectomatic.svg.edu.client.maze.RectangularMaze.RectangularCell;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Random;

public class Maze {
	private Cell cells[];
	public Maze(Cell cells[]) {
		this.cells = cells;
	}
	public Cell[] getCells() {
		return cells;
	}
	public void perfectRandomize() {
		long t1 = System.currentTimeMillis();
		// Restore all the walls
		for (Cell cell : cells) {
			for (Cell neighbor : cell.getNeighbors()) {
				cell.setHasWall(neighbor, true);
				((RectangularCell)cell).setOnPath(false);
			}
		}

    	// Generate a maze by DFS
		int visitedCount = 1;
        Cell currentCell = cells[Random.nextInt(cells.length)];
        Stack<Cell> stack = new Stack<Cell>();
        while(visitedCount < cells.length) {
        	Cell cell = currentCell.getNeighborWithAllWalls();
        	if (cell != null) {
    			cell.setHasWall(currentCell, false);
                stack.push(currentCell);
                currentCell = cell;
                visitedCount++;
        	} else {
        		currentCell = stack.pop();
        	}
        }
		long t2 = System.currentTimeMillis();
		GWT.log("randomize = " + (t2 - t1));
	}
	public Stack<Cell> resolve(Cell start, Cell end) {
		Set<Cell> visited = new HashSet<Cell>();
        Stack<Cell> stack = new Stack<Cell>();
        Cell currentCell = start;
        stack.push(start);
        visited.add(start);
        while(currentCell != end) {
        	currentCell = stack.peek();
        	boolean pathExists = false;
        	for (Cell cell : currentCell.getNeighbors()) {
        		if ((!visited.contains(cell)) && (!cell.hasWall(currentCell))) {
        			stack.push(cell);
        			visited.add(cell);
        			pathExists = true;
        			break;
        		}
        	}
        	if (!pathExists) {
        		currentCell = stack.pop();
        	}
        }
        stack.pop();
        return stack;
	}
}
