/*
 * Created on 2007-7-5
 * Copyright (c) 2002-2007 Cobo Education & Training Co., Ltd
 * $Header$
 */
package client;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Chris
 * @version $Revision$
 */
public class Base64Utils {
    private static final Log log = LogFactory.getLog(Base64Utils.class);
    public final static String ENCONDING = "UTF-8";

    public static String encode(byte[] str) {
        return new Base64(0, "".getBytes(), false).encodeToString(str);
    }

    public static byte[] decode(String str) throws Exception {
        return new Base64(0, "".getBytes(), false).decode(str);
    }

    public static String encodeString(String s) throws Exception {
        return new Base64(0, "".getBytes(), false).encodeToString(s
                .getBytes(ENCONDING));
    }

    public static String decodeString(String str) throws Exception {
        return new String(new Base64(0, "".getBytes(), false).decode(str),
                ENCONDING);
    }

    public static String encodeUrlSafe(byte[] str) {
        return new Base64(0, "".getBytes(), true).encodeToString(str);
    }

    public static byte[] decodeUrlSafe(String str) throws Exception {
        return new Base64(0, "".getBytes(), true).decode(str);
    }

    public static String encodeStringUrlSafe(String s) throws Exception {
        return new Base64(0, "".getBytes(), true).encodeToString(s
                .getBytes(ENCONDING));
    }

    public static String decodeStringUrlSafe(String str) throws Exception {
        return new String(new Base64(0, "".getBytes(), true).decode(str),
                ENCONDING);
    }

    public static String encryptToBase64(String filePath) {
        if (filePath == null) {
            return null;
        }
        try {
            byte[] b = Files.readAllBytes(Paths.get(filePath));
            return Base64.encodeBase64String(b);
        } catch (IOException e) {
             log.error(e);
        }

        return null;
    }

}
