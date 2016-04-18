package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;

/**
 * The Class AreaTypeTask.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlJPA (14/04/2016)
 */
@Entity
@Table ( name = "AREA_TYPE_TASK" )
@NamedQueries ( {
                  @NamedQuery ( name = "AreaTypeTask.findAll", query = "SELECT a FROM AreaTypeTask a" ),
                  @NamedQuery ( name = "AreaTypeTask.findTaskTypeByArea", query = "SELECT a FROM AreaTypeTask a WHERE a.area.id = :idArea" )
})

public class AreaTypeTask implements Serializable {
  private static final long serialVersionUID = 1L;
                                             
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int               id;
                            
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date              creationDate;
                            
  @Column ( name = "CREATION_USER" )
  private String            creationUser;
                            
  //bi-directional many-to-one association to GroupT
  @ManyToOne
  @JoinColumn ( name = "ID_AREA" )
  private Area              area;
                            
  //bi-directional many-to-one association to Person
  @ManyToOne
  @JoinColumn ( name = "ID_TYPETASK" )
  private TaskType          taskType;
                            
  public AreaTypeTask () {
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
  
  public Area getArea () {
    return area;
  }
  
  public void setArea ( Area area ) {
    this.area = area;
  }
  
  public TaskType getTaskType () {
    return taskType;
  }
  
  public void setTaskType ( TaskType taskType ) {
    this.taskType = taskType;
  }
  
}