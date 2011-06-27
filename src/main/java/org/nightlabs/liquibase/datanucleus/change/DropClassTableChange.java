/**
 * 
 */
package org.nightlabs.liquibase.datanucleus.change;

import java.util.ArrayList;
import java.util.List;

import liquibase.change.ChangeMetaData;
import liquibase.database.Database;
import liquibase.exception.SetupException;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.DeleteStatement;
import liquibase.statement.core.DropTableStatement;

import org.nightlabs.liquibase.datanucleus.util.DNUtil;
import org.nightlabs.liquibase.datanucleus.util.Log;

/**
 * A Change that drops the table data for a persistent class was stored in.
 * <p>
 * The change is used with the &lt;ext:dnDropClassTable&gt; Tag its attributes
 * are:
 * <ul>
 * <li>schemaName: Optional parameter, defaults to the current schemaName</li>
 * <li>className: The fully qualified name of the class of which the data and table should be dropped</li>
 * <li>cascadeConstraints: Whether to cascade 
 * <ul>
 * </p>
 * 
 * @author abieber
 */
public class DropClassTableChange extends AbstractDNChange {

	private String className;
	private boolean cascadeConstraints = true;
	
	public DropClassTableChange() {
		super("dnDropClassTable", "Drop a field from the data of a persistent class", ChangeMetaData.PRIORITY_DEFAULT);
	}
	
	@Override
	public void init() throws SetupException {
		super.init();
		if (null == getClassName() || getClassName().isEmpty()) {
			throw new SetupException(getChangeMetaData().getName() + " requires the 'className'-attribute to be set.");
		}
	}

	@Override
	public String getConfirmationMessage() {
		return getChangeMetaData().getName() + " successfully dropped the table for " + getClassName() + ".";
	}

	@Override
	protected List<SqlStatement> doGenerateStatements(Database database) {
		
		List<SqlStatement> statements = new ArrayList<SqlStatement>(2);
		
		String tableName = DNUtil.getTableName(database, getClassName());
		
		if (tableName != null && !tableName.isEmpty()) {
			
			DropTableStatement dropTable = new DropTableStatement(getSchemaName(), tableName, isCascadeConstraints());
			
			DeleteStatement delete = new DeleteStatement(getSchemaName(), DNUtil.getNucleusTablesName());
			delete.setWhereClause(DNUtil.getNucleusClassNameColumn() + " = ?");
			delete.addWhereParameter(getClassName());
		
			statements.add(dropTable);
		} else {
			Log.warn("Could not find table for className %s in nucleus_tables. %s will abort.", getClassName(), getChangeMetaData().getName());
		}
		
		return statements;
	}
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public void setCascadeConstraints(boolean cacadeConstraints) {
		this.cascadeConstraints = cacadeConstraints;
	}
	
	public boolean isCascadeConstraints() {
		return cascadeConstraints;
	}

}
