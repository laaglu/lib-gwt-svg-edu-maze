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

import org.vectomatic.dom.svg.OMSVGDocument;
import org.vectomatic.dom.svg.OMSVGGElement;
import org.vectomatic.dom.svg.OMSVGPathElement;
import org.vectomatic.dom.svg.OMSVGPathSeg;
import org.vectomatic.dom.svg.OMSVGPathSegCurvetoCubicAbs;
import org.vectomatic.dom.svg.OMSVGPathSegCurvetoCubicRel;
import org.vectomatic.dom.svg.OMSVGPathSegCurvetoCubicSmoothAbs;
import org.vectomatic.dom.svg.OMSVGPathSegCurvetoCubicSmoothRel;
import org.vectomatic.dom.svg.OMSVGPathSegLinetoAbs;
import org.vectomatic.dom.svg.OMSVGPathSegLinetoHorizontalAbs;
import org.vectomatic.dom.svg.OMSVGPathSegLinetoHorizontalRel;
import org.vectomatic.dom.svg.OMSVGPathSegLinetoRel;
import org.vectomatic.dom.svg.OMSVGPathSegLinetoVerticalAbs;
import org.vectomatic.dom.svg.OMSVGPathSegLinetoVerticalRel;
import org.vectomatic.dom.svg.OMSVGPathSegList;
import org.vectomatic.dom.svg.OMSVGPathSegMovetoAbs;
import org.vectomatic.dom.svg.OMSVGPathSegMovetoRel;
import org.vectomatic.dom.svg.OMSVGRect;
import org.vectomatic.dom.svg.OMSVGRectElement;
import org.vectomatic.svg.edu.client.maze.RectangularMaze.RectangularCell;

import com.google.gwt.core.client.GWT;

/**
 * Class to rasterize an arbitrary SVG path into a grid of maze cells
 * @author laaglu
 */
public class Rasterizer {
	static class RasterizationResult {
		RectangularCell[][] grid;
		int srcX, srcY;
		int destX, destY;
		public RasterizationResult(int colCount, int rowCount) {
			grid = new RectangularCell[colCount][rowCount];
			for (int i = 0; i < colCount; i++) {
				grid[i] = new RectangularCell[rowCount];
			}
		}
	}
	
	public static RasterizationResult rasterize(OMSVGPathElement path, OMSVGGElement cellGroup, int colCount, int rowCount) {
		long t1 = System.currentTimeMillis();
		OMSVGRect bbox = path.getBBox();
		float minx = bbox.getX();
		float miny = bbox.getY();
		float width = bbox.getWidth();
		float height = bbox.getHeight();
		Canvas canvas = Canvas.createCanvas((int)width, (int)height);

		// Map the SVG path to a canvas path
		OMSVGPathSegList segs = path.getPathSegList();
		canvas.beginPath();
		float x = 0f, y = 0f, prevx2 = 0, prevy2 = 0, x1, y1;
		boolean prevIsCubic = false;
		for (int i = 0, size = segs.getNumberOfItems(); i < size; i++) {
			OMSVGPathSeg seg = segs.getItem(i);
			switch (seg.getPathSegType()) {
				case OMSVGPathSeg.PATHSEG_CLOSEPATH:
					canvas.closePath();
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_MOVETO_ABS:
					OMSVGPathSegMovetoAbs moveToAbs = (OMSVGPathSegMovetoAbs)seg;
					prevx2 = x = moveToAbs.getX();
					prevy2 = y = moveToAbs.getY();
					canvas.moveTo(x, y);
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_MOVETO_REL:
					OMSVGPathSegMovetoRel moveToRel = (OMSVGPathSegMovetoRel)seg;
					x += moveToRel.getX();
					y += moveToRel.getY();
					prevx2 = x;
					prevy2 = y;
					canvas.moveTo(x, y);
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_LINETO_ABS:
					OMSVGPathSegLinetoAbs lineToAbs = (OMSVGPathSegLinetoAbs)seg;
					prevx2 = x = lineToAbs.getX();
					prevy2 = y = lineToAbs.getY();
					canvas.lineTo(x, y);
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_LINETO_REL:
					OMSVGPathSegLinetoRel lineToRel = (OMSVGPathSegLinetoRel)seg;
					x += lineToRel.getX();
					y += lineToRel.getY();
					prevx2 = x;
					prevy2 = y;
					canvas.lineTo(x, y);
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_CURVETO_CUBIC_ABS:
					OMSVGPathSegCurvetoCubicAbs curveToCubicAbs = (OMSVGPathSegCurvetoCubicAbs)seg;
					x = curveToCubicAbs.getX();
					y = curveToCubicAbs.getY();
					prevx2 = curveToCubicAbs.getX2();
					prevy2 = curveToCubicAbs.getY2();
					canvas.bezierCurveTo(curveToCubicAbs.getX1(), curveToCubicAbs.getY1(), prevx2, prevy2, x, y);
					prevIsCubic = true;
					break;
				case OMSVGPathSeg.PATHSEG_CURVETO_CUBIC_REL:
					OMSVGPathSegCurvetoCubicRel curveToCubicRel = (OMSVGPathSegCurvetoCubicRel)seg;
					prevx2 = (curveToCubicRel.getX2() + x);
					prevy2 = (curveToCubicRel.getY2() + y);
					canvas.bezierCurveTo(curveToCubicRel.getX1() + x, curveToCubicRel.getY1() + y, prevx2, prevy2, curveToCubicRel.getX() + x, curveToCubicRel.getY() + y);
					x += curveToCubicRel.getX();
					y += curveToCubicRel.getY();
					prevIsCubic = true;
					break;
				case OMSVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_ABS:
					OMSVGPathSegCurvetoCubicSmoothAbs curveToSmoothAbs = (OMSVGPathSegCurvetoCubicSmoothAbs)seg;
					if (prevIsCubic) {
						x1 = 2 * x - prevx2;
						y1 = 2 * y - prevy2;
					} else {
						x1 = x;
						y1 = y;
					}
					x = curveToSmoothAbs.getX();
					y = curveToSmoothAbs.getY();
					prevx2 = curveToSmoothAbs.getX2();
					prevy2 = curveToSmoothAbs.getY2();
					canvas.bezierCurveTo(x1, y1, prevx2, prevy2, x, y);
					prevIsCubic = true;
					break;
				case OMSVGPathSeg.PATHSEG_CURVETO_CUBIC_SMOOTH_REL:
					OMSVGPathSegCurvetoCubicSmoothRel curveToSmoothRel = (OMSVGPathSegCurvetoCubicSmoothRel)seg;
					if (prevIsCubic) {
						x1 = 2 * x - prevx2;
						y1 = 2 * y - prevy2;
					} else {
						x1 = x;
						y1 = y;
					}
					prevx2 = (x + curveToSmoothRel.getX2());
					prevy2 = (y + curveToSmoothRel.getY2());
					x += curveToSmoothRel.getX();
					y += curveToSmoothRel.getY();
					canvas.bezierCurveTo(x1, y1, prevx2, prevy2, x, y);
					prevIsCubic = true;
					break;
				case OMSVGPathSeg.PATHSEG_LINETO_HORIZONTAL_ABS:
					OMSVGPathSegLinetoHorizontalAbs lineToHorizAbs = (OMSVGPathSegLinetoHorizontalAbs)seg;
					prevx2 = x = lineToHorizAbs.getX();
					canvas.lineTo(x, y);
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_LINETO_HORIZONTAL_REL:
					OMSVGPathSegLinetoHorizontalRel lineToHorizRel = (OMSVGPathSegLinetoHorizontalRel)seg;
					x += lineToHorizRel.getX();
					prevx2 = x;
					canvas.lineTo(x, y);
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_LINETO_VERTICAL_ABS:
					OMSVGPathSegLinetoVerticalAbs lineToVertAbs = (OMSVGPathSegLinetoVerticalAbs)seg;
					prevy2 = y = lineToVertAbs.getY();
					canvas.lineTo(x, y);
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_LINETO_VERTICAL_REL:
					OMSVGPathSegLinetoVerticalRel lineToVertRel = (OMSVGPathSegLinetoVerticalRel)seg;
					y += lineToVertRel.getY();
					prevy2 = y;
					canvas.lineTo(x, y);
					prevIsCubic = false;
					break;
				case OMSVGPathSeg.PATHSEG_ARC_ABS:
				case OMSVGPathSeg.PATHSEG_ARC_REL:
				default:
					throw new IllegalStateException("Unsupported seg type:" + seg.getPathSegType());
			}
		}
		
		// Determine the start and end cell by computing the
		// cell with the smaller distance to the specified points
		String start = path.getAttributeNS(RectangularMaze.VECTOMATIC_NS, "start");
		String[] startArray = start.split("x");
		float xstart = Float.parseFloat(startArray[0]);
		float ystart = Float.parseFloat(startArray[1]);
		String end = path.getAttributeNS(RectangularMaze.VECTOMATIC_NS, "end");
		String[] endArray = end.split("x");
		float xend = Float.parseFloat(endArray[0]);
		float yend = Float.parseFloat(endArray[1]);
		float dstart = Float.MAX_VALUE, dend = Float.MAX_VALUE;
		

		
		// Render the canvas path as a cell grid using isPointInPath
		OMSVGDocument doc = (OMSVGDocument) cellGroup.getOwnerDocument();
		RasterizationResult result = new RasterizationResult(colCount, rowCount);
		
		float cellWidth = width / colCount;
		float cellHeight = height / rowCount;
		for (int i = 0; i < colCount; i++) {
			for (int j = 0; j < rowCount; j++) {
				// Is the cell center in the path
				float px = minx + (i + 0.5f) * cellWidth;
				float py = miny + (j + 0.5f) * cellHeight;
				if (canvas.isPointInPath(px, py)) {
					RectangularCell cell = new RectangularCell(i, j);
					OMSVGRectElement rect = doc.createSVGRectElement(minx + i * cellWidth, miny + j * cellHeight, cellWidth, cellHeight, 0f, 0f);
					cell.setRect(rect);
					cellGroup.appendChild(rect);
					float d = (px - xstart) * (px - xstart) + (py - ystart) * (py - ystart);
					if (d < dstart) {
						dstart = d;
						result.srcX = i;
						result.srcY = j;
					}
					d = (px - xend) * (px - xend) + (py - yend) * (py - yend);
					if (d < dend) {
						dend = d;
						result.destX = i;
						result.destY = j;
					}
					result.grid[i][j] = cell;
				}
			}
		}
		long t2 = System.currentTimeMillis();

		GWT.log("cells rasterization = " + (t2 - t1));
		return result;
	}
}
