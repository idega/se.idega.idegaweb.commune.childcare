package se.idega.idegaweb.commune.childcare.check.business;

import javax.ejb.*;

import com.idega.block.process.business.CaseBusiness;

public interface CheckBusiness extends com.idega.business.IBOService, CaseBusiness
{
 public int getCheckFee() throws java.rmi.RemoteException;
 public java.util.Collection findUnhandledChecks()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int createCheck(int p0,int p1,int p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,int p6,int p7,int p8,int p9,com.idega.user.data.User p10,java.lang.String p11,boolean p12,boolean p13,boolean p14,boolean p15,boolean p16)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.core.data.PostalCode getUserPostalCode(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public java.util.Collection findChecks()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllApprovedChecksByUser(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check createCheck(int p0,int p1,int p2,int p3,com.idega.user.data.User p4)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check getCheck(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.core.data.Address getUserAddress(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public com.idega.user.data.User getParent(com.idega.user.data.User p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean allRulesVerified(se.idega.idegaweb.commune.childcare.check.data.Check p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void sendMessageToPrinter(se.idega.idegaweb.commune.childcare.check.data.Check p0,int p1,java.lang.String p2,java.lang.String p3)throws java.lang.Exception, java.rmi.RemoteException;
 public java.util.Collection findAllChecksByUser(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check saveCheckRules(int p0,java.lang.String[] p1,java.lang.String p2,java.lang.String p3,com.idega.user.data.User p4)throws java.lang.Exception, java.rmi.RemoteException;
 public void approveCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0,java.lang.String p1,java.lang.String p2,com.idega.user.data.User p3)throws java.lang.Exception, java.rmi.RemoteException;
 public int getMethodSystem() throws java.rmi.RemoteException;
 public com.idega.user.data.User getUserById(int p0)throws java.lang.Exception, java.rmi.RemoteException;
 public int getCheckAmount() throws java.rmi.RemoteException;
 public int getMethodUser() throws java.rmi.RemoteException;
 public void sendMessageToArchive(se.idega.idegaweb.commune.childcare.check.data.Check p0,int p1,java.lang.String p2,java.lang.String p3)throws java.lang.Exception, java.rmi.RemoteException;
 public int getUserID(se.idega.idegaweb.commune.childcare.check.data.Check p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void retrialCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0,java.lang.String p1,java.lang.String p2,com.idega.user.data.User p3)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolType getSchoolType(int p0)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.user.data.User getUserByPersonalId(java.lang.String p0)throws java.lang.Exception, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check saveCheckRules(se.idega.idegaweb.commune.childcare.check.data.Check p0,java.lang.String[] p1,java.lang.String p2,java.lang.String p3,com.idega.user.data.User p4)throws java.lang.Exception, java.rmi.RemoteException;
 public void sendMessageToCitizen(se.idega.idegaweb.commune.childcare.check.data.Check p0,int p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int hasChildApprovedCheck(com.idega.user.data.User user, int childID) throws java.rmi.RemoteException;
 public int createGrantedCheck(com.idega.user.data.User child) throws CreateException,java.rmi.RemoteException;
 public int createGrantedCheck(se.idega.idegaweb.commune.childcare.check.data.Check check) throws CreateException,java.rmi.RemoteException; 
}
