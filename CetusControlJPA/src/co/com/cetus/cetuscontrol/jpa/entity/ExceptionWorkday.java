package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the exception_workday database table.
 * 
 */
@Entity
@Table(name="EXCEPTION_WORKDAY")


@NamedQueries ( {
  @NamedQuery(name="ExceptionWorkday.findAll", query="SELECT e FROM ExceptionWorkday e"),
  @NamedQuery(name="ExceptionWorkday.findExcepWorkdayByClientCetus", query="SELECT e FROM ExceptionWorkday e WHERE e.clientCetus.id = :idClientCetus order by e.dateException desc")
} )

public class ExceptionWorkday implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATE_EXCEPTION")
	private Date dateException;

	private String description;

	private int jornada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	//bi-directional many-to-one association to ClientCetus
	@ManyToOne
	@JoinColumn(name="ID_CLIENT_CETUS")
	private ClientCetus clientCetus;

	public ExceptionWorkday() {
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

	public Date getDateException() {
		return this.dateException;
	}

	public void setDateException(Date dateException) {
		this.dateException = dateException;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getJornada() {
		return this.jornada;
	}

	public void setJornada(int jornada) {
		this.jornada = jornada;
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

	public ClientCetus getClientCetus() {
		return this.clientCetus;
	}

	public void setClientCetus(ClientCetus clientCetus) {
		this.clientCetus = clientCetus;
	}

}