package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCareSessionBean extends IBOSessionBean implements ChildCareSession {

	protected static final String PARAMETER_CHILD_CARE_ID = "cc_c_c_id";
	protected static final String PARAMETER_GROUP_ID = "cc_group_id";
	protected static final String PARAMETER_USER_ID = "cc_user_id";
	protected static final String PARAMETER_APPLICATION_ID = "cc_application_id";
	protected static final String PARAMETER_FROM = "cc_from";
	protected static final String PARAMETER_TO = "cc_to";
	protected static final String PARAMETER_SORT_BY = "cc_sort_by";

	protected int _childcareID = -1;
	protected int _userID = -1;
	protected int _childID = -1;
	protected int _applicationID = -1;
	protected int _groupID = -1;
	protected int _sortBy = -1;
	protected IWTimestamp fromTimestamp;
	protected IWTimestamp toTimestamp;

	public CommuneUserBusiness getCommuneUserBusiness() throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CommuneUserBusiness.class);
	}
	
	/**
	 * Returns the schoolID.
	 * @return int
	 */
	public int getChildCareID() throws RemoteException {
		User user = getUserContext().getCurrentUser();
		if (user != null) {
			int userID = ((Integer)user.getPrimaryKey()).intValue();
			
			if (_userID == userID) {
				if (_childcareID != -1) {
					return _childcareID;
				}
				else {
					return getChildCareIDFromUser(user);
				}
			}
			else {
				_userID = userID;
				return getChildCareIDFromUser(user);
			}
		}
		else {
			return -1;	
		}
	}
	
	private int getChildCareIDFromUser(User user) throws RemoteException {
		_childcareID = -1;
		if (user != null) {
			try {
				School school = getCommuneUserBusiness().getFirstManagingChildCareForUser(user);
				if (school != null) {
					_childcareID = ((Integer) school.getPrimaryKey()).intValue();
				}
			}
			catch (FinderException fe) {
				_childcareID = -1;
			}
		}
		return _childcareID;
	}

	/**
	 * @return int
	 */
	public int getUserID() {
		return _userID;
	}

	/**
	 * Sets the childcareID.
	 * @param childcareID The childcareID to set
	 */
	public void setChildCareID(int childcareID) {
		_childcareID = childcareID;
	}

	/**
	 * Sets the userID.
	 * @param userID The userID to set
	 */
	public void setUserID(int userID) {
		_userID = userID;
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
	public String getParameterApplicationID() {
		return PARAMETER_APPLICATION_ID;
	}

	/**
	 * @return String
	 */
	public String getParameterGroupID() {
		return PARAMETER_GROUP_ID;
	}

	/**
	 * @return int
	 */
	public int getApplicationID() {
		return _applicationID;
	}

	/**
	 * @return int
	 */
	public int getChildID() {
		return _childID;
	}

	/**
	 * Sets the applicationID.
	 * @param applicationID The applicationID to set
	 */
	public void setApplicationID(int applicationID) {
		_applicationID = applicationID;
	}

	/**
	 * Sets the childID.
	 * @param childID The childID to set
	 */
	public void setChildID(int childID) {
		_childID = childID;
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
	 * @return int
	 */
	public int getSortBy() {
		return _sortBy;
	}

	/**
	 * @return IWTimestamp
	 */
	public IWTimestamp getFromTimestamp() {
		return fromTimestamp;
	}

	/**
	 * @return IWTimestamp
	 */
	public IWTimestamp getToTimestamp() {
		return toTimestamp;
	}

	/**
	 * Sets the sortBy.
	 * @param sortBy The sortBy to set
	 */
	public void setSortBy(int sortBy) {
		_sortBy = sortBy;
	}

	/**
	 * Sets the fromTimestamp.
	 * @param fromTimestamp The fromTimestamp to set
	 */
	public void setFromTimestamp(String timestamp) {
		if (timestamp != null)
			this.fromTimestamp = new IWTimestamp(timestamp);
		else
			this.fromTimestamp = null;
	}

	/**
	 * Sets the toTimestamp.
	 * @param toTimestamp The toTimestamp to set
	 */
	public void setToTimestamp(String timestamp) {
		if (timestamp != null)
			this.toTimestamp = new IWTimestamp(timestamp);
		else
			this.toTimestamp = null;
	}

	/**
	 * @return int
	 */
	public int getGroupID() {
		return _groupID;
	}

	/**
	 * Sets the groupID.
	 * @param groupID The groupID to set
	 */
	public void setGroupID(int groupID) {
		_groupID = groupID;
	}

}