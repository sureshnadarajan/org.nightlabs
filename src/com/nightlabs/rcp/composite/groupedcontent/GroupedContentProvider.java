/*
 * Created 	on Jun 17, 2005
 * 					by alex
 *
 */
package com.nightlabs.rcp.composite.groupedcontent;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public interface GroupedContentProvider {	
	public Image getGroupIcon();
	public String getGroupTitle();
	public Composite createGroupContent(Composite parent);
}
