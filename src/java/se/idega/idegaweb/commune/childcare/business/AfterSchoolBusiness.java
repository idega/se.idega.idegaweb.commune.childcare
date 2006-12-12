package se.idega.idegaweb.commune.childcare.business;


import com.idega.block.process.business.CaseBusiness;
import com.idega.block.school.data.School;
import com.idega.block.process.data.Case;
import javax.ejb.CreateException;
import com.idega.block.process.data.CaseStatus;
import se.idega.idegaweb.commune.care.data.AfterSchoolChoice;
import java.sql.Date;
import com.idega.user.data.User;
import com.idega.data.IDOCreateException;
import java.rmi.RemoteException;
import java.util.Locale;
import com.idega.block.school.data.SchoolClass;
import java.util.Collection;
import se.idega.idegaweb.commune.school.business.SchoolChoiceBusiness;
import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;
import com.idega.business.IBOService;
import com.idega.block.school.data.SchoolSeason;
import se.idega.idegaweb.commune.care.business.CareBusiness;
import com.idega.block.school.business.SchoolBusiness;
import java.util.List;

public interface AfterSchoolBusiness extends IBOService, CaseBusiness {

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getBundleIdentifier
	 */
	public String getBundleIdentifier() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getNumberOfApplications
	 */
	public int getNumberOfApplications(SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getAfterSchoolChoice
	 */
	public AfterSchoolChoice getAfterSchoolChoice(Object afterSchoolChoiceID) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByProvider
	 */
	public Collection findChoicesByProvider(int providerID) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByProvider
	 */
	public Collection findChoicesByProvider(int providerID, String sorting) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoicesByChildAndChoiceNumberAndSeason
	 */
	public AfterSchoolChoice findChoicesByChildAndChoiceNumberAndSeason(Integer childID, int choiceNumber, Integer seasonID) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getOngoingAndNextSeasons
	 */
	public Collection getOngoingAndNextSeasons() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#hasOpenApplication
	 */
	public boolean hasOpenApplication(User child, SchoolSeason season, int choiceNumber) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#hasCancelledApplication
	 */
	public boolean hasCancelledApplication(User child, SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#findChoiceByChild
	 */
	public AfterSchoolChoice findChoiceByChild(User child, SchoolSeason season, int choiceNumber) throws FinderException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#acceptAfterSchoolChoice
	 */
	public boolean acceptAfterSchoolChoice(Object afterSchoolChoiceID, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#denyAfterSchoolChoice
	 */
	public boolean denyAfterSchoolChoice(Object afterSchoolChoiceID, User performer) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoice
	 */
	public AfterSchoolChoice createAfterSchoolChoice(IWTimestamp stamp, User user, Integer childID, Integer providerID, Integer choiceNumber, String message, CaseStatus caseStatus, Case parentCase, Date placementDate, SchoolSeason season, boolean sendMessage, String subject, String body) throws CreateException, RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoice
	 */
	public AfterSchoolChoice createAfterSchoolChoice(IWTimestamp stamp, User user, Integer childID, Integer providerID, Integer choiceNumber, String message, CaseStatus caseStatus, Case parentCase, Date placementDate, SchoolSeason season, boolean sendMessage, String subject, String body, boolean isFClassAndPrio) throws CreateException, RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoices
	 */
	public List createAfterSchoolChoices(User user, Integer childId, Integer[] providerIDs, String message, String[] placementDates, SchoolSeason season, String subject, String body) throws IDOCreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createAfterSchoolChoices
	 */
	public List createAfterSchoolChoices(User user, Integer childId, Integer[] providerIDs, String message, String[] placementDates, SchoolSeason season, String subject, String body, boolean isFClassAndPrio) throws IDOCreateException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#createContractsForChildrenWithSchoolPlacement
	 */
	public Collection createContractsForChildrenWithSchoolPlacement(int providerId, User user, Locale locale, int seasonId) throws RemoteException, RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(Object schoolPK, Object seasonPK) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getDefaultGroup
	 */
	public SchoolClass getDefaultGroup(School school, SchoolSeason season) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#storeAfterSchoolCare
	 */
	public AfterSchoolChoice storeAfterSchoolCare(IWTimestamp stamp, User user, User child, School provider, String message, SchoolSeason season, String payerName, String payerPersonalID, String cardType, String cardNumber, int validMonth, int validYear) throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getSchoolBusiness
	 */
	public SchoolBusiness getSchoolBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getSchoolChoiceBusiness
	 */
	public SchoolChoiceBusiness getSchoolChoiceBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getChildCareBusiness
	 */
	public ChildCareBusiness getChildCareBusiness() throws RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.childcare.business.AfterSchoolBusinessBean#getCareBusiness
	 */
	public CareBusiness getCareBusiness() throws RemoteException;
}