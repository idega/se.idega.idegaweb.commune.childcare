package se.idega.idegaweb.commune.childcare.importer;


public class NackaPlacedChildImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements NackaPlacedChildImportFileHandlerHome
{
 @Override
protected Class getBeanInterfaceClass(){
  return NackaPlacedChildImportFileHandler.class;
 }


 @Override
public NackaPlacedChildImportFileHandler create() throws javax.ejb.CreateException{
  return (NackaPlacedChildImportFileHandler) super.createIBO();
 }



}