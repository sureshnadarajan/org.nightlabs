/* ********************************************************************
 * NightLabsBase - Utilities by NightLabs                             *
 * Copyright (C) 2004-2008 NightLabs GmbH - http://NightLabs.org      *
 *                                                                    *
 * This library is free software; you can redistribute it and/or      *
 * modify it under the terms of the GNU Lesser General Public         *
 * License as published by the Free Software Foundation; either       *
 * version 2.1 of the License, or (at your option) any later version. *
 *                                                                    *
 * This library is distributed in the hope that it will be useful,    *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of     *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU  *
 * Lesser General Public License for more details.                    *
 *                                                                    *
 * You should have received a copy of the GNU Lesser General Public   *
 * License along with this library; if not, write to the              *
 *     Free Software Foundation, Inc.,                                *
 *     51 Franklin St, Fifth Floor,                                   *
 *     Boston, MA  02110-1301  USA                                    *
 *                                                                    *
 * Or get it online:                                                  *
 *     http://www.gnu.org/copyleft/lesser.html                        *
 **********************************************************************/
package org.nightlabs.job;

import java.util.Collection;
import java.util.HashSet;

import org.nightlabs.config.ConfigModule;

/**
 * A job's configuration module. Usage of this config module is not
 * needed. Jobs can be configured by directly using
 * {@link JobConfigEntry} of {@link JobExecuterEntry}.
 *
 * @author Marc Klinger - marc[at]nightlabs[dot]de
 */
public class JobConfig extends ConfigModule
{
	/**
	 * The serial version for this class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The job entries in this config module.
	 */
	protected Collection<JobConfigEntry> entries = null;

	/**
	 * Get the job entries.
	 * @return the job entries
	 */
	public Collection<JobConfigEntry> getEntries()
	{
		return entries;
	}

	/**
	 * Set the job entries
	 * @param entries the job entries to set
	 */
	public void setEntries(Collection<JobConfigEntry> entries)
	{
		for (JobConfigEntry entry : entries)
			entry.config = this;
		this.entries = entries;
		setChanged();
	}

	/**
	 * Add a jobb entry to the list of entries.
	 * @param entry the entry to add
	 */
	public void addEntry(JobConfigEntry entry)
	{
		if(entries == null)
			entries = new HashSet<JobConfigEntry>(1);
		entry.config = this;
		entries.add(entry);
		setChanged();
	}

	/**
	 * Remove a job entry from the list of entries.
	 * @param entry the entry to remove
	 */
	public void removeEntry(JobConfigEntry entry)
	{
		if(entries != null)
			entries.remove(entry);
		setChanged();
	}
}
