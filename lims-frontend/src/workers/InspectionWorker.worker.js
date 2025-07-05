// 多线程里面需要保存的数据
import Big from "big.js";

let code = "";
// 表格数据（渲染）
let tableList = null;
// 方法列表
let excelMethodList = [];
// excel坐标比较列表
let comparisonList = [];
// 当前样品信息
let currentSample = {};
// 当前项目：检测中心、装备电缆
let PROJECT = "";
// 套管
let bushing = "";
// 多线程传递到主线程的数据
let result = {
  method: "",
  value: null,
};

let modelType = "";

// 保存时传到后端参数
let param = null;
// 当前的模板
let currentTable = "";
// 优化数采较多数据时-记录最后一个检验项的id
let getDataTypeId = null;
//当前检验项
let currentInsItem = null;
// 接收到主线程处理逻辑的消息
self.onmessage = function (event) {
  // 保存主线程传过来的值
  const data = JSON.parse(event.data);
  if (currentTable != data.currentTable) {
    // 检验模板改变后需要刷新表格
    tableList = data.tableList;
  }
  currentTable = data.currentTable;
  if (data.type && data.type == "saveData") {
    // 更新表格数据和传递到后端的参数
    tableList = data.tableList;
    param = data.param;
    return;
  }
  if (data.bushing && data.bushing != bushing && data.type == "saveData") {
    // 更新表格数据、传递到后端的参数和套管数据
    tableList = data.tableList;
    param = data.param;
    bushing = data.bushing;
    return;
  }
  modelType = data.modelType;
  code = data.code;
  if (data.getDataTypeId) {
    // 记录 优化数采较多数据时-记录最后一个检验项的id
    getDataTypeId = data.getDataTypeId;
  } else {
    getDataTypeId = null;
  }
  if (tableList) {
    // 如果表格数据存在，则更新当前修改检验项的表格数据
    let str = code.split("-");
    let r = str[1];
    let c = str[2];
    tableList[0].arr.forEach((item, index) => {
      item.forEach((m, i) => {
        if (m.c == c && m.r == r) {
          tableList[0].arr[index] = data.tableList[0].arr[index];
        }
      });
    });
  } else {
    // 如果初始化表格数据不存在，直接赋值
    tableList = data.tableList;
  }
  if (param) {
    // 如果传递到后端的参数存在，则更新当前修改检验项的传递到后端的参数
    let str = code.split("-");
    let pId = str[3];
    param[pId] = data.param[pId];
  } else {
    // 初始化传递到后端的参数
    param = data.param;
  }
  if (data.currentInsItem) {
    currentInsItem = data.currentInsItem;
  }
  // 更新方法列表、坐标比较列表、当前样品信息、项目
  excelMethodList = JSON.parse(JSON.stringify(data.excelMethodList));
  comparisonList = JSON.parse(JSON.stringify(data.comparisonList));
  currentSample = JSON.parse(JSON.stringify(data.currentSample));
  PROJECT = data.PROJECT;
  // 执行计算方法
  changeInput("", code);
};

function changeInput(m, code) {
  let str = code.split("-");
  let r = str[1]; //当前行
  let c = str[2]; //当前列
  let id = str[0];
  let pId = str[3]; //当前检验项id，param[pId]为当前检验项的所有值，包含：过程值insValue、计算值comValue、设备编码equipValue、设备名称equipName、最终值resValue、结论insResult
  var list = []; //一个双层数组，里面保存有当前页面渲染的所有数据
  // 赋值当前模板的表格数据
  for (let a in tableList) {
    if (tableList[a].templateId == id) {
      list = tableList[a].arr;
      break;
    }
  }
  let isToExponential = ""; //是否为科学计数法
  let list2 = []; //一个数组，里面保存有当前检验项的所有参数值
  let isPoint = ""; //是否为小数点
  let inputType = null;
  let currentInsItemId = "";
  let isSave = "true";
  // 循环所有excel方法，找到当前行、列的检验项，如果此单元格（r,c）是某个excel方法里面的参数，则执行此方法
  const index = excelMethodList.findIndex((item) => item.r == r);
  excelMethodList.forEach((item) => {
    inputType =
      item.valueList &&
      item.valueList.length > 0 &&
      item.valueList.find((m) => m.r == r && m.c == c);
    // item.valueList  是当前excel方法的参数列表，找到当前填入的值是否是参数
    if (
      item.valueList &&
      item.valueList.length > 0 &&
      item.valueList.find((m) => m.r == r && m.c == c)
    ) {
      isSave = "false";
      // 如果是，则定义一个参数的对象集合，用于保存当前excel方法参数的值
      var comValue = {};
      item.valueList.forEach((a) => {
        list.forEach((b) => {
          if (b[0].r == a.r) {
            b.forEach((c) => {
              if (c.c == a.c) {
                // 获取当前参数的Execl行坐标(ABCD)等等
                var tableCode = "";
                for (var d in comparisonList) {
                  if (c.c == comparisonList[d].value) {
                    tableCode = comparisonList[d].label;
                    break;
                  }
                }
                list2.push(c.v.v);
                // 组装参数的对象集合并赋值，列如{A3:12,B4:15}
                if (
                  getInspectionValueType(item.i) == 1 &&
                  !isNaN(parseFloat(c.v.v))
                ) {
                  // 如果是数字输入框
                  let n = String(c.v.v);
                  if (n.includes("/")) {
                    // 如果是分数，则赋值
                    comValue[tableCode + (c.r + 1)] = c.v.v;
                  } else {
                    comValue[tableCode + (c.r + 1)] = parseFloat(c.v.v);
                  }
                } else {
                  // 如果是文本、下拉框等等
                  comValue[tableCode + (c.r + 1)] = c.v.v;
                }
              }
            });
          }
        });
      });
      // 如果此excel方法是结论判断方法，则执行此方法
      // console.log('item.v.ps.value---', item.v.ps.value)
      if (item.v.ps != undefined && item.v.ps.value == "结论") {
        try {
          if (currentSample.insProduct.find((m) => m.id == item.i)) {
            // 如果当前检验项是产品检验项，则执行此方法，找到此检验项的要求值
            let ask = currentSample.insProduct.find((m) => m.id == item.i).ask
              ? currentSample.insProduct
                  .find((m) => m.id == item.i)
                  .ask.split("&")
              : null;
            // 获取当前结论的参数，也就是当前检验项的最终值
            let res = Object.values(comValue)[0];
            if (typeof res == "string" && res.includes(">")) {
              res = res.replace(">", "");
            }
            if (typeof res == "string" && res.includes("≤")) {
              res = res.replace("≤", "");
            }
            if (typeof res == "string" && res.includes("≥")) {
              res = res.replace("≥", "");
            }
            if (typeof res == "string" && res.includes("<")) {
              res = res.replace("<", "");
            }
            if (typeof res == "string" && res.includes("＞")) {
              res = res.replace("＞", "");
            }
            if (typeof res == "string" && res.includes("＜")) {
              res = res.replace("＜", "");
            }
            if (
              typeof res == "string" &&
              (res === "断裂" || res === "脆化" || res === "断裂脆化")
            ) {
              item.v.v = 0;
              list.forEach((a) => {
                if (a[0].r == item.r) {
                  for (let b = 0; b < a.length; b++) {
                    if (a[b].c == item.c) {
                      a[b].v.v = 0;
                      break;
                    }
                  }
                }
              });
            }
            let comp = [];
            // 判断当前结论的参数（当前检验项的最终值）是否为空，如果为空，则直接赋值
            if (
              res === "" ||
              res === null ||
              res === undefined ||
              res === "Infinity"
            ) {
              item.v.v = null;
              list.forEach((a) => {
                if (a[0].r == item.r) {
                  for (let b = 0; b < a.length; b++) {
                    if (a[b].c == item.c) {
                      a[b].v.v = null;
                      break;
                    }
                  }
                }
              });
            } else {
              // 如果不为空，则判断当前结论就需要进行判断
              //要求值为-，/，—，则结论设置为不判定，结论赋值为3
              if (
                ask == null ||
                ask[0] == "-" ||
                ask[0] == "/" ||
                ask[0] == "—"
              ) {
                item.v.v = 3;
                list.forEach((a) => {
                  if (a[0].r == item.r) {
                    for (let b = 0; b < a.length; b++) {
                      if (a[b].c == item.c) {
                        a[b].v.v = 3;
                        break;
                      }
                    }
                  }
                });
              } else {
                // 要求值不为-，/，—，则进行判断
                if (ask) {
                  // 循环要求值列表，判断当前结论的参数是否符合要求
                  comp = ask.map((m, i) => {
                    // 如果要求值包含=，则判断当前结论的参数是否等于要求值，
                    // 以下判断基本一致，只是判断类型不一样，就不做注释了
                    if (m.includes("=")) {
                      // 处理要求值
                      let str = handleFraction(m.split("=")[1]);
                      if (typeof res == "string" && typeof str == "string") {
                        // 如果要求值和当前结论的参数都是字符串，则执行
                        if (res.includes("/")) {
                          // 如果结论的参数是分数，则判断
                          if (m.includes("/")) {
                            // 如果要求值是分数，则判断
                            return eval(res) == eval(str);
                          } else {
                            // 如果要求值不是分数，则判断
                            return handleMoreParam(res, m.split("=")[1], "=");
                          }
                        } else {
                          // 如果结论的参数不是分数，则判断
                          return (
                            res.trim().replace(/[.,。、；：'";?？“，]/g, "") ==
                            str.trim().replace(/[.,。、；：'";?？“，]/g, "")
                          );
                        }
                      } else {
                        // 如果要求值和当前结论的参数有一个是数字，则执行
                        return eval(res) == eval(str);
                      }
                    } else if (m.includes("≥")) {
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let str = handleFraction(m.split("≥")[1]);
                          return eval(res) >= eval(str);
                        } else {
                          return handleMoreParam(res, m.split("≥")[1], "≥");
                        }
                      } else {
                        let str = handleFraction(m.split("≥")[1]);
                        return eval(res) >= eval(str);
                      }
                    } else if (m.includes("≤")) {
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let str = handleFraction(m.split("≤")[1]);
                          return eval(res) <= eval(str);
                        } else {
                          return handleMoreParam(res, m.split("≤")[1], "≤");
                        }
                      } else {
                        let str = handleFraction(m.split("≤")[1]);
                        if (typeof str == "string" && str.includes("A")) {
                          str = str.replace("A", "");
                        }
                        if (typeof str == "string" && str.includes("D")) {
                          str = str.replace("D", "");
                        }
                        if (typeof res == "string" && res.includes("A")) {
                          res = res.replace("A", "");
                        }
                        if (typeof res == "string" && res.includes("D")) {
                          res = res.replace("D", "");
                        }
                        return eval(res) <= eval(str);
                      }
                    } else if (m.includes("<")) {
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let str = handleFraction(m.split("<")[1]);
                          return eval(res) < eval(str);
                        } else {
                          return handleMoreParam(res, m.split("<")[1], "<");
                        }
                      } else {
                        let str = handleFraction(m.split("<")[1]);
                        return eval(res) < eval(str);
                      }
                    } else if (m.includes(">")) {
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let str = handleFraction(m.split(">")[1]);
                          return eval(res) > eval(str);
                        } else {
                          return handleMoreParam(res, m.split(">")[1], ">");
                        }
                      } else {
                        let str = handleFraction(m.split(">")[1]);
                        return eval(res) > eval(str);
                      }
                    } else if (m.includes("~")) {
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let k = m.split("~");
                          return (
                            eval(res) >= eval(handleFraction(k[0])) &&
                            eval(res) <= eval(handleFraction(k[1]))
                          );
                        } else {
                          return handleMoreParam(res, m, "~");
                        }
                      } else {
                        let k = m.split("~");
                        return (
                          eval(res) >= eval(handleFraction(k[0])) &&
                          eval(res) <= eval(handleFraction(k[1]))
                        );
                      }
                    } else if (m.includes("-")) {
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let k = m.split("-");
                          return (
                            eval(res) >= eval(handleFraction(k[0])) &&
                            eval(res) <= eval(handleFraction(k[1]))
                          );
                        } else {
                          return handleMoreParam(res, m, "-");
                        }
                      } else {
                        let k = m.split("-");
                        return (
                          eval(res) >= eval(handleFraction(k[0])) &&
                          eval(res) <= eval(handleFraction(k[1]))
                        );
                      }
                    } else if (m.includes("±")) {
                      if (modelType.includes("φ-")) {
                        if (m.indexOf("±") == 0) {
                          m =
                            modelType.substring(modelType.indexOf("-") + 1) + m;
                        }
                      }
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let k = m.split("±");
                          return (
                            eval(res) >=
                              eval(
                                handleFraction(k[0]) - handleFraction(k[1])
                              ) &&
                            eval(res) <=
                              eval(
                                Number(handleFraction(k[0])) +
                                  Number(handleFraction(k[1]))
                              )
                          );
                        } else {
                          return handleMoreParam(res, m, "±");
                        }
                      } else {
                        let k = m.split("±");
                        return (
                          eval(res) >=
                            eval(handleFraction(k[0]) - handleFraction(k[1])) &&
                          eval(res) <=
                            eval(
                              Number(handleFraction(k[0])) +
                                Number(handleFraction(k[1]))
                            )
                        );
                      }
                    } else if (m.includes("＞")) {
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let str = handleFraction(m.split("＞")[1]);
                          return eval(res) > eval(str);
                        } else {
                          return handleMoreParam(res, m.split("＞")[1], ">");
                        }
                      } else {
                        let str = handleFraction(m.split("＞")[1]);
                        return eval(res) > eval(str);
                      }
                    } else if (m.includes("＜")) {
                      if (typeof res == "string" && res.includes("/")) {
                        if (m.includes("/")) {
                          let str = handleFraction(m.split("＜")[1]);
                          return eval(res) < eval(str);
                        } else {
                          return handleMoreParam(res, m.split("＜")[1], "<");
                        }
                      } else {
                        let str = handleFraction(m.split("＜")[1]);
                        return eval(res) < eval(str);
                      }
                    }
                  });
                }
                // 如果要求值的每个条件都符合，则给结论赋值为1，合格
                if (comp.every((m) => m)) {
                  item.v.v = 1;
                  list.forEach((a) => {
                    if (a[0].r == item.r) {
                      for (let b = 0; b < a.length; b++) {
                        if (a[b].c == item.c) {
                          a[b].v.v = 1;
                          break;
                        }
                      }
                    }
                  });
                } else {
                  // 否则给结论赋值为0，不合格
                  item.v.v = 0;
                  list.forEach((a) => {
                    if (a[0].r == item.r) {
                      for (let b = 0; b < a.length; b++) {
                        if (a[b].c == item.c) {
                          a[b].v.v = 0;
                          break;
                        }
                      }
                    }
                  });
                }
              }
            }
            let getDataType0 = false;
            // 优化数采较多数据时-记录最后一个检验项的id，如果当前检验项的id与记录的id相同，则多传一个参数到主线程，进行数据保存，否则数采就不进行保存
            if (item.i == getDataTypeId) {
              getDataType0 = true;
            }
            // 赋值传递到主线程的数据，method：saveInsContext表示此消息需要保存数据
            currentInsItemId = item.i;
            // 赋值传递到主线程的数据，method：saveInsContext表示此消息需要保存数据
            result = {
              method: "saveInsContext",
              value: {
                tableList, // 表格数据
                param: getParam(), //传给后端的参数
                getDataTypeId: getDataTypeId,
                currentInsItemId: item.i,
              },
            };
            // 发消息给主线程
            self.postMessage(JSON.stringify(result));
          }
        } catch (error) {
          console.log("error---", error);
        }
      } else {
        // 如果是函数方法，则执行此方法
        let comResult = ""; //初始化计算结果
        try {
          if (getInspectionValueType(item.i) == 1) {
            // 如果检验值类型是数字输入框
            let tell = currentSample.insProduct.find((m) => m.id == item.i).tell
              ? currentSample.insProduct
                  .find((m) => m.id == item.i)
                  .tell.split("&")
              : null;
            isPoint =
              tell &&
              tell.length > 0 &&
              typeof tell[0] == "string" &&
              tell[0].includes("/") &&
              tell[0] !== "/"; // 装备电缆--判断要求值是否为分数
            const inspectionItemClass = currentSample.insProduct.find(
              (m) => m.id == item.i
            ).inspectionItemClass; // 检验项分类
            const inspectionItem = currentSample.insProduct.find(
              (m) => m.id == item.i
            ).inspectionItem; // 检验项
            const inspectionItemSubclass = currentSample.insProduct.find(
              (m) => m.id == item.i
            ).inspectionItemSubclass; // 检验子项
            let isHaveSymbol = false;
            let symbol = "";
            for (var a in comValue) {
              if (
                typeof comValue[a] == "string" &&
                (comValue[a].includes(">") ||
                  comValue[a].includes("≤") ||
                  comValue[a].includes("<") ||
                  comValue[a].includes("≥") ||
                  comValue[a].includes("＜") ||
                  comValue[a].includes("＞"))
              ) {
                if (comValue[a].includes(">")) {
                  symbol = ">";
                } else if (comValue[a].includes("≤")) {
                  symbol = "≤";
                } else if (comValue[a].includes("≥")) {
                  symbol = "≥";
                } else if (comValue[a].includes("＜")) {
                  symbol = "＜";
                } else if (comValue[a].includes("＞")) {
                  symbol = "＞";
                } else {
                  symbol = "<";
                }
                isHaveSymbol = true;
                break;
              }
            }
            // 根据输入的数值，进行计算
            comResult = compute(item.v.f.replace(/=/g, " "), comValue, isPoint);
            // 检验项为密度时，根据要求值的小数位改变最终值的小数位
            if (inspectionItem == "密度") {
              const str = "" + tell;
              const indexOfDecimal = str.indexOf(".");
              if (indexOfDecimal > -1) {
                const length = str.length - indexOfDecimal - 1;
                comResult = comResult.toFixed(length);
              }
            } else if (
              inspectionItemClass == "外护套机械物理性能" &&
              inspectionItem == "在IRM 902矿物油中浸泡后" &&
              (inspectionItemSubclass == "断裂伸长率变化率" ||
                inspectionItemSubclass == "抗张强度变化率")
            ) {
              comResult = Math.round(comResult).toFixed(0);
            } else if (
              typeof comResult == "string" &&
              (comResult.includes("断裂") || comResult.includes("脆化"))
            ) {
              comResult = comResult + "";
            }
            if (inspectionItem === "邵氏硬度" && tell[0].includes("A")) {
              // 要求值如果有包含字母，最终结果要展示出字母
              comResult = comResult + "A";
            }
            if (
              inspectionItemClass === "绝缘机械物理性能" &&
              inspectionItem === "弯曲性能" &&
              inspectionItemSubclass === "最小弯曲半径" &&
              tell[0].includes("D")
            ) {
              // 要求值如果有包含字母，最终结果要展示出字母
              comResult = comResult + "D";
            }
            if (isHaveSymbol) {
              comResult = symbol + comResult;
            }
            let list3 = list2.map((item) => item + "");
            // 判断填的检验值是否为科学计数法，如果为科学计数法，则进行转化
            isToExponential = list3.some(
              (val) => val.includes("e+") || val.includes("e-")
            );
            if (isToExponential) {
              let num2 = new Big(comResult);
              comResult = num2.toExponential(1);
            }
          } else {
            // 如果检验值类型是文本输入框、下拉框
            let valueList = [];
            // 处理excel函数参数列表
            item.valueList.forEach((a) => {
              valueList.push({
                name: `${comparisonList.find((e) => e.value == a.c).label}${
                  a.r + 1
                }`,
                value: 0,
              });
            });
            // 给excel函数参数赋文本值
            for (var a in comValue) {
              valueList.forEach((b) => {
                if (b.name == a) {
                  b.value = comValue[a];
                }
              });
            }
            // 计算公式，去掉excel函数的等号，并替换参数，列如：=A1  变成 A1 变成 ‘文本输入的值’
            let str = item.v.f.replace(/=/g, " ");
            valueList.forEach((b) => {
              str = str.replace(b.name, b.value);
            });
            // 计算结果赋值
            comResult = str;
          }
        } catch (error) {
          console.log("error---", error);
        }
        try {
          // 循环表格数据，给表格数据进行赋值
          list.forEach((a) => {
            if (a[0].r == item.r && comResult !== "") {
              // 判断当前行是否为当前检验项所在行，如果为当前行，则给表格数据赋值
              for (var b in a) {
                if (a[b].c == item.c) {
                  try {
                    if (comResult == 0) {
                      // 判断计算结果是否为0，如果为0，则给表格数据赋值为0
                      a[b].v.v = 0;
                    } else if (
                      a[b].v.ct &&
                      a[b].v.ct.fa &&
                      typeof a[b].v.ct.fa == "string" &&
                      a[b].v.ct.fa.includes(".")
                    ) {
                      // 判断当前单元格是否保留小数点，如果为保留小数点，则给表格数据赋值为保留小数点，这个是根据模板配置小数点来的
                      let num = 0;
                      let str = a[b].v.ct.fa.split(".")[1];
                      num = str.length;
                      a[b].v.v = comResult
                        ? Number(comResult).toFixed(num)
                        : comResult;
                    } else if (
                      typeof comResult == "string" &&
                      (comResult.includes("e+") || comResult.includes("e-"))
                    ) {
                      // 判断计算结果是否为科学计数法，如果为科学计数法，则给表格数据赋值为科学计数法
                      a[b].v.v = comResult;
                    } else {
                      const inspectionItem = currentSample.insProduct.find(
                        (m) => m.id == item.i
                      ).inspectionItem;
                      // 判断计算结果是否为数字，如果为数字，则给表格数据赋值为数字
                      if (inspectionItem == "铜线电阻率ρ20最大值") {
                        let val = parseFloat(Number(comResult).toFixed(5));
                        a[b].v.v = isNaN(val) ? comResult : val;
                      } else {
                        let val = parseFloat(Number(comResult).toFixed(3));
                        a[b].v.v = isNaN(val) ? comResult : val;
                      }
                    }
                  } catch (error) {
                    // 如果以上判断都不支持，则直接赋值
                    a[b].v.v = comResult;
                    console.log("error---", error);
                  }
                  break;
                }
              }
            }
          });
          // 如果此计算结果所属单元格，同时也是另一个excel函数的参数，那么就需要递归进行计算
          changeInput(comResult, `${id}-${item.r}-${item.c}-${pId}`); //改变最终值
          currentInsItemId = item.i;
        } catch (error) {
          console.log("error---", error);
        }
      }
    }
  });
  if (index === -1 || (inputType === undefined && isSave === "true")) {
    // 赋值传递到主线程的数据，method：saveInsContext表示此消息需要保存数据
    result = {
      method: "saveInsContext",
      value: {
        tableList, // 表格数据
        param: getParam(), //传给后端的参数
        getDataTypeId: getDataTypeId,
        currentInsItemId: currentInsItemId,
      },
    };
    // 发消息给主线程
    self.postMessage(JSON.stringify(result));
  }

  // 赋值多线程传输数据
  result = {
    method: "tableList",
    value: tableList,
  };
  // 发送主线程数据
  self.postMessage(JSON.stringify(result));
  try {
    // 赋值多线程传输数据
    result = {
      method: "getCurrentInsProduct",
      value: pId,
    };
    // 发送主线程数据
    self.postMessage(JSON.stringify(result));
  } catch (error) {
    console.log("error---", error);
  }
}
/**
 * 获取检测值类型
 *
 * @param id 检测值类型对应的id
 * @returns 返回检测值类型
 */
function getInspectionValueType(id) {
  for (var a in currentSample.insProduct) {
    if (currentSample.insProduct[a].id == id) {
      return currentSample.insProduct[a].inspectionValueType;
    }
  }
}
/**
 * 处理分数或带有乘法和乘方的字符串
 *
 * @param str 字符串类型，表示要处理的分数或乘方表达式
 * @returns 返回一个数字或原字符串，如果字符串为有效的分数或乘方表达式，则返回计算结果；否则返回原字符串
 */
function handleFraction(str) {
  if (str && typeof str == "string" && str.includes("/")) {
    // 处理分数
    return eval(str.split("/")[0] / str.split("/")[1]);
  } else if (
    str &&
    typeof str == "string" &&
    str.includes("*") &&
    str.includes("^")
  ) {
    // 计算乘方
    const num1 = str.split("*");
    const num2 = num1[1].split("^");
    let num3 = new Big(num2[0]);
    let num4 = new Big(num2[1]);
    let num5 = Math.pow(num3, num4); // 计算次方
    return num1[0] * num5; // 最后计算乘法
  } else {
    return str;
  }
}

/**
 * 处理带有多个参数的函数
 *
 * @param res 字符串，需要被分割并处理的字符串
 * @param str 字符串，与res中的每一项进行比较的字符串，可能是分数或者带有比较符号的字符串
 * @param comp 字符串，表示比较类型的字符（如 '>'、'<'、'='、'≥'、'≤'、'±'、'~'、'-'）
 * @returns 返回布尔值，如果res中每一项都满足与str的比较条件，则返回true，否则返回false
 */
function handleMoreParam(res, str, comp) {
  let arr = res.split("/");
  let arr0 = arr.every((item) => {
    switch (comp) {
      case ">":
        return item > handleFraction(str);
      case "<":
        return item < handleFraction(str);
      case "=":
        return item == handleFraction(str);
      case "≥":
        return item >= handleFraction(str);
      case "≤":
        return item <= handleFraction(str);
      case "±":
        let k = str.split("±");
        return (
          item >= eval(handleFraction(k[0]) - handleFraction(k[1])) &&
          item <=
            eval(Number(handleFraction(k[0])) + Number(handleFraction(k[1])))
        );
      case "~":
        let j = str.split("~");
        return (
          item >= eval(handleFraction(j[0])) &&
          item <= eval(handleFraction(j[1]))
        );
      case "-":
        let o = str.split("-");
        return (
          item >= eval(handleFraction(o[0])) &&
          item <= eval(handleFraction(o[1]))
        );
    }
  });
  if (arr0) {
    return true;
  } else {
    return false;
  }
}

/**
 * 获取参数
 *
 * @returns 返回处理后的参数对象
 */
function getParam() {
  tableList[0].arr.forEach((a) => {
    a.forEach((b) => {
      // 初始化传递到后端的参数
      if (param[b.i]) {
        param[b.i].insValue = [];
        param[b.i].comValue = [];
        param[b.i].equipValue = [];
        param[b.i].equipName = [];
        param[b.i].resValue = null;
        param[b.i].insResult = null;
      }
    });
  });
  tableList[0].arr.forEach((a) => {
    a.forEach((b) => {
      // 根据表格数据，赋值传递到后端的参数
      if (
        b.v.ps != undefined &&
        typeof b.v.ps.value == "string" &&
        b.v.ps.value.includes("检验值")
      ) {
        // 赋值检验值
        b.i && b.v.v && param[b.i].insValue.push(b);
      }
      if (b.v.ps != undefined && b.v.ps.value === "计算值") {
        // 赋值计算值
        b.i &&
          b.v.v &&
          b.valueList &&
          b.valueList.length > 0 &&
          param[b.i].comValue.push(b);
      }
      if (b.v.ps != undefined && b.v.ps.value === "设备编码") {
        // 赋值设备编码
        b.i && b.v && param[b.i].equipValue.push(b);
      }
      if (b.v.ps != undefined && b.v.ps.value === "设备名称") {
        // 赋值设备名称
        b.i && b.v && param[b.i].equipName.push(b);
      }
      if (b.v.ps != undefined && b.v.ps.value === "最终值") {
        // 赋值最终值
        b.i &&
          b.v &&
          b.valueList &&
          b.valueList.length > 0 &&
          (param[b.i].resValue = b);
      }
      if (b.v.ps != undefined && b.v.ps.value === "结论") {
        if (b.i && (b.v.v || b.v.v === 0 || b.v.v === "0")) {
          if (b.v.v != "合格" && b.v.v != "不合格") {
            // 赋值结论
            param[b.i].insResult = b;
          }
        }
      }
    });
  });
  return param;
}

/**
 * 计算多个数值的和
 *
 * @param val 数值型参数，可变长参数列表
 * @returns 返回所有参数的和，如果参数列表为空或参数类型非数值型，则返回null
 */
function SUM(...val) {
  try {
    let num = null;
    if (
      val != null &&
      val != undefined &&
      val != "undefined" &&
      val != "NaN" &&
      val.length > 0
    ) {
      val.forEach((item) => {
        num += item;
      });
    }
    return num;
  } catch (error) {
    console.log("error---", error);
  }
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
    if (
      val != null &&
      val != undefined &&
      val != "undefined" &&
      val != "NaN" &&
      val.length > 0
    ) {
      val = val.filter((item) => item != null && item !== "");
      if (val.length > 0) {
        max = Math.max(...val);
      } else {
        max = null;
      }
    }
    return max;
  } catch (error) {
    console.log("error---", error);
  }
}
/**
 * IF判断
 *
 *
 *
 */
function IF(...val) {
  try {
    let ifNUm = null;
    if (
      val != null &&
      val != undefined &&
      val != "undefined" &&
      val != "NaN" &&
      val.length > 0
    ) {
      if (val[0] === "" || val[0] == 0 || val[0] === " ") {
        ifNUm = val[1];
      } else {
        ifNUm = val[0];
      }
    }
    return ifNUm;
  } catch (error) {
    console.log("IFerror---", error);
  }
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
    if (
      val != null &&
      val != undefined &&
      val != "undefined" &&
      val != "NaN" &&
      val.length > 0
    ) {
      val = val.filter((item) => item != null && item !== "");
      if (val.length > 0) {
        min = Math.min(...val);
      }
    }
    return min;
  } catch (error) {
    console.log("error---", error);
  }
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
    if (
      val != null &&
      val != undefined &&
      val != "undefined" &&
      val != "NaN" &&
      val.length > 0
    ) {
      arr = val.filter(
        (item) => item !== null && item !== "" && item != undefined
      );
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
  } catch (error) {
    console.log("error---", error);
  }
}
/**
 * 取整数
 *
 * @param val 任意数值
 * @returns 返回该数值的整数
 */
function INT(val) {
  try {
    return Math.round(val);
  } catch (error) {
    console.log("error---", error);
  }
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
  } catch (error) {
    console.log("error---", error);
  }
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
  } catch (error) {
    console.log("error---", error);
  }
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
  } catch (error) {
    console.log("error---", error);
  }
}
/**
 * 转换π
 *
 *
 *
 */
function PI() {
  try {
    return 3.1415926;
  } catch (error) {
    console.log("error---", error);
  }
}
/**
 * 计算LOG
 *
 *
 *
 */
function LOG(...val) {
  try {
    let LOGNUm = null;
    if (
      val != null &&
      val != undefined &&
      val != "undefined" &&
      val != "NaN" &&
      val.length > 0
    ) {
      val = val.filter((item) => item != null && item !== "");
      if (val.length > 0) {
        LOGNUm = Math.log(val[0]);
      }
    }
    // console.log(LOGNUm);
    return LOGNUm;
  } catch (error) {
    console.log("error---", error);
  }
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
    console.log("error", e);
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
    console.log("error", e);
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
      // 判断是否为范围，列如MAX(A1:B2)
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
        // 没有则直接获取
        arr2.push(getIdFromColumnName(item));
      }
    });
    return arr2;
  } catch (error) {
    console.log("error", error);
  }
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
    // 上面计算函数新增后，这里也要同步增加
    let fouList = [
      "SUM",
      "MAX",
      "MIN",
      "AVERAGE",
      "ABS",
      "PI",
      "INT",
      "IF",
      "LOG",
    ];
    // 替换特殊字符
    f = f
      .replace(regex, ",")
      .replace(new RegExp('"&', "g"), "")
      .replace(new RegExp('&"', "g"), "");
    fouList.forEach((item) => {
      f = f.replace(new RegExp(item, "g"), ",");
    });
    let arr = f.split(",").filter((item) => {
      return (
        item && /[a-zA-Z]/.test(item) && item != "undefined" && item != "null"
      );
    });
    return arr;
  } catch (error) {
    console.log("error", error);
  }
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
  } catch (error) {
    console.log("error", error);
  }
}

/**
 * 计算函数
 *
 * @param f 字符串类型，表示待计算的公式
 * @param comValue 对象类型，表示要替换的单元格值，键为单元格名称，值为替换后的值
 * @returns 返回计算后的结果，如果计算失败则返回0
 */
function compute(f, comValue, isPoint) {
  try {
    let str = f;
    // 获取单元格对应值
    let arr = getAllCell(f);
    for (var a in comValue) {
      if (typeof comValue[a] == "string" && comValue[a].includes(">")) {
        comValue[a] = comValue[a].replace(">", "");
      }
      if (typeof comValue[a] == "string" && comValue[a].includes("≤")) {
        comValue[a] = comValue[a].replace("≤", "");
      }
      if (typeof comValue[a] == "string" && comValue[a].includes("≥")) {
        comValue[a] = comValue[a].replace("≥", "");
      }
      if (typeof comValue[a] == "string" && comValue[a].includes("<")) {
        comValue[a] = comValue[a].replace("<", "");
      }
      if (typeof comValue[a] == "string" && comValue[a].includes("＜")) {
        comValue[a] = comValue[a].replace("＜", "");
      }
      if (typeof comValue[a] == "string" && comValue[a].includes("＞")) {
        comValue[a] = comValue[a].replace("＞", "");
      }
      if (
        comValue[a] !== "undefined" &&
        comValue[a] !== "null" &&
        comValue[a] !== undefined
      ) {
        if (typeof comValue[a] == "string" && comValue[a].includes("^")) {
          // 计算幂次
          arr[a] = CalculatePower(comValue[a]);
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
    str = str.replace(new RegExp(":", "g"), "-");
    // 替换参数
    for (var a in obj) {
      str = str.replace(new RegExp(a, "g"), obj[a]);
    }
    // 计算
    for (var a in arr) {
      str = str.replace(new RegExp(a, "g"), arr[a]);
    }
    if (str.includes(",,")) {
      str = str.replace(new RegExp(",,", "g"), ",");
    }
    if (str.includes(",,")) {
      str = str.replace(new RegExp(",,", "g"), ",");
    }
    if (str.includes(",,")) {
      str = str.replace(new RegExp(",,", "g"), ",");
    }
    if (str.includes(",,")) {
      str = str.replace(new RegExp(",,", "g"), ",");
    }
    if (str.includes(",,")) {
      str = str.replace(new RegExp(",,", "g"), ",");
    }
    if (str.includes('&"/"&')) {
      // 计算带斜杠的
      return str
        .replace(new RegExp('&"/"&', "g"), "/")
        .replace(new RegExp("//", "g"), "");
    } else if (isPoint) {
      // 计算带小数点的
      return str.replace("ABS", "").replace(/\(|\)/g, "");
    } else {
      if (str.includes("IF")) {
        str = str.replace("IF", "").replace(/\(|\)/g, "");
        const str1 = str.split(",");
        if (str1[0] === " " || str1[0] === "") {
          str1[0] = 0;
        }
        if (str1[2] === " " || str1[2] === "") {
          str1[2] = 0;
        }
        const str2 = str1.join(",");
        str = "IF(" + str2 + ")";
      }
      // console.log('str---', str)
      if (str.includes("断裂") || str.includes("脆化")) {
        const result = str.replace(/[^\u4e00-\u9fa5]/g, "");
        if (result.includes("断裂") && !result.includes("脆化")) {
          return "断裂";
        } else if (!result.includes("断裂") && result.includes("脆化")) {
          return "脆化";
        } else if (result.includes("断裂") && result.includes("脆化")) {
          return "断裂脆化";
        }
      }
      // 计算常规的
      return eval(str);
    }
  } catch (error) {
    console.log("error", error);
  }
}
