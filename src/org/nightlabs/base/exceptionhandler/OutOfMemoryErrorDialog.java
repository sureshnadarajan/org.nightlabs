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

package org.nightlabs.base.exceptionhandler;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import org.nightlabs.base.exceptionhandler.errorreport.ErrorReport;

/**
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public class OutOfMemoryErrorDialog extends DefaultErrorDialog {
	
	private static final Logger LOGGER = Logger.getLogger(OutOfMemoryErrorDialog.class);
	
  protected static final int RESTART_WORKBENCH_ID = DefaultErrorDialog.SEND_ERROR_REPORT_ID + 10;
  
  private Button restartWorkbenchButton;
  
	/**
	 * @param parentShell
	 * @param dialogTitle
	 * @param message
	 * @param thrownException
	 * @param triggerException
	 */
	public OutOfMemoryErrorDialog(Shell parentShell, String dialogTitle,
			String message, Throwable thrownException, Throwable triggerException) {
		super(parentShell, dialogTitle, message, thrownException, triggerException);
	}

	/**
	 * @param dialogTitle
	 * @param message
	 * @param error
	 */
	public OutOfMemoryErrorDialog(String dialogTitle, String message,
			Throwable error) {
		super(dialogTitle, message, error);
	}

	/**
	 * @param error
	 */
	public OutOfMemoryErrorDialog(Throwable error) {
		super(error);
	}

	/**
	 * @param thrownException
	 * @param triggerException
	 */
	public OutOfMemoryErrorDialog(Throwable thrownException,
			Throwable triggerException) {
		super(thrownException, triggerException);
	}

	/**
	 * @param errorReport
	 */
	public OutOfMemoryErrorDialog(ErrorReport errorReport) {
		super(errorReport);
	}

	protected void createCustomButtons(Composite parent) {
		restartWorkbenchButton = createButton(parent, RESTART_WORKBENCH_ID, "Shutdown Workbench", false);
		super.createCustomButtons(parent);
	}
	
	protected void buttonPressed(int id) {
		if (id == RESTART_WORKBENCH_ID) {
			try {
				LOGGER.info("Trying to restart the workbench due to OutOfMemoryError");
				if (!PlatformUI.getWorkbench().restart()) {
					System.exit(1);
				}
				LOGGER.info("Close successful, restarting");
			} catch (Throwable t) {
				System.exit(1);
			}
		}
		else			
			super.buttonPressed(id);
	}

}
