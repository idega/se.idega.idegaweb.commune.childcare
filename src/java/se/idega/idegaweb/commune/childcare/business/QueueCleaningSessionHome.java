/*
 * $Id: QueueCleaningSessionHome.java,v 1.1 2004/11/25 10:44:06 aron Exp $
 * Created on 25.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;




import com.idega.business.IBOHome;

/**
 * 
 *  Last modified: $Date: 2004/11/25 10:44:06 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface QueueCleaningSessionHome extends IBOHome {
    public QueueCleaningSession create() throws javax.ejb.CreateException,
            java.rmi.RemoteException;

}
