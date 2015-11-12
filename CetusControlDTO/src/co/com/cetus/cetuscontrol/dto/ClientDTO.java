package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the client database table.
 * 
 */
public class ClientDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private String            code;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private int               master;
  
  private Date              modificationDate;
  
  private String            modificationUser;
  
  private String            name;
  
  private ClientCetusDTO    clientCetus;
  
  private List< PersonDTO > persons;
  
  public ClientDTO () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public String getCode () {
    return this.code;
  }
  
  public void setCode ( String code ) {
    this.code = code;
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
  
  public int getMaster () {
    return this.master;
  }
  
  public void setMaster ( int master ) {
    this.master = master;
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
  
  public String getName () {
    return this.name;
  }
  
  public void setName ( String name ) {
    this.name = name;
  }
  
  public ClientCetusDTO getClientCetus () {
    return this.clientCetus;
  }
  
  public void setClientCetus ( ClientCetusDTO clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
  public List< PersonDTO > getPersons () {
    return this.persons;
  }
  
  public void setPersons ( List< PersonDTO > persons ) {
    this.persons = persons;
  }
  
  public PersonDTO addPerson ( PersonDTO person ) {
    getPersons().add( person );
    person.setClient( this );
    
    return person;
  }
  
  public PersonDTO removePerson ( PersonDTO person ) {
    getPersons().remove( person );
    person.setClient( null );
    
    return person;
  }
  
}