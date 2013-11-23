/*
 * Copyright (C) 2003, 2004  Pascal Essiembre, Essiembre Consultant Inc.
 * 
 * This file is part of Essiembre ResourceBundle Editor.
 * 
 * Essiembre ResourceBundle Editor is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * Essiembre ResourceBundle Editor is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with Essiembre ResourceBundle Editor; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 */
package com.essiembre.eclipse.rbe.model.workbench;

import org.eclipse.core.runtime.Preferences;

import com.essiembre.eclipse.rbe.RBEPlugin;

/**
 * Application preferences, relevant to the resource bundle editor plugin.
 * @author Pascal Essiembre (essiembre@users.sourceforge.net)
 * @version $Author: essiembre $ $Revision: 1.3 $ $Date: 2007/02/02 04:16:36 $
 */
/**
 * 
 * @author Pascal Essiembre
 * @version $Author: essiembre $ $Revision: 1.3 $ $Date: 2007/02/02 04:16:36 $
 */
public final class RBEPreferences {
   
    /** Key group separator. */
    public static final String KEY_GROUP_SEPARATOR =
            "keyGroupSeparator"; //$NON-NLS-1$

    /** Should key tree be hiearchical by default. */
    public static final String KEY_TREE_HIERARCHICAL =
            "keyTreeHierarchical"; //$NON-NLS-1$
    /** Should key tree be expanded by default. */
    public static final String KEY_TREE_EXPANDED =
            "keyTreeExpanded"; //$NON-NLS-1$
    
    /** Should "Generated by" line be added to files. */
    public static final String SHOW_GENERATOR = "showGenerator"; //$NON-NLS-1$

    /** Should Eclipse "nl" directory structure be supported. */
    public static final String SUPPORT_NL = "supportNL"; //$NON-NLS-1$

    /** Should tab characters be inserted when tab key pressed on text field. */
    public static final String FIELD_TAB_INSERTS =
            "fieldTabInserts"; //$NON-NLS-1$

    /** Should equal signs be aligned. */
    public static final String ALIGN_EQUAL_SIGNS =
            "alignEqualSigns"; //$NON-NLS-1$
    /** Should spaces be put around equal signs. */
    public static final String SPACES_AROUND_EQUAL_SIGNS =
            "spacesAroundEqualSigns"; //$NON-NLS-1$

    /** Should keys be grouped. */
    public static final String GROUP_KEYS = "groupKeys"; //$NON-NLS-1$
    /** How many level deep should keys be grouped. */
    public static final String GROUP_LEVEL_DEEP =
            "groupLevelDeep"; //$NON-NLS-1$
    /** How many line breaks between key groups. */
    public static final String GROUP_LINE_BREAKS =
            "groupLineBreaks"; //$NON-NLS-1$
    /** Should equal signs be aligned within groups. */
    public static final String GROUP_ALIGN_EQUAL_SIGNS =
            "groupAlignEqualSigns"; //$NON-NLS-1$

    /** Should lines be wrapped. */
    public static final String WRAP_LINES = "wrapLines"; //$NON-NLS-1$
    /** Maximum number of character after which we should wrap. */
    public static final String WRAP_CHAR_LIMIT = "wrapCharLimit"; //$NON-NLS-1$
    /** Align subsequent lines with equal signs. */
    public static final String WRAP_ALIGN_EQUAL_SIGNS =
            "wrapAlignEqualSigns"; //$NON-NLS-1$
    /** Number of spaces to indent subsequent lines. */
    public static final String WRAP_INDENT_SPACES =
            "wrapIndentSpaces"; //$NON-NLS-1$
    
    /** Should unicode values be converted to their encoded equivalent. */
    public static final String CONVERT_UNICODE_TO_ENCODED =
            "convertUnicodeToEncoded"; //$NON-NLS-1$
    /** Should unicode values be converted to their encoded equivalent. */
    public static final String CONVERT_UNICODE_TO_ENCODED_UPPER =
            "convertUnicodeToEncodedUppercase"; //$NON-NLS-1$
    /** Should encoded values be converted to their unicode equivalent. */
    public static final String CONVERT_ENCODED_TO_UNICODE =
            "convertEncodedToUnicode"; //$NON-NLS-1$
    
    /** Impose a given new line type. */
    public static final String FORCE_NEW_LINE_TYPE =
            "forceNewLineType"; //$NON-NLS-1$
    /** How new lines are represented in resource bundle. */
    public static final String NEW_LINE_TYPE = "newLineType"; //$NON-NLS-1$
    /** Should new lines character produce a line break in properties files. */
    public static final String NEW_LINE_NICE = "newLineNice"; //$NON-NLS-1$
    
    /** New Line Type: UNIX. */
    public static final int NEW_LINE_UNIX = 0;
    /** New Line Type: Windows. */
    public static final int NEW_LINE_WIN = 1;
    /** New Line Type: Mac. */
    public static final int NEW_LINE_MAC = 2;

    /** Report missing values. */
    public static final String REPORT_MISSING_VALUES =
            "detectMissingValues"; //$NON-NLS-1$
    /** Report duplicate values. */
    public static final String REPORT_DUPL_VALUES =
            "reportDuplicateValues"; //$NON-NLS-1$
    /** Report similar values. */
    public static final String REPORT_SIM_VALUES =
            "reportSimilarValues"; //$NON-NLS-1$
    /** Report similar values: word compare. */
    public static final String REPORT_SIM_VALUES_WORD_COMPARE =
            "reportSimilarValuesWordCompare"; //$NON-NLS-1$
    /** Report similar values: levensthein distance. */
    public static final String REPORT_SIM_VALUES_LEVENSTHEIN =
            "reportSimilarValuesLevensthein"; //$NON-NLS-1$
    /** Report similar values: precision. */
    public static final String REPORT_SIM_VALUES_PRECISION =
            "reportSimilarValuesPrecision"; //$NON-NLS-1$
    
    /** Don't show the tree within the editor. */
    public static final String NO_TREE_IN_EDITOR =
            "noTreeInEditor"; //$NON-NLS-1$

    /** Keep empty fields. */
    public static final String KEEP_EMPTY_FIELDS =
            "keepEmptyFields"; //$NON-NLS-1$
    
    /** RBEPreferences. */
    private static final Preferences PREFS =
            RBEPlugin.getDefault().getPluginPreferences();
    
    /**
     * Constructor.
     */
    private RBEPreferences() {
        super();
    }

    /**
     * Gets key group separator.
     * @return key group separator.
     */
    public static String getKeyGroupSeparator() {
        return PREFS.getString(KEY_GROUP_SEPARATOR);
    }

    /**
     * Gets whether pressing tab inserts a tab in a field.
     * @return <code>true</code> if pressing tab inserts a tab in a field
     */
    public static boolean getFieldTabInserts() {
        return PREFS.getBoolean(FIELD_TAB_INSERTS);
    }
    
    /**
     * Gets whether equals signs should be aligned when generating file.
     * @return <code>true</code> if equals signs should be aligned
     */
    public static boolean getAlignEqualSigns() {
        return PREFS.getBoolean(ALIGN_EQUAL_SIGNS);
    }

    /**
     * Gets whether there should be spaces around equals signs when generating
     * file.
     * @return <code>true</code> there if should be spaces around equals signs
     */
    public static boolean getSpacesAroundEqualSigns() {
        return PREFS.getBoolean(SPACES_AROUND_EQUAL_SIGNS);
    }

    /**
     * Gets whether keys should be grouped when generating file.
     * @return <code>true</code> if keys should be grouped
     */
    public static boolean getGroupKeys() {
        return PREFS.getBoolean(GROUP_KEYS);
    }

    /**
     * Gets how many level deep keys should be grouped when generating file.
     * @return how many level deep
     */
    public static int getGroupLevelDeepness() {
        return PREFS.getInt(GROUP_LEVEL_DEEP);
    }
    
    /**
     * Gets how many blank lines should separate groups when generating file.
     * @return how many blank lines between groups
     */
    public static int getGroupLineBreaks() {
        return PREFS.getInt(GROUP_LINE_BREAKS);
    }

    /**
     * Gets whether equal signs should be aligned within each groups when
     * generating file.
     * @return <code>true</code> if equal signs should be aligned within groups
     */
    public static boolean getGroupAlignEqualSigns() {
        return PREFS.getBoolean(GROUP_ALIGN_EQUAL_SIGNS);
    }

    /**
     * Gets whether key tree should be displayed in hiearchical way by default.
     * @return <code>true</code> if hierarchical
     */
    public static boolean getKeyTreeHierarchical() {
        return PREFS.getBoolean(KEY_TREE_HIERARCHICAL);
    }
    /**
     * Gets whether key tree should be show expaned by default.
     * @return <code>true</code> if expanded
     */
    public static boolean getKeyTreeExpanded() {
        return PREFS.getBoolean(KEY_TREE_EXPANDED);
    }

    /**
     * Gets whether to print "Generated By..." comment when generating file.
     * @return <code>true</code> if we print it
     */
    public static boolean getShowGenerator() {
        return PREFS.getBoolean(SHOW_GENERATOR);
    }

    /**
     * Gets whether to support Eclipse NL directory structure.
     * @return <code>true</code> if supported
     */
    public static boolean getSupportNL() {
        return PREFS.getBoolean(SUPPORT_NL);
    }
    
    /**
     * Gets whether lines should be wrapped if too big when generating file.
     * @return <code>true</code> if wrapped
     */
    public static boolean getWrapLines() {
        return PREFS.getBoolean(WRAP_LINES);
    }
    
    /**
     * Gets the number of character after which lines should be wrapped when
     * generating file.
     * @return number of characters
     */
    public static int getWrapCharLimit() {
        return PREFS.getInt(WRAP_CHAR_LIMIT);
    }

    /**
     * Gets whether wrapped lines should be aligned with equal sign when
     * generating file.
     * @return <code>true</code> if aligned
     */
    public static boolean getWrapAlignEqualSigns() {
        return PREFS.getBoolean(WRAP_ALIGN_EQUAL_SIGNS);
    }

    /**
     * Gets the number of spaces to use for indentation of wrapped lines when
     * generating file.
     * @return number of spaces
     */
    public static int getWrapIndentSpaces() {
        return PREFS.getInt(WRAP_INDENT_SPACES);
    }

    /**
     * Gets whether to convert encoded strings to unicode characters when
     * reading file.
     * @return <code>true</code> if converting
     */
    public static boolean getConvertEncodedToUnicode() {
        return PREFS.getBoolean(CONVERT_ENCODED_TO_UNICODE);
    }

    /**
     * Gets whether to escape unicode characters when generating file.
     * @return <code>true</code> if escaping
     */
    public static boolean getConvertUnicodeToEncoded() {
        return PREFS.getBoolean(CONVERT_UNICODE_TO_ENCODED);
    }
    /**
     * Gets whether escaped unicode "alpha" characters should be uppercase
     * when generating file.
     * @return <code>true</code> if uppercase
     */
    public static boolean getConvertUnicodeToEncodedUpper() {
        return PREFS.getBoolean(CONVERT_UNICODE_TO_ENCODED_UPPER);
    }

    /**
     * Gets whether we want to overwrite system (or Eclipse) default new line
     * type when generating file.
     * @return <code>true</code> if overwriting
     */
    public static boolean getForceNewLineType() {
        return PREFS.getBoolean(FORCE_NEW_LINE_TYPE);
    }

    /**
     * Gets the new line type to use when overwriting system (or Eclipse)
     * default new line type when generating file.  Use constants to this
     * effect.
     * @return new line type
     */
    public static int getNewLineType() {
        return PREFS.getInt(NEW_LINE_TYPE);
    }
    
    /**
     * Gets whether new lines are escaped or printed as is when generating file.
     * @return <code>true</code> if printed as is.
     */
    public static boolean getNewLineNice() {
        return PREFS.getBoolean(NEW_LINE_NICE);
    }

    /**
     * Gets whether to report keys with missing values.
     * @return <code>true</code> if reporting
     */
    public static boolean getReportMissingValues() {
        return PREFS.getBoolean(REPORT_MISSING_VALUES);
    }
    
    /**
     * Gets whether to report keys with duplicate values.
     * @return <code>true</code> if reporting
     */
    public static boolean getReportDuplicateValues() {
        return PREFS.getBoolean(REPORT_DUPL_VALUES);
    }
    
    /**
     * Gets whether to report keys with similar values.
     * @return <code>true</code> if reporting
     */
    public static boolean getReportSimilarValues() {
        return PREFS.getBoolean(REPORT_SIM_VALUES);
    }

    /**
     * Gets whether to use the "word compare" method when reporting similar
     * values.
     * @return <code>true</code> if using "word compare" method
     */
    public static boolean getReportSimilarValuesWordCompare() {
        return PREFS.getBoolean(REPORT_SIM_VALUES_WORD_COMPARE);
    }

    /**
     * Gets whether to use the Levensthein method when reporting similar
     * values.
     * @return <code>true</code> if using Levensthein method
     */
    public static boolean getReportSimilarValuesLevensthein() {
        return PREFS.getBoolean(REPORT_SIM_VALUES_LEVENSTHEIN);
    }

    /**
     * Gets the minimum precision level to use for determining when to report
     * similarities.
     * @return precision
     */
    public static double getReportSimilarValuesPrecision() {
        return PREFS.getDouble(REPORT_SIM_VALUES_PRECISION);
    }

    /**
     * Gets whether a tree shall be displayed within the editor or not.
     * @return <code>true</code> A tree shall not be displayed.
     */
    public static boolean getNoTreeInEditor() {
        return PREFS.getBoolean(NO_TREE_IN_EDITOR);
    }
    
    /**
     * Gets whether to keep empty fields.
     * @return <code>true</code> if empty fields are to be kept.
     */
    public static boolean getKeepEmptyFields() {
        return PREFS.getBoolean(KEEP_EMPTY_FIELDS);
    }

}