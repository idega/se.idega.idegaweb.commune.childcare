/*
 * $Id: ChildCareSessionHome.java,v 1.4 2005/12/11 16:24:15 laddi Exp $
 * Created on Dec 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO laddi Describe Type ChildCareSessionHome
 * </p>
 *  Last modified: $Date: 2005/12/11 16:24:15 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public interface ChildCareSessionHome extends IBOHome {

	public ChildCareSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
