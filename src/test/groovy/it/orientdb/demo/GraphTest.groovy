package it.orientdb.demo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional

import orientdb.demo.MainConfig
import orientdb.demo.config.OrientDbConfig;
import orientdb.demo.frames.FrameFactory
import orientdb.demo.frames.vertex.Person;
import spock.lang.Specification

@ContextConfiguration(classes = [MainConfig])
@Transactional
class GraphTest extends Specification{
	@Autowired
	private FrameFactory frameFactory;

	def "create vertex"() {
		when:
		Person employee = frameFactory.createVertex(Person)
		
		then:
		employee != null
	}
}
