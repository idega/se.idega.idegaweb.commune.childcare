/*
 * Created on 7.3.2004
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.user.data.User;
import com.idega.user.util.Converter;
import com.idega.util.timer.PastDateException;
import com.idega.util.timer.TimerEntry;
import com.idega.util.timer.TimerListener;
import com.idega.util.timer.TimerManager;


/**
 * @author laddi
 */
public class ChildCareBundleStarter implements IWBundleStartable {

	public IWBundle _bundle;
	private TimerManager timerManager;
	private TimerEntry queueTimerEntry;
	
	private static final String BUNDLE_PROPERTY_NAME_QUEUE_INTERVAL = "child_care_queue_interval";

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		System.out.println("[CHILD CARE]: Child care bundle starter starting...");
    this._bundle = starterBundle;
		int queueInterval = Integer.parseInt(this._bundle.getProperty(BUNDLE_PROPERTY_NAME_QUEUE_INTERVAL, String.valueOf(1440)));
		System.out.println("[CHILD CARE]: Queue interval = " + queueInterval + "minutes");
		
		if (this.timerManager==null) {
			this.timerManager = new TimerManager();
		}
		
		if(this.queueTimerEntry==null) {
			try {
				this.queueTimerEntry = this.timerManager.addTimer(queueInterval, true, new TimerListener() {
					public void handleTimer(TimerEntry entry) {
			      ChildCareBusiness business = getChildCareBusiness(ChildCareBundleStarter.this._bundle.getApplication().getIWApplicationContext());
			      User performer = null;
			      try {
			      	performer = Converter.convertToNewUser(ChildCareBundleStarter.this._bundle.getApplication().getAccessController().getAdministratorUser());
			      }
			      catch (Exception e) {
			      	e.printStackTrace();
			      }
			      
			      try {
				      business.removePendingFromQueue(performer);
				      System.out.println("[CHILD CARE]: Removing timed out applications from queue...");
			      }
			      catch (RemoteException re) {
			      	re.printStackTrace();
			      }
					}
				});
			}
			catch(PastDateException e) {
				this.queueTimerEntry = null;
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
		if(this.timerManager!=null) {
			if (this.queueTimerEntry != null) {
				this.timerManager.removeTimer(this.queueTimerEntry);
				this.queueTimerEntry = null;
			}
		}
	}

	public ChildCareBusiness getChildCareBusiness(IWApplicationContext iwac) {
		try {
			return (ChildCareBusiness) IBOLookup.getServiceInstance(iwac, ChildCareBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}