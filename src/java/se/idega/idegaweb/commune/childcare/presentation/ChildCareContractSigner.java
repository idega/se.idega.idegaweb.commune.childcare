/*
 * Created on 21.5.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.util.Collection;
import java.util.Iterator;



import se.idega.block.pki.data.NBSSignedEntity;
import se.idega.block.pki.presentation.NBSSigningBlock;

import com.idega.block.contract.business.ContractBusiness;
import com.idega.block.contract.business.ContractFinder;
import com.idega.block.contract.data.Contract;
import com.idega.block.contract.data.ContractCategory;
import com.idega.builder.data.IBPage;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;

import com.idega.presentation.text.Link;

import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.util.IWTimestamp;


/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ChildCareContractSigner extends Block implements Builderaware{
	
	public final static String PAR_CONTRACT_ID = "ChildCareContractSigner_CONTRACT_ID";
	
	private final static String IW_BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	private final static String PAR_CONTRACT_TEXT = "CONTRACT_TEXT";
	
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
		
	public ChildCareContractSigner() {
		super();
	}	
		
	public void main(IWContext iwc) {
		try{
			control(iwc);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
	
	private static String ACTION = "se.idega.idegaweb.commune.childcare.presentation.ChildCareContractSigner.ACTION";
	private static String ACTION_SIGN = "SIGN";
	private static String ACTION_CREATE = "CREATE";
		
	private void control(IWContext iwc) throws Exception{
		
		
		System.out.println("control()");	
		
		String action = iwc.getParameter(ACTION);
		
		System.out.println("ChildCareContract.Signing.ACTION: " + action);
		
		if (action != null && action.equals(ACTION_SIGN)){
			
			System.out.println("Forwarding");	
			int contractId = Integer.parseInt(iwc.getParameter(PAR_CONTRACT_ID));
			
			Contract contract =
				((com.idega.block.contract.data.ContractHome) com.idega.data.IDOLookup.getHome(Contract.class))
					.findByPrimaryKey(contractId);			
			
			iwc.setSessionAttribute(NBSSigningBlock.NBS_SIGNED_ENTITY, 
				new NBSSignedEntity() {
					private Contract _contract = null;
					
					public Object init(Contract contract){
						_contract = contract;
						return this;
					}
					
					public void setXmlSignedData(String data) {
						_contract.setXmlSignedData(data);
					}
	
					public void setSignedBy(int userId) {
						//_contract.setUserId(userId); //This shall already be set 
					}
	
					public void setSignedDate(java.sql.Date time) {
						_contract.setSignedDate(time);
					}
	
					public void setSignedFlag(boolean flag) {
						_contract.setSignedFlag(new Boolean(flag));												
					}
	
					public void store() {
						_contract.store();
					}
	
					public String getText() {
						return _contract.getText();
					}
				}
				.init(contract)
			);
					
//			iwc.removeSessionAttribute(NBSSigningBlock.INIT_DONE);		
			add(new HiddenInput(ACTION, ""));
				
			iwc.forwardToIBPage(getParentPage(), getResponsePage());		
			
			
			
		} else if (action != null && action.equals(ACTION_CREATE)){
			System.out.println("");
			String text = iwc.getParameter(PAR_CONTRACT_TEXT);
			System.out.println("Insert text: " + text);
			ContractBusiness.createContract(iwc.getCurrentUser().getID(), 1, IWTimestamp.RightNow(), IWTimestamp.RightNow(), "C", text);
			add(createForm(iwc));			
			
		} else {
			add(createForm(iwc));
		}
		System.out.println("control() done");	
	}
	
	private Form createForm(IWContext iwc){
		Form f = new Form();
		f.add(makeTableOfContracts(iwc));
	
		return f;
	}



	private Table makeTableOfContracts(IWContext iwc) {
		System.out.println("makeTableOfContracts()");			
		
		Collection contracts = ContractFinder.findContractsByUserId(iwc.getCurrentUser().getID());
		
		
		if (contracts == null){
			return new Table();
		}
		
		Iterator i = contracts.iterator();
		
		Table t = new Table(3, contracts.size());
		t.setCellpadding(2);
		t.setCellspacing(2);	
		t.setBorder(0);	
		
		int row = 1;
		
//		t.add(new Text("Text"), 1, 1);
//		t.add(new Text("Text"), 1, 1);
//		t.add(new Text("Text"), 1, 1);
		IWResourceBundle iwrb = getResourceBundle(iwc);		
		
		while (i.hasNext()){
			Contract contract = (Contract) i.next();

			System.out.println("Contract.id: " + contract.getID());
			
			String text = contract.getText();
			if (text == null){
				text = " ";
			}
			
			ContractCategory cat = ContractBusiness.findCategory(contract.getCategoryId().intValue());
			
			t.add(new Text(cat.getName()), 1, row);
						
			if (contract.isSigned()) {
				t.add(new Text(iwrb.getLocalizedString("ccconsign_signed", "Signed") + contract.getSignedDate()), 3, row);
			}else {

				Link signBtn = new Link(iwrb.getLocalizedString("ccconsign_signcon","Sign Contract"));
				signBtn.setAsImageButton(true);
				signBtn.setParameter(PAR_CONTRACT_ID, ""+contract.getID());
				signBtn.setParameter(ACTION, ACTION_SIGN);
				t.add(signBtn, 2, row);				
			}
				
//			t.add(new Text(""+contract.getID()), 2, row);

//			t.add(new Text(escapeHTML(contract.getXmlSignedData())), 3, row);

			
			row ++;
		}
		setStyle(this, "font-size:10px");		
		setStyle(this, "font-family: sans-serif");
		return t;
	}

	private static IBPage _page;	

	public void setResponsePage(IBPage page){
		System.out.println("******* S E T T I N G   Page: " + page.getName());		
		_page = page;
	}
	
	public IBPage getResponsePage(){
		return _page;
	}
	
	public static final String escapeHTML(String s){
		if (s == null){
			return "";
		}
		
	   StringBuffer sb = new StringBuffer();
	   int n = s.length();
	   for (int i = 0; i < n; i++) {
		  char c = s.charAt(i);
		  switch (c) {
			 case '<': sb.append("&lt;"); break;
			 case '>': sb.append("&gt;"); break;
			 case '&': sb.append("&amp;"); break;
			 case '"': sb.append("&quot;"); break;
			 case '�': sb.append("&agrave;");break;
			 case '�': sb.append("&Agrave;");break;
			 case '�': sb.append("&acirc;");break;
			 case '�': sb.append("&Acirc;");break;
			 case '�': sb.append("&auml;");break;
			 case '�': sb.append("&Auml;");break;
			 case '�': sb.append("&aring;");break;
			 case '�': sb.append("&Aring;");break;
			 case '�': sb.append("&aelig;");break;
			 case '�': sb.append("&AElig;");break;
			 case '�': sb.append("&ccedil;");break;
			 case '�': sb.append("&Ccedil;");break;
			 case '�': sb.append("&eacute;");break;
			 case '�': sb.append("&Eacute;");break;
			 case '�': sb.append("&egrave;");break;
			 case '�': sb.append("&Egrave;");break;
			 case '�': sb.append("&ecirc;");break;
			 case '�': sb.append("&Ecirc;");break;
			 case '�': sb.append("&euml;");break;
			 case '�': sb.append("&Euml;");break;
			 case '�': sb.append("&iuml;");break;
			 case '�': sb.append("&Iuml;");break;
			 case '�': sb.append("&ocirc;");break;
			 case '�': sb.append("&Ocirc;");break;
			 case '�': sb.append("&ouml;");break;
			 case '�': sb.append("&Ouml;");break;
			 case '�': sb.append("&oslash;");break;
			 case '�': sb.append("&Oslash;");break;
			 case '�': sb.append("&szlig;");break;
			 case '�': sb.append("&ugrave;");break;
			 case '�': sb.append("&Ugrave;");break;         
			 case '�': sb.append("&ucirc;");break;         
			 case '�': sb.append("&Ucirc;");break;
			 case '�': sb.append("&uuml;");break;
			 case '�': sb.append("&Uuml;");break;
			 case '�': sb.append("&reg;");break;         
			 case '�': sb.append("&copy;");break;   
			 case '�': sb.append("&euro;"); break;
			 // be carefull with this one (non-breaking whitee space)
			 case ' ': sb.append("&nbsp;");break;         
         
			 default:  sb.append(c); break;
		  }
	   }
	   return sb.toString();
	}	
	
}
