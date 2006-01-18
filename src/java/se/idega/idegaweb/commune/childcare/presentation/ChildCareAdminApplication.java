package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.event.ChildCareEventListener;
import com.idega.block.school.data.School;
import com.idega.core.builder.data.ICPage;
import com.idega.core.location.data.Address;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table2;
import com.idega.presentation.TableCell2;
import com.idega.presentation.TableRow;
import com.idega.presentation.TableRowGroup;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;

/**
 * @author laddi
 */
public class ChildCareAdminApplication extends ChildCareBlock {

	private boolean showParentsAgree = false;
	private boolean showRecreateContract = false;

	private static final String PARAMETER_COMMENTS = "cc_comments";
	private static final String PARAMETER_CREATE_CONTRACT = "cc_create_contract";
	
	private static final String ACTION_CREATE_REGULAR_CONTRACT = "cc_create_regular_contract";
	private static final String ACTION_CREATE_BANKID_CONTRACT = "cc_create_bankid_contract";
	
	private User child;
	private ChildCareApplication application;
	private boolean isAdministrator;
	private ICPage contractsPage;
	public static ICPage ccOverviewPage;
	public static ICPage ascOverviewPage;
	private Boolean _canEdit;
    
    private static final int ORDER_BY_QUEUE_DATE = 1;    // see ChildCareApplicationBMPBean ORDER_BY_QUEUE_DATE
    private static final int ORDER_BY_DATE_OF_BIRTH = 2; // see ChildCareApplicationBMPBean ORDER_BY_DATE_OF_BIRTH
	
	//private boolean _useSubmitConfirm;
	
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		parse(iwc);
		isAdministrator = isCommuneAdministrator(iwc);
		if (_canEdit != null)
			isAdministrator = !_canEdit.booleanValue();

		Form form = new Form();
		form.add(new HiddenInput(PARAMETER_CREATE_CONTRACT, ""));
		
		if (getSession().getChildID() != -1) {
			form.add(getInformation(iwc));
			form.add(getApplicationTable(iwc));
			form.add(getLegend());
			form.add(getButtons(iwc, !isAdministrator));
		}
		else {
			form.add(new Text(localize("child_care.no_child_or_application_found","No child or application found.")));
			form.add(getButtons(iwc, false));
		}
		
		add(form);
	}

	private Lists getLegend() {
		Lists list = new Lists();
		list.setStyleClass("legend");
		
		ListItem item = new ListItem();
		item.setStyleClass("accepted");
		item.add(new Text(localize("child_care.application_status_accepted", "Accepted")));
		list.add(item);
		
		item = new ListItem();
		item.setStyleClass("parentAccepted");
		item.add(new Text(localize("child_care.application_status_parents_accepted", "Parents accepted")));
		list.add(item);

		item = new ListItem();
		item.setStyleClass("contract");
		item.add(new Text(localize("child_care.application_status_contract", "Contract")));
		list.add(item);
		
		return list;
	}

	protected Layer getInformation(IWContext iwc) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("formSection");
		
		child = getBusiness().getUserBusiness().getUser(getSession().getChildID());
		if (child != null) {
			Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
			Address address = getBusiness().getUserBusiness().getUsersMainAddress(child);
			
			Layer formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			Label label = new Label();
			label.add(new Text(localize("child_care.child", "Child")));
			Layer span = new Layer(Layer.SPAN);
			span.add(new Text(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true)));
			formItem.add(label);
			formItem.add(span);
			layer.add(formItem);

			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(localize("child_care.personal_id", "Personal ID")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(PersonalIDFormatter.format(child.getPersonalID(), iwc.getCurrentLocale())));
			formItem.add(label);
			formItem.add(span);
			layer.add(formItem);

			if (address != null) {
				formItem = new Layer(Layer.DIV);
				formItem.setStyleClass("formItem");
				label = new Label();
				label.add(new Text(localize("child_care.address", "Address")));
				span = new Layer(Layer.SPAN);
				span.add(new Text(address.getStreetAddress()));
				formItem.add(label);
				formItem.add(span);
				layer.add(formItem);

				if (address.getPostalAddress() != null) {
					formItem = new Layer(Layer.DIV);
					formItem.setStyleClass("formItem");
					label = new Label();
					label.add(new Text(localize("child_care.postal", "Postal code")));
					span = new Layer(Layer.SPAN);
					span.add(new Text(address.getPostalAddress()));
					formItem.add(label);
					formItem.add(span);
					layer.add(formItem);
				}
			}
			
			/*Collection parents = getBusiness().getUserBusiness().getParentsForChild(child);
			if (parents != null) {
				table.add(getLocalizedSmallHeader("child_care.parents","Parents"), 1, row);
				Phone phone = null;
				Phone phoneMobile = null;
				Phone phoneWork = null;
				Email email= null;

				Iterator iter = parents.iterator();
				while (iter.hasNext()) {
					User parent = (User) iter.next();
					address = getBusiness().getUserBusiness().getUsersMainAddress(parent);
					email = getBusiness().getUserBusiness().getEmail(parent);
					phone = getBusiness().getUserBusiness().getHomePhone(parent);
					try {
						phoneMobile = getBusiness().getUserBusiness().getUsersMobilePhone(parent);	
					}
					catch (NoPhoneFoundException e){
						log(e);
					}
					
					try{
						phoneWork = getBusiness().getUserBusiness().getUsersWorkPhone(parent);
					}
					catch (NoPhoneFoundException e){
						log(e);
					}
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
					if (phoneMobile != null && phoneMobile.getNumber() != null) {
						table.add(new Break(), 2, row);
						table.add(getSmallText(localize("school.phone_mobile", "Mobile phone") + ": "), 3, row);
						table.add(getSmallText(phoneMobile.getNumber()), 3, row++);
					}
					if (phoneWork != null && phoneWork.getNumber() != null) {
						table.add(new Break(), 2, row);
						table.add(getSmallText(localize("school.phone_work", "Work phone") + ": "), 3, row);
						table.add(getSmallText(phoneWork.getNumber()), 3, row++);
					}
					
					if (email != null && email.getEmailAddress() != null) {
						Link link = getSmallLink(email.getEmailAddress());
						link.setURL("mailto:"+email.getEmailAddress(), false, false);
						table.add(link, 3, row++);
					}
					
					phone = null;
					phoneMobile = null;
					phoneWork = null;
					email= null;
					
					table.setHeight(row++, 12);
				}
			}*/
		 
			School provider = getBusiness().getCurrentProviderByPlacement(getSession().getChildID());
			String school = null;
			if (provider != null){
				school = provider.getName();				
			}
			else {
				school = localize("child_care.no_current_provider", "No current provider");
			}
			
			formItem = new Layer(Layer.DIV);
			formItem.setStyleClass("formItem");
			label = new Label();
			label.add(new Text(localize("child_care.current_provider", "Current provider")));
			span = new Layer(Layer.SPAN);
			span.add(new Text(school));
			formItem.add(label);
			formItem.add(span);
			layer.add(formItem);
			
			if (getSession().getApplicationID() != -1 && !isAdministrator) {
				application = getBusiness().getApplication(getSession().getApplicationID());
				if (application != null) {
					if (application.getMessage() != null) {
						formItem = new Layer(Layer.DIV);
						formItem.setStyleClass("formItem");
						label = new Label();
						label.add(new Text(localize("child_care.message", "Message")));
						span = new Layer(Layer.SPAN);
						span.add(new Text(application.getMessage()));
						formItem.add(label);
						formItem.add(span);
						layer.add(formItem);
					}
					
					if (application.getApplicationStatus() == getBusiness().getStatusAccepted()) {
						IWTimestamp validUntil = new IWTimestamp(application.getOfferValidUntil());

						formItem = new Layer(Layer.DIV);
						formItem.setStyleClass("formItem");
						label = new Label();
						label.add(new Text(localize("child_care.reply_date", "Reply date")));
						span = new Layer(Layer.SPAN);
						span.add(new Text(validUntil.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));
						formItem.add(label);
						formItem.add(span);
						layer.add(formItem);
					}
				
					TextArea comments = (TextArea) getStyledInterface(new TextArea(PARAMETER_COMMENTS));
					comments.setStyleClass("textarea");
					if (application.getPresentation() != null) {
						comments.setContent(application.getPresentation());
					}
					SubmitButton saveComments = new SubmitButton(localize("child_care.save_comments","Save comments"));
					saveComments.setStyleClass("indentedButton");

					formItem = new Layer(Layer.DIV);
					formItem.setStyleClass("formItem");
					label = new Label(localize("child_care.comments", "Comments"), comments);
					formItem.add(label);
					formItem.add(comments);
					layer.add(formItem);
				}
			}
		}
		
		Layer clearLayer = new Layer(Layer.DIV);
		clearLayer.setStyleClass("Clear");
		layer.add(clearLayer);
		
		return layer;
	}
	
	protected Table2 getApplicationTable(IWContext iwc) throws RemoteException {
		Table2 table = new Table2();
		table.setStyleClass("adminTable");
		table.setStyleClass("ruler");
		table.setWidth("100%");
		table.setCellpadding(0);
		table.setCellspacing(0);
		
		TableRowGroup group = table.createHeaderRowGroup();
		TableRow row = group.createRow();
		TableCell2 cell = row.createHeaderCell();
		cell.setStyleClass("firstColumn");
		cell.setStyleClass("provider");
		cell.add(new Text(localize("child_care.provider","Provider")));

		cell = row.createHeaderCell();
		cell.setStyleClass("status");
		cell.add(new Text(localize("child_care.status","Status")));

		cell = row.createHeaderCell();
		cell.setStyleClass("phone");
		cell.add(new Text(localize("child_care.phone","Phone")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("queueDate");
		cell.add(new Text(localize("child_care.queue_date","Queue date")));
		
		cell = row.createHeaderCell();
		cell.setStyleClass("lastColumn");
		cell.setStyleClass("placementDate");
		cell.add(new Text(localize("child_care.placement_date","Placement date")));
			
		group = table.createBodyRowGroup();
		int iRow = 1;

		ChildCareApplication application;
		String phone;
		IWTimestamp queueDate;
		IWTimestamp placementDate;
		boolean isCurrentProvider = false;
		Link archive;

		Collection applications = getBusiness().getApplicationsForChild(child, getSession().getCaseCode());
		Iterator iter = applications.iterator();
		while (iter.hasNext()) {
			application = (ChildCareApplication) iter.next();
			phone = getBusiness().getSchoolBusiness().getSchoolPhone(application.getProviderId());
			queueDate = new IWTimestamp(application.getQueueDate());
			placementDate = new IWTimestamp(application.getFromDate());
			if (application.getProviderId() == getSession().getChildCareID())
				isCurrentProvider = true;
			else
				isCurrentProvider = false;
				
			if (iRow % 2 == 0) {
				row.setStyleClass("evenRow");
			}
			else {
				row.setStyleClass("oddRow");
			}
			if (application.getApplicationStatus() == getBusiness().getStatusAccepted()) {
				row.setStyleClass("accepted");
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusParentsAccept()) {
				row.setStyleClass("parentAccepted");
			}
			else if (application.getApplicationStatus() == getBusiness().getStatusContract()) {
				row.setStyleClass("contract");
			}
			if (isCurrentProvider) {
				row.setStyleClass("currentProvider");
			}

			cell = row.createCell();
			cell.setStyleClass("firstColumn");
			cell.setStyleClass("provider");
			if (contractsPage != null) {
				Name name = new Name(child.getFirstName(), child.getMiddleName(), child.getLastName());
				archive = new Link(name.getName(iwc.getApplicationSettings().getDefaultLocale(), true));
				archive.setEventListener(ChildCareEventListener.class);
				archive.addParameter(getSession().getParameterUserID(), application.getChildId());
				archive.addParameter(getSession().getParameterApplicationID(), ((Integer)application.getPrimaryKey()).intValue());
				archive.setPage(getResponsePage());
				cell.add(archive);
			}
			else {
				cell.add(new Text(application.getProvider().getSchoolName()));
			}
			
			cell = row.createCell();
			cell.setStyleClass("status");
			cell.add(new Text(getStatusString(application)));
			
			cell = row.createCell();
			cell.setStyleClass("phone");
			if (phone != null) {
				cell.add(new Text(phone));
			}
			else {
				cell.add(new Text("-"));
			}
			
			cell = row.createCell();
			cell.setStyleClass("queueDate");
			cell.add(getText(queueDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));
			
			cell = row.createCell();
			cell.setStyleClass("lastColumn");
			cell.setStyleClass("placementDate");
			cell.add(new Text(placementDate.getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)));
		}
		
		return table;
	}
	
	protected Layer getButtons(IWContext iwc, boolean showAllButtons) throws RemoteException {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("buttonLayer");
		
		String dateWarning = null;
		List logicMessages = new ArrayList();
		
		GenericButton back = new GenericButton("back",localize("back","Back"));
		back.setPageToOpen(getResponsePage());
		layer.add(back);
		
		if (showAllButtons) {
			int numberInQueue = 1;
			boolean hasPriority = application.getHasPriority();
			boolean isAfterSchoolApplication = getBusiness().isAfterSchoolApplication(application);
			if (!isAfterSchoolApplication) {
                int orderBy = ORDER_BY_QUEUE_DATE;
                if (application.getProvider().getSortByBirthdate()) {
                    orderBy = ORDER_BY_DATE_OF_BIRTH;
                }
                numberInQueue = getBusiness().getNumberInQueueByStatus(application, orderBy);
            }		
			
			char status = application.getApplicationStatus();

			boolean hasSchoolPlacement = getBusiness().getSchoolBusiness().hasActivePlacement(application.getChildId(), application.getProviderId(), getBusiness().getSchoolBusiness().getCategoryElementarySchool());
			boolean instantContract = isAfterSchoolApplication && hasSchoolPlacement && (status == getBusiness().getStatusSentIn());

			//if (status == getBusiness().getStatusSentIn() && !instantContract) {
			if (status == getBusiness().getStatusSentIn()) {
				if (numberInQueue == 1 || hasPriority) {
					GenericButton changeDate = getButton("change_date", localize("child_care.change_date","Change date"), ChildCareAdminWindow.METHOD_CHANGE_DATE);
					layer.add(changeDate);

					GenericButton offer = new GenericButton("offer", localize("child_care.offer_placing","Offer placing"));
					if (application.getHasDateSet()) {
						offer.setWindowToOpen(ChildCareWindow.class);
						offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
						offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
						offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_OFFER);
						offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					}
					else{
						offer.setDisabled(true);
						offer.setToolTip(localize("child_care.tooltip.button.offerplacing.date_not_yet_set","Date has not been set"));
						logicMessages.add(localize("child_care.contracting.offer_button_disabled_no_date_has_been_set","Offer button is disabled, application has no date set"));
					}
					layer.add(offer);
				}
				else {
					GenericButton priority = getButton("priority", localize("child_care.grant_priority","Grant priority"), ChildCareAdminWindow.METHOD_GRANT_PRIORITY);
					layer.add(priority);
				}

				if (isAfterSchoolApplication) {
					GenericButton reject = new GenericButton("reject", localize("child_care.reject_application","Reject application"));
					reject.setWindowToOpen(ChildCareWindow.class);
					reject.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
					reject.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
					reject.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_REJECT_APPLICATION);
					reject.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					layer.add(reject);
				}
			}
			else if (status == getBusiness().getStatusAccepted()) {
				if (showParentsAgree) {
					GenericButton parentsAgree = getButton("parents_agree", localize("child_care.parents_agree","Parents agree"), -1);
					parentsAgree.addParameterToWindow(ChildCareAdminWindow.PARAMETER_ACTION, ChildCareAdminWindow.ACTION_PARENTS_AGREE);
					layer.add(parentsAgree);
				}
				
				IWTimestamp dateNow = new IWTimestamp();
				IWTimestamp validUntil = new IWTimestamp(application.getOfferValidUntil());
				if (dateNow.isLaterThanOrEquals(validUntil)) {
					GenericButton offer = new GenericButton("change_offer", localize("child_care.change_offer_date","Change offer date"));
					offer.setWindowToOpen(ChildCareWindow.class);
					offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
					offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
					offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_CHANGE_OFFER);
					offer.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					layer.add(offer);
					
					GenericButton removeFromQueue = new GenericButton("remove_from_queue", localize("child_care.remove_from_queue","Remove from queue"));
					removeFromQueue.setWindowToOpen(ChildCareWindow.class);
					removeFromQueue.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
					removeFromQueue.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
					removeFromQueue.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, ChildCareAdminWindow.METHOD_RETRACT_OFFER);
					removeFromQueue.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					layer.add(removeFromQueue);
				}
			}
			else if (status == getBusiness().getStatusParentsAccept() || instantContract) {
				GenericButton createContract = null;
				GenericButton disabledCreateContract = null;
								
				if (getBusiness().getUserBusiness().hasBankLogin(application.getOwner())) {
					createContract = getButton(new GenericButton("create_contract", localize("child_care.create_contract_for_digital_signing","Create contract for BankID")));
					/*createContract.setValueOnClick(PARAMETER_CREATE_CONTRACT, ACTION_CREATE_BANKID_CONTRACT);
					if (_useSubmitConfirm) {
						createContract.setSingleSubmitConfirm(localize("child_care.confirm_create_contract", "OK to proceed with creating contract?"));
					}*/
					createContract.setWindowToOpen(ChildCareWindow.class);
					createContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
					createContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
					createContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					createContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_ACTION, ChildCareAdminWindow.ACTION_CREATE_CONTRACT_FOR_BANKID);
					createContract.setToDisableOnClick(createContract, true);
					createContract.setOnClick("this.style.display='none';");
							
					disabledCreateContract = getButton(new GenericButton("create_contract", localize("child_care.create_contract_for_digital_signing","Create contract for BankID")));
					disabledCreateContract.setDisabled(true);
					
				}
				else {
					createContract = getButton(new GenericButton("create_contract", localize("child_care.create_contract","Create contract")));
					/*createContract.setValueOnClick(PARAMETER_CREATE_CONTRACT, ACTION_CREATE_REGULAR_CONTRACT);
					if (_useSubmitConfirm) {
						createContract.setSingleSubmitConfirm(localize("child_care.confirm_create_contract", "OK to proceed with creating contract?"));
					}
					form.setToDisableOnSubmit(createContract, true);*/
					createContract.setWindowToOpen(ChildCareWindow.class);
					createContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
					createContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
					createContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
					createContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_ACTION, ChildCareAdminWindow.ACTION_CREATE_CONTRACT);
					createContract.setToDisableOnClick(createContract, true);
					createContract.setOnClick("this.style.display='none';");
					
					disabledCreateContract = (GenericButton) getStyledInterface(new GenericButton("create_contract", localize("child_care.create_contract","Create contract")));
				    disabledCreateContract.setDisabled(true);
				}

				GenericButton changeDate = getButton("change_date", localize("child_care.change_date","Change date"), ChildCareAdminWindow.METHOD_CHANGE_DATE);
					
				if (getBusiness().hasActivePlacementNotWithProvider(getSession().getChildID(), getSession().getChildCareID())) {
				  if (getBusiness().hasFutureActivePlacementsNotWithProvider(getSession().getChildID(), getSession().getChildCareID(), application.getFromDate())) {
						layer.add(changeDate);
						layer.add(createContract);
						dateWarning = localize("child_care.child_has_future_active_contract", "Child has an active future contract");
				  }
				  else {
						disabledCreateContract.setToolTip(localize("child_care.tooltip.button.create_contract.active_placement_at_other_provider","Has an active placement at other provider"));
						layer.add(disabledCreateContract);
						dateWarning = localize("child_care.child_has_active_contract", "Child has an active contract");
				  }
				}
				else {
					if (getBusiness().hasTerminationInFutureNotWithProvider(getSession().getChildID(), getSession().getChildCareID())) {
						
						ChildCareContract archive = getBusiness().getLatestTerminatedContract(getSession().getChildID()); 
						
						if (archive != null) {
							IWTimestamp terminationDate = new IWTimestamp(archive.getTerminatedDate());
														
							IWTimestamp validFrom = new IWTimestamp(application.getFromDate());
							
							if (terminationDate.isLaterThanOrEquals(validFrom)) {
								terminationDate.addDays(1);
								dateWarning = localize("child_care.earliest_possible_placement_date", "Earliest possible placement date") + ": " + terminationDate.getLocaleDate(iwc.getLocale(), IWTimestamp.SHORT);
								changeDate.addParameterToWindow(ChildCareAdminWindow.PARAMETER_EARLIEST_DATE, terminationDate.toString());
								layer.add(changeDate);
								disabledCreateContract.setToolTip(localize("child_care.tooltip.button.create_contract.startdate_is_before_latest_termination","Start date is before latest termination date"));
								layer.add(disabledCreateContract);
								logicMessages.add(localize("child_care.contracting.creation_button_disabled_startdate_is_before_latest_termination","Contract creation button is disabled, start date is before latest termination"));
							}
							else {
								layer.add(changeDate);
								layer.add(createContract);
							}
						}
						else {
							layer.add(changeDate);
							layer.add(createContract);
						}
					}
					else {
						layer.add(changeDate);
						layer.add(createContract);
					}
				}
			}
			else if (status == getBusiness().getStatusContract()) {
				GenericButton viewContract = new GenericButton("view_contract", localize("child_care.view_contract","View contract"));
				viewContract.setFileToOpen(application.getContractFileId());
				layer.add(viewContract);

				if (showRecreateContract) {
					GenericButton recreateContract = getButton("recreate_contract", localize("child_care.recreate_contract", "Recreate contract"), -1);
					recreateContract.addParameterToWindow(ChildCareAdminWindow.PARAMETER_ACTION, ChildCareAdminWindow.ACTION_CREATE_CONTRACT);
					layer.add(recreateContract);
				}
				
				GenericButton placeInGroup = null;

				if (getBusiness().getUserBusiness().hasBankLogin(application.getOwner())) {
					if (! application.getContract().isSigned()) {
						placeInGroup = new GenericButton("place_in_group", localize("child_care.place_in_group","Place in group"));
						placeInGroup.setDisabled(true);
						placeInGroup.setToolTip(localize("child_care.tooltip.button.place_in_group.contract_unsigned","Contract is unsigned"));
						layer.add(placeInGroup);
						logicMessages.add(localize("child_care.contracting.group_placing_button_disabled_unsigned_contract","Place in group button is disabled, contract still not signed by parents"));
					}
					else {
						placeInGroup = getButton("place_in_group", localize("child_care.place_in_group","Place in group"), ChildCareAdminWindow.METHOD_PLACE_IN_GROUP);
						if (getBusiness().hasActivePlacementNotWithProvider(getSession().getChildID(), getSession().getChildCareID()) && !getBusiness().hasFutureActivePlacementsNotWithProvider(getSession().getChildID(), getSession().getChildCareID(), application.getFromDate())){
							//placeInGroup.setDisabled(true);	changed june 2005 - ac
							placeInGroup.setToDisableOnClick(placeInGroup, true);
							dateWarning = localize("child_care.child_has_active_contract", "Child has an active contract");
						}
						placeInGroup.setOnClick("this.style.display='none';");
						
						layer.add(placeInGroup);
					}
				}
				else {
					placeInGroup = getButton("place_in_group", localize("child_care.place_in_group","Place in group"), ChildCareAdminWindow.METHOD_PLACE_IN_GROUP);
					//if (getBusiness().hasActivePlacementNotWithProvider(getSession().getChildID(), getSession().getChildCareID())) {
					if (getBusiness().hasActiveApplication(getSession().getChildID())){
						placeInGroup.setDisabled(true);	
						dateWarning = localize("child_care.child_has_active_contract", "Child has an active contract");
					}
					placeInGroup.setOnClick("this.style.display='none';");
					
					layer.add(placeInGroup);
				}
			}
		}
		
		/*if (dateWarning != null) {
			table.setHeight(2, 6);
			table.mergeCells(1, 3, table.getColumns(), 3);
			table.add(getSmallErrorText(dateWarning), 1, 3);
		}
		if(!logicMessages.isEmpty()){
		    table.setHeight(5,6);
		    int row = 6;
		    for (Iterator iter = logicMessages.iterator(); iter.hasNext();) {
                String msg = (String) iter.next();
                table.add(getSmallText(msg),1,row);
                table.mergeCells(1,row,table.getColumns(),row);
                row++;
            }
		}*/
		
		return layer;
	}
	
	protected GenericButton getButton(String name, String value, int method)  throws RemoteException {
		GenericButton button = new GenericButton(name, value);
		button.setWindowToOpen(ChildCareWindow.class);
		button.addParameterToWindow(ChildCareAdminWindow.PARAMETER_APPLICATION_ID, String.valueOf(getSession().getApplicationID()));
		button.addParameterToWindow(ChildCareAdminWindow.PARAMETER_USER_ID, String.valueOf(getSession().getChildID()));
		button.addParameterToWindow(ChildCareAdminWindow.PARAMETER_METHOD, method);
		button.addParameterToWindow(ChildCareAdminWindow.PARAMETER_PAGE_ID, getParentPageID());
				
		return button;
	}
	
	private void parse(IWContext iwc) throws RemoteException {
		getBusiness().saveComments(getSession().getApplicationID(), iwc.getParameter(PARAMETER_COMMENTS));
		
		if (iwc.isParameterSet(PARAMETER_CREATE_CONTRACT)) {
			if (iwc.getParameter(PARAMETER_CREATE_CONTRACT).equals(ACTION_CREATE_REGULAR_CONTRACT)) {
				getBusiness().assignContractToApplication(getSession().getApplicationID(), -1,null, null, -1, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);
			}
			else if (iwc.getParameter(PARAMETER_CREATE_CONTRACT).equals(ACTION_CREATE_BANKID_CONTRACT)) {
				getBusiness().assignContractToApplication(getSession().getApplicationID(), -1,null, null, -1, iwc.getCurrentUser(), iwc.getCurrentLocale(), true);
			}
		}
	}

	public void setContractsPage(ICPage page) {
		contractsPage = page;
	}

	public void setChildcareOverviewPage(ICPage page) {
		ccOverviewPage = page;
	}
	
	public void setAfterSchoolcareOverviewPage(ICPage page) {
		ascOverviewPage = page;
	}
	
	public void setCanEdit(boolean b) {
		_canEdit = new Boolean(b);
	}

	/**
	 * @param b
	 */
	public void setShowParentsAgree(boolean b) {
		showParentsAgree = b;
	}
	
	/**
	 * @param b
	 */
	public void setShowRecreateContract(boolean b) {
		showRecreateContract = b;
	}

	/**
	 * @param useSubmitConfirm The useSubmitConfirm to set.
	 */
	public void setToUseSubmitConfirm(boolean useSubmitConfirm) {
		if (useSubmitConfirm) {
			log("setToUseSubmitConfirm called in ChildCareAdminApplication...");
		}
	}
	
	
}