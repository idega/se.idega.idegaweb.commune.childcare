/*
 * Created on 22.3.2004
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;

/**
 * @author laddi
 */
public class ChildCareReportBusinessBean extends IBOSessionBean implements ChildCareReportBusiness {

	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";

	private IWBundle _iwb;
	private IWResourceBundle _iwrb;

	private void initializeBundlesIfNeeded() {
		if (_iwb == null) {
			_iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		}
		_iwrb = _iwb.getResourceBundle(this.getUserContext().getCurrentLocale());
	}

	public ReportableCollection getChildCareReport(Integer numberOfWeeks, Integer numberOfMonths, Object areaID, Boolean firstHandOnly) {
		initializeBundlesIfNeeded();
		Locale currentLocale = this.getUserContext().getCurrentLocale();
		List childrenList = new ArrayList();
		
		ReportableCollection reportCollection = new ReportableCollection();

		ReportableField personalID = new ReportableField(FIELD_PERSONAL_ID, String.class);
		personalID.setLocalizedName(getLocalizedString(FIELD_PERSONAL_ID, "Personal ID"), currentLocale);
		reportCollection.addField(personalID);

		ReportableField name = new ReportableField(FIELD_NAME, String.class);
		name.setLocalizedName(getLocalizedString(FIELD_NAME, "Name"), currentLocale);
		reportCollection.addField(name);

		ReportableField address = new ReportableField(FIELD_ADDRESS, String.class);
		address.setLocalizedName(getLocalizedString(FIELD_ADDRESS, "Address"), currentLocale);
		reportCollection.addField(address);

		ReportableField zipCode = new ReportableField(FIELD_ZIP_CODE, String.class);
		zipCode.setLocalizedName(getLocalizedString(FIELD_ZIP_CODE, "Zip code"), currentLocale);
		reportCollection.addField(zipCode);

		ReportableField area = new ReportableField(FIELD_AREA, String.class);
		area.setLocalizedName(getLocalizedString(FIELD_AREA, "Area"), currentLocale);
		reportCollection.addField(area);

		ReportableField email = new ReportableField(FIELD_EMAIL, String.class);
		email.setLocalizedName(getLocalizedString(FIELD_EMAIL, "E-mail"), currentLocale);
		reportCollection.addField(email);

		ReportableField phone = new ReportableField(FIELD_PHONE, String.class);
		phone.setLocalizedName(getLocalizedString(FIELD_PHONE, "Phone"), currentLocale);
		reportCollection.addField(phone);

		ReportableField providers = new ReportableField(FIELD_PROVIDER, String.class);
		providers.setLocalizedName(getLocalizedString(FIELD_PROVIDER, "Provider"), currentLocale);
		reportCollection.addField(providers);

		ReportableField status = new ReportableField(FIELD_STATUS, String.class);
		status.setLocalizedName(getLocalizedString(FIELD_STATUS, "Status"), currentLocale);
		reportCollection.addField(status);

		ReportableField queueDate = new ReportableField(FIELD_QUEUE_DATE, String.class);
		queueDate.setLocalizedName(getLocalizedString(FIELD_QUEUE_DATE, "Queue Date"), currentLocale);
		reportCollection.addField(queueDate);

		ReportableField placementDate = new ReportableField(FIELD_PLACEMENT_DATE, String.class);
		placementDate.setLocalizedName(getLocalizedString(FIELD_PLACEMENT_DATE, "Placement date"), currentLocale);
		reportCollection.addField(placementDate);

		try {
			Collection children = getChildCareBusiness().findSentInAndRejectedApplicationsByArea(areaID, numberOfMonths.intValue(), numberOfWeeks.intValue(), firstHandOnly.booleanValue(), getChildCareBusiness().getChildCareCaseCode());
			if (children != null) {
				Iterator iter = children.iterator();
				while (iter.hasNext()) {
					ChildCareApplication application = (ChildCareApplication) iter.next();
					/*if (getChildCareBusiness().hasActiveApplications(application.getChildId(), getChildCareBusiness().getChildCareCaseCode(), new IWTimestamp().getDate())) {
						continue;
					}*/
					School provider = application.getProvider();
					IWTimestamp queue = new IWTimestamp(application.getQueueDate());
					IWTimestamp placement = new IWTimestamp(application.getFromDate());
					boolean addRejected = false;

					ReportableData data = new ReportableData();
					if (!childrenList.contains(new Integer(application.getChildId()))) {
						addRejected = true;
						User user = application.getChild();
						Address homeAddress = getUserBusiness().getUsersMainAddress(user);
						Phone homePhone = getUserBusiness().getChildHomePhone(user);
						User parent = getUserBusiness().getCustodianForChild(user);
						Email mail = null;
						if (parent != null) {
							mail = getUserBusiness().getEmail(parent);
						}

						data.addData(personalID, PersonalIDFormatter.format(user.getPersonalID(), currentLocale));
						data.addData(name, user.getNameLastFirst(true));

						if (homeAddress != null) {
							data.addData(address, homeAddress.getStreetAddress());
							PostalCode code = homeAddress.getPostalCode();
							
							if (code != null) {
								data.addData(zipCode, code.getPostalCode());
								data.addData(area, code.getName());
							}
						}
						if (mail != null) {
								data.addData(email, mail.getEmailAddress());
						}
						if (homePhone != null) {
								data.addData(phone, homePhone.getNumber());
						}
						childrenList.add(new Integer(application.getChildId()));
					}
					else {
						addRejected = false;
						data.addData(personalID, "");
						data.addData(name, "");
						data.addData(address, "");
						data.addData(zipCode, "");
						data.addData(area, "");
						data.addData(email, "");
						data.addData(phone, "");
					}
					
					data.addData(providers, provider.getSchoolName());
					data.addData(status, getChildCareBusiness().getStatusString(application.getApplicationStatus()));
					data.addData(queueDate, queue.getLocaleDate(currentLocale, IWTimestamp.SHORT));
					data.addData(placementDate, placement.getLocaleDate(currentLocale, IWTimestamp.SHORT));
					reportCollection.add(data);
					
					if (addRejected) {
						try {
							Collection rejected = getChildCareBusiness().findRejectedApplicationsByChild(application.getChildId());
							Iterator iterator = rejected.iterator();
							while (iterator.hasNext()) {
								ChildCareApplication rejectedApplication = (ChildCareApplication) iterator.next();
								provider = rejectedApplication.getProvider();
								queue = new IWTimestamp(rejectedApplication.getQueueDate());
								placement = new IWTimestamp(rejectedApplication.getFromDate());

								data = new ReportableData();
								data.addData(providers, provider.getSchoolName());
								data.addData(status, getChildCareBusiness().getStatusString(rejectedApplication.getApplicationStatus()));
								data.addData(queueDate, queue.getLocaleDate(currentLocale, IWTimestamp.SHORT));
								data.addData(placementDate, placement.getLocaleDate(currentLocale, IWTimestamp.SHORT));
								reportCollection.add(data);
							}
						}
						catch (FinderException fex) {
							//Nothing found...
						}
					}
				}
			}
		}
		catch (FinderException fe) {
			log(fe);
		}
		catch (RemoteException re) {
			log(re);
		}

		return reportCollection;
	}

	private ChildCareBusiness getChildCareBusiness() {
		try {
			return (ChildCareBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), ChildCareBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private String getLocalizedString(String key, String defaultValue) {
		return _iwrb.getLocalizedString(PREFIX + key, defaultValue);
	}
}