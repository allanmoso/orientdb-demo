package it.orientdb.demo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.transaction.annotation.Transactional

import orientdb.demo.MainConfig
import orientdb.demo.mvc.PersonApi
import spock.lang.Specification

@ContextConfiguration(classes = [MainConfig])
@Transactional
class PersonApiTest extends Specification {
	@Autowired
	private PersonApi personApi;
	
	//add tests
}
