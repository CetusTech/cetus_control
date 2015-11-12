package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The persistent class for the workday database table.
 *
 * @author Andres Herrera - Cetus Technology
 * @version CetusControlDTO (7/10/2015)
 */
public class WorkdayDTO implements Serializable {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;
  
  /** The id. */
  private int               id;
  
  /** The col day. */
  private String            colDay;
  
  /** The creation date. */
  private Date              creationDate;
  
  /** The creation user. */
  private String            creationUser;
  
  /** The end time. */
  private int               endTime;
  
  /** The jornada. */
  private int               jornada;
  
  /** The modification date. */
  private Date              modificationDate;
  
  /** The modification user. */
  private String            modificationUser;
  
  /** The start time. */
  private int               startTime;
  
  /** The client cetus. */
  private ClientCetusDTO    clientCetus;
  
  /** The start time str. */
  private String            startTimeStr;
  
  /** The end time str. */
  private String            endTimeStr;
  
  /**
   * </p> Instancia un nuevo workday dto. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlDTO (7/10/2015)
   */
  public WorkdayDTO () {
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
   * </p> Gets the col day. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlDTO (7/10/2015)
   */
  public String getColDay () {
    return this.colDay;
  }
  
  /**
   * </p> Sets the col day. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param colDay the col day
   * @since CetusControlDTO (7/10/2015)
   */
  public void setColDay ( String colDay ) {
    this.colDay = colDay;
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
   * </p> Gets the end time. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el int
   * @since CetusControlDTO (7/10/2015)
   */
  public int getEndTime () {
    return this.endTime;
  }
  
  /**
   * </p> Sets the end time. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param endTime the end time
   * @since CetusControlDTO (7/10/2015)
   */
  public void setEndTime ( int endTime ) {
    this.endTime = endTime;
  }
  
  /**
   * </p> Gets the jornada. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el int
   * @since CetusControlDTO (7/10/2015)
   */
  public int getJornada () {
    return this.jornada;
  }
  
  /**
   * </p> Sets the jornada. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param jornada the jornada
   * @since CetusControlDTO (7/10/2015)
   */
  public void setJornada ( int jornada ) {
    this.jornada = jornada;
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
   * </p> Gets the start time. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el int
   * @since CetusControlDTO (7/10/2015)
   */
  public int getStartTime () {
    return this.startTime;
  }
  
  /**
   * </p> Sets the start time. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param startTime the start time
   * @since CetusControlDTO (7/10/2015)
   */
  public void setStartTime ( int startTime ) {
    this.startTime = startTime;
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
   * </p> Gets the start time str. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlDTO (7/10/2015)
   */
  public String getStartTimeStr () {
    return startTimeStr;
  }
  
  /**
   * </p> Sets the start time str. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param startTimeStr the start time str
   * @since CetusControlDTO (7/10/2015)
   */
  public void setStartTimeStr ( String startTimeStr ) {
    this.startTimeStr = startTimeStr;
  }
  
  /**
   * </p> Gets the end time str. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el string
   * @since CetusControlDTO (7/10/2015)
   */
  public String getEndTimeStr () {
    return endTimeStr;
  }
  
  /**
   * </p> Sets the end time str. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param endTimeStr the end time str
   * @since CetusControlDTO (7/10/2015)
   */
  public void setEndTimeStr ( String endTimeStr ) {
    this.endTimeStr = endTimeStr;
  }
  
}