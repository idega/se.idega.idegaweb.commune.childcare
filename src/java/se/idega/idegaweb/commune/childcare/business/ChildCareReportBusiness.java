/*
 * Created on 17.12.2003
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.business.IBOSession;

/**
 * @author laddi
 */
public interface ChildCareReportBusiness extends IBOSession {

	public final static String PREFIX = "child_care_report.";

	public static final String FIELD_PERSONAL_ID = "personal_id";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_ADDRESS = "address";
	public static final String FIELD_ZIP_CODE = "zip_code";
	public static final String FIELD_AREA = "area";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_PHONE = "phone";
	public static final String FIELD_PLACEMENT_DATE = "placement_date";
	public static final String FIELD_QUEUE_DATE = "queue_date";
	public static final String FIELD_PROVIDER = "provider";
	public static final String FIELD_STATUS = "status";
	
	public ReportableCollection getChildCareReport(Integer numberOfWeeks, Integer numberOfMonths, Object areaID, Boolean firstHandOnly) throws RemoteException;
}