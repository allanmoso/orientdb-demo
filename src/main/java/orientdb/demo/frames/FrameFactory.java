package orientdb.demo.frames;

import java.util.Iterator;

import org.ops4j.orient.spring.tx.OrientBlueprintsGraphFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphQuery;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.FramedTransactionalGraph;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;
import com.tinkerpop.gremlin.java.GremlinPipeline;

import orientdb.demo.frames.vertex.VertexDefinition;

@Component
@Transactional
public class FrameFactory {
	@Autowired
	private OrientBlueprintsGraphFactory graphFactory;
	
	/**
	 * Create a new vertex based on a provided {@link VertexFrame} class. 
	 * @param vertexFrameClass The class that extends {@link VertexFrame}. Must also have {@link VertexDefinition}.
	 * @return instance of vertexFrameClass. This represents a vertex in the database.
	 */
	public <T extends VertexFrame> T createVertex(final Class<T> vertexFrameClass) {
		final VertexDefinition vertexDefinition = vertexFrameClass.getAnnotation(VertexDefinition.class);
		if(vertexDefinition == null) {
			throw new RuntimeException("Missing VertexDefinition annotation on VertexFrame class: " + vertexFrameClass.getName());
		}
		
		final String orientVertexClass = String.format("class:%s,null", vertexDefinition.value());
		return getFramedGraph().addVertex(orientVertexClass, vertexFrameClass);
	}
	
	/**
	 * Gets all vertices of the type.
	 *
	 * @return collection of all vertex instances of type
	 */
	public <T extends VertexFrame> Iterable<T> getAll(Class<T> vClass) {
		final OrientGraphQuery query = (OrientGraphQuery) getFramedGraph().getBaseGraph().query();
		final VertexDefinition vertexDefinition = vClass.getAnnotation(VertexDefinition.class);
		final String vertexName = vertexDefinition.value();
		final Iterable<Vertex> vertices = query.labels(vertexName).vertices();
		return getFramedGraph().frameVertices(vertices, vClass);
	}
	
	/**
	 * Retrieve a {@link Vertex} with a unique key-value pair.
	 *
	 * @param key
	 *            The key of the unique property on the vertex.
	 * @param value
	 *            The value of the unique property on the vertex
	 *
	 * @return Returns the {@link Vertex} with the unique key or null.
	 */
	public <T extends VertexFrame> T getByUniqueKey(final String key, final Object value, Class<T> vClass) {
		final VertexDefinition vertexDefinition = vClass.getAnnotation(VertexDefinition.class);
		if (vertexDefinition == null) {
			throw new IllegalStateException("No VertexDefinition annotation found on Vertex class: " + vClass);
		}
		final String vName = vertexDefinition.value();
		final GremlinPipeline<Iterable<com.tinkerpop.blueprints.Vertex>, com.tinkerpop.blueprints.Vertex> gremlin = new GremlinPipeline<>();

		//@formatter:off
		final Iterator<com.tinkerpop.blueprints.Vertex> vertexIterator =
				gremlin
						.start(getFramedGraph().getBaseGraph().getVerticesOfClass(vName))
						.has(key, value)
						.cast(com.tinkerpop.blueprints.Vertex.class)
						.iterator();
		//@formatter:on

		T framedVertex = null;
		if (vertexIterator.hasNext()) {
			framedVertex = getFramedGraph().frame(vertexIterator.next(), vClass);
		}
		if (vertexIterator.hasNext()) {
			throw new RuntimeException("A unique key lookup returned multiple results.");
		}
		return framedVertex;
	}
	
	private FramedGraph<OrientGraph> getFramedGraph() {
		final FramedGraphFactory framedFactory = new FramedGraphFactory(new JavaHandlerModule());
		final OrientGraph baseGraph = graphFactory.graph();
		final FramedTransactionalGraph<OrientGraph> framedGraph = framedFactory.create(baseGraph);
		return framedGraph;
	}
}
