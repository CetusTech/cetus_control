package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the group_t database table.
 * 
 */
public class GroupTDTO implements Serializable {
  private static final long      serialVersionUID = 1L;
  
  private int                    id;
  
  private Date                   creationDate;
  
  private String                 creationUser;
  
  private String                 description;
  
  private Date                   endDate;
  
  private int                    hour;
  
  private Date                   initDate;
  
  private Date                   modificationDate;
  
  private String                 modificationUser;
  
  private String                 name;
  
  private String                 status;
  
  private GroupTypeDTO           groupType;
  
  private PersonDTO              person;
  
  private List< PersonGroupDTO > personGroups;
  
  public GroupTDTO () {
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
  
  public String getDescription () {
    return this.description;
  }
  
  public void setDescription ( String description ) {
    this.description = description;
  }
  
  public Date getEndDate () {
    return this.endDate;
  }
  
  public void setEndDate ( Date endDate ) {
    this.endDate = endDate;
  }
  
  public int getHour () {
    return this.hour;
  }
  
  public void setHour ( int hour ) {
    this.hour = hour;
  }
  
  public Date getInitDate () {
    return this.initDate;
  }
  
  public void setInitDate ( Date initDate ) {
    this.initDate = initDate;
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
  
  public String getStatus () {
    return this.status;
  }
  
  public void setStatus ( String status ) {
    this.status = status;
  }
  
  public GroupTypeDTO getGroupType () {
    return this.groupType;
  }
  
  public void setGroupType ( GroupTypeDTO groupType ) {
    this.groupType = groupType;
  }
  
  public PersonDTO getPerson () {
    return this.person;
  }
  
  public void setPerson ( PersonDTO person ) {
    this.person = person;
  }
  
  public List< PersonGroupDTO > getPersonGroups () {
    return this.personGroups;
  }
  
  public void setPersonGroups ( List< PersonGroupDTO > personGroups ) {
    this.personGroups = personGroups;
  }
  
  public PersonGroupDTO addPersonGroup ( PersonGroupDTO personGroup ) {
    getPersonGroups().add( personGroup );
    personGroup.setGroupT( this );
    
    return personGroup;
  }
  
  public PersonGroupDTO removePersonGroup ( PersonGroupDTO personGroup ) {
    getPersonGroups().remove( personGroup );
    personGroup.setGroupT( null );
    
    return personGroup;
  }
  
}