package com.ruoyi.common.numgen;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 编号生成器
 *
 * @Author: zhangxy
 * @Date: 2020-09-08 16:31
 */
@Slf4j
@Service
@AllArgsConstructor
public class NumberGenerator<T> {

	private static Pattern NUMBER_PATTERN = Pattern.compile("(.+)\\((\\d+)\\)");

	private NumberGeneratorModelHelper numberGeneratorModelHelper;


	public String getCopyValueOfUniqueField(final String value, SFunction<T, ?> column) {
		if (value == null) {
			return null;
		} else {
			Matcher matcher = NUMBER_PATTERN.matcher(value);
			String oldValue = value;
			int index = 1;
			if (matcher.matches()) {
				oldValue = matcher.group(1);
				index = Integer.valueOf(matcher.group(2)) + 1;
			}

			NumberTableInfo info = initDbInfo(column);
			while (true) {
				String newValue = oldValue + "(" + (index++) + ")";
				boolean exist = numberGeneratorModelHelper.numberExist(newValue, info);
				if (!exist) {
					return newValue;
				}
			}
		}
	}


	/**
	 * 生成编号
	 *
	 * @param numOfDigits
	 * @param column
	 * @return
	 */
	public String generateNumber(final int numOfDigits, SFunction<T, ?> column) {
		return generateNumberWithPrefix(numOfDigits, null, column);
	}

	/**
	 * 生成带前缀的编号
	 *
	 * @param numOfDigits
	 * @param prefix
	 * @param column
	 * @return
	 */
	public String generateNumberWithPrefix(final int numOfDigits, final String prefix, SFunction<T, ?> column) {
		NumberTableInfo info = initDbInfo(column);
		String generatedNumber = generateNumberWithExtension(info, numOfDigits, prefix, "");
		return prependPrefix(prefix, generatedNumber);
	}


	/**
	 * 生成带后缀的编号
	 *
	 * @param numOfDigits
	 * @param suffix
	 * @param column
	 * @return
	 */
	public String generateNumberWithSuffix(final int numOfDigits, final String suffix, SFunction<T, ?> column) {
		NumberTableInfo info = initDbInfo(column);
		String generatedNumber = generateNumberWithExtension(info, numOfDigits, "", suffix);
		return appendSuffix(suffix, generatedNumber);
	}

	/**
	 * 初始化数据库表名、字段名
	 *
	 * @param column
	 * @return
	 */
	private NumberTableInfo initDbInfo(SFunction<T, ?> column) {
		SerializedLambda ld = LambdaUtils.resolve(column);
		TableInfo tableInfo = TableInfoHelper.getTableInfo(ld.getImplClass());
		String fieldName = PropertyNamer.methodToProperty(ld.getImplMethodName());
		Optional<TableFieldInfo> op = tableInfo.getFieldList().stream().filter(f -> fieldName.equals(f.getProperty())).findFirst();
		if (!op.isPresent()) {
			throw new RuntimeException("获取数据库字段出错，请检查映射");
		}
		TableFieldInfo fieldInfo = op.get();

		NumberTableInfo info = new NumberTableInfo();
		info.setLogicDelete(tableInfo.isLogicDelete());
		info.setNumberFieldName(fieldInfo.getColumn());
		info.setTableName(tableInfo.getTableName());
		return info;
	}


	private String generateNumberWithExtension(NumberTableInfo numberTableInfo, int numOfDigits, String prefix, String suffix) {
		Long dbMax = numberGeneratorModelHelper.getNumbersProjection(numberTableInfo, prefix, suffix);
		Long greatestNumber = 0L;
		if (dbMax != null) {
			greatestNumber = dbMax;
		}
		return String.format("%0" + numOfDigits + "d", greatestNumber + 1);
	}

	private String prependPrefix(final String prefix, final String generatedNumber) {
		if (prefix == null) {
			return generatedNumber;
		}
		return prefix + generatedNumber;
	}


	private String appendSuffix(final String suffix, final String generatedNumber) {
		if (suffix == null) {
			return generatedNumber;
		}
		return generatedNumber + suffix;
	}


}
