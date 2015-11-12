package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;


/**
 * The persistent class for the parameter_general database table.
 * 
 */
@Entity
@Table(name="PARAMETER_GENERAL")
@NamedQueries ( {
  @NamedQuery(name="ParameterGeneral.findAll", query="SELECT p FROM ParameterGeneral p"),
  @NamedQuery(name="ParameterGeneral.findParameterGeneralByClientCetus", query="SELECT p FROM ParameterGeneral p WHERE p.clientCetus.id = :idClientCetus "),
  @NamedQuery(name="ParameterGeneral.findTimeBeforeExpirationByClientCetus", query="SELECT p.timeBeforeExpiration FROM ParameterGeneral p WHERE p.clientCetus.id = :idClientCetus ")
} )
public class ParameterGeneral implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="COL_NUMBER_1")
	private int colNumber1;

	@Column(name="COL_NUMBER_2")
	private int colNumber2;

	@Column(name="COL_NUMBER_3")
	private int colNumber3;

	@Column(name="COL_NUMBERDC_1")
	private int colNumberdc1;

	@Column(name="COL_NUMBERDC_2")
	private int colNumberdc2;

	@Column(name="COL_NUMBERDC_3")
	private int colNumberdc3;

	@Column(name="COL_VARCHAR_1")
	private String colVarchar1;

	@Column(name="COL_VARCHAR_2")
	private String colVarchar2;

	@Column(name="COL_VARCHAR_3")
	private String colVarchar3;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	@Column(name="DAY_MONTH")
	private int dayMonth;

	@Column(name="HOUR_FORMAT")
	private String hourFormat;

	@Column(name="HOUR_WEEK")
	private int hourWeek;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="MODIFICATION_DATE")
	private Date modificationDate;

	@Column(name="MODIFICATION_USER")
	private String modificationUser;

	@Column(name="TIME_AFTER_EXPIRATION")
	private int timeAfterExpiration;

	@Column(name="TIME_BEFORE_EXPIRATION")
	private int timeBeforeExpiration;

	//bi-directional many-to-one association to ClientCetus
	@ManyToOne
	@JoinColumn(name="ID_CLIENT_CETUS")
	private ClientCetus clientCetus;

	public ParameterGeneral() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getColNumber1() {
		return this.colNumber1;
	}

	public void setColNumber1(int colNumber1) {
		this.colNumber1 = colNumber1;
	}

	public int getColNumber2() {
		return this.colNumber2;
	}

	public void setColNumber2(int colNumber2) {
		this.colNumber2 = colNumber2;
	}

	public int getColNumber3() {
		return this.colNumber3;
	}

	public void setColNumber3(int colNumber3) {
		this.colNumber3 = colNumber3;
	}

	public int getColNumberdc1() {
		return this.colNumberdc1;
	}

	public void setColNumberdc1(int colNumberdc1) {
		this.colNumberdc1 = colNumberdc1;
	}

	public int getColNumberdc2() {
		return this.colNumberdc2;
	}

	public void setColNumberdc2(int colNumberdc2) {
		this.colNumberdc2 = colNumberdc2;
	}

	public int getColNumberdc3() {
		return this.colNumberdc3;
	}

	public void setColNumberdc3(int colNumberdc3) {
		this.colNumberdc3 = colNumberdc3;
	}

	public String getColVarchar1() {
		return this.colVarchar1;
	}

	public void setColVarchar1(String colVarchar1) {
		this.colVarchar1 = colVarchar1;
	}

	public String getColVarchar2() {
		return this.colVarchar2;
	}

	public void setColVarchar2(String colVarchar2) {
		this.colVarchar2 = colVarchar2;
	}

	public String getColVarchar3() {
		return this.colVarchar3;
	}

	public void setColVarchar3(String colVarchar3) {
		this.colVarchar3 = colVarchar3;
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

	public int getDayMonth() {
		return this.dayMonth;
	}

	public void setDayMonth(int dayMonth) {
		this.dayMonth = dayMonth;
	}

	public String getHourFormat() {
		return this.hourFormat;
	}

	public void setHourFormat(String hourFormat) {
		this.hourFormat = hourFormat;
	}

	public int getHourWeek() {
		return this.hourWeek;
	}

	public void setHourWeek(int hourWeek) {
		this.hourWeek = hourWeek;
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

	public int getTimeAfterExpiration() {
		return this.timeAfterExpiration;
	}

	public void setTimeAfterExpiration(int timeAfterExpiration) {
		this.timeAfterExpiration = timeAfterExpiration;
	}

	public int getTimeBeforeExpiration() {
		return this.timeBeforeExpiration;
	}

	public void setTimeBeforeExpiration(int timeBeforeExpiration) {
		this.timeBeforeExpiration = timeBeforeExpiration;
	}

	public ClientCetus getClientCetus() {
		return this.clientCetus;
	}

	public void setClientCetus(ClientCetus clientCetus) {
		this.clientCetus = clientCetus;
	}

}