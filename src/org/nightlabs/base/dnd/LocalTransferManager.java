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

package org.nightlabs.base.dnd;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Bieber <alex[AT]nightlabs[DOT]de>
 *
 */
public class LocalTransferManager {

	/**
	 * key String: objectkey
	 * value SoftReferene: A SoftReferene to the object held here
	 */
	private Map transferObjects = new HashMap();
	
	public LocalTransferManager() {
		super();
	}
	
	
	public String addObject(Object object) {
		synchronized (transferObjects) {
			String key = Integer.toHexString(object.hashCode())+"_"+Long.toHexString(System.currentTimeMillis());
			while (transferObjects.containsKey(key))
				key = Integer.toHexString(object.hashCode())+"_"+Long.toHexString(System.currentTimeMillis());
			
			WeakReference softReference = new WeakReference(object);
			
			transferObjects.put(key, softReference);
			return key;
		}
	}
	
	public Object popObject(String key) {
		synchronized (transferObjects) {
			Object result = getObject(key);
			transferObjects.remove(key);
			return result;
		}
	}

	public Object getObject(String key) {
		synchronized (transferObjects) {
			
			WeakReference softReference = (WeakReference) transferObjects.get(key);
			if (softReference == null)
				throw new IllegalArgumentException("Could not find a Object for the given key "+key);

			if (softReference.get() == null)
				throw new IllegalStateException("No reference to the Object with key "+key+" exists any more the SoftReference was cleared");

			return softReference.get();
		}
	}
	
	
	private static LocalTransferManager sharedInstance;
	
	public static LocalTransferManager sharedInstance() {
		if (sharedInstance == null)
			sharedInstance = new LocalTransferManager();
		return sharedInstance;
	}
	
}
