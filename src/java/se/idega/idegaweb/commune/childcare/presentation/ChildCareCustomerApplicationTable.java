package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import javax.ejb.RemoveException;

import se.idega.block.pki.business.NBSLoginBusinessBean;
import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.contract.data.Contract;
import com.idega.block.navigation.presentation.UserHomeLink;
import com.idega.block.school.data.School;
import com.idega.builder.data.IBPage;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * ChildCareOfferTable
 * @author <a href="mailto:roar@idega.is">roar</a>
 * @version $Id: ChildCareCustomerApplicationTable.java,v 1.46 2003/06/30 21:03:02 roar Exp $
 * @since 12.2.2003 
 */

public class ChildCareCustomerApplicationTable extends CommuneBlock {

	private final static String[] SUBMIT = { "ccot_submit", "Next" }, CANCEL = { "ccot_cancel", "Cancel" }, SUBMIT_ALERT_2 = { "ccot_alert_2", "Do you want to commit your choice? This can not be undone afterwards." }, NO_PLACEMENT = { "ccot_no_placement", "Detta barn har ingen placering" }, PLACED_AT = { "ccot_placed_at", "Placerad hos" }, PERSONAL_ID = { "ccot_personal_id", "Personal id" }, NAME = { "ccot_name", "Name" }, REQUEST_CONFIRM = { "ccot_request_sent_confirm", "Your request has been sent." }, NO_APPLICATION = { "ccot_no_application", "No application found" }, NEW_CARETIME = { "ccot_new_caretime", "New caretime" }, END_CARETIME = { "ccot_end_caretime", "Avsluta kontrakt" }, REQUEST_SUBJECT = { "ccot_request_subject", "Request for queue information" }, REQUEST_MESSAGE1 = { "ccot_request_message1", "Parents of" }, REQUEST_MESSAGE2 = 	{ "ccot_request_message2", "are requesting queue information." }, SIGN_TOOLTIP = new String[] {"ccot_sign_tooltip", "Sign contract"};

	public final static int PAGE_1 = 1;
	public final static int PAGE_2 = 2;

	//Redeclaration of constants from CaseBMPBean, because there they are not declared public...
	public final static String STATUS_BVJD = "BVJD";
	public final static String STATUS_PREL = "PREL";
	public final static String STATUS_TYST = "TYST";

	//Session variable for disabling used request-buttons
	final static String REQ_BUTTON = "REQ_BUTTON";
	//Session variable for rejected and cancelled applications
	final static String DELETED_APPLICATIONS = "DELETED_APPLICATIONS";

	private String CHILD_ID = CitizenChildren.getChildIDParameterName();

	ChildCareBusiness childCarebusiness = null;

	/**
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {

		setCacheable(false);
		childCarebusiness = getChildCareBusiness(iwc);

		Form form = new Form();
		Table layoutTbl = new Table();
		layoutTbl.setCellpadding(0);
		layoutTbl.setCellspacing(0);
		layoutTbl.setWidth(getWidth());

		Collection applications = findApplications(iwc);

		switch (parseAction(iwc)) {
			case CCConstants.ACTION_SUBMIT_1 :
				handleAcceptStatus(iwc, getAcceptedStatus(iwc));
				applications = findApplications(iwc);
				if (getChildCareBusiness(iwc).hasOutstandingOffers(getChildId(iwc))) {
					form.setOnSubmit(createPagePhase1(iwc, layoutTbl, applications));
				}
				else {
					form.setOnSubmit(createPagePhase2(iwc, layoutTbl, applications));
				}
				break;

			case CCConstants.ACTION_SUBMIT_2 :
				handleKeepQueueStatus(iwc, getKeepInQueue(iwc));
				if (getEndPage() != null) {
					iwc.forwardToIBPage(getParentPage(), getEndPage());
				}
				break;

			case CCConstants.ACTION_CANCEL_1 :
			case CCConstants.ACTION_CANCEL_2 :
				if (getEndPage() != null)
					iwc.forwardToIBPage(getParentPage(), getEndPage());
				else
					iwc.forwardToIBPage(getParentPage(), getChildCareBusiness(iwc).getUserBusiness().getHomePageForUser(iwc.getCurrentUser()));
				break;

			case CCConstants.ACTION_REQUEST_INFO :
				ChildCareApplication application = getChildCareBusiness(iwc).getApplicationByPrimaryKey(iwc.getParameter(CCConstants.APPID));
				getChildCareBusiness(iwc).sendMessageToProvider(
					application,
					localize(REQUEST_SUBJECT),
					localize(REQUEST_MESSAGE1)
						+ " "
						+ application.getChild().getName()
						+ ", "
						+ application.getChild().getPersonalID()
						+ " "
						+ localize(REQUEST_MESSAGE2),
					application.getOwner());
				
				iwc.setSessionAttribute(REQ_BUTTON + application.getNodeID(), new Boolean(true));
				createRequestInfoConfirmPage(layoutTbl);
				break;

			case CCConstants.ACTION_DELETE :
				application = getChildCareBusiness(iwc).getApplicationByPrimaryKey(iwc.getParameter(CCConstants.APPID));
				//				application.setApplicationStatus(childCarebusiness.getStatusRejected());
				//				application.setStatus(STATUS_TYST);

				addDeletedAppToSession(iwc, application);
				getChildCareBusiness(iwc).removeFromQueue(application, iwc.getCurrentUser());
				applications = findApplications(iwc);
				form.setOnSubmit(createPagePhase1(iwc, layoutTbl, applications));

				break;

			case CCConstants.ACTION_SUBMIT_CONFIRM :
				if (getEndPage() != null)
					iwc.forwardToIBPage(getParentPage(), getEndPage());
				else
					iwc.forwardToIBPage(getParentPage(), getChildCareBusiness(iwc).getUserBusiness().getHomePageForUser(iwc.getCurrentUser()));
				break;

			default :
				iwc.removeSessionAttribute(DELETED_APPLICATIONS);
				form.setOnSubmit(createPagePhase1(iwc, layoutTbl, applications));

		}

		form.add(layoutTbl);
		add(form);
	}

	/**
	 * Finds and returns the command action
	 * @param iwc
	 * @return
	 */
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(CCConstants.ACTION)) {
			return Integer.parseInt(iwc.getParameter(CCConstants.ACTION));
		}
		else if (iwc.isParameterSet(ChildCarePlaceOfferTable1.REQUEST_INFO[0])) {
			return CCConstants.ACTION_REQUEST_INFO;
		}

		return CCConstants.NO_ACTION;
	}

	/**
	 * Method handleKeepQueueStatus removes applications from the queue as specified in the second screen.
	 * @param iwc
	 * @param l List of String arraya of length 2. First index is application id, second is keep status.
	 * @throws RemoteException
	 * @throws RemoveException
	 */
	private void handleKeepQueueStatus(IWContext iwc, List l) throws RemoteException, RemoveException {
		Iterator i = l.iterator();
		while (i.hasNext()) {
			String[] status = (String[]) i.next();
			if (status[0] != null) {
				if (status[1] != null && status[1].equals(CCConstants.NO)) {
					getChildCareBusiness(iwc).removeFromQueue(new Integer(status[0]).intValue(), iwc.getCurrentUser());
				}
			}
		}

		//delete all removed application from session
		iwc.removeSessionAttribute(DELETED_APPLICATIONS);
	}

	/**
	 * Method getKeepInQueue returns the applications keep request from screen 2.
	 * @param iwc
	 * @return List of String arrays of length two. Index 0 is application id,
	 * index 1 is keep status.
	 */
	private List getKeepInQueue(IWContext iwc) {
		List list = new ArrayList();
		int i = 1;
		while (iwc.isParameterSet(CCConstants.APPID + i)) {
			list.add(new String[] { iwc.getParameter(CCConstants.APPID + i), iwc.getParameter(CCConstants.KEEP_IN_QUEUE + i)});
			i++;
		}
		return list;
	}

	/**
	 * Method handleAcceptStatus handles the accept/reject requests from screen 1.
	 * @param iwc
	 * @param l List of AcceptedStatus objects.
	 * @throws RemoteException
	 */
	private void handleAcceptStatus(IWContext iwc, List l) throws RemoteException {
		Iterator i = l.iterator();
		int acceptedChoiceNumber = 10;

		while (i.hasNext()) {
			AcceptedStatus status = (AcceptedStatus) i.next();

			if (status.isDefined()) {
				ChildCareApplication application = childCarebusiness.getApplicationByPrimaryKey(status._appid);
				User child = application.getChild();
				String subject =
					localize(
						"ccot_offer_answer_subject",
						"Svar p� erbjudande om plats");				

				if (status.equals(CCConstants.YES)) {
					getChildCareBusiness(iwc).parentsAgree(Integer.valueOf(status._appid).intValue(), application.getOwner(),
					subject,
					localize("ccot_accept_msg1", "V�rdnadshavare f�r")
						+ child.getName()
						+ ", "
						+ child.getPersonalID()
						+ " "
						+ localize(
							"ccot_accept_msg2",
							"tackar ja till erbjudandet om platsen hos")
						+ " "
						+ application.getProvider().getName());
						
					acceptedChoiceNumber = application.getChoiceNumber();
				}
				else if (status.equals(CCConstants.NO_NEW_DATE)) {
					getChildCareBusiness(iwc).rejectOfferWithNewDate(Integer.valueOf(status._appid).intValue(), application.getOwner(), status._date);
					getChildCareBusiness(iwc).sendMessageToProvider(
											application,
											subject,
											localize("ccot_new_date_msg1", "V�rdnadshavare f�r")
												+ " "
												+ child.getName()
												+ ", "
												+ child.getPersonalID()
												+ " "
												+ localize(
													"ccot_new_date_msg2",
													"vill flytta fram det �nskade placeringsdatumet til")
												+ status._date,
											application.getOwner());					
				}
				else if (status.equals(CCConstants.NO)) {
					getChildCareBusiness(iwc).rejectOffer(Integer.valueOf(status._appid).intValue(), application.getOwner());
					addDeletedAppToSession(iwc, application);
					getChildCareBusiness(iwc).sendMessageToProvider(
						application,
						subject,
						localize("ccot_reject_msg1", "V�rdnadshavare f�r")
							+ " "
							+ child.getName()
							+ ", "
							+ child.getPersonalID()
							+ " "
							+ localize(
								"ccot_reject_msg1",
								"tackar nej till erbjudandet om platsen hos")
							+ application.getProvider().getName(),
						application.getOwner());					
				}
			}
		}

		//Removing other applications from the queue
		Collection applications = findApplications(iwc);
		Iterator allaps = applications.iterator();
		//If choice 1 accepted, choice 2 shall not be deleted, unless it is already an accepted offer
		int deleteFromChoice = acceptedChoiceNumber == 1 ? 2 : acceptedChoiceNumber;

		while (allaps.hasNext()) {
			ChildCareApplication app = (ChildCareApplication) allaps.next();

			if (app.getChoiceNumber() > deleteFromChoice //TODO: This is probably not nessesary anymore (Roar)
			|| (acceptedChoiceNumber == 2 && app.getChoiceNumber() == 1 && isAccepted(app))) {
				childCarebusiness.removeFromQueue(app.getNodeID(), app.getOwner());
				app.setApplicationStatus(childCarebusiness.getStatusCancelled());

				addDeletedAppToSession(iwc, app);
			}
		}
	}

	/**
	 * Method getAcceptedStatus returns a List of AcceptedStatus objects, where each object 
	 * represent a accept/reject request for an applications.
	 * @param iwc
	 * @return List of AcceptedStatus objects
	 */
	private List getAcceptedStatus(IWContext iwc) {
		List list = new ArrayList();
		int i = 1;
		while (iwc.isParameterSet(CCConstants.APPID + i)) {
			list.add(new AcceptedStatus(iwc.getParameter(CCConstants.APPID + i), iwc.getParameter(CCConstants.ACCEPT_OFFER + i), iwc.getParameter(CCConstants.NEW_DATE + i + "_day"), iwc.getParameter(CCConstants.NEW_DATE + i + "_month"), iwc.getParameter(CCConstants.NEW_DATE + i + "_year")));
			i++;
		}

		return list;
	}

	/**
	 * Return true iff the application has an accepted offer; it has status PREL/C
	 * @param application
	 * @return
	 * @throws RemoteException
	 */
	private boolean isAccepted(ChildCareApplication application) throws RemoteException {

		return application.getStatus().equals(STATUS_PREL) && application.getApplicationStatus() == childCarebusiness.getStatusParentsAccept();
	}

	/**
	 * Add a deleted application to the session so that it will not be deleted from the screen until the session has ended.
	 * @param iwc
	 * @param application
	 * @throws RemoteException
	 */
	private void addDeletedAppToSession(IWContext iwc, ChildCareApplication application) throws RemoteException {
		Collection deletedApps = (Collection) iwc.getSessionAttribute(DELETED_APPLICATIONS);
		if (deletedApps == null) {
			deletedApps = new ArrayList();
			iwc.setSessionAttribute(DELETED_APPLICATIONS, deletedApps);
		}
		//The application is given status TYST/Z, so that it will be rendered correctly (red font)
		//		application.setMessage("Deleted!"); //Todo Roar for debugging only
		application.setApplicationStatus(childCarebusiness.getStatusRejected());
		application.setStatus(STATUS_TYST);
		deletedApps.add(application);
	}

	/**
	 * Represent a accept/reject request for an applications
	 * @author Roar
	 *
	 */
	private class AcceptedStatus {
		String _appid, _status;
		Date _date;
		int _choiceNumber;

		AcceptedStatus(String appId, String status, String day, String month, String year) {
			_appid = appId;
			_status = status;

			if (day != null && month != null && year != null) {
				try {
					IWTimestamp stamp = new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));

					_date = stamp.getDate();
				}
				catch (NumberFormatException ex) {
					_date = new Date(0);
				}
				catch (IllegalArgumentException ex) {
					_date = new Date(0);
				}
			}
		}

		boolean equals(String status) {
			return _status.equals(status);
		}

		boolean isAccepted() {
			return _status != null && _status.equals(CCConstants.YES);
		}

		boolean isRejected() {
			return _status != null && _status.equals(CCConstants.NO);
		}

		boolean isRejectedNewDate() {
			return _status != null && _status.equals(CCConstants.NO_NEW_DATE);
		}

		boolean isDefined() {
			return _status != null;
		}

	} // End class

	/**
	 * Creates confirmation page after pressing request info button
	 * @param layoutTbl
	 * @throws RemoteException
	 */

	private void createRequestInfoConfirmPage(Table layoutTbl) throws RemoteException {
		layoutTbl.add(new Text(localize(REQUEST_CONFIRM)), 1, 1);
		layoutTbl.setHeight(2, 12);
		layoutTbl.add(new UserHomeLink(), 1, 3);
	}

	/**
	 * Construct the html for the first screen
	 * @param iwc
	 * @param layoutTbl
	 * @return
	 * @throws RemoteException
	 */
	private String createPagePhase1(IWContext iwc, Table layoutTbl, Collection applications) throws RemoteException {
		int numberOfApplications = getChildCareBusiness(iwc).getNumberOfApplicationsForChildNotInactive(getChildId(iwc));
		if (numberOfApplications == 0) {
			layoutTbl.add(getSmallErrorText(localize(NO_APPLICATION)));
			return "";
		}
		else {
			layoutTbl.add(new HiddenInput(CCConstants.ACTION, "-1"));
			boolean hasActiveApplication = getChildCareBusiness(iwc).hasActiveApplication(getChildId(iwc));
			Table placementInfo = getPlacedAtSchool(iwc, hasActiveApplication);

			boolean hasOffer = getChildCareBusiness(iwc).hasOutstandingOffers(getChildId(iwc));

			Table appTable = new ChildCarePlaceOfferTable1(iwc, this, sortApplications(applications, false), hasOffer, hasActiveApplication);

			GenericButton cancelBtn = (GenericButton) getButton(new GenericButton("cancel", localize(CANCEL)));
			cancelBtn.setPageToOpen(getParentPageID());
			cancelBtn.addParameterToPage(CCConstants.ACTION, CCConstants.ACTION_CANCEL_1);

			SubmitButton submitBtn = (SubmitButton) getButton(new SubmitButton(localize(SUBMIT)));
			submitBtn.setValueOnClick(CCConstants.ACTION, String.valueOf(CCConstants.ACTION_SUBMIT_1));
			
			int row = 1;
			layoutTbl.add(placementInfo, 1, row++);
			if (applications.size() > 0) {
				layoutTbl.setHeight(row++, 12);
				layoutTbl.add(appTable, 1, row++);
			}
			if (hasOffer) {
				layoutTbl.setHeight(row++, 12);
				layoutTbl.add(cancelBtn, 1, row);
				layoutTbl.add(Text.getNonBrakingSpace(), 1, row);
				layoutTbl.add(submitBtn, 1, row);
				layoutTbl.setAlignment(1, row++, Table.HORIZONTAL_ALIGN_RIGHT);
				layoutTbl.setHeight(row++, 12);
				layoutTbl.add(getHelpTextPage1(), 1, row);
			}
			else {
				layoutTbl.setHeight(row++, 12);
				layoutTbl.add(new UserHomeLink(), 1, row);
			}
			return ((ChildCarePlaceOfferTable1) appTable).getOnSubmitHandler();
		}
	}

	private Table getPlacedAtSchool(IWContext iwc, boolean hasActiveApplication) throws RemoteException {
		hasActiveApplication = ! hasActiveApplication; //UNUSED! TODO: REMOVE THIS PARAMETER
		
		Table layoutTbl = new Table();
		layoutTbl.setCellpadding(0);
		layoutTbl.setCellspacing(0);
		layoutTbl.setColumns(3);
		layoutTbl.setWidth(2, 6);
		int row = 1;

		String childId = iwc.getParameter(CHILD_ID);
		if (childId == null) {
			childId = (String) iwc.getSessionAttribute(CHILD_ID);
		}

		User child = UserBusiness.getUser(Integer.parseInt(childId));
		layoutTbl.add(getSmallHeader(localize(NAME) + ":"), 1, row);
		layoutTbl.add(getSmallText(child.getName()), 3, row++);
		layoutTbl.add(getSmallHeader(localize(PERSONAL_ID) + ":"), 1, row);
		layoutTbl.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), 3, row++);

		ChildCareApplication acceptedApplication = getChildCareBusiness(iwc).getAcceptedApplicationsByChild(Integer.parseInt(childId));
		if (acceptedApplication != null) {
			IWTimestamp fromDate = new IWTimestamp(acceptedApplication.getFromDate());
			layoutTbl.setHeight(row++, 12);
			layoutTbl.add(getSmallHeader(localize("child_care.in_process", "In process") + ":"), 1, row);
			layoutTbl.add(getSmallText(acceptedApplication.getProvider().getSchoolName()), 3, row++);
			layoutTbl.add(getSmallHeader(localize("child_care.placement_date", "Placement date") + ":"), 1, row);
			layoutTbl.add(getSmallText(fromDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), 3, row++);

			boolean hasBankId = false;
			try{
				hasBankId = new NBSLoginBusinessBean().hasBankLogin(acceptedApplication.getOwner().getID());
			}catch(SQLException ex){
				//ignore
			}
			if (hasBankId){
				Collection contracts = childCarebusiness.getContractsByApplication(acceptedApplication.getNodeID());
				Iterator i = contracts.iterator();
				while (i.hasNext()){
					Contract c = ((ChildCareContractArchive) i.next()).getContract();
					System.out.println("CONTRACT: " + c.getPrimaryKey());
					if (! c.isSigned()){
						Link signBtn = new Link(localize(SIGN_TOOLTIP));
						signBtn.setWindowToOpen(ChildCareWindowBig.class);
						signBtn.addParameter(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_SIGN_CONTRACT);
						signBtn.setParameter(ChildCareAdminWindow.PARAMETER_CONTRACT_ID, c.getPrimaryKey().toString());
						signBtn.setAsImageButton(true);
						layoutTbl.setHeight(row++, 6);						
						layoutTbl.add(signBtn, 3, row++);
					}
				}
			}			
		}
	
		ChildCareApplication activeApplication = this.getChildCareBusiness(iwc).getActiveApplicationByChild(Integer.parseInt(childId)); 
		
		if (activeApplication != null) {
			ChildCareContractArchive archive = getChildCareBusiness(iwc).getValidContract(((Integer)activeApplication.getPrimaryKey()).intValue());
			School school = activeApplication.getProvider();

			layoutTbl.setHeight(row++, 12);
			layoutTbl.add(getSmallHeader(localize(PLACED_AT) + ":"), 1, row);
			layoutTbl.add(getSmallText(school.getName()), 3, row++);
			layoutTbl.add(getSmallText(school.getSchoolAddress()), 3, row++);
			layoutTbl.add(getSmallText(school.getSchoolPhone()), 3, row++);

			GenericButton careTimePopup = (GenericButton) getButton(new GenericButton("new_care_time", localize(NEW_CARETIME)));
			careTimePopup.setWindowToOpen(ChildCareWindow.class);
			careTimePopup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, String.valueOf(ChildCareAdminWindow.METHOD_NEW_CARE_TIME));
			careTimePopup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
			careTimePopup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, activeApplication.getNodeID());
			
			careTimePopup.addParameterToWindow(CCConstants.APPID, activeApplication.getNodeID());

			GenericButton cancelPopup = (GenericButton) getButton(new GenericButton("end_contract", localize(END_CARETIME)));
			cancelPopup.setWindowToOpen(ChildCareWindow.class);
			cancelPopup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, String.valueOf(ChildCareAdminWindow.METHOD_END_CONTRACT));
			cancelPopup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
			cancelPopup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, activeApplication.getNodeID());

			layoutTbl.setHeight(row++, 12);
			layoutTbl.add(careTimePopup, 3, row);
			layoutTbl.add(Text.getNonBrakingSpace(), 3, row);
			layoutTbl.add(cancelPopup, 3, row);
			
			if (archive != null) {
				GenericButton contractPopup = (GenericButton) getButton(new GenericButton("contract", localize("child_care.show_contract", "Show contract")));
				contractPopup.setFileToOpen(archive.getContractFileID());
				layoutTbl.add(Text.getNonBrakingSpace(), 3, row);
				layoutTbl.add(contractPopup, 3, row);
			}
		}
		else {
			layoutTbl.setHeight(row++, 12);
			layoutTbl.add(getSmallErrorText(localize(NO_PLACEMENT)), 1, row);
			layoutTbl.mergeCells(1, row, 3, row);
		}
		return layoutTbl;
	}

	/**
	 * Construct the html for the second screen
	 * @param layoutTbl
	 * @param applications
	 * @throws RemoteException
	 */
	private String createPagePhase2(IWContext iwc, Table layoutTbl, Collection applications) throws RemoteException {
		Table appTable = new ChildCarePlaceOfferTable2(iwc, this, sortApplications(applications, true));

		GenericButton cancelBtn = (GenericButton) getButton(new GenericButton("cancel", localize(CANCEL)));
		cancelBtn.setPageToOpen(getParentPageID());
		cancelBtn.addParameterToPage(CCConstants.ACTION, CCConstants.ACTION_CANCEL_1);

		SubmitButton submitBtn = (SubmitButton) getButton(new SubmitButton(localize(SUBMIT)));
		submitBtn.setValueOnClick(CCConstants.ACTION, String.valueOf(CCConstants.ACTION_SUBMIT_1));

		layoutTbl.add(new HiddenInput(CCConstants.ACTION, "-1"));
		layoutTbl.add(appTable, 1, 1);
		layoutTbl.setHeight(2, 12);
		layoutTbl.add(cancelBtn, 1, 3);
		layoutTbl.add(Text.getNonBrakingSpace(), 1, 3);
		layoutTbl.add(submitBtn, 1, 3);
		layoutTbl.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);

		return "return confirm('" + localize(SUBMIT_ALERT_2) + "')";
	}

	/**
	 * Method findApplications finds application for a specific child. 
	 * Removed applications from earlier sessions is not included.
	 * Applications removed in this session is included.
	 * @param iwc
	 * @return Collection
	 */
	private Collection findApplications(IWContext iwc) {
		Collection applications = null;

		try {
			String childId = iwc.getParameter(CHILD_ID);

			if (childId != null) {
				iwc.setSessionAttribute(CHILD_ID, childId);
			}
			else {
				childId = (String) iwc.getSessionAttribute(CHILD_ID);
			}

			applications = getChildCareBusiness(iwc).getUnhandledApplicationsByChild(Integer.parseInt(childId));

			//Add canceled and removed applications from this session	
			/*Collection deletedApps = (Collection) iwc.getSessionAttribute(DELETED_APPLICATIONS);
			if (deletedApps != null) {
				applications.addAll(deletedApps);
			}*/

		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (NumberFormatException e) { //parameter not a number
			return new ArrayList(); //empty collection
		}
		catch (NullPointerException e) { //no parameter set
			return new ArrayList(); //empty collection
		}

		return applications;
	}

	private int getChildId(IWContext iwc) {

		String childId = iwc.getParameter(CHILD_ID);

		if (childId != null) {
			iwc.setSessionAttribute(CHILD_ID, childId);
		}
		else {
			childId = (String) iwc.getSessionAttribute(CHILD_ID);
		}

		//System.out.println("getChildId()eturning:" + childId);
		return Integer.parseInt(childId);
	}

	/**
	 * Checks if the specifid application has an offer connected to it (status BVJD/B).
	 * @param applications
	 * @return
	 * @throws RemoteException
	 */
	/*
	private boolean hasOffer(Collection applications) throws RemoteException {

		Iterator i = applications.iterator();

		while (i.hasNext()) {
			ChildCareApplication app = (ChildCareApplication) i.next();

			String caseStatus = app.getStatus();
			char appStatus = app.getApplicationStatus();
			//			System.out.println(
			//				"STATUS: "
			//					+ app.getNodeID()
			//					+ " - "
			//					+ caseStatus
			//					+ "/"
			//					+ appStatus);
			if (caseStatus.equals(ChildCareCustomerApplicationTable.STATUS_BVJD) && appStatus != childCarebusiness.getStatusAccepted())
				return true;

		}
		return false;
	}*/

	/**
	 * Checks if the specifid application has an offer connected to it (status BVJD/B).
	 * @param applications
	 * @return
	 * @throws RemoteException
	 */
	//	private ChildCareApplication getAcceptedOffer(Collection applications)
	//		throws RemoteException {
	//
	//		Iterator i = applications.iterator();
	//
	//		while (i.hasNext()) {
	//			ChildCareApplication app = (ChildCareApplication) i.next();
	//
	//			String caseStatus = app.getStatus();
	//			char appStatus = app.getApplicationStatus();
	//			//				System.out.println(
	//			//					"STATUS: "
	//			//						+ app.getNodeID()
	//			//						+ " - "
	//			//						+ caseStatus
	//			//						+ "/"
	//			//						+ appStatus);
	//			if (caseStatus
	//				.equals(ChildCareCustomerApplicationTable.STATUS_PREL)
	//				&& appStatus == childCarebusiness.getStatusParentsAccept())
	//				return app;
	//
	//		}
	//		return null;
	//	}

	/*
	private ChildCareApplication getActiveApplication(Collection applications) throws RemoteException {

		Iterator i = applications.iterator();
		while (i.hasNext()) {
			ChildCareApplication app = (ChildCareApplication) i.next();

			if (app.isActive()) {
				return app;
			}
		}
		return null;
	}*/

	/**
	 * Method getChildCareBusiness returns the ChildCareBusiness object.
	 * @param iwc
	 * @return ChildCareBusiness
	 */
	ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (ChildCareBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}

	UserBusiness getUserBusiness(IWContext iwc) {
		try {
			return (UserBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		}
		catch (RemoteException e) {
			return null;
		}
	}

	/**
	 * Method sortApplications sorts a Collection of applications.
	 * @param apps
	 * @param grantedFirst true implies that an application that is granted and accepted by the citizen is placed first
	 * @return SortedSet the sorted set of applications
	 */

	public SortedSet sortApplications(Collection apps, boolean grantedFirst) {
		SortedSet set = new TreeSet();
		Iterator i = apps.iterator();
		while (i.hasNext()) {
			set.add(new ComparableApp(i.next(), grantedFirst));
		}

		return set;
	}

	/**
	 * Method getLocalHeader is used from classes that doens't subclass
	 * CommuneBlock, but has a refernce to an object of this class.
	 * @param key
	 * @param defaultValue
	 * @return Text
	 */
	public Text getLocalHeader(String key, String defaultValue) {
		return getSmallHeader(localize(key, defaultValue));
	}

	private IBPage _endPage;

	/**
	 * 
	 * Property method
	 * @param page The page to return after finshed or cancelled
	 */
	public void setEndPage(IBPage page) {
		_endPage = page;
	}

	public IBPage getEndPage() {
		return _endPage;
	}

	//Because these methods is made protected in CommuneBlock, 
	//they need to be made public to delegates

	public String getZebraColor1() {
		return super.getZebraColor1();
	}

	public String getZebraColor2() {
		return super.getZebraColor2();
	}

	public Text getSmallHeader(String s) {
		return super.getSmallHeader(s);
	}

	public String getHeaderColor() {
		return super.getHeaderColor();
	}

	public int getCellpadding() {
		return super.getCellpadding();
	}

	public int getCellspacing() {
		return super.getCellspacing();
	}

	public Text getSmallText(String s) {
		return super.getSmallText(s);
	}

	public String getSmallTextFontStyle() {
		return super.getSmallTextFontStyle();
	}

	public InterfaceObject getStyledInterface(InterfaceObject o) {
		return super.getStyledInterface(o);
	}

	public Image getEditIcon(String toolTip) {
		return super.getEditIcon(toolTip);
	}

	public Image getDeleteIcon(String toolTip) {
		return super.getDeleteIcon(toolTip);
	}
	
	public Image getSignIcon(String toolTip) {
		return super.getVariousIcon(toolTip);
	}	
	

	public Image getQuestionIcon(String toolTip) {
		return super.getQuestionIcon(toolTip);
	}

	private Table getHelpTextPage1() {
		Table tbl = new Table(1, 1);
		tbl.setWidth(1, 1, 700);
		Text t = getLocalizedSmallText("ccot1_help", "Om du accepterar erbjudande kan du enbart kvarst� i k� till i de ovanst�ende valen. Du stryks automatiskt fr�n de underliggande alternativen. Om ditt erbjudande g�ller ditt f�rstahandsval har du m�jlighet att v�lja att kvarst� i k� f�r ETT alternativ av de underliggande alternativen.");
		t.setItalic(true);
		tbl.add(t);
		return tbl;
	}

	//property setDebug

	private boolean _debug = false;

	public void setDebug(boolean debug) {
		_debug = debug;
	}

	public boolean getDebug() {
		return _debug;
	}

	/**
	 * Returns a string of debug information if the property setDebug is turned on, empty string otherwise.
	 * @param app
	 * @return
	 * @throws RemoteException
	 */
	String getDebugInfo(ChildCareApplication app) throws RemoteException {
		return (getDebug()) ? " (Id:" + app.getNodeID() + " - " + app.getStatus() + " - " + app.getApplicationStatus() + ")" : "";
	}

}