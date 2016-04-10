package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class TaskHistoryDTO.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlDTO (7/04/2016)
 */
public class TaskHistoryDTO implements Serializable {
  private static final long serialVersionUID = 1L;
                                             
  private int               id;
  private Date              creationDate;
  private int               idTtask;
  private String            taskObject;
                            
  public TaskHistoryDTO () {
  }
  
  public int getId () {
    return id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public Date getCreationDate () {
    return creationDate;
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
    return taskObject;
  }
  
  public void setTaskObject ( String taskObject ) {
    this.taskObject = taskObject;
  }
  
}