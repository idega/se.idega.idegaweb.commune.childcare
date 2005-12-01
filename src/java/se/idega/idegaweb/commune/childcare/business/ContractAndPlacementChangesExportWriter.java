package se.idega.idegaweb.commune.childcare.business;

import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import se.idega.idegaweb.commune.care.data.ChildCareContract;
import se.idega.idegaweb.commune.care.data.ChildCareContractHome;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.block.school.data.SchoolClassMemberHome;
import com.idega.block.school.data.SchoolClassMemberLog;
import com.idega.block.school.data.SchoolClassMemberLogHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.core.file.data.ICFile;
import com.idega.core.file.data.ICFileHome;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

public class ContractAndPlacementChangesExportWriter {
	public static final String SEPARATOR = ";";
	
	IWContext iwc;
	
	public boolean createExportFile(IWContext iwc, ICFile folder) {
		
		this.iwc = iwc;
		
		Date firstDate = getFirstDateOfCurrentMonth();
		Date lastDate = getLastDateOfCurrentMonth();		
		
		try {
			MemoryFileBuffer buffer = new MemoryFileBuffer();
			MemoryOutputStream mos = new MemoryOutputStream(buffer);
			
			//XXX: data gathered here
			//lets get just first 1000 school class members for now
			//later this is going to business, now it's here
			SchoolClassMemberHome memberHome = null;	            
			memberHome = (SchoolClassMemberHome)IDOLookup.getHome(SchoolClassMember.class);		            
	        Collection collection =  memberHome.findAll();
			
	        if (collection.size() > 0) {
	        	Iterator iter = collection.iterator();
	        	while (iter.hasNext()) {
	        		// XXX use stringbuffer
	        		SchoolClassMember member = (SchoolClassMember) iter.next();	       		
	        		
	        		//sch_school.EXTRA_PROVIDER_ID
	        		String sss = member.getSchoolClass().getSchool().getName();
	        		sss += SEPARATOR;	        		
	        		//sch_school_class.GROUP_STRING_ID
	        		sss += nullToEmpty(member.getSchoolClass().getGroupStringId()); 
	        		sss += SEPARATOR;
	        		//ic_user.PERSONAL_ID
	        		sss += " ";
	        		sss += member.getStudent().getPersonalID();  //TODO: formatting 
	        		sss += SEPARATOR;	        		
	        		//sch_class_member_log	START_DATE
	        		sss += " ";
	        		sss += (new IWTimestamp(firstDate)).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT);
	        		sss += SEPARATOR;	        		
	        		//sch_class_member_log	END_DATE
	        		sss += " ";
	        		sss += (new IWTimestamp(lastDate)).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT);
	        		
	        		sss += SEPARATOR;	        		
	        		//comm_childcare_archive	CARE_TIME_STRING
	        		sss += "care_time_string";
	        		sss += SEPARATOR;	        		
	        		//sch_school_type.TYPE_STRING_ID
	        		sss += nullToEmpty(member.getSchoolType().getTypeStringId());
	        		sss += SEPARATOR;	        		
	        		sss+= "\r\n";
	        		mos.write(sss.getBytes("UTF8")); //XXX: hmm...   
	        	}
	        }
			
			buffer.setMimeType("text/plain"); 
			InputStream mis = new MemoryInputStream(buffer);
	
			ICFileHome icFileHome = (ICFileHome) IDOLookup.getHome(ICFile.class);
			ICFile file = icFileHome.create();
			file.setFileValue(mis);
			file.setMimeType("text/plain");
			
			IWTimestamp toDate = new IWTimestamp();
			String s = toDate.toString().replaceAll(":", ""); // on windows filenames cannot contain colons
			
			file.setName("export " + s + ".txt");			
			file.setFileSize(buffer.length());
			file.store();
			
			mos.close();
			mis.close();
			
			folder.addChild(file);
			
			return true;
			
			
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//XXX: get first and last date of current month
	
	private Date getFirstDateOfCurrentMonth() {		
	    Calendar cal = roundCalendarToDate(new GregorianCalendar());
	        
	    int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH); // It will be 1	    
	    cal.set(Calendar.DAY_OF_MONTH, firstDay);
	    
	    return new Date(cal.getTimeInMillis());
	}	

	private Date getLastDateOfCurrentMonth() {		
	    Calendar cal = roundCalendarToDate(new GregorianCalendar());	  
	        
	    int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);	    
	    cal.set(Calendar.DAY_OF_MONTH, days);

	    return new Date(cal.getTimeInMillis());
	}
	
	private Calendar roundCalendarToDate(Calendar cal){
	    cal.set(Calendar.HOUR, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    return cal;
	}	

	private Date timestampToRoundedDate(Timestamp stamp) {
		if (stamp == null) return null;
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(stamp.getTime());
		return roundCalendarToDate(cal).getTime();
		
	}
	
	private Date roundDate(Date date) {
		if (date == null) return null;
		
		Calendar cal = new GregorianCalendar();
		cal.setTimeInMillis(date.getTime());
		return roundCalendarToDate(cal).getTime();
	}
	
	private String nullToEmpty(String s) {
		if(s == null) {
			return "";
		}
		else {
			return "";
		}
	}
	
	private void testing()  {
		Date firstDateOfCurrentMonth = this.getFirstDateOfCurrentMonth();
		Date lastDateOfCurrentMonth = this.getLastDateOfCurrentMonth();
		
		try {
			//get all placements
			//retrieve the placements we need
			//... and add them to the ubercolection
			Iterator placementsIterator =  getSchoolClassMemberHome().findAllByCategory(getSchoolBusiness().getCategoryChildcare()).iterator();
			for(; placementsIterator.hasNext();) {
				SchoolClassMember placement = (SchoolClassMember) placementsIterator.next();
				
				//now, when will we take this placement?
//				2.	IF it’s a new placement where sch_class_member.register_date >= 1st of current month 
//					AND sch_class_member.register_date <= last date of current month
				Date registerDate = timestampToRoundedDate(placement.getRegisterDate()); //java.sql.Timestamp; and only contains date
				if (registerDate.compareTo(firstDateOfCurrentMonth) < 0 || registerDate.compareTo(lastDateOfCurrentMonth) > 0) {
					continue;
				}
				
//				3.	IF it’s a placement that ends (has removed_date set in sch_class_member) and 
//					sch_class_member.removed date <= last date of current month
				Date removedDate = timestampToRoundedDate(placement.getRemovedDate()); //java.sql.Timestamp; contains date and time
				if (removedDate != null && (registerDate.compareTo(firstDateOfCurrentMonth) < 0 || registerDate.compareTo(lastDateOfCurrentMonth) > 0) ) {
					continue;
				}
				
				Iterator placementLogs = getSchoolClassMemberLogHome().findAllBySchoolClassMember(placement).iterator();
				while (placementLogs.hasNext()){
					SchoolClassMemberLog log = (SchoolClassMemberLog) placementLogs.next();
					
					Date startDate = log.getStartDate(); //java.util.Date, contains date only
					Date endDate = log.getEndDate(); //java.util.Date, contains date only
					
					if (registerDate.equals(startDate)) {
						//placement started
						// add and continue
					}
					
					if (removedDate != null && removedDate.equals(endDate)){
						//placement ended
//						 add and continue
					}
					

//					4.	IF a group has been changed (new entry in sch_class_member_log with 
//					sch_class_member_log.start date >= 1st of current month AND 
//					sch_class_member_log.start date <= last date of current month)	
					
					if (startDate.compareTo(firstDateOfCurrentMonth)>= 0) {
//						none of the above, so group was changed
						//add 
						
					}			
					
				}
				
			}
			
			//get all contracts
			//retrieve the contracts we need
			//... and add them to the ubercollection
			
			//btw, table comm_childcare_archive = ChildCareContractBMPBean
			Iterator contractsIterator = getChildCareContractHome().findChangedBetween(
					new java.sql.Date(firstDateOfCurrentMonth.getTime()), new java.sql.Date(lastDateOfCurrentMonth.getTime())).iterator(); //TODO: check, if this date conversion is okay.
			
			
			while (contractsIterator.hasNext()) {
				ChildCareContract contract = (ChildCareContract) contractsIterator.next();
				
//				5.	IF a caretime has been changed (new entry in comm_childcare_archive with 
//					comm_childcare_archive.start date >=1st of current month AND comm_childcare_archive.start date <= last date of current month)
				Date validFromDate = contract.getValidFromDate(); //java.util.Date , only date
				
				if (validFromDate.compareTo(firstDateOfCurrentMonth) >= 0 || validFromDate.compareTo(lastDateOfCurrentMonth) <= 0) {
					//add
					SchoolClassMember member = contract.getSchoolClassMember(); 
					member.getSchoolClass().getSchool().getExtraProviderId();
					member.getSchoolClass().getGroupStringId();
					member.getStudent().getPersonalID();
					contract.getValidFromDate();
					contract.getTerminatedDate();
					contract.getCareTime(); // it's okay and actually returns care_time_string
					member.getSchoolType().getTypeStringId();
					
				}
				
				
			}

						
			
			
			//reshuffle the ubercollection so it is in correct order
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	private SchoolClassMemberHome getSchoolClassMemberHome() throws IDOLookupException {
		return (SchoolClassMemberHome) IDOLookup.getHome(SchoolClassMember.class);
	}
	
	private SchoolClassMemberLogHome getSchoolClassMemberLogHome() throws IDOLookupException {
		return (SchoolClassMemberLogHome) IDOLookup.getHome(SchoolClassMember.class);
	}	

	private SchoolBusiness getSchoolBusiness() throws IBOLookupException{
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);   
	}
	
	private ChildCareContractHome getChildCareContractHome() throws IDOLookupException {
		return (ChildCareContractHome) IDOLookup.getHome(ChildCareContract.class);
	}
	
	private class DataForExport implements Comparable {
		int aaa;

		public int compareTo(Object arg0) {  //must return > 0 if argument is smaller
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
}
