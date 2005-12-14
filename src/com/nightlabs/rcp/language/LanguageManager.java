/**
 * <p> Project: com.nightlabs.base </p>
 * <p> Copyright: Copyright (c) 2005 </p>
 * <p> Company: NightLabs GmbH (Germany) </p>
 * <p> Creation Date: 01.08.2005 </p>
 * <p> Author: Daniel Mazurek </p>
**/
package com.nightlabs.rcp.language;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.nightlabs.config.Config;
import com.nightlabs.config.ConfigException;
import com.nightlabs.language.LanguageCf;
import com.nightlabs.rcp.config.LanguageCfMod;
import com.nightlabs.rcp.ressource.SharedImages;

public class LanguageManager
implements ILanguageManager
{		
	public static final String LANGUAGE_CHANGED = "language changed";
	
	private static LanguageManager sharedInstance = null;
	public static LanguageManager sharedInstance()  
	{
		if (sharedInstance == null)
			sharedInstance = new LanguageManager();
		
		return sharedInstance;
	}
	
	/**
	 * 
	 * @return the default Language, by default the corresponding languageID is Locale.getDefault().getLanguage() 
	 */
	public static LanguageCf createDefaultLanguage() {
		return new LanguageCf(Locale.getDefault().getLanguage());		
	}
	
	/**
	 * 
	 * @param languageID the id (@see Locale.getLanguage()) of the LanguageCf
	 * @return a LanguageCf with the given languageID
	 */
	public static LanguageCf createLanguage(String languageID) {
		return new LanguageCf(languageID);
	}
	
	/**
	 * 
	 * @param locale The Locale to get the languageID from 
	 * @return the languageID for the given java.util.Locale
	 */
	public static String getLanguageID(Locale locale) 
	{
		if (locale == null)
			throw new IllegalArgumentException("Param locale must not be null!");
		
		return locale.getLanguage();
	}

	/**
	 * @param languageID
	 * @return a Locale for the given languageID
	 */
	public static Locale getLocale(String languageID) {
		return new Locale(languageID);
	}

	public LanguageManager() 
	{
		super();
		try {
			langCfMod = (LanguageCfMod) Config.sharedInstance().createConfigModule(LanguageCfMod.class);
			for (Iterator it = langCfMod.getLanguages().iterator(); it.hasNext(); ) {
				LanguageCf langCf = (LanguageCf) it.next();
				languageID2LanguageCf.put(langCf.getLanguageID(), langCf);
			}
			currentLanguage = getLanguage(Locale.getDefault(), false);
			if (currentLanguage == null) {
				currentLanguage = createDefaultLanguage();
				addLanguage(currentLanguage);
			}
		} catch (ConfigException e) {
			throw new RuntimeException(e);
		}
	}

	protected LanguageCfMod langCfMod;	
		
	/**
	 * 
	 * @param langCf The LanguageCf to add
	 */
	public void addLanguage(LanguageCf langCf) {
		if (languageID2LanguageCf.containsKey(langCf.getLanguageID()))
			return;

		langCfMod.getLanguages().add(langCf);
		languageID2LanguageCf.put(langCf.getLanguageID(), langCf);
		// config is saved on exit.
//		try {
//			langCfMod._getConfig().saveConfFile();
//		} catch (ConfigException e) {
//			throw new RuntimeException(e);
//		}
	}
	
	/**
	 * creates an LanguageCF with the given languageID and adds it
	 * @param languageID
	 */
	public void addLanguage(String languageID) {
		if (languageID2LanguageCf.containsKey(languageID))
			return;

		LanguageCf langCf = createLanguage(languageID);
		addLanguage(langCf);
	}

	/**
	 * creates an LanguageCF based on the given Locale and adds it
	 * @param locale
	 */
	public void addLanguage(Locale locale) {
		if (languageID2LanguageCf.containsKey(locale.getLanguage()))
			return;

		LanguageCf langCf = createLanguage(getLanguageID(locale));
		addLanguage(langCf);
	}
		
	protected Map languageID2LanguageCf = new HashMap();	

	private Collection unmodifiableLanguages = null;
	/**
	 * 
	 * @return a java.util.Collection which contains all added com.nightlabs.language.LanguageCf �s
	 */
	public Collection getLanguages() {
		if (unmodifiableLanguages == null)
			unmodifiableLanguages = Collections.unmodifiableCollection(langCfMod.getLanguages());
		return unmodifiableLanguages;
	}

	/**
	 * Convenience method which calls {@link #getLanguage(String, boolean)}.
	 */
	public LanguageCf getLanguage(Locale locale, boolean throwExceptionIfNotFound)
	{
		return getLanguage(locale.getLanguage(), throwExceptionIfNotFound);
	}

	/**
	 * @param languageID The 2-char-iso-code of the desired Language.
	 * @param throwExceptionIfNotFound Whether or not to throw an exception if the language couldn't be found.
	 *		If this is <tt>true</tt>, this method will never return <tt>null</tt>.
	 * @return The found <tt>LanguageCf</tt> or <tt>null</tt>, if not found and <tt>throwExceptionIfNotFound == false</tt>.
	 */
	public LanguageCf getLanguage(String languageID, boolean throwExceptionIfNotFound)
	{
		LanguageCf res = (LanguageCf) languageID2LanguageCf.get(languageID);

		if (res == null && throwExceptionIfNotFound)
			throw new IllegalArgumentException("No language registered for languageID=\"" + languageID + "\"!");

		return res;
	}

	/**
	 * 
	 * @param languageID
	 * @return the Native Name of the Language 
	 */
	public static String getNativeLanguageName(String languageID) {
		return getLocale(languageID).getDisplayLanguage();
	}

	protected LanguageCf currentLanguage;

	/**
	 * 
	 * @return The current Language
	 */
	public LanguageCf getCurrentLanguage() {
		return currentLanguage;
	}

	/**
	 * 
	 * @return the languageID of the currentLanguageCf
	 */
	public String getCurrentLanguageID() {
		return getCurrentLanguage().getLanguageID();
	}

	/**
	 * 
	 * @param newCurrentLanguage The current Language to set
	 */
	public void setCurrentLanguage(LanguageCf newCurrentLanguage) 
	{
		LanguageCf oldLanguage = currentLanguage;
		this.currentLanguage = newCurrentLanguage;
		pcs.firePropertyChange(LANGUAGE_CHANGED, oldLanguage, currentLanguage);
	}
	
	public void setCurrentLanguageID(String newLanguageID) 
	{
		if (languageID2LanguageCf.containsKey(newLanguageID)) {
			LanguageCf langCf = (LanguageCf) languageID2LanguageCf.get(newLanguageID);
			setCurrentLanguage(langCf);
		} 
		else {
			LanguageCf langCf = createLanguage(newLanguageID);
			setCurrentLanguage(langCf);
		}
	}	
	
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	/**
	 * 
	 * @param pcl the java.beans.PropertyChangeListener to add
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	/**
	 * 
	 * @param pcl the java.beans.PropertyChangeListener to remove
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
		
	/**
	 * 
	 * @param languageID the ID of the Language (e.g. en, de, us) (@see java.util.Locale.getLanguage())
	 * @return the flag of the country for the given Language
	 */
	public static Image getImage(String languageID) 
	{
		ImageDescriptor desc = SharedImages.getImageDescriptor(languageID); 
		if (desc != null)
			return desc.createImage();
		
		return null;
	}

	/**
	 * Calling this method will cause the config module to be marked as changed.
	 *
	 * @param languageCf The <tt>LanguageCf</tt> which has been changed. 
	 */
	public void makeDirty(LanguageCf languageCf)
	{
		langCfMod.setChanged();
	}
}
