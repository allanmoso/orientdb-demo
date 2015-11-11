package orientdb.demo.mvc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import orientdb.demo.dto.PersonDto;
import orientdb.demo.frames.FrameFactory;
import orientdb.demo.frames.vertex.Person;

/**
 * Person domain API.
 * 
 * @author Allan Moso
 *
 */
@RestController
@RequestMapping("person")
@Transactional
@Api("person")
public class PersonApi {
	
	@Autowired
	private FrameFactory frameFactory;
	
	/**
	 * Retrieve all the person information.
	 * @return All the person information.s
	 */
	@RequestMapping(method=RequestMethod.GET, produces="application/json")
	@ApiOperation("getAll")
	@ApiResponses({
		@ApiResponse(
				response=PersonDto.class,
				message="All person information.",
				code= 200)
	})
	@ResponseStatus(HttpStatus.OK)
	public List<PersonDto> getAll() {
		final Iterable<Person> persons = frameFactory.getAll(Person.class);
		final List<PersonDto> personDtos = new ArrayList<>();
		for(final Person person : persons) {
			personDtos.add(new PersonDto(person));
		}
		return personDtos;
	}
	
	/**
	 * Create a new person
	 * @param personDto Person data.
	 */
	@RequestMapping(method=RequestMethod.POST, consumes="application/json", produces="application/json")
	@ApiOperation("addPerson")
	@ApiResponses({
		@ApiResponse(
				response=PersonDto.class,
				message="The created person information.",
				code= 201)
	})
	@ResponseStatus(HttpStatus.CREATED)
	public PersonDto addPerson(@RequestBody final PersonDto personDto) {
		final Person person = frameFactory.createVertex(Person.class);
		person.setFirstName(personDto.getFirstName());
		person.setLastName(personDto.getLastName());
		person.setId(personDto.getFirstName() + person.getLastName());
		return new PersonDto(person);
	}

	/**
	 * Retrieve a person by ID.
	 * @param personId The person ID.
	 * @return The person information.
	 */
	@RequestMapping(path="{personId}", method=RequestMethod.GET, produces="application/json")
	@ApiOperation("getPerson")
	@ApiResponses({
		@ApiResponse(
				response=PersonDto.class,
				message="The referenced person information.",
				code= 200)
	})
	@ResponseStatus(HttpStatus.OK)
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
	@ApiOperation("addChild")
	@ApiResponses({
		@ApiResponse(
				response=Void.class,
				message="No response body. Child is added to person.",
				code= 201)
	})
	@ResponseStatus(HttpStatus.CREATED)
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
	@ApiOperation("addParent")
	@ApiResponses({
		@ApiResponse(
				response=Void.class,
				message="No response body. Parent is added to person.",
				code= 201)
	})
	@ResponseStatus(HttpStatus.CREATED)
	public void addParent(
			@PathVariable("personId") final String personId, 
			@PathVariable("parentId") final String parentId) {
		final Person person = frameFactory.getByUniqueKey("personId", personId, Person.class);
		final Person parent = frameFactory.getByUniqueKey("personId", parentId, Person.class);
		person.addParent(parent);
	}
}
