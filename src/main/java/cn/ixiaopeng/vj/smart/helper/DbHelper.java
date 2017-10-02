package cn.ixiaopeng.vj.smart.helper;

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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作助手类
 * @author venus
 * @since 1.2.0
 * @version 1.0.0
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
    private static String getTableName (Class<?> entityClass) {
        return entityClass.getSimpleName();
    }

    /**
     * 插入实体
     * @param entityClass 实体类的类
     * @param fieldMap 属性map
     * @param <T> 实体类型
     * @return 返回是否插入成功
     */
    public static <T> boolean insertEntity (Class<T> entityClass, Map<String, Object> fieldMap) {
        if (fieldMap.isEmpty()) {
            LOGGER.error("Can not insert entity: fieldMap is empty");
            return false;
        }
        String sql = "INSERT INTO " + getTableName(entityClass) + " ";
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }
        columns.replace(columns.lastIndexOf(", "), columns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += columns + " VALUES " + values;
        Object[] params = fieldMap.values().toArray();
        return executeUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     * @param entityClass 实体类的类
     * @param id 记录的id
     * @param fieldMap 实体属性Map
     * @param <T> 实体类型
     * @return 返回是否更新成功
     */
    public static <T> boolean updateEntity (Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if (fieldMap.isEmpty()) {
            LOGGER.error("Can not update entity: fieldMap is empty");
            return false;
        }
        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();
        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(" = ?, ");
        }
        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id = ?";
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        return executeUpdate(sql, paramList.toArray()) == 1;
    }

    /**
     * 删除实体
     * @param entityClass 实体类的类
     * @param id 记录的id
     * @param <T> 实体类型
     * @return 返回是否删除成功
     */
    public static <T> boolean deleteEntity (Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE id = ?";
        return executeUpdate(sql, id) == 1;
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
