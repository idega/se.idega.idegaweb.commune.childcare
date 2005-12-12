/*
 * $Id$
 * Created on Aug 3, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.care.business.CareConstants;
import se.idega.idegaweb.commune.care.business.Relative;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolArea;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.localisation.data.ICLanguage;
import com.idega.core.localisation.data.ICLanguageHome;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.text.HorizontalRule;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TimeInput;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.TextSoap;

/**
 * Last modified: $Date$ by $Author$
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision$
 */
public class ChildCareDetailedApplication extends ChildCareBlock {

	protected static final int ACTION_PHASE_1 = 1;
	protected static final int ACTION_PHASE_2 = 2;
	protected static final int ACTION_PHASE_3 = 3;
	protected static final int ACTION_PHASE_4 = 4;
	protected static final int ACTION_PHASE_5 = 5;
	protected static final int ACTION_OVERVIEW = 10;
	protected static final int ACTION_SAVE = 0;
	
	protected static final String PARAMETER_ACTION = "prm_action";

	private static final String PARAMETER_PROVIDER = "prm_provider";
	private static final String PARAMETER_AREA = "prm_area";
	private static final String PARAMETER_DATE = "prm_date";
	private static final String PARAMETER_MESSAGE = "prm_message";
	private static final String PARAMETER_FROM_TIME = "prm_from_time";
	private static final String PARAMETER_TO_TIME = "prm_to_time";

	private static final String PARAMETER_USER = "prm_user_id";
	private static final String PARAMETER_RELATIVE = "prm_relative_user_id";
	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_HOME_PHONE = "prm_home";
	private static final String PARAMETER_WORK_PHONE = "prm_work";
	private static final String PARAMETER_MOBILE_PHONE = "prm_mobile";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_RELATION = "prm_relation";
	
	protected static final String PARAMETER_GROWTH_DEVIATION = "prm_growth_deviation";
	protected static final String PARAMETER_GROWTH_DEVIATION_DETAILS = "prm_growth_deviation_details";
	protected static final String PARAMETER_ALLERGIES = "prm_allergies";
	protected static final String PARAMETER_ALLERGIES_DETAILS = "prm_allergies_details";
	protected static final String PARAMETER_LAST_CARE_PROVIDER = "prm_last_care_provider";
	protected static final String PARAMETER_CAN_CONTACT_LAST_PROVIDER = "prm_can_contact_last_provider";
	protected static final String PARAMETER_OTHER_INFORMATION = "prm_other_information";
	protected static final String PARAMETER_CAN_DISPLAY_IMAGES = "prm_can_display_images";
	protected static final String PARAMETER_CHILD_CARE_INFORMATION = "prm_child_care_information";
	protected static final String PARAMETER_HAS_MULTI_LANGUAGE_HOME = "prm_multi_language_home";
	protected static final String PARAMETER_LANGUAGE= "prm_language";

	protected static final String PARAMETER_CUSTODIAN_HAS_STUDIES = "prm_custodian_has_studies";
	protected static final String PARAMETER_CUSTODIAN_STUDIES = "prm_custodian_studies";
	protected static final String PARAMETER_CUSTODIAN_STUDY_START = "prm_custodian_study_start";
	protected static final String PARAMETER_CUSTODIAN_STUDY_END = "prm_custodian_study_end";

	private final static String PARAM_PROVIDER = "cca_provider";
	private final static String CARE_FROM = "cca_care_from";
	private final static String EMAIL_PROVIDER_SUBJECT = "child_care.application_received_subject";
	private final static String EMAIL_PROVIDER_MESSAGE = "child_care.application_received_body";
	private final static String APPLICATION_INSERTED = "cca_application_ok";

	protected boolean iShowOverview = false;
	private boolean iUseSessionUser = false;
	private int iNumberOfApplications = 5;
	
	protected ICPage iHomePage;

	private Collection areas;
	private Map providerMap;
	private School currentProvider;
	private boolean hasActivePlacement = false;
	
	public void init(IWContext iwc) throws Exception {
		if (getSession().getChild() != null) {
			try {
				currentProvider = getBusiness().getCurrentProviderByPlacement(((Integer) getSession().getChild().getPrimaryKey()).intValue());
				hasActivePlacement = getBusiness().hasActiveApplication(((Integer) getSession().getChild().getPrimaryKey()).intValue(), CareConstants.CASE_CODE_KEY);
			}
			catch (RemoteException re) {
				currentProvider = null;
				hasActivePlacement = false;
			}
		}
		else {
			return;
		}

		switch (parseAction(iwc)) {
			case ACTION_PHASE_1:
				showPhaseOne(iwc);
				break;
			
			case ACTION_PHASE_2:
				showPhaseTwo(iwc);
				break;
				
			case ACTION_PHASE_3:
				showPhaseThree(iwc);
				break;
				
			case ACTION_PHASE_4:
				showPhaseFour(iwc);
				break;
				
			case ACTION_PHASE_5:
				showPhaseFive(iwc);
				break;
				
			case ACTION_OVERVIEW:
				showOverview(iwc);
				break;
				
			case ACTION_SAVE:
				save(iwc);
				break;
		}
	}
	
	protected User getUser(IWContext iwc) throws RemoteException {
		if (iUseSessionUser) {
			return getUserSession(iwc).getUser();
		}
		else {
			return iwc.getCurrentUser();
		}
	}

	protected void showPhaseOne(IWContext iwc) throws RemoteException {
		User child = getSession().getChild();

		Form form = createForm();
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getChild()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.provider_information", "Child care provider information")), 1, row++);
		table.setHeight(row++, 6);
		
		Table applicationTable = new Table();
		applicationTable.setCellpadding(3);
		applicationTable.setCellspacing(0);
		table.add(applicationTable, 1, row++);
		table.setHeight(row++, 12);
		int iRow = 1;
		
		String provider = localize(PARAM_PROVIDER, "Provider");
		String from = localize(CARE_FROM, "From") + ":";
		String message = null;
		Text providerText = null;
		Text fromText = getSmallHeader(from);
		IWTimestamp stamp = new IWTimestamp();

		ChildCareApplication application = null;
		int areaID = -1;
		int numberOfApplications = hasActivePlacement ? (iNumberOfApplications - 1) : iNumberOfApplications;
		if (getBusiness().getAcceptedApplicationsByChild(((Integer) child.getPrimaryKey()).intValue()) != null) {
			numberOfApplications--;
		}
		
		for (int a = 1; a <= numberOfApplications; a++) {
			try {
				application = getBusiness().getNonActiveApplication(((Integer) child.getPrimaryKey()).intValue(), a);
				if (application != null) {
					areaID = application.getProvider().getSchoolAreaId();
					message = application.getMessage();
				}
			}
			catch (RemoteException re) {
				application = null;
			}
			
			ProviderDropdownDouble dropdown = (ProviderDropdownDouble) getStyledInterface(getDropdown(iwc.getCurrentLocale(), PARAMETER_AREA + "_" + a, PARAMETER_PROVIDER + "_" + a));
			if (application != null) {
				dropdown.setSelectedValues(String.valueOf(areaID), String.valueOf(application.getProviderId()));
			}
			providerText = getSmallHeader(provider + Text.NON_BREAKING_SPACE + a + ":");
			applicationTable.add(providerText, 1, iRow);
			applicationTable.add(dropdown, 2, iRow++);

			DateInput date = (DateInput)getStyledInterface(new DateInput(PARAMETER_DATE + "_" + a));
			if (application != null){
				IWTimestamp fromDate = new IWTimestamp(application.getFromDate());
				
				if (fromDate.isEarlierThan(stamp)) {
					date.setEarliestPossibleDate(application.getFromDate(), localize("child_care.no_date_back_prev", "You cannot set a date before the previous start date"));	
				}
				else {
					date.setEarliestPossibleDate(stamp.getDate(), localize("child_care.no_date_back_in_time", "You cannot set a date back in time"));	
				}
				date.setDate(application.getFromDate());
				
			}
			else {
				date.setEarliestPossibleDate(stamp.getDate(), localize("child_care.no_date_back_in_time", "You cannot set a date back in time"));
			}

			applicationTable.add(fromText, 1, iRow);
			applicationTable.add(date, 2, iRow++);
			applicationTable.setHeight(iRow++, 12);
		}
		
		TextArea messageArea = (TextArea) getStyledInterface(new TextArea(PARAMETER_MESSAGE, message));
		messageArea.setWidth(Table.HUNDRED_PERCENT);
		messageArea.setRows(6);
		messageArea.keepStatusOnAction(true);
		
		table.add(getText(localize("application.other_school_message", "Message")), 1, row++);
		table.add(messageArea, 1, row++);
		table.setHeight(row++, 18);
		
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2)));
		
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_child_care_application_phase_1"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}

	protected void showPhaseTwo(IWContext iwc) throws RemoteException {
		saveCustodianInfo(iwc, false);

		Form form = createForm();
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getChild()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.from_to_time_information", "From/To time information")), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.from_to_time_details", "From/To time details...")), 1, row++);
		table.setHeight(row++, 18);
		
		Table applicationTable = new Table(2, 2);
		applicationTable.setCellpadding(3);
		applicationTable.setCellspacing(0);
		table.add(applicationTable, 1, row++);
		table.setHeight(row++, 12);

		TimeInput fromTime = (TimeInput) getStyledInterface(new TimeInput(PARAMETER_FROM_TIME));
		fromTime.keepStatusOnAction(true);
		fromTime.setFromHour(8);
		fromTime.setToHour(17);
		fromTime.setMinuteInterval(15);
		applicationTable.add(getSmallHeader(localize("application.from_time", "From time")), 1, 1);
		applicationTable.add(fromTime, 2, 1);
		
		TimeInput toTime = (TimeInput) getStyledInterface(new TimeInput(PARAMETER_TO_TIME));
		toTime.keepStatusOnAction(true);
		toTime.setFromHour(8);
		toTime.setToHour(17);
		toTime.setMinuteInterval(15);
		applicationTable.add(getSmallHeader(localize("application.to_time", "To time")), 1, 2);
		applicationTable.add(toTime, 2, 2);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_1)));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next"), PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3)));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_child_care_application_phase_2"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	protected void showPhaseThree(IWContext iwc) throws RemoteException {
		saveCustodianInfo(iwc, true);
		
		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getChild()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.custodian_information", "Custodian information")), 1, row++);
		table.setHeight(row++, 18);
		
		Table applicationTable = new Table();
		applicationTable.setCellpadding(0);
		applicationTable.setCellspacing(0);
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(applicationTable, 1, row++);
		int aRow = 1;

		Collection custodians = getUserBusiness(iwc).getParentsForChild(getSession().getChild());
		Iterator iter = custodians.iterator();
		while (iter.hasNext()) {
			User custodian = (User) iter.next();
			aRow = addParentToTable(iwc, applicationTable, custodian, aRow, false, 0, false);
			
			if (iter.hasNext()) {
				applicationTable.setHeight(aRow++, 6);
				applicationTable.mergeCells(1, aRow, applicationTable.getColumns(), aRow);
				applicationTable.add(new HorizontalRule(), 1, aRow++);
				applicationTable.setHeight(aRow++, 6);
			}
		}

		applicationTable.setHeight(aRow++, 6);
		applicationTable.mergeCells(1, aRow, applicationTable.getColumns(), aRow);
		applicationTable.add(new HorizontalRule(), 1, aRow++);
		applicationTable.setHeight(aRow++, 6);
		
		User custodian = getCareBusiness().getExtraCustodian(getSession().getChild());
		if (iwc.isParameterSet(PARAMETER_PERSONAL_ID)) {
			saveCustodianInfo(iwc, false);
			
			String personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);
			try {
				custodian = getUserBusiness().getUser(personalID);
			}
			catch (FinderException fe) {
				getParentPage().setAlertOnLoad(localize("no_user_found_with_personal_id", "No user found with personal ID") + ": " + personalID);
			}
		}
		
		addParentToTable(iwc, applicationTable, custodian, aRow, false, 0, true);
		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_2));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_child_care_application_phase_3"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}
	
	private int addParentToTable(IWContext iwc, Table table, User custodian, int row, boolean isExtraCustodian, int number, boolean editable) throws RemoteException {
		Address address = null;
		Phone phone = null;
		Phone work = null;
		Phone mobile = null;
		Email email = null;

		if (custodian != null) {
			address = getUserBusiness(iwc).getUsersMainAddress(custodian);

			try {
				phone = getUserBusiness(iwc).getUsersHomePhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				phone = null;
			}
			
			try {
				work = getUserBusiness(iwc).getUsersWorkPhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				work = null;
			}
			
			try {
				mobile = getUserBusiness(iwc).getUsersMobilePhone(custodian);
			}
			catch (NoPhoneFoundException npfe) {
				mobile = null;
			}
			
			try {
				email = getUserBusiness(iwc).getUsersMainEmail(custodian);
			}
			catch (NoEmailFoundException nefe) {
				email = null;
			}
		}

		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("name", "Name")), 1, row);
		table.add(getText(localize("personal_id", "Personal ID")), 2, row++);
		if (custodian != null) {
			table.add(new HiddenInput(!isExtraCustodian ? PARAMETER_USER : PARAMETER_RELATIVE, custodian.getPrimaryKey().toString()), 1, row);
		}
		if (!editable) {
			table.setCellpaddingRight(1, row, 5);
			table.setCellpaddingLeft(2, row, 5);
			table.add(getTextInput("name", custodian.getName(), true), 1, row);
			table.add(getTextInput("personalID", PersonalIDFormatter.format(custodian.getPersonalID(), iwc.getCurrentLocale()), true), 2, row++);
		}
		else {
			TextInput personalID = (TextInput) getStyledInterface(new TextInput(PARAMETER_PERSONAL_ID + (isExtraCustodian ? "_" + number : "")));
			personalID.setLength(10);
			personalID.setAsPersonalID(iwc.getCurrentLocale(), localize("not_valid_personal_id", "Not a valid personal ID"));
			if (custodian != null) {
				personalID.setContent(custodian.getPersonalID());
			}
			
			SubmitButton search = (SubmitButton) getButton(new SubmitButton(localize("search", "Search")));
			
			table.setCellpaddingRight(1, row, 5);
			table.setCellpaddingLeft(2, row, 5);
			table.add(getTextInput("name", custodian != null ? custodian.getName() : null, true), 1, row);
			table.add(personalID, 2, row);
			table.add(Text.getNonBrakingSpace(), 2, row);
			table.add(search, 2, row++);
		}
		table.setHeight(row++, 5);
		
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("address", "Address")), 1, row);
		table.add(getText(localize("zip_code", "Zip code")), 2, row++);
		TextInput addr = getTextInput("address", null, true);
		TextInput zip = getTextInput("zipCode", null, true);
		if (address != null) {
			addr.setContent(address.getStreetAddress());
			PostalCode code = address.getPostalCode();
			if (code != null) {
				zip.setContent(code.getPostalAddress());
			}
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(addr, 1, row);
		table.add(zip, 2, row++);
		table.setHeight(row++, 5);

		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("home_phone", "Home phone")), 1, row);
		table.add(getText(localize("work_phone", "Work phone")), 2, row++);
		TextInput homePhone = getTextInput(PARAMETER_HOME_PHONE, null, false);
		TextInput workPhone = getTextInput(PARAMETER_WORK_PHONE, null, false);
		if (phone != null) {
			homePhone.setContent(phone.getNumber());
		}
		if (custodian != null && isExtraCustodian) {
			homePhone.setAsNotEmpty(localize("must_enter_home_phone", "You must enter a home phone for relative."));
		}
		if (work != null) {
			workPhone.setContent(work.getNumber());
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(homePhone, 1, row);
		table.add(workPhone, 2, row++);
		table.setHeight(row++, 5);

		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("mobile_phone", "Mobile phone")), 1, row);
		table.add(getText(localize("email", "E-mail")), 2, row++);
		TextInput mobilePhone = getTextInput(PARAMETER_MOBILE_PHONE, null, false);
		TextInput mail = getTextInput(PARAMETER_EMAIL, null, false);
		if (mobile != null) {
			mobilePhone.setContent(mobile.getNumber());
		}
		if (email != null) {
			mail.setContent(email.getEmailAddress());
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(mobilePhone, 1, row);
		table.add(mail, 2, row++);
		table.setHeight(row++, 5);
		
		table.add(getText(localize("relation", "Relation")), 1, row++);
		DropdownMenu relationMenu = getRelationDropdown(custodian);
		if (custodian != null) {
			String relation = getCareBusiness().getUserRelation(getSession().getChild(), custodian);
			if (relation != null) {
				relationMenu.setSelectedElement(relation);
			}
			relationMenu.setAsNotEmpty(localize("must_select_relation", "You must select a relation to the child."), "");
		}
		table.add(relationMenu, 1, row++);
		
		return row;
	}

	private int addRelativeToTable(Table table, Relative relative, int row) {
		table.setCellpaddingRight(1, row, 5);
		table.add(getText(localize("name", "Name")), 1, row++);
		table.mergeCells(1, row, 2, row);
		table.add(getTextInput(PARAMETER_RELATIVE, relative != null ? relative.getName() : null, false), 1, row++);
		table.setHeight(row++, 5);
		
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("home_phone", "Home phone")), 1, row);
		table.add(getText(localize("work_phone", "Work phone")), 2, row++);
		TextInput homePhone = getTextInput(PARAMETER_HOME_PHONE, null, false);
		TextInput workPhone = getTextInput(PARAMETER_WORK_PHONE, null, false);
		if (relative != null && relative.getHomePhone() != null) {
			homePhone.setContent(relative.getHomePhone());
		}
		if (relative != null && relative.getWorkPhone() != null) {
			workPhone.setContent(relative.getWorkPhone());
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(homePhone, 1, row);
		table.add(workPhone, 2, row++);
		table.setHeight(row++, 5);

		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(getText(localize("mobile_phone", "Mobile phone")), 1, row);
		table.add(getText(localize("email", "E-mail")), 2, row++);
		TextInput mobilePhone = getTextInput(PARAMETER_MOBILE_PHONE, null, false);
		TextInput mail = getTextInput(PARAMETER_EMAIL, null, false);
		if (relative != null && relative.getMobilePhone() != null) {
			mobilePhone.setContent(relative.getMobilePhone());
		}
		if (relative != null && relative.getEmail() != null) {
			mail.setContent(relative.getEmail());
		}
		table.setCellpaddingRight(1, row, 5);
		table.setCellpaddingLeft(2, row, 5);
		table.add(mobilePhone, 1, row);
		table.add(mail, 2, row++);
		table.setHeight(row++, 5);
		
		table.add(getText(localize("relation", "Relation")), 1, row++);
		DropdownMenu relationMenu = getRelationDropdown(null);
		if (relative != null && relative.getRelation() != null) {
			relationMenu.setSelectedElement(relative.getRelation());
		}
		table.add(relationMenu, 1, row++);
		
		return row;
	}

	protected void showPhaseFour(IWContext iwc) throws RemoteException {
		saveCustodianInfo(iwc, false);
		saveChildInfo(iwc);

		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, ACTION_PHASE_4);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getChild()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getText(localize("application.relative_information", "Relative information")), 1, row++);
		table.setHeight(row++, 18);
		
		Table applicationTable = new Table();
		applicationTable.setCellpadding(0);
		applicationTable.setCellspacing(0);
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(applicationTable, 1, row++);
		int aRow = 1;

		List relatives = getCareBusiness().getRelatives(getSession().getChild());
		for (int a = 1; a <= 2; a++) {
			Relative relative = null;
			if (relatives.size() >= a) {
				relative = (Relative) relatives.get(a - 1);
			}

			aRow = addRelativeToTable(applicationTable, relative, aRow);
			
			if (a == 1) {
				applicationTable.setHeight(aRow++, 6);
				applicationTable.mergeCells(1, aRow, applicationTable.getColumns(), aRow);
				applicationTable.add(new HorizontalRule(), 1, aRow++);
				applicationTable.setHeight(aRow++, 6);
			}
		}

		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_3));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_child_care_application_phase_4"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);

		add(form);
	}

	protected void showPhaseFive(IWContext iwc) throws RemoteException {
		saveCustodianInfo(iwc, true);

		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getChild()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.child_information", "Child information")), 1, row++);
		table.setHeight(row++, 6);
		
		Table applicationTable = new Table();
		applicationTable.setColumns(4);
		applicationTable.setCellpadding(3);
		applicationTable.setCellspacing(0);
		table.add(applicationTable, 1, row++);
		int aRow = 1;
		
		applicationTable.add(getSmallHeader(localize("yes", "Yes")), 2, aRow);
		applicationTable.add(getSmallHeader(localize("no", "No")), 3, aRow);
		applicationTable.add(getSmallHeader(localize("no_answer", "Won't answer")), 4, aRow++);
		
		applicationTable.add(getSmallHeader(localize("child.multi_language_home", "Multi language home")), 1, aRow);
		RadioButton yes = getRadioButton(PARAMETER_HAS_MULTI_LANGUAGE_HOME, Boolean.TRUE.toString());
		RadioButton no = getRadioButton(PARAMETER_HAS_MULTI_LANGUAGE_HOME, Boolean.FALSE.toString());
		boolean hasMultiLanguageHome = getBusiness().hasMultiLanguageHome(getSession().getChild());
		if (hasMultiLanguageHome) {
			yes.setSelected(true);
		}
		else {
			no.setSelected(true);
		}
		applicationTable.add(yes, 2, aRow);
		applicationTable.add(no, 3, aRow++);

		applicationTable.add(getSmallHeader(localize("child.other_native_language", "Other native language") + ":"), 1, aRow);
		applicationTable.add(Text.getNonBrakingSpace(), 1, aRow);
		applicationTable.add(getNativeLanguagesDropdown(getBusiness().getLanguage(getSession().getChild())), 1, aRow++);
		
		applicationTable.setHeight(aRow++, 12);
		
		applicationTable.add(getSmallHeader(localize("custodian.has_studies", "Has studies")), 1, aRow);
		yes = getRadioButton(PARAMETER_CUSTODIAN_HAS_STUDIES, Boolean.TRUE.toString());
		no = getRadioButton(PARAMETER_CUSTODIAN_HAS_STUDIES, Boolean.FALSE.toString());
		boolean hasStudies = getBusiness().hasStudies(iwc.getCurrentUser());
		if (hasStudies) {
			yes.setSelected(true);
		}
		else {
			no.setSelected(true);
		}
		applicationTable.add(yes, 2, aRow);
		applicationTable.add(no, 3, aRow++);
		
		applicationTable.add(getSmallHeader(localize("custodian.studies", "Studies")), 1, aRow);
		applicationTable.add(new Break(), 1, aRow);
		applicationTable.add(getTextArea(PARAMETER_CUSTODIAN_STUDIES, getBusiness().getStudies(iwc.getCurrentUser())), 1, aRow++);

		applicationTable.add(getSmallHeader(localize("custodian.studies_start", "Studies start") + ":"), 1, aRow);
		applicationTable.add(Text.getNonBrakingSpace(), 1, aRow);
		DateInput date = new DateInput(PARAMETER_CUSTODIAN_STUDY_START);
		if (getBusiness().getStudyStart(iwc.getCurrentUser()) != null) {
			date.setDate(getBusiness().getStudyStart(iwc.getCurrentUser()));
		}
		applicationTable.add(date, 1, aRow++);

		applicationTable.add(getSmallHeader(localize("custodian.studies_end", "Studies end") + ":"), 1, aRow);
		applicationTable.add(Text.getNonBrakingSpace(), 1, aRow);
		date = new DateInput(PARAMETER_CUSTODIAN_STUDY_END);
		if (getBusiness().getStudyEnd(iwc.getCurrentUser()) != null) {
			date.setDate(getBusiness().getStudyEnd(iwc.getCurrentUser()));
		}
		applicationTable.add(date, 1, aRow++);

		applicationTable.setHeight(aRow++, 12);

		applicationTable.add(getSmallHeader(localize("child.has_growth_deviation", "Has growth deviation")), 1, aRow);
		yes = getRadioButton(PARAMETER_GROWTH_DEVIATION, Boolean.TRUE.toString());
		no = getRadioButton(PARAMETER_GROWTH_DEVIATION, Boolean.FALSE.toString());
		RadioButton noAnswer = getRadioButton(PARAMETER_GROWTH_DEVIATION, "");
		Boolean hasGrowthDeviation = getCareBusiness().hasGrowthDeviation(getSession().getChild());
		if (hasGrowthDeviation != null) {
			if (hasGrowthDeviation.booleanValue()) {
				yes.setSelected(true);
			}
			else {
				no.setSelected(true);
			}
		}
		else {
			no.setSelected(true);
		}
		applicationTable.add(yes, 2, aRow);
		applicationTable.add(no, 3, aRow);
		applicationTable.add(noAnswer, 4, aRow++);

		applicationTable.add(getSmallHeader(localize("child.growth_deviation_details", "Growth deviation details")), 1, aRow);
		applicationTable.add(new Break(), 1, aRow);
		applicationTable.add(getTextArea(PARAMETER_GROWTH_DEVIATION_DETAILS, getCareBusiness().getGrowthDeviationDetails(getSession().getChild())), 1, aRow++);

		applicationTable.add(getSmallHeader(localize("child.has_allergies", "Has allergies")), 1, aRow);
		yes = getRadioButton(PARAMETER_ALLERGIES, Boolean.TRUE.toString());
		no = getRadioButton(PARAMETER_ALLERGIES, Boolean.FALSE.toString());
		noAnswer = getRadioButton(PARAMETER_ALLERGIES, "");
		Boolean hasAllergies = getCareBusiness().hasAllergies(getSession().getChild());
		if (hasAllergies != null) {
			if (hasAllergies.booleanValue()) {
				yes.setSelected(true);
			}
			else {
				no.setSelected(true);
			}
		}
		else {
			no.setSelected(true);
		}
		applicationTable.add(yes, 2, aRow);
		applicationTable.add(no, 3, aRow);
		applicationTable.add(noAnswer, 4, aRow++);

		applicationTable.add(getSmallHeader(localize("child.allergies_details", "Allergies details")), 1, aRow);
		applicationTable.add(new Break(), 1, aRow);
		applicationTable.add(getTextArea(PARAMETER_ALLERGIES_DETAILS, getCareBusiness().getAllergiesDetails(getSession().getChild())), 1, aRow++);

		applicationTable.add(getSmallHeader(localize("child.last_care_provider", "Last care provider")), 1, aRow);
		applicationTable.add(new Break(), 1, aRow);
		applicationTable.add(getTextArea(PARAMETER_LAST_CARE_PROVIDER, getCareBusiness().getLastCareProvider(getSession().getChild())), 1, aRow++);

		applicationTable.add(getSmallHeader(localize("child.can_contact_last_care_provider", "Can contact last care provider")), 1, aRow);
		yes = getRadioButton(PARAMETER_CAN_CONTACT_LAST_PROVIDER, Boolean.TRUE.toString());
		no = getRadioButton(PARAMETER_CAN_CONTACT_LAST_PROVIDER, Boolean.FALSE.toString());
		Boolean canContactLastProvider = getCareBusiness().canContactLastCareProvider(getSession().getChild());
		if (canContactLastProvider != null) {
			if (canContactLastProvider.booleanValue()) {
				yes.setSelected(true);
			}
			else {
				no.setSelected(true);
			}
		}
		else {
			no.setSelected(true);
		}
		applicationTable.add(yes, 2, aRow);
		applicationTable.add(no, 3, aRow++);

		applicationTable.add(getSmallHeader(localize("child.other_information", "Other information")), 1, aRow);
		applicationTable.add(new Break(), 1, aRow);
		applicationTable.add(getTextArea(PARAMETER_OTHER_INFORMATION, getCareBusiness().getOtherInformation(getSession().getChild())), 1, aRow++);

		applicationTable.add(getSmallHeader(localize("child.can_diplay_images", "Can display images")), 1, aRow);
		yes = getRadioButton(PARAMETER_CAN_DISPLAY_IMAGES, Boolean.TRUE.toString());
		no = getRadioButton(PARAMETER_CAN_DISPLAY_IMAGES, Boolean.FALSE.toString());
		boolean canDisplayImages = getBusiness().canDisplayChildCareImages(getSession().getChild());
		if (canDisplayImages) {
			yes.setSelected(true);
		}
		else {
			no.setSelected(true);
		}
		applicationTable.add(yes, 2, aRow);
		applicationTable.add(no, 3, aRow++);
		
		for (int a = 1; a <= applicationTable.getRows(); a++) {
			if (a > 1) {
				applicationTable.setLeftCellBorder(2, a, 1, "#D7D7D7", "solid");
				applicationTable.setLeftCellBorder(3, a, 1, "#D7D7D7", "solid");
				applicationTable.setLeftCellBorder(4, a, 1, "#D7D7D7", "solid");
			}
			
			applicationTable.setCellpaddingRight(1, a, 6);
			applicationTable.setAlignment(2, a, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setAlignment(3, a, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setAlignment(4, a, Table.HORIZONTAL_ALIGN_CENTER);
		}

		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_4));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_OVERVIEW));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_child_care_application_phase_5"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		
		add(form);
	}
	
	protected void showOverview(IWContext iwc) throws RemoteException {
		saveChildInfo(iwc);

		Form form = createForm();
		form.addParameter(PARAMETER_ACTION, String.valueOf(ACTION_OVERVIEW));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		
		table.add(getPersonInfoTable(iwc, getSession().getChild()), 1, row++);
		table.setHeight(row++, 6);
		
		table.add(getHeader(localize("application.overview", "Overview")), 1, row++);
		table.setHeight(row++, 18);
		
		Table verifyTable = new Table();
		verifyTable.setCellpadding(getCellpadding());
		verifyTable.setCellspacing(getCellspacing());
		verifyTable.setColumns(2);
		table.add(verifyTable, 1, row++);
		int iRow = 1;
		
		String message = iwc.getParameter(PARAMETER_MESSAGE);

		for (int a = 1; a < iNumberOfApplications; a++) {
			String provider = iwc.getParameter(PARAMETER_PROVIDER + "_" + a);
			School school = getBusiness().getSchoolBusiness().getSchool(provider);
			if (school != null) {
				verifyTable.add(getSmallHeader(localize(PARAM_PROVIDER, "Provider") + " " + a + ":"), 1, iRow);
				verifyTable.add(getSmallText(school.getSchoolName()), 2, iRow++);
				
				String date = iwc.getParameter(PARAMETER_DATE + "_" + a);
				if (date != null) {
					verifyTable.add(getSmallHeader(localize(CARE_FROM, "From") + ":"), 1, iRow);
					verifyTable.add(new IWTimestamp(date).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT), 2, iRow++);
				}
				verifyTable.setHeight(iRow++, 3);
			}
		}
		
		verifyTable.setHeight(iRow++, 6);
		
		if (message != null) {
			verifyTable.add(getSmallHeader(localize("application.message", "Message")), 1, iRow++);
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.add(getSmallText(message), 1, iRow++);
			verifyTable.setHeight(iRow++, 6);
			verifyTable.mergeCells(1, iRow, 2, iRow);
			verifyTable.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			verifyTable.setHeight(iRow++, 6);
		}
		
		iRow = addChildInformation(verifyTable, getSession().getChild(), iwc.getCurrentUser(), iRow);
		
		boolean canDisplaySchoolImages = getBusiness().canDisplayChildCareImages(getSession().getChild());
		verifyTable.mergeCells(1, iRow, verifyTable.getColumns(), iRow);
		verifyTable.add(getBooleanTable(getSmallHeader(localize("child.can_diplay_images_info", "Can display images")), canDisplaySchoolImages), 1, iRow);
		verifyTable.setWidth(1, "50%");
		verifyTable.setWidth(2, "50%");
		
		table.setHeight(row++, 18);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_PHASE_5));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("send", "Send")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		
		table.add(previous, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(next, 1, row);
		table.add(getSmallText(Text.NON_BREAKING_SPACE), 1, row);
		table.add(getHelpButton("help_school_application_overview"), 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setCellpaddingRight(1, row, 12);
		
		next.setSubmitConfirm(localize("confirm_application_submit", "Are you sure you want to send the application?"));
		form.setToDisableOnSubmit(next, true);

		add(form);
	}
	
	protected int addChildInformation(Table table, User child, User custodian, int iRow) throws RemoteException {
		boolean multiLanguageHome = getBusiness().hasMultiLanguageHome(child);
		ICLanguage language = getBusiness().getLanguage(child);
		
		table.mergeCells(1, iRow, table.getColumns(), iRow);
		table.add(getBooleanTable(getSmallHeader(localize("child.has_multi_language_info", "Has multi language")), multiLanguageHome), 1, iRow++);

		if (language != null) {
			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(Text.getNonBrakingSpace(), 1, iRow);
			table.add(getSmallHeader(localize("child.language_info", "Language") + ":"), 1, iRow);
			table.add(Text.getNonBrakingSpace(), 1, iRow);
			table.add(getSmallText(language.getName()), 1, iRow++);
		}
			
		table.setHeight(iRow++, 6);
		table.mergeCells(1, iRow, table.getColumns(), iRow);
		table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		table.setHeight(iRow++, 6);
		
		boolean hasStudies = getBusiness().hasStudies(custodian);
		String studies = getBusiness().getStudies(custodian);
		Date start = getBusiness().getStudyStart(custodian);
		Date end = getBusiness().getStudyEnd(custodian);
		
		table.mergeCells(1, iRow, table.getColumns(), iRow);
		table.add(getBooleanTable(getSmallHeader(localize("custodian.has_studies_info", "Has studies")), hasStudies), 1, iRow++);

		if (hasStudies) {
			if (studies != null) {
				table.setHeight(iRow++, 6);
				table.mergeCells(1, iRow, table.getColumns(), iRow);
				table.add(getTextAreaTable(getSmallHeader(localize("custodian.studies_info", "Studies")), studies), 1, iRow++);
			}
			
			if (start != null) {
				table.setHeight(iRow++, 6);
				table.mergeCells(1, iRow, table.getColumns(), iRow);
				table.add(getSmallHeader(localize("custodian.study_start_info", "Study start") + ":"), 1, iRow);
				table.add(Text.getNonBrakingSpace(), 1, iRow);
				table.add(start.toString(), 1, iRow++);
			}

			if (end != null) {
				table.setHeight(iRow++, 6);
				table.mergeCells(1, iRow, table.getColumns(), iRow);
				table.add(getSmallHeader(localize("custodian.study_end_info", "Study end") + ":"), 1, iRow);
				table.add(Text.getNonBrakingSpace(), 1, iRow);
				table.add(end.toString(), 1, iRow++);
			}
		}
			
		table.setHeight(iRow++, 6);
		table.mergeCells(1, iRow, table.getColumns(), iRow);
		table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		table.setHeight(iRow++, 6);
		
		Boolean hasGrowthDeviation = getCareBusiness().hasGrowthDeviation(child);
		String growthDeviation = getCareBusiness().getGrowthDeviationDetails(child);
		Boolean hasAllergies = getCareBusiness().hasAllergies(child);
		String allergies = getCareBusiness().getAllergiesDetails(child);
		String lastCareProvider = getCareBusiness().getLastCareProvider(child);
		Boolean canContactLastProvider = getCareBusiness().canContactLastCareProvider(child);
		String otherInformation = getCareBusiness().getOtherInformation(child);
		
		if (hasGrowthDeviation != null) {
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(getBooleanTable(getSmallHeader(localize("child.has_growth_deviation_info", "Has growth deviation")), hasGrowthDeviation.booleanValue()), 1, iRow++);

			if (growthDeviation != null) {
				table.setHeight(iRow++, 6);
				table.mergeCells(1, iRow, table.getColumns(), iRow);
				table.add(getTextAreaTable(getSmallHeader(localize("child.growth_deviation_details_info", "Growth deviation details")), growthDeviation), 1, iRow++);
			}
			
			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			table.setHeight(iRow++, 6);
		}
		
		if (hasAllergies != null) {
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(getBooleanTable(getSmallHeader(localize("child.has_allergies_info", "Has allergies")), hasAllergies.booleanValue()), 1, iRow++);
	
			if (allergies != null) {
				table.setHeight(iRow++, 6);
				table.mergeCells(1, iRow, table.getColumns(), iRow);
				table.add(getTextAreaTable(getSmallHeader(localize("child.allergies_details_info", "Allergies details")), allergies), 1, iRow++);
			}
			
			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			table.setHeight(iRow++, 6);
		}
		
		if (lastCareProvider != null) {
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(getTextInputTable(getSmallHeader(localize("child.last_care_provider_info", "Last care provider")), lastCareProvider), 1, iRow++);

			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			table.setHeight(iRow++, 6);
		}
		
		table.mergeCells(1, iRow, table.getColumns(), iRow);
		table.add(getBooleanTable(getSmallHeader(localize("child.can_contact_last_care_provider_info", "Can contact last care provider")), canContactLastProvider != null ? canContactLastProvider.booleanValue() : false), 1, iRow++);
		
		table.setHeight(iRow++, 6);
		table.mergeCells(1, iRow, table.getColumns(), iRow);
		table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
		table.setHeight(iRow++, 6);

		if (otherInformation != null) {
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.add(getTextAreaTable(getSmallHeader(localize("child.other_information_info", "Other information")), otherInformation), 1, iRow++);

			table.setHeight(iRow++, 6);
			table.mergeCells(1, iRow, table.getColumns(), iRow);
			table.setBottomCellBorder(1, iRow++, 1, "#D7D7D7", "solid");
			table.setHeight(iRow++, 6);
		}

		return iRow;
	}
	
	protected Table getBooleanTable(Text text, boolean checked) {
		Table table = new Table(3, 1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, 6);
		table.setVerticalAlignment(1, 1, Table.VERTICAL_ALIGN_TOP);
		
		CheckBox box = getCheckBox("void", "");
		box.setChecked(checked);
		box.setDisabled(true);
		table.add(box, 1, 1);
		table.add(text, 3, 1);
		
		return table;
	}

	protected Table getTextInputTable(Text text, String value) {
		Table table = new Table(1, 2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		table.add(text, 1, 1);

		TextInput input = (TextInput) getStyledInterface(new TextInput("void"));
		input.setContent(value);
		input.setDisabled(true);
		table.add(input, 1, 2);
		
		return table;
	}

	private ProviderDropdownDouble getDropdown(Locale locale, String primaryName, String secondaryName) {
		ProviderDropdownDouble dropdown = new ProviderDropdownDouble(primaryName, secondaryName);
		String emptyString = localize("child_care.select_provider","Select provider...");
		dropdown.addEmptyElement(localize("child_care.select_area","Select area..."), emptyString);
		
		try {
			if (areas == null)
				areas = getBusiness().getSchoolBusiness().findAllSchoolAreas();
			if (providerMap == null)
				providerMap = getBusiness().getProviderAreaMap(areas, currentProvider, locale, emptyString, false);			
            
			if (areas != null && providerMap != null) {
				Iterator iter = areas.iterator();
				while (iter.hasNext()) {
					SchoolArea area = (SchoolArea) iter.next();
           Map areaProviders = (Map) providerMap.get(area);
                    
					dropdown.addMenuElement(area.getPrimaryKey().toString(), area.getSchoolAreaName(), areaProviders);
				}
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		
		return dropdown;
	}

	protected Table getTextAreaTable(Text text, String value) {
		Table table = new Table(1, 2);
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		table.add(text, 1, 1);

		TextArea input = (TextArea) getStyledInterface(new TextArea("void"));
		input.setContent(value);
		input.setDisabled(true);
		input.setWidth("100%");
		input.setRows(4);
		table.add(input, 1, 2);
		
		return table;
	}

	private void save(IWContext iwc) throws RemoteException {
		boolean saved = false;
		User child = getSession().getChild();
		
		int numberOfApplications = hasActivePlacement ? (iNumberOfApplications - 1) : iNumberOfApplications;
		if (getBusiness().getAcceptedApplicationsByChild(((Integer) child.getPrimaryKey()).intValue()) != null) {
			numberOfApplications--;
		}

		int[] providers = new int[numberOfApplications];
		String[] dates = new String[numberOfApplications];
		Date[] queueDates = new Date[numberOfApplications];
		
		for (int i = 0; i < numberOfApplications; i++) {
			providers[i] = iwc.isParameterSet(PARAMETER_PROVIDER + "_" + (i + 1)) ? Integer.parseInt(iwc.getParameter(PARAMETER_PROVIDER + "_" + (i + 1))) : -1;
			dates[i] = iwc.isParameterSet(PARAMETER_DATE + "_" + (i + 1)) ? iwc.getParameter(PARAMETER_DATE + "_" + (i + 1)) : null;
		}
			
		Collection applications = getBusiness().getApplicationsForChild(child);
		loop:
		for (int i = 0; i < providers.length; i++){
			Iterator apps = applications.iterator();
			while(apps.hasNext()){
				ChildCareApplication app = (ChildCareApplication) apps.next();
				if (app.getProviderId() == providers[i]){
					queueDates[i] = app.getQueueDate();
					continue loop;
				}
			}
		}
					
		String message = iwc.getParameter(PARAMETER_MESSAGE);
		IWTimestamp fromTime = iwc.isParameterSet(PARAMETER_FROM_TIME) ? new IWTimestamp("2005-01-01 " + iwc.getParameter(PARAMETER_FROM_TIME)) : null;
		IWTimestamp toTime = iwc.isParameterSet(PARAMETER_TO_TIME) ? new IWTimestamp("2005-01-01 " + iwc.getParameter(PARAMETER_TO_TIME)) : null;

		String subject = localize(EMAIL_PROVIDER_SUBJECT, "Child care application received");
		String body = localize(EMAIL_PROVIDER_MESSAGE, "You have received a new childcare application");
		User parent = iwc.getCurrentUser();
		boolean sendMessages = true;

		saved = getBusiness().insertApplications(parent, providers, dates, message, fromTime != null ? fromTime.getTime() : null, toTime != null ? toTime.getTime() : null, -1, ((Integer) child.getPrimaryKey()).intValue(), subject, body, false, sendMessages, queueDates, null);
		
		if (saved) {
			if (getResponsePage() != null)
				iwc.forwardToIBPage(getParentPage(), getResponsePage());
			else
				add(new Text(TextSoap.formatText(localize(APPLICATION_INSERTED, "Application submitted"))));
		}
		else {
			add(getErrorText(localize("application.save_failed", "Save failed, contact the community office")));
		}
	}

	protected void saveChildInfo(IWContext iwc) throws RemoteException {
		Boolean growthDeviation = iwc.isParameterSet(PARAMETER_GROWTH_DEVIATION) ? new Boolean(iwc.getParameter(PARAMETER_GROWTH_DEVIATION)) : null;
		Boolean allergies = iwc.isParameterSet(PARAMETER_ALLERGIES) ? new Boolean(iwc.getParameter(PARAMETER_ALLERGIES)) : null;
		
		Boolean canContactLastProvider = new Boolean(iwc.getParameter(PARAMETER_CAN_CONTACT_LAST_PROVIDER));
		Boolean canDisplayImages = new Boolean(iwc.getParameter(PARAMETER_CAN_DISPLAY_IMAGES));
		Boolean hasMultiLanguageHome = new Boolean(iwc.getParameter(PARAMETER_HAS_MULTI_LANGUAGE_HOME));
		String otherChildCareInformation = iwc.getParameter(PARAMETER_CHILD_CARE_INFORMATION);
		String language = iwc.getParameter(PARAMETER_LANGUAGE);
		
		String growthDeviationDetails = iwc.getParameter(PARAMETER_GROWTH_DEVIATION_DETAILS);
		String allergiesDetails = iwc.getParameter(PARAMETER_ALLERGIES_DETAILS);
		String lastCareProvider = iwc.getParameter(PARAMETER_LAST_CARE_PROVIDER);
		String otherInformation = iwc.getParameter(PARAMETER_OTHER_INFORMATION);
		
		Boolean custodianHasStudies = new Boolean(iwc.getParameter(PARAMETER_CUSTODIAN_HAS_STUDIES));
		String custodianStudies = iwc.getParameter(PARAMETER_CUSTODIAN_STUDIES);
		Date custodianStudyStart = iwc.isParameterSet(PARAMETER_CUSTODIAN_STUDY_START) ? new IWTimestamp(iwc.getParameter(PARAMETER_CUSTODIAN_STUDY_START)).getDate() : null;
		Date custodianStudyEnd = iwc.isParameterSet(PARAMETER_CUSTODIAN_STUDY_END) ? new IWTimestamp(iwc.getParameter(PARAMETER_CUSTODIAN_STUDY_END)).getDate() : null;

		getCareBusiness().storeChildInformation(getSession().getChild(), growthDeviation, growthDeviationDetails, allergies, allergiesDetails, lastCareProvider, canContactLastProvider.booleanValue(), otherInformation);
		getBusiness().storeChildCareInformation(getSession().getChild(), canDisplayImages.booleanValue(), otherChildCareInformation, hasMultiLanguageHome.booleanValue(), language);
		getBusiness().storeCustodianInformation(iwc.getCurrentUser(), custodianHasStudies.booleanValue(), custodianStudies, custodianStudyStart, custodianStudyEnd);
	}
	
	protected void saveCustodianInfo(IWContext iwc, boolean storeRelatives) throws RemoteException {
		String[] userPKs = storeRelatives ? iwc.getParameterValues(PARAMETER_RELATIVE) : iwc.getParameterValues(PARAMETER_USER);
		String[] homePhones = iwc.getParameterValues(PARAMETER_HOME_PHONE);
		String[] workPhones = iwc.getParameterValues(PARAMETER_WORK_PHONE);
		String[] mobilePhones = iwc.getParameterValues(PARAMETER_MOBILE_PHONE);
		String[] emails = iwc.getParameterValues(PARAMETER_EMAIL);
		String[] relations = iwc.getParameterValues(PARAMETER_RELATION);
		
		if (userPKs != null) {
			for (int a = 0; a < userPKs.length; a++) {
				String userPK = userPKs[a];
				String relation = iwc.getParameter(PARAMETER_RELATION + "_" + userPK);
				
				if (storeRelatives) {
					if (userPK.length() > 0) {
						getCareBusiness().storeRelative(getSession().getChild(), userPK, relations[a], a + 1, homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
					}
				}
				else {
					User custodian = getUserBusiness().getUser(new Integer(userPK));
					if (getUserBusiness().getMemberFamilyLogic().isCustodianOf(custodian, getSession().getChild())) {
						getCareBusiness().updateUserInfo(custodian, homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
						if (relation != null && relation.length() > 0) {
							getCareBusiness().storeUserRelation(getSession().getChild(), custodian, relation);
						}
					}
					else {
						getCareBusiness().storeExtraCustodian(getSession().getChild(), custodian, relation, homePhones[a], workPhones[a], mobilePhones[a], emails[a]);
					}
				}
			}
		}
	}

	private int parseAction(IWContext iwc) {
		int action = ACTION_PHASE_1;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		return action;
	}
	
	protected Form createForm() {
		Form form = new Form();
		for (int a = 1; a <= iNumberOfApplications; a++) {
			form.maintainParameter(PARAMETER_PROVIDER + "_" + a);
			form.maintainParameter(PARAMETER_AREA + "_" + a);
			form.maintainParameter(PARAMETER_DATE + "_" + a);
		}
		form.maintainParameter(PARAMETER_MESSAGE);
		form.maintainParameter(PARAMETER_FROM_TIME);
		form.maintainParameter(PARAMETER_TO_TIME);
		
		return form;
	}
	
	private DropdownMenu getRelationDropdown(User relative) {
		DropdownMenu relations = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_RELATION + (relative != null ? "_" + relative.getPrimaryKey().toString() : "")));
		relations.addMenuElement("", localize("select_relation", "Select relation"));
		relations.addMenuElement(CareConstants.RELATION_MOTHER, localize("relation.mother", "Mother"));
		relations.addMenuElement(CareConstants.RELATION_FATHER, localize("relation.father", "Father"));
		relations.addMenuElement(CareConstants.RELATION_STEPMOTHER, localize("relation.stepmother", "Stepmother"));
		relations.addMenuElement(CareConstants.RELATION_STEPFATHER, localize("relation.stepfather", "Stepfather"));
		relations.addMenuElement(CareConstants.RELATION_GRANDMOTHER, localize("relation.grandmother", "Grandmother"));
		relations.addMenuElement(CareConstants.RELATION_GRANDFATHER, localize("relation.grandfather", "Grandfather"));
		relations.addMenuElement(CareConstants.RELATION_SIBLING, localize("relation.sibling", "Sibling"));
		relations.addMenuElement(CareConstants.RELATION_OTHER, localize("relation.other", "Other"));
		
		return relations;
	}
	
	private DropdownMenu getNativeLanguagesDropdown(ICLanguage selected) {
		DropdownMenu drop = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_LANGUAGE));
		drop.addMenuElement("-1", localize("application.choose_language", "Choose languge"));
		try {
			ICLanguageHome languageHome = (ICLanguageHome) IDOLookup.getHome(ICLanguage.class);
			Collection langs = languageHome.findAll();
			if (langs != null) {
				for (Iterator iter = langs.iterator(); iter.hasNext();) {
					ICLanguage aLang = (ICLanguage) iter.next();
					drop.addMenuElement(aLang.getPrimaryKey().toString(), aLang.getName());
				}
			}
		}
		catch (RemoteException re) {
			re.printStackTrace();
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		if (selected != null) {
			drop.setSelectedElement(selected.getPrimaryKey().toString());
		}
		drop.keepStatusOnAction(true);
		
		return drop;
	}
	
	protected ICPage getHomePage() {
		return iHomePage;
	}
	
	public void setHomePage(ICPage homePage) {
		iHomePage = homePage;
	}
	
	public void setUseSessionUser(boolean useSessionUser) {
		iUseSessionUser = useSessionUser;
	}

	
	public void setNumberOfApplications(int numberOfApplications) {
		iNumberOfApplications = numberOfApplications;
	}
}