/*
 * Copyright (c) 2001-2014 Convertigo SA.
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
 * $URL: http://sourceus/svn/convertigo/CEMS_opensource/branches/7.1.x/Studio/src/com/twinsoft/convertigo/eclipse/popup/actions/ChangeToScExitHandlerStatementAction.java $
 * $Author: julienda $
 * $Revision: 34593 $
 * $Date: 2013-07-25 17:22:06 +0200 (ven., 25 juil. 2014) $
 */

package com.twinsoft.convertigo.eclipse.popup.actions;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.twinsoft.convertigo.beans.core.DatabaseObject;
import com.twinsoft.convertigo.beans.core.Statement;
import com.twinsoft.convertigo.beans.statements.ScEntryHandlerStatement;
import com.twinsoft.convertigo.beans.statements.ScExitHandlerStatement;
import com.twinsoft.convertigo.eclipse.ConvertigoPlugin;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.ProjectExplorerView;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.TreeParent;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.TreePath;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.model.DatabaseObjectTreeObject;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.model.StatementTreeObject;
import com.twinsoft.convertigo.eclipse.views.projectexplorer.model.TreeObject;

public class ChangeToScExitHandlerStatementAction extends MyAbstractAction {

	public ChangeToScExitHandlerStatementAction() { }

	/* (non-Javadoc)
	 * @see com.twinsoft.convertigo.eclipse.popup.actions.MyAbstractAction#run()
	 */
	@Override
	public void run() {
		Display display = Display.getDefault();
		Cursor waitCursor = new Cursor(display, SWT.CURSOR_WAIT);		
		
		Shell shell = getParentShell();
		shell.setCursor(waitCursor);
		
        try {
    		ProjectExplorerView explorerView = getProjectExplorerView();
    		if (explorerView != null) {
    			TreeObject treeObject = explorerView.getFirstSelectedTreeObject();
    			Object databaseObject = treeObject.getObject();
    			// For ScEntryHandler statement
    			if ((databaseObject != null) && (databaseObject instanceof ScEntryHandlerStatement)) {
    				ScEntryHandlerStatement scEntryHandlerStatement = (ScEntryHandlerStatement) databaseObject;
  						
					TreeParent treeParent = treeObject.getParent();
					DatabaseObjectTreeObject parentTreeObject = null;
					if (treeParent instanceof DatabaseObjectTreeObject) {
						parentTreeObject = (DatabaseObjectTreeObject)treeParent;
					} else {
						parentTreeObject = (DatabaseObjectTreeObject)treeParent.getParent();
					}
					
	        		if (parentTreeObject != null) {
						// New ScExitHandler statement
	        			ScExitHandlerStatement scExitHandlerStatement = new ScExitHandlerStatement();
	        			
	        			// Set properties
	        			scExitHandlerStatement.setHandlerResult(scEntryHandlerStatement.getHandlerResult());
	        			scExitHandlerStatement.setHandlerType(scEntryHandlerStatement.getHandlerType());
	        			scExitHandlerStatement.setComment(scEntryHandlerStatement.getComment());
	        			scExitHandlerStatement.setEnable(scEntryHandlerStatement.isEnable());
	        			scExitHandlerStatement.setPreventFromLoops(scEntryHandlerStatement.preventFromLoops());
	        			scExitHandlerStatement.setParent(scEntryHandlerStatement.getParent());
	        			scExitHandlerStatement.setReturnedValue(scEntryHandlerStatement.getReturnedValue());
	        			scExitHandlerStatement.setVersion(scEntryHandlerStatement.getVersion());
	        			scExitHandlerStatement.setNormalizedScreenClassName(scEntryHandlerStatement.getNormalizedScreenClassName());
	        			scExitHandlerStatement.setName("on"+scEntryHandlerStatement.getNormalizedScreenClassName()+"Exit");
	        			
	        			// Change status of scExitHanlder statement
						scExitHandlerStatement.bNew = true;
						scExitHandlerStatement.hasChanged = true;
						
						// Add new ScExitHandler statement to parent
						DatabaseObject parentDbo = scEntryHandlerStatement.getParent();
						parentDbo.add(scExitHandlerStatement);
						
						// Add new ScExitHandler statement in Tree
						StatementTreeObject statementTreeObject = new StatementTreeObject(explorerView.viewer, scExitHandlerStatement);
						treeParent.addChild(statementTreeObject);
						
						
	    				if (scEntryHandlerStatement.hasStatements()) {
	    					
	    					List<Statement> list = scEntryHandlerStatement.getStatements();
	    					TreePath[] selectedPaths = new TreePath[list.size()];
	    					for (int i=0; i<list.size(); i++) {
	    						StatementTreeObject statementTreeObjects = (StatementTreeObject)explorerView.findTreeObjectByUserObject(list.get(i));
	    						selectedPaths[i] = new TreePath(statementTreeObjects);
	    					}
	    					
    						// Cut/Paste statements under screen class entry
    						if (selectedPaths.length > 0) {
    							new ClipboardAction(ConvertigoPlugin.clipboardManagerDND).cut(explorerView, selectedPaths, ProjectExplorerView.TREE_OBJECT_TYPE_DBO_STATEMENT);
	    						for (int i = 0 ; i < ConvertigoPlugin.clipboardManagerDND.objects.length ; i++) {
	    							ConvertigoPlugin.clipboardManagerDND.cutAndPaste(ConvertigoPlugin.clipboardManagerDND.objects[i], statementTreeObject);
	    						}
	    						ConvertigoPlugin.clipboardManagerDND.reset();
    						}
	    				}
						
		   				// Delete ScEntryHandler statement
						scEntryHandlerStatement.delete();
						
	        			parentTreeObject.hasBeenModified(true);
		                explorerView.reloadTreeObject(parentTreeObject);
		                explorerView.setSelectedTreeObject(explorerView.findTreeObjectByUserObject(scExitHandlerStatement));
	        		}
				}
			}
        }
        catch (Throwable e) {
        	ConvertigoPlugin.logException(e, "Unable to change screen class entry handler statement to screen class exit handler statement!");
        }
        finally {
			shell.setCursor(null);
			waitCursor.dispose();
        }
	}
}