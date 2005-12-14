/**
 * 
 */
package com.nightlabs.rcp.action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.nightlabs.rcp.composite.XComposite;
import com.nightlabs.rcp.custom.ColorCombo;

/**
 * A ContributionItem for a ToolBar showing a defined List of items.
 * 
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 */
public abstract class ComboContributionItem extends XContributionItem {

	private XComposite wrapper;
	private ColorCombo combo;
	private List entries;

	private boolean updating;
	/**
	 * 
	 */
	public ComboContributionItem() {
		super();
		this.entries = getEntries();
	}

	/**
	 * @param id
	 */
	public ComboContributionItem(String id) {
		super(id);
		this.entries = getEntries();
	}

	/**
	 * Returns a list of keys that should be made selectable.
	 */
	protected abstract List getEntries();
	
	/**
	 * Returns a short description for a given entry (key).
	 */
	protected abstract String getText(Object entry);

	/**
	 * Returns a icon Image for a given entry (key).
	 */
	protected abstract Image getImage(Object entry);

	/**
	 * Creates the actual Control of this contribution item
	 */
	protected Control createControl(Composite parent) 
	{
//		wrapper = new XComposite(parent, SWT.NONE, XComposite.LAYOUT_MODE_TIGHT_WRAPPER, XComposite.LAYOUT_DATA_MODE_NONE);
//		wrapper.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		combo = new ColorCombo(parent, SWT.READ_ONLY);
		combo.setSize(200, 30);
		
		for (Iterator iter = nullAddedSelListener.iterator(); iter.hasNext();) {
			combo.addSelectionListener((SelectionListener) iter.next());
		}
		nullAddedSelListener.clear();

		for (Iterator iter = entries.iterator(); iter.hasNext();) {
			Object entry = (Object) iter.next();
			combo.add(getImage(entry), getText(entry));
		}
		if (entries.size() > 0)
			combo.select(0);
//		return wrapper;
		return combo;
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.Composite)
	 */
	public void fill(Composite parent) {
		createControl(parent);
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.ToolBar, int)
	 */
	public void fill(ToolBar parent, int index) {
		ToolItem toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
		Control control = createControl(parent);
		toolitem.setWidth(100);
		toolitem.setControl(control);	
	}
	
	/**
	 * Returns the Combo control
	 * @return
	 */
	public ColorCombo getCombo() {
		return combo;
	}

	/**
	 * Returns the selected entry within the list passed by {@link #getEntries()}.
	 */
	public Object getSelectedItem() {
		if (combo == null)
			return null;
		else 
			if (combo.getSelectionIndex() >= 0 && combo.getSelectionIndex() < entries.size())
				return entries.get(combo.getSelectionIndex());
			else 
				return null;
	}
	
	/**
	 * Set the selection of the Combo
	 */
	public void setSelectedItem(Object entry) {
		for (int i = 0; i < entries.size(); i++) {			
			if (entries.get(i).equals(entry)) {
				updating = true;
				try {
					combo.select(i);
				} finally {
					updating = false;
				}
			}
		}
	}
		
	public boolean isUpdating() {
		return updating;
	}
	
	private List nullAddedSelListener = new LinkedList();
	
	/**
	 * Call this to add a SelectionListener to the Combo. This is neccessary
	 * because the Combo Control is created lazy. This method either delegates
	 * to the Combo if it is already created or collects all listeners until 
	 * it will be created and adds them then. To remove the listener call
	 * {@link #removeSelectionListener(SelectionListener)}.
	 */
	public void addSelectionListener(SelectionListener listener) {
		if (combo != null)
			combo.addSelectionListener(listener);
		else
			nullAddedSelListener.add(listener);
	}
	
	/**
	 * Remove a SelectionListener from the Combo.
	 * @see #addSelectionListener(SelectionListener)
	 */
	public void removeSelectionListener(SelectionListener listener) {
		if (combo != null)
			combo.removeSelectionListener(listener);
		else
			nullAddedSelListener.remove(listener);
	}
}
