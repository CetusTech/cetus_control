package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the exception_workday database table.
 * 
 */

public class ExceptionWorkdayDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private Date              dateException;
  
  private String            description;
  
  private int               jornada;
  
  private Date              modificationDate;
  
  private String            modificationUser;
  
  private ClientCetusDTO    clientCetus;
  
  public ExceptionWorkdayDTO () {
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
  
  public Date getDateException () {
    return this.dateException;
  }
  
  public void setDateException ( Date dateException ) {
    this.dateException = dateException;
  }
  
  public String getDescription () {
    return this.description;
  }
  
  public void setDescription ( String description ) {
    this.description = description;
  }
  
  public int getJornada () {
    return this.jornada;
  }
  
  public void setJornada ( int jornada ) {
    this.jornada = jornada;
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
  
  public ClientCetusDTO getClientCetus () {
    return this.clientCetus;
  }
  
  public void setClientCetus ( ClientCetusDTO clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
}