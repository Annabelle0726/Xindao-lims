/**
 * 获取年月日
 */
export function getYearAndMonthAndDays(date=new Date()) {
  let year = date.getFullYear()
  let month = date.getMonth() + 1
  if (month < 10) {
    month = '0' + month + '-'
  } else {
    month = month + '-'
  }
  year = year + '-'
  let days = date.getDate()
  if (days < 10) {
    days = '0' + days
  } else {
    days = days
  }
  return (year + month + days)
}
/**
 * 日期格式化
 */
export function dateFormat(date, format = 'yyyy-MM-dd hh:mm:ss') {
  if (date !== 'Invalid Date') {
    var o = {
      'M+': date.getMonth() + 1, // month
      'd+': date.getDate(), // day
      'h+': date.getHours(), // hour
      'm+': date.getMinutes(), // minute
      's+': date.getSeconds(), // second
      'q+': Math.floor((date.getMonth() + 3) / 3), // quarter
      S: date.getMilliseconds() // millisecond
    }
    if (/(y+)/.test(format)) {
      format = format.replace(
        RegExp.$1,
        (date.getFullYear() + '').substr(4 - RegExp.$1.length)
      )
    }
    for (var k in o) {
      if (new RegExp('(' + k + ')').test(format)) {
        format = format.replace(
          RegExp.$1,
          RegExp.$1.length === 1
            ? o[k]
            : ('00' + o[k]).substr(('' + o[k]).length)
        )
      }
    }
    return format
  }
  return ''
}

/**
 * 获取年月日  26号+1
 * @param date
 * @returns {string}
 */
export function getYearAndMonthAndDaysZTZB(date = new Date()) {
  let year = date.getFullYear();
  let month = date.getMonth();
  let days = date.getDate();

  // 判断是否是26号
  if (days === 26) {
    month++;
    if (month > 11) {
      month = 0;
      year++;
    }
  }

  month += 1;
  month = month < 10 ? '0' + month + '-' : month + '-';
  year = year + '-';
  days = days < 10 ? '0' + days : days;

  return (year + month + days);
}