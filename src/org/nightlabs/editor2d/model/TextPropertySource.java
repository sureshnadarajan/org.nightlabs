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

package org.nightlabs.editor2d.model;

import java.awt.Color;
import java.util.List;

import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.nightlabs.base.property.AWTColorPropertyDescriptor;
import org.nightlabs.base.property.CheckboxPropertyDescriptor;
import org.nightlabs.base.property.XTextPropertyDescriptor;
import org.nightlabs.editor2d.EditorPlugin;
import org.nightlabs.editor2d.ShapeDrawComponent;
import org.nightlabs.editor2d.TextDrawComponent;
import org.nightlabs.editor2d.properties.FontNamePropertyDescriptor;

public class TextPropertySource 
extends ShapeDrawComponentPropertySource 
{
	/**
	 * LOG4J logger used by this class
	 */
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(TextPropertySource.class);
	
	public TextPropertySource(TextDrawComponent text) {
		super(text);
	}
	
	protected TextDrawComponent getTextDrawComponent() {
		return (TextDrawComponent) drawComponent;
	}
	
	public static final String CATEGORY_FONT = EditorPlugin.getResourceString("property.category.text");	
		
	protected List createPropertyDescriptors() 
	{
//		List descriptors = super.createPropertyDescriptors();		
		List descriptors = getDescriptors();
		
		// Name
		descriptors.add(createNamePD());				
		// X
		descriptors.add(createXPD());		
		// Y		
		descriptors.add(createYPD());		
		// Width		
		descriptors.add(createWidthPD());		
		// Height		
		descriptors.add(createHeightPD());		
		// Rotation		
		descriptors.add(createRotationPD());
		// Color
		descriptors.add(createColorPD());
		// Font Name
		descriptors.add(createFontNamePD());		
		// Font Size
		descriptors.add(createFontSizePD());		
		// Bold
		descriptors.add(createBoldPD());		
		// Italic
		descriptors.add(createItalicPD());		
		// Text
		descriptors.add(createTextPD());
		// Visible
		descriptors.add(createVisiblePD());
		
		return descriptors;
	}	
	
	protected PropertyDescriptor createFontNamePD() 
	{
		PropertyDescriptor desc = new FontNamePropertyDescriptor(TextDrawComponent.PROP_FONT_NAME,
				EditorPlugin.getResourceString("property.fontname.label"));
		desc.setCategory(CATEGORY_FONT);
		return desc;
	}
		
	protected PropertyDescriptor createBoldPD() 
	{
		PropertyDescriptor desc = new CheckboxPropertyDescriptor(TextDrawComponent.PROP_BOLD,
				EditorPlugin.getResourceString("property.bold.label"));
		desc.setCategory(CATEGORY_FONT);
		return desc;
	}

	protected PropertyDescriptor createItalicPD() 
	{
		PropertyDescriptor desc = new CheckboxPropertyDescriptor(TextDrawComponent.PROP_ITALIC,
				EditorPlugin.getResourceString("property.italic.label"));
		desc.setCategory(CATEGORY_FONT);
		return desc;
	}
	
	protected PropertyDescriptor createTextPD() 
	{
		PropertyDescriptor desc = new TextPropertyDescriptor(TextDrawComponent.PROP_TEXT,
				EditorPlugin.getResourceString("property.text.label"));
		desc.setCategory(CATEGORY_FONT);
		return desc;
	}
	
	protected PropertyDescriptor createColorPD()
	{
		PropertyDescriptor desc = new AWTColorPropertyDescriptor(ShapeDrawComponent.PROP_FILL_COLOR, 
				EditorPlugin.getResourceString("property.color.label"));
		desc.setCategory(CATEGORY_COLORS);
		return desc;
	}
	
//	protected PropertyDescriptor createFontSizePD() 
//	{
////	PropertyDescriptor desc = new FontSizePropertyDescriptor(TextDrawComponent.PROP_FONT_SIZE,
////	EditorPlugin.getResourceString("property.fontsize.label"));
////  desc.setCategory(CATEGORY_FONT);		
//		PropertyDescriptor desc = new IntPropertyDescriptor(TextDrawComponent.PROP_FONT_SIZE,
//				EditorPlugin.getResourceString("property.fontsize.label"));
//		desc.setCategory(CATEGORY_FONT);
//		return desc;
//	}

	protected PropertyDescriptor createFontSizePD() 
	{
//		boolean editable = !getTextDrawComponent().isTransformed();
		PropertyDescriptor desc = new XTextPropertyDescriptor(TextDrawComponent.PROP_FONT_SIZE,
				EditorPlugin.getResourceString("property.fontsize.label"));
		desc.setCategory(CATEGORY_FONT);
		return desc;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) 
	{		
		if (id.equals(TextDrawComponent.PROP_FONT_NAME)) {
			getTextDrawComponent().setFontName((String)value);
			return;
		}
		if (id.equals(TextDrawComponent.PROP_BOLD)) {
			getTextDrawComponent().setBold(((Boolean)value).booleanValue());
			return;			
		}
		if (id.equals(TextDrawComponent.PROP_ITALIC)) {
			getTextDrawComponent().setItalic(((Boolean)value).booleanValue());
			return;			
		}
		if (id.equals(TextDrawComponent.PROP_TEXT)) {
			getTextDrawComponent().setText((String)value);
			return;			
		}
		if (id.equals(TextDrawComponent.PROP_FONT_SIZE)) 
		{
			int fontSize;
			try {
				fontSize = Integer.valueOf((String)value);	
				logger.debug("new fontSize = "+fontSize);
			} catch (NumberFormatException nfe) {
				fontSize = getTextDrawComponent().getFontSize();
				logger.debug("NumberFormatException for fontSize = "+value);				
			}
			getTextDrawComponent().setFontSize(fontSize);
			return;			
		}		
		if (id.equals(ShapeDrawComponent.PROP_FILL_COLOR)) {
			getTextDrawComponent().setFillColor((Color)value);
			getTextDrawComponent().setLineColor((Color)value);			
		}		
		super.setPropertyValue(id, value);		
	}		
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) 
	{
		if (id.equals(TextDrawComponent.PROP_FONT_NAME)) {
			return getTextDrawComponent().getFontName();
		}
		else if (id.equals(TextDrawComponent.PROP_BOLD)) {
			return new Boolean(getTextDrawComponent().isBold());
		}
		else if (id.equals(TextDrawComponent.PROP_ITALIC)) {
			return new Boolean(getTextDrawComponent().isItalic());
		}
		else if (id.equals(TextDrawComponent.PROP_TEXT)) {
			return getTextDrawComponent().getText();
		}
//		else if (id.equals(TextDrawComponent.PROP_FONT_SIZE)) {
//			return new Integer(getTextDrawComponent().getFontSize());
//		}
		else if (id.equals(TextDrawComponent.PROP_FONT_SIZE)) 
		{
			if (!getTextDrawComponent().isTransformed())
				return new String(new Integer(getTextDrawComponent().getFontSize()).toString());
			else
				return EditorPlugin.getResourceString("property.fontSizeUndefined.label");
		}				
		return super.getPropertyValue(id);
	}			
	
}
