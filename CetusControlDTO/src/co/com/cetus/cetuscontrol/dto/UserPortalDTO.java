package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the user_portal database table.
 * 
 */
public class UserPortalDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private String            loginCetus;
  
  private Date              modificationDate;
  
  private String            modificationUser;
  
  private int               status;
  
  private PersonDTO         person;
  
  private String            loginOld;
  
  public UserPortalDTO () {
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
  
  public String getLoginCetus () {
    return this.loginCetus;
  }
  
  public void setLoginCetus ( String loginCetus ) {
    this.loginCetus = loginCetus;
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
  
  public int getStatus () {
    return this.status;
  }
  
  public void setStatus ( int status ) {
    this.status = status;
  }
  
  public PersonDTO getPerson () {
    return this.person;
  }
  
  public void setPerson ( PersonDTO person ) {
    this.person = person;
  }
  
  public String getLoginOld () {
    return loginOld;
  }
  
  public void setLoginOld ( String loginOld ) {
    this.loginOld = loginOld;
  }
  
}