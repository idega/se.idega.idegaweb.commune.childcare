/*
 * Created on 7.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Date;

import se.idega.idegaweb.commune.childcare.business.ChildCareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.data.User;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Window;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChildCareNewCareTimeWindow extends Window {

	private final static int ACTION_NEW_CARE_TIME = 0;

	private final static String[] CARE_TIME_LABEL =
		{ "ccnctw_care_time", "Care time" },
		FROM_DATE_LABEL = { "ccnctw_from_date", "From date" },
		ALERT_UNVALID_DATE_FORMAT =
			{
				"ccnctw_unvalid_date_format_alert",
				"Please choose a valid from date." },
		ALERT_UNVALID_DATE =
			{ "ccnctw_unvalid_date_alert", "The date most be in the future." };

	final static String CARE_TIME = "CARE_TIME";
	final static String FROM_DATE = "FROM_DATE";
	private final static String[] INFO =
		new String[] { "ccnctw_info", "Info about care time." };

	private CommuneBlock style = new CommuneBlock();

	public void main(IWContext iwc) throws Exception {
		ChildCareApplication application =
			getChildCareBusiness(iwc).getApplicationByPrimaryKey(
				iwc.getParameter(CCConstants.APPID));

		if (iwc.getParameter(CCConstants.ACTION) != null
			&& Integer.parseInt(iwc.getParameter(CCConstants.ACTION))
				== ACTION_NEW_CARE_TIME) {

			sendRequest(iwc, application);
		} else {
			makeGUI(iwc, application);
		}

	}

	private void makeGUI(IWContext iwc, ChildCareApplication application) {
		Form form = new Form();
		Table layoutTbl = new Table();

		//		add(new Text("Appid:" + iwc.getParameter(CCConstants.APPID) + "."));

		TextInput careTime = new TextInput(CARE_TIME);
		careTime.setValue(application.getCareTime());
		careTime.setLength(4);
		careTime.setStyleAttribute(style.getSmallTextFontStyle());

		DateInput fromDate = new DateInput(FROM_DATE);
		fromDate.setAsNotEmpty(style.localize(ALERT_UNVALID_DATE_FORMAT));
		fromDate.setStyleAttribute("style", style.getSmallTextFontStyle());

		HiddenInput action = new HiddenInput(CCConstants.ACTION);
		action.setValue(ACTION_NEW_CARE_TIME);

		HiddenInput appid = new HiddenInput(CCConstants.APPID);
		appid.setValue(iwc.getParameter(CCConstants.APPID));

		SubmitButton submit = new SubmitButton(style.localize(CCConstants.OK));
		submit.setAsImageButton(true);
		CloseButton close = new CloseButton(style.localize(CCConstants.CANCEL));
		close.setAsImageButton(true);

		int row = 1;
		layoutTbl.add(
			style.getSmallText(style.localize(CARE_TIME_LABEL) + ":"),
			1,
			row);
		layoutTbl.add(careTime, 2, row++);

		layoutTbl.add(
			style.getSmallText(style.localize(FROM_DATE_LABEL) + ":"),
			1,
			row);
		layoutTbl.add(fromDate, 2, row++);
		fromDate.setEarliestPossibleDate(
			new Date(),
			style.localize(ALERT_UNVALID_DATE));

		row++;

		layoutTbl.add(close, 2, row);
		layoutTbl.add(submit, 2, row);
		layoutTbl.setAlignment(2, row++, "right");

		layoutTbl.add(style.getSmallText(style.localize(INFO)), 1, row);

		form.add(action);
		form.add(appid);
		form.add(layoutTbl);
		form.setOnSubmit("window.close()");

		add(form);

		setWidth(50);
		setHeight(20);
	}

	private void sendRequest(IWContext iwc, ChildCareApplication application)
		throws RemoteException {

		User owner = application.getOwner();
		User child = UserBusiness.getUser(application.getChildId());

		getChildCareBusiness(iwc).sendMessageToParents(
			application,
			style.localize(
				"ccnctw_new_caretime_msg_parents_subject",
				"Beg�ran om �ndrad omsorgstid gjord"),
			style.localize(
				"ccnctw_new_caretime_msg_parents_message",
				"Du har skickat en beg�ran om �ndrad omsorgstid f�r ")
				+ child.getName()
				+ " "
				+ child.getPersonalID());

		getChildCareBusiness(iwc).sendMessageToProvider(
			application,
			style.localize(
				"ccnctw_new_caretime_msg_provider_subject",
				"Beg�ran om �ndrad omsorgstid"),
			owner.getName()
				+ " "
				+ style.localize(
					"ccnctw_new_caretime_msg_provider_message1",
					"har beg�rt �ndrad omsorgstid till")
				+ " "
				+ iwc.getParameter(CARE_TIME)
				+ " "
				+ style.localize(
					"ccnctw_new_caretime_msg_provider_message2",
					"tim/vecka f�r")
				+ " "
				+ child.getName()
				+ " "
				+ child.getPersonalID()
				+ ". "
				+ style.localize(
					"ccnctw_new_caretime_msg_provider_message3",
					"Den nya omsorgstiden skall g�lla fr.o.m.")
				+ " "
				+ iwc.getParameter(FROM_DATE)
				+ ".",
			application.getOwner());
	}

	/**
	 * Method getChildCareBusiness returns the ChildCareBusiness object.
	 * @param iwc
	 * @return ChildCareBusiness
	 */
	ChildCareBusiness getChildCareBusiness(IWContext iwc) {
		try {
			return (
				ChildCareBusiness) com
					.idega
					.business
					.IBOLookup
					.getServiceInstance(
				iwc,
				ChildCareBusiness.class);
		} catch (RemoteException e) {
			return null;
		}
	}
}
