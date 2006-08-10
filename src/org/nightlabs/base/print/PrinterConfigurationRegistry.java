/**
 * 
 */
package org.nightlabs.base.print;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.nightlabs.base.extensionpoint.AbstractEPProcessor;
import org.nightlabs.base.extensionpoint.EPProcessorException;

/**
 * 
 * @author Alexander Bieber <!-- alex [AT] nightlabs [DOT] de -->
 *
 */
public class PrinterConfigurationRegistry extends AbstractEPProcessor {

	public static final String EXTENSION_POINT_ID = "org.nightlabs.base.printerConfiguration";
	
	public static class ConfiguratorFactoryEntry {
		private String id;
		private String name;
		private PrinterConfiguratorFactory printerConfiguratorFactory;		
		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}
		/**
		 * @return the printerConfiguratorFactory
		 */
		public PrinterConfiguratorFactory getPrinterConfiguratorFactory() {
			return printerConfiguratorFactory;
		}
		/**
		 * @param printerConfiguratorFactory the printerConfiguratorFactory to set
		 */
		public void setPrinterConfiguratorFactory(
				PrinterConfiguratorFactory printerConfiguratorFactory) {
			this.printerConfiguratorFactory = printerConfiguratorFactory;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		
		
	}
	
	private Map<String, PrinterUseCase> printerUseCases = new HashMap<String, PrinterUseCase>();
	private Map<String, ConfiguratorFactoryEntry> printerConfiguratorFactories = new HashMap<String, ConfiguratorFactoryEntry>();
	
	/**
	 * 
	 */
	public PrinterConfigurationRegistry() {
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.base.extensionpoint.AbstractEPProcessor#getExtensionPointID()
	 */
	@Override
	public String getExtensionPointID() {
		return EXTENSION_POINT_ID;
	}

	/* (non-Javadoc)
	 * @see org.nightlabs.base.extensionpoint.AbstractEPProcessor#processElement(org.eclipse.core.runtime.IExtension, org.eclipse.core.runtime.IConfigurationElement)
	 */
	@Override
	public void processElement(IExtension extension, IConfigurationElement element) 
	throws EPProcessorException 
	{
		if (element.getName().equalsIgnoreCase("printerConfiguratorFactory"))
			processConfigurator(extension, element);
		else if (element.getName().equalsIgnoreCase("printerUseCase"))
			processUseCase(extension, element);
	}
	
	private void processUseCase(IExtension extension, IConfigurationElement element) 
	throws EPProcessorException 
	{
		String id = element.getAttribute("id");
		if (!checkString(id))
			throw new EPProcessorException("The attribute id must be defined for element printerUseCase.", extension);
		String name = element.getAttribute("name");
		if (!checkString(name))
			throw new EPProcessorException("The attribute id must be defined for element printerUseCase.", extension);
		String description = element.getAttribute("description");
		String defConfigurator = element.getAttribute("defaultConfigurator");
		PrinterUseCase useCase = new PrinterUseCase();
		useCase.setId(id);
		useCase.setName(name);
		useCase.setDescription(description);
		useCase.setDefaultConfiguratorID(defConfigurator);
		printerUseCases.put(id, useCase);
	} 

	private void processConfigurator(IExtension extension, IConfigurationElement element) 
	throws EPProcessorException 
	{
		String id = element.getAttribute("id");
		if (!checkString(id))
			throw new EPProcessorException("The attribute id must be defined for element printerConfiguratorFactory.", extension);
		
		String name = element.getAttribute("name");
		if (!checkString(id))
			throw new EPProcessorException("The attribute name must be defined for element printerConfiguratorFactory.", extension);
		
		PrinterConfiguratorFactory factory;
		try {
			factory = (PrinterConfiguratorFactory)element.createExecutableExtension("class");
		} catch (CoreException e) {
			throw new EPProcessorException(e);
		}
		ConfiguratorFactoryEntry entry = new ConfiguratorFactoryEntry();
		entry.setId(id);
		entry.setName(name);
		entry.setPrinterConfiguratorFactory(factory);
		printerConfiguratorFactories.put(id, entry);
	}
	
	private void validate() {
		for (PrinterUseCase useCase : printerUseCases.values()) {
			useCase.validate();
		}
	}

	/**
	 * Returns the registered {@link PrinterConfiguratorFactory} for the given id.
	 * 
	 * @param id The unique id to search a {@link PrinterConfiguratorFactory} for.
	 * @return The registered {@link PrinterConfiguratorFactory} for the given id.
	 */
	public PrinterConfiguratorFactory getPrinterConfiguratorFactory(String id) {
		ConfiguratorFactoryEntry entry = printerConfiguratorFactories.get(id);
		if (entry == null)
			return null;
		return entry.getPrinterConfiguratorFactory();
	}
	
	public Collection<ConfiguratorFactoryEntry> getPrinterConfiguratorEntries() {
		return printerConfiguratorFactories.values();
	}
	
	/**
	 * Returns the registered {@link PrinterUseCase} with the given id or null if none found.
	 * 
	 * @param id The id to search a {@link PrinterUseCase} for.
	 * @return The registered {@link PrinterUseCase} with the given id or null if none found.
	 */
	public PrinterUseCase getPrinterUseCase(String id) {
		return printerUseCases.get(id);
	}
	
	private static PrinterConfigurationRegistry sharedInstance;
	
	public static PrinterConfigurationRegistry sharedInstance() {
		if (sharedInstance == null) {
			sharedInstance = new PrinterConfigurationRegistry();
			try {
				sharedInstance.process();
			} catch (EPProcessorException e) {
				throw new RuntimeException(e);
			}
			sharedInstance.validate();
		}
		return sharedInstance;
	}
}
