package com.bpm.core.constant;

public class SQL {
	public static String SERVICE_CONFIG_STATEMENT = "SELECT system, service_config, log_on FROM si_version WHERE si_version=? AND service_name=? AND status=1";
	public static String MAP_INFO_STATEMENT = "SELECT from_field, to_field, is_calcula, is_date, default_data, condition_field, condition, ctr_name, formula, is_required FROM si_auto_mapping WHERE subject_code=? AND status=1";
	public static String INSERT_LOG_SQL = "{call SI_HANDLING_V2.INSERT_SI_LOG(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
}
