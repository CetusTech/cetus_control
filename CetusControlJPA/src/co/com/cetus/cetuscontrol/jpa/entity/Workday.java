package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the workday database table.
 * 
 */
@Entity
@Table(name="WORKDAY")
@NamedQueries ( {
  @NamedQuery(name="Workday.findAll", query="SELECT w FROM Workday w"),
  @NamedQuery(name="Workday.findWorkDayByClientCetus", query="SELECT w FROM Workday w WHERE w.clientCetus.id = :idClientCetus order by w.colDay, w.jornada"),
  @NamedQuery(name="Workday.findJornadaMax", query="SELECT MAX(w.endTime) FROM Workday w WHERE w.clientCetus.id = :idClientCetus AND w.colDay = :day "),
  @NamedQuery(name="Workday.findJornadaMin", query="SELECT MIN(w.startTime) FROM Workday w WHERE w.clientCetus.id = :idClientCetus AND w.colDay = :day ")
} )
public class Workday implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="COL_DAY")
	private String colDay;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	@Column(name="END_TIME")
	private int endTime;

	private int jornada;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	@Column(name="START_TIME")
	private int startTime;

	//bi-directional many-to-one association to ClientCetus
	@ManyToOne
	@JoinColumn(name="ID_CLIENT_CETUS")
	private ClientCetus clientCetus;

	public Workday() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColDay() {
		return this.colDay;
	}

	public void setColDay(String colDay) {
		this.colDay = colDay;
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

	public int getEndTime() {
		return this.endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
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

	public int getStartTime() {
		return this.startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public ClientCetus getClientCetus() {
		return this.clientCetus;
	}

	public void setClientCetus(ClientCetus clientCetus) {
		this.clientCetus = clientCetus;
	}

}