/*
 * $Id: ChildCareBusinessHome.java 1.1 3.12.2004 laddi Exp $
 * Created on 3.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHome;


/**
 * Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public interface ChildCareBusinessHome extends IBOHome {

	public ChildCareBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
