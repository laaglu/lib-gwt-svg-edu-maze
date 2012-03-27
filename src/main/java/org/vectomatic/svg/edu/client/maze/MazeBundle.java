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

import org.vectomatic.dom.svg.ui.SVGResource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface MazeBundle extends ClientBundle {
	public static final MazeBundle INSTANCE = GWT.create(MazeBundle.class);
	@Source("maze.css")
	public MazeCss getCss();
	@Source("levels.txt")
	public TextResource levels();
	@Source("direction_a_suivre_4_yve_01.svg")
	public SVGResource up();
	@Source("direction_a_suivre_1_yve_01.svg")
	public SVGResource down();
	@Source("direction_a_suivre_2_yve_01.svg")
	public SVGResource left();
	@Source("direction_a_suivre_3_yve_01.svg")
	public SVGResource right();
	@Source("eraser.svg")
	public SVGResource back();
	@Source("salvagente_architetto_fr_01.svg")
	public SVGResource help();
	@Source("computer.svg")
	public SVGResource compute();
}
