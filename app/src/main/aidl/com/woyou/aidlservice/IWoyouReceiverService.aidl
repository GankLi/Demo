package com.woyou.aidlservice;

/**
 * @author blanks
 * @versionCode 1
 */
interface IWoyouReceiverService {

    	/**
	 *
	 * @param data
	 * @param onset
	 * @param offset
	 */
    void postPrintData(in byte[] data, int onset, int offset);

}
