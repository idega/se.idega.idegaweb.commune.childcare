package se.idega.idegaweb.commune.childcare.importer;


public interface NackaCheckImportFileHandler extends com.idega.business.IBOService,com.idega.block.importer.business.ImportFileHandler
{
 public void printFailedRecords() throws java.rmi.RemoteException;
 @Override
public boolean handleRecords()throws java.rmi.RemoteException, java.rmi.RemoteException;
 @Override
public java.util.List getFailedRecords() throws java.rmi.RemoteException;
 @Override
public void setImportFile(com.idega.block.importer.data.ImportFile p0) throws java.rmi.RemoteException;
 @Override
public void setRootGroup(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
}
