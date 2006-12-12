package se.idega.idegaweb.commune.childcare.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class AfterSchoolBusinessHomeImpl extends IBOHomeImpl implements AfterSchoolBusinessHome {

	public Class getBeanInterfaceClass() {
		return AfterSchoolBusiness.class;
	}

	public AfterSchoolBusiness create() throws CreateException {
		return (AfterSchoolBusiness) super.createIBO();
	}
}