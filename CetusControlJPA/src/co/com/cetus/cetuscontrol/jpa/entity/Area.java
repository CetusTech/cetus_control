package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the area database table.
 * 
 */
@Entity
@NamedQueries ( {
                 @NamedQuery ( name = "Area.findAll", query = "SELECT a FROM Area a" ),
                 @NamedQuery ( name = "Area.findAreaByClientCetus", query = "SELECT a FROM Area a WHERE a.clientCetus.id = :idClientCetus order by a.description" )
} )
public class Area implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int               id;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date              creationDate;
  
  @Column ( name = "CREATION_USER" )
  private String            creationUser;
  
  private String            description;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "MODIFICATION_DATE" )
  private Date              modificationDate;
  
  @Column ( name = "MODIFICATION_USER" )
  private String            modificationUser;
  
  //bi-directional many-to-one association to ClientCetus
  @ManyToOne
  @JoinColumn ( name = "ID_CLIENT_CETUS" )
  private ClientCetus       clientCetus;
  
  //bi-directional many-to-one association to Task
  @OneToMany ( mappedBy = "area" )
  private List< Task >      tasks;
  
  public Area () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public Date getCreationDate () {
    return this.creationDate;
  }
  
  public void setCreationDate ( Date creationDate ) {
    this.creationDate = creationDate;
  }
  
  public String getCreationUser () {
    return this.creationUser;
  }
  
  public void setCreationUser ( String creationUser ) {
    this.creationUser = creationUser;
  }
  
  public String getDescription () {
    return this.description;
  }
  
  public void setDescription ( String description ) {
    this.description = description;
  }
  
  public Date getModificationDate () {
    return this.modificationDate;
  }
  
  public void setModificationDate ( Date modificationDate ) {
    this.modificationDate = modificationDate;
  }
  
  public String getModificationUser () {
    return this.modificationUser;
  }
  
  public void setModificationUser ( String modificationUser ) {
    this.modificationUser = modificationUser;
  }
  
  public ClientCetus getClientCetus () {
    return this.clientCetus;
  }
  
  public void setClientCetus ( ClientCetus clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
  public List< Task > getTasks () {
    return this.tasks;
  }
  
  public void setTasks ( List< Task > tasks ) {
    this.tasks = tasks;
  }
  
  public Task addTask ( Task task ) {
    getTasks().add( task );
    task.setArea( this );
    
    return task;
  }
  
  public Task removeTask ( Task task ) {
    getTasks().remove( task );
    task.setArea( null );
    
    return task;
  }
  
}