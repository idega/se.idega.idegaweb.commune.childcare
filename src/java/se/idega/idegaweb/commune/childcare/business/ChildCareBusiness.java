package se.idega.idegaweb.commune.childcare.business;


import com.idega.block.process.business.CaseBusiness;
import com.idega.core.file.data.ICFile;
import se.idega.idegaweb.commune.childcare.check.business.CheckBusiness;
import com.idega.block.school.data.School;
import com.idega.block.process.data.Case;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.business.PlacementHelper;
import se.idega.idegaweb.commune.care.data.ChildCareApplication;
import com.idega.data.IDORemoveRelationshipException;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosis;
import se.idega.idegaweb.commune.childcare.data.ChildCarePrognosisHome;
import com.idega.idegaweb.IWBundle;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import java.sql.Time;
import se.idega.idegaweb.commune.accounting.regulations.business.EmploymentTypeFinderBusiness;
import javax.ejb.EJBException;
import com.idega.business.IBOService;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import se.idega.idegaweb.commune.childcare.data.ChildCareQueueHome;
import java.io.File;
import com.idega.block.pdf.business.PrintingContext;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import se.idega.idegaweb.commune.care.business.AlreadyCreatedException;
import java.util.Map;
import se.idega.idegaweb.commune.care.data.CareTime;
import com.idega.exception.IWBundleDoesNotExist;
import java.sql.Date;
import com.idega.user.data.User;
import com.idega.block.school.data.SchoolClassMember;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import com.idega.data.IDOAddRelationshipException;
import java.util.Locale;
import java.util.Collection;
import se.idega.idegaweb.commune.accounting.regulations.business.ManagementTypeFinderBusiness;
import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;
import com.idega.block.school.data.SchoolHome;
import com.idega.block.school.business.SchoolBusiness;
import se.idega.idegaweb.commune.message.business.CommuneMessageBusiness;

public interface ChildCareBusiness extends IBOService, CaseBusiness, EmploymentTypeFinderBusiness, ManagementTypeFinderBusiness {

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getBundleIdentifier
	 */
	public String getBundleIdentifier() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getChildCarePrognosisHome
	 */
	public ChildCarePrognosisHome getChildCarePrognosisHome() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getChildCareContractArchiveHome
	 */
	public ChildCareContractHome getChildCareContractArchiveHome() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getChildCareQueueHome
	 */
	public ChildCareQueueHome getChildCareQueueHome() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#usePrognosis
	 */
	public boolean usePrognosis() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#showPriorities
	 */
	public boolean showPriorities() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPrognosis
	 */
	public ChildCarePrognosis getPrognosis(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplication
	 */
	public ChildCareApplication getApplication(int childID, int choiceNumber) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNonActiveApplication
	 */
	public ChildCareApplication getNonActiveApplication(int childID, int choiceNumber) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNewestApplication
	 */
	public ChildCareApplication getNewestApplication(int providerID, Date date) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasApplications
	 */
	public boolean hasApplications(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#updatePrognosis
	 */
	public void updatePrognosis(int providerID, int threeMonthsPrognosis, int oneYearPrognosis, int threeMonthsPriority, int oneYearPriority, int providerCapacity, int vacancies, String providerComments) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#setChildCareQueueExported
	 */
	public void setChildCareQueueExported(ChildCareQueue queue) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#exportQueue
	 */
	public void exportQueue(Collection choices) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getHasUnexportedChoices
	 */
	public boolean getHasUnexportedChoices(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
	 */
	public boolean insertApplications(User user, int[] provider, String date, int checkId, int childId, String subject, String message, boolean freetimeApplication) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
	 */
	public boolean insertApplications(User user, int[] provider, String[] dates, String message, int checkId, int childId, String subject, String body, boolean freetimeApplication) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
	 */
	public boolean insertApplications(User user, int[] provider, String[] dates, String message, int childID, Date[] queueDates, boolean[] hasPriority) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
	 */
	public boolean insertApplications(User user, int[] provider, String[] dates, String message, int checkId, int childId, String subject, String body, boolean freetimeApplication, boolean sendMessages, Date[] queueDates, boolean[] hasPriority) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#insertApplications
	 */
	public boolean insertApplications(User user, int[] provider, String[] dates, String message, Time fromTime, Time toTime, int checkId, int childId, String subject, String body, boolean freetimeApplication, boolean sendMessages, Date[] queueDates, boolean[] hasPriority, String payerName, String payerPersonalID, String cardType, String cardNumber, int validMonth, int validYear) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToOtherParent
	 */
	public void sendMessageToOtherParent(Collection applications) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changePlacingDate
	 */
	public void changePlacingDate(int applicationID, Date placingDate, String preSchool) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changePlacingDate
	 */
	public void changePlacingDate(ChildCareApplication application, Date placingDate, String preSchool) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToProvider
	 */
	public void sendMessageToProvider(ChildCareApplication application, String subject, String message, User sender) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToProvider
	 */
	public void sendMessageToProvider(ChildCareApplication application, String subject, String message) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueChoices
	 */
	public Collection getQueueChoices(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPositionInQueue
	 */
	public int getPositionInQueue(ChildCareQueue queue) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByProviderAndApplicationStatus
	 */
	public Collection getApplicationsByProviderAndApplicationStatus(int providerID, String applicationStatus) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getTerminatedApplicationsByProviderAndApplicationStatus
	 */
	public Collection getTerminatedApplicationsByProviderAndApplicationStatus(int providerID, String applicationStatus) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getInactiveApplicationsByProvider
	 */
	public Collection getInactiveApplicationsByProvider(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
	 */
	public Collection getUnhandledApplicationsByProvider(int providerId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfUnhandledApplicationsByProvider
	 */
	public int getNumberOfUnhandledApplicationsByProvider(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsByProvider
	 */
	public int getNumberOfApplicationsByProvider(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsByProvider
	 */
	public int getNumberOfApplicationsByProvider(int providerID, int sortBy, Date fromDate, Date toDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFirstHandChoicesByProvider
	 */
	public int getNumberOfFirstHandChoicesByProvider(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFirstHandChoicesByProvider
	 */
	public int getNumberOfFirstHandChoicesByProvider(int providerID, Date from, Date to) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFirstHandNettoChoicesByProvider
	 */
	public int getNumberOfFirstHandNettoChoicesByProvider(int providerID, Date from, Date to) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFirstHandBruttoChoicesByProvider
	 */
	public int getNumberOfFirstHandBruttoChoicesByProvider(int providerID, Date from, Date to) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsInQueueBeforeDate
	 */
	public Collection getApplicationsInQueueBeforeDate(int providerID, Date beforeDate) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPendingApplications
	 */
	public Collection getPendingApplications(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPendingApplications
	 */
	public Collection getPendingApplications(int childID, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasPendingApplications
	 */
	public boolean hasPendingApplications(int childID, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveApplications
	 */
	public boolean hasActiveApplications(int childID, String caseCode, Date activeDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removePendingFromQueue
	 */
	public boolean removePendingFromQueue(User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#renewApplication
	 */
	public void renewApplication(int applicationID, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberInQueue
	 */
	public int getNumberInQueue(ChildCareApplication application) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberInQueue
	 */
	public int getNumberInQueue(ChildCareApplication application, int orderBy) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberInQueueByStatus
	 */
	public int getNumberInQueueByStatus(ChildCareApplication application) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberInQueueByStatus
	 */
	public int getNumberInQueueByStatus(ChildCareApplication application, int orderBy) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
	 */
	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
	 */
	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry, int orderBy) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
	 */
	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry, int sortBy, Date fromDate, Date toDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
	 */
	public Collection getUnhandledApplicationsByProvider(int providerId, int numberOfEntries, int startingEntry, int sortBy, Date fromDate, Date toDate, int orderBy) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByChild
	 */
	public Collection getUnhandledApplicationsByChild(int childID, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByChild
	 */
	public Collection getUnhandledApplicationsByChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByChildAndProvider
	 */
	public ChildCareApplication getUnhandledApplicationsByChildAndProvider(int childID, int providerID) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
	 */
	public Collection getUnhandledApplicationsByProvider(School provider) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnhandledApplicationsByProvider
	 */
	public Collection getUnhandledApplicationsByProvider(User provider) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnsignedApplicationsByProvider
	 */
	public Collection getUnsignedApplicationsByProvider(int providerId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnsignedApplicationsByProvider
	 */
	public Collection getUnsignedApplicationsByProvider(School provider) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUnsignedApplicationsByProvider
	 */
	public Collection getUnsignedApplicationsByProvider(User provider) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isAfterSchoolApplication
	 */
	public boolean isAfterSchoolApplication(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#placeApplication
	 */
	public boolean placeApplication(int applicationID, String subject, String body, String childCareTime, int groupID, int schoolTypeID, int employmentTypeID, IWTimestamp terminationDate, User user, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#placeApplication
	 */
	public boolean placeApplication(ChildCareApplication application, String subject, String body, String childCareTime, int groupID, int schoolTypeID, int employmentTypeID, IWTimestamp terminationDate, User user, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#placeApplication
	 */
	public boolean placeApplication(ChildCareApplication application, String subject, String body, File attachment, String childCareTime, int groupID, int schoolTypeID, int employmentTypeID, IWTimestamp terminationDate, User user, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterValidFromDate
	 */
	public void alterValidFromDate(int applicationID, Date newDate, int employmentTypeID, Locale locale, User user) throws RemoteException, NoPlacementFoundException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterValidFromDate
	 */
	public void alterValidFromDate(int applicationID, Date newDate, int employmentTypeID, int schoolTypeID, int schoolClassID, Locale locale, User user) throws RemoteException, NoPlacementFoundException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterValidFromDate
	 */
	public void alterValidFromDate(ChildCareApplication application, Date newDate, int employmentTypeID, Locale locale, User user) throws RemoteException, NoPlacementFoundException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterValidFromDate
	 */
	public void alterValidFromDate(ChildCareApplication application, Date newDate, int employmentTypeID, int schoolTypeID, int schoolClassID, Locale locale, User user) throws RemoteException, NoPlacementFoundException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterContract
	 */
	public boolean alterContract(int childcareContractID, String careTime, Date fromDate, Date endDate, Locale locale, User performer, int employmentType, int invoiceReceiver, int schoolType, int schoolClass) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#alterContract
	 */
	public boolean alterContract(ChildCareContract childcareContract, String careTime, Date fromDate, Date endDate, Locale locale, User performer, int employmentType, int invoiceReceiver, int schoolType, int schoolClass) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#recreateContractFile
	 */
	public ICFile recreateContractFile(ChildCareContract archive, Locale locale) throws IDORemoveRelationshipException, RemoteException, IWBundleDoesNotExist, IDOAddRelationshipException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#moveToGroup
	 */
	public void moveToGroup(int childID, int providerID, int schoolClassID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#moveToGroup
	 */
	public void moveToGroup(int placementID, int schoolClassID, User performer) throws RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFromProvider
	 */
	public void removeFromProvider(int placementID, Timestamp date, boolean parentalLeave, String message, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLatestPlacement
	 */
	public SchoolClassMember getLatestPlacement(int childID, int providerID) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#acceptApplication
	 */
	public boolean acceptApplication(ChildCareApplication application, IWTimestamp validUntil, float fee, String subject, String message, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#retractOffer
	 */
	public boolean retractOffer(int applicationID, String subject, String message, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#retractOffer
	 */
	public boolean retractOffer(ChildCareApplication application, String subject, String message, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#reactivateApplication
	 */
	public boolean reactivateApplication(int applicationID, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#reactivateApplication
	 */
	public boolean reactivateApplication(ChildCareApplication application, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changeApplicationStatus
	 */
	public boolean changeApplicationStatus(int applicationID, char newStatus, User performer) throws IllegalArgumentException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changeApplicationStatus
	 */
	public boolean changeApplicationStatus(ChildCareApplication application, char newStatus, User performer) throws IllegalArgumentException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#createCancelForm
	 */
	public void createCancelForm(ChildCareApplication application, Date cancelDate, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#cancelContract
	 */
	public boolean cancelContract(ChildCareApplication application, boolean parentalLeave, IWTimestamp date, String message, String subject, String body, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#acceptApplication
	 */
	public boolean acceptApplication(int applicationId, IWTimestamp validUntil, float fee, String subject, String message, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectOffer
	 */
	public boolean rejectOffer(int applicationId, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectOffer
	 */
	public boolean rejectOffer(ChildCareApplication application, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectOfferWithNewDate
	 */
	public boolean rejectOfferWithNewDate(int applicationId, User user, Date date) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFromQueue
	 */
	public boolean removeFromQueue(int applicationId, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFromQueue
	 */
	public boolean removeFromQueue(ChildCareApplication application, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getAcceptedApplicationsByChild
	 */
	public ChildCareApplication getAcceptedApplicationsByChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getAcceptedChildCareApplicationByChild
	 */
	public ChildCareApplication getAcceptedChildCareApplicationByChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getAcceptedChildCareOrAfterSchoolCareApplicationByChild
	 */
	public ChildCareApplication getAcceptedChildCareOrAfterSchoolCareApplicationByChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByProvider
	 */
	public Collection getApplicationsByProvider(int providerId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getOpenAndGrantedApplicationsByProvider
	 */
	public Collection getOpenAndGrantedApplicationsByProvider(int providerId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getOpenAndGrantedApplicationsByProvider
	 */
	public Collection getOpenAndGrantedApplicationsByProvider(int providerId, int orderBy) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getAcceptedApplicationsByProvider
	 */
	public Collection getAcceptedApplicationsByProvider(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByProvider
	 */
	public Collection getApplicationsByProvider(School provider) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByProvider
	 */
	public Collection getApplicationsByProvider(User provider) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getRejectedApplicationsByProvider
	 */
	public Collection getRejectedApplicationsByProvider(Integer providerID, String fromDateOfBirth, String toDateOfBirth, String fromDate, String toDate) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#parentsAgree
	 */
	public void parentsAgree(ChildCareApplication application, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#parentsAgree
	 */
	public void parentsAgree(int applicationID, User user, String subject, String message) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#parentsAgree
	 */
	public void parentsAgree(ChildCareApplication application, User user, String subject, String message) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#saveComments
	 */
	public void saveComments(int applicationID, String comment) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#createNewPlacement
	 */
	public SchoolClassMember createNewPlacement(int applicationID, int schooltypeID, int schoolclassID, SchoolClassMember oldStudent, IWTimestamp validFrom, User user) throws RemoteException, EJBException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#createNewPlacement
	 */
	public SchoolClassMember createNewPlacement(ChildCareApplication application, int schooltypeID, int schoolclassID, SchoolClassMember oldStudent, IWTimestamp validFrom, User user) throws RemoteException, EJBException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#createNewPlacement
	 */
	public SchoolClassMember createNewPlacement(Integer childID, Integer schooltypeID, Integer schoolclassID, SchoolClassMember oldStudent, IWTimestamp validFrom, User user) throws RemoteException, EJBException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
	 */
	public ICFile assignContractToApplication(int applicationID, int oldArchiveID, String childCareTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isOnlyGroupChange
	 */
	public boolean isOnlyGroupChange(int applicationId, String careTime, int employmentTypeID, Date validFrom, int schoolTypeId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isGroupChange
	 */
	public boolean isGroupChange(int applicationId, Date validFrom, int schoolClassID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changeGroup
	 */
	public void changeGroup(int applicationId, Date validFrom, int schoolClassId, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
	 */
	public ICFile assignContractToApplication(int applicationID, int archiveID, String childCareTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus, boolean createNewStudent, int schoolTypeId, int schoolClassId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
	 */
	public ICFile assignContractToApplication(ChildCareApplication application, int archiveID, String childCareTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus, boolean createNewStudent, int schoolTypeId, int schoolClassId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
	 */
	public ICFile assignContractToApplication(ChildCareApplication application, int archiveID, String childCareTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus, boolean createNewStudent, int schoolTypeId, int schoolClassId, boolean sendMessages) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
	 */
	public ICFile assignContractToApplication(PrintingContext printingContext, ChildCareApplication application, int archiveID, String childCareTime, Timestamp fromTime, Timestamp toTime, IWTimestamp validFrom, int employmentTypeID, User user, Locale locale, boolean changeStatus, boolean createNewStudent, int schoolTypeId, int schoolClassId, boolean sendMessages, boolean createNewContract) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignContractToApplication
	 */
	public boolean assignContractToApplication(String[] ids, User user, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignApplication
	 */
	public boolean assignApplication(int id, User user, String subject, String body) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#assignApplication
	 */
	public boolean assignApplication(String[] ids, User user, String subject, String body) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getGrantedApplicationsByUser
	 */
	public Collection getGrantedApplicationsByUser(User owner) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsByUser
	 */
	public Collection getApplicationsByUser(User owner) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsForChild
	 */
	public Collection getApplicationsForChild(User child) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsForChild
	 */
	public Collection getApplicationsForChild(User child, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsForChild
	 */
	public Collection getApplicationsForChild(int childId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationsForChild
	 */
	public Collection getApplicationsForChild(int childId, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChildByStatus
	 */
	public int getNumberOfApplicationsForChildByStatus(int childID, String caseStatus) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChildByStatus
	 */
	public int getNumberOfApplicationsForChildByStatus(int childID, String caseStatus, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChild
	 */
	public int getNumberOfApplicationsForChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChildNotInactive
	 */
	public int getNumberOfApplicationsForChildNotInactive(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfApplicationsForChildNotInactive
	 */
	public int getNumberOfApplicationsForChildNotInactive(int childID, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasOutstandingOffers
	 */
	public boolean hasOutstandingOffers(int childID, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationForChildAndProvider
	 */
	public ChildCareApplication getApplicationForChildAndProvider(int childID, int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationForChildAndProviderinStatus
	 */
	public ChildCareApplication getApplicationForChildAndProviderinStatus(int childID, int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationForChildAndProviderinStatus
	 */
	public ChildCareApplication getApplicationForChildAndProviderinStatus(int childID, int providerID, String[] statuses) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findAllGrantedApplications
	 */
	public Collection findAllGrantedApplications() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findAllPendingApplications
	 */
	public Collection findAllPendingApplications() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findAllApplicationsWithChecksToRedeem
	 */
	public Collection findAllApplicationsWithChecksToRedeem() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplicationByPrimaryKey
	 */
	public ChildCareApplication getApplicationByPrimaryKey(String key) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#deleteOffer
	 */
	public void deleteOffer(int applicationID, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#redeemApplication
	 */
	public boolean redeemApplication(String applicationId, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPreSchoolTypes
	 */
	public Collection getPreSchoolTypes() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getFamilyDayCareTypes
	 */
	public Collection getFamilyDayCareTypes() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getFamilyAfterSchoolTypes
	 */
	public Collection getFamilyAfterSchoolTypes() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getXMLContractTxtURL
	 */
	public String getXMLContractTxtURL(IWBundle iwb, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getXMLContractPdfURL
	 */
	public String getXMLContractPdfURL(IWBundle iwb, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getXMLCancelFormPdfURL
	 */
	public String getXMLCancelFormPdfURL(IWBundle iwb, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#setAsPriorityApplication
	 */
	public void setAsPriorityApplication(int applicationID, String message, String body) throws RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#setAsPriorityApplication
	 */
	public void setAsPriorityApplication(ChildCareApplication application, String message, String body) throws RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasBeenPlacedWithOtherProvider
	 */
	public boolean hasBeenPlacedWithOtherProvider(int childID, int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasSchoolPlacement
	 */
	public boolean hasSchoolPlacement(User child) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasSchoolPlacement
	 */
	public boolean hasSchoolPlacement(User child, School school) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getActivePlacement
	 */
	public SchoolClassMember getActivePlacement(User child, School school) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractFile
	 */
	public ChildCareContract getContractFile(int contractFileID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractsByChild
	 */
	public Collection getContractsByChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractsByChildAndProvider
	 */
	public Collection getContractsByChildAndProvider(int childID, int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractsByApplication
	 */
	public Collection getContractsByApplication(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLatestContractsForChild
	 */
	public Collection getLatestContractsForChild(int childID, int maxNumberOfContracts) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLocalizedCaseDescription
	 */
	public String getLocalizedCaseDescription(Case theCase, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getMessageBusiness
	 */
	public CommuneMessageBusiness getMessageBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUserBusiness
	 */
	public CommuneUserBusiness getUserBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCheckBusiness
	 */
	public CheckBusiness getCheckBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getAfterSchoolBusiness
	 */
	public AfterSchoolBusiness getAfterSchoolBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusAccepted
	 */
	public char getStatusAccepted() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusContract
	 */
	public char getStatusContract() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusParentsAccept
	 */
	public char getStatusParentsAccept() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusPriority
	 */
	public char getStatusPriority() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusReady
	 */
	public char getStatusReady() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusSentIn
	 */
	public char getStatusSentIn() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusTimedOut
	 */
	public char getStatusTimedOut() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusWaiting
	 */
	public char getStatusWaiting() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusParentTerminated
	 */
	public char getStatusParentTerminated() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusCancelled
	 */
	public char getStatusCancelled() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusDenied
	 */
	public char getStatusDenied() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusDeleted
	 */
	public char getStatusDeleted() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusMoved
	 */
	public char getStatusMoved() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusRejected
	 */
	public char getStatusRejected() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusNotAnswered
	 */
	public char getStatusNotAnswered() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusNewChoice
	 */
	public char getStatusNewChoice() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalByProvider
	 */
	public int getQueueTotalByProvider(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueByProvider
	 */
	public int getQueueByProvider(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalByProvider
	 */
	public int getQueueTotalByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueByProvider
	 */
	public int getQueueByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalByProviderWithinMonths
	 */
	public int getQueueTotalByProviderWithinMonths(int providerID, int months, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalBeforeUpdate
	 */
	public int getQueueTotalBeforeUpdate(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getBruttoQueueTotalByProvider
	 */
	public int getBruttoQueueTotalByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getBruttoQueueByProvider
	 */
	public int getBruttoQueueByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getBruttoQueueTotalByProviderWithinMonths
	 */
	public int getBruttoQueueTotalByProviderWithinMonths(int providerID, int months, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNettoQueueTotalByProvider
	 */
	public int getNettoQueueTotalByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNettoQueueByProvider
	 */
	public int getNettoQueueByProvider(int providerID, Date from, Date to, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNettoQueueTotalByProviderWithinMonths
	 */
	public int getNettoQueueTotalByProviderWithinMonths(int providerID, int months, boolean isOnlyFirstHand) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueTotalByArea
	 */
	public int getQueueTotalByArea(int areaID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getQueueByArea
	 */
	public int getQueueByArea(int areaID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getOldQueueTotal
	 */
	public int getOldQueueTotal(String[] queueType, boolean exported) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getProviderAreaMap
	 */
	public Map getProviderAreaMap(Collection schoolAreas, Locale locale, String emptyString, boolean isFreetime) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getProviderAreaMap
	 */
	public Map getProviderAreaMap(Collection schoolAreas, School currentSchool, Locale locale, String emptyString, boolean isFreetime) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContractForChild
	 */
	public ChildCareContract getValidContractForChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContractForChild
	 */
	public ChildCareContract getValidContractForChild(int childID, Date validDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContract
	 */
	public ChildCareContract getValidContract(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContract
	 */
	public ChildCareContract getValidContract(int applicationID, Date validDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContractByPlacement
	 */
	public ChildCareContract getValidContractByPlacement(SchoolClassMember member) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContractByPlacement
	 */
	public ChildCareContract getValidContractByPlacement(SchoolClassMember member, Date validDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getActiveApplicationByChild
	 */
	public ChildCareApplication getActiveApplicationByChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveApplication
	 */
	public boolean hasActiveApplication(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveApplication
	 */
	public boolean hasActiveApplication(int childID, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getValidContractByChild
	 */
	public ChildCareContract getValidContractByChild(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFutureContracts
	 */
	public void removeFutureContracts(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeFutureContracts
	 */
	public void removeFutureContracts(int applicationID, Date date) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeLatestFutureContract
	 */
	public void removeLatestFutureContract(int applicationID, Date earliestAllowedRemoveDate, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeContract
	 */
	public boolean removeContract(int childcareContractID, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasContractRelation
	 */
	public boolean hasContractRelation(SchoolClassMember member) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#removeContract
	 */
	public boolean removeContract(ChildCareContract childcareContract, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#addMissingGrantedChecks
	 */
	public void addMissingGrantedChecks() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#convertOldQueue
	 */
	public void convertOldQueue() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActivePlacement
	 */
	public boolean hasActivePlacement(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#canCancelContract
	 */
	public boolean canCancelContract(int applicationID, Date endDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfContractsForApplication
	 */
	public int getNumberOfContractsForApplication(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasFutureContracts
	 */
	public boolean hasFutureContracts(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasFutureContracts
	 */
	public boolean hasFutureContracts(int applicationID, Date date) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveContract
	 */
	public boolean hasActiveContract(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFutureContracts
	 */
	public int getNumberOfFutureContracts(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getNumberOfFutureContracts
	 */
	public int getNumberOfFutureContracts(int applicationID, Date from) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasFutureLogs
	 */
	public boolean hasFutureLogs(int applicationID, Date from) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasUnansweredOffers
	 */
	public boolean hasUnansweredOffers(int childID, String caseCode) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getActivePlacement
	 */
	public ChildCareApplication getActivePlacement(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActiveNotRemovedPlacements
	 */
	public boolean hasActiveNotRemovedPlacements(int childId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasActivePlacementNotWithProvider
	 */
	public boolean hasActivePlacementNotWithProvider(int childID, int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasFutureActivePlacementsNotWithProvider
	 */
	public boolean hasFutureActivePlacementsNotWithProvider(int childID, int providerID, Date date) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getFirstFuturePlacementNotWithProvider
	 */
	public ChildCareApplication getFirstFuturePlacementNotWithProvider(int childID, int providerID, Date date) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasTerminationInFutureNotWithProvider
	 */
	public boolean hasTerminationInFutureNotWithProvider(int childID, int providerID, IWTimestamp stamp) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#hasTerminationInFuture
	 */
	public boolean hasTerminationInFuture(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getEarliestPossiblePlacementDate
	 */
	public Date getEarliestPossiblePlacementDate(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLatestTerminatedContract
	 */
	public ChildCareContract getLatestTerminatedContract(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCurrentProviderByPlacement
	 */
	public School getCurrentProviderByPlacement(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCurrentProviderByNotTerminatedPlacement
	 */
	public School getCurrentProviderByNotTerminatedPlacement(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLatestContract
	 */
	public ChildCareContract getLatestContract(int childID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getLatestContractByApplication
	 */
	public ChildCareContract getLatestContractByApplication(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getContractByApplicationAndDate
	 */
	public ChildCareContract getContractByApplicationAndDate(int applicationID, Date date) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCaseLogNewContracts
	 */
	public Collection getCaseLogNewContracts(Timestamp fromDate, Timestamp toDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCaseLogAlteredContracts
	 */
	public Collection getCaseLogAlteredContracts(Timestamp fromDate, Timestamp toDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCaseLogTerminatedContracts
	 */
	public Collection getCaseLogTerminatedContracts(Timestamp fromDate, Timestamp toDate) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#importChildToProvider
	 */
	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, String careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin) throws AlreadyCreatedException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#importChildToProvider
	 */
	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, String careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin, boolean canCreateMultiple) throws AlreadyCreatedException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#importChildToProvider
	 */
	public boolean importChildToProvider(int applicationID, int childID, int providerID, int groupID, String careTime, int employmentTypeID, int schoolTypeID, String comment, IWTimestamp fromDate, IWTimestamp toDate, Locale locale, User parent, User admin, boolean canCreateMultiple, IWTimestamp lastReplyDate, String preSchool, boolean extraContract, String extraContractMessage, boolean extraContractOther, String extraContractOtherMessage) throws AlreadyCreatedException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findAllEmploymentTypes
	 */
	public Collection findAllEmploymentTypes() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findAllManagementTypes
	 */
	public Collection findAllManagementTypes() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#updateMissingPlacements
	 */
	public void updateMissingPlacements() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findUnhandledApplicationsNotInCommune
	 */
	public Collection findUnhandledApplicationsNotInCommune() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getOutsideSchoolArea
	 */
	public int getOutsideSchoolArea() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findSentInAndRejectedApplicationsByArea
	 */
	public Collection findSentInAndRejectedApplicationsByArea(Object area, int monthsInQueue, int weeksToPlacementDate, boolean firstHandOnly, String caseCode) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#findRejectedApplicationsByChild
	 */
	public Collection findRejectedApplicationsByChild(int childID) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusString
	 */
	public String getStatusString(char status) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getStatusStringAbbr
	 */
	public String getStatusStringAbbr(char status) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#wasRejectedByParent
	 */
	public boolean wasRejectedByParent(ChildCareApplication application) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isSchoolClassBelongingToSchooltype
	 */
	public boolean isSchoolClassBelongingToSchooltype(int schoolClassId, int schoolTypeId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isTryingToChangeSchoolTypeButNotSchoolClass
	 */
	public boolean isTryingToChangeSchoolTypeButNotSchoolClass(int currentArchiveID, int schoolTypeId, int schoolClassId) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getProviderStats
	 */
	public Collection getProviderStats(Locale sortLocale) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getProviderStatsBeforeUpdate
	 */
	public Collection getProviderStatsBeforeUpdate(Locale sortLocale) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPlacementHelper
	 */
	public PlacementHelper getPlacementHelper(Integer applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getPlacementHelper
	 */
	public PlacementHelper getPlacementHelper(ChildCareApplication application) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#setUserAsDeceased
	 */
	public boolean setUserAsDeceased(Integer userID, Date deceasedDate) throws RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCareBusiness
	 */
	public CareBusiness getCareBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#deleteApplication
	 */
	public void deleteApplication(int applicationID, User user, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectApplication
	 */
	public boolean rejectApplication(ChildCareApplication application, String subject, String message, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#rejectApplication
	 */
	public boolean rejectApplication(int applicationId, String subject, String body, User user) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToParents
	 */
	public void sendMessageToParents(ChildCareApplication application, String subject, String body) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToParents
	 */
	public void sendMessageToParents(ChildCareApplication application, String subject, String body, File attachment) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToParents
	 */
	public void sendMessageToParents(ChildCareApplication application, String subject, String body, boolean alwaysSendLetter) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToParents
	 */
	public void sendMessageToParents(ChildCareApplication application, String subject, String body, String letterBody, boolean alwaysSendLetter) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToParents
	 */
	public void sendMessageToParents(ChildCareApplication application, String subject, String body, String letterBody, boolean alwaysSendLetter, boolean sendToOtherParent) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#sendMessageToParents
	 */
	public void sendMessageToParents(ChildCareApplication application, String subject, String body, String letterBody, File attachment, boolean alwaysSendLetter, boolean sendToOtherParent) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#isAfterSchoolApplication
	 */
	public boolean isAfterSchoolApplication(Case application) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getApplication
	 */
	public ChildCareApplication getApplication(int applicationID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getFirstProviderForUser
	 */
	public School getFirstProviderForUser(User user) throws FinderException, RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getSchoolHome
	 */
	public SchoolHome getSchoolHome() throws RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getSchoolTypeClassMap
	 */
	public Map getSchoolTypeClassMap(Collection schoolTypes, int schoolID, int seasonID, Boolean showSubGroups, Boolean showNonSeasonGroups, String noSchoolClassFoundEntry) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCareTimes
	 */
	public Collection getCareTimes() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCareTimes
	 */
	public Collection getCareTimes(User child) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getCareTime
	 */
	public CareTime getCareTime(String careTime) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUseVacancies
	 */
	public boolean getUseVacancies() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUseEmployment
	 */
	public boolean getUseEmployment() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUseParental
	 */
	public boolean getUseParental() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getUsePreschoolLine
	 */
	public boolean getUsePreschoolLine() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getMarkChildrenOutsideCommune
	 */
	public boolean getMarkChildrenOutsideCommune() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getEventListener
	 */
	public Class getEventListener() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#changeAllContractsInRange
	 */
	public Collection changeAllContractsInRange(String fromCareTime, String toCareTime, Date dayOfChange, Date fromDateOfBirth, Date toDateOfBirth, User performer, Locale locale) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getChildCareContractsByProviderAndClassMemberDates
	 */
	public Collection getChildCareContractsByProviderAndClassMemberDates(Integer schoolId, Date startFrom, Date startTo, Date endFrom, Date endTo) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.ChildCareBusinessBean#getChildCareContractBySchoolClassMember
	 */
	public ChildCareContract getChildCareContractBySchoolClassMember(SchoolClassMember member) throws RemoteException;
}