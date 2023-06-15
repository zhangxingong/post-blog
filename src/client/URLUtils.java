/*
 * Created on 2004-9-16
 * Copyright (c) 2002-2004 Cobo Education & Training Co., Ltd
 * $Header: /var/cvsroot/repository/HDMS3/src/com/cobocn/hdms/utils/URLUtils.java,v 1.4.2.2 2005-06-24 09:56:50 qgyang Exp $
 */
package client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author qinguo for we can not know what version of JDK that our program run.
 *         so we need to make the URLDecoder works fine in both.
 */
public class URLUtils {

	private static Log log = LogFactory.getLog(URLUtils.class);

	public final static String ENCONDING = "UTF-8";

	public static String decode(String orig) {
		return decode(orig, ENCONDING);
	}

	public static String decode(String orig, String encoding) {
		if (orig == null || encoding == null)
			return "";
		String s = "";
		try {
			s = URLDecoder.decode(orig, encoding);
		} catch (Exception e) {
			log.error("Error", e);
		}
		return s;
	}

	public static String encode(String orig, String encoding) {
		String r = "";
		try {
			r = URLEncoder.encode(orig, encoding);
		} catch (Exception e) {
			log.error("Error", e);
		}
		return r;
	}

	public static String encode(String orig) {
		return encode(orig, ENCONDING);
	}

}