/**
 * 
 */
package org.nightlabs.base.exceptionhandler;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * @author tobias
 *
 */
public class TestExceptionAction extends Action implements IWorkbenchWindowActionDelegate {

	/**
	 * 
	 */
	public TestExceptionAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		Thread thread1 = new Thread() {
			public void run()
			{
				try
				{
					Thread.sleep(1000);
				}
				catch(Exception e) { }
				throw new RuntimeException(new IllegalStateException("Test test1 ..."));
			}
		};
		
		Thread thread2 = new Thread() {
			public void run()
			{
				try
				{
					Thread.sleep(4000);
				}
				catch(Exception e) { }
				throw new RuntimeException(new IllegalStateException("Test test2 ..."));
			}
		};
		
		Thread thread3 = new Thread() {
			public void run()
			{
				try
				{
					Thread.sleep(7000);
				}
				catch(Exception e) { }
				throw new RuntimeException(new IllegalStateException("Test test3 ..."));
			}
		};
		
		thread1.start();
		thread2.start();
		thread3.start();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
