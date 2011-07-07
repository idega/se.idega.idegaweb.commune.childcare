package se.idega.idegaweb.commune.childcare.importer;


public class NackaDBVWOPlaceImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements NackaDBVWOPlaceImportFileHandlerHome
{
 protected Class getBeanInterfaceClass(){
  return NackaDBVWOPlaceImportFileHandler.class;
 }


 public NackaDBVWOPlaceImportFileHandler create() throws javax.ejb.CreateException{
  return (NackaDBVWOPlaceImportFileHandler) super.createIBO();
 }



}