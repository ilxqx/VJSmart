package cn.ixiaopeng.vj.smart.helper;

import cn.ixiaopeng.vj.smart.core.FormParam;
import cn.ixiaopeng.vj.smart.core.Params;
import cn.ixiaopeng.vj.smart.utils.ArrayUtil;
import cn.ixiaopeng.vj.smart.utils.CodecUtil;
import cn.ixiaopeng.vj.smart.utils.StreamUtil;
import cn.ixiaopeng.vj.smart.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 请求助手类
 * @author venus
 * @since 1.3.0
 * @version 1.0.0
 */
public final class RequestHelper {
    public static Params createParams (HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        formParamList.addAll(parseParameters(request));
        formParamList.addAll(parseInputStream(request));
        return new Params(formParamList);
    }

    /**
     * 获取请求参数
     * @param request 请求对象
     * @return List
     */
    private static List<FormParam> parseParameters (HttpServletRequest request) {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String fieldName = paramNames.nextElement();
            String[] fieldValues = request.getParameterValues(fieldName);
            if (ArrayUtil.isNotEmpty(fieldValues)) {
                Object fieldValue;
                if (fieldValues.length == 1) {
                    fieldValue = fieldValues[0];
                } else {
                    StringBuilder stringBuilder = new StringBuilder("");
                    for (int i = 0, len = fieldValues.length; i < len; i++) {
                        stringBuilder.append(fieldValues[i]);
                        if (i != len - 1) {
                            stringBuilder.append(StringUtil.SEPARATOR);
                        }
                    }
                    fieldValue = stringBuilder.toString();
                }
                formParamList.add(new FormParam(fieldName, fieldValue));
            }
        }
        return formParamList;
    }

    /**
     * 封装输入流参数
     * @param request 请求对象
     * @return List
     * @throws IOException 抛出异常
     */
    private static List<FormParam> parseInputStream (HttpServletRequest request) throws IOException {
        List<FormParam> formParamList = new ArrayList<FormParam>();
        String str = CodecUtil.decodeUrl(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(str)) {
            String[] keyValStrArr = StringUtil.split(str, "&");
            if (ArrayUtil.isNotEmpty(keyValStrArr)) {
                for (String keyValStr : keyValStrArr) {
                    String[] keyValArr = StringUtil.split(keyValStr, "=");
                    if (ArrayUtil.isNotEmpty(keyValArr) && keyValArr.length == 2) {
                        formParamList.add(new FormParam(keyValArr[0], keyValArr[1]));
                    }
                }
            }
        }
        return formParamList;
    }
}
