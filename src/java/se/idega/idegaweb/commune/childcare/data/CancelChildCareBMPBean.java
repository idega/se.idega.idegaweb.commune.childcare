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

import se.idega.idegaweb.commune.care.check.data.Check;
import se.idega.idegaweb.commune.care.check.data.GrantedCheck;

import com.idega.block.process.data.AbstractCaseBMPBean;
import com.idega.block.process.data.Case;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;

/**
 * This class does something very clever.....
 *
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class CancelChildCareBMPBean extends AbstractCaseBMPBean implements CancelChildCare, Case {

	private final static String ENTITY_NAME = "comm_cancel_care";
	private final static String CASE_CODE_KEY = "MBANKOO";
	private final static String CASE_CODE_KEY_DESC = "Cancel child care contract";

	protected final static String REASON = "reason";
	protected final static String CHECK_ID = "check_id";
	protected final static String CANCELLATION_DATE = "cancellation_date";

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
		addAttribute(REASON,"",true,true,java.lang.String.class,1000);
		addAttribute(CANCELLATION_DATE,"",true,true,java.sql.Date.class);

		addManyToOneRelationship(CHECK_ID,GrantedCheck.class);
	}

	/**
	 * A method to get the reason for the cancellation.
	 */
	@Override
	public String getReason() {
		return getStringColumnValue(REASON);
	}

	@Override
	public Date getCancellationDate() {
		return (Date)getColumnValue(CANCELLATION_DATE);
	}

	@Override
	public int getCheckId() {
		return getIntColumnValue(CHECK_ID);
	}

	@Override
	public GrantedCheck getCheck() {
		return (GrantedCheck)getColumnValue(CHECK_ID);
	}

	@Override
	public void setReason(String reason) {
		setColumn(REASON,reason);
	}

	@Override
	public void setCancellationDate(Date date) {
		setColumn(CANCELLATION_DATE,date);
	}

	@Override
	public void setCheckId(int checkId) {
		setColumn(CHECK_ID,checkId);
	}

	@Override
	public void setCheck(Check check) {
		setColumn(CHECK_ID,check);
	}

	@Override
	public void addSubscriber(User arg0) throws IDOAddRelationshipException {
	}

	@Override
	public Collection<User> getSubscribers() {
		return null;
	}

	@Override
	public void removeSubscriber(User arg0)	throws IDORemoveRelationshipException {
	}
}