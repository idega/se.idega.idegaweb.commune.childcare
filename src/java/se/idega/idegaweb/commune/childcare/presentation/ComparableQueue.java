package se.idega.idegaweb.commune.childcare.presentation;

import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;

/**
 * This class is used to sort ChildCareQueue object according to their
 * choice number and granted status.
 * @author <a href="mailto:joakim@idega.is">joakim</a>
 * @version $Id: ComparableQueue.java,v 1.5 2006/04/09 11:45:19 laddi Exp $
 * @since 12.2.2003 
 */
class ComparableQueue implements Comparable {
	private ChildCareQueue _queue;
	private boolean _grantedFirst;
				
	ComparableQueue(Object queue, boolean grantedFirst){
		this._queue = (ChildCareQueue) queue;
		this._grantedFirst = grantedFirst;
	}
		
	ChildCareQueue getQueue(){
		return this._queue;
	}

	/**
	 * Compareing two granted queues will give different result
	 * depending on which queue is used as 'comparator' and parameter.
	 * This situation should never happen though, and the order of granted
	 * queues is not important. Two granted queues will never be
	 * equal.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object queue){
		ChildCareQueue que = ((ComparableQueue) queue).getQueue();
		int diff = this._queue.getChoiceNumber() - que.getChoiceNumber();
		
		if (this._grantedFirst){
			if (this._queue.getStatus().equals(ChildCareQueueUpdateTable.STATUS_UBEH)){
				return  -1;
			} else if (que.getStatus().equals(ChildCareQueueUpdateTable.STATUS_UBEH)){
				return  1;
			} else {
				return diff;
			}
		} else {
			if (diff == 0 && this._queue.getStatus().equals(ChildCareQueueUpdateTable.STATUS_UBEH)){
				return  -1;
			} else if (diff == 0 && que.getStatus().equals(ChildCareQueueUpdateTable.STATUS_UBEH)){
				return  1;
			} else {
				return diff;
			}
		} 
	}
}