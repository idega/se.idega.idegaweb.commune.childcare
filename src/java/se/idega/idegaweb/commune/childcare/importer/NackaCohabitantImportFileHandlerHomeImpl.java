package se.idega.idegaweb.commune.childcare.importer;


public class NackaCohabitantImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements NackaCohabitantImportFileHandlerHome
{
 @Override
protected Class getBeanInterfaceClass(){
  return NackaCohabitantImportFileHandler.class;
 }


 @Override
public NackaCohabitantImportFileHandler create() throws javax.ejb.CreateException{
  return (NackaCohabitantImportFileHandler) super.createIBO();
 }



}