/*
 * $Id: ChildCareBusinessHomeImpl.java 1.1 Oct 19, 2004 thomas Exp $
 * Created on Oct 19, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public class ChildCareBusinessHomeImpl extends IBOHomeImpl implements ChildCareBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ChildCareBusiness.class;
	}

	public ChildCareBusiness create() throws javax.ejb.CreateException {
		return (ChildCareBusiness) super.createIBO();
	}
}
