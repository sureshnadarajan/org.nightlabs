/* *****************************************************************************
 * JFire - it's hot - Free ERP System - http://jfire.org                       *
 * Copyright (C) 2004-2006 NightLabs - http://NightLabs.org                    *
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
 *     http://opensource.org/licenses/lgpl-license.php                         *
 ******************************************************************************/
package org.nightlabs.base.entity.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.nightlabs.base.composite.XComposite;
import org.nightlabs.base.progress.CompoundProgressMonitor;
import org.nightlabs.base.progress.SaveProgressMonitorPart;

/**
 * <p>An editor page to be used when you need to load data (with the editors controller)
 * in the background and want to provide progress feedback to the user</p>
 * 
 * <p>The page hooks a Composite with a stack layout into its parent Form. 
 * One entry in the stack will be an implemenation of {@link IProgressMonitor}
 * the other the page's actual content. You can switch the vision 
 * by {@link #switchToContent()} and {@link #switchToProgress()}.</p> 
 * 
 * 
 * <p>On the creation of its contents ({@link #createFormContent(IManagedForm)}) 
 * this FormPage will start a job that tries to access the
 * {@link IEntityEditorPageController} associated to this page. It will herefore
 * assume that the page is embedded in an {@link EntityEditor} and a page 
 * controller was created by the {@link IEntityEditorPageFactory} of this
 * page. <br/>
 * Within the job the page will load the controllers data. if a controller was found.
 * Then it will call the {@link #asyncCallback()} method still on the jobs thread,
 * to let implementations react on to the loading. Implementations could fill the gui with 
 * the obtained data, for example. The callback is on the jobs thread, however, 
 * in order to enable subclasses to extend the background job with own tasks. 
 * Implementators should also not forget to switch to the content view when 
 * the loading is finished.</p>
 * 
 * <p>Also on creation of its contents this FormPage will call several 
 * methods that are intended to override or obliged to implement. 
 * These methods let subclasses create the page's actual content and 
 * configure the wrapping elements of this page. <br/>
 * Note, that its recommended when using this class to use parts ({@link Section}s)
 * that do not access the controller when they are created, but in contrast
 * have the ability to be "filled" with data from the {@link #asyncCallback()}.   
 * </p>
 * 
 *
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 */
public abstract class EntityEditorPageWithProgress extends FormPage
{

	/**
	 * Wrapper that holds the stack layout.
	 */
	private XComposite wrapper;
//	private Composite pageWrapper;
	/**
	 * Wrapper for the page's real content
	 */
	protected ScrolledForm pageWrapper;
	/**
	 * Wrapper for the progress monitor
	 */	
	protected XComposite progressWrapper;
	/**
	 * the progress monitor (used implementation {@link SaveProgressMonitorPart}).
	 */
	protected IProgressMonitor progressMonitorPart;
	/**
	 * The stack layout to switch views.
	 */
	private StackLayout stackLayout;
	
	/**
	 * Create a new editor page with progress.
	 * 
	 * @param editor The page's editor.
	 * @param id The page's id.
	 * @param name The page's name.
	 */
	public EntityEditorPageWithProgress(FormEditor editor, String id, String name) {
		super(editor, id, name);
	}
	
	/**
	 * The job that loads with the help of this page's controller.
	 * If the controller is an instance of {@link EntityEditorPageController}
	 * the job will use its special method to join the background-loading-job that
	 * might already run.
	 * After loading the job will call {@link #asyncCallback()} to notify 
	 * the page of its end.
	 */
	private Job asyncLoadJob = new Job("Async load") {
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			IEntityEditorPageController controller = getPageController();
			if (controller != null) {
				CompoundProgressMonitor compoundMonitor = new CompoundProgressMonitor(progressMonitorPart, monitor);
				if (controller instanceof EntityEditorPageController)
					((EntityEditorPageController)controller).load(compoundMonitor);
				else
					controller.doLoad(compoundMonitor);
			}
			asyncCallback();
			return Status.OK_STATUS;
		}
	};

	/**
	 * This method is called <b>on the loading job's thread</b> after 
	 * the loading was done. It is intended to be used in order
	 * to fill the page with the data now loaded.
	 * Remember that gui operations have to be done on the {@link Display} thread.
	 * Also implementations might want to {@link #switchToContent()} at
	 * the end of this method.
	 */
	protected abstract void asyncCallback();
	
	
	/**
	 * Get the {@link IEntityEditorPageController} for this page.
	 * Assumes the editor of this page is an instance of
	 * {@link EntityEditor}.
	 * 
	 * @return The page controller for this page.
	 */
	public IEntityEditorPageController getPageController() {
		return ((EntityEditor)getEditor()).getPageController(this);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * This implementation will create a stack layout an put a progress monitor 
	 * and the real content wrapped into a new Form on the stack.
	 * Besides {@link #addSections(Composite)}, where subclasses add their real content,
	 * several callback methods exist to configure the rest of the page.
	 *  
	 * @see #addSections(Composite)
	 * @see #createProgressMonitorPart(Composite)
	 * @see #configureBody(Composite)
	 * @see #configureProgressWrapper(XComposite)
	 * @see #configurePageWrapper(Composite)	 * 
	 * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createFormContent(IManagedForm managedForm)
	{
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText(getFormPageTitle()); 
		fillBody(managedForm, toolkit);
	}

	/**
	 * This method is called when the page's body is created.
	 * Add the page's sections here. Remember not to 
	 * access the controllers data here, but provide the possibility
	 * to assign the data to the part created at a later time.
	 * 
	 * @param parent The parent given here is a new Form created for the page and configured by {@link #configurePageWrapper(Composite)}.
	 */
	protected abstract void addSections(Composite parent);

	/**
	 * Return the title of the form page
	 * @return the title of the form page
	 */
	protected abstract String getFormPageTitle();

	/**
	 * Returns the progress monitor of this page.
	 * The progress monitor is created by {@link #createProgressMonitorPart(Composite)}
	 * so this getter is not intended to be overridden.
	 * 
	 * @return the progress monitor of this page.
	 */
	public IProgressMonitor getProgressMonitor() {
		return progressMonitorPart;
	}

	/**
	 * Configure (the layout of) the composite wrapping the progress monitor.
	 * 
	 * @param pWrapper The composite wrapping the progress monitor.
	 */
	protected void configureProgressWrapper(XComposite pWrapper) {
		pWrapper.getGridLayout().marginLeft = 5;
	}
	
	/**
	 * Create the gui representation of an {@link IProgressMonitor}
	 * to the given parent. This method is intended to be overridden,
	 * the default implementation will use an {@link SaveProgressMonitorPart}. 
	 * 
	 * @param progressWrapper The parent of the progress monitor. 
	 * @return A new gui representation of an {@link IProgressMonitor}.
	 */
	protected IProgressMonitor createProgressMonitorPart(Composite progressWrapper) {
		return new SaveProgressMonitorPart(progressWrapper, null);		
	}
	
	/**
	 * Configure the (layout of the) Composite wrapping the page's
	 * real content.
	 * The default implementation will assign a {@link GridLayout}
	 * with moderate indenting.
	 * 
	 * @param parent The Composite wrapping the page's
	 * real content. 
	 */
	protected void configurePageWrapper(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginBottom = 10;
		layout.marginTop = 5;
		layout.marginLeft = 5;
		layout.marginRight = 5;
		layout.numColumns = 1;
		layout.horizontalSpacing = 10;
		parent.setLayout(layout);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));
	}
	
	/**
	 * Configure (the layout of) this page's body (it's form's body).
	 * The default implementation will assign a
	 * {@link GridLayout} with zero margins and spacing.
	 * 
	 * @param body This page's body.
	 */
	protected void configureBody(Composite body) {
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginLeft = 0;
		layout.marginRight = 0;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		body.setLayout(layout);
		body.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	/**
	 * Configure what view should be visible initally, progress or content.
	 * Default implementation will set progress.
	 */
	protected void configureInitialStack() {
		stackLayout.topControl = progressWrapper;
	}

	/**
	 * Creates stack layout, progress-stack-item and the page's content
	 * with the help of the configure and create callbacks. 
	 * 
	 * @param managedForm The managed form
	 * @param toolkit The tookit to use
	 */
	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) 
	{
		Composite body = managedForm.getForm().getBody();
		configureBody(body);
		
		wrapper = new XComposite(body, SWT.NONE, XComposite.LayoutMode.TIGHT_WRAPPER);
		stackLayout = new StackLayout();
		stackLayout.marginHeight = 0;
		stackLayout.marginWidth = 0;
		wrapper.setLayout(stackLayout);
		
		progressWrapper = new XComposite(wrapper, SWT.NONE, XComposite.LayoutMode.ORDINARY_WRAPPER);
		configureProgressWrapper(progressWrapper);		
		progressMonitorPart = createProgressMonitorPart(progressWrapper);
		
		pageWrapper = managedForm.getToolkit().createScrolledForm(wrapper);
		configurePageWrapper(pageWrapper.getBody());
		
		asyncLoadJob.schedule();		
		
		addSections(pageWrapper.getBody());
		configureInitialStack();
	}
	
	
	/**
	 * Switch to view the progress monitor.
	 * Note that this will always be called asynchronously on the {@link Display} thread.
	 */
	public void switchToProgress() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				stackLayout.topControl = progressWrapper;
				wrapper.layout(true, true);
			}
		});
	}
	
	/**
	 * Switch to view the page's content.
	 * Note that this will always be called asynchronously on the {@link Display} thread.
	 */
	public void switchToContent() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				stackLayout.topControl = pageWrapper;
				wrapper.layout(true, true);
				pageWrapper.reflow(true);
				getManagedForm().getForm().redraw();
				getManagedForm().getForm().getBody().layout(true, true);
//				pageWrapper.refresh();
			}
		});
	}
}
