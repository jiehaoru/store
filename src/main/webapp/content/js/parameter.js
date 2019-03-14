/**
 * 获取get参数
 * @Author: huangliming@2019-03-12
 * 用法示例：GetParameter(window)
 * 返回值：包含此链接中所有Get参数的JSON对象
 * 示例代码：假设连接为http://domain.com/page.html?a=1&b=2&c=3
 * <script type="text/javascript">
 *     var params = GetParameter(window);
 *     // the variable params is {"a":1, "b":2, "c":3}
 * </script>
 */
function GetParameter(windowObject) {
    var search = windowObject.location.search.substr(1);
    var result = {};
    // 拆分参数对
    var params = search.split("&");
    for (var i in params) {
        var ps = params[i].split("=");
        result[ps[0]] = ps[1];
    }
    return result;
}