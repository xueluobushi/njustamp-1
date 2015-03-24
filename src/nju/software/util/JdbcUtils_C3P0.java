package nju.software.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * ���ݿ⹤�������� ʵ��������dao֮������ݽ���
 */
public class JdbcUtils_C3P0 {
	private static ThreadLocal<Connection> tLocal = new ThreadLocal<Connection>();
	private static ComboPooledDataSource ds = null;
	// �ھ�̬������д������ݿ����ӳ�
	static {
		try {
			// ͨ����ȡC3P0��xml�����ļ���������Դ��C3P0��xml�����ļ�c3p0-config.xml�������srcĿ¼��
			// ds = new ComboPooledDataSource();//ʹ��C3P0��Ĭ����������������Դ
			ds = new ComboPooledDataSource("MySQL");// ʹ��C3P0��������������������Դ

		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * @Method: getConnection
	 * @Description: ������Դ�л�ȡ���ݿ�����
	 * @return Connection
	 * @throws SQLException
	 */
	public static Connection getConnection() {
		// ������Դ�л�ȡ���ݿ�����
		try {
			Connection conn = tLocal.get();
			if (conn == null) {
				Connection c = ds.getConnection();
				tLocal.set(c);
				return c;
			}
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void release() {
		Connection conn = tLocal.get();
		if (conn != null) {
			try {
				conn.close();
				tLocal.remove();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	public static void rollback() {
		Connection conn = tLocal.get();
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void commit() {
		Connection conn = tLocal.get();
		if (conn != null) {
			try {
				conn.commit();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void startTransaction() {
		Connection conn = tLocal.get();
		if (conn == null) {
			conn = getConnection();
			tLocal.set(conn);
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Method: release
	 * @Description: �ͷ���Դ��
	 *               �ͷŵ���Դ����Connection���ݿ����Ӷ��󣬸���ִ��SQL�����Statement���󣬴洢��ѯ�����ResultSet����
	 * 
	 * @param conn
	 * @param st
	 * @param rs
	 */
	public static void release(Connection conn, Statement st, ResultSet rs) {
		if (rs != null) {
			try {
				// �رմ洢��ѯ�����ResultSet����
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			rs = null;
		}
		if (st != null) {
			try {
				// �رո���ִ��SQL�����Statement����
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (conn != null) {
			try {
				// ��Connection���Ӷ��󻹸����ݿ����ӳ�
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}