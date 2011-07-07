package se.idega.idegaweb.commune.childcare.importer;


public class NackaCheckImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements NackaCheckImportFileHandlerHome
{
 @Override
protected Class getBeanInterfaceClass(){
  return NackaCheckImportFileHandler.class;
 }


 @Override
public NackaCheckImportFileHandler create() throws javax.ejb.CreateException{
  return (NackaCheckImportFileHandler) super.createIBO();
 }



}