package org.besus.meice.oramtt.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OracleDataMap {

	private static Map<String, String> dataTypeMap;

	static {
		dataTypeMap = new HashMap<String, String>();
		dataTypeMap.put("String", "varchar2");
		dataTypeMap.put("Integer", "int");
	}

	public static Set<String> getUserDataTypes() {
		return dataTypeMap.keySet();
	}

	public static String getOracleDataType(String userDataType) {
		return dataTypeMap.get(userDataType);
	}
}
