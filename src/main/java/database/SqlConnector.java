package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dataHolder.SrvAccInfo;
import dataHolder.TarSrvAccInfo;
import dataHolder.Token;

public class SqlConnector {
	// The JDBC Connector Class.
	private static final String dbClassName = "com.mysql.jdbc.Driver";

	// Connection string. 5221Project is the database the program
	// is connecting to. You can include user and password after this
	// by adding (say) ?user=cloud&password=cloud. Not recommended!

	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/5221Project";

	// List all service accounts
	public static List<SrvAccInfo> srvAcctList() throws ClassNotFoundException,
			SQLException {
		List<SrvAccInfo> acctList = new ArrayList<SrvAccInfo>();

		Class.forName(dbClassName);

		Properties p = new Properties();
		p.put("user", "cloud");
		p.put("password", "cloud");

		// Now try to connect
		Connection c = DriverManager.getConnection(CONNECTION, p);

		if (!c.isClosed())
			System.out.println("database connected !");

		String getAllAcctInfo = "SELECT * FROM service_account";

		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(getAllAcctInfo);

		while (rs.next()) {
			SrvAccInfo tar = new SrvAccInfo(rs.getString(1), rs.getString(2));
			acctList.add(tar);
		}

		c.close();

		return acctList;
	}

	// get source/destination tokens
	public static Token get_tokens(String srcSrvID, String dstSrvID)
			throws ClassNotFoundException, SQLException {
		String src_access_token = "";
		String dst_access_token = "";
		String src_refresh_token = "";
		String dst_refresh_token = "";

		Class.forName(dbClassName);

		Properties p = new Properties();
		p.put("user", "cloud");
		p.put("password", "cloud");

		// Now try to connect
		Connection c = DriverManager.getConnection(CONNECTION, p);

		if (!c.isClosed())
			System.out.println("database connected !");

		String get_src_token = "SELECT * FROM service_account WHERE srvacc_id = '"
				+ srcSrvID + "'";
		String get_dst_token = "SELECT * FROM service_account WHERE srvacc_id = '"
				+ dstSrvID + "'";

		Statement stmt = c.createStatement();
		Statement stmt2 = c.createStatement();
		ResultSet src_rs = stmt.executeQuery(get_src_token);
		ResultSet dst_rs = stmt2.executeQuery(get_dst_token);

		while (src_rs.next()) {
			src_access_token = src_rs.getString(3);
			src_refresh_token = src_rs.getString(4);
		}
		while (dst_rs.next()) {
			dst_access_token = dst_rs.getString(3);
			dst_refresh_token = dst_rs.getString(4);
		}
		c.close();
		Token t = new Token(src_access_token, dst_access_token,
				src_refresh_token, dst_refresh_token);
		return t;
	}

	// create new service account
	public static String newSrvAcct(String name, String access_token,
			String refresh_token) throws ClassNotFoundException, SQLException {

		Class.forName(dbClassName);

		Properties p = new Properties();
		p.put("user", "cloud");
		p.put("password", "cloud");

		// Now try to connect
		Connection c = DriverManager.getConnection(CONNECTION, p);

		if (!c.isClosed())
			System.out.println("Service account added !");

		Statement stmt = c.createStatement();

		String updata_srv_acct = "INSERT INTO service_account (name, access_token, refresh_token) VALUES ('"
				+ name + "', '" + access_token + "', '" + refresh_token + "')";

		stmt.executeUpdate(updata_srv_acct);

		String get_src_id = "SELECT * FROM service_account WHERE name = '"
				+ name + "' AND access_token ='" + access_token + "'";

		Statement stmt2 = c.createStatement();

		ResultSet src_id = stmt2.executeQuery(get_src_id);

		int id = 0;

		while (src_id.next()) {
			id = src_id.getInt(1);
		}

		c.close();

		String sid = new Integer(id).toString();
		return sid;

	}

	// update rules of service account
	public static void newRule(String srvacc_id, List<TarSrvAccInfo> tarSrv)
			throws ClassNotFoundException, SQLException {

		Class.forName(dbClassName);

		Properties p = new Properties();
		p.put("user", "cloud");
		p.put("password", "cloud");

		// Now try to connect
		Connection c = DriverManager.getConnection(CONNECTION, p);

		Statement stmt = c.createStatement();

		String updata_rule = "";

		if (tarSrv != null) {
			for (int i = 0; i < tarSrv.size(); i++) {
				updata_rule = "INSERT INTO sync_rule (srvacc_id, dst_srv_acc_id, time) VALUES ('"
						+ srvacc_id
						+ "','"
						+ tarSrv.get(i).getTarSrvAccID()
						+ "','" + tarSrv.get(i).getTime() + "')";
				stmt.executeUpdate(updata_rule);
			}

			System.out.println("Rule added !");
		}
		c.close();
	}

	// update service account
	public static void updateAcct(String srvacc_id, String name,
			List<TarSrvAccInfo> tarSrv) throws ClassNotFoundException,
			SQLException {

		Class.forName(dbClassName);

		Properties p = new Properties();
		p.put("user", "cloud");
		p.put("password", "cloud");

		// Now try to connect
		Connection c = DriverManager.getConnection(CONNECTION, p);

		if (!c.isClosed())
			System.out.println("Account updated successfully !");

		Statement stmt = c.createStatement();
		String update_acct = "";
		String delete_rule = "";
		String updata_rule = "";

		update_acct = "UPDATE service_account SET name='" + name
				+ "' WHERE srvacc_id='" + srvacc_id + "'";

		delete_rule = "DELETE FROM sync_rule WHERE srvacc_id='" + srvacc_id
				+ "'";

		stmt.executeUpdate(update_acct);
		stmt.executeUpdate(delete_rule);
		if (tarSrv != null) {
			for (int i = 0; i < tarSrv.size(); i++) {
				updata_rule = "INSERT INTO sync_rule (srvacc_id, dst_srv_acc_id, time) VALUES ('"
						+ srvacc_id
						+ "','"
						+ tarSrv.get(i).getTarSrvAccID()
						+ "','" + tarSrv.get(i).getTime() + "')";
				stmt.executeUpdate(updata_rule);
			}
		}
		c.close();
	}

	public static String getTokens(String srcSrvID) {

		String src_refresh_token = null;
		try {
			Class.forName(dbClassName);

			Properties p = new Properties();
			p.put("user", "cloud");
			p.put("password", "cloud");

			// Now try to connect
			Connection c = DriverManager.getConnection(CONNECTION, p);

			if (!c.isClosed())
				System.out.println("database connected !");

			String get_src_token = "SELECT * FROM service_account WHERE srvacc_id = '"
					+ srcSrvID + "'";

			Statement stmt = c.createStatement();
			ResultSet src_rs = stmt.executeQuery(get_src_token);

			while (src_rs.next()) {
				src_refresh_token = src_rs.getString(4);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return src_refresh_token;
	}
}
