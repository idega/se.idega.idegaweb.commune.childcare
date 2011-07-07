package se.idega.idegaweb.commune.childcare.importer;


public interface NackaCohabitantImportFileHandler extends com.idega.business.IBOService,com.idega.block.importer.business.ImportFileHandler
{
 @Override
public java.util.List getFailedRecords() throws java.rmi.RemoteException;
 @Override
public boolean handleRecords()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void printFailedRecords() throws java.rmi.RemoteException;
 @Override
public void setImportFile(com.idega.block.importer.data.ImportFile p0) throws java.rmi.RemoteException;
 @Override
public void setRootGroup(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
}
