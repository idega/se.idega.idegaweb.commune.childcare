/*
 * Created on 26.3.2003
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.core.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

/**
 * @author laddi
 */
public class ChildCareContractArchiveBMPBean extends GenericEntity implements ChildCareContractArchive {

	private static final String ENTITY_NAME = "comm_childcare_archive";
	
	public static final String COLUMN_CHILD_ID = "child_id";
	public static final String COLUMN_APPLICATION_ID = "application_id";
	public static final String COLUMN_CONTRACT_FILE_ID = "contract_file_id";
	public static final String COLUMN_CREATED_DATE = "created_date";
	public static final String COLUMN_VALID_FROM_DATE = "valid_from_date";
	public static final String COLUMN_TERMINATED_DATE = "terminated_date";
	public static final String COLUMN_CARE_TIME = "care_time";
	
	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_CREATED_DATE,"",true,true,java.sql.Date.class);
		addAttribute(COLUMN_VALID_FROM_DATE,"",true,true,java.sql.Date.class);
		addAttribute(COLUMN_TERMINATED_DATE,"",true,true,java.sql.Date.class);
		addAttribute(COLUMN_CARE_TIME,"",true,true,java.lang.Integer.class);
		
		addManyToOneRelationship(COLUMN_CHILD_ID,User.class);
		addManyToOneRelationship(COLUMN_APPLICATION_ID,ChildCareApplication.class);
		addOneToOneRelationship(COLUMN_CONTRACT_FILE_ID,ICFile.class);
	}
	
	public Date getCreatedDate() {
		return (Date) getColumnValue(COLUMN_CREATED_DATE);
	}

	public Date getValidFromDate() {
		return (Date) getColumnValue(COLUMN_VALID_FROM_DATE);
	}
	
	public Date getTerminatedDate() {
		return (Date) getColumnValue(COLUMN_TERMINATED_DATE);
	}
	
	public int getCareTime() {
		return getIntColumnValue(COLUMN_CARE_TIME);
	}
	
	public int getChildID() {
		return getIntColumnValue(COLUMN_CHILD_ID);
	}
	
	public User getChild() {
		return (User) getColumnValue(COLUMN_CHILD_ID);
	}
	
	public int getApplicationID() {
		return getIntColumnValue(COLUMN_APPLICATION_ID);
	}
	
	public ChildCareApplication getApplication() {
		return (ChildCareApplication) getColumnValue(COLUMN_APPLICATION_ID);
	}

	public int getContractFileID() {
		return getIntColumnValue(COLUMN_CONTRACT_FILE_ID);
	}
	
	public ICFile getContractFile() {
		return (ICFile) getColumnValue(COLUMN_CONTRACT_FILE_ID);
	}
	
	public void setCreatedDate(Date createdDate) {
		setColumn(COLUMN_CREATED_DATE, createdDate);
	}

	public void setValidFromDate(Date validFromDate) {
		setColumn(COLUMN_VALID_FROM_DATE, validFromDate);
	}
	
	public void setTerminatedDate(Date terminatedDate) {
		setColumn(COLUMN_TERMINATED_DATE, terminatedDate);
	}
	
	public void setCareTime(int careTime) {
		setColumn(COLUMN_CARE_TIME, careTime);
	}
	
	public void setChildID(int childID) {
		setColumn(COLUMN_CHILD_ID, childID);
	}
	
	public void setChild(User child) {
		setColumn(COLUMN_CHILD_ID, child);
	}
	
	public void setApplicationID(int applicationID) {
		setColumn(COLUMN_APPLICATION_ID, applicationID);
	}
	
	public void setApplication(ChildCareApplication application) {
		setColumn(COLUMN_APPLICATION_ID, application);
	}
	
	public void setContractFileID(int contractFileID) {
		setColumn(COLUMN_CONTRACT_FILE_ID, contractFileID);
	}
	
	public void setContractFile(ICFile contractFile) {
		setColumn(COLUMN_CONTRACT_FILE_ID, contractFile);
	}
	
	public Collection ejbFindByChild(int childID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CHILD_ID, childID);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindByChildAndProvider(int childID, int providerID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).append(" a, ").append(ChildCareApplicationBMPBean.ENTITY_NAME).append(" c");
		sql.appendWhereEquals("a."+COLUMN_CHILD_ID, childID);
		sql.appendAndEquals("a."+COLUMN_CHILD_ID, "c."+ChildCareApplicationBMPBean.CHILD_ID);
		sql.appendAndEquals("c."+ChildCareApplicationBMPBean.PROVIDER_ID, providerID);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindByApplication(int applicationID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_APPLICATION_ID, applicationID);
		sql.appendOrderBy(COLUMN_VALID_FROM_DATE+" desc");
		return idoFindPKsByQuery(sql);
	}

	public Integer ejbFindByContractFileID(int contractFileID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_CONTRACT_FILE_ID, contractFileID);
		return (Integer) idoFindOnePKByQuery(sql);
	}
}