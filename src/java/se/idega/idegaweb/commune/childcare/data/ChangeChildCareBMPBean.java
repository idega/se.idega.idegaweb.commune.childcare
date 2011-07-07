/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;
import java.util.Collection;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.block.school.data.School;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;

/**
 * This class does something very clever.....
 *
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ChangeChildCareBMPBean extends AbstractCaseBMPBean implements ChangeChildCare, Case {

	private final static String ENTITY_NAME = "comm_change_care";
	private final static String CASE_CODE_KEY = "MBANKON";
	private final static String CASE_CODE_KEY_DESC = "Change child care contract";

	protected final static String PROVIDER_ID = "provider_id";
	protected final static String FROM_DATE = "from_date";
	protected final static String CHILD_ID = "child_id";
	protected final static String CARE_TIME = "care_time";

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeKey()
	 */
	@Override
	public String getCaseCodeKey() {
		return CASE_CODE_KEY;
	}

	/**
	 * @see com.idega.block.process.data.AbstractCaseBMPBean#getCaseCodeDescription()
	 */
	@Override
	public String getCaseCodeDescription() {
		return CASE_CODE_KEY_DESC;
	}

	/**
	 * @see com.idega.data.IDOEntity#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOEntity#initializeAttributes()
	 */
	@Override
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(FROM_DATE,"",true,true,java.sql.Date.class);
		addAttribute(CARE_TIME,"",true,true,java.lang.Integer.class);

		addManyToOneRelationship(PROVIDER_ID,School.class);
		addManyToOneRelationship(CHILD_ID,User.class);
	}

	/**
	 * Gets the id of the child care provider.
	 */
	@Override
	public int getProviderId() {
		return getIntColumnValue(PROVIDER_ID);
	}

	@Override
	public School getProvider() {
		return (School)getColumnValue(PROVIDER_ID);
	}

	@Override
	public Date getFromDate() {
		return (Date)getColumnValue(FROM_DATE);
	}

	@Override
	public int getChildId() {
		return getIntColumnValue(CHILD_ID);
	}

	@Override
	public User getChild() {
		return (User) getColumnValue(CHILD_ID);
	}

	@Override
	public int getCareTime() {
		return getIntColumnValue(CARE_TIME);
	}

	@Override
	public void setProviderId(int id) {
		setColumn(PROVIDER_ID,id);
	}

	@Override
	public void setProvider(School provider) {
		setColumn(PROVIDER_ID,provider);
	}

	@Override
	public void setFromDate(Date date) {
		setColumn(FROM_DATE,date);
	}

	@Override
	public void setChildId(int id) {
		setColumn(CHILD_ID,id);
	}

	@Override
	public void setChild(User child) {
		setColumn(CHILD_ID,child);
	}

	@Override
	public void setCareTime(int careTime) {
		setColumn(CARE_TIME,careTime);
	}

	@Override
	public void addSubscriber(User arg0) throws IDOAddRelationshipException {
	}

	@Override
	public Collection<User> getSubscribers() {
		return null;
	}

	@Override
	public void removeSubscriber(User arg0) throws IDORemoveRelationshipException {
	}
}