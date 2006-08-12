/**
 * 
 */
package org.nightlabs.base.print;

import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.nightlabs.base.NLBasePlugin;
import org.nightlabs.base.composite.XComposite;

/**
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 *
 */
public class PrinterConfiguratorComposite extends XComposite {

	private Group printerGroup;
	private Label printerName;
	private Button selectPrinterButton;
	
	private Group pageFormatGroup;
	private Label pageFormatDescription;
	private Button editPageFormat;
	
	private PageFormat pageFormat;
	
	private PrinterJob printerJob;
	
	/**
	 * @param parent
	 * @param style
	 */
	public PrinterConfiguratorComposite(Composite parent, int style) {
		super(parent, style);
		initGUI(parent);
	}

	/**
	 * @param parent
	 * @param style
	 * @param layoutMode
	 */
	public PrinterConfiguratorComposite(Composite parent, int style,
			LayoutMode layoutMode) {
		super(parent, style, layoutMode);
		initGUI(parent);
	}

	/**
	 * @param parent
	 * @param style
	 * @param layoutDataMode
	 */
	public PrinterConfiguratorComposite(Composite parent, int style,
			LayoutDataMode layoutDataMode) {
		super(parent, style, layoutDataMode);
		initGUI(parent);
	}

	/**
	 * @param parent
	 * @param style
	 * @param layoutMode
	 * @param layoutDataMode
	 */
	public PrinterConfiguratorComposite(Composite parent, int style,
			LayoutMode layoutMode, LayoutDataMode layoutDataMode) {
		super(parent, style, layoutMode, layoutDataMode);
		initGUI(parent);
	}

	/**
	 * @param parent
	 * @param style
	 * @param layoutMode
	 * @param layoutDataMode
	 * @param cols
	 */
	public PrinterConfiguratorComposite(Composite parent, int style,
			LayoutMode layoutMode, LayoutDataMode layoutDataMode, int cols) {
		super(parent, style, layoutMode, layoutDataMode, cols);
		initGUI(parent);
	}

	private void initGUI(Composite parent) {
		printerGroup = new Group(this, SWT.NONE);
		printerGroup.setText(NLBasePlugin.getResourceString("dialog.printerConfiguration.default.printerGroup"));
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		printerGroup.setLayout(gl);
		printerGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		printerName = new Label(printerGroup, SWT.WRAP);
		printerName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		selectPrinterButton = new Button(printerGroup, SWT.PUSH);
		selectPrinterButton.setText(NLBasePlugin.getResourceString("dialog.printerConfiguration.default.choosePrinter"));
		selectPrinterButton.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				PrinterJob printerJob = getPrinterJob();
				PrintService printService = PrintUtil.lookupPrintService(printerName.getText());
				if (printService != null)
					try {
						printerJob.setPrintService(printService);
					} catch (PrinterException e) {
						throw new RuntimeException(e);
					}
				if (printerJob.printDialog()) {
					printerName.setText(printerJob.getPrintService().getName());
				}
			}
		});
		
		pageFormatGroup = new Group(this, SWT.NONE);
		pageFormatGroup.setText(NLBasePlugin.getResourceString("dialog.printerConfiguration.default.pageFormatGroup"));
		GridLayout gl1 = new GridLayout();
		gl1.numColumns = 2;
		pageFormatGroup.setLayout(gl1);
		pageFormatGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pageFormatDescription = new Label(pageFormatGroup, SWT.WRAP);
		pageFormatDescription.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		editPageFormat = new Button(pageFormatGroup, SWT.PUSH);		
		editPageFormat.setText(NLBasePlugin.getResourceString("dialog.printerConfiguration.default.editPageFormat"));
		editPageFormat.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
			public void widgetSelected(SelectionEvent arg0) {
				PrinterJob printerJob = getPrinterJob();
				PageFormat newPageFormat;
				if (PrinterConfiguratorComposite.this.pageFormat != null)
					newPageFormat = printerJob.pageDialog(PrinterConfiguratorComposite.this.pageFormat);
				else
					newPageFormat = printerJob.pageDialog(printerJob.defaultPage());
				PrinterConfiguratorComposite.this.pageFormat = newPageFormat;
				pageFormatDescription.setText(getPageFormatDescription(PrinterConfiguratorComposite.this.pageFormat));
			}
		});
	}
	
	public void init(PrinterConfiguration printerConfiguration) {
		if (printerConfiguration != null && printerConfiguration.getPrintServiceName() != null)
			printerName.setText(printerConfiguration.getPrintServiceName());
		else
			printerName.setText(NLBasePlugin.getResourceString("dialog.printerConfiguration.default.noPrinterAssigned"));

		if (printerConfiguration != null) {
			this.pageFormat = printerConfiguration.getPageFormat();
			pageFormatDescription.setText(getPageFormatDescription(printerConfiguration.getPageFormat()));
		}
		if (printerConfiguration == null || printerConfiguration.getPageFormat() == null)
			pageFormatDescription.setText(NLBasePlugin.getResourceString("dialog.printerConfiguration.default.noPageFormatAssigned"));
		
	}
	
	public PrinterConfiguration readPrinterConfiguration() {
		PrinterConfiguration configuration = new PrinterConfiguration();
		configuration.setPrintServiceName(printerName.getText());
		configuration.setPageFormat(pageFormat);
		return configuration;
	}
	
	private PrinterJob getPrinterJob() {
		if (printerJob == null)
			printerJob = PrinterJob.getPrinterJob();
		return printerJob;
	}
	
	private String getPageFormatDescription(PageFormat pageFormat) {
		if (pageFormat == null)
			return NLBasePlugin.getResourceString("dialog.printerConfiguration.default.noPageFormatAssigned");
		else
			return NLBasePlugin.getResourceString("dialog.printerConfiguration.default.customPageFormat");
	}
}
