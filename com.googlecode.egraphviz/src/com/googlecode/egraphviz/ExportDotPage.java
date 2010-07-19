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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;

public class ExportDotPage extends WizardExportResourcesPage implements Listener {

	private Combo destinationNameField;

	private Button destinationBrowseButton;
	
	private Combo cmbOutputFormat;
	
	private Combo cmbAlgorithm;
	
	private DotOptions dotOptions;

	public ExportDotPage(IStructuredSelection selection) {
		super("main_page", selection);
		this.setTitle("Export selected dot file");
		this.setDescription("Export a Graphviz dot graph file to a graphic file.");
		this.dotOptions = new DotOptions();
	}
	
	public DotOptions getDotOptions() {
		return this.dotOptions;
	}
	
	protected void createOptionsGroup(Composite parent) {
		Composite grpOptions = new Composite(parent,SWT.NONE);
		Font font = parent.getFont();
		grpOptions.setVisible(true);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		
		grpOptions.setLayout(layout);
		grpOptions.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		grpOptions.setFont(font);
		
		Label lbAlgorithm = new Label(grpOptions,SWT.NONE);
		lbAlgorithm.setText("Select algorithm:");
		
		cmbAlgorithm = new Combo(grpOptions,SWT.NONE| SWT.READ_ONLY);
		for( String a: DotOptions.ALGORITHMS )
			cmbAlgorithm.add(a);
		cmbAlgorithm.setText(DotOptions.DEFAULT_ALGORITHM);
		cmbAlgorithm.addListener(SWT.Modify, this);
		
		Label lbOutputFormat = new Label(grpOptions,SWT.NONE);
		lbOutputFormat.setText("Select graphic format:");
		
		cmbOutputFormat =  new Combo(grpOptions,SWT.NONE| SWT.READ_ONLY);
		for(String f: DotOptions.OUTPUT_FORMATS )
			cmbOutputFormat.add(f);
		cmbOutputFormat.setText(DotOptions.DEFAULT_OUTPUT_FORMAT);
		cmbOutputFormat.addListener(SWT.Modify, this);
	}

	@Override
	protected void createDestinationGroup(Composite parent)
	{
		Font font = parent.getFont();
		// destination specification group
		Composite destinationSelectionGroup = new Composite(parent, SWT.NONE);
		destinationSelectionGroup.setVisible(true);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		destinationSelectionGroup.setLayout(layout);
		destinationSelectionGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		destinationSelectionGroup.setFont(font);

		Label destinationLabel = new Label(destinationSelectionGroup, SWT.NONE);
		destinationLabel.setText("Save to");
		destinationLabel.setFont(font);

		// destination name entry field
		destinationNameField = new Combo(destinationSelectionGroup, SWT.SINGLE
				| SWT.BORDER);
		destinationNameField.addListener(SWT.Modify, this);
		destinationNameField.addListener(SWT.Selection, this);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		destinationNameField.setLayoutData(data);
		destinationNameField.setFont(font);

		// destination browse button
		destinationBrowseButton = new Button(destinationSelectionGroup, SWT.PUSH);
		destinationBrowseButton.setText("Browse");
		destinationBrowseButton.addListener(SWT.Selection, this);
		destinationBrowseButton.setFont(font);
		setButtonLayoutData(destinationBrowseButton);

		new Label(parent, SWT.NONE); // vertical spacer
	}

	@Override
	public boolean isPageComplete()
	{
		return this.dotOptions.isReady();
	}
	
	private void handleSourceSelectionChanged() {
		if (  this.getSelectedResources().size() != 1 )
			return;
		else {
			Object o = this.getSelectedResources().get(0);
			if ( !(o instanceof IFile) )
				return;
			else {
				IFile f = (IFile) o;
				if ( ! f.isAccessible() )
					return;
				if ( ! f.getFileExtension().toLowerCase().equals("dot") )
					return;
				else {
					this.dotOptions.setInputFile(f.getRawLocation().toFile().getAbsolutePath());
				}
					
			}
		}
	}

	private void handleDestinationBrowseButtonPressed()
	{
		FileDialog dialog = new FileDialog(getContainer().getShell(),SWT.SAVE );
		dialog.setText("Select file");
		dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
		
		String fileName = dialog.open();

		if ( fileName != null && !"".equals(fileName) ) {
			this.destinationNameField.setText(fileName);
			this.dotOptions.setOutputFile(fileName);
		}
		else
			this.setErrorMessage("You must select a destination file.");

	}
	
	private void handleCmbAlgorithmChanged() {
		this.dotOptions.setAlgorithm(cmbAlgorithm.getText());
	}
	
	private void handleCmbOutputFormatChanged() {
		this.dotOptions.setOutputFormat(cmbOutputFormat.getText());
	}
	
	@Override
	public void handleEvent(Event event) {
		Widget source = event.widget;
		
		handleSourceSelectionChanged();

		if( source == destinationBrowseButton )
			handleDestinationBrowseButtonPressed();
		else if ( source == cmbAlgorithm )
			handleCmbAlgorithmChanged();
		else if ( source == cmbOutputFormat )
			handleCmbOutputFormatChanged();
		
		this.updatePageCompletion();
	}
	
}
