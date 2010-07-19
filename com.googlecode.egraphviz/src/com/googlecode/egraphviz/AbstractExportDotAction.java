 /*******************************************************************************
 * Copyright (c) 2008, Jean-Rémy Falleri.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jean-Rémy Falleri - initial API and implementation
 *******************************************************************************/

package com.googlecode.egraphviz;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


public abstract class AbstractExportDotAction implements IObjectActionDelegate {
	
	protected IStructuredSelection selection;
	
	protected DotOptions dotOptions;

	public AbstractExportDotAction() {
		super();
		dotOptions = new DotOptions();
	}
	
	public abstract String getFormat();

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		for(Object object: selection.toArray() ) {
			if ( object instanceof IFile ) {
				IFile iFile = ((IFile) object);
				String source = iFile.getRawLocation().toFile().getAbsolutePath();
				dotOptions.setInputFile(source);
				dotOptions.setOutputFile(source + "." + getFormat());
				dotOptions.setOutputFormat(getFormat());
				try {
					dotOptions.runDotCommand();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				IProgressMonitor monitor = new NullProgressMonitor();
				try {
					iFile.getParent().refreshLocal(1, monitor);
				}
				catch(CoreException e) {}
			}
		}
	}
	
	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}
	
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
			if (selection instanceof IStructuredSelection)
				this.selection = (IStructuredSelection) selection;
	}
}
