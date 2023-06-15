/*
 * Created on 2004-7-13
 * Copyright (c) 2002-2004 Cobo Education & Training Co., Ltd
 * $Header: /var/cvsroot/repository/HDMS3/src/com/cobocn/hdms/utils/FileUtils.java,v 1.4.10.9 2010-01-31 14:50:43 hyu Exp $
 */
package client;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Chris
 * @version $Revision: 1.4.10.9 $
 */
public class FileUtils {

	public static final long ONE_KB = 1024;

	public static final long ONE_MB = ONE_KB * ONE_KB;

	public static final long ONE_GB = ONE_KB * ONE_MB;

	public static String FILETYPE_OFFICE = "OFFICE";
	public static String FILETYPE_ZIP = "ZIP";
	public static String FILETYPE_IMAGE = "IMAGE";

	public static String FILETYPE_WORD = "WORD";
	public static String FILETYPE_EXCEL = "EXCEL";

	public static String FILETYPE_PDF = "PDF";
	public static String FILETYPE_PPT = "PPT";
	public static String FILETYPE_TXT = "TXT";
	public static String FILETYPE_VIDEO = "VIDEO";
	public static String FILETYPE_AUDIO = "AUDIO";

	private static Collection<String> suffixsAbbr;

	public static String[] readLinesFromFile(String fileName)
			throws IOException {
		RandomAccessFile raf = new RandomAccessFile(fileName, "r");
		Vector v = new Vector();
		String l = null;
		while ((l = raf.readLine()) != null) {
			v.add(l);
		}
		raf.close();

		String[] lines = new String[v.size()];

		for (int i = 0; i < lines.length; i++) {
			lines[i] = (String) v.elementAt(i);
		}

		return lines;
	}

	public static String[] readLinesFromFile(String fileName, String encoding)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName), encoding));
		Vector v = new Vector();
		String l = null;
		while ((l = in.readLine()) != null) {
			v.add(l);
		}
		in.close();

		String[] lines = new String[v.size()];

		for (int i = 0; i < lines.length; i++) {
			lines[i] = (String) v.elementAt(i);
		}

		return lines;
	}

	public static String[] readLinesFromFileUTF8(String fileName)
			throws IOException {
		return readLinesFromFile(fileName, "UTF-8");
	}

	public static String readStringFromFile(String fileName) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(fileName, "r");
		StringBuffer sb = new StringBuffer();
		String l = null;
		while ((l = raf.readLine()) != null) {
			sb.append(l).append("\n");
		}
		raf.close();
		return sb.toString();
	}

	public static String readStringFromFileUTF8(String fileName)
			throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(fileName), "UTF-8"));

		StringBuffer sb = new StringBuffer();
		String l = null;
		while ((l = in.readLine()) != null) {
			sb.append(l).append("\n");
		}
		in.close();
		return sb.toString();
	}

	public static void writeStringAsFile(String content, String fileName)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
		out.write(content);
		out.close();
	}

	public static void writeStringAsFileUTF8(String content, String fileName)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName), "UTF-8"));
		out.write(content);
		out.close();
	}

	public static void writeStringAsFile(String content, String fileName,String encoding)
			throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName), encoding));
		out.write(content);
		out.close();
	}

	/**
	 * @param string
	 * @return
	 */
	public static String getBaseName(String fileName) {
		if (fileName == null)
			return null;
		fileName = StringUtils.replace(fileName, "\\", "/");
		int i = fileName.lastIndexOf("/");
		if (i > 0)
			fileName = fileName.substring(i + 1);
		return fileName;
	}


	public static String toDisplaySize(long size) {
		String displaySize;

		if (size / ONE_GB > 10) {
			displaySize = String.valueOf(size / ONE_GB) + " GB";
		} else if (size * 1.0 / ONE_MB >= 1) {
			displaySize = NumUtils.formatNumber(size * 1.0 / ONE_MB, 1) + " MB";
		} else if (size / ONE_KB > 1) {
			displaySize = String.valueOf(size / ONE_KB) + " KB";
		} else {
			displaySize = String.valueOf(size) + " B";
		}
		return displaySize;
	}

	public static Collection<String> getSuffixAbbrs() {
		if (suffixsAbbr == null || true) {
			suffixsAbbr = new ArrayList<String>();
			suffixsAbbr.add(FILETYPE_WORD);
			suffixsAbbr.add(FILETYPE_EXCEL);
			suffixsAbbr.add(FILETYPE_PDF);
			suffixsAbbr.add(FILETYPE_PPT);
			suffixsAbbr.add(FILETYPE_TXT);
			suffixsAbbr.add(FILETYPE_ZIP);
			suffixsAbbr.add(FILETYPE_IMAGE);
			suffixsAbbr.add(FILETYPE_OFFICE);
			suffixsAbbr.add(FILETYPE_VIDEO);
			suffixsAbbr.add(FILETYPE_AUDIO);
		}
		return suffixsAbbr;
	}


	public static String convertFileName(String origFileName) {
		String fileName = StringUtils.shortUUID();
		int dotInx = origFileName.lastIndexOf(".");
		if (dotInx > 0) {
			String suffix = origFileName.substring(
					origFileName.lastIndexOf(".")).toLowerCase();
			fileName += suffix;
		} else {
			fileName += ".dat";
		}
		return fileName;
	}

    public static String getFileSuffix(String fileName) {
        if (StringUtils.isValid(fileName) && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }


    /**
     * 生成随机文件名，当前年月日小时分钟秒+五位随机数
     *
     * @param args
     * @throws IOException
     */
    public static String getRandomFileName() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Random r = new Random();
        // 获取随机五位数
        int rannum = r.nextInt(89999) + 10000;
        // 当前时间
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }


    /**
     * 目标路径文件夹不存在就创建
     *
     * @param args
     * @throws IOException
     */
    public static void makeDirPath(String targetAddr) {
        // 文件全路径
        String realFileParentPath = targetAddr;
        File dirPath = new File(realFileParentPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

    }

}