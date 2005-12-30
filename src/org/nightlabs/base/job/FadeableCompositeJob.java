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

package org.nightlabs.base.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import org.nightlabs.base.composite.Fadeable;

/**
 * @author Niklas Schiffler <nick@nightlabs.de>
 *
 */
public abstract class FadeableCompositeJob extends Job
{
  private Fadeable composite;
  private Object source;
  
  public FadeableCompositeJob(String label, Fadeable comp, Object source)
  {
  	super(label);
  	this.composite = comp;
  	this.source = source;
  }

  protected IStatus run(IProgressMonitor monitor)
  {
  	IStatus ret = Status.CANCEL_STATUS;
  	try
  	{
  		Display.getDefault().syncExec(
  				new Runnable() 
  				{
  					public void run() 
  					{
  						composite.setFaded(true);
  					}
  				}
  		);
  		ret = run(monitor, source);
  	}
  	finally
  	{
  		Display.getDefault().syncExec(
  				new Runnable() 
  				{
  					public void run() 
  					{
  						composite.setFaded(false);
  					}
  				}
  		);
  	}
    return ret;
  }

  public abstract IStatus run(IProgressMonitor monitor, Object source);
}
