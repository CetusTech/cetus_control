package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * The persistent class for the priority database table.
 * 
 */
@Entity
@NamedQueries ( {
  @NamedQuery ( name = "Priority.findAll", query = "SELECT p FROM Priority p" ),
  @NamedQuery ( name = "Priority.findPriorityByClientCetus", query = "SELECT p FROM Priority p WHERE p.clientCetus.id = :idClientCetus order by p.description" )
} )
public class Priority implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private int id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="CREATION_DATE")
  private Date creationDate;

  @Column(name="CREATION_USER")
  private String creationUser;

  private String description;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="MODIFICATION_DATE")
  private Date modificationDate;

  @Column(name="MODIFICATION_USER")
  private String modificationUser;

  private int value;

  //bi-directional many-to-one association to ClientCetus
  @ManyToOne
  @JoinColumn(name="ID_CLIENT_CETUS")
  private ClientCetus clientCetus;

  public Priority() {
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

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public ClientCetus getClientCetus() {
    return this.clientCetus;
  }

  public void setClientCetus(ClientCetus clientCetus) {
    this.clientCetus = clientCetus;
  }

}