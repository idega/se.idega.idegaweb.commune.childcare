package se.idega.idegaweb.commune.childcare.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface ChildCareBusinessHome extends IBOHome {

	public ChildCareBusiness create() throws CreateException, RemoteException;
}