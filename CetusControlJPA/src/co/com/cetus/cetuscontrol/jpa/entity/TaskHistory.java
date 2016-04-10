package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the task_history database table.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlJPA (7/04/2016)
 */

@Entity
@Table ( name = "TASK_HISTORY" )
@NamedQueries ( {
                  @NamedQuery ( name = "TaskHistory.findAll", query = "SELECT t FROM TaskHistory t" ),
                  @NamedQuery ( name = "TaskHistory.findTAskHistory", query = "SELECT t FROM TaskHistory t WHERE t.idTtask = :idTask ORDER BY t.creationDate DESC" )
})
public class TaskHistory implements Serializable {
  private static final long serialVersionUID = 1L;
                                             
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int               id;
                            
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date              creationDate;
                            
  @Column ( name = "ID_TASK" )
  private int               idTtask;
                            
  @Column ( name = "TASK_OBJECT" )
  private String            taskObject;
                            
  public TaskHistory () {
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
  
  public int getIdTtask () {
    return idTtask;
  }
  
  public void setIdTtask ( int idTtask ) {
    this.idTtask = idTtask;
  }
  
  public String getTaskObject () {
    return this.taskObject;
  }
  
  public void setTaskObject ( String taskObject ) {
    this.taskObject = taskObject;
  }
  
}