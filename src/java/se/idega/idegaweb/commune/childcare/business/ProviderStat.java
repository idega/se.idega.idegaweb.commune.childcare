/*
 * $Id: ProviderStat.java,v 1.4 2006/04/09 11:45:18 laddi Exp $
 * Created on 8.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.util.Date;

/**
 * 
 *  Last modified: $Date: 2006/04/09 11:45:18 $ by $Author: laddi $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.4 $
 */
public class ProviderStat{
    
    private Integer providerID;
    private String providerName;
    private Integer prognosisID;
    private Date lastUpdate;
    private Integer threeMonthsPrognosis;
    private Integer oneYearPrognosis;
    private Integer threeMonthsPriority;
    private Integer oneYearPriority;
    private Integer providerCapacity;
    private Integer queueTotal;
    private Integer vacancies;
    private Boolean queueSortedByBirthdate;
   

    /**
     * @return Returns the lastUpdate.
     */
    public Date getLastUpdate() {
        return this.lastUpdate;
    }
    /**
     * @param lastUpdate The lastUpdate to set.
     */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    /**
     * @return Returns the oneYearPriority.
     */
    public Integer getOneYearPriority() {
        return this.oneYearPriority;
    }
    /**
     * @param oneYearPriority The oneYearPriority to set.
     */
    public void setOneYearPriority(Integer oneYearPriority) {
        this.oneYearPriority = oneYearPriority;
    }
    /**
     * @return Returns the oneYearPrognosis.
     */
    public Integer getOneYearPrognosis() {
        return this.oneYearPrognosis;
    }
    /**
     * @param oneYearPrognosis The oneYearPrognosis to set.
     */
    public void setOneYearPrognosis(Integer oneYearPrognosis) {
        this.oneYearPrognosis = oneYearPrognosis;
    }
    /**
     * @return Returns the prognosisID.
     */
    public Integer getPrognosisID() {
        return this.prognosisID;
    }
    /**
     * @param prognosisID The prognosisID to set.
     */
    public void setPrognosisID(Integer prognosisID) {
        this.prognosisID = prognosisID;
    }
    
    public boolean hasPrognosis(){
        return this.getPrognosisID()!=null && getPrognosisID().intValue()>0;
    }
    /**
     * @return Returns the providerCapacity.
     */
    public Integer getProviderCapacity() {
        return this.providerCapacity;
    }
    /**
     * @param providerCapacity The providerCapacity to set.
     */
    public void setProviderCapacity(Integer providerCapacity) {
        this.providerCapacity = providerCapacity;
    }
    /**
     * @return Returns the providerID.
     */
    public Integer getProviderID() {
        return this.providerID;
    }
    /**
     * @param providerID The providerID to set.
     */
    public void setProviderID(Integer providerID) {
        this.providerID = providerID;
    }
    /**
     * @return Returns the providerName.
     */
    public String getProviderName() {
        return this.providerName;
    }
    /**
     * @param providerName The providerName to set.
     */
    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    /**
     * @return Returns the threeMonthsPriority.
     */
    public Integer getThreeMonthsPriority() {
        return this.threeMonthsPriority;
    }
    /**
     * @param threeMonthsPriority The threeMonthsPriority to set.
     */
    public void setThreeMonthsPriority(Integer threeMonthsPriority) {
        this.threeMonthsPriority = threeMonthsPriority;
    }
    /**
     * @return Returns the threeMonthsPrognosis.
     */
    public Integer getThreeMonthsPrognosis() {
        return this.threeMonthsPrognosis;
    }
    /**
     * @param threeMonthsPrognosis The threeMonthsPrognosis to set.
     */
    public void setThreeMonthsPrognosis(Integer threeMonthsPrognosis) {
        this.threeMonthsPrognosis = threeMonthsPrognosis;
    }
    /**
     * @return Returns the queueTotal.
     */
    public Integer getQueueTotal() {
        return this.queueTotal;
    }
    /**
     * @param queueTotal The queueTotal to set.
     */
    public void setQueueTotal(Integer queueTotal) {
        this.queueTotal = queueTotal;
    }
    
    /**
     * @return Returns the vacancies.
     */
    public Integer getVacancies() {
    	return this.vacancies;
    }
    
    /**
     * @param vacancies The vacancies to set.
     */
    public void setVacancies(Integer vacancies) {
    	this.vacancies = vacancies;
    }
    
    
    public Boolean getQueueSortedByBirthdate() {
        return this.queueSortedByBirthdate;
    }
    public void setQueueSortedByBirthdate(Boolean queueSortedByBirthdate) {
        this.queueSortedByBirthdate = queueSortedByBirthdate;
    }
    public void setQueueSortedByBirthdate(String queueSortedByBirthdate) {
        if (queueSortedByBirthdate != null) {
            if (queueSortedByBirthdate.equalsIgnoreCase("Y")) {
                this.queueSortedByBirthdate = Boolean.TRUE;
                return;
            }
        }
        this.queueSortedByBirthdate = Boolean.FALSE;
    }
    
}
