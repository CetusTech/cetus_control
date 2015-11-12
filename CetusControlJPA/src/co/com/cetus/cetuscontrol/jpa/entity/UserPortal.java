package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the user_portal database table.
 * 
 */
@Entity
@Table ( name = "USER_PORTAL" )
@NamedQueries ( {
                 @NamedQuery ( name = "UserPortal.findAll", query = "SELECT u FROM UserPortal u" ),
                 @NamedQuery ( name = "UserPortal.searchUserLogged", query = "SELECT u FROM UserPortal u where u.loginCetus = :userCetus and u.status = :status" )
} )
public class UserPortal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	@Column(name="LOGIN_CETUS")
	private String loginCetus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	private int status;

	//bi-directional many-to-one association to Person
	@ManyToOne
	@JoinColumn(name="ID_PERSON")
	private Person person;

	public UserPortal() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getLoginCetus() {
		return this.loginCetus;
	}

	public void setLoginCetus(String loginCetus) {
		this.loginCetus = loginCetus;
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

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}