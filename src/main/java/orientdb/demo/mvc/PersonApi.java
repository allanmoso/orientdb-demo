package orientdb.demo.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import orientdb.demo.dto.PersonDto;
import orientdb.demo.frames.FrameFactory;
import orientdb.demo.frames.vertex.Person;

@RestController
@RequestMapping("person")
@Transactional
public class PersonApi {
	
	@Autowired
	private FrameFactory frameFactory;
	
	/**
	 * Create a new person
	 * @param personDto Person data.
	 */
	@RequestMapping(method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public PersonDto addPerson(@RequestBody final PersonDto personDto) {
		final Person person = frameFactory.createVertex(Person.class);
		person.setFirstName(personDto.getFirstName());
		person.setLastName(personDto.getLastName());
		return new PersonDto(person);
	}

	@RequestMapping(path="{personId}", method=RequestMethod.GET, produces="application/json")
	public PersonDto getPerson(@PathVariable("personId") final String personId) {
		final Person person = frameFactory.getByUniqueKey("personId", personId, Person.class);
		return new PersonDto(person);
	}
	
	/**
	 * Associate a child to a person.
	 * @param personId The ID of the person.
	 * @param childId The ID of the child.
	 */
	@RequestMapping(path="{personId}/child/{childId}", method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.OK)
	public void addChild(
			@PathVariable("personId") final String personId, 
			@PathVariable("childId") final String childId) {
		final Person person = frameFactory.getByUniqueKey("personId", personId, Person.class);
		final Person child = frameFactory.getByUniqueKey("personId", childId, Person.class);
		person.addChild(child);
	}
	
	/**
	 * Associate a parent to a person.
	 * @param personId The ID of the person.
	 * @param parenId The ID of the parent.
	 */
	@RequestMapping(path="{personId}/parent/{parentId}", method=RequestMethod.POST)
	@ResponseStatus(code=HttpStatus.OK)
	public void addParent(
			@PathVariable("personId") final String personId, 
			@PathVariable("parentId") final String parentId) {
		final Person person = frameFactory.getByUniqueKey("personId", personId, Person.class);
		final Person parent = frameFactory.getByUniqueKey("personId", parentId, Person.class);
		person.addParent(parent);
	}
}
