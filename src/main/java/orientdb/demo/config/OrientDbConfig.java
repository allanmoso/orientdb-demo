package orientdb.demo.config;

import org.ops4j.orient.spring.tx.OrientBlueprintsGraphFactory;
import org.ops4j.orient.spring.tx.OrientTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.PlatformTransactionManager;

import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

@Configuration
@DependsOn("dbInitializer")
public class OrientDbConfig {
	@Bean
	public String dbHost() {
		return "localhost:2424";
	}

	@Bean
	public String dbName() {
		return "demo";
	}
	
	@Bean
	public String dbUser() {
		return "root";
	}
	
	@Bean
	public String dbPassword() {
		return "demo";
	}
	
	/**
	 * This is the connection factory for OrientDB graph DB.
	 *
	 * @return A connection to the graph DB.
	 */
	@Bean
	public OrientBlueprintsGraphFactory databaseFactory() {
		final OrientBlueprintsGraphFactory manager = new OrientBlueprintsGraphFactory();
		manager.setUrl(String.format("remote:%s/%s", dbHost(), dbName()));
		manager.setUsername(dbUser());
		manager.setPassword(dbName());
		manager.setMinPoolSize(1);
		manager.setMaxPoolSize(10);
		return manager;
	}

	/**
	 * @return a Spring {@link PlatformTransactionManager} for OrientDB.
	 */
	@Bean
	public OrientTransactionManager transactionManager() {
		final OrientTransactionManager bean = new OrientTransactionManager();
		bean.setDatabaseManager(databaseFactory());
		return bean;
	}
	
	/**
	 * Returns the {@link FramedGraph} instance which allows access to the underlying Graph database
	 * through Frame interfaces (see Tinkerpop Frames).
	 *
	 * @return The {@link FramedGraph} which represents a connection to the graph DB.
	 */
	@Bean
	@Scope(scopeName="prototype")
	public FramedGraph<OrientGraph> getFramedGraph() {
		final FramedGraphFactory framedFactory = new FramedGraphFactory(new JavaHandlerModule());
		final OrientGraph baseGraph = databaseFactory().graph();
		final FramedTransactionalGraph<OrientGraph> framedGraph = framedFactory.create(baseGraph);
		return framedGraph;
	}
}
