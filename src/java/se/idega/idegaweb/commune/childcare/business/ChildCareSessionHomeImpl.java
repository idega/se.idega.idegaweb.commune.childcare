/*
 * $Id: ChildCareSessionHomeImpl.java,v 1.4 2005/12/11 16:24:15 laddi Exp $
 * Created on Dec 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO laddi Describe Type ChildCareSessionHomeImpl
 * </p>
 *  Last modified: $Date: 2005/12/11 16:24:15 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class ChildCareSessionHomeImpl extends IBOHomeImpl implements ChildCareSessionHome {

	protected Class getBeanInterfaceClass() {
		return ChildCareSession.class;
	}

	public ChildCareSession create() throws javax.ejb.CreateException {
		return (ChildCareSession) super.createIBO();
	}
}
