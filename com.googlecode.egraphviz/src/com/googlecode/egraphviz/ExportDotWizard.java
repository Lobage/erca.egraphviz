 /*******************************************************************************
 * Copyright (c) 2009, Jean-Rémy Falleri.
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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

public class ExportDotWizard extends Wizard implements IExportWizard {
	
	private ExportDotPage mainPage;
	
	private IStructuredSelection selection;

	public ExportDotWizard() {
		super();
		this.setHelpAvailable(false);
		this.setWindowTitle("Graphviz Dot Graph Export Wizard");
	}
	
	@Override
	public void addPages() {
		super.addPages();
		mainPage = new ExportDotPage(selection);
		this.addPage(mainPage);
	}
	
	@Override
	public boolean performFinish() {
		if ( !mainPage.isPageComplete() ) {
			return false;
		}
		else {
			try {
				mainPage.getDotOptions().runDotCommand();
			}
			catch(IOException e) {
			}
			catch(InterruptedException e) {
			}
			return true;
		}
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	    this.selection = selection;
	}

}
