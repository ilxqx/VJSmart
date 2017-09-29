package cn.ixiaopeng.core;

import cn.ixiaopeng.utils.CastUtil;

import java.util.Map;

/**
 * 请求参数对象
 * @author venus
 * @since 1.0.0
 * @version 1.0.0
 */
public class Params {
    // 参数Map
    private Map<String, Object> paramMap;

    /**
     * 构造方法
     * @param paramMap 参数Map
     */
    public Params (Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 获取所有参数
     * @return 参数Map
     */
    public Map<String, Object> getAllParams () {
        return paramMap;
    }

    public long getLongParam (String name) {
        return CastUtil.castLong(paramMap.get(name));
    }

    public int getIntParam (String name) {
        return CastUtil.castInt(paramMap.get(name));
    }

    public String getStringParam (String name) {
        return CastUtil.castString(paramMap.get(name));
    }

    public boolean getBoolParam (String name) {
        return CastUtil.castBoolean(paramMap.get(name));
    }

    public double getDoubleParam (String name) {
        return CastUtil.castDouble(paramMap.get(name));
    }
}
