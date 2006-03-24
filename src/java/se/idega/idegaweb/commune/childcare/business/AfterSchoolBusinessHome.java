/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHome;


/**
 * <p>
 * TODO Dainis Describe Type AfterSchoolBusinessHome
 * </p>
 *  Last modified: $Date: 2006/03/24 13:15:16 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.10 $
 */
public interface AfterSchoolBusinessHome extends IBOHome {

	public AfterSchoolBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
