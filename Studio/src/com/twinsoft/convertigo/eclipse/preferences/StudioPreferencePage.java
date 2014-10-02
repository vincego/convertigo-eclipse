/*
 * Copyright (c) 2001-2011 Convertigo SA.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 *
 * $URL$
 * $Author$
 * $Revision$
 * $Date$
 */

package com.twinsoft.convertigo.eclipse.preferences;

import java.io.File;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.twinsoft.convertigo.eclipse.ConvertigoPlugin;

public class StudioPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private ComboFieldEditor comboLevel = null;
	private BooleanFieldEditor cbHighlight = null;
	private BooleanFieldEditor cbIgnoreNews = null;
	private BooleanFieldEditor cbValidateXmlSchema = null;
	private IntegerFieldEditor intTracePlayerPort = null;
	private StringFieldEditor localBuildAdditionalPath = null;
	
	public StudioPreferencePage() {
		super();
		setDescription("Studio Settings");
	}
	
	public void init(IWorkbench workbench) {
		//Initialize the preference store we wish to use
		setPreferenceStore(ConvertigoPlugin.getDefault().getPreferenceStore());
	}
	
	protected Control createContents(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		top.setLayout(new GridLayout(1, false));
		
		// General options
		Group groupGeneral = new Group(top, SWT.SHADOW_IN);
		groupGeneral.setText("General options");
		groupGeneral.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	
		intTracePlayerPort = new IntegerFieldEditor(
				ConvertigoPlugin.PREFERENCE_TRACEPLAYER_PORT,
				"Trace player listening port", groupGeneral);
		intTracePlayerPort.setPage(this);
		intTracePlayerPort.setPreferenceStore(getPreferenceStore());
		intTracePlayerPort.load();
		
		cbHighlight = new BooleanFieldEditor(
				ConvertigoPlugin.PREFERENCE_TREE_HIGHLIGHT_DETECTED,
				"Highlight detected objects in tree", groupGeneral);
		cbHighlight.setPage(this);
		cbHighlight.setPreferenceStore(getPreferenceStore());
		cbHighlight.load();
				
		cbValidateXmlSchema = new BooleanFieldEditor(
				ConvertigoPlugin.PREFERENCE_XMLSCHEMA_VALIDATE,
				"Validate schema and response compliance", groupGeneral);
		cbValidateXmlSchema.setPage(this);
		cbValidateXmlSchema.setPreferenceStore(getPreferenceStore());
		cbValidateXmlSchema.load();

		cbIgnoreNews = new BooleanFieldEditor(
				ConvertigoPlugin.PREFERENCE_IGNORE_NEWS,
				"Automatically dismiss the splashscreen", groupGeneral);
		cbIgnoreNews.setPage(this);
		cbIgnoreNews.setPreferenceStore(getPreferenceStore());
		cbIgnoreNews.load();

		// Diagnostics
		Group groupDiagnostics = new Group(top, SWT.SHADOW_IN);
		groupDiagnostics.setText("Diagnostics");

		comboLevel = new ComboFieldEditor(
							ConvertigoPlugin.PREFERENCE_LOG_LEVEL,
							"Trace level", 
							new String[][] {
								{ "Errors", "0" },
								{ "Exceptions", "1" },
								{ "Warnings", "2" },
								{ "Messages", "3" },
								{ "Debug", "4" },
								{ "Debug+", "5" },
								{ "Debug++", "6" }
							},
							groupDiagnostics);
		
		comboLevel.setPage(this);
		comboLevel.setPreferenceStore(getPreferenceStore());
		comboLevel.load();

		Group groupLocalBuild = new Group(top, SWT.SHADOW_IN);
		groupLocalBuild.setText("Local Build");
		groupLocalBuild.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		localBuildAdditionalPath = new StringFieldEditor(
				ConvertigoPlugin.PREFERENCE_LOCAL_BUILD_ADDITIONAL_PATH,
				"Additional PATH (folders separated by '" + File.pathSeparator + "')", groupLocalBuild);
		localBuildAdditionalPath.setPage(this);
		localBuildAdditionalPath.setPreferenceStore(getPreferenceStore());
		localBuildAdditionalPath.load();
		
		BooleanFieldEditor btest = new BooleanFieldEditor("", "", groupLocalBuild);
		btest.getDescriptionControl(groupLocalBuild).setVisible(false);
		
		return top;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	@Override
	protected void performDefaults() {
		comboLevel.loadDefault();
		cbHighlight.loadDefault();
		intTracePlayerPort.loadDefault();
		cbIgnoreNews.loadDefault();
		cbValidateXmlSchema.loadDefault();
		localBuildAdditionalPath.loadDefault();
		
		super.performDefaults();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	@Override
	public boolean performOk() {
		ConvertigoPlugin.setLogLevel(Integer.valueOf(comboLevel.getValue(),10));
		ConvertigoPlugin.setHighlightDetectedObject(cbHighlight.getBooleanValue());
		ConvertigoPlugin.setValidateXmlSchema(cbValidateXmlSchema.getBooleanValue());
		
		comboLevel.store();
		cbHighlight.store();
		intTracePlayerPort.store();
		cbIgnoreNews.store();
		cbValidateXmlSchema.store();
		localBuildAdditionalPath.store();
		
		return super.performOk();
	}
}
