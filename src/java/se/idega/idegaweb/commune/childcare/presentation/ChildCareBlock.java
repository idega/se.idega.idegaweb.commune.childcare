package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.data.CareTime;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import se.idega.idegaweb.commune.presentation.CommuneBlock;
import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareBlock extends CommuneBlock {

	private CareBusiness careBusiness;
	private ChildCareBusiness business;
	protected ChildCareSession session;
	private CommuneUserBusiness userBusiness;
	private int _childCareID = -1;
	private boolean checkRequired;
	private boolean usePredefinedCareTimeValues;
	private boolean allowChangeGroupFromToday;
	
	//public static final String ACCEPTED_COLOR = "#FFEAEA";
	//public static final String PARENTS_ACCEPTED_COLOR = "#EAFFEE";
	//public static final String CONTRACT_COLOR = "#EAF1FF";
	public static String ACCEPTED_COLOR = "#FFE0E0";
	public static String PARENTS_ACCEPTED_COLOR = "#E0FFE0";
	public static String CONTRACT_COLOR = "#E0E0FD";
	//public static final String PENDING_COLOR = "#FDFFDD";
	public static String PENDING_COLOR = "#FFEBDD";
	
	public static final String STATUS_ALL = "status_all";
	
	private static final String PROPERTY_CHECK_REQUIRED = "check_required";
	private static final String PROPERTY_USE_PREDEFINED_CARE_TIME_VALUES = "use_predefined_care_time_values";
	private static final String PROPERTY_ALLOW_CHANGE_GROUP_FROM_TODAY = "allow_change_group_from_today";
	
	private static final String PROPERTY_ACCEPTED_COLOR = "child_care_accepted_color";
	private static final String PROPERTY_PARENTS_ACCEPTED_COLOR = "child_care_parents_accepted_color";
	private static final String PROPERTY_CONTRACT_COLOR = "child_care_contract_color";
	private static final String PROPERTY_PENDING_COLOR = "child_care_pending_color";
    
	public void main(IWContext iwc) throws Exception{
		setResourceBundle(getResourceBundle(iwc));
		this.business = getChildCareBusiness(iwc);
		this.session = getChildCareSession(iwc);
		this.careBusiness = getCareBusiness(iwc);
		this.userBusiness = getUserBusiness(iwc);
		this.checkRequired = new Boolean(getPropertyValue(getBundle(iwc), PROPERTY_CHECK_REQUIRED, Boolean.TRUE.toString())).booleanValue();
		this.usePredefinedCareTimeValues = new Boolean(getPropertyValue(getBundle(iwc), PROPERTY_USE_PREDEFINED_CARE_TIME_VALUES, Boolean.FALSE.toString())).booleanValue();
		this.allowChangeGroupFromToday = new Boolean(getPropertyValue(getBundle(iwc), PROPERTY_ALLOW_CHANGE_GROUP_FROM_TODAY, Boolean.TRUE.toString())).booleanValue();
		initialize();
		
		ACCEPTED_COLOR = getBundle(iwc).getProperty(PROPERTY_ACCEPTED_COLOR, "#FFE0E0");
		PARENTS_ACCEPTED_COLOR = getBundle(iwc).getProperty(PROPERTY_PARENTS_ACCEPTED_COLOR, "#E0FFE0");
		CONTRACT_COLOR = getBundle(iwc).getProperty(PROPERTY_CONTRACT_COLOR, "#E0E0FD");
		PENDING_COLOR = getBundle(iwc).getProperty(PROPERTY_PENDING_COLOR, "#FFEBDD");

		init(iwc);
	}
	
	private void initialize() throws RemoteException {
		this._childCareID = this.session.getChildCareID();	
	}
	
	public void init(IWContext iwc) throws Exception {
		//Override this method...
		iwc.isLoggedOn();
	}
	
	private ChildCareBusiness getChildCareBusiness(IWContext iwc) throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);	
	}
	
	private ChildCareSession getChildCareSession(IWContext iwc) throws RemoteException {
		return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);	
	}
	
	private CareBusiness getCareBusiness(IWContext iwc) throws RemoteException {
		return (CareBusiness) IBOLookup.getServiceInstance(iwc, CareBusiness.class);
	}
	
	private CommuneUserBusiness getUserBusiness(IWContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc, CommuneUserBusiness.class);
	}
	
	/**
	 * @return CareBusiness
	 */
	public CareBusiness getCareBusiness() {
		return this.careBusiness;
	}
	
	
	/**
	 * @return ChildCareBusiness
	 */
	public ChildCareBusiness getBusiness() {
		return this.business;
	}

	/**
	 * @return ChildCareSession
	 */
	public ChildCareSession getSession() {
		return this.session;
	}
	
	public CommuneUserBusiness getUserBusiness() {
		return this.userBusiness;
	}

	/**
	 * @return int
	 */
	public int getChildcareID() {
		return this._childCareID;
	}

	protected Table getLegendTable() {
		return getLegendTable(false);
	}
	
	protected Table getLegendTable(boolean showPending) {
		Table table = new Table();
		table.setHeight(1, 12);
		table.setWidth(1, "12");
		table.setWidth(3, "12");
		table.setWidth(4, "12");
		table.setWidth(6, "12");
		table.setWidth(7, "12");
		if (showPending) {
			table.setWidth(9, "12");
			table.setWidth(10, "12");
		}
		
		table.add(getColorTable(ACCEPTED_COLOR), 1, 1);
		table.add(getColorTable(PARENTS_ACCEPTED_COLOR), 4, 1);
		table.add(getColorTable(CONTRACT_COLOR), 7, 1);
		if (showPending) {
			table.add(getColorTable(PENDING_COLOR), 10, 1);
		}
		
		table.add(getSmallHeader(localize("child_care.application_status_accepted","Accepted")), 2, 1);
		table.add(getSmallHeader(localize("child_care.application_status_parents_accepted","Parents accepted")), 5, 1);
		table.add(getSmallHeader(localize("child_care.application_status_contract","Contract")), 8, 1);
		if (showPending) {
			table.add(getSmallHeader(localize("child_care.application_status_pending","Pending")), 11, 1);
		}
		
		return table;
	}
	
	protected Table getContractColorTable() {
		Table table = new Table();
		table.setHeight(1, 12);
		table.setWidth(1, "12");
		table.setWidth(3, "12");
		table.setWidth(4, "12");
		table.setWidth(6, "12");
		table.setWidth(7, "12");
		
		table.add(getColorTable(CONTRACT_COLOR), 1, 1);
		table.add(getColorTable(ACCEPTED_COLOR), 7, 1);
	
		table.add(getSmallHeader(localize("child_care.application_status_par_cancelled","Parent cancelled")), 2, 1);
		table.add(getSmallHeader(localize("child_care.application_status_waiting","Waiting")), 8, 1);
		
		
		return table;
	}
	
	private Table getColorTable(String color) {
		Table colorTable = new Table(1, 1);
		colorTable.setHeight(1, 1, "12");
		colorTable.setWidth(1, 1, "12");
		colorTable.setColor("#000000");
		colorTable.setColor(1, 1, color);
		colorTable.setCellpadding(0);
		colorTable.setCellspacing(1);
		
		return colorTable;		
	}


	protected DropdownMenu getSchoolTypes(int typeID, int typeToIgnoreID) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(getSession().getParameterSchoolTypeID());
		int ccId =getSession().getChildCareID();
		
		School childcare = null;
		Collection types = null;
		String catChildcare = getBusiness().getSchoolBusiness().getCategoryChildcare().getCategory();
		
		try {
		childcare = getBusiness().getSchoolBusiness().getSchoolHome().findByPrimaryKey(new Integer (ccId));
		}
		catch (FinderException e){
			log(e);
		}
		
		//Collection types = getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare();
		try {
			types = childcare.getSchoolTypes();
		}
		catch (IDORelationshipException relEx)	{
			log (relEx);
		}
				
		Iterator iter = types .iterator();
		while (iter.hasNext()) {
			SchoolType element = (SchoolType) iter.next();
			String catCC = element.getCategory().getCategory().toString().toUpperCase();
			//only add to list if category is childcare
			if (catCC.equals(catChildcare)){
				if (((Integer)element.getPrimaryKey()).intValue() != typeToIgnoreID) {
					menu.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolTypeName());
				}	
			}
			
		}
		if (typeID != -1) {
			menu.setSelectedElement(typeID);
		}
		
		return (DropdownMenu) getStyledInterface(menu);	
	}

	protected DropdownMenu getEmploymentTypes(String parameterName, int selectedType) throws RemoteException {
		SelectorUtility util = new SelectorUtility();
		DropdownMenu menu = (DropdownMenu) getStyledInterface(util.getSelectorFromIDOEntities(new DropdownMenu(parameterName), getBusiness().findAllEmploymentTypes(), "getLocalizationKey", getResourceBundle()));
		menu.addMenuElementFirst("-1", "");
		if (selectedType != -1) {
			menu.setSelectedElement(selectedType);
		}
		return menu;
	}

	protected DropdownMenu getGroups(int groupID, int groupToIgnoreID) throws RemoteException {
		DropdownMenu menu = new DropdownMenu(getSession().getParameterGroupID());
		
		Collection groups = getBusiness().getSchoolBusiness().findChildcareClassesBySchool(getSession().getChildCareID());
		Iterator iter = groups.iterator();
		while (iter.hasNext()) {
			SchoolClass element = (SchoolClass) iter.next();
			if (((Integer)element.getPrimaryKey()).intValue() != groupToIgnoreID) {
				menu.addMenuElement(element.getPrimaryKey().toString(), element.getSchoolClassName());
			}
		}
		if (groupID != -1) {
			menu.setSelectedElement(groupID);
		}
		
		return (DropdownMenu) getStyledInterface(menu);	
	}
	
	protected String getStatusString(ChildCareApplication application) throws RemoteException {
		return getStatusStringAbbr(application.getApplicationStatus());
	}
	
	protected String getStatusString(char status) throws RemoteException {
		return getBusiness().getStatusString(status);
	}
	
	protected String getStatusStringAbbr(char status) throws RemoteException {
		return getBusiness().getStatusStringAbbr(status);
	}
	
	protected DropdownMenu getRejectedStatuses() throws RemoteException {
		DropdownMenu menu = (DropdownMenu) getStyledInterface(new DropdownMenu(getSession().getParameterStatus()));
		menu.addMenuElement(STATUS_ALL, localize("child_care.all_rejected_applications", "Show all rejected"));
		menu.addMenuElement(String.valueOf(getBusiness().getStatusCancelled()), getStatusString(getBusiness().getStatusCancelled()) + " (" + getStatusStringAbbr(getBusiness().getStatusCancelled()) + ")");
		menu.addMenuElement(String.valueOf(getBusiness().getStatusDenied()), getStatusString(getBusiness().getStatusDenied()) + " (" + getStatusStringAbbr(getBusiness().getStatusDenied()) + ")");
		menu.addMenuElement(String.valueOf(getBusiness().getStatusNotAnswered()), getStatusString(getBusiness().getStatusNotAnswered()) + " (" + getStatusStringAbbr(getBusiness().getStatusNotAnswered()) + ")");
		menu.addMenuElement(String.valueOf(getBusiness().getStatusRejected()), getStatusString(getBusiness().getStatusRejected()) + " (" + getStatusStringAbbr(getBusiness().getStatusRejected()) + ")");
		menu.addMenuElement(String.valueOf(getBusiness().getStatusTimedOut()), getStatusString(getBusiness().getStatusTimedOut()) + " (" + getStatusStringAbbr(getBusiness().getStatusTimedOut()) + ")");
		menu.addMenuElement(String.valueOf(getBusiness().getStatusDeleted()), getStatusString(getBusiness().getStatusDeleted()) + " (" + getStatusStringAbbr(getBusiness().getStatusDeleted()) + ")");
		if (getSession().getStatus() != null) {
			menu.setSelectedElement(getSession().getStatus());
		}

		return menu;
	}

	protected DropdownMenu getSeasons() throws RemoteException {
		SelectorUtility util = new SelectorUtility();
		Collection seasons = this.business.getSchoolBusiness().findAllSchoolSeasons(getBusiness().getSchoolBusiness().getCategoryElementarySchool());

		DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(getSession().getParameterSeasonID()), seasons, "getSchoolSeasonName");
		menu.setToSubmit();
		
		if ( getSession().getSeasonID() != -1 ) {
			menu.setSelectedElement(getSession().getSeasonID());
		}
		else {
			try {
				SchoolSeason currentSeason = getBusiness().getSchoolBusiness().getCurrentSchoolSeason(getBusiness().getSchoolBusiness().getCategoryElementarySchool());
				menu.setSelectedElement(currentSeason.getPrimaryKey().toString());
			}
			catch (FinderException e) {
				try {
					SchoolSeason currentSeason = this.careBusiness.getCurrentSeason();
					menu.setSelectedElement(currentSeason.getPrimaryKey().toString());
				}
				catch (FinderException e1) {
					e1.printStackTrace();
				}
			}
		}
		
		return (DropdownMenu) getStyledInterface(menu);	
	}
	
	/**
	 * Returns a <code>DropdownMenu</code> that uses the given <code>Collection</code> of entities as options where the
	 * value is a localization key.
	 * @param name The form name for the returned <code>DropdownMenu</code>
	 * @param entities The entity beans to use as values.
	 * @param methodName The name of the method from which the values are retrieved.
	 * @param defaultValue The default value to set if method returns null
	 * @return
	 */
	protected DropdownMenu getDropdownMenuLocalized(String name, Collection entities, String methodName, String defaultValue) {
		SelectorUtility util = new SelectorUtility();
		DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(name), entities, methodName, getResourceBundle(), defaultValue);
		
		return (DropdownMenu) getStyledInterface(menu);
	}
	
	protected DropdownMenu getCareTimeMenu(String name)  {
		try {
			SelectorUtility util = new SelectorUtility();
			DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(name), getBusiness().getCareTimes(), "getLocalizedKey", getResourceBundle());
			
			return (DropdownMenu) getStyledInterface(menu);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	protected DropdownMenu getCareTimeMenu(String name, User child)  {
		try {
			SelectorUtility util = new SelectorUtility();
			DropdownMenu menu = (DropdownMenu) util.getSelectorFromIDOEntities(new DropdownMenu(name), getBusiness().getCareTimes(child), "getLocalizedKey", getResourceBundle());
			
			return (DropdownMenu) getStyledInterface(menu);
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}
	
	protected String getCareTime(String careTime) {
		if (careTime == null) {
			return "-";
		}
		
		try {
			Integer.parseInt(careTime);
		}
		catch (NumberFormatException nfe) {
			try {
				CareTime time = getBusiness().getCareTime(careTime);
				return getResourceBundle().getLocalizedString(time.getLocalizedKey(), careTime);
			}
			catch (FinderException fe) {
				log(fe);
			}
			catch (RemoteException re) {
				log(re);
			}
		}
		return careTime;
	}
		
	/**
	 * @return Returns the checkRequired.
	 */
	public boolean isCheckRequired() {
		return this.checkRequired;
	}
	
	/**
	 * @return Returns the usePredefinedCareTimeValues.
	 */
	public boolean isUsePredefinedCareTimeValues() {
		return this.usePredefinedCareTimeValues;
	}
	
	/**
	 * @return Returns the allowChangeGroupFromToday.
	 */
	public boolean isAllowChangeGroupFromToday() {
		return this.allowChangeGroupFromToday;
	}
    
	/**
	 * Gets the value for a property name ... replaces the bundle properties that were used previously
	 * @param propertyName
	 * @return
	 */
	private String getPropertyValue(IWBundle iwb, String propertyName, String defaultValue) {
		IWMainApplicationSettings settings = getSettings();
		String value = settings.getProperty(propertyName);
		if (value != null) {
			return value;
		}
		else {
			value = iwb.getProperty(propertyName);
			settings.setProperty(propertyName, value != null ? value : defaultValue);
		}

		return defaultValue;
	}
	
	private IWMainApplicationSettings getSettings() {
		return getIWApplicationContext().getApplicationSettings();
	}

	protected Table getPersonInfoTable(IWContext iwc, User user) throws RemoteException {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(0);
		table.setColumns(5);
		table.setWidth(3, 12);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		
		Address address = getUserBusiness().getUsersMainAddress(user);
		PostalCode postal = null;
		if (address != null) {
			postal = address.getPostalCode();
		}
		Phone phone = null;
		try {
			phone = getUserBusiness().getUsersHomePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			phone = null;
		}
		Phone mobile = null;
		try {
			mobile = getUserBusiness().getUsersMobilePhone(user);
		}
		catch (NoPhoneFoundException npfe) {
			mobile = null;
		}
		Email email = null;
		try {
			email = getUserBusiness().getUsersMainEmail(user);
		}
		catch (NoEmailFoundException nefe) {
			email = null;
		}
		
		table.add(getSmallHeader(localize("name", "Name")), 1, row);
		table.add(getSmallText(user.getName()), 2, row);
		
		table.add(getSmallHeader(localize("personal_id", "Personal ID")), 4, row);
		table.add(getSmallText(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale())), 5, row++);
		
		table.add(getSmallHeader(localize("address", "Address")), 1, row);
		table.add(getSmallHeader(localize("zip_code", "Postal code")), 4, row);
		if (address != null) {
			table.add(getSmallText(address.getStreetAddress()), 2, row);
		}
		if (postal != null) {
			table.add(getSmallText(postal.getPostalAddress()), 5, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("home_phone", "Home phone")), 1, row);
		table.add(getSmallHeader(localize("mobile_phone", "Mobile phone")), 4, row);
		if (phone != null && phone.getNumber() != null) {
			table.add(getSmallText(phone.getNumber()), 2, row);
		}
		if (mobile != null && mobile.getNumber() != null) {
			table.add(getSmallText(mobile.getNumber()), 5, row);
		}
		row++;
		
		table.add(getSmallHeader(localize("email", "E-mail")), 1, row);
		if (email != null && email.getEmailAddress() != null) {
			table.add(getSmallText(email.getEmailAddress()), 2, row);
		}
		row++;
		
		table.setHeight(row, 6);
		table.mergeCells(1, row, table.getColumns(), row);
		table.setBottomCellBorder(1, row++, 1, "#D7D7D7", "solid");
		table.setHeight(row++, 6);
		
		return table;
	}
}