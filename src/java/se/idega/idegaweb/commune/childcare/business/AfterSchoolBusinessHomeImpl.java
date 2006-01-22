/*
 * $Id: AfterSchoolBusinessHomeImpl.java,v 1.5 2006/01/22 08:40:23 laddi Exp $
 * Created on Jan 22, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO laddi Describe Type AfterSchoolBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/01/22 08:40:23 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public class AfterSchoolBusinessHomeImpl extends IBOHomeImpl implements AfterSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AfterSchoolBusiness.class;
	}

	public AfterSchoolBusiness create() throws javax.ejb.CreateException {
		return (AfterSchoolBusiness) super.createIBO();
	}
}
