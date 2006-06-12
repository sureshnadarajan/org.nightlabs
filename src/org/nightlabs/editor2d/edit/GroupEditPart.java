/* *****************************************************************************
 * NightLabs Editor2D - Graphical editor framework                             *
 * Copyright (C) 2004-2005 NightLabs - http://NightLabs.org                    *
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
package org.nightlabs.editor2d.edit;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.nightlabs.editor2d.DrawComponent;
import org.nightlabs.editor2d.DrawComponentContainer;
import org.nightlabs.editor2d.GroupDrawComponent;
import org.nightlabs.editor2d.figures.DrawComponentFigure;
import org.nightlabs.editor2d.model.GroupPropertySource;

/**
 * <p> Author: Daniel.Mazurek[AT]NightLabs[DOT]de </p>
 */
public class GroupEditPart 
extends AbstractDrawComponentContainerEditPart 
{
	public static final Logger LOGGER = Logger.getLogger(GroupEditPart.class);
	
	/**
	 * @param drawComponentContainer
	 */
	public GroupEditPart(GroupDrawComponent group) {
		super(group);
	}

	public GroupDrawComponent getGroupDrawComponent() {
		return (GroupDrawComponent) getModel();
	}
	
  protected IPropertySource getPropertySource()
  {
    if (propertySource == null) {
      propertySource = new GroupPropertySource(getGroupDrawComponent());
    }
    return propertySource;
  }

	@Override
	protected void propertyChanged(PropertyChangeEvent evt) 
	{
		super.propertyChanged(evt);
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(DrawComponentContainer.CHILD_ADDED)) 
		{
			LOGGER.debug(propertyName);
			Collection<DrawComponent> children = (Collection<DrawComponent>) evt.getNewValue();
			if (children != null) 
			{
				for (Iterator<DrawComponent> it = children.iterator(); it.hasNext(); ) 
				{
					Object o = getViewer().getEditPartRegistry().get(it.next());
					if (o != null && o instanceof GraphicalEditPart) 
					{
						GraphicalEditPart gep = (GraphicalEditPart) o;
						IFigure figure = gep.getFigure();
						if (figure instanceof DrawComponentFigure) {
							DrawComponentFigure dcFigure = (DrawComponentFigure) figure;
							dcFigure.setContains(false);
							LOGGER.debug("DrawComponentFigure found and set contains to false!");
						}					
					}
				}
				refresh();				
			}
			return;
		}
		else if (propertyName.equals(DrawComponentContainer.CHILD_REMOVED)) 
		{
			LOGGER.debug(propertyName);
			Collection<DrawComponent> newChildren = (Collection<DrawComponent>) evt.getNewValue();
			Collection<DrawComponent> oldChildren = (Collection<DrawComponent>) evt.getOldValue();
			if (newChildren != null && oldChildren != null) 
			{
				oldChildren.removeAll(newChildren);
				for (Iterator<DrawComponent> it = oldChildren.iterator(); it.hasNext(); ) 
				{
					Object o = getViewer().getEditPartRegistry().get(it.next());
					if (o != null && o instanceof GraphicalEditPart) 
					{
						GraphicalEditPart gep = (GraphicalEditPart) o;
						IFigure figure = gep.getFigure();
						if (figure instanceof DrawComponentFigure) {
							DrawComponentFigure dcFigure = (DrawComponentFigure) figure;
							dcFigure.setContains(true);
							LOGGER.debug("DrawComponentFigure found and set contains to true!");
						}					
					}
				}
				refresh();										
			}
			return;
		}		
	} 	
    
}
