package se.idega.idegaweb.commune.childcare.presentation;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueueHomeImpl;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.builder.data.IBPage;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.InterfaceObject;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;
/**
 * ChildCareQueueUpdateTable
 * @author <a href="mailto:joakim@idega.is">Joakim</a>
 * @version $Id: ChildCareQueueUpdateTable.java,v 1.3 2003/04/22 18:48:36 joakim Exp $
 * @since 12.2.2003 
 */
public class ChildCareQueueUpdateTable extends CommuneBlock {
	private final static String[] SUBMIT = new String[] { "ccot_submit", "Next" };
	private final static String[] CANCEL = new String[] { "ccot_cancel", "Cancel" };
	public final static int PAGE_1 = 1;
	public final static int PAGE_2 = 2;
	public final static int PAGE_3 = 3;
	public final static String STATUS_UBEH = "UBEH";
	private String prmChildId = CitizenChildren.getChildIDParameterName();
	/**
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	public void main(IWContext iwc) throws Exception {
		System.out.println("Debug: Started the ChildCareQueueUpdateTable");
		Form form = new Form();
		Table layoutTbl = new Table();
		switch (parseAction(iwc)) {
			case CCConstants.ACTION_SUBMIT_1 :
				iwc.setSessionAttribute(CCConstants.SESSION_ACCEPTED_STATUS, getAcceptedStatus(iwc));
//<<<<<<< ChildCareQueueUpdateTable.java
//				createPagePhase2(iwc, layoutTbl);
//=======
//				createPagePhase2(layoutTbl);
//>>>>>>> 1.2
				break;
			case CCConstants.ACTION_SUBMIT_CONFIRM :
				createPagePhase1(iwc, layoutTbl);
				break;
			case CCConstants.ACTION_CANCEL_1 :
				/**@todo: What should happen here? */
				iwc.forwardToIBPage(getParentPage(), getEndPage());
				break;
			case CCConstants.ACTION_SUBMIT_2 :
				//				iwc.setSessionAttribute(CCConstants.SESSION_KEEP_IN_QUEUE, getKeepInQueue(iwc));
				handleAcceptStatus((List) iwc.getSessionAttribute(CCConstants.SESSION_ACCEPTED_STATUS));
				handleKeepQueueStatus(getKeepInQueue(iwc));
				iwc.forwardToIBPage(getParentPage(), getEndPage());
				//				createSubmitPage(iwc, layoutTbl);
				break;
			case CCConstants.ACTION_CANCEL_2 :
				/**@todo: What should happen here? */
				iwc.forwardToIBPage(getParentPage(), getEndPage());
				break;
			case CCConstants.ACTION_REQUEST_INFO :
				/**@todo: How do i 'connect' to the message editor block? */
//				ChildCareApplication application =
//					getChildCareBusiness(iwc).getApplicationByPrimaryKey(iwc.getParameter(CCConstants.APPID));
//				getChildCareBusiness(iwc).sendMessageToProvider(
//					application,
//					"Requst for information",
//					"Requesting information...",
//					application.getOwner());
				createRequestInfoConfirmPage(layoutTbl);
				/*	layoutTbl.add(new Text("[Requesting info from " + 
						getChildCareBusiness(iwc)
						.getApplicationByPrimaryKey(iwc.getParameter(CCConstants.APPID))
						.getProvider().getName() + "]")); */
				break;
				//			case CCConstants.ACTION_SUBMIT_3: 
				//				handleKeepQueueStatus(iwc, (List) iwc.getSessionAttribute(CCConstants.SESSION_KEEP_IN_QUEUE));
				//				handleAcceptStatus(iwc, (List) iwc.getSessionAttribute(CCConstants.SESSION_ACCEPTED_STATUS));
				//				break;
			case CCConstants.ACTION_CANCEL_3 :
				break;
			default :
				/**@todo: What should happen here? */
				createPagePhase1(iwc, layoutTbl);
		}
		form.add(layoutTbl);
		add(form);
	}
	//	private void createSubmitPage(IWContext iwc, Table layoutTbl) throws RemoteException{
	//
	//		Iterator acceptedStatus = ((List) iwc.getSessionAttribute(CCConstants.SESSION_ACCEPTED_STATUS)).iterator();
	//		String s = "";
	//		while(acceptedStatus.hasNext()){
	//			AcceptedStatus as = (AcceptedStatus) acceptedStatus.next();
	//			ChildCareApplication application = getChildCareBusiness(iwc).getApplicationByPrimaryKey(as._appid);
	//			
	//			if (as.isDefined()) {
	//				s = "You have descided to ";
	//				if (as.isAccepted()){
	//					s += "accept ";
	//				} else if (as.isRejected() || as.isRejectedNewDate()){
	//					s += "reject ";
	//				}
	//				s += " the offer from " + application.getProvider().getName() + ". ";
	//				
	//				if (as.isRejectedNewDate()){
	//					s += "New date is set to " + as._date + ".";	
	//				
	//				}
	//			}
	//		}
	//		
	//
	//		Iterator keepInQueue = ((List) iwc.getSessionAttribute(CCConstants.SESSION_KEEP_IN_QUEUE)).iterator();
	//		while (keepInQueue.hasNext()){
	//			
	//			String[] status = (String[]) keepInQueue.next();
	//			if (status[0] != null){
	//				if (status[1] != null && status[1].equals(CCConstants.NO)){
	//					s += "<p>You have decided to cancel the appliction sent to " +
	//						getChildCareBusiness(iwc).getApplicationByPrimaryKey(status[0]).getProvider().getName() +
	//						". ";
	//				} else {
	//					s+= "<p> "+getChildCareBusiness(iwc).getApplicationByPrimaryKey(status[0]).getProvider().getName() +
	//	"Status: " + status[1];	
	//				
	//				}
	//			}
	//		}		
	//
	//		SubmitButton submitBtn = new SubmitButton(localize(SUBMIT), CCConstants.ACTION, new Integer(CCConstants.ACTION_SUBMIT_3).toString());
	//		submitBtn.setSubmitConfirm("Are you sure you want to submit?");
	//
	//		//submitBtn.setName(SUBMIT[0] + PAGE_1);
	//		submitBtn.setAsImageButton(true);
	//
	//		SubmitButton cancelBtn = new SubmitButton(localize(CANCEL), CCConstants.ACTION, new Integer(CCConstants.ACTION_CANCEL_3).toString());
	//		//cancelBtn.setName(CANCEL[0] + PAGE_1);
	//		cancelBtn.setAsImageButton(true);	
	//		
	//		layoutTbl.add(new Text(s + "<p>"), 1, 2);
	//		layoutTbl.add(submitBtn, 1, 3);
	//		layoutTbl.add(cancelBtn, 1, 3);
	//		layoutTbl.setAlignment(1, 3, "right");
	//		
	//	}
	/**
	 * Method handleKeepQueueStatus.
	 * @param iwc
	 * @param l
	 * @throws RemoteException
	 * @throws RemoveException
	 */
	private void handleKeepQueueStatus(List l) throws RemoteException, RemoveException {
		Iterator i = l.iterator();
		while (i.hasNext()) {
			String[] status = (String[]) i.next();
			if (status[0] != null) {
				if (status[1] != null && status[1].equals(CCConstants.NO)) {
//					getChildCareBusiness(iwc).rejectOffer(new Integer(status[0]).intValue(), iwc.getCurrentUser());
					//ChildCareApplication application = getChildCareBusiness(iwc).getApplicationByPrimaryKey(status[0]);
					//application.setStatus(CaseBMPBean.STATE_DELETED);
				}
			}
		}
	}

		/**
	 * Method handleAcceptStatus.
	 * @param iwc
	 * @param l
	 * @throws RemoteException
	 */
	private void handleAcceptStatus(List l) throws RemoteException {
		Iterator i = l.iterator();
		while (i.hasNext()) {
			AcceptedStatus status = (AcceptedStatus) i.next();
			if (status.isDefined()) {
//				ChildCareApplication application = getChildCareBusiness(iwc).getApplicationByPrimaryKey(status._appid);
				if (status.equals(CCConstants.YES)) {
					System.out.println("Accepting application.");
//					getChildCareBusiness(iwc).parentsAgree(
//						Integer.valueOf(status._appid).intValue(),
//						application.getOwner(),
//						localize(CCConstants.TEXT_OFFER_ACCEPTED_SUBJECT),
//						getAcceptedMessage(iwc, application));
				} else if (status.equals(CCConstants.NO_NEW_DATE)) {
//					getChildCareBusiness(iwc).rejectOfferWithNewDate(
//						Integer.valueOf(status._appid).intValue(),
//						application.getOwner(),
//						status._date);
					//						localize(CCConstants.TEXT_OFFER_REJECTED_SUBJECT), 
					//						getRejectedMessage(iwc, application),
					//						localize(CCConstants.TEXT_OFFER_ACCEPTED_SUBJECT),
					//						getAcceptedMessage(iwc, application),
					//						application.getOwner()); 
					//						application.setFromDate(status._date);							
				} else if (status.equals(CCConstants.NO)) {
//					getChildCareBusiness(iwc).rejectOffer(
//						Integer.valueOf(status._appid).intValue(),
//						application.getOwner());
					//						localize(CCConstants.TEXT_OFFER_REJECTED_SUBJECT), 
					//						getRejectedMessage(iwc, application),
					//						localize(CCConstants.TEXT_OFFER_ACCEPTED_SUBJECT),
					//						getAcceptedMessage(iwc, application),
					//						application.getOwner()); 
				}
			}
		}
	}

/*	
	private String getAcceptedMessage(IWContext iwc, ChildCareApplication application) throws RemoteException {
		return localize(CCConstants.TEXT_OFFER_ACCEPTED_MESSAGE)
			+ "<br><br>"
			+ getHeader(localize(CCConstants.TEXT_DETAILS) + ":")
			+ "<br>"
			+ localize(CCConstants.TEXT_CUSTOMER)
			+ ": "
			+ iwc.getCurrentUser().getName()
			+ "<br>"
			+ localize(CCConstants.TEXT_CHILD)
			+ ": "
			+ application.getChild().getDisplayName()
			+ "<br>"
			+ localize(CCConstants.TEXT_FROM)
			+ ": "
			+ application.getFromDate();
	}
*/
		//	private String getRejectedMessage(IWContext iwc, ChildCareApplication application) throws RemoteException{
	//		return localize(CCConstants.TEXT_OFFER_REJECTED_MESSAGE)
	//			+"<br><br>"
	//			+ getHeader(localize(CCConstants.TEXT_DETAILS) + ":") + "<br>"
	//			+ localize(CCConstants.TEXT_CUSTOMER) + ": "+ iwc.getCurrentUser().getName() + "<br>"
	//			+ localize(CCConstants.TEXT_CHILD) + ": "+ application.getChild().getDisplayName() + "<br>"
	//			+ localize(CCConstants.TEXT_FROM) + ": "+ application.getFromDate();
	//		
	//	}
	/**
	 * Method getAcceptedStatus.
	 * @param iwc
	 * @return List of AcceptedStatus objects
	 */
	private List getAcceptedStatus(IWContext iwc) {
		List list = new ArrayList();
		int i = 1;
		while (iwc.isParameterSet(CCConstants.APPID + i)) {
			list.add(
				new AcceptedStatus(
					iwc.getParameter(CCConstants.APPID + i),
					iwc.getParameter(CCConstants.ACCEPT_OFFER + i),
					iwc.getParameter(CCConstants.NEW_DATE + i + "_day"),
					iwc.getParameter(CCConstants.NEW_DATE + i + "_month"),
					iwc.getParameter(CCConstants.NEW_DATE + i + "_year")));
			i++;
		}
		return list;
	}
	private class AcceptedStatus {
		String _appid, _status;
		Date _date;
		AcceptedStatus(String appId, String status, String day, String month, String year) {
			_appid = appId;
			_status = status;
			if (day != null && month != null && year != null) {
				IWTimestamp stamp =
					new IWTimestamp(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
				/*Calendar calendar = Calendar.getInstance();
				calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));*/
				try {
					//_date = new java.sql.Date(calendar.getTimeInMillis());
					_date = stamp.getDate();
				} catch (IllegalArgumentException ex) {
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
	}
	/**
	 * Method getKeepInQueue.
	 * @param iwc
	 * @return List of String arrays of length two. Index 0 is application id,
	 * index 1 is keep status.
	 */
	private List getKeepInQueue(IWContext iwc) {
		List list = new ArrayList();
		int i = 1;
		while (iwc.isParameterSet(CCConstants.APPID + i)) {
			list.add(
				new String[] {
					iwc.getParameter(CCConstants.APPID + i),
					iwc.getParameter(CCConstants.KEEP_IN_QUEUE + i)});
			i++;
		}
		add(new Text("Length:" + list.size()));
		return list;
	}
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(CCConstants.ACTION)) {
			return Integer.parseInt(iwc.getParameter(CCConstants.ACTION));
		} else if (iwc.isParameterSet(ChildCareQueueTable.REQUEST_INFO[0])) {
			return CCConstants.ACTION_REQUEST_INFO;
		}
		return CCConstants.NO_ACTION;
	}
	private void createRequestInfoConfirmPage(Table layoutTbl) throws RemoteException {
		SubmitButton submitBtn =
			new SubmitButton(
				localize(SUBMIT),
				CCConstants.ACTION,
				new Integer(CCConstants.ACTION_SUBMIT_CONFIRM).toString());
		//		submitBtn.setName(SUBMIT[0] + PAGE_1);
		submitBtn.setAsImageButton(true);
		layoutTbl.add(new Text("Your request has been sent."), 1, 1);
		layoutTbl.add(submitBtn, 1, 2);
		layoutTbl.setAlignment(1, 2, "right");
	}
	/**
	 * Creates the object for the first step of the selection process
	 * @param iwc
	 * @param layoutTbl
	 * @throws RemoteException
	 */
	private void createPagePhase1(IWContext iwc, Table layoutTbl) throws RemoteException {
		//Get all the choices for the selected child
		Collection choices = findChoices(iwc);
		if (choices.size() == 0) {
			layoutTbl.add(new Text("No choices have been made for this person."));
		} else {
			Table choiceTable = new ChildCareQueueTable(this, sortApplications(choices, false));
			SubmitButton submitBtn =
				new SubmitButton(
					localize(SUBMIT),
					CCConstants.ACTION,
					new Integer(CCConstants.ACTION_SUBMIT_1).toString());
			//		submitBtn.setName(SUBMIT[0] + PAGE_1);
			submitBtn.setAsImageButton(true);
			//			submitBtn.setSubmitConfirm("Are you sure you want to submit?");
			SubmitButton cancelBtn =
				new SubmitButton(
					localize(CANCEL),
					CCConstants.ACTION,
					new Integer(CCConstants.ACTION_CANCEL_1).toString());
			//		cancelBtn.setName(CANCEL[0] + PAGE_1);
			cancelBtn.setAsImageButton(true);
			layoutTbl.add(choiceTable, 1, 1);
			layoutTbl.add(submitBtn, 1, 3);
			layoutTbl.add(cancelBtn, 1, 3);
			layoutTbl.setAlignment(1, 3, "right");
			layoutTbl.add(getHelpTextPage1(), 1, 4);
			layoutTbl.setStyle(1, 4, "padding-top", "15px");
		}
	}
//<<<<<<< ChildCareQueueUpdateTable.java
/*
	private void createPagePhase2(IWContext iwc, Table layoutTbl) throws RemoteException {
=======
	private void createPagePhase2(Table layoutTbl) throws RemoteException {
>>>>>>> 1.2
//		Table appTable = new ChildCarePlaceOfferTable2(this, sortApplications(findApplications(iwc), true));
		SubmitButton submitBtn =
			new SubmitButton(localize(SUBMIT), CCConstants.ACTION, new Integer(CCConstants.ACTION_SUBMIT_2).toString());
		//		submitBtn.setName(SUBMIT[0] + PAGE_2);
		submitBtn.setAsImageButton(true);
		submitBtn.setSubmitConfirm("Are you sure you want to submit?");
		SubmitButton cancelBtn =
			new SubmitButton(localize(CANCEL), CCConstants.ACTION, new Integer(CCConstants.ACTION_CANCEL_2).toString());
		//		cancelBtn.setName(CANCEL[0] + PAGE_2);
		cancelBtn.setAsImageButton(true);
//		layoutTbl.add(appTable, 1, 2);
		layoutTbl.add(submitBtn, 1, 3);
		layoutTbl.add(cancelBtn, 1, 3);
		layoutTbl.setAlignment(1, 3, "right");
	}
*/
		/**
	 * Method findApplications.
	 * @param iwc
	 * @return Collection
	 */
	private Collection findChoices(IWContext iwc) {
		Collection choices = null;
//		Hashtable ht = new Hashtable();
//		ht.
//		SortedSet sort = new TreeSet();
		try {
			int childId = Integer.parseInt(iwc.getParameter(prmChildId));
			ChildCareQueueHomeImpl ccqHome = (ChildCareQueueHomeImpl)IDOLookup.getHome(ChildCareQueue.class);
			choices = ccqHome.findQueueByChild(childId);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) { //parameter not a number
			return new ArrayList(); //empty collection
		} catch (NullPointerException e) { //no parameter set
			return new ArrayList(); //empty collection
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		return choices;
	}

//<<<<<<< ChildCareQueueUpdateTable.java

//=======
	
//>>>>>>> 1.2
	/**
	 * Method getChildCareBusiness.
	 * @param iwc
	 * @return ChildCareBusiness
<<<<<<< ChildCareQueueUpdateTable.java
	 */
/*	private ChildCareQueue getChildCareQueue(IWContext iwc) {
=======
	private ChildCareQueue getChildCareQueue(IWContext iwc) {
>>>>>>> 1.2
		try {
			return (ChildCareQueue) com.idega.business.IBOLookup.getServiceInstance(iwc, ChildCareQueue.class);
		} catch (RemoteException e) {
			return null;
		}
	}
<<<<<<< ChildCareQueueUpdateTable.java
*/
		/**
=======
	 */

	/**
>>>>>>> 1.2
	 * Method sortApplications.
	 * @param apps
	 * @param offerFirst true means that granted application is placed first
	 * @return SortedSet
	 */
	public SortedSet sortApplications(Collection apps, boolean grantedFirst) {
		SortedSet set = new TreeSet();
		Iterator i = apps.iterator();
		while (i.hasNext()) {
			set.add(new ComparableQueue(i.next(), grantedFirst));
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
	private Table getHelpTextPage1() {
		Table tbl = new Table(1, 1);
		tbl.setWidth(1, 1, 700);
		Text t =
			getLocalizedSmallText(
				"ccatp1_help",
				"Om du accepterar erbjudande kan du enbart kvarst� i k� till i de ovanst�ende valen. Du stryks automatiskt fr�n de underliggande alternativen. Om ditt erbjudande g�ller ditt f�rstahandsval har du m�jlighet att v�lja att kvarst� i k� f�r ETT alternativ av de underliggande alternativen.");
		t.setItalic(true);
		tbl.add(t);
		return tbl;
	}
}