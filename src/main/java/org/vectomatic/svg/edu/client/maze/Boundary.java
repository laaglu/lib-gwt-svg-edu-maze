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

public class Boundary {
	protected boolean hasWall;
	protected Cell cell1;
	protected Cell cell2;
	public Boundary(Cell cell1, Cell cell2) {
		this.cell1 = cell1;
		this.cell2 = cell2;
		cell1.boundaries.put(cell2, this);
		cell2.boundaries.put(cell1, this);
		hasWall = true;
	}
	public void setHasWall(boolean hasWall) {
		this.hasWall = hasWall;
	}
	public boolean hasWall() {
		return hasWall;
	}
	public Cell getCell1() {
		return cell1;
	}
	public Cell getCell2() {
		return cell2;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("(");
		builder.append(cell1.getId());
		builder.append("-");
		builder.append(cell2.getId());
		builder.append(" : ");
		builder.append(hasWall);
		builder.append(")");
		return builder.toString();
	}
}
