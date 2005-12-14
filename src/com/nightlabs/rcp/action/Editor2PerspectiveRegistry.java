/**
 * <p> Project: com.nightlabs.base </p>
 * <p> Copyright: Copyright (c) 2005 </p>
 * <p> Company: NightLabs GmbH (Germany) </p>
 * <p> Creation Date: 01.07.2005 </p>
 * <p> Author: Daniel Mazurek </p>
**/

package com.nightlabs.rcp.action;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;

import com.nightlabs.rcp.extensionpoint.AbstractEPProcessor;
import com.nightlabs.rcp.extensionpoint.EPProcessorException;

public class Editor2PerspectiveRegistry 
extends AbstractEPProcessor 
{
	public static final String EXTENSION_POINT_ID = "com.nightlabs.base.editor2perspective";
	
	public String getExtensionPointID() {
		return EXTENSION_POINT_ID;
	}

	private static Editor2PerspectiveRegistry sharedInstance;
	/**
	 * @deprecated Use {@link #sharedInstance()} instead
	 */
	public static Editor2PerspectiveRegistry getSharedInstance() {
		if (sharedInstance == null)
			sharedInstance = new Editor2PerspectiveRegistry();
		return sharedInstance;
	}
	
	public static Editor2PerspectiveRegistry sharedInstance() {
		if (sharedInstance == null)
			sharedInstance = new Editor2PerspectiveRegistry();
		return sharedInstance;
	}	
		
	protected Map editorID2PerspectiveID = new HashMap();
	public String getPerspectiveID(String editorID) 
	{
		checkProcessing();
		return (String) editorID2PerspectiveID.get(editorID);
	}
	
	public void processElement(IExtension extension, IConfigurationElement element)
	throws EPProcessorException 
	{
		if (element.getName().equalsIgnoreCase("registry")) 
		{
			String editorID = element.getAttribute("editorID");
			if (!checkString(editorID))
				throw new EPProcessorException("Element registry has to define attribute editorID.");
			
			String perspectiveID = element.getAttribute("perspectiveID");
			if (!checkString(perspectiveID))
				throw new EPProcessorException("Element registry has to define attribute perspectiveID.");
			
			editorID2PerspectiveID.put(editorID, perspectiveID);
		}
	}

	protected boolean checkString(String s) 
	{
		if (s == null || "".equals(s))
			return false;
		
		return true;
	}
	
}
