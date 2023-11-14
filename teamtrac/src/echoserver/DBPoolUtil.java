package echoserver;

import org.quickserver.net.*;
import org.quickserver.net.server.*;
import org.quickserver.sql.*;
import org.quickserver.util.xmlreader.*;

import java.io.*;
import java.util.logging.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBPoolUtil implements org.quickserver.sql.DBPoolUtil {
	private HashMap dbPool;

	public void setDatabaseConnections(Iterator iterator) 
			throws Exception {
		dbPool = new HashMap();
		while(iterator.hasNext()) {
			DatabaseConnectionConfig dcc = (DatabaseConnectionConfig)iterator.next();
			dbPool.put(dcc.getId(), dcc);
		}
	}

	public boolean initPool() {
		if(dbPool==null)
			throw new IllegalStateException("Call setDatabaseConnections first.!!");
		Iterator iterator = dbPool.keySet().iterator();
		try {
			while(iterator.hasNext()) {
				DatabaseConnectionConfig dcc = (DatabaseConnectionConfig) dbPool.get((String)iterator.next() );
				Class.forName(dcc.getDriver());
			}
			return true;
		} catch(Exception e) {
			System.err.println("In DBPoolUtil.initPool : "+e);
			return false;
		} 
	}

	public boolean clean() {
		dbPool = null;
		return true;
	}

	public Connection getConnection(String id) throws Exception {
		DatabaseConnectionConfig dcc = (DatabaseConnectionConfig) 
				dbPool.get(	id );
		return DriverManager.getConnection(dcc.getUrl(), dcc.getUsername(), 
			dcc.getPassword());
	}
}
