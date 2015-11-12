package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the group_type database table.
 * 
 */
public class GroupTypeDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private String            acronym;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private String            description;
  
  private Date              modificationDate;
  
  private String            modificationUser;
  
  private List< GroupTDTO > groupTs;
  
  private ClientCetusDTO    clientCetus;
  
  public GroupTypeDTO () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public String getAcronym () {
    return this.acronym;
  }
  
  public void setAcronym ( String acronym ) {
    this.acronym = acronym;
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
  
  public List< GroupTDTO > getGroupTs () {
    return this.groupTs;
  }
  
  public void setGroupTs ( List< GroupTDTO > groupTs ) {
    this.groupTs = groupTs;
  }
  
  public GroupTDTO addGroupT ( GroupTDTO groupT ) {
    getGroupTs().add( groupT );
    groupT.setGroupType( this );
    
    return groupT;
  }
  
  public GroupTDTO removeGroupT ( GroupTDTO groupT ) {
    getGroupTs().remove( groupT );
    groupT.setGroupType( null );
    
    return groupT;
  }
  
  public ClientCetusDTO getClientCetus () {
    return this.clientCetus;
  }
  
  public void setClientCetus ( ClientCetusDTO clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
}