package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the person database table.
 * 
 */
@Entity
@Table(name="PERSON")
@NamedQueries ( {
  @NamedQuery ( name = "Person.findAll", query = "SELECT p FROM Person p" ),
  @NamedQuery ( name = "Person.findPersonByClient", query = "select p from Person p where p.client.id = :idClient and p.master <> 1 order by p.names,p.lastNames" ),
  @NamedQuery ( name = "Person.findEmailsByClientCetus", query = "SELECT NEW java.lang.String(p.email) "
                                                                 + "FROM Person p where p.client.id =:idClientCetus ORDER BY p.email ASC" ),
  @NamedQuery ( name = "Person.findUserByPerson", query = "SELECT up FROM Person p, UserPortal up where  p.id  = up.person.id and p.id =:idPerson" ),
  @NamedQuery ( name = "Person.findPersonByGroup", query = "SELECT p FROM Person p, PersonGroup pg, GroupT g where g.id = pg.groupT.id and p.id = pg.person.id and g.id = :idGroup " ),
  @NamedQuery ( name = "Person.findPersonByClientCetus", query = "select p from Person p where p.client.clientCetus.id =:idClientCetus and p.master <> 1 order by p.names,p.lastNames" ) } )

public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String address;

	@Column(name="CELL_PHONE")
	private String cellPhone;

	private String code;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	private String email;

	private String identity;

	@Column(name="LAST_NAMES")
	private String lastNames;

	private int master;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	private String names;

	private String phone;

	//bi-directional many-to-one association to GroupT
	@OneToMany(mappedBy="person")
	private List<GroupT> groupTs;

	//bi-directional many-to-one association to Client
	@ManyToOne
	@JoinColumn(name="ID_CLIENT")
	private Client client;

	//bi-directional many-to-one association to PersonGroup
	@OneToMany(mappedBy="person")
	private List<PersonGroup> personGroups;

	//bi-directional many-to-one association to UserPortal
	@OneToMany(mappedBy="person")
	private List<UserPortal> userPortals;

	public Person() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreationUser() {
		return this.creationUser;
	}

	public void setCreationUser(String creationUser) {
		this.creationUser = creationUser;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentity() {
		return this.identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getLastNames() {
		return this.lastNames;
	}

	public void setLastNames(String lastNames) {
		this.lastNames = lastNames;
	}

	public int getMaster() {
		return this.master;
	}

	public void setMaster(int master) {
		this.master = master;
	}

	public Date getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getModificationUser() {
		return this.modificationUser;
	}

	public void setModificationUser(String modificationUser) {
		this.modificationUser = modificationUser;
	}

	public String getNames() {
		return this.names;
	}

	public void setNames(String names) {
		this.names = names;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<GroupT> getGroupTs() {
		return this.groupTs;
	}

	public void setGroupTs(List<GroupT> groupTs) {
		this.groupTs = groupTs;
	}

	public GroupT addGroupT(GroupT groupT) {
		getGroupTs().add(groupT);
		groupT.setPerson(this);

		return groupT;
	}

	public GroupT removeGroupT(GroupT groupT) {
		getGroupTs().remove(groupT);
		groupT.setPerson(null);

		return groupT;
	}

	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<PersonGroup> getPersonGroups() {
		return this.personGroups;
	}

	public void setPersonGroups(List<PersonGroup> personGroups) {
		this.personGroups = personGroups;
	}

	public PersonGroup addPersonGroup(PersonGroup personGroup) {
		getPersonGroups().add(personGroup);
		personGroup.setPerson(this);

		return personGroup;
	}

	public PersonGroup removePersonGroup(PersonGroup personGroup) {
		getPersonGroups().remove(personGroup);
		personGroup.setPerson(null);

		return personGroup;
	}

	public List<UserPortal> getUserPortals() {
		return this.userPortals;
	}

	public void setUserPortals(List<UserPortal> userPortals) {
		this.userPortals = userPortals;
	}

	public UserPortal addUserPortal(UserPortal userPortal) {
		getUserPortals().add(userPortal);
		userPortal.setPerson(this);

		return userPortal;
	}

	public UserPortal removeUserPortal(UserPortal userPortal) {
		getUserPortals().remove(userPortal);
		userPortal.setPerson(null);

		return userPortal;
	}

}