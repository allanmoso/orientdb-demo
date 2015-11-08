package orientdb.demo.frames.vertex;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import groovy.swing.factory.FrameFactory;

/**
 * Annotation containing information, such as the vertex name, that is needed in the {@link FrameFactory}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VertexDefinition {
	/**
	 * Vertex name.
	 */
	String value();
}
