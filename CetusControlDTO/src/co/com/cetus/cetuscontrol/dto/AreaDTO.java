package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The persistent class for the area database table.
 *
 * @author Andres Herrera - Cetus Technology
 * @version CetusControlDTO (7/10/2015)
 */
public class AreaDTO implements Serializable {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  
  /** The id. */
  private int               id;
  
  /** The creation date. */
  private Date              creationDate;
  
  /** The creation user. */
  private String            creationUser;
  
  /** The description. */
  private String            description;
  
  /** The modification date. */
  private Date              modificationDate;
  
  /** The modification user. */
  private String            modificationUser;
  
  /** The client cetus. */
  private ClientCetusDTO    clientCetus;
  
  /** The tasks. */
  private List< TaskDTO >   tasks;
  
  private List<AreaTypeTaskDTO> areaTypeTask;
  
  /**
   * </p> Instancia un nuevo area dto. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlDTO (7/10/2015)
   */
  public AreaDTO () {
  }
  
  /**
   * </p> Gets the id. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el int
   * @since CetusControlDTO (7/10/2015)
   */
  public int getId () {
    return this.id;
  }
  
  /**
   * </p> Sets the id. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param id the id
   * @since CetusControlDTO (7/10/2015)
   */
  public void setId ( int id ) {
    this.id = id;
  }
  
  /**
   * </p> Gets the creation date. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el date
   * @since CetusControlDTO (7/10/2015)
   */
  public Date getCreationDate () {
    return this.creationDate;
  }
  
  /**
   * </p> Sets the creation date. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param creationDate the creation date
   * @since CetusControlDTO (7/10/2015)
   */
  public void setCreationDate ( Date creationDate ) {
    this.creationDate = creationDate;
  }
  
  /**
   * </p> Gets the creation user. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlDTO (7/10/2015)
   */
  public String getCreationUser () {
    return this.creationUser;
  }
  
  /**
   * </p> Sets the creation user. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param creationUser the creation user
   * @since CetusControlDTO (7/10/2015)
   */
  public void setCreationUser ( String creationUser ) {
    this.creationUser = creationUser;
  }
  
  /**
   * </p> Gets the description. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlDTO (7/10/2015)
   */
  public String getDescription () {
    return this.description;
  }
  
  /**
   * </p> Sets the description. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param description the description
   * @since CetusControlDTO (7/10/2015)
   */
  public void setDescription ( String description ) {
    this.description = description;
  }
  
  /**
   * </p> Gets the modification date. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el date
   * @since CetusControlDTO (7/10/2015)
   */
  public Date getModificationDate () {
    return this.modificationDate;
  }
  
  /**
   * </p> Sets the modification date. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param modificationDate the modification date
   * @since CetusControlDTO (7/10/2015)
   */
  public void setModificationDate ( Date modificationDate ) {
    this.modificationDate = modificationDate;
  }
  
  /**
   * </p> Gets the modification user. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlDTO (7/10/2015)
   */
  public String getModificationUser () {
    return this.modificationUser;
  }
  
  /**
   * </p> Sets the modification user. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param modificationUser the modification user
   * @since CetusControlDTO (7/10/2015)
   */
  public void setModificationUser ( String modificationUser ) {
    this.modificationUser = modificationUser;
  }
  
  /**
   * </p> Gets the client cetus. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el client cetus dto
   * @since CetusControlDTO (7/10/2015)
   */
  public ClientCetusDTO getClientCetus () {
    return this.clientCetus;
  }
  
  /**
   * </p> Sets the client cetus. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param clientCetus the client cetus
   * @since CetusControlDTO (7/10/2015)
   */
  public void setClientCetus ( ClientCetusDTO clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
  /**
   * </p> Gets the tasks. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlDTO (7/10/2015)
   */
  public List< TaskDTO > getTasks () {
    return this.tasks;
  }
  
  /**
   * </p> Sets the tasks. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param tasks the tasks
   * @since CetusControlDTO (7/10/2015)
   */
  public void setTasks ( List< TaskDTO > tasks ) {
    this.tasks = tasks;
  }
  
  /**
   * </p> Adds the task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param task the task
   * @return el task dto
   * @since CetusControlDTO (7/10/2015)
   */
  public TaskDTO addTask ( TaskDTO task ) {
    getTasks().add( task );
    task.setArea( this );
    
    return task;
  }
  
  /**
   * </p> Removes the task. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param task the task
   * @return el task dto
   * @since CetusControlDTO (7/10/2015)
   */
  public TaskDTO removeTask ( TaskDTO task ) {
    getTasks().remove( task );
    task.setArea( null );
    
    return task;
  }

  public List< AreaTypeTaskDTO > getAreaTypeTask () {
    return areaTypeTask;
  }

  public void setAreaTypeTask ( List< AreaTypeTaskDTO > areaTypeTask ) {
    this.areaTypeTask = areaTypeTask;
  }

}