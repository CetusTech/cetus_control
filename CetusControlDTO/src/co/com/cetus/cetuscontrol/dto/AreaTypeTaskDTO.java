package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class AreaTypeTaskDTO.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlDTO (14/04/2016)
 */
public class AreaTypeTaskDTO implements Serializable {
  private static final long serialVersionUID = 1L;
                                             
  private int               id;
  private Date              creationDate;
  private String            creationUser;
  private AreaDTO           area;
  private TaskTypeDTO       taskType;
                            
  public AreaTypeTaskDTO () {
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
  
  public AreaDTO getArea () {
    return area;
  }
  
  public void setArea ( AreaDTO area ) {
    this.area = area;
  }
  
  public TaskTypeDTO getTaskType () {
    return taskType;
  }
  
  public void setTaskType ( TaskTypeDTO taskType ) {
    this.taskType = taskType;
  }
  
}