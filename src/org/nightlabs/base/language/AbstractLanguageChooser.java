/* *****************************************************************************
 * org.nightlabs.base - NightLabs Eclipse utilities                            *
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

package org.nightlabs.base.language;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Composite;

import org.nightlabs.base.composite.XComposite;
import org.nightlabs.language.LanguageCf;

/**
 * @author Marco Schulze - marco at nightlabs dot de
 */
public abstract class AbstractLanguageChooser
	extends XComposite
	implements LanguageChooser
{
	private List languageChangeListeners = new LinkedList();

	/**
	 * @param parent
	 * @param style
	 * @param setLayoutData
	 */
	public AbstractLanguageChooser(Composite parent, int style,
			boolean setLayoutData)
	{
		super(parent, style, XComposite.LAYOUT_MODE_TIGHT_WRAPPER,
				setLayoutData ? XComposite.LAYOUT_DATA_MODE_GRID_DATA : LAYOUT_DATA_MODE_NONE);
	}
	
	/**
	 * @see org.nightlabs.ipanema.language.LanguageChooser#addLanguageChangeListener(org.nightlabs.ipanema.language.LanguageChangeListener)
	 */
	public void addLanguageChangeListener(LanguageChangeListener l)
	{
		languageChangeListeners.add(l);
	}

	/**
	 * @see org.nightlabs.ipanema.language.LanguageChooser#removeLanguageChangeListener(org.nightlabs.ipanema.language.LanguageChangeListener)
	 */
	public void removeLanguageChangeListener(LanguageChangeListener l)
	{
		languageChangeListeners.remove(l);
	}
	
	private LanguageCf oldLanguage = null;

	public void fireLanguageChangeEvent()
	{
		if (languageChangeListeners.size() < 1)
			return;

		LanguageChangeEvent languageChangeEvent = new LanguageChangeEvent(this, oldLanguage, getLanguage());
		for (Iterator it = languageChangeListeners.iterator(); it.hasNext(); ) {
			((LanguageChangeListener)it.next()).languageChanged(languageChangeEvent);
		}

		oldLanguage = getLanguage();
	}
}
