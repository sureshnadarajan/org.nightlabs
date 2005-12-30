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

package org.nightlabs.base.wizard;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Extends WizardPage and resolves its wickedness of NullPointerExceptions.
 * Subclass this instead of WizardPage and simply implement
 * {@link #createPageContents(Composite)}.
 * 
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public abstract class DynamicPathWizardPage
extends WizardPage
implements IDynamicPathWizardPage
{

	/**
	 * @param pageName
	 */
	public DynamicPathWizardPage(String pageName) {
		super(pageName);
	}

	/**
	 * @param pageName
	 * @param title
	 */
	public DynamicPathWizardPage(String pageName, String title) {
		super(pageName);
		setTitle(title);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public DynamicPathWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	protected Control contents;

	/**
	 * Overidden to prevent NullPointerExceptions.
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#getControl()
	 */
	public Control getControl() {
		return contents;
	}
	
	/**
	 * Calls createPageContents and then setContents() with the result.
	 * Additionally set the message to {@link #getDefaultPageMessage()} if
	 * overridden and does not return null.
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		contents = createPageContents(parent);
		setDefaultPageMessage();
		setControl(contents);
	}
	
	/**
	 * Create the WizardPage contents and return the topmost Control.
	 * 
	 * @param parent
	 * @return
	 */
	public abstract Control createPageContents(Composite parent);

	/**
	 * The implementation of this method in <tt>DynamicPathWizardPage</tt> returns
	 * always <tt>true</tt>. If you want to forbid this page from being the
	 * last, you must overwrite it.
	 * 
	 * @see org.nightlabs.base.wizard.IDynamicPathWizardPage#canBeLastPage()
	 */
	public boolean canBeLastPage()
	{
		return true;
	}
	
	/**
	 * This method is called by {@link #updateStatus(String)}. 
	 * Override to set the message displayed initially and on 
	 * page completition.
	 * Default implementation will return an empty string.
	 * 
	 * @return null
	 */
	protected String getDefaultPageMessage() {
		return null;
	}
	
	private void setDefaultPageMessage() {
		String defaultMessage = getDefaultPageMessage();
		if (defaultMessage != null)
			setMessage(defaultMessage);
	}
	
	/**
	 * Lets you control the status with an error-message.
	 * When null is passed the displayed message is set to
	 * {@link #getDefaultPageMessage()} and pageComplete 
	 * will be set to true.
	 * Otherwise pageComplete will be false and the
	 * passed errMsg will be displayed.
	 * 
	 * @param errMsg
	 */
	protected void updateStatus(String errMsg) {
		if (errMsg != null) {
			setErrorMessage(errMsg);
			setPageComplete(false);
		}
		else {
			setErrorMessage(null);
			setDefaultPageMessage();
			setPageComplete(true);
		}
	}

	/**
	 * @see org.nightlabs.base.wizard.IDynamicPathWizardPage#onShow()
	 */
	public void onShow()
	{
	}

	/**
	 * @see org.nightlabs.base.wizard.IDynamicPathWizardPage#onHide()
	 */
	public void onHide()
	{
	}
}
