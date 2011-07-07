package se.idega.idegaweb.commune.childcare.importer;


public class NackaQueueImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements NackaQueueImportFileHandlerHome
{
 @Override
protected Class getBeanInterfaceClass(){
  return NackaQueueImportFileHandler.class;
 }


 @Override
public NackaQueueImportFileHandler create() throws javax.ejb.CreateException{
  return (NackaQueueImportFileHandler) super.createIBO();
 }



}