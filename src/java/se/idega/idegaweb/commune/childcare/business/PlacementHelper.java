/*
 * $Id: PlacementHelper.java,v 1.2 2004/10/07 13:42:31 thomas Exp $
 * Created on 5.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.util.Date;

import com.idega.idegaweb.IWResourceMessage;

import se.idega.idegaweb.commune.accounting.childcare.data.ChildCareContract;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

/**
 * 
 *  Last modified: $Date: 2004/10/07 13:42:31 $ by $Author: thomas $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.2 $
 */
public interface PlacementHelper {

    public void setApplication(ChildCareApplication app);
    public ChildCareApplication getApplication();
    public ChildCareContract getContract();
    public void setContract(ChildCareContract contract);
    public boolean hasEarliestPlacementDate();
    	public Date getEarliestPlacementDate();
    	public boolean hasLatestPlacementDate();
    	public Date getLatestPlacementDate();
    	public Integer getMaximumCareTimeHours();
    	public Integer getCurrentCareTimeHours();
    	public Integer getCurrentClassID();
    	public Integer getCurrentSchoolTypeID();
    	public Integer getCurrentProviderID();
    	public Integer getCurrentEmploymentID();
    	public IWResourceMessage getEarliestPlacementMessage();
    	public IWResourceMessage getLatestPlacementMessage();
    	
}