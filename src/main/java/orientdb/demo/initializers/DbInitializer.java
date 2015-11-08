package orientdb.demo.initializers;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.orientechnologies.orient.core.command.script.OCommandScript;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;

@Component("dbInitializer")
public class DbInitializer implements InitializingBean {

	@Autowired
	@Qualifier("dbHost")
	private String dbHost;
	
	@Autowired
	@Qualifier("dbName")
	private String dbName;
	
	@Autowired
	@Qualifier("dbUser")
	private String dbUser;
	
	@Autowired
	@Qualifier("dbPassword")
	private String dbPassword;
	
	@Autowired
	private ResourceLoader resourceLoader;

	
	@Override
	public void afterPropertiesSet() throws Exception {
		final String url = String.format("remote:%s/%s", dbHost, dbName);
		final OServerAdmin serverAdmin = new OServerAdmin(url);
		serverAdmin.connect(dbUser, dbPassword);
		boolean databaseExists = false;
		try {
			final Map<String,String> dbs = serverAdmin.listDatabases();
			databaseExists = dbs.keySet().contains(dbName);
		} finally {
			serverAdmin.close();
		}
		
		if(!databaseExists) {
			init();
		}
	}
	
	private void init() throws IOException {
		final String url = String.format("remote:%s/%s", dbHost, dbName);
		final OServerAdmin serverAdmin = new OServerAdmin(url);
		serverAdmin.connect(dbUser, dbPassword);
		
		try {
			serverAdmin.createDatabase(dbName,"graph", "memory");
		} finally {
			serverAdmin.close();
		}
		
		final ODatabaseDocumentTx db = new ODatabaseDocumentTx(url);
		db.open(dbUser, dbPassword);
		final org.springframework.core.io.Resource dbInitScriptFile = resourceLoader.getResource("classpath:dbInit.sql");
		String sql = IOUtils.toString(dbInitScriptFile.getInputStream());
		try {
			db.command(new OCommandScript("sql", sql)).execute();
		} finally {
			db.close();
		}
	}

}
