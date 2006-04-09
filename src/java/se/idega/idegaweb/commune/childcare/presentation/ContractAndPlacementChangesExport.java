package se.idega.idegaweb.commune.childcare.presentation;

import java.text.DecimalFormat;
import java.util.Iterator;
import se.idega.idegaweb.commune.childcare.business.ContractAndPlacementChangesExportWriter;
import com.idega.core.file.data.ICFile;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

public class ContractAndPlacementChangesExport extends ChildCareBlock {
	
	private static final String PARAMETER_CREATE_REPORT = "create_report";	

	private ICFile exportFolder;
	IWContext iwc = null;
	
	private boolean createExport = false;
	private boolean exportCreated = false;	
	
	public void init(IWContext iwc) throws Exception {		
		setIwc(iwc); 
		
		parseAction();			
		createGui();
	}
	
	private void parseAction() {		
		if (this.iwc.isParameterSet(PARAMETER_CREATE_REPORT)) {
			this.createExport = true;
			
			ContractAndPlacementChangesExportWriter writer = 
					new ContractAndPlacementChangesExportWriter();
			
			this.exportCreated = writer.createExportFile(this.getIwc(), this.getExportFolder());			
		}		
	}
	
	private void createGui() {
		PresentationObjectContainer poc = new PresentationObjectContainer();
		
		if (this.exportFolder != null ) {
			Table table = new Table(1, 3);
			table.setCellpadding(0);
			table.setCellspacing(0);
			table.setHeight(2, 12);
			table.setWidth(getWidth());
			
			table.add(getCreateForm(), 1, 1);
			table.add(getFileList(), 1, 3);		
			
			poc.add(table);
		} 
		else {
			poc.add(getSmallErrorText("The statistics folder has not been set..."));
		}
		
		add(poc);
		
	}
	
	private Table getFileList() {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(3);
		table.setRowColor(1, getHeaderColor());
		table.setWidth(1, Table.HUNDRED_PERCENT);
		int row = 1;
		int column = 1;
			
		table.add(getLocalizedSmallHeader("child_care.file_name", "File name"), column++, row);
		table.setNoWrap(column, row);
		table.add(getLocalizedSmallHeader("child_care.file_size", "File size"), column++, row);
		table.setNoWrap(column, row);
		table.add(getLocalizedSmallHeader("child_care.created_date", "Created date"), column++, row);
		//table.setNoWrap(column, row);
		//table.add(getLocalizedSmallHeader("child_care.mimetype", "Mimetype"), column++, row++);
		row++;
		
		Iterator iter = getExportFolder().getChildrenIterator();
		
		if (iter != null) {
			while (iter.hasNext()) {
				column = 1;
				ICFile file = (ICFile) iter.next();
				
				double size = 0;
				if (!file.isFolder()) {
					size = (double) file.getFileSize().intValue() / (double) 1024;					
				}
				DecimalFormat format = new DecimalFormat("0.0 KB");

				if (row % 2 == 0) {
					table.setRowColor(row, getZebraColor1());
				}
				else {
					table.setRowColor(row, getZebraColor2());
				}
	
				Link link = getSmallLink(file.getName());
				link.setFile(file);
				
				table.add(link, column++, row);
				table.setNoWrap(column, row);
				table.add(getSmallText(format.format(size)), column++, row);
				table.setNoWrap(column, row);
				table.add(getSmallText(new IWTimestamp(file.getCreationDate()).getLocaleDateAndTime(this.iwc.getCurrentLocale(), IWTimestamp.SHORT, IWTimestamp.SHORT)), column++, row);
				//table.setNoWrap(column, row);
				//table.add(getSmallText(file.getMimeType()), column++, mos.write(new String("hello, world").getBytes()););
				row++;	
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}
	
	private Form getCreateForm() {
		Form form = new Form();
		form.addParameter(PARAMETER_CREATE_REPORT, "true");
		
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(localize("child_care.create_report", "Create report")));
		form.add(button);
		
		if (this.createExport && !this.exportCreated) {
			form.add(Text.getNonBrakingSpace());
			form.add(getSmallErrorText(localize("child_care.create_report_failed", "Failed to create report. Possibly no changes found.")));
		}
		
		return form;
	}	
	

	public ICFile getExportFolder() {
		return this.exportFolder;
	}

	public void setExportFolder(ICFile statisticsFolder) {
		this.exportFolder = statisticsFolder;
	}
	
	public IWContext getIwc() {
		return this.iwc;
	}
	
	public void setIwc(IWContext iwc) {
		this.iwc = iwc;
	}	
	
}
