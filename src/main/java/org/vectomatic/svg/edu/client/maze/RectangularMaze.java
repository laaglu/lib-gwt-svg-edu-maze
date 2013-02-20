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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.vectomatic.dom.svg.OMSVGCircleElement;
import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.dom.svg.OMSVGStyle;
import org.vectomatic.svg.edu.client.maze.Rasterizer.RasterizationResult;

import com.google.gwt.core.client.GWT;

public class RectangularMaze extends Maze {
	// Namespace for the maze definition attributes
	static final String VECTOMATIC_NS = "http://www.vectomatic.org";
	// Rasterization resolution at level
	static final String RES_TAG = "res";
	// Coordinates of the start point
	static final String START_TAG = "start";
	// Coordinates of the end point
	static final String END_TAG = "end";
	// Stroke color for the maze walls
	static final String WALL_TAG = "wall";
	// Stroke color for the maze borders
	static final String BORDER_TAG = "border";
	private int colCount;
	private int rowCount;
	private OMSVGRect bbox;
	private OMSVGPathElement wallPath;
	private RectangularCell[][] grid;
	private int currentX, currentY;
	private int srcX, srcY;
	private int destX, destY;
	private static MazeCss style = MazeBundle.INSTANCE.getCss();
	private Stack<Cell> solution;
	private OMSVGCircleElement srcCircle;
	OMSVGPathElement destPath;

	static class RectangularCell extends Cell {
		private int x;
		private int y;
		private boolean onPath;
		private OMSVGRectElement rect;
		public RectangularCell(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public void setRect(OMSVGRectElement rect) {
			this.rect = rect;
		}
		@Override
		public String getId() {
			return "R" + x + "C" + y;
		}
		public String getClassName() {
			return rect.getClassName().getBaseVal();
		}
		public void setClassName(String className) {
			rect.setClassNameBaseVal(className);
		}
		public void setOnPath(boolean onPath) {
			this.onPath = onPath;
		}
		public boolean isOnPath() {
			return onPath;
		}
		public OMSVGStyle getStyle() {
			return rect.getStyle();
		}
	}
	
	private RectangularMaze(
			Cell[] cells, 
			RasterizationResult result,
			int colCount, 
			int rowCount, 
			OMSVGRect bbox, 
			OMSVGPathElement wallPath) {
		super(cells);
		this.grid = result.grid;
		this.colCount = colCount;
		this.rowCount = rowCount;
		this.bbox = bbox;
		this.wallPath = wallPath;
		this.currentX = this.srcX = result.srcX;
		this.currentY = this.srcY = result.srcY;
		this.destX = result.destX;
		this.destY = result.destY;
	}
	public int getColCount() {
		return colCount;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void left() {
		move(currentX - 1, currentY);
	}
	public boolean canGoLeft() {
		int x = currentX - 1;
		return x >= 0 && grid[x][currentY] != null && !grid[x][currentY].getBoundary(grid[currentX][currentY]).hasWall();
	}
	public void right() {
		move(currentX + 1, currentY);
	}
	public boolean canGoRight() {
		int x = currentX + 1;
		return x < colCount && grid[x][currentY] != null && !grid[x][currentY].getBoundary(grid[currentX][currentY]).hasWall();
	}
	public void up() {
		move(currentX, currentY - 1);
	}
	public boolean canGoUp() {
		int y = currentY - 1;
		return y >= 0 && grid[currentX][y] != null && !grid[currentX][y].getBoundary(grid[currentX][currentY]).hasWall();
	}
	public void down() {
		move(currentX, currentY + 1);
	}
	public boolean canGoDown() {
		int y = currentY + 1;
		return y < rowCount && grid[currentX][y] != null && !grid[currentX][y].getBoundary(grid[currentX][currentY]).hasWall();
	}
	public void back() {
		for (Cell cell : grid[currentX][currentY].getNeighbors()) {
			if (!cell.hasWall(grid[currentX][currentY])) {
				String cellStyle = ((RectangularCell)cell).getClassName();
				if (style.path().equals(cellStyle) || style.src().equals(cellStyle)) {
					if (currentX > 0 && grid[currentX - 1][currentY] == cell) {
						left();
					} else if (currentX < colCount - 1 && grid[currentX + 1][currentY] == cell) {
						right();
					} else if (currentY > 0 && grid[currentX][currentY - 1] == cell) {
						up();
					} else if (currentY < rowCount - 1 && grid[currentX][currentY + 1] == cell) {
						down();
					}
					break;
				}
			}
		}
	}
	public boolean canGoBack() {
		for (Cell cell : grid[currentX][currentY].getNeighbors()) {
			if (!cell.hasWall(grid[currentX][currentY])) {
				String cellStyle = ((RectangularCell)cell).getClassName();
				if (style.path().equals(cellStyle) || style.src().equals(cellStyle)) {
					if ((currentX > 0 && grid[currentX - 1][currentY] == cell)
					|| (currentX < colCount - 1 && grid[currentX + 1][currentY] == cell)
					|| (currentY > 0 && grid[currentX][currentY - 1] == cell)
					|| (currentY < rowCount - 1 && grid[currentX][currentY + 1] == cell)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public void move(int x, int y) {
		// Cancel illegal moves
		if (x < 0 || x >= colCount
		 || y < 0 || y >= rowCount
		 || grid[x][y] == null
		 || grid[x][y].getBoundary(grid[currentX][currentY]).hasWall()) {
			return;
		}
		
		int prevX = currentX;
		int prevY = currentY;
		currentX = x;
		currentY = y;
		grid[prevX][prevY].setOnPath(!grid[x][y].isOnPath());
		update(prevX, prevY);
	}
	
	public void update(int x, int y) {
		if (srcX == x && srcY == y) {
			grid[x][y].setClassName(style.src());
		} else if (destX == x && destY == y) {
			grid[x][y].setClassName(style.dest());
		} else if (grid[x][y].isOnPath()) {
			grid[x][y].setClassName(style.path());
		} else {
			grid[x][y].setClassName(style.blank());
		}
	}
	
	public void updateCurrent() {
		if (grid != null && grid[currentX] != null && grid[currentX][currentY] != null) {
			if (style.current().equals(grid[currentX][currentY].getClassName())) {
				update(currentX, currentY);
			} else {
				grid[currentX][currentY].setClassName(style.current());
			}
		}
	}

	public boolean gameWon() {
		return currentX == destX && currentY == destY;
	}
	public Stack<Cell> getSolution() {
		return solution;
	}

	public void displaySolution(boolean show) {
		if (show) {
			for (Cell cell : solution) {
				if (style.blank().equals(((RectangularCell)cell).getClassName())) {
					((RectangularCell)cell).setClassName(style.solution());
				}
			}
		} else {
			for (Cell cell : solution) {
				if (style.solution().equals(((RectangularCell)cell).getClassName())) {
					((RectangularCell)cell).setClassName(style.blank());
				}
			}
		}
	}
	
	public static RectangularMaze createMaze(int colCount, int rowCount, OMSVGDocument document, OMSVGPathElement mazeDef, OMSVGGElement cellGroup, OMSVGPathElement borderPath, OMSVGPathElement wallPath) {
		
		// Cells
		RasterizationResult result = Rasterizer.rasterize(mazeDef, cellGroup, colCount, rowCount);
		long t1 = System.currentTimeMillis();
		RectangularCell[][] grid = result.grid;
		List<RectangularCell> list = new ArrayList<RectangularCell>();
		for (int i = 0; i < colCount; i++) {
			for (int j = 0; j < rowCount; j++) {
				if (grid[i][j] != null) {
					list.add(grid[i][j]);
				}
			}
		}
		RectangularCell[] cells = list.toArray(new RectangularCell[list.size()]);
		
		// Borders
		long t2 = System.currentTimeMillis();
		OMSVGRect bbox = mazeDef.getBBox();
		float x = bbox.getX();
		float y = bbox.getY();
		float width = bbox.getWidth();
		float height = bbox.getHeight();
		float cellWidth = width / colCount;
		float cellHeight = height / rowCount;
		OMSVGPathSegList borderSegs = borderPath.getPathSegList();
		for (int i = 0; i < colCount; i++) {
			for (int j = 0; j < rowCount; j++) {
				if ((i == 0 && grid[i][j] != null) || (i > 0 && grid[i - 1][j] == null && grid[i][j] != null)) {
					borderSegs.appendItem(borderPath.createSVGPathSegMovetoAbs(x + i * cellWidth, y + j * cellHeight));
					borderSegs.appendItem(borderPath.createSVGPathSegLinetoVerticalRel(cellHeight));
				}
				if ((i == colCount - 1 && grid[i][j] != null) || (i < colCount -1 && grid[i][j] != null && grid[i + 1][j] == null)) {
					borderSegs.appendItem(borderPath.createSVGPathSegMovetoAbs(x + (i + 1) * cellWidth, y + j * cellHeight));
					borderSegs.appendItem(borderPath.createSVGPathSegLinetoVerticalRel(cellHeight));
				}
				if ((j == 0 && grid[i][j] != null) || (j > 0 && grid[i][j - 1] == null && grid[i][j] != null)) {
					borderSegs.appendItem(borderPath.createSVGPathSegMovetoAbs(x + i * cellWidth, y + j * cellHeight));
					borderSegs.appendItem(borderPath.createSVGPathSegLinetoHorizontalRel(cellWidth));
				}
				if ((j == rowCount - 1 && grid[i][j] != null) || (j < rowCount -1 && grid[i][j] != null && grid[i][j + 1] == null)) {
					borderSegs.appendItem(borderPath.createSVGPathSegMovetoAbs(x + i * cellWidth, y + (j + 1) * cellHeight));
					borderSegs.appendItem(borderPath.createSVGPathSegLinetoHorizontalRel(cellWidth));
				}
			}
		}
		
		// Boundaries: connect adjacent cell grids
		long t3 = System.currentTimeMillis();
		for (int i = 0; i < colCount - 1; i++) {
			for (int j = 0; j < rowCount; j++) {
				if ((grid[i][j] != null) && (grid[i + 1][j] != null)) {
					new Boundary(grid[i][j], grid[i + 1][j]);
				}
			}
		}
		for (int i = 0; i < colCount; i++) {
			for (int j = 0; j < rowCount - 1; j++) {
				if ((grid[i][j] != null) && (grid[i][j + 1] != null)) {
					new Boundary(grid[i][j], grid[i][j + 1]);
				}
			}
		}
		long t4 = System.currentTimeMillis();
		

		GWT.log("allocate cells = " + (t2 - t1));
		GWT.log("ui borders = " + (t3 - t2));
		GWT.log("allocate boundaries = " + (t4 - t3));
		return new RectangularMaze(cells, result, colCount, rowCount, bbox, wallPath);
	}
	
	@Override
	public void perfectRandomize() {
		super.perfectRandomize();
		long t1 = System.currentTimeMillis();
		OMSVGPathSegList wallsSegs = wallPath.getPathSegList();
		wallsSegs.clear();
		int wc = 0;

		// Clear Ui Cells
		for (int i = 0; i < colCount; i++) {
			for (int j = 0; j < rowCount; j++) {
				if (grid[i][j] != null) {
					grid[i][j].setClassName(style.blank());
				}
			}
		}
		
		// Draw the start and end markers
		grid[srcX][srcY].setClassName(style.src());
		grid[destX][destY].setClassName(style.dest());
		OMSVGRectElement srcRect = grid[srcX][srcY].rect;
		OMSVGGElement cellGroup = (OMSVGGElement)srcRect.getParentNode();
		if (srcCircle == null) {
			float srcX = srcRect.getX().getBaseVal().getValue();
			float srcY = srcRect.getY().getBaseVal().getValue();
			float srcW = srcRect.getWidth().getBaseVal().getValue();
			float srcH = srcRect.getHeight().getBaseVal().getValue();
			srcX += 0.1f * srcW;
			srcY += 0.1f * srcH;
			srcW *= 0.8f;
			srcH *= 0.8f;
			srcCircle = new OMSVGCircleElement(srcX + 0.5f * srcW, srcY + 0.5f * srcH, (float)Math.sqrt(0.5f * 0.5f * Math.min(srcW, srcH) * Math.min(srcW, srcH)));
			srcCircle.setClassNameBaseVal(style.symbol());
			cellGroup.appendChild(srcCircle);
		}
		if (destPath == null) {
			OMSVGRectElement destRect = grid[destX][destY].rect;
			float destX = destRect.getX().getBaseVal().getValue();
			float destY = destRect.getY().getBaseVal().getValue();
			float destW = destRect.getWidth().getBaseVal().getValue();
			float destH = destRect.getHeight().getBaseVal().getValue();
			destX += 0.1f * destW;
			destY += 0.1f * destH;
			destW *= 0.8f;
			destH *= 0.8f;
			destPath = new OMSVGPathElement();
			OMSVGPathSegList destSegs = destPath.getPathSegList();
			destSegs.appendItem(destPath.createSVGPathSegMovetoAbs(destX, destY));
			destSegs.appendItem(destPath.createSVGPathSegLinetoAbs(destX + destW, destY));
			destSegs.appendItem(destPath.createSVGPathSegLinetoAbs(destX + 0.5f * destW, destY + destH));
			destSegs.appendItem(destPath.createSVGPathSegClosePath());
			destPath.setClassNameBaseVal(style.symbol());
			cellGroup.appendChild(destPath);
		}
			
		currentX = this.srcX;
		currentY = this.srcY;

		// Ui walls
		float x = bbox.getX();
		float y = bbox.getY();
		float width = bbox.getWidth();
		float height = bbox.getHeight();
		float cellWidth = width / colCount;
		float cellHeight = height / rowCount;
		for (int i = 0; i < colCount - 1; i++) {
			boolean hasSegment = false;
			for (int j = 0; j < rowCount; j++) {
				if (grid[i][j] != null && grid[i + 1][j] != null && grid[i][j].getBoundary(grid[i + 1][j]).hasWall()) {
					if (!hasSegment) {
						wallsSegs.appendItem(wallPath.createSVGPathSegMovetoAbs(x + (i + 1) * cellWidth, y + j * cellHeight));
						hasSegment = true;
					}
					if (j == rowCount -1) {
						wallsSegs.appendItem(wallPath.createSVGPathSegLinetoVerticalAbs(y + (j + 1) * cellHeight));
					}
				} else {
					if (hasSegment) {
						wallsSegs.appendItem(wallPath.createSVGPathSegLinetoVerticalAbs(y + j * cellHeight));
						hasSegment = false;
						wc++;
					}
				}
			}
		}
		for (int j = 0; j < rowCount - 1; j++) {
			boolean hasSegment = false;
			for (int i = 0; i < colCount; i++) {
				if (grid[i][j] != null && grid[i][j + 1] != null && grid[i][j].getBoundary(grid[i][j + 1]).hasWall()) {
					if (!hasSegment) {
						wallsSegs.appendItem(wallPath.createSVGPathSegMovetoAbs(x + i * cellWidth, y + (j + 1) * cellHeight));
						hasSegment = true;
					}
					if (i == colCount -1) {
						wallsSegs.appendItem(wallPath.createSVGPathSegLinetoHorizontalAbs(x + (i + 1) * cellWidth));
					}
				} else {
					if (hasSegment) {
						wallsSegs.appendItem(wallPath.createSVGPathSegLinetoHorizontalAbs(x + i * cellWidth));
						hasSegment = false;
						wc++;
					}
				}
			}
		}
		long t2 = System.currentTimeMillis();
		solution = resolve(grid[this.srcX][this.srcY], grid[this.destX][this.destY]);
		long t3 = System.currentTimeMillis();
		GWT.log("ui walls = " + (t2 - t1));
		GWT.log("wc = " + wc);
		GWT.log("solution = " + (t3 - t2));
	}
}
