package org.besus.meice.oramtt.util;

public interface QueryConstants {

	String TABLE_SELECTION_MYSQL = "show tables";
	String TABLE_SELECTION_ORACLE = "select object_name from user_objects where object_type = 'TABLE'";
}
