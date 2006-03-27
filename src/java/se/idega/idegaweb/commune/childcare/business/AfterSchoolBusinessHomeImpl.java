/*
 * $Id: AfterSchoolBusinessHomeImpl.java,v 1.11 2006/03/27 15:27:29 laddi Exp $
 * Created on Mar 27, 2006
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
 *  Last modified: $Date: 2006/03/27 15:27:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.11 $
 */
public class AfterSchoolBusinessHomeImpl extends IBOHomeImpl implements AfterSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AfterSchoolBusiness.class;
	}

	public AfterSchoolBusiness create() throws javax.ejb.CreateException {
		return (AfterSchoolBusiness) super.createIBO();
	}

}
