package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the attach database table.
 * 
 */
public class AttachDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private String            fileName;
  
  private byte[]            objFile;
  
  private TaskDTO           task;
  
  public AttachDTO () {
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
  
  public String getFileName () {
    return this.fileName;
  }
  
  public void setFileName ( String fileName ) {
    this.fileName = fileName;
  }
  
  public byte[] getObjFile () {
    return this.objFile;
  }
  
  public void setObjFile ( byte[] objFile ) {
    this.objFile = objFile;
  }
  
  public TaskDTO getTask () {
    return this.task;
  }
  
  public void setTask ( TaskDTO task ) {
    this.task = task;
  }
  
}