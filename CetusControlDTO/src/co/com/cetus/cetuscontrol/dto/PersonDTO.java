package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the person database table.
 * 
 */
public class PersonDTO implements Serializable {
  private static final long      serialVersionUID = 1L;
  
  private int                    id;
  
  private String                 address;
  
  private String                 cellPhone;
  
  private String                 code;
  
  private Date                   creationDate;
  
  private String                 creationUser;
  
  private String                 email;
  
  private String                 identity;
  
  private String                 lastNames;
  
  private int                    master;
  
  private Date                   modificationDate;
  
  private String                 modificationUser;
  
  private String                 names;
  
  private String                 phone;
  
  private List< GroupTDTO >      groupTs;
  
  private ClientDTO              client;
  
  private List< PersonGroupDTO > personGroups;
  
  private List< UserPortalDTO >  userPortals;
  
  public PersonDTO () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public String getAddress () {
    return this.address;
  }
  
  public void setAddress ( String address ) {
    this.address = address;
  }
  
  public String getCellPhone () {
    return this.cellPhone;
  }
  
  public void setCellPhone ( String cellPhone ) {
    this.cellPhone = cellPhone;
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
  
  public String getEmail () {
    return this.email;
  }
  
  public void setEmail ( String email ) {
    this.email = email;
  }
  
  public String getIdentity () {
    return this.identity;
  }
  
  public void setIdentity ( String identity ) {
    this.identity = identity;
  }
  
  public String getLastNames () {
    return this.lastNames;
  }
  
  public void setLastNames ( String lastNames ) {
    this.lastNames = lastNames;
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
  
  public String getNames () {
    return this.names;
  }
  
  public void setNames ( String names ) {
    this.names = names;
  }
  
  public String getPhone () {
    return this.phone;
  }
  
  public void setPhone ( String phone ) {
    this.phone = phone;
  }
  
  public List< GroupTDTO > getGroupTs () {
    return this.groupTs;
  }
  
  public void setGroupTs ( List< GroupTDTO > groupTs ) {
    this.groupTs = groupTs;
  }
  
  public GroupTDTO addGroupT ( GroupTDTO groupT ) {
    getGroupTs().add( groupT );
    groupT.setPerson( this );
    
    return groupT;
  }
  
  public GroupTDTO removeGroupT ( GroupTDTO groupT ) {
    getGroupTs().remove( groupT );
    groupT.setPerson( null );
    
    return groupT;
  }
  
  public ClientDTO getClient () {
    return this.client;
  }
  
  public void setClient ( ClientDTO client ) {
    this.client = client;
  }
  
  public List< PersonGroupDTO > getPersonGroups () {
    return this.personGroups;
  }
  
  public void setPersonGroups ( List< PersonGroupDTO > personGroups ) {
    this.personGroups = personGroups;
  }
  
  public PersonGroupDTO addPersonGroup ( PersonGroupDTO personGroup ) {
    getPersonGroups().add( personGroup );
    personGroup.setPerson( this );
    
    return personGroup;
  }
  
  public PersonGroupDTO removePersonGroup ( PersonGroupDTO personGroup ) {
    getPersonGroups().remove( personGroup );
    personGroup.setPerson( null );
    
    return personGroup;
  }
  
  public List< UserPortalDTO > getUserPortals () {
    return this.userPortals;
  }
  
  public void setUserPortals ( List< UserPortalDTO > userPortals ) {
    this.userPortals = userPortals;
  }
  
  public UserPortalDTO addUserPortal ( UserPortalDTO userPortal ) {
    getUserPortals().add( userPortal );
    userPortal.setPerson( this );
    
    return userPortal;
  }
  
  public UserPortalDTO removeUserPortal ( UserPortalDTO userPortal ) {
    getUserPortals().remove( userPortal );
    userPortal.setPerson( null );
    
    return userPortal;
  }
  
}