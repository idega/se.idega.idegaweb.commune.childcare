package se.idega.idegaweb.commune.childcare.importer;


public class SollentunaChildCareImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements SollentunaChildCareImportFileHandlerHome
{
 @Override
protected Class getBeanInterfaceClass(){
  return SollentunaChildCareImportFileHandler.class;
 }


 @Override
public SollentunaChildCareImportFileHandler create() throws javax.ejb.CreateException{
  return (SollentunaChildCareImportFileHandler) super.createIBO();
 }



}