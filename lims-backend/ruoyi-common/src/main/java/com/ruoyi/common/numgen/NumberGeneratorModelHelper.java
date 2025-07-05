/**
 * ***************************************************************************
 * Copyright (c) 2010 Qcadoo Limited
 * Project: Qcadoo Framework
 * Version: 1.4
 * <p>
 * This file is part of Qcadoo.
 * <p>
 * Qcadoo is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation; either version 3 of the License,
 * or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * ***************************************************************************
 */
package com.ruoyi.common.numgen;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author zhangxy
 */
@AllArgsConstructor
@Service
public class NumberGeneratorModelHelper {

	private JdbcTemplate jdbcTemplate;


	private static final String NUMBER_EXIST_QUERY_TEMPLATE = "select count(*) from ${TABLE_NAME} where ${NUMBER_FIELD}='${VALUE}'";


	private static final String GET_NUMBERS_QUERY_TEMPLATE = "select MAX( CAST( "
			+ " coalesce(TRIM(LEADING '0' from ${NUMBER_FIELD}), '0')   "
			+ " AS UNSIGNED ) ) "
			+ "from ${TABLE_NAME} where 1=1";

	private static final String GET_PREFIX_AWARE_NUMBERS_QUERY_TEMPLATE = "select MAX( CAST( "
			+ " TRIM(LEADING '0' from SUBSTRING(${NUMBER_FIELD}, ${NUMBER_STARTS_AT}))  "
			+ " AS UNSIGNED ) ) "
			+ "from ${TABLE_NAME}  where ${NUMBER_FIELD} like '${PREFIX}%'";

	private static final String GET_SUFFIX_AWARE_NUMBERS_QUERY_TEMPLATE = "select MAX( CAST( "
			+ " TRIM(LEADING '0' from SUBSTRING(${NUMBER_FIELD}, 1, POSITION('${SUFFIX}' IN ${NUMBER_FIELD}) - 1)) "
			+ " AS UNSIGNED )) "
			+ "from ${TABLE_NAME}  where ${NUMBER_FIELD} like '%${SUFFIX}'";


	public boolean numberExist(final String value, final NumberTableInfo numberTableInfo) {
		Map<String, String> placeholderValues = Maps.newHashMap();

		placeholderValues.put("TABLE_NAME", numberTableInfo.getTableName());
		placeholderValues.put("NUMBER_FIELD", numberTableInfo.getNumberFieldName());
		placeholderValues.put("VALUE", value);
		String query = new StringSubstitutor(placeholderValues, "${", "}").replace(NUMBER_EXIST_QUERY_TEMPLATE);
		Long count = jdbcTemplate.queryForObject(query, Long.class);
		return count > 0;
	}

	public Long getNumbersProjection(final NumberTableInfo numberTableInfo, final String prefix, final String suffix) {
		String sqlQuery = buildQuery(numberTableInfo, prefix, suffix);
		return jdbcTemplate.queryForObject(sqlQuery, Long.class);
	}

	private String buildQuery(final NumberTableInfo numberTableInfo,
							  final String prefix, final String suffix) {
		Map<String, String> placeholderValues = Maps.newHashMap();

		placeholderValues.put("TABLE_NAME", numberTableInfo.getTableName());
		placeholderValues.put("NUMBER_FIELD", numberTableInfo.getNumberFieldName());

		String query;
		if (StringUtils.isNotEmpty(prefix)) {
			placeholderValues.put("PREFIX", prefix);
			int prefixLength = StringUtils.length(prefix);
			placeholderValues.put("NUMBER_STARTS_AT", String.valueOf(prefixLength + 1));
			query = GET_PREFIX_AWARE_NUMBERS_QUERY_TEMPLATE;
		} else if (StringUtils.isNotEmpty(suffix)) {
			placeholderValues.put("SUFFIX", suffix);
			query = GET_SUFFIX_AWARE_NUMBERS_QUERY_TEMPLATE;
		} else {
			query = GET_NUMBERS_QUERY_TEMPLATE;
		}

		if(numberTableInfo.isLogicDelete()){
			query += " and active=true";
		}
		StringSubstitutor substitutor = new StringSubstitutor(placeholderValues, "${", "}");
		return substitutor.replace(query);
	}

}
