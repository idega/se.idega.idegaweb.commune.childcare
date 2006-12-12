package se.idega.idegaweb.commune.childcare.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class ChildCareBusinessHomeImpl extends IBOHomeImpl implements ChildCareBusinessHome {

	public Class getBeanInterfaceClass() {
		return ChildCareBusiness.class;
	}

	public ChildCareBusiness create() throws CreateException {
		return (ChildCareBusiness) super.createIBO();
	}
}