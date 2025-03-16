/**
 * 数组乱序算法
 */
export function shuffle(arr: string[]): string[] {
    let length = arr.length, r = length, rand = 0;

    while (r) {
        rand = Math.floor(Math.random() * r--);
        [arr[r], arr[rand]] = [arr[rand], arr[r]];
    }
    return arr;
}

/**
 * 格式化时间，输出字符串
 *
 * @param   date    要格式化的时间
 * @param   formatString    返回时间的格式：
 * @return  格式化后的时间字符串
 * */
export function dateFormatter(date: Date, formatString: string): string {
    formatString = formatString ? formatString : 'yyyy-MM-dd hh:mm:ss';
    const dateRegArray: { [key: string]: number | string } = {
        "M+": date.getMonth() + 1,                      // 月份
        "d+": date.getDate(),                           // 日
        "h+": date.getHours(),                          // 小时
        "m+": date.getMinutes(),                        // 分
        "s+": date.getSeconds(),                        // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3),    // 季度
        "S": date.getMilliseconds()                     // 毫秒
    };
    if (/(y+)/.test(formatString)) {
        formatString = formatString.replace(RegExp.$1, (date.getFullYear() + "").substring(4 - RegExp.$1.length));
    }
    for (const section in dateRegArray) {
        if (new RegExp("(" + section + ")").test(formatString)) {
            const value = dateRegArray[section];
            formatString = formatString.replace(RegExp.$1, (RegExp.$1.length === 1) ? value.toString() : (("00" + dateRegArray[section]).substring(("" + dateRegArray[section]).length)));
        }
    }
    return formatString;
}

/**
 * @return：输出倒计时字符串 时时:分分:秒秒
 * @param timeLeft
 **/
export function formatTimeLeft(timeLeft: number): string {
    timeLeft = Math.floor(timeLeft / 1000);
    const minus = Math.floor(timeLeft / 60);
    const seconds = timeLeft % 60;
    return `${minus.toString().padStart(2, '00')}:${seconds.toString().padStart(2, '00')}`;
}
