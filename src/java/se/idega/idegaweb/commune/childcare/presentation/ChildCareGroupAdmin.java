/*
 * Created on 20.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.data.Address;
import com.idega.core.data.Phone;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareGroupAdmin extends ChildCareBlock {

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		Table table = new Table(1,5);
		table.setWidth(getWidth());
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setHeight(2, 12);
		table.setHeight(4, 12);
		add(table);
		
		table.add(getNavigationTable(), 1, 1);
		table.add(getChildrenTable(iwc), 1, 3);
		
		String localized = "";
		if (getSession().getGroupID() != -1)
			localized = localize("child_care.change_group", "Change group");
		else
			localized = localize("child_care.create_group", "Create group");

		GenericButton createGroup = (GenericButton) getButton(new GenericButton("create_change_group", localized));
		createGroup.setWindowToOpen(ChildCareWindow.class);
		createGroup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_CREATE_GROUP);
		createGroup.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
		table.add(createGroup, 1, 5);
	}

	protected Table getChildrenTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(5);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
			
		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.personal_id","Personal ID"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.address","Address"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.phone","Phone"), column++, row++);
			
		SchoolClassMember student;
		User child;
		Address address;
		Phone phone;
		Link move;

		Collection students = getBusiness().getSchoolBusiness().findStudentsInSchool(getSession().getChildCareID(), getSession().getGroupID());
		Iterator iter = students.iterator();
		while (iter.hasNext()) {
			column = 1;
			student = (SchoolClassMember) iter.next();
			child = student.getStudent();
			address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			phone = getBusiness().getUserBusiness().getChildHomePhone(child);

			move = new Link(getEditIcon(localize("child_care.move_to_another_group", "Move child to another group")));
			move.setWindowToOpen(ChildCareWindow.class);
			move.setParameter(ChildCareAdminWindow.PARAMETER_METHOD, String.valueOf(ChildCareAdminWindow.METHOD_MOVE_TO_GROUP));
			move.addParameter(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
			move.addParameter(ChildCareAdminWindow.PARAMETER_OLD_GROUP, student.getSchoolClassId());
			move.addParameter(ChildCareAdminWindow.PARAMETER_USER_ID, student.getClassMemberId());

			if (row % 2 == 0)
				table.setRowColor(row, getZebraColor1());
			else
				table.setRowColor(row, getZebraColor2());

			table.add(getSmallText(child.getNameLastFirst(true)), column++, row);
			table.add(getSmallText(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())), column++, row);
			if (address != null)
				table.add(getSmallText(address.getStreetAddress()), column, row);
			column++;
			if (phone != null)
				table.add(getSmallText(phone.getNumber()), column, row);
			column++;
			
			table.setWidth(column, row, 12);
			table.add(move, column++, row++);
		}
		
		return table;
	}

	protected Form getNavigationTable() throws RemoteException {
		Form form = new Form();
		form.setEventListener(ChildCareEventListener.class);
		
		Table table = new Table(3,1);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(2, 2);
		form.add(table);

		table.add(getSmallHeader(localize("child_care.group","Group")+":"),1,1);
		table.add(getChildCareGroups(), 3, 1);
		
		return form;
	}
	
	protected DropdownMenu getChildCareGroups() throws RemoteException {
		DropdownMenu menu = getGroups(getSession().getGroupID(), -1);
		menu.addMenuElementFirst("-1", localize("child_care.show_all_groups","Show all"));
		menu.setToSubmit();
		
		return menu;	
	}
		
}