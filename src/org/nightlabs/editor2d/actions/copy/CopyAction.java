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
package org.nightlabs.editor2d.actions.copy;

import java.util.List;

import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.actions.ActionFactory;
import org.nightlabs.editor2d.AbstractEditor;
import org.nightlabs.editor2d.DrawComponent;
import org.nightlabs.editor2d.EditorPlugin;
import org.nightlabs.editor2d.actions.AbstractEditorSelectionAction;
import org.nightlabs.editor2d.actions.EditorActionConstants;

/**
 * <p> Author: Daniel.Mazurek[AT]NightLabs[DOT]de </p>
 */
public class CopyAction 
extends AbstractEditorSelectionAction 
{
//	public static final String ID = CopyAction.class.getName();
	public static final String ID = ActionFactory.COPY.getId();
		
	/**
	 * @param editor
	 * @param style
	 */
	public CopyAction(AbstractEditor editor, int style) {
		super(editor, style);
	}

	/**
	 * @param editor
	 */
	public CopyAction(AbstractEditor editor) {
		super(editor);
	}

  protected void init() 
  {
  	super.init();
  	setText(EditorPlugin.getResourceString("action.copy.text"));
  	setToolTipText(EditorPlugin.getResourceString("action.copy.tooltip"));
  	setId(ID);
  	setActionDefinitionId(ID);
//  	setAccelerator(SWT.CTRL | 'C');
  } 
	
	/**
	*@return true, if objects are selected, except the RootEditPart or LayerEditParts
	*/
	protected boolean calculateEnabled() {
		return !getDefaultSelection(false).isEmpty();
	}
		
	/**
	 * adds all selected DrawComponents to the {@link Clipboard} 
	 *  
	 */
	public void run() 
	{
		List dcs = getSelection(DrawComponent.class, true);
		Clipboard clipboard = Clipboard.getDefault();
		Object oldContent = clipboard.getContents();
		clipboard.setContents(dcs);
		firePropertyChange(EditorActionConstants.PROP_COPY_TO_CLIPBOARD, oldContent, dcs);
	}	
					
}
