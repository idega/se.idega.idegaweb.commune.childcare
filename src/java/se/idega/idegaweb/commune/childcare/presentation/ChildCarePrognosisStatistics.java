/*
 * Created on 25.3.2003
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import se.idega.idegaweb.commune.childcare.business.ProviderStat;

import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.CloseButton;
import com.idega.util.IWTimestamp;

/**
 * @author laddi
 */
public class ChildCarePrognosisStatistics extends ChildCareBlock {
    
    

    /**
     * 
     */
    public ChildCarePrognosisStatistics() {
        // cache for 30 minutes
       // setCacheable(getCacheKey(),30*60*1000);  //remove //
    }
    
    
    /* (non-Javadoc)
     * @see com.idega.presentation.Block#getCacheKey()
     */
    public String getCacheKey() {
        return "comm_cc_prv_stat";
    }
	/**
	 * @see se.idega.idegaweb.commune.childcare.presentation.ChildCareBlock#init(com.idega.presentation.IWContext)
	 */
	public void init(IWContext iwc) throws Exception {
		add(getProviderStatTable(iwc));
		add(new Break());
		
		CloseButton close = (CloseButton) getButton(new CloseButton(localize("close", "Close")));
		add(close);
	}

	/*private Table getProviderTable(IWContext iwc) throws RemoteException {
		Table table = getTable(7);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 2;
		int column = 1;

		table.add(getLocalizedSmallHeader("child_care.prognosis_3m","Prognosis (3M)"), 3, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_priority_3m","Priority (3M)"), 4, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_12m","Prognosis (12M)"), 5, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_priority_12m","Priority (12M)"), 6, 1);
		table.add(getLocalizedSmallHeader("child_care.last_updated","Last updated"), 7, 1);

		SchoolBusiness sb = getBusiness().getSchoolBusiness();
		
		Collection c = sb.findAllSchoolsByType(getBusiness().getSchoolBusiness().findAllSchoolTypesForChildCare());
		c = sb.getHomeCommuneSchools(c);		
		List providers = new Vector(c);
		if (providers != null && !providers.isEmpty()) {
			School school;
			ChildCarePrognosis prognosis;
			int providerID = -1;
			
			Collections.sort(providers, new SchoolComparator(iwc.getCurrentLocale()));
			Iterator iter = providers.iterator();
			while (iter.hasNext()) {
				column = 1;
				school = (School) iter.next();
				providerID = ((Integer)school.getPrimaryKey()).intValue();
				prognosis = getBusiness().getPrognosis(providerID);
				
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				table.add(getSmallText(school.getSchoolName()), column++, row);
				//table.add(getSmallText(String.valueOf(getBusiness().getQueueByProvider(providerID))), column++, row);
				table.add(getSmallText(String.valueOf(getBusiness().getQueueTotalByProvider(providerID))), column++, row);
				if (prognosis != null) {
					table.add(getSmallText(String.valueOf(prognosis.getThreeMonthsPrognosis())), column++, row);
					if (prognosis.getThreeMonthsPriority() != -1)
						table.add(getSmallText(String.valueOf(prognosis.getThreeMonthsPriority())), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					table.add(getSmallText(String.valueOf(prognosis.getOneYearPrognosis())), column++, row);
					if (prognosis.getOneYearPriority() != -1)
						table.add(getSmallText(String.valueOf(prognosis.getOneYearPriority())), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					table.add(getSmallText(new IWTimestamp(prognosis.getUpdatedDate()).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row++);
				}
				else {
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row++);
				}
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(7, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}*/
	
	private Table getProviderStatTable(IWContext iwc) throws RemoteException{
		Table table = getTable(10);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 2;
		int column = 3;

		boolean useVacancies = getBusiness().getUseVacancies();
		if (useVacancies)
			table.add(getLocalizedSmallHeader("child_care.total_vacancies","Vacancies"), column++, 1);
		
		table.add(getLocalizedSmallHeader("child_care.prognosis_3m","Prognosis (3M)"), column++, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_priority_3m","Priority (3M)"), column++, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_queue3months","Within (3M)"), column++, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_12m","Prognosis (12M)"), column++, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_priority_12m","Priority (12M)"), column++, 1);
		table.add(getLocalizedSmallHeader("child_care.prognosis_queue12months","Within (12M)"), column++, 1);
		table.add(getLocalizedSmallHeader("child_care.last_updated","Last updated"), column++, 1);

		column = 1; //set column back to 1 
		
		Collection stats = null;
        try {
            stats = getBusiness().getProviderStats(iwc.getCurrentLocale());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (FinderException e) {
            e.printStackTrace();
        }
        if (stats != null && !stats.isEmpty()) {
			
			Iterator iter = stats.iterator();
			ProviderStat stat;
			while (iter.hasNext()) {
				int queueWithin3Months = -1;
				int queueWithin12Months = -1;
			    stat = (ProviderStat)iter.next();
				column = 1;
				if (row % 2 == 0)
					table.setRowColor(row, getZebraColor1());
				else
					table.setRowColor(row, getZebraColor2());

				table.add(getSmallText(stat.getProviderName()), column++, row);
				//table.add(getSmallText(String.valueOf(getBusiness().getQueueByProvider(providerID))), column++, row);
				table.add(getSmallText(String.valueOf(stat.getQueueTotal())), column++, row);
				if (stat.hasPrognosis()) {
					if (useVacancies)
						table.add(getSmallText(String.valueOf(stat.getVacancies())), column++, row);
					table.add(getSmallText(String.valueOf(stat.getThreeMonthsPrognosis())), column++, row);
					if (stat.getThreeMonthsPriority().intValue() != -1)
						table.add(getSmallText(String.valueOf(stat.getThreeMonthsPriority())), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					
					queueWithin3Months = getBusiness().getQueueTotalByProviderWithinMonths(stat.getProviderID().intValue(), 3, false);
					
					table.add(getSmallText(String.valueOf(queueWithin3Months)), column++, row);
					
					table.add(getSmallText(String.valueOf(stat.getOneYearPrognosis())), column++, row);
					if (stat.getOneYearPriority().intValue() != -1)
						table.add(getSmallText(String.valueOf(stat.getOneYearPriority())), column++, row);
					else
						table.add(getSmallText("-"), column++, row);
					
					queueWithin12Months = getBusiness().getQueueTotalByProviderWithinMonths(stat.getProviderID().intValue(), 12, false);
					table.add(getSmallText(String.valueOf(queueWithin12Months)), column++, row);
					
					
					table.add(getSmallText(new IWTimestamp(stat.getLastUpdate()).getLocaleDate(iwc.getCurrentLocale(), IWTimestamp.SHORT)), column++, row++);
					
				}
				else {
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row);
					table.add(getSmallText("-"), column++, row++);
				}
			}
		}
		table.setColumnAlignment(2, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(3, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(5, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(6, Table.HORIZONTAL_ALIGN_CENTER);
		table.setColumnAlignment(7, Table.HORIZONTAL_ALIGN_CENTER);
		
		return table;
	}
	
	private Table getTable(int columns) {
		Table table = new Table();
		table.setCellpadding(getCellpadding());
		table.setCellspacing(getCellspacing());
		table.setColumns(columns);
		table.setRowColor(1, getHeaderColor());
		int row = 1;
		int column = 1;
		
		table.add(getLocalizedSmallHeader("child_care.name","Name"), column++, row);
		table.add(getLocalizedSmallHeader("child_care.queue_order","Queue order"), column++, row++);

		return table;
	}
}