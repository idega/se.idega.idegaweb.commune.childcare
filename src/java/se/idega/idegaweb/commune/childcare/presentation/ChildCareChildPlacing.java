/*
 * Created on 26.5.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContractHome;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClass;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.IDOStoreException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.GenericButton;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class ChildCareChildPlacing extends ChildCareBlock {

	private static final String PRM_RM_CLASS_MEMBER_ID = "ccc_rm_cl_mb";
    private static final String PRM_UPD_RM_DATE = "ccc_upd_cl_mb_rm_date";
    private static final String PRM_UPD_CLASS_MEMBER_ID = "ccc_upd_cl_mb_id";
    
    private boolean allowUnrelatedRemoval = true;
    private boolean allowPreviousTermination = true;

	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		
		if(iwc.isParameterSet(PRM_RM_CLASS_MEMBER_ID)){
			try {
				Integer classMemberID = Integer.valueOf(iwc.getParameter(PRM_RM_CLASS_MEMBER_ID));
				getBusiness().getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(classMemberID).remove();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (EJBException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (RemoveException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		if(iwc.isParameterSet(PRM_UPD_CLASS_MEMBER_ID)&& iwc.isParameterSet(PRM_UPD_RM_DATE)){
		    try {
                Integer classMemberID = Integer.valueOf(iwc.getParameter(PRM_UPD_CLASS_MEMBER_ID));
                IWTimestamp removedDate = new IWTimestamp(iwc.getParameter(PRM_UPD_RM_DATE));
                SchoolClassMember updateMember  = getBusiness().getSchoolBusiness().getSchoolClassMemberHome().findByPrimaryKey(classMemberID);
                updateMember.setRemovedDate(removedDate.getTimestamp());
                updateMember.store();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IDOStoreException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (FinderException e) {
                e.printStackTrace();
            }
		
		}
		
		Table table = new Table(1,5);
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(getWidth());
		table.setHeight(2, 12);
		table.setHeight(4, 12);
		
		GenericButton back = (GenericButton) getStyledInterface(new GenericButton("back",localize("back","Back")));
		if (getResponsePage() != null)
			back.setPageToOpen(getResponsePage());

		if (getSession().getUserID() != -1) {
			table.add(getInformationTable(iwc), 1, 1);
			table.add(getPlacingsTable(iwc), 1, 3);
			table.add(back, 1, 5);
			
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, 1, 12);
				table.setCellpaddingLeft(1, 5, 12);
				table.setCellpaddingRight(1, 1, 12);
				table.setCellpaddingRight(1, 5, 12);
			}
			
		}
		else {
			table.add(getLocalizedHeader("child_care.no_child_or_application_found","No child or application found."), 1, 1);
			table.add(back, 1, 3);
			
			if (useStyleNames()) {
				table.setCellpaddingLeft(1, 1, 12);
				table.setCellpaddingLeft(1, 3, 12);
				table.setCellpaddingRight(1, 1, 12);
				table.setCellpaddingRight(1, 3, 12);
			}
			
		}
		
		add(table);
	}
	
	protected Table getPlacingsTable(IWContext iwc) throws RemoteException {
		Table table = new Table();
		table.setWidth(getWidth());
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(4);
		if (useStyleNames()) {
			table.setRowStyleClass(1, getHeaderRowClass());
		}
		else {
			table.setRowColor(1, getHeaderColor());
		}
		int column = 1;
		int row = 1;
			
		if (useStyleNames()) {
			table.setCellpaddingLeft(1, row, 12);
			table.setCellpaddingRight(table.getColumns(), row, 12);
		}
		table.add(getLocalizedSmallHeader("child_care.provider","Provider"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.group","Group"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.valid_from","Valid from"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.removed","Removed"), column++, row++);
		
		SchoolClassMember member;
		SchoolClassMember previousMember=null;
		SchoolClass group;
		School provider;
		IWTimestamp validFrom;
		IWTimestamp terminated = null;
		
		Collection placings = getBusiness().getSchoolBusiness().findClassMemberInChildCare(getSession().getChildID(), getSession().getChildCareID());
		ChildCareContractHome contractHome = getBusiness().getChildCareContractArchiveHome();
		Iterator iter = placings.iterator();
		
		while (iter.hasNext()) {
			column = 1;
			member = (SchoolClassMember) iter.next();
			group = member.getSchoolClass();
			provider = group.getSchool();
			validFrom = new IWTimestamp(member.getRegisterDate());
			if (member.getRemovedDate() != null)
				terminated = new IWTimestamp(member.getRemovedDate());

			if (useStyleNames()) {
				if (row % 2 == 0) {
					table.setRowStyleClass(row, getDarkRowClass());
				}
				else {
					table.setRowStyleClass(row, getLightRowClass());
				}
				table.setCellpaddingLeft(1, row, 12);
				table.setCellpaddingRight(table.getColumns(), row, 12);
			}
			else {
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());
			}
	
			table.add(getSmallText(provider.getSchoolName()), column++, row);
			table.add(getSmallText(group.getSchoolClassName()), column++, row);
			table.add(getSmallText(validFrom.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			if (member.getRemovedDate() != null)
				table.add(getSmallText(terminated.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row);
			else
				table.add(getSmallText("-"), column++, row);
			
			ChildCareContract contract = null;
			try {
				// test for contract relation, allow deletion of class member if none
				contract = contractHome.findBySchoolClassMember(member);

			} catch (FinderException e) {
			
			}
			
			// Fix link, when a classmember is not related to any contract, it can be deleted
			if(allowUnrelatedRemoval && contract==null){
				Link removeLink = new Link(getDeleteIcon(localize("child_care.tooltip.removed_noncontract_placement","Remove noncontract placement")));
				removeLink.addParameter(PRM_RM_CLASS_MEMBER_ID,member.getPrimaryKey().toString());
				table.add(removeLink,column++,row);
			}
			// Fix link , when old classmember's removed_date has not been set
			if(allowPreviousTermination && previousMember!=null && member.getRemovedDate()==null){
			    IWTimestamp removedDate = new IWTimestamp(previousMember.getRegisterDate());
			    removedDate.addDays(-1);
			    java.text.DateFormat df = java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT,iwc.getCurrentLocale());
			    Link updateLink = new Link(getEditIcon(localize("child_care.tooltip.update_placement_removeddate","Update removed date to one day before next startdate")+" ("+df.format(removedDate.getDate()) +")"));
			    updateLink.addParameter(PRM_UPD_RM_DATE,removedDate.toString());
			    updateLink.addParameter(PRM_UPD_CLASS_MEMBER_ID,member.getPrimaryKey().toString());
			    table.add(updateLink,column++,row);
			}
				
			row++;
			previousMember = member;
			
		}
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);

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
			Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
			table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 3, row);
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

					name = new Name(parent.getFirstName(), parent.getMiddleName(), parent.getLastName());
					table.add(getSmallText(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)), 3, row);
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
    /**
     * @return Returns the allowPreviousTermination.
     */
    public boolean isAllowPreviousTermination() {
        return allowPreviousTermination;
    }
    /**
     * @param allowPreviousTermination The allowPreviousTermination to set.
     */
    public void setAllowPreviousTermination(boolean allowPreviousTermination) {
        this.allowPreviousTermination = allowPreviousTermination;
    }
    /**
     * @return Returns the allowUnrelatedRemoval.
     */
    public boolean isAllowUnrelatedRemoval() {
        return allowUnrelatedRemoval;
    }
    /**
     * @param allowUnrelatedRemoval The allowUnrelatedRemoval to set.
     */
    public void setAllowUnrelatedRemoval(boolean allowUnrelatedRemoval) {
        this.allowUnrelatedRemoval = allowUnrelatedRemoval;
    }
}
