/*
 * Created on 26.11.2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ChildCareAdminApplications extends ChildCareBlock {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table applicationTable = new Table();
		applicationTable.setWidth(Table.HUNDRED_PERCENT);
		applicationTable.setCellpadding(getCellpadding());
		applicationTable.setCellspacing(getCellspacing());
		applicationTable.setColumns(4);
		applicationTable.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
		
		applicationTable.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.address","Address"), column++, row);
		applicationTable.add(getLocalizedSmallHeader("child_care.phone","Phone"), column++, row++);
		
		Collection applications = getApplicationCollection();
		if (applications != null && !applications.isEmpty()) {
			ChildCareApplication application;
			User child;
			Address address;
			Phone phone;
			Link link;
			
			Iterator iter = applications.iterator();
			while (iter.hasNext()) {
				column = 1;
				application = (ChildCareApplication) iter.next();
				child = application.getChild();
				address = getBusiness().getUserBusiness().getUsersMainAddress(child);
				phone = getBusiness().getUserBusiness().getChildHomePhone(child);
				
				if (row % 2 == 0)
					applicationTable.setRowColor(row, getZebraColor1());
				else
					applicationTable.setRowColor(row, getZebraColor2());
				
				link = getSmallLink(child.getNameLastFirst(true));
				link.setEventListener(ChildCareEventListener.class);
				link.setParameter(getSession().getParameterUserID(), String.valueOf(application.getChildId()));
				link.setParameter(getSession().getParameterApplicationID(), application.getPrimaryKey().toString());
				if (getResponsePage() != null)
					link.setPage(getResponsePage());
				
				applicationTable.add(link, column++, row);
				applicationTable.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
				if (address != null)
					applicationTable.add(getSmallText(address.getStreetAddress()), column++, row);
				else
					applicationTable.add(getSmallText("-"), column++, row);
				if (phone != null)
					applicationTable.add(getSmallText(phone.getNumber()), column++, row++);
				else
					applicationTable.add(getSmallText("-"), column++, row++);
			}
			applicationTable.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
			applicationTable.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		}
		
		add(applicationTable);
	}
	
	private Collection getApplicationCollection() throws RemoteException {
		Collection applications = getBusiness().findUnhandledApplicationsNotInCommune();
		return applications;
	}
}