/*
 * Created 	on Jan 7, 2005
 * 					by alex
 *
 */
package com.nightlabs.rcp.composite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Slider;

/**
 * Takes SelectableComposite s and display them in a
 * vertically fixed but horizontal expandable list.
 * A column number can be defined for the viewport.
 * All composites will be set to the width needed
 * to fill the whole column but keep their height. 
 * 
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public class HorizontalMultiColumnCompositeList extends Composite implements ISelectionProvider {

	private List children = new ArrayList();
	private Set selectedChildren = new HashSet();
	/**
	 * SelectionListener to add and remove to selectedChildren 
	 */
	private SelectableCompositeListener selectionChangeListener = new SelectableCompositeListener() {
		public void selectionStateChanged(CompositeSelectionEvent evt) {
			if (preserveRecursion)
				return;
			if (!multiSelect) {
				exclusiveSelect(evt.getSource(),evt.isSelected());
			}
			else {
				boolean ctrlPressed = (evt.getStateMask() & SWT.CTRL) > 0;
				if (!ctrlPressed)
					exclusiveSelect(evt.getSource(),evt.isSelected());
				else {
					if (evt.isSelected()) {
						if (!selectedChildren.contains(evt.getSource()))
							selectedChildren.add(evt.getSource());
						else
							selectedChildren.remove(evt.getSource());
					}
					else
						selectedChildren.remove(evt.getSource());
				}
			}
		}
		public void compositeDoubleClicked(SelectableComposite composite) {
		}
	};
	
	/**
	 * SelectionListener to the slider.
	 */
	private SelectionListener scrollListener = new SelectionListener() {
		public void widgetSelected(SelectionEvent evt) {
			scrollTo(slider.getSelection());
		}
		public void widgetDefaultSelected(SelectionEvent arg0) {
			// not called
		}
	};
	
	
	private ControlListener coltrolListener = new ControlListener(){
		public void controlMoved(ControlEvent arg0) {
			// don't care
		}
		public void controlResized(ControlEvent arg0) {
			HorizontalMultiColumnCompositeList.this.layout();
			refreshList();
		}
	};
	
	private Set keySet = new HashSet();
	private KeyListener keyListener = new KeyListener() {
		public void keyPressed(KeyEvent evt) {
			keySet.add(new Integer(evt.keyCode));
			System.out.println("Key pressed for CompList : Key Code is: "+evt.keyCode);
		}
		public void keyReleased(KeyEvent evt) {
			keySet.remove(new Integer(evt.keyCode));
		}		
	};
	
	private int numColumns = 1;
	private int verticalSpacing = 5;
	private int verticalMargin = 5;
	private int horizontalSpacing = 5;
	private boolean multiSelect = false;
	private Composite carrier;

	private XComposite wrapper;
	private Composite carrierWrapper;
	private Slider slider;
	
	
	/**
	 * @param parent
	 * @param style
	 */
	public HorizontalMultiColumnCompositeList(Composite parent, int style) {
		super(parent, style);
		this.setLayout(new GridLayout());
		wrapper = new XComposite(this, SWT.NONE, XComposite.LAYOUT_MODE_TIGHT_WRAPPER);

		carrierWrapper = new Composite(wrapper,SWT.NONE);
		carrierWrapper.setLayout(null);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		carrierWrapper.setLayoutData(gd);
		
		carrier = new Composite(carrierWrapper,SWT.NONE);
		carrier.setLayout(null);
		
		slider = new Slider(wrapper,SWT.HORIZONTAL);
		GridData sliderGD = new GridData();
		sliderGD.grabExcessHorizontalSpace = true;
		sliderGD.horizontalAlignment = GridData.FILL;
		slider.setLayoutData(sliderGD);
		slider.setVisible(true);
		slider.addSelectionListener(scrollListener);
		this.addControlListener(coltrolListener);
		this.addKeyListener(keyListener);
	}
	
	/**
	 * Create and add a SelectableComposite
	 * @param style
	 * @return
	 */
	public SelectableComposite createChild(int style) {
		SelectableComposite child = new SelectableComposite(getCarrier(),style);
		addChild(child);
		return child;
	}
	
	/**
	 * Add a child. 
	 * @param child
	 */
	public void addChild(SelectableComposite child) {
		child.compositeListIdx = children.size();
		child.addSelectionChangeListener(selectionChangeListener);
		children.add(child);
	}
	
	/**
	 * Add a child at the specified position.
	 * 
	 * @param child
	 * @param idx
	 */
	public void addChild(SelectableComposite child, int idx) {
		child.compositeListIdx = idx;
		child.addSelectionChangeListener(selectionChangeListener);
		children.add(idx,child);
		for (int i=idx+1; i<children.size(); i++){
			SelectableComposite comp = (SelectableComposite)children.get(i);
			comp.compositeListIdx = i;
		}
	}
	
	/**
	 * Remove the child at the specified index.
	 * If dispose id true the Composite will be disposed as well.
	 *  
	 * @param idx
	 * @param dispose
	 */
	public void removeChild(int idx, boolean dispose) {
		selectedChildren.remove(children.get(idx));
		if (dispose) {
			((SelectableComposite)children.get(idx)).dispose();
		}
		((SelectableComposite)children.get(idx)).removeSelectionChangeListener(selectionChangeListener);
		children.remove(idx);
		for (int i=idx; i<children.size(); i++) {
			SelectableComposite comp = (SelectableComposite)children.get(i);
			comp.compositeListIdx = i;
		}
	}
	
	/**
	 * Will remove and dispose the child.
	 *  
	 * @param idx
	 */
	public void removeChild(int idx) {
		removeChild(idx,true);
	}
	
	/**
	 * Remove the given child and dispose it if dispose it true.
	 * 
	 * @param child
	 * @param dispose
	 */
	public void removeChild(SelectableComposite child, boolean dispose) {
		removeChild(child.getCompositeListIdx(), dispose);
	}
	
	/**
	 * Remove the first child with the given selectionObject and dispose it if dispose it true.
	 * 
	 * @param selectionObject
	 * @param dispose
	 */
	public void removeChildBySelectionObject(Object selectionObject, boolean dispose) {
		SelectableComposite childToRemove = null;
		if (selectionObject == null)
			return;
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			SelectableComposite child = (SelectableComposite) iter.next();
			if (selectionObject.equals(child.getSelectionObject())) {
				childToRemove = child;
				break;
			}
		}
		if (childToRemove != null)
			removeChild(childToRemove, dispose);
	}

	/**
	 * Remove all children. If dispose is true
	 * they will be disposed as well.
	 * 
	 * @param dispose
	 */
	public void removeAll(boolean dispose) {
		for (int i=children.size()-1; i>=0; i--){
			removeChild(i,dispose);
		}
	}
	
	/**
	 * Remove and dispose all children. 
	 */
	public void removeAll() {
		removeAll(true);
	}
	
	/**
	 * Replace the child at replacedIdx with the Composite
	 * passed. If disposeReplaced is true the replaced Composite
	 * will be disposed.
	 * 
	 * @param replacedIdx
	 * @param replacing
	 * @param disposeReplaced
	 */
	public void replaceChild(int replacedIdx, SelectableComposite replacing, boolean disposeReplaced) {
		selectedChildren.remove(children.get(replacedIdx));
		if (disposeReplaced)
			((SelectableComposite)children.get(replacedIdx)).dispose();
		((SelectableComposite)children.get(replacedIdx)).removeSelectionChangeListener(selectionChangeListener);
		replacing.compositeListIdx = replacedIdx;
		replacing.addSelectionChangeListener(selectionChangeListener);
		children.set(replacedIdx,replacing);
	}
	
	/**
	 * Replace the child replaced with the Composite replacing. 
	 * If disposeReplaced is true replaced will be disposed.
	 * 
	 * @param replaced
	 * @param replacing
	 * @param disposeReplaced
	 */
	public void replaceChild(SelectableComposite replaced, SelectableComposite replacing, boolean disposeReplaced) {
		replaceChild(replaced.getCompositeListIdx(),replacing,disposeReplaced);
	}
	
	/**
	 * Get the current set number of columns.
	 * @return
	 */
	public int getNumColumns() {
		return numColumns;
	}
	/**
	 * Set the number of columns. Default is 1.
	 * @param numColumns
	 */
	public void setNumColumns(int numColumns) {
		this.numColumns = numColumns;
	}
	
	/**
	 * Check if items in this list are 
	 * multiselectable.
	 * 
	 * @return
	 */
	public boolean isMultiSelect() {
		return multiSelect;
	}
	
	/**
	 * Set if items in this list are multiselectable.
	 * 
	 * @param multiSelect
	 */
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	
	/**
	 * Returns the carrier for all children.
	 * A child should be created with this
	 * as a parent or use {@link #createChild(int)}.
	 *  
	 * @return
	 */
	public Composite getCarrier() {
		return carrier;
	}
	
	private int columnScrollInc;
	private int currentScrollColumn;
	private int totalNumColumns;
	
	/**
	 * Rearranges the children of this list
	 * and redraws the composite.
	 * 
	 */
	protected void arrangeChildren() {
		int carrierHeight = carrierWrapper.getSize().y;
		int width = carrierWrapper.getSize().x;
		int columnWidth = (width - (2*horizontalSpacing)) / numColumns;
		int compositeWidth = columnWidth - (horizontalSpacing / 2);
		columnScrollInc = columnWidth + (horizontalSpacing / 2);
		int yRun = verticalMargin;
		int xRun = horizontalSpacing;
		totalNumColumns = 1;
		carrier.setSize(0,carrierHeight);
		
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			SelectableComposite comp = (SelectableComposite) iter.next();
			int compHeight = comp.getSize().y;
			int compWidth = columnWidth - 2 * horizontalSpacing;
			
			if ((yRun+compHeight > carrierHeight-(2*verticalMargin)) && (yRun != verticalMargin)) {
				// if composite doesn't fit into column and 
				// is not the first comp in this column then
				xRun += columnScrollInc;
				yRun = verticalMargin;
				totalNumColumns ++;
			}
			comp.setBounds(xRun,yRun,compWidth,compHeight);
			yRun += compHeight + verticalSpacing;
		}		
		slider.setVisible(totalNumColumns>numColumns);
		slider.setMinimum(0);
		slider.setMaximum(totalNumColumns-(numColumns-1));
		slider.setSelection(currentScrollColumn);
		slider.setIncrement(1);
		slider.setThumb(1);
		slider.setPageIncrement(1);
		carrier.setSize((totalNumColumns + 1)*columnScrollInc, carrierHeight);
		scrollTo(currentScrollColumn);
		this.redraw();
	}
	
	/**
	 * Scrolls the List to the given columnIndex but
	 * only until (totalColumnCount - numColumns) is reached.
	 * 
	 * @param columnScrollIdx
	 */
	protected void scrollTo(int columnScrollIdx) {
		if (columnScrollIdx > totalNumColumns - numColumns) {
			currentScrollColumn = totalNumColumns - numColumns;
			slider.setSelection(currentScrollColumn);
		}
		else 
			currentScrollColumn = columnScrollIdx;
	
		if (currentScrollColumn < 0)
			currentScrollColumn = 0;
		carrier.setLocation(0-(currentScrollColumn*columnScrollInc),0);
		
		if ((slider.getSelection()) != currentScrollColumn) {
			slider.setSelection(currentScrollColumn);
		}
		this.redraw();
	}
	
	/**
	 * Rearranges the children and the scrollbar.
	 *
	 */
	public void refreshList() {
		arrangeChildren();
	}
	
	private boolean preserveRecursion = false;
	/**
	 * Clears the Map of selected Composites 
	 * after setting the selection state of all
	 * entries to false. 
	 */
	private void clearSelectionMap(SelectableComposite causingComposite) {
		preserveRecursion = true;
		try {
			for (Iterator iter = selectedChildren.iterator(); iter.hasNext();) {
				SelectableComposite comp = (SelectableComposite) iter.next();
				if (comp != causingComposite)
					comp.setSelected(false);
			}
			selectedChildren.clear();
		} finally {
			preserveRecursion = false;
		}
	}
	
	private void exclusiveSelect(SelectableComposite selection, boolean selected) {
		clearSelectionMap(selection);
		if (selected)
			selectedChildren.add(selection);
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		throw new UnsupportedOperationException("Not implemented yet. Contact alex[at]nightlabs[dot]de.");		
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		throw new UnsupportedOperationException("Not implemented yet. Contact alex[at]nightlabs[dot]de.");		
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		throw new UnsupportedOperationException("Not implemented yet. Contact alex[at]nightlabs[dot]de.");
	}

	/**
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		Object[] sel = new Object[selectedChildren.size()];
		Object[] comps = selectedChildren.toArray();
		for (int i=0; i<comps.length; i++) {
			Object selObj = (Object) ((SelectableComposite)comps[i]).getSelectionObject();
			sel[i] = selObj;
		}
		ISelection result = new StructuredSelection(sel);		
		return result;
	}
}
