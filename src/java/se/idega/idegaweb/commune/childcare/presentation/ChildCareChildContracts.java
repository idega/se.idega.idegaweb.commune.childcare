/*
 * Created on 27.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareContractArchive;

import com.idega.block.school.data.School;
import com.idega.core.data.Address;
import com.idega.core.data.Email;
import com.idega.core.data.Phone;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareChildContracts extends ChildCareBlock {

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table table = new Table(1,5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setHeight(2, 12);
		table.setHeight(4, 12);
		
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("back","Back")));
		back.setPageToOpen(getResponsePage());

		if (getSession().getUserID() != -1) {
			table.add(getInformationTable(iwc), 1, 1);
			table.add(getContractsTable(iwc), 1, 3);
			table.add(back, 1, 5);
		}
		else {
			table.add(getLocalizedHeader("child_care.no_child_or_application_found","No child or application found."), 1, 1);
			table.add(back, 1, 3);
		}
		
		add(table);
	}
	
	protected Table getContractsTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(6);
		table.setRowColor(1, getHeaderColor());
		int column = 1;
		int row = 1;
			
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.created","Created"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.terminated","Terminated"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.care_time","Care time"), column++, row++);
		
		ChildCareContractArchive contract;
		School provider;
		IWTimestamp created;
		IWTimestamp validFrom;
		IWTimestamp terminated = null;
		Link viewContract;
		String careTime;
		
		Collection contracts = null;
		if (getSession().getApplicationID() != -1)
			contracts = getBusiness().getContractsByApplication(getSession().getApplicationID());
		else
			contracts = getBusiness().getContractsByChild(getSession().getUserID());

		Iterator iter = contracts.iterator();
		while (iter.hasNext()) {
			column = 1;
			contract = (ChildCareContractArchive) iter.next();
			provider = contract.getApplication().getProvider();
			created = new IWTimestamp(contract.getCreatedDate());
			validFrom = new IWTimestamp(contract.getValidFromDate());
			if (contract.getTerminatedDate() != null)
				terminated = new IWTimestamp(contract.getTerminatedDate());
			careTime = String.valueOf(contract.getCareTime());
			if (contract.getCareTime() == -1)
				careTime = "-";
					
			viewContract = new Link(getPDFIcon(localize("child_care.view_contract","View contract")));
			viewContract.setFile(contract.getContractFileID());
			viewContract.setTarget(Link.TARGET_NEW_WINDOW);
					
			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());
	
			table.add(getSmallText(provider.getSchoolName()), column++, row);
			table.add(getSmallText(created.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			if (contract.getTerminatedDate() != null)
				table.add(getSmallText(terminated.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			else
				table.add(getSmallText("-"), column++, row);
			table.add(getSmallText(careTime), column++, row);
					
			table.setWidth(column, row, 12);
			table.add(viewContract, column++, row++);
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);

		return table;
	}

	protected Table getInformationTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setColumns(3);
		table.setWidth(1, "100");
		table.setWidth(2, "6");
		int row = 1;
		
		User child = getBusiness().getUserBusiness().getUser(getSession().getChildID());
		if (child != null) {
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			Collection parents = getBusiness().getUserBusiness().getParentsForChild(child);
			
			table.add(getLocalizedSmallHeader("child_care.child","Child"), 1, row);
			table.add(getSmallText(child.getNameLastFirst(true)), 3, row);
			table.add(getSmallText(" - "), 3, row);
			table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
			if (address != null) {
				table.add(getLocalizedSmallHeader("child_care.address","Address"), 1, row);
				table.add(getSmallText(address.getStreetAddress()), 3, row);
				if (address.getPostalAddress() != null)
					table.add(getSmallText(", "+address.getPostalAddress()), 3, row);
				row++;
			}
			
			table.setHeight(row++, 12);
			
			if (parents != null) {
				table.add(getLocalizedSmallHeader("child_care.parents","Parents"), 1, row);
				Phone phone;
				Email email;

				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					address = getBusiness().getUserBusiness().getUsersMainAddress(parent);
					email = getBusiness().getUserBusiness().getEmail(parent);
					phone = getBusiness().getUserBusiness().getHomePhone(parent);

					table.add(getSmallText(parent.getNameLastFirst(true)), 3, row);
					table.add(getSmallText(" - "), 3, row);
					table.add(getSmallText(PersonalIDFormatter.format(parent.getPersonalID(), iwc.getCurrentLocale())), 3, row++);
			
					if (address != null) {
						table.add(getSmallText(address.getStreetAddress()), 3, row);
						if (address.getPostalAddress() != null)
							table.add(getSmallText(", "+address.getPostalAddress()), 3, row);
						row++;
					}
					if (phone != null && phone.getNumber() != null) {
						table.add(getSmallText(localize("child_care.phone","Phone")+": "), 3, row);
						table.add(getSmallText(phone.getNumber()), 3, row++);
					}
					if (email != null && email.getEmailAddress() != null) {
						Link link = getSmallLink(email.getEmailAddress());
						link.setURL("mailto:"+email.getEmailAddress(), false, false);
						table.add(link, 3, row++);
					}
			
					table.setHeight(row++, 12);
				}
			}
		}
		
		return table;
	}
}