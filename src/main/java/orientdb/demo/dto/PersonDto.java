package orientdb.demo.dto;

import java.util.ArrayList;
import java.util.List;

import orientdb.demo.frames.vertex.Person;

public class PersonDto {
	private String id;
	private String firstName;
	private String lastName;
	private List<PersonDto> parents;
	private List<PersonDto> children;
	private List<PersonDto> grandChildren;
	private List<PersonDto> siblings;
	
	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public PersonDto() {
		//No-op
	}

	public PersonDto(Person person) {
		this(person, true);
	}
	
	public PersonDto(Person person, boolean deep) {
		this.id = person.getId();
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		
		if(deep) {
			this.parents = new ArrayList<>();
			this.children = new ArrayList<>();
			this.grandChildren =  new ArrayList<>();
			this.siblings =  new ArrayList<>();
			
			for(final Person parent : person.getParents()) {
				this.parents.add(new PersonDto(parent, false));
			}
			
			for(final Person child : person.getChildren()) {
				this.children.add(new PersonDto(child, false));
			}
			
			for(final Person grandChild : person.getGrandChildren()) {
				this.grandChildren.add(new PersonDto(grandChild, false));
			}
			
			for(final Person sibling : person.getSiblings()) {
				this.siblings.add(new PersonDto(sibling, false));
			}
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<PersonDto> getParents() {
		return parents;
	}

	public List<PersonDto> getChildren() {
		return children;
	}

	public List<PersonDto> getGrandChildren() {
		return grandChildren;
	}

	public List<PersonDto> getSiblings() {
		return siblings;
	}
}
