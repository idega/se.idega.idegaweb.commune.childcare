package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.presentation.CollectionNavigator;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
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

	private int _numberPerPage = 15;
	private int _start = 0;
	private int _applicantsSize = 0;

	protected final int SORT_DATE_OF_BIRTH = 1;
	protected final int SORT_QUEUE_DATE = 2;
	protected final int SORT_PLACEMENT_DATE = 3;
	public static final int SORT_ALL = 4;

	/*
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		if (getSession().hasPrognosis()) {
		
			Table table = new Table(1,7);
			table.setWidth(getWidth());
			table.setHeight(2, 12);
			table.setHeight(4, 3);
			table.setHeight(6, 6);
			table.setCellpadding(0);
			table.setCellspacing(0);
			add(table);
			
			table.add(getSortTable(), 1, 1);
			table.add(getNavigator(iwc), 1, 3);
			table.add(getApplicationTable(iwc), 1, 5);
			table.add(getLegendTable(), 1, 7);
		}
		else {
			add(getSmallErrorText(localize("child_care.prognosis_must_be_set","Prognosis must be set or updated before you can continue!")));
		}
	}
	
	private CollectionNavigator getNavigator(IWContext iwc) throws RemoteException {
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
	}
	
	private Collection getApplicationCollection() throws RemoteException {
		Collection applications;
		if (getSession().getSortBy() != -1 && getSession().getSortBy() != SORT_ALL)
			applications = getBusiness().getUnhandledApplicationsByProvider(getSession().getChildCareID(), _numberPerPage, _start, getSession().getSortBy(), getSession().getFromTimestamp().getDate(), getSession().getToTimestamp().getDate());
		else
			applications = getBusiness().getUnhandledApplicationsByProvider(getSession().getChildCareID(), _numberPerPage, _start);
		return applications;
	}
	
	private Table getApplicationTable(IWContext iwc) throws RemoteException {
		Table applicationTable = new Table();
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		applicationTable.setCellpadding(getCellpadding());
		applicationTable.setCellspacing(getCellspacing());
		applicationTable.setColumns(6);
		applicationTable.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
		int queueOrder = -1;
		int netOrder = -1;
		boolean showComment = false;
			
		applicationTable.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.queue_date","Queue date"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.placement_date","Placement date"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.order","Order"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row++);
				
		Collection applications = getApplicationCollection();
		if (applications != null && !applications.isEmpty()) {
			ChildCareApplication application;
			User child;
			IWCalendar queueDate;
			IWCalendar placementDate;
			Link link;
			boolean hasOtherPlacing = false;
				
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				column = 1;
				application = (ChildCareApplication) iter.next();
				child = application.getChild();
				queueDate = new IWCalendar(iwc.getCurrentLocale(), application.getCreated());
				placementDate = new IWCalendar(iwc.getCurrentLocale(), application.getFromDate());
				queueOrder = getBusiness().getNumberInQueue(application);
				if (application.getApplicationStatus() == getBusiness().getStatusSentIn())
					netOrder = getBusiness().getNumberInQueueByStatus(application);
				else
					netOrder = -1;
				hasOtherPlacing = getBusiness().hasBeenPlacedWithOtherProvider(application.getChildId(), getSession().getChildCareID());
						
				if (netOrder == 1 && row != 2) {
					applicationTable.mergeCells(1, row, applicationTable.getColumns(), row);
					applicationTable.setStyle(1, row, "padding", "0px");
					applicationTable.setColor(1, row++, "#000000");
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
					if (row % 2 == 0)
						applicationTable.setRowColor(row, getZebraColor1());
					else
						applicationTable.setRowColor(row, getZebraColor2());
				}
					
				link = (Link) this.getSmallLink(child.getNameLastFirst(true));
				link.setEventListener(ChildCareEventListener.class);
				link.setParameter(getSession().getParameterUserID(), String.valueOf(application.getChildId()));
				link.setParameter(getSession().getParameterApplicationID(), application.getPrimaryKey().toString());
				if (getResponsePage() != null)
					link.setPage(getResponsePage());
	
				if (hasOtherPlacing) {
					showComment = true;
					applicationTable.add(getSmallErrorText("* "), column, row);
				}
					
				applicationTable.add(link, column++, row);
				applicationTable.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				applicationTable.add(getSmallText(queueDate.getLocaleDate(IWCalendar.SHORT)), column++, row);
				applicationTable.add(getSmallText(placementDate.getLocaleDate(IWCalendar.SHORT)), column++, row);
				if (netOrder != -1)
					applicationTable.add(getSmallText(String.valueOf(netOrder)), column++, row);
				else 
					applicationTable.add(getSmallText("-"), column++, row);
				if (queueOrder != -1)
					applicationTable.add(getSmallText("("+String.valueOf(queueOrder)+")"), column, row++);
				else 
					applicationTable.add(getSmallText("-"), column, row++);
			}
			applicationTable.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		}
			
		if (showComment) {
			applicationTable.setHeight(2, row++);
			applicationTable.mergeCells(1, row, applicationTable.getColumns(), row);
			applicationTable.add(getSmallErrorText("* "), 1, row);
			applicationTable.add(getSmallText(localize("child_care.placed_at_other_provider","Placed at other provider")), 1, row);
		}
		
		return applicationTable;
	}
	
	private Form getSortTable() {
		Form form = new Form();
		form.setEventListener(ChildCareEventListener.class);
		form.maintainParameter(CollectionNavigator.getParameterName());
		
		Table table = new Table(7,2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, "12");
		table.setWidth(4, "12");
		table.setWidth(6, "12");
		form.add(table);
		
		IWTimestamp stamp = new IWTimestamp();
		
		DateInput from = (DateInput) getStyledInterface(new DateInput(getSession().getParameterFrom(), true));
		from.setAsNotEmpty(localize("child_care.must_select_from_date","You have to select a from date"));
		from.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
		if (getSession().getFromTimestamp() != null)
			from.setDate(getSession().getFromTimestamp().getDate());

		DateInput to = (DateInput) getStyledInterface(new DateInput(getSession().getParameterTo(), true));
		to.setAsNotEmpty(localize("child_care.must_select_to_date","You have to select a to date"));
		to.setYearRange(stamp.getYear() - 11, stamp.getYear()+3);
		if (getSession().getToTimestamp() != null)
			to.setDate(getSession().getToTimestamp().getDate());

		DropdownMenu sortBy = (DropdownMenu) getStyledInterface(new DropdownMenu(getSession().getParameterSortBy()));
		sortBy.setAsNotEmpty(localize("child_care.sort_can_not_be_empty","You must select a sorting method"), "-1");
		sortBy.addMenuElement(-1, localize("child_care.sort","- Sort -"));
		sortBy.addMenuElement(SORT_DATE_OF_BIRTH, localize("child_care.date_of_birth","Date of birth"));
		sortBy.addMenuElement(SORT_QUEUE_DATE, localize("child_care.queue_date","Queue date"));
		sortBy.addMenuElement(SORT_PLACEMENT_DATE, localize("child_care.placement_date","Placement date"));
		if (getSession().getSortBy() != -1) {
			sortBy.setSelectedElement(getSession().getSortBy());
			sortBy.addMenuElement(SORT_ALL, localize("child_care.show_all","Show all"));
		}

		SubmitButton submit = (SubmitButton) getStyledInterface(new SubmitButton(localize("child_care.get_sorted","Get sorted")));
		
		table.add(getSmallHeader(localize("child_care.sort_by","Sort by")+":"), 1, 1);		
		table.add(getSmallHeader(localize("child_care.from","From")+":"), 3, 1);		
		table.add(getSmallHeader(localize("child_care.to","To")+":"), 5, 1);		

		table.add(sortBy, 1, 2);		
		table.add(from, 3, 2);		
		table.add(to, 5, 2);		
		table.add(submit, 7, 2);
		
		return form;
	}
}