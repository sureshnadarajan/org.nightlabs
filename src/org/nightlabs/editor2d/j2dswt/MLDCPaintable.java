/* *****************************************************************************
 * NightLabs Editor2D - Graphical editor framework                             *
 * Copyright (C) 2004-2005 NightLabs - http://NightLabs.org                    *
 * Project author: Daniel Mazurek <Daniel.Mazurek [at] nightlabs [dot] org>    *
 *                                                                             *
 * This library is free software; you can redistribute it and/or               *
 * modify it under the terms of the GNU Lesser General Public                  *
 * License as published by the Free Software Foundation; either                *
 * version 2.1 of the License, or (at your option) any later version.          *
 *                                                                             *
 * This library is distributed in the hope that it will be useful,             *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of              *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU           *
 * Lesser General Public License for more details.                             *
 *                                                                             *
 * You should have received a copy of the GNU Lesser General Public            *
 * License along with this library; if not, write to the                       *
 *     Free Software Foundation, Inc.,                                         *
 *     51 Franklin St, Fifth Floor,                                            *
 *     Boston, MA  02110-1301  USA                                             *
 *                                                                             *
 * Or get it online :                                                          *
 *     http://www.gnu.org/copyleft/lesser.html                                 *
 *                                                                             *
 *                                                                             *
 ******************************************************************************/

package org.nightlabs.editor2d.j2dswt;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;
import org.holongate.j2d.IPaintable;
import org.holongate.j2d.J2DUtilities;

import org.nightlabs.editor2d.DrawComponent;
import org.nightlabs.editor2d.DrawComponentContainer;
import org.nightlabs.editor2d.MultiLayerDrawComponent;
import org.nightlabs.editor2d.render.J2DRenderContext;
import org.nightlabs.editor2d.render.RenderContext;
import org.nightlabs.editor2d.render.Renderer;

public class MLDCPaintable implements IPaintable
{
	protected MultiLayerDrawComponent mldc;
	public MLDCPaintable(MultiLayerDrawComponent mldc) 
	{
		this.mldc = mldc;
	}
	
//	public void paint(Control control, Graphics2D g2d)
//	{
//		for (Iterator itLayers = mldc.getDrawComponents().iterator(); itLayers.hasNext(); ) {
//			Layer l = (Layer) itLayers.next();
//			for (Iterator it = l.getDrawComponents().iterator(); it.hasNext(); ) {
//				DrawComponent dc = (DrawComponent) it.next();
//				Renderer r = dc.getRenderer();
//				if (r != null) 
//					r.paint(dc, g2d);
//			}
//		}
//	}
	public void paint(Control control, Graphics2D g2d)
	{
		for (Iterator itLayers = mldc.getDrawComponents().iterator(); itLayers.hasNext(); ) {
			DrawComponent dc = (DrawComponent) itLayers.next();
			paintDrawComponent(dc, g2d);
		}
	}

	protected void paintDrawComponent(DrawComponent dc, Graphics2D g2d) 
	{
		if (dc instanceof DrawComponentContainer) {
			DrawComponentContainer dcContainer = (DrawComponentContainer) dc;
			for (Iterator it = dcContainer.getDrawComponents().iterator(); it.hasNext(); ) {
				DrawComponent drawComponent = (DrawComponent) it.next();
				paintDrawComponent(drawComponent, g2d);
			}
		}
		else {
			Renderer r = dc.getRenderer();
			if (r != null) {
//			r.paint(dc, g2d);
				RenderContext rc = r.getRenderContext();
				if (rc != null && rc instanceof J2DRenderContext)
					((J2DRenderContext)rc).paint(dc, g2d);								
			}
		}
	}
	
	public void redraw(Control control, GC gc)
	{
		
	}

	public Rectangle2D getBounds(Control control)
	{
		return J2DUtilities.toRectangle2D(control.getBounds());
	}
}
