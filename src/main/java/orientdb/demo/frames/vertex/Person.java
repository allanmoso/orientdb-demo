package orientdb.demo.frames.vertex;

import java.util.UUID;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;
import com.tinkerpop.frames.modules.javahandler.Initializer;
import com.tinkerpop.frames.modules.javahandler.JavaHandler;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerContext;
import com.tinkerpop.pipes.PipeFunction;

@VertexDefinition("Person")
public interface Person extends VertexFrame, JavaHandlerContext<Vertex> {
	
	abstract class Impl implements Person {
		@Initializer
		public void init() {
			setId(UUID.randomUUID().toString());
		}
		
		@Override
		public Iterable<Person> getGrandChildren() {
			return frameVertices(
					gremlin().in("Parent").in("Parent")
				);
		}
		
		@Override
		public Iterable<Person> getSiblings() {
			return frameVertices(
					gremlin()
						.out("Parent")
						.in("Parent")
						.dedup()
						.filter(new PipeFunction<Vertex, Boolean>() {
							@Override
							public Boolean compute(Vertex argument) {
								return !argument.equals(Impl.this.asVertex());
							}
						})
				);
		}
		
	}
	
	@Property("personId")
	String getId();
	
	@Property("personId")
	void setId(String id);
	
	@Property("firstName")
	String getFirstName();
	
	@Property("firstName")
	void setFirstName(String firstName);
	
	@Property("lastName")
	String getLastName();
	
	@Property("lastName")
	void setLastName(String lastName);
	
	@Adjacency(label="Parent", direction=Direction.OUT)
	void addParent(Person person);
		
	@Adjacency(label="Parent", direction=Direction.IN)
	void addChild(Person person);
	
	@Adjacency(label="Parent", direction=Direction.OUT)
	Iterable<Person> getParents();
	
	@Adjacency(label="Parent", direction=Direction.IN)
	Iterable<Person> getChildren();
	
	@JavaHandler
	Iterable<Person> getGrandChildren();
	
	@JavaHandler
	Iterable<Person> getSiblings();
	
}
