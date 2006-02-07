/*
 * $Id: AfterSchoolBusinessHome.java,v 1.6 2006/02/07 15:53:09 laddi Exp $
 * Created on Feb 7, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO laddi Describe Type AfterSchoolBusinessHome
 * </p>
 *  Last modified: $Date: 2006/02/07 15:53:09 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public interface AfterSchoolBusinessHome extends IBOHome {

	public AfterSchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
