package se.idega.idegaweb.commune.childcare.business;

import is.idega.block.family.business.FamilyLogic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.care.business.Relative;
import se.idega.idegaweb.commune.presentation.CommuneBlock;

import com.idega.block.school.data.SchoolClassMember;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.DownloadWriter;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.PersonalIDFormatter;
import com.idega.util.text.Name;
import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLOutput;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company: idega multimedia
 * 
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class AfterSchoolCareGroupWriter extends DownloadWriter implements
		MediaWritable {

	private MemoryFileBuffer buffer = null;

	private ChildCareBusiness business;

	private CommuneUserBusiness userBusiness;

	private Locale locale;

	private IWResourceBundle iwrb;

	private String schoolName;

	private String groupName;

	public final static String PARAMETER_PROVIDER_ID = "provider_id";

	public final static String PARAMETER_GROUP_ID = "group_id";

	public final static String PARAMETER_SHOW_NOT_YET_ACTIVE = "show_not_yet_active";

	public final static String PARAMETER_TYPE = "print_type";

	private final static String XML_STUDENTS = "Students";

	private final static String XML_STUDENT = "Student";

	private final static String XML_SSN = "PersonalID";

	private final static String XML_NAME = "Name";

	private final static String XML_ADDRESS = "Address";

	private final static String XML_POSTALCODE = "PostalCode";

	private final static String XML_SCHOOL = "School";

	private final static String XML_CLASS = "Class";

	private final static String XML_EMAIL = "Email";

	private final static String XML_PHONE = "Phone";

	private final static String XML_HOME_SCHOOL = "HomeSchool";

	private final static String XML_SEASON = "Season";

	private final static String XML_LANGUAGE = "Language";

	private final static String XML_CHOICES = "Choices";

	private final static String XML_CHOICE = "Choice";

	private final static String XML_NUMBER = "Number";

	private final static String XML_DISTRICT = "District";

	private final static String XML_OTHER_INFO = "OtherInfo";

	private final static String XML_CUSTODIANS = "Custodians";

	private final static String XML_CUSTODIAN = "Custodian";

	private final static String XML_WORKPLACE = "Workplace";

	private final static String XML_WORK_PHONE = "WorkPhone";

	private final static String XML_MOBILE_PHONE = "MobilePhone";

	private final static String XML_RELATION_TO_CHILD = "RelationToChild";

	private final static String XML_CONTACTS = "Contacts";

	private final static String XML_CONTACT = "Contact";

	private final static String XML_DISABILITY = "Disability";

	private final static String XML_DISABILITY_INFO = "DisabilityInfo";

	private final static String XML_ALLERGY = "Allergy";

	private final static String XML_ALLERGY_INFO = "AllergyInfo";

	private final static String XML_LAST_DAY_CARE = "LastDaycare";

	private final static String XML_CAN_CONTACT_LAST_DAY_CARE = "CanContactLastDaycare";

	private final static String XML_CAN_SHOW_IMAGE = "CanShowImage";

	private final static String XML_AFTER_SCHOOL_CARE = "AfterSchoolCare";

	private final static String XML_SCHOOL_NAME = "SchoolName";

	private final static String XML_INFO = "Info";

	private final static String XML_CARE_TIME = "CareTime";

	private final static String XML_DAY = "Day";

	private final static String XML_STAY_UNTIL = "StayUntil";

	private final static String XML_IS_PICKED_UP = "IsPickedUp";

	public AfterSchoolCareGroupWriter() {
	}

	public void init(HttpServletRequest req, IWContext iwc) {
		try {
			locale = iwc.getApplicationSettings().getApplicationLocale();
			business = getChildCareBusiness(iwc);
			userBusiness = getCommuneUserBusiness(iwc);
			iwrb = iwc.getIWMainApplication().getBundle(
					CommuneBlock.IW_BUNDLE_IDENTIFIER)
					.getResourceBundle(locale);

			if (req.getParameter(PARAMETER_PROVIDER_ID) != null
					&& req.getParameter(PARAMETER_GROUP_ID) != null) {
				int groupID = Integer.parseInt(req
						.getParameter(PARAMETER_GROUP_ID));
				int providerID = Integer.parseInt(req
						.getParameter(PARAMETER_PROVIDER_ID));
				boolean showNotYetActive = false;
				boolean hasShowNotYetActive = false;
				if (req.getParameter(PARAMETER_SHOW_NOT_YET_ACTIVE) != null) {
					hasShowNotYetActive = true;
					showNotYetActive = Boolean.valueOf(
							req.getParameter(PARAMETER_SHOW_NOT_YET_ACTIVE))
							.booleanValue();
				}

				IWTimestamp stamp = new IWTimestamp();
				Collection students = null;
				if (hasShowNotYetActive) {
					students = business.getSchoolBusiness()
							.findStudentsInSchoolByDate(providerID, groupID,
									stamp.getDate(), showNotYetActive);
				} else {
					students = business.getSchoolBusiness()
							.findStudentsInSchoolByDate(providerID, groupID,
									stamp.getDate());
				}

				if (groupID != -1) {
					groupName = business.getSchoolBusiness().findSchoolClass(
							new Integer(groupID)).getSchoolClassName();
				}
				schoolName = business.getSchoolBusiness().getSchool(
						new Integer(providerID)).getSchoolName();

				buffer = writeXML(students, iwc);
				setAsDownload(iwc, "after_school_care_class.xml", buffer
						.length());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMimeType() {
		if (buffer != null)
			return buffer.getMimeType();
		return super.getMimeType();
	}

	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		} else
			System.err.println("buffer is null");
	}

	public MemoryFileBuffer writeXML(Collection students, IWContext iwc)
			throws Exception {
		MemoryFileBuffer buffer = new MemoryFileBuffer();
		MemoryOutputStream mos = new MemoryOutputStream(buffer);
		if (!students.isEmpty()) {
			XMLDocument doc = new XMLDocument(new XMLElement(XML_STUDENTS));

			XMLElement root = doc.getRootElement();
			// case_.addContent(XML_ID, "-1");

			User student;
			Address address;
			PostalCode postalCode = null;
			Phone phone;
			SchoolClassMember studentMember;
			Email email;

			Iterator iter = students.iterator();
			while (iter.hasNext()) {
				XMLElement elStudent = new XMLElement(XML_STUDENT);
				root.addContent(elStudent);

				studentMember = (SchoolClassMember) iter.next();
				student = studentMember.getStudent();
				address = userBusiness.getUsersMainAddress(student);
				if (address != null)
					postalCode = address.getPostalCode();
				phone = userBusiness.getChildHomePhone(student);
				email = userBusiness.getEmail(student);

				elStudent.addContent(XML_SSN, PersonalIDFormatter.format(
						student.getPersonalID(), locale));

				Name name = new Name(student.getFirstName(), "", student
						.getLastName());
				elStudent.addContent(XML_NAME, name.getName(locale, true));

				String streetAddress = "";
				if (address != null) {
					streetAddress = address.getStreetAddress();
				}
				elStudent.addContent(XML_ADDRESS, streetAddress);

				String postalAddress = "";
				if (address != null && postalCode != null) {
					postalAddress = postalCode.getPostalAddress();
				}
				elStudent.addContent(XML_POSTALCODE, postalAddress);

				elStudent.addContent(XML_SCHOOL, schoolName);
				elStudent.addContent(XML_CLASS, studentMember.getSchoolClass()
						.getName());

				String emailAddress = "";
				if (email != null) {
					emailAddress = email.getEmailAddress();
				}
				elStudent.addContent(XML_EMAIL, emailAddress);

				String phoneNumber = "";
				if (phone != null) {
					phoneNumber = phone.getNumber();
				}
				elStudent.addContent(XML_PHONE, phoneNumber);

				elStudent.addContent(XML_HOME_SCHOOL, "");
				elStudent.addContent(XML_SEASON, studentMember.getSchoolClass()
						.getSchoolSeason().getName());
				elStudent.addContent(XML_LANGUAGE, "");

				XMLElement elChoices = new XMLElement(XML_CHOICES);
				root.addContent(elChoices);

				Collection custodians = getFamilyLogic(iwc).getCustodiansFor(
						student);
				if (!custodians.isEmpty()) {
					XMLElement elCustodians = new XMLElement(XML_CUSTODIANS);
					elStudent.addContent(elCustodians);
					Iterator it2 = custodians.iterator();
					while (it2.hasNext()) {
						XMLElement elCustodian = new XMLElement(XML_CUSTODIAN);
						elCustodians.addContent(elCustodian);

						User custodian = (User) it2.next();
						address = userBusiness.getUsersMainAddress(custodian);
						if (address != null)
							postalCode = address.getPostalCode();
						phone = userBusiness.getHomePhone(custodian);
						email = userBusiness.getEmail(custodian);
						Phone workPhone = userBusiness
								.getUsersWorkPhone(custodian);
						Phone mobilePhone = userBusiness
								.getUsersMobilePhone(custodian);

						elCustodian.addContent(XML_SSN, PersonalIDFormatter
								.format(custodian.getPersonalID(), locale));

						name = new Name(custodian.getFirstName(), "", custodian
								.getLastName());
						elCustodian.addContent(XML_NAME, name.getName(locale,
								true));

						streetAddress = "";
						if (address != null) {
							streetAddress = address.getStreetAddress();
						}
						elCustodian.addContent(XML_ADDRESS, streetAddress);

						postalAddress = "";
						if (address != null && postalCode != null) {
							postalAddress = postalCode.getPostalAddress();
						}
						elCustodian.addContent(XML_POSTALCODE, postalAddress);

						phoneNumber = "";
						if (phone != null) {
							phoneNumber = phone.getNumber();
						}
						elCustodian.addContent(XML_PHONE, phoneNumber);

						elCustodian.addContent(XML_WORKPLACE, "");

						phoneNumber = "";
						if (workPhone != null) {
							phoneNumber = workPhone.getNumber();
						}
						elCustodian.addContent(XML_WORK_PHONE, phoneNumber);

						phoneNumber = "";
						if (mobilePhone != null) {
							phoneNumber = mobilePhone.getNumber();
						}
						elCustodian.addContent(XML_MOBILE_PHONE, phoneNumber);

						elCustodian.addContent(XML_RELATION_TO_CHILD, getCareBusiness(iwc).getUserRelation(student, custodian));
						
						emailAddress = "";
						if (email != null) {
							emailAddress = email.getEmailAddress();
						}
						elCustodian.addContent(XML_EMAIL, emailAddress);
					}

				}
				
				List contacts = getCareBusiness(iwc).getRelatives(student);
				if (!contacts.isEmpty()) {
					XMLElement elContacts = new XMLElement(XML_CONTACTS);
					elStudent.addContent(elContacts);
					Iterator it2 = contacts.iterator();
					while (it2.hasNext()) {
						XMLElement elContact = new XMLElement(XML_CONTACT);
						elContacts.addContent(elContact);

						Relative contact = (Relative) it2.next();

						elContact.addContent(XML_SSN, "");
						elContact.addContent(XML_NAME, contact.getName());
						elContact.addContent(XML_PHONE, contact.getHomePhone());
						elContact.addContent(XML_MOBILE_PHONE, contact.getMobilePhone());
						elContact.addContent(XML_EMAIL, contact.getEmail());
						elContact.addContent(XML_RELATION_TO_CHILD, contact.getRelation());
					}

				}
 				 
			}

			XMLOutput output = new XMLOutput(" ", true);
			output.setLineSeparator(System.getProperty("line.separator"));
			output.setTextNormalize(true);
			output.setEncoding("UTF-8");
			output.output(doc, mos);
		}

		buffer.setMimeType("application/xml");
		return buffer;
	}

	protected ChildCareBusiness getChildCareBusiness(IWApplicationContext iwc)
			throws RemoteException {
		return (ChildCareBusiness) IBOLookup.getServiceInstance(iwc,
				ChildCareBusiness.class);
	}

	protected CareBusiness getCareBusiness(IWApplicationContext iwc)
			throws RemoteException {
		return (CareBusiness) IBOLookup.getServiceInstance(iwc,
				CareBusiness.class);
	}

	protected FamilyLogic getFamilyLogic(IWApplicationContext iwc)
			throws RemoteException {
		return (FamilyLogic) IBOLookup.getServiceInstance(iwc,
				FamilyLogic.class);
	}

	protected CommuneUserBusiness getCommuneUserBusiness(
			IWApplicationContext iwc) throws RemoteException {
		return (CommuneUserBusiness) IBOLookup.getServiceInstance(iwc,
				CommuneUserBusiness.class);
	}
}