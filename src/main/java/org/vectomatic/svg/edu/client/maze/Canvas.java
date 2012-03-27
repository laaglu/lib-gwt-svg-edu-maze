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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A Canvas wrapper
 * @author Lukas Laag (laaglu@gmail.com)
 */
public class Canvas {
	@SuppressWarnings("unused")
	private JavaScriptObject context;
	@SuppressWarnings("unused")
	private JavaScriptObject element;
	
	private Canvas() {
	}
	
	public static Canvas createCanvas(int width, int height) {
		Canvas canvas = new Canvas();
		canvas.element = createCanvas_(width, height);
		canvas.context = canvas.getContext();
		return canvas;
	}
	
	private final native JavaScriptObject getContext() /*-{
      return this.@org.vectomatic.svg.edu.client.maze.Canvas::element.getContext("2d");
    }-*/;
	
	private static final native JavaScriptObject createCanvas_(int width, int height) /*-{
	  var elt = $doc.createElement("canvas");
	  elt.width = width;
	  elt.height = height;
	  return elt;
    }-*/;
	
    public final native int getWidth() /*-{
      return this.width;
    }-*/;
    
    public final native void setWidth(int width) /*-{
      return this.width = width;
    }-*/;
    
    public final native int getHeight() /*-{
	  return this.height;
	}-*/;
    
    public final native void setHeight(int height) /*-{
      return this.height = height;
    }-*/;

    public final native void beginPath() /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.beginPath();
    }-*/;

    public final native void closePath() /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.closePath();
    }-*/;

    public final native void moveTo(float x, float y) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.moveTo(x,y);
    }-*/;

    public final native void lineTo(float x, float y) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.lineTo(x,y);
    }-*/;

    public final native void quadraticCurveTo(float cpx, float cpy, float x, float y) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.quadraticCurveTo(cpx, cpy, x, y);
    }-*/;

    public final native void bezierCurveTo(float cp1x, float cp1y, float cp2x,
            float cp2y, float x, float y) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.bezierCurveTo(
      cp1x, cp1y, cp2x, cp2y, x, y);
    }-*/;

    public final native void arcTo(float x1, float y1, float x2, float y2,
            float radius) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.arcTo(x1, y1, x2, y2, radius);
    }-*/;

    public final native void rect(float x, float y, float w, float h) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.rect(x, y, w, h);
    }-*/;

    public final native void clearRect(float x, float y, float w, float h) /*-{
      this.@org.vectomatic.svg.edu.client.maze.Canvas::context.clearRect(x, y, w, h);
    }-*/;

    public final native void arc(float x, float y, float radius, float startAngle,
            float endAngle, boolean anticlockwise) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.arc(
      x, y, radius, startAngle, endAngle, anticlockwise);
    }-*/;

    public final native boolean isPointInPath(float x, float y) /*-{
     return this.@org.vectomatic.svg.edu.client.maze.Canvas::context.isPointInPath(x, y);
    }-*/;
    
    public final native void fill() /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.fill();
    }-*/;
    
    public native void scale(float x, float y) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.scale(x, y);
    }-*/;

    public native void rotate(float angle)/*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.rotate(angle);
    }-*/;

    public native void translate(float x, float y) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.translate(x, y);
    }-*/;

    public native void transform(float m11, float m12, float m21, float m22,
           float dx, float dy) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.transform(
     m11, m12, m21, m22, dx, dy);
     }-*/;

    public native void setTransform(float m11, float m12, float m21, float m22,
           float dx, float dy) /*-{
     this.@org.vectomatic.svg.edu.client.maze.Canvas::context.setTransform(
     m11, m12, m21, m22, dx, dy);
    }-*/;

}
