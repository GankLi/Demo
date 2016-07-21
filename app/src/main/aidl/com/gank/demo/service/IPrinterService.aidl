package com.gank.demo.service;

/**
 * @author Gank
 * @versionCode 1
 */
interface IPrinterService {

    	/**
	 *
	 * @param data
	 * @param start
	 * @param offset
	 */
    void postPrintData(in byte[] data, int onset, int offset);

}

