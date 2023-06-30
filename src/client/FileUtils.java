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
	public static String getAbbrSuffixes(String abbr) {
		if (FILETYPE_ZIP.equals(abbr)) {
			return "*.zip,*.rar,*.7z,*.gz";
		} else if (FILETYPE_IMAGE.equals(abbr)) {
			return "*.jpg,*.jpeg,*.png,*.gif,*.bmp,*.tif,*.heic";
		} else if (FILETYPE_WORD.equals(abbr)) {
			return "*.doc,*.docx";
		} else if (FILETYPE_EXCEL.equals(abbr)) {
			return "*.xls,*.xlsx";
		} else if (FILETYPE_PDF.equals(abbr)) {
			return "*.pdf";
		} else if (FILETYPE_TXT.equals(abbr)) {
			return "*.txt,*.csv";
		} else if (FILETYPE_PPT.equals(abbr)) {
			return "*.ppt,*.pptx";
		} else if (FILETYPE_VIDEO.equals(abbr)) {
			return "*.avi,*.mp4,*.mkv,*.wmv,*.mov";
		} else if (FILETYPE_AUDIO.equals(abbr)) {
			return "*.mp3,*.wav,*.wma,*.flac,*.ogg";
		} else if (FILETYPE_OFFICE.equals(abbr)) {
			return "*.doc,*.docx,*.xls,*.xlsx,*.ppt,*.pptx,*.wps,*.pdf,*.txt,*.csv";
		}
		return null;
	}

	public static String getExtractdSuffixs(String suffixs) {
		StringBuffer sb = new StringBuffer();
		boolean appended = false;
		if (StringUtils.isValid(suffixs)) {
			String[] sa = StringUtils.fuzzySplit(suffixs);
			for (int i = 0; i < sa.length; i++) {
				String s = sa[i];
				if (StringUtils.isValid(s)) {
					if (appended)
						sb.append(",");
					if (getSuffixAbbrs().contains(s)) {
						sb.append(getAbbrSuffixes(s));
					} else {
						sb.append(s);
					}
					appended = true;
				}
			}
		} else {
			sb.append("*.*");
		}
		return sb.toString();
	}

	public static boolean fileSuffixMatch(String fileName, String suffixList) {
		if (!StringUtils.isValid(suffixList)) {
			return true;
		}
		if (!StringUtils.isValid(fileName)) {
			return false;
		}
		String[] suffixes = StringUtils
				.fuzzySplit(getExtractdSuffixs(suffixList));
		for (int i = 0; i < suffixes.length; i++) {
			String suf = suffixes[i].trim();
			if (suf.equals("*.*")) {
				return true;
			}
			if (suf.startsWith("*.")) {
				suf = suf.substring(2);
			}
			if (fileName.toUpperCase().endsWith(suf.toUpperCase())) {
				return true;
			}
		}
		return false;
	}


	public static boolean isImageFile(String fileName) {
		return fileSuffixMatch(fileName, getAbbrSuffixes(FILETYPE_IMAGE));
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