package se.idega.idegaweb.commune.childcare.event;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import se.idega.idegaweb.commune.childcare.presentation.ChildCareAdmin;

import com.idega.business.IBOLookup;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;

/**
 * @author laddi
 */
public class ChildCareEventListener implements IWPageEventListener {

	/**
	 * @see com.idega.business.IWEventListener#actionPerformed(com.idega.presentation.IWContext)
	 */
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			ChildCareSession session = getChildCareSession(iwc);
	
			if (iwc.isParameterSet(session.getParameterChildCareID()))
				session.setChildCareID(Integer.parseInt(iwc.getParameter(session.getParameterChildCareID())));

			if (iwc.isParameterSet(session.getParameterUserID()))
				session.setChildID(Integer.parseInt(iwc.getParameter(session.getParameterUserID())));

			if (iwc.isParameterSet(session.getParameterApplicationID()))
				session.setApplicationID(Integer.parseInt(iwc.getParameter(session.getParameterApplicationID())));

			if (iwc.isParameterSet(session.getParameterFrom()))
				session.setFromTimestamp(iwc.getParameter(session.getParameterFrom()));

			if (iwc.isParameterSet(session.getParameterTo()))
				session.setToTimestamp(iwc.getParameter(session.getParameterTo()));

			if (iwc.isParameterSet(session.getParameterSortBy()))
				session.setSortBy(Integer.parseInt(iwc.getParameter(session.getParameterSortBy())));
				
			if (iwc.isParameterSet(session.getParameterGroupID()))
				session.setGroupID(Integer.parseInt(iwc.getParameter(session.getParameterGroupID())));
				
			if (iwc.isParameterSet(session.getParameterCheckID()))
				session.setCheckID(Integer.parseInt(iwc.getParameter(session.getParameterCheckID())));
				
			if (iwc.isParameterSet(session.getParameterSeasonID()))
				session.setSeasonID(Integer.parseInt(iwc.getParameter(session.getParameterSeasonID())));

			if (session.getSortBy() == ChildCareAdmin.SORT_ALL) {
				session.setSortBy(-1);
				session.setFromTimestamp(null);
				session.setToTimestamp(null);
			}
				
			return true;
		}
		catch (RemoteException re) {
			return false;
		}
	}

	private ChildCareSession getChildCareSession(IWContext iwc) throws RemoteException {
		return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);	
	}
}
