package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareApplicationHome extends com.idega.data.IDOHome
{
 public ChildCareApplication create() throws javax.ejb.CreateException;
 public ChildCareApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ChildCareApplication findActiveApplicationByChild(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndNotInStatus(int p0,int p1,java.sql.Date p2,java.sql.Date p3,java.lang.String[] p4,int p5,int p6)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndNotInStatus(int p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(int p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(com.idega.block.school.data.School p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderAndStatus(int p0,com.idega.block.process.data.CaseStatus p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByProviderStatusNotRejected(int p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByStatus(java.lang.String p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllCasesByUserAndStatus(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllChildCasesByProvider(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection findApplicationByChild(int p0)throws javax.ejb.FinderException;
 public ChildCareApplication findApplicationByChildAndChoiceNumber(com.idega.user.data.User p0,int p1)throws javax.ejb.FinderException;
 public ChildCareApplication findApplicationByChildAndChoiceNumber(int p0,int p1)throws javax.ejb.FinderException;
 public ChildCareApplication findApplicationByChildAndChoiceNumberInStatus(int p0,int p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public ChildCareApplication findApplicationByChildAndChoiceNumberNotInStatus(int p0,int p1,java.lang.String[] p2)throws javax.ejb.FinderException;
 public ChildCareApplication findApplicationByChildAndChoiceNumberWithStatus(int p0,int p1,java.lang.String p2)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationByChildAndNotInStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public ChildCareApplication findApplicationByChildAndProvider(int p0,int p1)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndDate(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndStatus(int p0,java.lang.String[] p1)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndStatus(int p0,java.lang.String[] p1,int p2,int p3)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndStatus(int p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.util.Collection findApplicationsByProviderAndStatus(int p0,java.lang.String p1,int p2,int p3)throws javax.ejb.FinderException;
 public ChildCareApplication findNewestApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public ChildCareApplication findOldestApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public int getNumberOfActiveApplications(int p0)throws com.idega.data.IDOException;
 public int getNumberOfApplications(int p0,java.lang.String[] p1)throws com.idega.data.IDOException;
 public int getNumberOfApplications(int p0,java.lang.String[] p1,int p2,java.sql.Date p3,java.sql.Date p4)throws com.idega.data.IDOException;
 public int getNumberOfApplications(int p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getNumberOfApplicationsForChild(int p0)throws com.idega.data.IDOException;
 public int getNumberOfApplicationsForChild(int p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getNumberOfPlacedApplications(int p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException;
 public int getPositionInQueue(java.sql.Date p0,int p1,java.lang.String[] p2)throws com.idega.data.IDOException;
 public int getPositionInQueue(java.sql.Date p0,int p1,java.lang.String p2)throws com.idega.data.IDOException;
 public int getPositionInQueueByDate(int p0,java.sql.Date p1,int p2,java.lang.String p3)throws com.idega.data.IDOException;
 public int getPositionInQueueByDate(int p0,java.sql.Date p1,int p2,java.lang.String[] p3)throws com.idega.data.IDOException;
 public int getQueueSizeByAreaInStatus(int p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getQueueSizeByAreaNotInStatus(int p0,java.lang.String[] p1)throws com.idega.data.IDOException;
 public int getQueueSizeInStatus(int p0,java.lang.String p1)throws com.idega.data.IDOException;
 public int getQueueSizeNotInStatus(int p0,java.lang.String[] p1)throws com.idega.data.IDOException;

}