package cn.ixiaopeng.vj.smart.helper;

import cn.ixiaopeng.vj.smart.annotation.Entity;
import cn.ixiaopeng.vj.smart.utils.CastUtil;
import cn.ixiaopeng.vj.smart.utils.ReflectionUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作助手类
 * @author venus
 * @since 1.2.0
 * @version 1.1.0
 */
public final class DbHelper {
    /**
     * 定义类常亮
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbHelper.class);
    private static final QueryRunner QUERY_RUNNER;
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final BasicDataSource DATA_SOURCE;

    /**
     * 静态初始化块，用于初始化这些静态常量
     */
    static {
        CONNECTION_HOLDER = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(ConfigHelper.getJdbcDriver());
        DATA_SOURCE.setUrl(ConfigHelper.getJdbcUrl());
        DATA_SOURCE.setUsername(ConfigHelper.getJdbcUsername());
        DATA_SOURCE.setPassword(ConfigHelper.getJdbcPassword());
    }

    /**
     * 开启事务
     */
    public static void beginTrans () {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                LOGGER.error("Begin transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTrans () {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Commit transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTrans () {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Rollback transaction failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 获取数据库连接
     * @return 返回连接对象
     */
    public static Connection getConnection () {
        Connection connection = CONNECTION_HOLDER.get();
        if (connection == null) {
            try {
                connection = DATA_SOURCE.getConnection();
            } catch (SQLException e) {
                LOGGER.error("Get connection failure", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
        return connection;
    }

    /**
     * 查询实体列表
     * @param entityClass 实体类的类
     * @param sql 查询SQL
     * @param params 其他参数
     * @param <T> 实体类类型
     * @return 实体对象列表
     */
    public static <T> List<T> queryEntityList (Class<T> entityClass, String sql, Object... params) {
        List<T> entityList;
        try {
            entityList = QUERY_RUNNER.query(getConnection(), sql, new BeanListHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("Query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    /**
     * 查询实体
     * @param entityClass 实体类的类
     * @param sql 查询SQL
     * @param params 其他参数
     * @param <T> 实体类型
     * @return 实体对象
     */
    public static <T> T queryEntity (Class<T> entityClass, String sql, Object... params) {
        T entity;
        try {
            entity = QUERY_RUNNER.query(getConnection(), sql, new BeanHandler<T>(entityClass), params);
        } catch (SQLException e) {
            LOGGER.error("Query entity list failure", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    /**
     * 执行查询语句
     * @param sql SQL语句
     * @param params 绑定参数
     * @return 查询结果List
     */
    public static List<Map<String, Object>> executeQuery (String sql, Object... params) {
        List<Map<String, Object>> res;
        try {
            res = QUERY_RUNNER.query(getConnection(), sql, new MapListHandler(), params);
        } catch (SQLException e) {
            LOGGER.error("Execute query failure", e);
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 执行更新
     * @param sql SQL语句
     * @param params 绑定参数
     * @return 返回影响行数
     */
    public static int executeUpdate (String sql, Object... params) {
        int rows = 0;
        try {
            rows = QUERY_RUNNER.update(getConnection(), sql, params);
        } catch (SQLException e) {
            LOGGER.error("Execute update failure", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    /**
     * 获取表名
     * @param entityClass 实体类的类
     * @return 返回表名
     */
    public static String getTableName (Class<?> entityClass) {
        if (entityClass.isAnnotationPresent(Entity.class)) {
            return entityClass.getAnnotation(Entity.class).value();
        }
        return null;
    }

    /**
     * 插入实体
     * @param entityObj 实体类的对象
     * @param <T> 实体类型
     * @return 返回是否插入成功
     */
    public static <T> boolean insertEntity (T entityObj, String tableName) {
        Class<T> entityClass = CastUtil.cast(entityObj.getClass());
        Field[] fields = entityClass.getDeclaredFields();
        String sql = "INSERT INTO `" + tableName + "` ";
        StringBuilder columns = new StringBuilder("(`");
        StringBuilder values = new StringBuilder("(");
        List<Object> params = new LinkedList<Object>();
        for (Field field : fields) {
            String key = field.getName();
            if (key.equals("id")) {
                continue;
            }
            Object value = ReflectionUtil.getField(entityObj, field);
            if (value != null) {
                columns.append(key).append("`, `");
                values.append("?, ");
                params.add(value);
            }
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;
        return executeUpdate(sql, params.toArray()) == 1;
    }

    /**
     * 更新实体
     * @param entityObj 实体类的类
     * @param <T> 实体类型
     * @return 返回是否更新成功
     */
    public static <T> boolean updateEntity (T entityObj, String tableName) {
        Class<T> entityClass = CastUtil.cast(entityObj.getClass());
        Field[] fields = entityClass.getDeclaredFields();
        String sql = "UPDATE `" + tableName + "` SET `";
        StringBuilder columns = new StringBuilder();
        List<Object> params = new LinkedList<Object>();
        long id = -1;
        for (Field field : fields) {
            String key = field.getName();
            Object value = ReflectionUtil.getField(entityObj, field);
            if (value != null) {
                if (key.equals("id")) {
                    id = CastUtil.castLong(value);
                    continue;
                }
                columns.append(key).append("` = ?, `");
                params.add(value);
            }
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE `id` = ?";
        if (id == -1) {
            return false;
        }
        params.add(id);
        return executeUpdate(sql, params.toArray()) == 1;
    }

    /**
     * 删除实体
     * @param entityObj 实体类的类
     * @param <T> 实体类型
     * @return 返回是否删除成功
     */
    public static <T> boolean deleteEntity (T entityObj, String tableName) {
        Class<T> entityClass = CastUtil.cast(entityObj.getClass());
        Field[] fields = entityClass.getDeclaredFields();
        String sql = "DELETE FROM `" + tableName + "` WHERE `id` = ?";
        long id = -1;
        for (Field field : fields) {
            if (field.getName().equals("id")) {
                Object value = ReflectionUtil.getField(entityObj, field);
                if (value != null) {
                    id = CastUtil.castLong(value);
                    break;
                }
            }
        }
        return id != -1 && executeUpdate(sql, id) == 1;
    }

    public static void executeSqlFile (String sqlFilePath) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(sqlFilePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String sql;
            while ((sql = reader.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (IOException e) {
            LOGGER.error("Execute sql file failure", e);
            throw new RuntimeException(e);
        }
    }
}
