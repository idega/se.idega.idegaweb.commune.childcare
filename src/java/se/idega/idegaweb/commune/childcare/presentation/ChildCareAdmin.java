package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.care.business.CareConstants;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.business.ChildCareConstants;
import se.idega.idegaweb.commune.childcare.business.ChildCareQueueWriter;
import se.idega.idegaweb.commune.childcare.business.ChildCareSiblingListWriter;
import se.idega.idegaweb.commune.childcare.business.QueueCleaningSession;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.io.DownloadWriter;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.DownloadLink;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.data.User;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareAdmin extends ChildCareBlock {

	private static final String PARAMETER_CLEAN_QUEUE = "cc_clean_queue";
	private static final String PARAMETER_DELETE_APPLICATION = "cc_remove_application";
	
	//private int _numberPerPage = 15;
	//private int _start = 0;
	//private int _applicantsSize = 0;
	private int _numberPerPage = -1;
	private int _start = -1;

	protected final int SORT_DATE_OF_BIRTH = 1;
	protected final int SORT_QUEUE_DATE = 2;
	protected final int SORT_PLACEMENT_DATE = 3;
	public static final int SORT_ALL = 4;
	
	public Boolean _queueCleaned = null;

	private boolean _showQueueCleaning = false;
	private boolean showViewSiblingListButton = false;
	
    public static final int ORDER_BY_QUEUE_DATE = 1;    // see ChildCareApplicationBMPBean ORDER_BY_QUEUE_DATE
    public static final int ORDER_BY_DATE_OF_BIRTH = 2; // see ChildCareApplicationBMPBean ORDER_BY_DATE_OF_BIRTH

	/*
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
	    if (isCommuneAdministrator(iwc) || getSession().hasPrognosis()) {
			parseAction(iwc);
			addTable(iwc);
		}
		else {
			add(getSmallErrorText(localize("child_care.prognosis_must_be_set","Prognosis must be set or updated before you can continue!")));
		}
	}
	
	/**
     * @param iwc
     * @throws RemoteException
     */
    private void addTable(IWContext iwc) throws RemoteException {
        Table table = new Table(1,9);
        table.setWidth(getWidth());
        table.setHeight(2, 12);
        //table.setHeight(4, 3);
        table.setHeight(6, 6);
        table.setHeight(4, 6);
        table.setCellpadding(0);
        table.setCellspacing(0);
        table.setAlignment(1, 5, Table.HORIZONTAL_ALIGN_RIGHT);
        
        add(table);
        
        table.add(getSortTable(), 1, 1);
        //table.add(getNavigator(iwc), 1, 3);
        //table.add(getApplicationTable(iwc), 1, 5);
        table.add(getLegendTable(true), 1, 3);
        if(getShowViewSiblingListButton()) {
        	Table buttonTable = new Table(3,1);
            buttonTable.add(getSiblingListXLSLink(),1,1);            
            buttonTable.add(getPDFLink(), 2, 1);            
            buttonTable.add(getXSLLink(), 3, 1);
            buttonTable.setBorder(0);        
            buttonTable.setCellpadding(1);            
            table.add(buttonTable,1,5);
        } else {        	
            table.add(getPDFLink(), 1, 5);
            table.add(Text.getNonBrakingSpace(), 1, 5);
            table.add(getXSLLink(), 1, 5);
        }
                        
        table.add(getApplicationTable(iwc), 1, 7);
        table.add(getLegendTable(true), 1, 9);
    }

	
	private void parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_CLEAN_QUEUE)) {
			try {
			    QueueCleaningSession cleaningSession = (QueueCleaningSession)IBOLookup.getSessionInstance(iwc,QueueCleaningSession.class);
			    _queueCleaned = new Boolean(cleaningSession.cleanQueue(getSession().getChildCareID(),iwc.getCurrentUser()));
				//_queueCleaned = new Boolean(getBusiness().cleanQueue(getSession().getChildCareID(), iwc.getCurrentUser(), iwc));
			}
			catch (Exception fe) {
				//Nothing found and everyone is happy about that :D
			}
		}
		if (iwc.isParameterSet(PARAMETER_DELETE_APPLICATION)) {
			try {
				int applicationID = Integer.parseInt(iwc.getParameter(PARAMETER_DELETE_APPLICATION));
				getBusiness().deleteApplication(applicationID, iwc.getCurrentUser(), iwc.getCurrentLocale());
			}
			catch (RemoteException re) {
				log(re);
			}
			catch (NumberFormatException nfe) {
				log(nfe);
			}
		}
	}
	
	/*private CollectionNavigator getNavigator(IWContext iwc) throws RemoteException {
		if (getSession().getSortBy() != -1 && getSession().getSortBy() != SORT_ALL)
			_applicantsSize = getBusiness().getNumberOfApplicationsByProvider(getSession().getChildCareID(), getSession().getSortBy(), getSession().getFromTimestamp().getDate(), getSession().getToTimestamp().getDate());
		else
			_applicantsSize = getBusiness().getNumberOfApplicationsByProvider(getSession().getChildCareID());

		CollectionNavigator navigator = new CollectionNavigator(_applicantsSize);
		navigator.setEventListener(ChildCareEventListener.class);
		navigator.setTextStyle(STYLENAME_SMALL_TEXT);
		navigator.setLinkStyle(STYLENAME_SMALL_LINK);
		navigator.setNumberOfEntriesPerPage(_numberPerPage);
		navigator.setPadding(getCellpadding());
		_start = navigator.getStart(iwc);
		
		return navigator;
	}*/
	
	private Collection getApplicationCollection() throws RemoteException {
		Collection applications = null;
        
        int ordering = getOrdering(); 
        
		if (getSession().getSortBy() != -1 && getSession().getSortBy() != SORT_ALL){
			try {
				IWTimestamp stamp = new IWTimestamp();
				
				IWTimestamp stampFrom = getSession().getFromTimestamp();
				IWTimestamp stampTo = getSession().getToTimestamp();
				Date from =  null;
				Date to = null;
				if (stampFrom != null)
					from = stampFrom.getDate();
				if (stampTo != null)
					to = stampTo.getDate();
				
			
				if (from == null){
					stamp.addYears(-10);
					from = stamp.getDate();					
				}
				stamp = new IWTimestamp();
				
				if (to == null){
					stamp.addYears(10);
					to = stamp.getDate();					
				}
					
				//applications = getBusiness().getUnhandledApplicationsByProvider(getSession().getChildCareID(), _numberPerPage, _start, getSession().getSortBy(), getSession().getFromTimestamp().getDate(), getSession().getToTimestamp().getDate());
				applications = getBusiness()                
                        .getUnhandledApplicationsByProvider(
                                getSession().getChildCareID(), _numberPerPage,
                                _start, getSession().getSortBy(), from, to, ordering);
				
			}
			catch (Exception e){
				log(e);
				
			}
		}
		else
			applications = getBusiness().getUnhandledApplicationsByProvider(getSession().getChildCareID(), _numberPerPage, _start, ordering);
		return applications;
	}

    private int getOrdering() throws RemoteException {
        School provider = getSession().getProvider();
        int ordering;
        if (provider != null) {
            ordering = provider.getSortByBirthdate() ? ORDER_BY_DATE_OF_BIRTH : ORDER_BY_QUEUE_DATE;
        } else {
            ordering = ORDER_BY_QUEUE_DATE;
        }
        return ordering;
    }
	
	
	
	private Form getApplicationTable(IWContext iwc) throws RemoteException {
		Form form = new Form();

		Table applicationTable = new Table();
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		applicationTable.setCellpadding(getCellpadding());
		applicationTable.setCellspacing(getCellspacing());
		applicationTable.setColumns(8);
		applicationTable.setRowColor(1, getHeaderColor());
		form.add(applicationTable);
		int row = 1;
		int column = 1;
		int number =1;
		int queueOrder = -1;
		int netOrder = -1;
		boolean showComment = false;
		boolean showPriority = false;
		boolean showMessage = false;
			
		applicationTable.add(getLocalizedSmallHeader("child_care.number","No"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.order","Order"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row++);
        
		Collection applications = getApplicationCollection();
		
		int numberInqueueNoOffer = getBusiness().getNumberOfUnhandledApplicationsByProvider(getSession().getChildCareID());
		
		if (applications != null && !applications.isEmpty()) {
            
            int ordering = getOrdering(); 
            
			ChildCareApplication application;
			User child;
			IWCalendar queueDate;
			IWCalendar placementDate;
			Link link;
			SubmitButton delete = null;
			HorizontalRule hr;
			boolean hasOtherPlacing = false;
			boolean hasMessage = false;
			boolean hasSchoolPlacement = false;
			String name = null;
				
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				column = 1;
				application = (ChildCareApplication) iter.next();
				child = application.getChild();
				queueDate = new IWCalendar(iwc.getCurrentLocale(), application.getQueueDate());
				placementDate = new IWCalendar(iwc.getCurrentLocale(), application.getFromDate());
				
                queueOrder = getBusiness().getNumberInQueue(application, ordering);
				if (application.getApplicationStatus() == getBusiness().getStatusSentIn())
					netOrder = getBusiness().getNumberInQueueByStatus(application, ordering);
				else
					netOrder = -1;
                
				hasOtherPlacing = getBusiness().hasBeenPlacedWithOtherProvider(application.getChildId(), getSession().getChildCareID());
				hasSchoolPlacement = getBusiness().hasSchoolPlacement(child);
				hasMessage = application.getMessage() != null;		
				
				if (netOrder == 1 && row != 2) {
					hr = new HorizontalRule();
					applicationTable.mergeCells(1, row, applicationTable.getColumns(), row);
					applicationTable.setStyle(1, row, "padding", "0px");
					hr.setHeight(1);
					hr.setColor("#000000");
					applicationTable.add(hr, 1, row++);			
					//applicationTable.setColor(1, row++, "#000000");
				}
					
				if (application.getApplicationStatus() == getBusiness().getStatusAccepted()) {
					applicationTable.setRowColor(row, ACCEPTED_COLOR);
				}
				else if (application.getApplicationStatus() == getBusiness().getStatusParentsAccept()) {
					applicationTable.setRowColor(row, PARENTS_ACCEPTED_COLOR);
				}
				else if (application.getApplicationStatus() == getBusiness().getStatusContract()) {
					applicationTable.setRowColor(row, CONTRACT_COLOR);
				}
				else {
					if (application.getCaseStatus().equals(getBusiness().getCaseStatusPending())) {
						applicationTable.setRowColor(row, PENDING_COLOR);
					}
					else if (row % 2 == 0)
						applicationTable.setRowColor(row, getZebraColor1());
					else
						applicationTable.setRowColor(row, getZebraColor2());
				}
				
				//link = getSmallLink(child.getNameLastFirst(true));
				name = getBusiness().getUserBusiness().getNameLastFirst(child, true);
				link = getSmallLink(name);
				link.setEventListener(ChildCareEventListener.class);
				link.setParameter(getSession().getParameterUserID(), String.valueOf(application.getChildId()));
				link.setParameter(getSession().getParameterApplicationID(), application.getPrimaryKey().toString());
				link.setParameter(getSession().getParameterCaseCode(), CareConstants.CASE_CODE_KEY);
				if (getResponsePage() != null)
					link.setPage(getResponsePage());
	
				//enumerated list
				applicationTable.add(Integer.toString(number), column++, row);
				number++;
				
				if (hasMessage) {
					showMessage = true;
					applicationTable.add(getSmallErrorText("*"), column, row);
				}
				if (hasOtherPlacing) {
					showComment = true;
					applicationTable.add(getSmallErrorText("&Delta;"), column, row);
				}
				if (application.getHasQueuePriority()) {
					showPriority = true;
					applicationTable.add(getSmallErrorText("+"), column, row);
				}
				if (showComment || showPriority || showMessage)
					applicationTable.add(getSmallText(Text.NON_BREAKING_SPACE), column, row);
				
				
				applicationTable.add(link, column++, row);
				applicationTable.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				applicationTable.add(getSmallText(queueDate.getLocaleDate(IWCalendar.SHORT)), column++, row);
				applicationTable.add(getSmallText(placementDate.getLocaleDate(IWCalendar.SHORT)), column++, row);
				if (netOrder != -1)
					applicationTable.add(getSmallText(String.valueOf(netOrder)), column++, row);
				else 
					applicationTable.add(getSmallText("-"), column++, row);
				if (queueOrder != -1)
					applicationTable.add(getSmallText("("+String.valueOf(queueOrder)+")"), column++, row);
				else 
					applicationTable.add(getSmallText("-"), column++, row);
				
				if (hasSchoolPlacement) {
					delete = new SubmitButton(getDeleteIcon(localize("child_care.remove_application","Remove application")), PARAMETER_DELETE_APPLICATION, application.getPrimaryKey().toString());
					delete.setToolTip(localize("child_care.remove_application","Remove application"));
					delete.setSubmitConfirm(localize("child_care.remove_application_request", "Do you really want to remove the application?"));
					applicationTable.add(delete, column, row);
				}
				
				row++;
			}
			applicationTable.setColumnAlignment(1, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(7, Table.HORIZONTAL_ALIGN_CENTER);
		}
		
		applicationTable.mergeCells(1, row, applicationTable.getColumns(), row);
		applicationTable.add(new Break(), 1, row);
		applicationTable.add(getSmallHeader(localize("child_care.number_in_queue_no_offer","Number in queue without offer") + ": "), 1, row);
		applicationTable.add(getSmallText(String.valueOf(numberInqueueNoOffer)), 1, row++);
		
				
		if (showComment || showPriority || showMessage) {
			applicationTable.setHeight(row++, 2);
			if (showMessage) {
				applicationTable.mergeCells(1, row, applicationTable.getColumns(), row);
				applicationTable.add(getSmallErrorText("* "), 1, row);
				applicationTable.add(getSmallText(localize("child_care.has_message_in_application","The application has a message attached")), 1, row++);
			}
			if (showComment) {
				applicationTable.mergeCells(1, row, applicationTable.getColumns(), row);
				applicationTable.add(getSmallErrorText("&Delta; "), 1, row);
				applicationTable.add(getSmallText(localize("child_care.placed_at_other_provider","Placed at other provider")), 1, row++);
			}
			if (showPriority) {
				applicationTable.mergeCells(1, row, applicationTable.getColumns(), row);
				applicationTable.add(getSmallErrorText("+ "), 1, row);
				applicationTable.add(getSmallText(localize("child_care.has_priority","Child has priority")), 1, row++);
			}
		}
		
		
		
		if (_showQueueCleaning) {
			form.setEventListener(ChildCareEventListener.class);
			row++;
			applicationTable.mergeCells(1, row, applicationTable.getColumns(), row);
			applicationTable.setHeight(row, 12);
			
			SubmitButton button = (SubmitButton) getButton(new SubmitButton(localize("child_care.clean_queue", "Clean queue"), PARAMETER_CLEAN_QUEUE, Boolean.TRUE.toString()));
			applicationTable.add(button, 1, row);
			button.setSubmitConfirm(localize("child_care.clean_queue_request", "Do you really want to send the queue request?"));
			if (iwc.getSessionAttribute(ChildCareConstants.CLEAN_QUEUE_RUNNING) != null) {
				form.setDisabled(true);
			}
			form.setToDisableOnSubmit(button, true);
			
			if (_queueCleaned != null) {
				button.setDisabled(true);
				form.add(Text.getNonBrakingSpace());
				if (_queueCleaned.booleanValue()) {
					applicationTable.add(getSmallText(localize("child_care.clean_queue_successful", "Cleaning of queue ran successfully!")), 1, row);
				}
				else {
					applicationTable.add(getSmallErrorText(localize("child_care.clean_queue_failed", "Cleaning of queue failed!")), 1, row);
				}
			}
		}
		
		return form;
	}
	
	private Form getSortTable() throws RemoteException {
		Form form = new Form();
		form.setEventListener(ChildCareEventListener.class);
		//form.maintainParameter(CollectionNavigator.getParameterName());
		
		Table table = new Table(7,2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, "12");
		table.setWidth(4, "12");
		table.setWidth(6, "12");
		form.add(table);
		
		IWTimestamp stamp = new IWTimestamp();
		
		DateInput from = (DateInput) getStyledInterface(new DateInput(getSession().getParameterFrom(), true));
		table.add(from, 3, 2);		
		from.setAsNotEmpty(localize("child_care.must_select_from_date","You have to select a from date"));
		from.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
		if (getSession().getFromTimestamp() != null)
			from.setDate(getSession().getFromTimestamp().getDate());

		DateInput to = (DateInput) getStyledInterface(new DateInput(getSession().getParameterTo(), true));
		table.add(to, 5, 2);		
		to.setAsNotEmpty(localize("child_care.must_select_to_date","You have to select a to date"));
		to.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
		if (getSession().getToTimestamp() != null)
			to.setDate(getSession().getToTimestamp().getDate());

		DropdownMenu sortBy = (DropdownMenu) getStyledInterface(new DropdownMenu(getSession().getParameterSortBy()));
		table.add(sortBy, 1, 2);		
		sortBy.setAsNotEmpty(localize("child_care.sort_can_not_be_empty","You must select a sorting method"), "-1");
		sortBy.addMenuElement(-1, localize("child_care.sort","- Sort -"));
		sortBy.addMenuElement(SORT_DATE_OF_BIRTH, localize("child_care.date_of_birth","Date of birth"));
		sortBy.addMenuElement(SORT_QUEUE_DATE, localize("child_care.queue_date","Queue date"));
		sortBy.addMenuElement(SORT_PLACEMENT_DATE, localize("child_care.placement_date","Placement date"));
		if (getSession().getSortBy() != -1) {
			sortBy.setSelectedElement(getSession().getSortBy());
			sortBy.addMenuElement(SORT_ALL, localize("child_care.show_all","Show all"));
			sortBy.setToSubmit(String.valueOf(SORT_ALL));
		}

		SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.get_sorted","Get sorted")));
		
		table.add(getSmallHeader(localize("child_care.sort_by","Sort by")+":"), 1, 1);		
		table.add(getSmallHeader(localize("child_care.from","From")+":"), 3, 1);		
		table.add(getSmallHeader(localize("child_care.to","To")+":"), 5, 1);		

		table.add(submit, 7, 2);
		
		return form;
	}
	
	protected Link getPDFLink() throws RemoteException {		
		DownloadLink link = new DownloadLink(getBundle().getImage("shared/pdf.gif"));
		link.setMediaWriterClass(ChildCareQueueWriter.class);
		link.addParameter(ChildCareQueueWriter.PARAMETER_TYPE, ChildCareQueueWriter.PDF);
		link.addParameter(ChildCareQueueWriter.PARAMETER_PROVIDER_ID, getSession().getChildCareID());
		link.addParameter(ChildCareQueueWriter.PARAMETER_SORT_BY, getSession().getSortBy());
		link.addParameter(ChildCareQueueWriter.PARAMETER_NUMBER_PER_PAGE, _numberPerPage);
		link.addParameter(ChildCareQueueWriter.PARAMETER_START, _start);
		if (getSession().getFromTimestamp() != null)
			link.addParameter(ChildCareQueueWriter.PARAMETER_FROM_DATE, String.valueOf(getSession().getFromTimestamp().getDate()));
		if (getSession().getToTimestamp() != null)
			link.addParameter(ChildCareQueueWriter.PARAMETER_TO_DATE, String.valueOf(getSession().getToTimestamp().getDate()));
		
		return link;
	}
	
	protected Link getXSLLink() throws RemoteException {
		DownloadLink link = new DownloadLink(getBundle().getImage("shared/xls.gif"));
		link.setMediaWriterClass(ChildCareQueueWriter.class);
		link.addParameter(ChildCareQueueWriter.PARAMETER_TYPE, ChildCareQueueWriter.XLS);
		link.addParameter(ChildCareQueueWriter.PARAMETER_PROVIDER_ID, getSession().getChildCareID());
		link.addParameter(ChildCareQueueWriter.PARAMETER_SORT_BY, getSession().getSortBy());
		link.addParameter(ChildCareQueueWriter.PARAMETER_NUMBER_PER_PAGE, _numberPerPage);
		link.addParameter(ChildCareQueueWriter.PARAMETER_START, _start);
		if (getSession().getFromTimestamp() != null)
			link.addParameter(ChildCareQueueWriter.PARAMETER_FROM_DATE, String.valueOf(getSession().getFromTimestamp().getDate()));
		if (getSession().getToTimestamp() != null)
			link.addParameter(ChildCareQueueWriter.PARAMETER_TO_DATE, String.valueOf(getSession().getToTimestamp().getDate()));
		

		return link;
	}
	
	private Form getSiblingListButton(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.setAction(iwc.getIWMainApplication().getMediaServletURI());
		form.addParameter(DownloadWriter.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(ChildCareSiblingListWriter.class));		
		form.addParameter(ChildCareSiblingListWriter.PARAMETER_PROVIDER_ID, getSession().getChildCareID());
		form.addParameter(ChildCareSiblingListWriter.PARAMETER_SORT_BY, getSession().getSortBy());
		form.addParameter(ChildCareSiblingListWriter.PARAMETER_NUMBER_PER_PAGE, _numberPerPage);
		form.addParameter(ChildCareSiblingListWriter.PARAMETER_START, _start);
		SubmitButton button = (SubmitButton) getButton(new SubmitButton(localize("child_care.sibling_list", "See sibling list"),
		PARAMETER_CLEAN_QUEUE,		
		Boolean.TRUE.toString()));
		form.setToShowLoadingOnSubmit(true);		
		form.setToDisableOnSubmit(button, true);
		form.add(button);		
		return form;
		}

	protected Link getSiblingListXLSLink() throws RemoteException {
		DownloadLink link = new DownloadLink(getBundle().getImage("shared/xls.gif"));
		link.setMediaWriterClass(ChildCareQueueWriter.class);
		
		link.addParameter(DownloadWriter.PRM_WRITABLE_CLASS, IWMainApplication.getEncryptedClassName(ChildCareSiblingListWriter.class));		
		link.addParameter(ChildCareSiblingListWriter.PARAMETER_PROVIDER_ID, getSession().getChildCareID());
		link.addParameter(ChildCareSiblingListWriter.PARAMETER_SORT_BY, getSession().getSortBy());
		link.addParameter(ChildCareSiblingListWriter.PARAMETER_NUMBER_PER_PAGE, _numberPerPage);
		link.addParameter(ChildCareSiblingListWriter.PARAMETER_START, _start);
		
		
		/*if (getSession().getFromTimestamp() != null)
			link.addParameter(ChildCareQueueWriter.PARAMETER_FROM_DATE, String.valueOf(getSession().getFromTimestamp().getDate()));
		if (getSession().getToTimestamp() != null)
			link.addParameter(ChildCareQueueWriter.PARAMETER_TO_DATE, String.valueOf(getSession().getToTimestamp().getDate()));*/
		return link;
	}
	
	
	/**
	 * @param showQueueCleaning The showQueueCleaning to set.
	 */
	public void setShowQueueCleaning(boolean showQueueCleaning) {
		_showQueueCleaning = showQueueCleaning;
	}

	public boolean getShowViewSiblingListButton() {
		return showViewSiblingListButton;
	}

	public void setShowViewSiblingListButton(boolean viewSiblingListButton) {
		showViewSiblingListButton = viewSiblingListButton;
	}
}