package cn.ixiaopeng.vj.smart.core;

import cn.ixiaopeng.vj.smart.helper.DbHelper;
import cn.ixiaopeng.vj.smart.utils.ArrayUtil;
import cn.ixiaopeng.vj.smart.utils.CastUtil;
import cn.ixiaopeng.vj.smart.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据操作类
 * @author venus
 * @since 1.5.0
 * @version 1.0.0
 */
public class Model <T> {
    // 日志类
    private static final Logger LOGGER = LoggerFactory.getLogger(Model.class);
    // 实体类
    private Class<T> entity;
    // 表名
    private String tableName;
    // 选择sql
    private String selectSql;
    // 删除sql
    private String deleteSql;
    // 统计sql
    private String countSql;

    // 条件语句
    private StringBuilder whereSql;
    // 拼接参数
    private List<Object> args = new LinkedList<Object>();
    // 条件计数
    private int conditionCount = 0;
    // OrderBy子句
    private StringBuilder orderBySql;
    //Order字段排序计数
    private int orderFieldCount = 0;
    // Limit子句
    private StringBuilder limitSql;

    /**
     * 构造函数
     * @param entity 实体类类型
     */
    public Model (Class<T> entity) {
        this.entity = entity;
        this.tableName = DbHelper.getTableName(entity);
        if (StringUtil.isEmpty(this.tableName)) {
            throw new NullPointerException("Table name is null");
        }
        // 初始化各类SQL
        this.selectSql = "SELECT * FROM `" + this.tableName + "`";
        this.deleteSql = "DELETE FROM `" + this.tableName + "`";
        this.countSql = "SELECT count(*) AS `total` FROM `" + this.tableName + "`";
        // 初始化动态变化SQL
        this.whereSql = new StringBuilder("");
        this.orderBySql = new StringBuilder("");
        this.limitSql = new StringBuilder("");
    }

    /**
     * 初始化条件
     */
    private void initCondition () {
        // where子句初始化
        whereSql.delete(0, whereSql.length());
        args.clear();
        conditionCount = 0;
        // orderBy子句初始化
        orderBySql.delete(0, orderBySql.length());
        orderFieldCount = 0;
        // limit子句初始化
        limitSql.delete(0, limitSql.length());
    }

    /**
     * 添加条件
     * @param name 字段名
     * @param operator 操作符
     * @param value 字段值
     * @return 对象本身
     */
    public Model where (String name, Operator operator, Object value) {
        if ((++conditionCount) == 1) {
            whereSql.append(" WHERE (`").append(name).append("` ").append(operator.getOp()).append(" ?");
        } else {
            whereSql.append(" AND `").append(name).append("` ").append(operator.getOp()).append(" ?");
        }
        args.add(value);
        return this;
    }

    public Model where (String name, Object value) {
        return where(name, Operator.EQ, value);
    }

    /**
     * 或条件
     * @param name 字段名
     * @param operator 操作符
     * @param value 字段值
     * @return 对象本身
     */
    public Model whereOr (String name, Operator operator, Object value) {
        if ((++conditionCount) == 1) {
            return this;
        } else {
            whereSql.append(") OR (`").append(name).append("` ").append(operator.getOp()).append(" ?");
            args.add(value);
        }
        return this;
    }

    public Model whereOr (String name, Object value) {
        return whereOr(name, Operator.EQ, value);
    }

    /**
     * 拼接In条件
     * @param name 字段名
     * @param values in的范围值
     * @return 对象本身
     */
    public Model whereIn (String name, Object... values) {
        if (ArrayUtil.isEmpty(values)) {
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder(" (");
        for (int i = 0, len = values.length; i < len; i++) {
            if (i == len - 1) {
                stringBuilder.append("?)");
            } else {
                stringBuilder.append("?, ");
            }
        }
        if ((++conditionCount) == 1) {
            whereSql.append(" WHERE (`").append(name).append("` IN ").append(stringBuilder);
        } else {
            whereSql.append(" AND `").append(name).append("` IN").append(stringBuilder);
        }
        args.addAll(Arrays.asList(values));
        return this;
    }

    /**
     * 排序
     * @param fieldName 字段名
     * @param isAsc 是否升序
     * @return 对象本身
     */
    public Model order (String fieldName, boolean isAsc) {
        if ((++orderFieldCount) == 1) {
            orderBySql.append(" ORDER BY `").append(fieldName).append("` ");
        } else {
            orderBySql.append(", `").append(fieldName).append("` ");
        }
        if (isAsc) {
            orderBySql.append("ASC");
        } else {
            orderBySql.append("DESC");
        }
        return this;
    }

    /**
     * 排序（默认升序）
     * @param fieldName 字段名
     * @return 对象本身
     */
    public Model order (String fieldName) {
        return order(fieldName, true);
    }

    /**
     * 限制数量
     * @param start 开始位置
     * @param num 记录条数
     * @return 对象本身
     */
    public Model limit (long start, long num) {
        if (limitSql.length() != 0) {
            limitSql.delete(0, limitSql.length());
        }
        limitSql.append(" LIMIT ").append(start).append(", ").append(num);
        return this;
    }

    /**
     * 限制数量
     * @param num 数量
     * @return 对象本身
     */
    public Model limit (long num) {
        limit(0, num);
        return this;
    }

    /**
     * 选择查询
     * @return List结果集
     */
    public List<T> select () {
        String sql = selectSql + whereSql.toString() + (conditionCount == 0 ? "" : ")") + orderBySql.toString() + limitSql.toString();
        LOGGER.debug(sql);
        List<T> res = DbHelper.queryEntityList(entity, sql, args.toArray());
        initCondition();
        return res;
    }

    /**
     * 统计查询
     * @return 统计数量
     */
    public long count () {
        String sql = countSql + whereSql.toString() + (conditionCount == 0 ? "" : ")");
        LOGGER.debug(sql);
        List<Map<String, Object>> res = DbHelper.executeQuery(sql, args.toArray());
        initCondition();
        return CastUtil.castLong(res.get(0).get("total"));
    }

    /**
     * 获取最大值
     * @param fieldName 字段名
     * @return double最大值
     */
    public double max (String fieldName) {
        if (StringUtil.isEmpty(fieldName)) {
            throw new RuntimeException("Field name can not be empty");
        }
        String sql = "SELECT MAX(`" + fieldName + "`) AS `result` FROM `" + tableName + "`" + whereSql.toString() + (conditionCount == 0 ? "" : ")");
        LOGGER.debug(sql);
        List<Map<String, Object>> res = DbHelper.executeQuery(sql, args.toArray());
        initCondition();
        return CastUtil.castDouble(res.get(0).get("result"));
    }

    /**
     * 获取最小值
     * @param fieldName 字段名
     * @return double最小值
     */
    public double min (String fieldName) {
        if (StringUtil.isEmpty(fieldName)) {
            throw new RuntimeException("Field name can not be empty");
        }
        String sql = "SELECT MIN(`" + fieldName + "`) AS `result` FROM `" + tableName + "`" + whereSql.toString() + (conditionCount == 0 ? "" : ")");
        LOGGER.debug(sql);
        List<Map<String, Object>> res = DbHelper.executeQuery(sql, args.toArray());
        initCondition();
        return CastUtil.castDouble(res.get(0).get("result"));
    }

    /**
     * 获取平均值
     * @param fieldName 字段名
     * @return double平均值
     */
    public double avg (String fieldName) {
        if (StringUtil.isEmpty(fieldName)) {
            throw new RuntimeException("Field name can not be empty");
        }
        String sql = "SELECT AVG(`" + fieldName + "`) AS `result` FROM `" + tableName + "`" + whereSql.toString() + (conditionCount == 0 ? "" : ")");
        LOGGER.debug(sql);
        List<Map<String, Object>> res = DbHelper.executeQuery(sql, args.toArray());
        initCondition();
        return CastUtil.castDouble(res.get(0).get("result"));
    }

    /**
     * 查询一条记录
     * @return 返回查询到的实体类
     */
    public T find () {
        String sql = selectSql + whereSql.toString() + (conditionCount == 0 ? "" : ")") + orderBySql.toString();
        LOGGER.debug(sql);
        T res = DbHelper.queryEntity(entity, sql, args.toArray());
        initCondition();
        return res;
    }

    /**
     * 删除记录
     * @return 布尔
     */
    public boolean delete () {
        String sql = deleteSql + whereSql.toString() + (conditionCount == 0 ? "" : ")");
        LOGGER.debug(sql);
        int res = DbHelper.executeUpdate(sql, args.toArray());
        initCondition();
        return res == 1;
    }

    /**
     * 根据id删除记录
     * @param id 记录id
     * @return 布尔
     */
    public boolean delById (long id) {
        return where("id", id).delete();
    }

    /**
     * 查找一条记录的实体根据id
     * @param id 记录id
     * @return 实体对象
     */
    public T findById (long id) {
        return CastUtil.cast(where("id", id).limit(1).find());
    }

    /**
     * 插入一条记录根据一个实体
     * @param entityObj 实体对象
     * @return 布尔
     */
    public boolean insert (T entityObj) {
        return DbHelper.insertEntity(entityObj, tableName);
    }

    /**
     * 更新一条记录根据一个实体
     * @param entityObj 实体对象
     * @return 布尔
     */
    public boolean update (T entityObj) {
        return DbHelper.updateEntity(entityObj, tableName);
    }

    public enum Operator {
        EQ("="), GT(">"), LT("<"), GTE(">="), LTE("<=");

        private String op;

        private Operator (String op) {
            this.op = op;
        }

        public String getOp() {
            return op;
        }
    }
}
