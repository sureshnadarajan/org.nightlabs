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

package org.nightlabs.base.config;

import org.nightlabs.base.language.LanguageManager;
import org.nightlabs.config.CfModList;
import org.nightlabs.config.ConfigModule;
import org.nightlabs.config.InitException;

public class LanguageCfMod 
extends ConfigModule
{
	public LanguageCfMod() {
		super();
	}

	protected CfModList languages = null;	
	public CfModList getLanguages() {
		return languages;
	}
	public void setLanguages(CfModList languages) {
		this.languages = languages;		
	}

	public void init() 
	throws InitException 
	{
		super.init();
		if (languages == null || languages.isEmpty())
			languages = createDefaultLanguage();
		languages.setOwnerCfMod(this);
	}

	protected CfModList createDefaultLanguage() 
	{
		CfModList l = new CfModList(this);
		l.add(LanguageManager.createDefaultLanguage());		
		return l;
	}
	
//	public static Collection createDefaultLanguages() 
//	{
//		List languages = new ArrayList();
//		languages.add(Locale.ENGLISH.getLanguage());
//		languages.add(Locale.GERMAN.getLanguage());
//		languages.add(Locale.FRENCH.getLanguage());
//		return languages;
//	}
}
