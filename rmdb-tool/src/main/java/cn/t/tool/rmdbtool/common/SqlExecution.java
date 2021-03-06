package cn.t.tool.rmdbtool.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Map;

public class SqlExecution {

    private static final Logger logger = LoggerFactory.getLogger(SqlExecution.class);

    private final JdbcHelper jdbcHelper;

    public void executeSql(String sql, ResultSetCallback callback, String... prepareSqls) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = jdbcHelper.getConnection();
            executePrepareSqls(jdbcHelper, conn, prepareSqls);
            stmt = conn.createStatement();
            logger.debug("\nsql: {}", sql);
            rs = stmt.executeQuery(sql);
            callback.callback(rs);
        } finally {
            jdbcHelper.release(conn, stmt, rs);
        }
    }

    public void executeSql(String sql, Map<Integer, String> param, ResultSetCallback callback, String... prepareSqls) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = jdbcHelper.getConnection();
            executePrepareSqls(jdbcHelper, conn, prepareSqls);
            stmt = conn.prepareStatement(sql);
            if(param != null) {
                for(Map.Entry<Integer, String> entry: param.entrySet()) {
                    stmt.setString(entry.getKey(), entry.getValue());
                }
            }
            logger.debug("\nsql: {}\nparam: {}", sql, param);
            rs = stmt.executeQuery();
            callback.callback(rs);
        } finally {
            jdbcHelper.release(conn, stmt, rs);
        }
    }

    private void executePrepareSqls(JdbcHelper jdbcHelper, Connection conn, String... prepareSqls) throws SQLException {
        if(prepareSqls != null && prepareSqls.length > 0) {
            Statement statement = null;
            try {
                statement = conn.createStatement();
                for(String str: prepareSqls) {
                    statement.addBatch(str);
                }
                statement.executeBatch();
            } finally {
                jdbcHelper.release(null, statement, null);
            }
        }
    }


    public SqlExecution(DbConfiguration configuration) {
        jdbcHelper = new JdbcHelper(configuration);
    }

    public JdbcHelper getJdbcHelper() {
        return jdbcHelper;
    }
}
