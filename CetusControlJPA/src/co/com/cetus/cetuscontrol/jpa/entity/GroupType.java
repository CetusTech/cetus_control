package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the group_type database table.
 * 
 */
@Entity
@Table ( name = "GROUP_TYPE" )
@NamedQueries ( {
                 @NamedQuery ( name = "GroupType.findAllGroupTypeByClientCetus", query = "select gt from GroupType gt where gt.clientCetus.id = :idClientCetus" ),
                 @NamedQuery ( name = "GroupType.findAll", query = "SELECT g FROM GroupType g" ),
                 @NamedQuery ( name = "GroupType.findTypeByClientCetus", query = "SELECT gt FROM GroupType gt WHERE gt.clientCetus.id =:idclientCetus ORDER BY gt.description ASC")
} )
public class GroupType implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int               id;
  
  private String            acronym;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date              creationDate;
  
  @Column ( name = "CREATION_USER" )
  private String            creationUser;
  
  private String            description;
  
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "MODIFICATION_DATE" )
  private Date              modificationDate;
  
  @Column ( name = "MODIFICATION_USER" )
  private String            modificationUser;
  
  //bi-directional many-to-one association to GroupT
  @OneToMany ( mappedBy = "groupType" )
  private List< GroupT >    groupTs;
  
  //bi-directional many-to-one association to ClientCetus
  @ManyToOne
  @JoinColumn ( name = "ID_CLIENT" )
  private ClientCetus       clientCetus;
  
  public GroupType () {
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
  
  public List< GroupT > getGroupTs () {
    return this.groupTs;
  }
  
  public void setGroupTs ( List< GroupT > groupTs ) {
    this.groupTs = groupTs;
  }
  
  public GroupT addGroupT ( GroupT groupT ) {
    getGroupTs().add( groupT );
    groupT.setGroupType( this );
    
    return groupT;
  }
  
  public GroupT removeGroupT ( GroupT groupT ) {
    getGroupTs().remove( groupT );
    groupT.setGroupType( null );
    
    return groupT;
  }
  
  public ClientCetus getClientCetus () {
    return this.clientCetus;
  }
  
  public void setClientCetus ( ClientCetus clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
}