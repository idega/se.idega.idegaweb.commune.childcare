package se.idega.idegaweb.commune.childcare.data;

import javax.ejb.*;

public interface ChildCareApplication extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public void setChild(com.idega.user.data.User p0) throws java.rmi.RemoteException;
 public void setParentsAgree(boolean p0) throws java.rmi.RemoteException;
 public int getChildrenCareTypeId() throws java.rmi.RemoteException;
 public void setProviderId(int p0) throws java.rmi.RemoteException;
 public int getCheckId() throws java.rmi.RemoteException;
 public com.idega.user.data.User getChild() throws java.rmi.RemoteException;
 public void setChoiceNumber(int p0) throws java.rmi.RemoteException;
 public java.sql.Date getQueueDate() throws java.rmi.RemoteException;
 public com.idega.block.school.data.School getProvider() throws java.rmi.RemoteException;
 public void setCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0) throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
 public java.sql.Date getFromDate() throws java.rmi.RemoteException;
 public void setChildrenCareType(com.idega.block.school.data.SchoolType p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check getCheck() throws java.rmi.RemoteException;
 public com.idega.block.school.data.SchoolType getChildrenCareType() throws java.rmi.RemoteException;
 public int getChildId() throws java.rmi.RemoteException;
 public int getMethod() throws java.rmi.RemoteException;
 public void setProvider(com.idega.block.school.data.School p0) throws java.rmi.RemoteException;
 public void setCheckId(int p0) throws java.rmi.RemoteException;
 public void setQueueDate(java.sql.Date p0) throws java.rmi.RemoteException;
 public void setMethod(int p0) throws java.rmi.RemoteException;
 public void setFromDate(java.sql.Date p0) throws java.rmi.RemoteException;
 public int getProviderId() throws java.rmi.RemoteException;
 public void setChildrenCareTypeId(int p0) throws java.rmi.RemoteException;
 public boolean getParentsAgree() throws java.rmi.RemoteException;
 public int getCareTime() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public int getChoiceNumber() throws java.rmi.RemoteException;
 public void setChildId(int p0) throws java.rmi.RemoteException;
 public void setCareTime(int p0) throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
}
