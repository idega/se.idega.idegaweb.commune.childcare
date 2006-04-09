package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCareSessionBean extends IBOSessionBean implements ChildCareSession {

	protected static final String PARAMETER_CHILD_CARE_ID = "cc_c_c_id";
	protected static final String PARAMETER_SCHOOL_TYPE_ID = "cc_school_type_id";	
	protected static final String PARAMETER_GROUP_ID = "cc_group_id";
	protected static final String PARAMETER_USER_ID = "cc_user_id";
	protected static final String PARAMETER_UNIQUE_ID="cc_unique_id";
	protected static final String PARAMETER_CHECK_ID = "cc_check_id";
	protected static final String PARAMETER_APPLICATION_ID = "cc_application_id";
	protected static final String PARAMETER_FROM = "cc_from";
	protected static final String PARAMETER_TO = "cc_to";
	protected static final String PARAMETER_SORT_BY = "cc_sort_by";
	protected static final String PARAMETER_SEASON = "cc_season";
	protected static final String PARAMETER_STATUS = "cc_status";
	protected static final String PARAMETER_CASE_CODE = "cc_case_code";

	protected int _childcareID = -1;
	protected School _provider;
	protected int _userID = -1;
	protected int _childID = -1;
	protected String _uniqueID = null;
	protected int _applicationID = -1;
	protected int _schoolTypeID = -1;
	protected int _groupID = -1;
	protected int _checkID = -1;
	protected int _seasonID = -1;
	protected int _sortBy = -1;
	protected IWTimestamp fromTimestamp;
	protected IWTimestamp toTimestamp;
	protected Boolean hasPrognosis;
	protected boolean _outDatedPrognosis = false;
	protected String _status;
	protected String _caseCode;
	
	private User _child;

	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}
	
	public ChildCareBusiness getChildCareBusiness() throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ChildCareBusiness.class);
	}
	
	public School getProvider() {
		return this._provider;
	}
	
	public boolean hasPrognosis() throws RemoteException {
		if (this.hasPrognosis == null) {
			setHasPrognosis();
		}
		return this.hasPrognosis.booleanValue();
	}
	
	private void setHasPrognosis() throws RemoteException {
		if (!getChildCareBusiness().usePrognosis()) {
			this.hasPrognosis = new Boolean(true);
			return;
		}
		
		ChildCarePrognosis prognosis = getChildCareBusiness().getPrognosis(getChildCareID());
		if (prognosis != null) {
			IWTimestamp stamp = new IWTimestamp();
			IWTimestamp lastUpdated = new IWTimestamp(prognosis.getUpdatedDate());
			if (IWTimestamp.getDaysBetween(lastUpdated, stamp) > 90) {
				this.hasPrognosis = new Boolean(false);
				this._outDatedPrognosis = true;
			}
			else {
				this.hasPrognosis = new Boolean(true);
			}
		}
		else {
			this.hasPrognosis = new Boolean(false);
		}
	}
	
	public void setHasPrognosis(boolean hasPrognosis) {
		this.hasPrognosis = new Boolean(hasPrognosis);
	}
	
	public boolean hasOutdatedPrognosis() {
		return this._outDatedPrognosis;
	}
	
	public void setHasOutdatedPrognosis(boolean hasOutdatedPrognosis) {
		this._outDatedPrognosis = hasOutdatedPrognosis;
	}
	
	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getChildCareID() throws RemoteException {
		if (getUserContext().isLoggedOn()) {
			User user = getUserContext().getCurrentUser();
			int userID = ((Integer)user.getPrimaryKey()).intValue();
			
			if (this._userID == userID) {
				if (this._childcareID != -1) {
					return this._childcareID;
				}
				else {
					this.hasPrognosis = null;
					return getChildCareIDFromUser(user);
				}
			}
			else {
				this.hasPrognosis = null;
				this._userID = userID;
				return getChildCareIDFromUser(user);
			}
		}
		else {
			return this._childcareID;	
		}
	}
	
	private int getChildCareIDFromUser(User user) throws RemoteException {
		if (user != null) {
			try {
				School school = getCareBusiness().getProviderForUser(user);
				if (school != null) {
					this._provider = school;
					this._childcareID = ((Integer) school.getPrimaryKey()).intValue();
				}
			}
			catch (FinderException fe) {
			}
		}
		return this._childcareID;
	}

	/**
	 * @return int
	 */
	public int getUserID() {
		return this._userID;
	}

	/**
	 * Sets the childcareID.
	 * @param childcareID The childcareID to set
	 */
	public void setChildCareID(int childcareID) {
		this._childcareID = childcareID;
	}

	/**
	 * Sets the provider.
	 * @param provider The provider to set
	 */
	public void setProvider(School provider) {
		this._provider = provider;
	}

	/**
	 * Sets the userID.
	 * @param userID The userID to set
	 */
	public void setUserID(int userID) {
		this._userID = userID;
	}

	/**
	 * @return String
	 */
	public String getParameterChildCareID() {
		return PARAMETER_CHILD_CARE_ID;
	}

	/**
	 * @return String
	 */
	public String getParameterUserID() {
		return PARAMETER_USER_ID;
	}
	
	/**
	 * @return String
	 */
	public String getParameterUniqueID() {
		return PARAMETER_UNIQUE_ID;
	}

	/**
	 * @return String
	 */
	public String getParameterApplicationID() {
		return PARAMETER_APPLICATION_ID;
	}

	/**
	 * @return String
	 */
	public String getParameterCaseCode() {
		return PARAMETER_CASE_CODE;
	}

	/**
	 * @return String
	 */
	public String getParameterSchoolTypeID() {
		return PARAMETER_SCHOOL_TYPE_ID;
	}


	/**
	 * @return String
	 */
	public String getParameterGroupID() {
		return PARAMETER_GROUP_ID;
	}

	/**
	 * @return String
	 */
	public String getParameterCheckID() {
		return PARAMETER_CHECK_ID;
	}

	/**
	 * @return int
	 */
	public int getApplicationID() {
		return this._applicationID;
	}

	/**
	 * @return int
	 */
	public int getChildID() {
		return this._childID;
	}

	public User getChild() {
		try {
			if (this._child == null && this._childID != -1) {
				this._child = getUserBusiness().getUser(new Integer(this._childID));
			}
			else if (this._child == null && this._uniqueID != null) {
				try {
					this._child = getUserBusiness().getUserByUniqueId(this._uniqueID);
				}
				catch (FinderException fe) {
					fe.printStackTrace();
					this._child = null;
				}
			}
		}
		catch (RemoteException re) {
			this._child = null;
		}
		return this._child;
	}
	
	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) this.getServiceInstance(CommuneUserBusiness.class);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	/**
	 * @return int
	 */
	public String getUniqueID() {
		return this._uniqueID;
	}

	/**
	 * @return int
	 */
	public int getCheckID() {
		return this._checkID;
	}

	/**
	 * Sets the applicationID.
	 * @param applicationID The applicationID to set
	 */
	public void setApplicationID(int applicationID) {
		this._applicationID = applicationID;
	}

	/**
	 * Sets the childID.
	 * @param childID The childID to set
	 */
	public void setChildID(int childID) {
		this._childID = childID;
		this._child = null;
	}
	
	/**
	 * Sets the uniqueID.
	 * @param childID The childID to set
	 */
	public void setUniqueID(String uniqueID) {
		this._uniqueID = uniqueID;
		this._child = null;
	}

	/**
	 * Sets the checkID.
	 * @param checkID The checkID to set
	 */
	public void setCheckID(int checkID) {
		this._checkID = checkID;
	}

	/**
	 * @return String
	 */
	public String getParameterFrom() {
		return PARAMETER_FROM;
	}

	/**
	 * @return String
	 */
	public String getParameterSeasonID() {
		return PARAMETER_SEASON;
	}

	/**
	 * @return String
	 */
	public String getParameterStatus() {
		return PARAMETER_STATUS;
	}

	/**
	 * @return String
	 */
	public String getParameterSortBy() {
		return PARAMETER_SORT_BY;
	}

	/**
	 * @return String
	 */
	public String getParameterTo() {
		return PARAMETER_TO;
	}

	/**
	 * @return String
	 */
	public String getStatus() {
		return this._status;
	}

	/**
	 * @return int
	 */
	public int getSortBy() {
		return this._sortBy;
	}

	/**
	 * @return IWTimestamp
	 */
	public IWTimestamp getFromTimestamp() {
		return this.fromTimestamp;
	}

	/**
	 * @return IWTimestamp
	 */
	public IWTimestamp getToTimestamp() {
		return this.toTimestamp;
	}

	/**
	 * Sets the status.
	 * @param status The status to set
	 */
	public void setStatus(String status) {
		this._status = status;
	}

	/**
	 * Sets the sortBy.
	 * @param sortBy The sortBy to set
	 */
	public void setSortBy(int sortBy) {
		this._sortBy = sortBy;
	}

	/**
	 * Sets the fromTimestamp.
	 * @param fromTimestamp The fromTimestamp to set
	 */
	public void setFromTimestamp(String timestamp) {
		if (timestamp != null) {
			this.fromTimestamp = new IWTimestamp(timestamp);
		}
		else {
			this.fromTimestamp = null;
		}
	}

	/**
	 * Sets the toTimestamp.
	 * @param toTimestamp The toTimestamp to set
	 */
	public void setToTimestamp(String timestamp) {
		if (timestamp != null) {
			this.toTimestamp = new IWTimestamp(timestamp);
		}
		else {
			this.toTimestamp = null;
		}
	}


	/**
	 * @return int
	 */
	public int getSchoolTypeID() {
		return this._schoolTypeID;
	}

	/**
	 * Sets the groupID.
	 * @param groupID The groupID to set
	 */
	public void setSchoolTypeID(int schTypeID) {
		this._schoolTypeID = schTypeID;
	}

	/**
	 * @return int
	 */
	public int getGroupID() {
		return this._groupID;
	}

	/**
	 * Sets the groupID.
	 * @param groupID The groupID to set
	 */
	public void setGroupID(int groupID) {
		this._groupID = groupID;
	}

	/**
	 * @return
	 */
	public int getSeasonID() {
		return this._seasonID;
	}

	/**
	 * @param seasonID
	 */
	public void setSeasonID(int seasonID) {
		this._seasonID = seasonID;
	}

	/**
	 * @return Returns the _caseCode.
	 */
	public String getCaseCode() {
		return this._caseCode;
	}
	
	/**
	 * @param code The _caseCode to set.
	 */
	public void setCaseCode(String caseCode) {
		this._caseCode = caseCode;
	}
	
	private CareBusiness getCareBusiness() throws RemoteException {
		return (CareBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CareBusiness.class);
	}
}