/*
 * Created on 26.5.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;

import com.idega.block.school.business.SchoolContentBusiness;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/**
 * @author laddi
 */
public class PrognosisInformation extends ChildCareBlock {

	int providerID = -1;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		
		if (providerID != -1) {
			ChildCarePrognosis prognosis = getBusiness().getPrognosis(providerID);
			if (prognosis != null) {
				add(getPrognosisInformation(prognosis));
			}
		}
	}
	
	protected Table getPrognosisInformation(ChildCarePrognosis prognosis) throws RemoteException {
		Table table = new Table(2, 6);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(2, 12);
		table.setHeight(5, 12);
		
		table.mergeCells(1, 1, 2, 1);
		table.add(getSmallHeader(localize("child_care.provider_prognosis", "Prognosis for provider")+":"), 1, 1);

		table.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setAlignment(1, 4, Table.HORIZONTAL_ALIGN_RIGHT);
		table.add(getSmallText(localize("child_care.3_months", "3 months")+":"+Text.NON_BREAKING_SPACE), 1, 3);		
		table.add(getSmallText(localize("child_care.12_months", "12 months")+":"+Text.NON_BREAKING_SPACE), 1, 4);
		table.add(getSmallHeader(String.valueOf(prognosis.getThreeMonthsPrognosis())), 2, 3);	
		table.add(getSmallHeader(String.valueOf(prognosis.getOneYearPrognosis())), 2, 4);
		
		if (prognosis.getThreeMonthsPriority() != -1 && prognosis.getOneYearPriority() != -1) {
			table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.there_of", "there of") + Text.NON_BREAKING_SPACE), 2, 3);
			table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.there_of", "there of") + Text.NON_BREAKING_SPACE), 2, 4);
			table.add(getSmallHeader(String.valueOf(prognosis.getThreeMonthsPriority())), 2, 3);
			table.add(getSmallHeader(String.valueOf(prognosis.getOneYearPriority())), 2, 4);
			table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.with_priority", "with priority")), 2, 3);
			table.add(getSmallText(Text.NON_BREAKING_SPACE + localize("child_care.with_priority", "with priority")), 2, 4);
		}
		
		table.mergeCells(1, 6, 2, 6);
		table.add(getSmallText(localize("child_care.total_in_queue", "Total in queue") + ":" + Text.NON_BREAKING_SPACE), 1, 6);
		table.add(getSmallHeader(String.valueOf(getBusiness().getQueueTotalByProvider(providerID))), 1, 6);
		
		return table;
	}
	
	private void parse(IWContext iwc) {
		try {
			if (iwc.isParameterSet(getSchoolContentBusiness(iwc).getParameterSchoolId()))
				providerID = Integer.parseInt(iwc.getParameter(getSchoolContentBusiness(iwc).getParameterSchoolId()));
			else
				providerID = getSession().getChildCareID();
		}
		catch (RemoteException e) {
			e.printStackTrace();
			providerID = -1;
		}
	}

	protected SchoolContentBusiness getSchoolContentBusiness(IWUserContext iwac) throws RemoteException{
		return (SchoolContentBusiness) IBOLookup.getSessionInstance( iwac, SchoolContentBusiness.class);
	}
}