package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareApplicationHome extends com.idega.data.IDOHome
{
 public ChildCareApplication create() throws javax.ejb.CreateException;
 public ChildCareApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ChildCareApplication findApplicationByChildAndChoiceNumber(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByUserAndStatus(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllCasesByStatus(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,java.lang.String p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllCasesByProviderStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllCasesByProviderStatusNotRejected(int p0,java.lang.String p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findAllCasesByProviderAndStatus(int p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public ChildCareApplication findApplicationByChildAndChoiceNumber(com.idega.user.data.User p0,int p1)throws javax.ejb.FinderException;

}