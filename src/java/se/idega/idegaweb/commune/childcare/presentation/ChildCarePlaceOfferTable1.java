package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.SortedSet;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
	
class ChildCarePlaceOfferTable1 extends Table{	
	
	private static Text HEADER_YOUR_CHOICE;
	private static Text HEADER_OFFER;
	private static Text HEADER_PROGNOSE;
	private static Text HEADER_QUEUE_INFO;
	private static Text HEADER_YES;
	private static Text HEADER_YES_BUT;
	private static Text HEADER_NO;

	private static String GRANTED;

	private static boolean _initializeStatics = false;
	

	private static ChildCareCustomerApplicationTable _page;
	


	final static String[] REQUEST_INFO = new String[]{"ccatp1_request_info", "Request info"};
	

	private void initConstants(ChildCareCustomerApplicationTable page){
		if (!_initializeStatics) {
			_page = page;
			HEADER_YOUR_CHOICE = page.getLocalHeader("ccatp1_your_choice", "Your Choice");
			HEADER_OFFER = page.getLocalHeader("ccatp1_offer", "Offer");
			HEADER_PROGNOSE = page.getLocalHeader("ccatp1_prognose", "Prognoses");
			HEADER_QUEUE_INFO = page.getLocalHeader("ccatp1_queue_info", "Request queue information");
			HEADER_YES = page.getLocalHeader("ccatp1_yes", "Yes");
			HEADER_YES_BUT = page.getLocalHeader("ccatp1_yes_but", "No, but don't delete from queue");
			HEADER_NO = page.getLocalHeader("ccatp1_no", "No");
	
			GRANTED = page.localize("ccatp1_granted", "You have received an offer from ").toString();

			_initializeStatics = true;
		}
	}
	
	public ChildCarePlaceOfferTable1(ChildCareCustomerApplicationTable page, SortedSet applications) throws RemoteException {
		super(7, applications.size() + 1);
		initConstants(page);
	
		initTable();
					
		System.out.println("Applications: " + applications);
		Iterator i = applications.iterator();
		int row = 2;
		boolean disable = false;
		while (i.hasNext()) {
			ChildCareApplication app = ((ComparableApp) i.next()).getApplication();
			app.getChoiceNumber();
			
			String id = ((Integer) app.getPrimaryKey()).toString();
				
			String name = app.getProvider().getName();
				
			String offerText = "";
			boolean offer = app.getStatus().equalsIgnoreCase(ChildCareCustomerApplicationTable.STATUS_UBEH); /**@TODO: is this correct status?*/
			if (offer) {
					offerText = GRANTED + app.getFromDate(); 
			}
					
			String prognosis = app.getPrognosis() != null ? app.getPrognosis() : "";
	
			addToTable(row, id, app.getChoiceNumber() + ": " + name 
				//+ " (nodeId:" + app.getNodeID() + ")"
				, offerText, prognosis, offer, disable);
	
			if (offer){
				disable = true;
			}
			
			row++;
			
		}
	}
	
	/**
	 * Method addToTable.
	 * @param table
	 * @param row 
	 * @param name
	 * @param status
	 * @param prognosis
	 */
	private void addToTable(int row, String id, String name, String status, String prognosis, boolean offer, boolean disable) {
		int index = row - 1; //row=2 for first row because of heading is in row 1
		add(new HiddenInput(CCConstants.APPID + index, id)); 
		String textColor = disable ? "gray":"black";

		if (name != null){
			Text t = _page.getSmallText(name);
			t.setStyleAttribute("color:" + textColor);
			add(t, 1, row);
		}
		if (status != null){
			Text t = _page.getSmallText(status);
			t.setStyleAttribute("color:" + textColor);
			add(t, 2, row);
		}
		if (prognosis != null){
			Text t = _page.getSmallText(prognosis);
			t.setStyleAttribute("color:" + textColor);
			add(t, 3, row);
		}
		//add(prognosis, 3, row);
		//SubmitButton reqBtn = new SubmitButton(_page.localize(REQUEST_INFO), CCConstants.APPID, id);
		Link reqBtn = new Link(_page.localize(REQUEST_INFO));
		reqBtn.addParameter(CCConstants.ACTION, CCConstants.ACTION_REQUEST_INFO);
		reqBtn.addParameter(CCConstants.APPID, id);
		
		reqBtn.setName(REQUEST_INFO[0]);
		reqBtn.setAsImageButton(true);
			
		RadioButton rb1 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.YES);
		RadioButton rb2 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.NO_NEW_DATE);
		RadioButton rb3 = new RadioButton(CCConstants.ACCEPT_OFFER + index, CCConstants.NO);
	/*	rb1.setOnChange(CCConstants.NEW_DATE + index + ".disabled=true;"); //NewDate" + index + ".value=''
		rb2.setOnChange(CCConstants.NEW_DATE + index + ".disabled=false;");
		rb3.setOnChange(CCConstants.NEW_DATE + index + ".disabled=true;"); //NewDate" + index + ".value=''
	*/	
		rb1.setSelected();
		DateInput date = (DateInput) _page.getStyledInterface(new DateInput(CCConstants.NEW_DATE + index, true));
		date.setStyleAttribute("style", _page.getSmallTextFontStyle());
		

		
		//TextInput ti = new TextInput(CCConstants.NEW_DATE + index);
		//ti.setLength(8);
		
//The trigger is called when the radio button is turned on

		
//		ti.setWidth("10");

        if (!disable){
			add(reqBtn, 4, row);
        }

			
		if (offer){
			
			add(rb1, 5, row);
			add(rb2, 6, row);
			add(date, 6, row);
			add(rb3, 7, row);
		}


		if (row % 2 == 0)
			setRowColor(row++, _page.getZebraColor1());
		else
			setRowColor(row++, _page.getZebraColor2());
					
	}
	
	/**
	 * Method createTable.
	 * @return Table
	 */
	private void initTable() {
//		Table table = new Table(7, rows + 1); //Heading
//		setBorder(1);
//		setBorderColor("GREEN");

		setRowColor(1, _page.getHeaderColor());

		
		setCellspacing(2);
		setCellpadding(4);
	
		//Heading
		add(HEADER_YOUR_CHOICE, 1, 1);
		add(HEADER_OFFER, 2, 1);
		add(HEADER_PROGNOSE, 3, 1);
		add(HEADER_QUEUE_INFO, 4, 1);
		add(HEADER_YES, 5, 1);
		add(HEADER_YES_BUT, 6, 1);
		add(HEADER_NO, 7, 1);
	}
	

	
}
