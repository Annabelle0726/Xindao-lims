/**
 * 计算多个数值的和
 *
 * @param val 数值型参数，可变长参数列表
 * @returns 返回所有参数的和，如果参数列表为空或参数类型非数值型，则返回null
 */
function SUM(...val) {
  try {
    let num = null;
    if (val && val.length > 0) {
      val.forEach((item) => {
        num += item;
      });
    }
    return num;
  } catch (error) {}
}
/**
 * 计算传入参数中的最大值
 *
 * @param ...val 可变参数列表，用于计算最大值的数值或可转换为数值的类型
 * @returns 返回最大值，如果参数列表为空或为null/undefined/''，则返回null
 */
function MAX(...val) {
  try {
    let max = null;
    if (val && val.length > 0) {
      val = val.filter((item) => item != null && item !== "");
      if (val.length > 0) {
        max = Math.max(...val);
      } else {
        max = null;
      }
    }
    return max;
  } catch (error) {}
}
/**
 * 计算传入参数中的最小值
 *
 * @param val 可变参数，用于计算最小值的数值数组
 * @returns 返回传入参数中的最小值，如果传入参数为空或所有值均为null或空字符串，则返回null
 */
function MIN(...val) {
  try {
    let min = null;
    if (val && val.length > 0) {
      val = val.filter((item) => item != null && item !== "");
      if (val.length > 0) {
        min = Math.min(...val);
      }
    }
    return min;
  } catch (error) {}
}
/**
 * 计算给定数值的平均值
 *
 * @param val 数值列表，可包含任意个参数
 * @returns 返回平均值，如果数值列表为空或包含非数值项，则返回null
 */
function AVERAGE(...val) {
  try {
    let num = null;
    let arr = [];
    if (val && val.length > 0) {
      arr = val.filter((item) => item != null && item !== "");
      arr.forEach((item) => {
        num += item;
      });
      if (arr.length > 0) {
        return num / arr.length;
      } else {
        return null;
      }
    } else {
      return null;
    }
  } catch (error) {}
}
/**
 * 计算一个数的绝对值
 *
 * @param val 任意数值
 * @returns 返回该数值的绝对值
 */
function ABS(val) {
  try {
    return Math.abs(val);
  } catch (error) {}
}
/**
 * 计算一组数字的中位数
 *
 * @param val 任意个参数，需要计算中位数的数字
 * @returns 如果参数中有有效的数字，则返回计算出的中位数；否则返回null
 */
function MEDIAN(...val) {
  try {
    let arr = [];
    if (val && val.length > 0) {
      arr = val.filter((item) => item != null && item !== "");
      const sortedArr = arr.sort((a, b) => a - b);
      // 计算中位数
      const half = Math.floor(sortedArr.length / 2);
      if (arr.length > 0) {
        // 如果数组长度是奇数，直接取中间的元素
        if (sortedArr.length % 2 === 1) {
          return sortedArr[half];
        } else {
          // 如果数组长度是偶数，取中间两个元素的平均值
          return (sortedArr[half - 1] + sortedArr[half]) / 2;
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  } catch (error) {}
}

/**
 * 计算幂
 *
 * @param str 字符串形式的幂表达式，例如"2^3"
 * @returns 返回计算后的幂值，如果表达式无效则返回null
 */
function CalculatePower(str) {
  try {
    if (str && str.includes("^")) {
      let arr = str.split("^");
      if (arr && arr.length > 1) {
        return Math.pow(arr[0], arr[1]);
      } else {
        return null;
      }
    }
  } catch (error) {}
}

/**
 * 根据坐标获取列名
 * @param {Object} cellId
 */
function getColumnNameFromId(cellId) {
  try {
    if (!Array.isArray(cellId)) {
      cellId = cellId.split("-");
    }
    var i = cellId[0];
    var letter = "";
    if (i > 701) {
      letter += String.fromCharCode(64 + parseInt(i / 676));
      letter += String.fromCharCode(64 + parseInt((i % 676) / 26));
    } else if (i > 25) {
      letter += String.fromCharCode(64 + parseInt(i / 26));
    }
    letter += String.fromCharCode(65 + (i % 26));
    return letter + (parseInt(cellId[1]) + 1);
  } catch (e) {
    console.log("error", cellId);
  }
}
/**
 * 根据列名获取坐标
 * @param {Object} id
 * @param {Object} arr
 */
function getIdFromColumnName(id, arr) {
  try {
    // Get the letters
    var t = /^[a-zA-Z]+/.exec(id);
    if (t) {
      // Base 26 calculation
      var code = 0;
      for (var i = 0; i < t[0].length; i++) {
        code +=
          parseInt(t[0].charCodeAt(i) - 64) * Math.pow(26, t[0].length - 1 - i);
      }
      code--;
      // Make sure jexcel starts on zero
      if (code < 0) {
        code = 0;
      }

      // Number
      var number = parseInt(/[0-9]+$/.exec(id));
      if (number > 0) {
        number--;
      }

      if (arr == true) {
        id = [code, number];
      } else {
        // id = code + '-' + number;
        id = {
          c: code,
          r: number,
        };
      }
    }
    return id;
  } catch (e) {
    console.log("error", id);
  }
}

/**
 * 更改参数
 *
 * @param f 参数列表
 * @returns 返回一个包含坐标信息的数组
 */
function changeParameter(f) {
  try {
    let arr = getABCList(f);
    let arr2 = [];
    arr.forEach((item) => {
      if (item.includes(":")) {
        let r0 = getIdFromColumnName(item.split(":")[0]).r;
        let c0 = getIdFromColumnName(item.split(":")[0]).c;
        let r1 = getIdFromColumnName(item.split(":")[1]).r;
        let c1 = getIdFromColumnName(item.split(":")[1]).c;
        for (let i = Number(r0); i <= Number(r1); i++) {
          for (let u = Number(c0); u <= Number(c1); u++) {
            arr2.push({
              r: i,
              c: u,
            });
          }
        }
      } else {
        arr2.push(getIdFromColumnName(item));
      }
    });
    return arr2;
  } catch (error) {}
}
/**
 * 获取包含 ABC 字符的列表
 *
 * @param f 字符串，包含需要解析的公式或表达式
 * @returns 包含 ABC 字符的数组列表
 */
function getABCList(f) {
  try {
    let regex = /[=\+\-\*\%\(\)\/\^\s]/g;
    let fouList = ["SUM", "MAX", "MIN", "AVERAGE", "ABS"];
    f = f
      .replace(regex, ",")
      .replace(new RegExp('"&', "g"), "")
      .replace(new RegExp('&"', "g"), "");
    fouList.forEach((item) => {
      f = f.replace(new RegExp(item, "g"), "");
    });
    let arr = f.split(",").filter((item) => {
      return (
        item && /[a-zA-Z]/.test(item) && item != "undefined" && item != "null"
      );
    });
    return arr;
  } catch (error) {}
}
/**
 * 获取所有单元格
 *
 * @param f 表格数据或相关参数
 * @returns 返回一个对象，其键为单元格的唯一标识符（由列和行ID拼接而成），值为null
 */
function getAllCell(f) {
  try {
    let arr = changeParameter(f);
    let arr0 = {};
    arr.forEach((item) => {
      arr0[getColumnNameFromId(`${item.c}` + "-" + `${item.r}`)] = null;
    });
    return arr0;
  } catch (error) {}
}

/**
 * 计算函数
 *
 * @param f 字符串类型，表示待计算的公式
 * @param comValue 对象类型，表示要替换的单元格值，键为单元格名称，值为替换后的值
 * @returns 返回计算后的结果，如果计算失败则返回0
 */
function compute(f, comValue) {
  try {
    let str = f;
    // 获取单元格对应值
    let arr = getAllCell(f);
    for (var a in comValue) {
      if (
        comValue[a] !== "undefined" &&
        comValue[a] !== "null" &&
        comValue[a] !== undefined
      ) {
        if (typeof comValue[a] == "string" && comValue[a].includes("^")) {
          // 计算幂次
          arr[a] = CalculatePower(comValue[a]);
        } else if (
          typeof comValue[a] == "string" &&
          comValue[a].includes("/")
        ) {
          arr[a] = comValue[a];
        } else {
          arr[a] = comValue[a];
        }
      }
    }
    // 解析公式参数，特别是带：的
    let arr0 = getABCList(f);
    let obj = {};
    arr0.forEach((item) => {
      if (item.includes(":")) {
        let arr1 = [];
        let r0 = getIdFromColumnName(item.split(":")[0]).r;
        let c0 = getIdFromColumnName(item.split(":")[0]).c;
        let r1 = getIdFromColumnName(item.split(":")[1]).r;
        let c1 = getIdFromColumnName(item.split(":")[1]).c;
        for (let i = Number(r0); i <= Number(r1); i++) {
          for (let u = Number(c0); u <= Number(c1); u++) {
            arr1.push({
              r: i,
              c: u,
            });
          }
        }
        let arr2 = [];
        arr1.forEach((m) => {
          arr2.push(getColumnNameFromId(`${m.c}` + "-" + `${m.r}`));
        });
        obj[item.split(":").join("-")] = arr2.join(",");
      } else {
        obj[item] = item;
      }
    });
    str = str.replace(new RegExp(":", "g"), "");
    // 替换参数
    for (var a in obj) {
      str = str.replace(new RegExp(a, "g"), obj[a]);
    }
    // 计算
    for (var a in arr) {
      str = str.replace(new RegExp(a, "g"), arr[a]);
    }
    return eval(str);
  } catch (error) {}
}

export default {
  compute,
  changeParameter,
};
