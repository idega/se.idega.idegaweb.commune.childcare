package se.idega.idegaweb.commune.childcare.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface AfterSchoolBusinessHome extends IBOHome {

	public AfterSchoolBusiness create() throws CreateException, RemoteException;
}