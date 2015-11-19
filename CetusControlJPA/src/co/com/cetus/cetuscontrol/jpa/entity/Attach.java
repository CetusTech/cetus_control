package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the attach database table.
 * 
 */
@Entity
@Table(name="ATTACH")
@NamedQuery(name="Attach.findAll", query="SELECT a FROM Attach a")
public class Attach implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATION_DATE")
	private Date creationDate;

	@Column(name="CREATION_USER")
	private String creationUser;

	@Column(name="FILE_NAME")
	private String fileName;

	@Lob
	@Column(name="OBJ_FILE")
	private byte[] objFile;

	//bi-directional many-to-one association to Task
	@ManyToOne
	@JoinColumn(name="ID_TASK")
	private Task task;

	public Attach() {
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

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getObjFile() {
		return this.objFile;
	}

	public void setObjFile(byte[] objFile) {
		this.objFile = objFile;
	}

	public Task getTask() {
		return this.task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

}