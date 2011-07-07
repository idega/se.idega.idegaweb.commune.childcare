package se.idega.idegaweb.commune.childcare.importer;


public class NackaAfterSchoolPlacementImportFileHandlerHomeImpl extends com.idega.business.IBOHomeImpl implements NackaAfterSchoolPlacementImportFileHandlerHome
{
 @Override
protected Class getBeanInterfaceClass(){
  return NackaAfterSchoolPlacementImportFileHandler.class;
 }


 @Override
public NackaAfterSchoolPlacementImportFileHandler create() throws javax.ejb.CreateException{
  return (NackaAfterSchoolPlacementImportFileHandler) super.createIBO();
 }



}