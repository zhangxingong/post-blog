/*
 * Created on 2004-8-5
 * Copyright (c) 2002-2004 Cobo Education & Training Co., Ltd
 * $Header: /var/cvsroot/repository/HDMS3/src/com/cobocn/hdms/utils/ReflectUtils.java,v 1.7 2005-03-07 10:42:21 hyu Exp $
 */
package client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *
 * @version $Revision: 1.7 $
 * @author Chris
 *
 */
public class ReflectUtils {

	private final static Log log = LogFactory.getLog(ReflectUtils.class);

	public static Object invokeMethod(String classMethodName) {
		return invokeMethod(classMethodName, null, null);
	}

	/**
	 * a method string is a string formatted like this:
	 * class.methodName,strParam1,strParam2 and first element is method name,
	 * secod is 1st argument, third is 2rd argument (String only)
     *
     * @param methodStr
     * @return
	 */
	public static Object invokeMethodStr(String methodStr) {
		String[] strs = StringUtils.split(methodStr, ",");
		if (strs.length == 0)
			return null;
		if (strs.length == 1)
			return invokeMethod(strs[0]);
		String classMethodName = strs[0];
		Class[] argTypes = new Class[strs.length - 1];
		Object[] args = new Object[strs.length - 1];
		for (int i = 1; i < strs.length; i++) {
			if (strs[i].trim().length() > 0) {
				argTypes[i - 1] = strs[i].getClass();
				args[i - 1] = strs[i];
			}
		}
		return invokeMethod(classMethodName, argTypes, args);
	}

	public static Object invokeMethod(String classMethodName, Class[] argTypes,
			Object[] args) {
		if (argTypes == null) {
			argTypes = new Class[0];
		}
		if (args == null) {
			args = new Object[0];
		}
		try {
			int idx = classMethodName.lastIndexOf(".");
			String className = classMethodName.substring(0, idx);
			String methodName = classMethodName.substring(idx + 1);
			Object o = Class.forName(className).newInstance();
			Method m = o.getClass().getMethod(methodName, argTypes);
			return m.invoke(o, args);
		} catch (Exception e) {
			log.error("Exception here", e);
		}
		return null;
	}

	public static Object invokeMethod(Object obj, String methodName) {
		return invokeMethod(obj, methodName, null, null);
	}

	public static Object invokeMethod(Object obj, String methodName,
			Class[] argTypes, Object[] args) {

		if (methodName.indexOf(".") != -1)
			return invokeMethodByProperty(obj, methodName);

		if (argTypes == null) {
			argTypes = new Class[0];
		}
		if (args == null) {
			args = new Object[0];
		}
		try {
			Method m = obj.getClass().getMethod(methodName, argTypes);
			return m.invoke(obj, args);
		} catch (Exception e) {
			if (obj != null && methodName != null) {
				log.error("Exception when invokeMethod : "
						+ obj.getClass().getName() + methodName, e);
			} else if (obj != null) {
				log.error("Exception when invokeMethod : obj is "
						+ obj.getClass().getName() + " and methodName is null",
						e);
			} else if (methodName != null) {
				log.error("Exception when invokeMethod : "
						+ "obj is null and methodName is" + methodName, e);
			} else {
				log.error("Exception when invokeMethod : "
						+ "obj is null and methodName is null", e);
			}
		}

		return null;
	}

	/**
	 * @param obj
	 * @param methodName
	 * @return
	 */
	private static Object invokeMethodByProperty(Object obj, String methodName) {
		String[] mn = StringUtils.split(methodName, ".");
		Object result = null;
		for (int i = 0; i < mn.length; i++) {
			if (i == 0)
				result = invokeMethod(obj, mn[i]);
			else
				result = invokeMethod(result, mn[i]);
			if (result == null)
				return null;
		}
		return result;
	}

	public static Object getNewInstance(String className) {
		try {
			return Class.forName(className).newInstance();
		} catch (Exception e) {
			log.error("Can not getNewInstance of " + className, e);
		}
		return null;
	}

	/**
	 * @param data
	 * @param field
	 * @return
	 */
	public static Object getFieldValue(Object data, String field) {
		String methodName = "get" + field.substring(0, 1).toUpperCase()
				+ field.substring(1);
		return invokeMethod(data, methodName);
	}

	public static Object getNestFieldValue(Object data, String field) {
		if (field.indexOf(".") == -1) {
			return getFieldValue(data, field);
		}
		String[] fields = StringUtils.split(field, ".");

		Object o = data;
		for (int i = 0; i < fields.length; i++) {
			String f = fields[i];
			o = getFieldValue(o, f);
			if (o == null) {
				return "";
			}
		}
		return o;
	}

	/**
	 * check if childClassName is instance of parentClassName
     *
     * @param child
     * @param parentClassName
	 * @return
	 */
	public static boolean isSubClassOf(String childClassName,
			String parentClassName) {
		Class childClass = null;
		try {
			childClass = Class.forName(childClassName);
		} catch (Exception e) {
			log.warn("[" + childClassName + "] is not a valid ClassName", e);
			return false;
		}
		if (childClass.getName().equals(parentClassName))
			return true;
		while (childClass.getSuperclass() != null) {
			if (childClass.getSuperclass().getName().equals(parentClassName))
				return true;
			Class[] iterfaces = childClass.getInterfaces();
			for (int i = 0; i < iterfaces.length; i++) {
				if (iterfaces[i].getName().equals(parentClassName))
					return true;
			}
			childClass = childClass.getSuperclass();
		}
		return false;
	}

    /**
     * @param data
     * @param field
     * @return
     */
    public static Object setFieldValue(Object data, String field, Class[] argTypes,
                                       Object[] args) {
        String methodName = "set" + field.substring(0, 1).toUpperCase()
                + field.substring(1);
        return invokeMethod(data, methodName, argTypes, args);
    }

    public static boolean isDateType(Class clazz, String fieldName) {
        boolean flag = false;
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Object typeObj = field.getType().newInstance();
            flag = typeObj instanceof Date;
        } catch (Exception e) {
            // 把异常吞掉直接返回false
        }
        return flag;
    }
}