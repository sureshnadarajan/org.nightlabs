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

package org.nightlabs.base.composite.groupedcontent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import org.nightlabs.base.composite.XComposite;

/**
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public class GroupedContentComposite extends XComposite {
	
	private XComposite tableWrapper;
	private GroupedContentSwitcherTable switcherTable;
	
	private Composite contentWrapper;
	private StackLayout contentStackLayout;
	
	private List groupedContentProvider = new ArrayList();
	private Map providerComposites = new HashMap(); 
	
	
	private ISelectionChangedListener switcherListener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection selection = (IStructuredSelection)switcherTable.getTableViewer().getSelection();
			if (selection.size() != 1)
				return;
			GroupedContentProvider contentProvider = (GroupedContentProvider)selection.getFirstElement();
			selectContentProvider(contentProvider);
		}
	};
	
	
	
	
	/**
	 * @param parent
	 * @param style
	 * @param setLayoutData
	 */
	public GroupedContentComposite(Composite parent, int style, boolean setLayoutData) {
		super(parent, style, XComposite.LAYOUT_MODE_ORDINARY_WRAPPER,
				setLayoutData ? XComposite.LAYOUT_DATA_MODE_GRID_DATA : LAYOUT_DATA_MODE_NONE);
		getGridLayout().numColumns = 2;

		tableWrapper = new XComposite(this, SWT.NONE, XComposite.LAYOUT_MODE_TIGHT_WRAPPER, XComposite.LAYOUT_DATA_MODE_NONE);
		GridData tableGD = new GridData(GridData.FILL_BOTH);
		tableGD.grabExcessHorizontalSpace = false;
		tableGD.widthHint = 150;
		tableWrapper.setLayoutData(tableGD);

		switcherTable = new GroupedContentSwitcherTable(tableWrapper, SWT.NONE);
		switcherTable.getTableViewer().addSelectionChangedListener(switcherListener);
		
		contentWrapper = new Composite(this, SWT.NONE);
		contentWrapper.setLayoutData(new GridData(GridData.FILL_BOTH));
		contentStackLayout = new StackLayout();		
		contentWrapper.setLayout(contentStackLayout);
	}
	
	
	public void addGroupedContentProvider(GroupedContentProvider groupedContentProvider) {
		this.groupedContentProvider.add(groupedContentProvider);
		switcherTable.setInput(this.groupedContentProvider);
		layout(true, true);
	}
	
	public void addGroupedContentProvider(GroupedContentProvider groupedContentProvider, int index) {
		this.groupedContentProvider.add(index, groupedContentProvider);
		switcherTable.setInput(this.groupedContentProvider);
		if (switcherTable.getTable().getItemCount() == 1) {
			switcherTable.getTable().select(0);
			selectContentProvider(groupedContentProvider);
		}
		layout(true, true);
	}

	protected void selectContentProvider(GroupedContentProvider contentProvider) {
		Composite providerComp = (Composite)providerComposites.get(contentProvider);
		if (providerComp == null) {
			providerComp = contentProvider.createGroupContent(contentWrapper);			
			providerComposites.put(contentProvider, providerComp);
		}
		contentStackLayout.topControl = providerComp;
		contentWrapper.layout();
	}
	
	public void setGroupTitle(String title) { 
		switcherTable.setGroupTitle(title);
	}
}
