
package com.twinsoft.convertigo.eclipse.dialogs;

import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.twinsoft.convertigo.beans.core.Project;
import com.twinsoft.convertigo.eclipse.ConvertigoPlugin;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.model.ProjectTreeObject;
import com.twinsoft.convertigo.engine.util.ProjectUtils;

public class StatisticsDialog extends Dialog {
	private Display display;
	private Label topLabel;
	private Label labelImage;
	private Image imageLeft;
	private Composite descriptifRight;
	private String projectName, comment, version;
 
	/**
	 * Create the dialog.
	 * @param parentShell, errorMessage
	 */
	public StatisticsDialog(Shell parentShell, String projectName, String comment, String version) {
		super(parentShell);
		this.projectName = projectName;
		this.comment = comment;
		this.version = version;
		
		this.setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE);
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Statistics");
		newShell.setSize(800,500); 
		display = newShell.getDisplay();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 5;
		gridLayout.numColumns = 2;
		Color white = new Color(display, 255,255,255);
		container.setLayout(gridLayout);
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		
		topLabel = new Label(container, SWT.NONE);
		topLabel.setText(projectName+(version.equals("") ? "" : " v"+version)+"\n"+comment);
		topLabel.setLayoutData(gridData);		
		
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = false;
		
		imageLeft = new Image(display, getClass().getResourceAsStream("images/dialog_statistic.jpg"));
		labelImage = new Label(container, SWT.NONE);
		labelImage.setImage(imageLeft);
		labelImage.setBackground(white);
		labelImage.setLayoutData(gridData);
		
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		descriptifRight = new Composite(container, SWT.NONE);		
		
		descriptifRight.setBackground(white);
		descriptifRight.setLayoutData(gridData);
		gridLayout = new GridLayout();
		descriptifRight.setLayout(gridLayout);
		
		addStats();
		
		return container;
	}
	
	private void addStats() {
		ProjectTreeObject projectTreeObject = (ProjectTreeObject)ConvertigoPlugin.getDefault().getProjectExplorerView().getFirstSelectedTreeObject();
		Project project = (Project) projectTreeObject.getObject();
		
		Map<String, String> statsProject = null;
		try {
			statsProject = ProjectUtils.getStatByProject(project);
		} catch (Exception e) {
			ConvertigoPlugin.logException(e, "Exception when compute statistics");
		}
		CLabel projectInfo = new CLabel(descriptifRight, SWT.NONE);
		projectInfo.setImage(new Image(display, getClass().getResourceAsStream("images/project_16x16.png")));	                                                                
		projectInfo.setText(statsProject.get(project.getName()).replaceAll("<br/>", "\r\n").replaceAll("&nbsp;", " "));
		projectInfo.setBackground(new Color(display, 255,255,255));
		
		for (String key: statsProject.keySet()) {
			if (key != project.getName()) {
				CLabel t = new CLabel(descriptifRight,SWT.NONE);
				t.setText(key+"\n"+statsProject.get(key).replaceAll("<br/>", "\r\n").replaceAll("&nbsp;", " "));
				t.setImage(new Image(display, getClass().getResourceAsStream("images/"+key.replaceAll(" ", "_").toLowerCase()+"_16x16.png")));
				t.setBackground(new Color(display, 255,255,255));
			}
		}
	}
	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button buttonOk = createButton(parent, IDialogConstants.OK_ID, "OK", true);
		buttonOk.setEnabled(true);
	}
}