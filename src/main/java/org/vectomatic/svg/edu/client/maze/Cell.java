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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Random;

public abstract class Cell {
	protected Map<Cell, Boundary> boundaries;
	public Cell() {
		boundaries = new HashMap<Cell, Boundary>();
	}
	public abstract String getId();
	
	public Collection<Boundary> getBoundaries() {
		return boundaries.values();
	}
	public Set<Cell> getNeighbors() {
		return boundaries.keySet();
	}
	public Boundary getBoundary(Cell neighbor) {
		return boundaries.get(neighbor);
	}
	public boolean hasWall(Cell neighbor) {
		return boundaries.get(neighbor).hasWall();
	}
	public void setHasWall(Cell neighbor, boolean hasWall) {
		boundaries.get(neighbor).setHasWall(hasWall);
	}
	public boolean hasAllWalls() {
		for (Boundary boundary : boundaries.values()) {
			if (!boundary.hasWall()) {
				return false;
			}
		}
		return true;
	}
	public Cell getNeighborWithAllWalls() {
		int count = 0;
    	for (Cell cell : getNeighbors()) {
    		if (cell.hasAllWalls()) {
    			count++;
    		}
    	}
    	if (count > 0) {
    		count = Random.nextInt(count);
        	for (Cell cell : getNeighbors()) {
        		if (cell.hasAllWalls()) {
        			if (count == 0) {
        				return cell;
        			} else {
                		count--;
        			}
        		}
        	}
    	}
    	return null;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[" + getId());
		if (boundaries.size() > 0) {
			builder.append(" : ");
		}
		for (Cell cell : boundaries.keySet()) {
			builder.append(cell.getId());
			builder.append(" ");
		}
		builder.append("]");
		return builder.toString();
	}
}