package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareContractHome extends com.idega.data.IDOHome
{
 public ChildCareContract create() throws javax.ejb.CreateException;
 public ChildCareContract findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ChildCareContract findApplicationByContract(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByApplication(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByChild(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByChildAndProvider(int p0,int p1)throws javax.ejb.FinderException;
 public ChildCareContract findByContractFileID(int p0)throws javax.ejb.FinderException;
 public java.util.Collection findByDateRange(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findFutureContractsByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public ChildCareContract findLatestContractByChild(int p0)throws javax.ejb.FinderException;
 public ChildCareContract findLatestTerminatedContractByChild(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public ChildCareContract findValidContractByApplication(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public ChildCareContract findValidContractByChild(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public java.util.Collection findValidContractByProvider(int p0,java.sql.Date p1)throws javax.ejb.FinderException;
 public int getContractsCountByApplication(int p0)throws com.idega.data.IDOException;
 public int getFutureContractsCountByApplication(int p0,java.sql.Date p1)throws com.idega.data.IDOException;
 public int getNumberOfActiveForApplication(int p0,java.sql.Date p1)throws com.idega.data.IDOException;
 public int getNumberOfActiveNotWithProvider(int p0,int p1)throws com.idega.data.IDOException;
 public int getNumberOfTerminatedLaterNotWithProvider(int p0,int p1,java.sql.Date p2)throws com.idega.data.IDOException;

}