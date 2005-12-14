/*
 * Created on Jun 13, 2005
 */
package com.nightlabs.rcp.wizard;

import java.util.List;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author Marco Schulze - marco at nightlabs dot de
 */
public interface IWizardHop
{
	/**
	 * @return Returns the wizard.
	 */
	IWizard getWizard();

	/**
	 * @return Returns either <tt>null</tt> or the parent {@link IWizardHop} if there
	 *		has been a branching and this is a child hop.
	 *
	 * @see #setParentHop(IWizardHop)
	 */
	IWizardHop getParentHop();

	/**
	 * This method sets the parent hop. It should be set automatically when the
	 * entryPage of this hop is set as exitPage of the parent.
	 *
	 * @param parentHop The hop from which we came.
	 */
	void setParentHop(IWizardHop parentHop);

	/**
	 * @param entryPage The entryPage to set.
	 *
	 * @see #getEntryPage()
	 */
	void setEntryPage(IWizardHopPage entryPage);

	/**
	 * @return Returns the entryPage. This page must be registered in the <tt>Wizard</tt>
	 *		and is not registered here as one of the hop pages.
	 *
	 * @see #setEntryPage(IWizardHopPage)
	 */
	IWizardHopPage getEntryPage();

	/**
	 * Call this method to fork out of the current hop into a new hop. Note, that
	 * the <tt>exitPage</tt> here is always identical with <tt>entryPage</tt> of
	 * the child hop. Hence, <tt>exitPage.getWizardHop()</tt> must return the
	 * child hop - NOT this one.
	 * <p>
	 * This method should call {@link #setParentHop(IWizardHop)} of the exitPage's
	 * hop like this: <tt>exitPage.getWizardHop().setParentHop(this);</tt>
	 * <p>
	 * You can set <tt>null</tt> as <tt>exitPage</tt>. If the <tt>exitPage</tt> is
	 * <tt>null</tt>, the wizard
	 *
	 * @param exitPage The exitPage to which shall be forked.
	 *
	 * @see #getExitPage()
	 */
	void setExitPage(IWizardHopPage exitPage);

	/**
	 * @return Returns either </tt>null</tt> or the <tt>entryPage</tt> of the next hop.
	 *		Note, that <tt>exitPage.getWizardHop() != this</tt>, because it is part of the
	 *		following child hop.
	 *
	 * @see #setExitPage(IWizardHopPage)
	 */
	IWizardHopPage getExitPage();

	/**
	 * @return Returns the first page after the entry page or null, if not existent.
	 *		Note that this is not the entryPage, but the first page AFTER the entry page.
	 */
	IWizardHopPage getFirstHopPage();

	/**
	 * @return Returns the last page of this hop or null, if no hop page existent.
	 *		Note that this is not the exitPage, but the last page BEFORE the exit page.
	 */
	IWizardHopPage getLastHopPage();

	/**
	 * This method adds a {@link IWizardHopPage} to the <tt>List</tt>. Neither the
	 * entry page, nor the exit page are registered as hop page!!!
	 * 
	 * @param page The new page to be added.
	 */
	void addHopPage(IWizardHopPage page);

	boolean removeHopPage(IWizardHopPage page);

	void removeAllHopPages();

	/**
	 * This methdo removes all pages after the given <tt>page</tt>, which will
	 * NOT be removed.
	 *
	 * @param page The reference page, after which all other pages are removed.
	 */
	void removeAllHopPagesAfter(IWizardHopPage page);

	/**
	 * @return Returns a read-only list of all hop pages (both, entry and exit
	 *		page, are both not contained).
	 */
	List getHopPages();

	IWizardPage getNextPage(IWizardPage currentPage);
	
	/**
	 * Registeres the entry page of this hop in the dynamic pages of the given
	 * wizard and sets the wizard to all WizardHopPages of this WizardHop 
	 * and all its child WizardHops.
	 * 
	 * @param wizard The wizard this WizardHop should be linked to
	 */
	void hookWizard(IDynamicPathWizard wizard);

	/**
	 * Unregisteres the entry page of this WizardHop from the dynamic pages 
	 * of the given wizard and sets the wizard of all WizardHopPages of this
	 * hop and its child hops to null.
	 * 
	 * @param wizard The wizard this WizardHop should be unregistered from
	 */
	void unhookWizard(IDynamicPathWizard wizard);
	
//	IWizardPage getPreviousPage(IWizardPage currentPage);
}