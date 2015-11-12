package co.com.cetus.cetuscontrol.web.bean;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.model.DualListModel;

import co.com.cetus.cetuscontrol.dto.GroupTDTO;
import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.dto.PersonGroupDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class GroupPersonMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (17/06/2015)
 */
@ManagedBean
@RequestScoped
public class GroupPersonMBean extends GeneralManagedBean {
  
  private static final long          serialVersionUID        = -3255236201554716426L;
  
  private UserPortalDTO              userPortalDTO           = null;
  private List< GroupTDTO >          listRegister            = null;
  private GroupTDTO                  selectedObject          = null;
  private DualListModel< PersonDTO > pickListGroupPersons    = null;
  private DualListModel< String >    pickListGroupPersons2   = null;
  private boolean                    showConfirmAdd          = false;
  private boolean                    showDialogAdd           = false;
  private boolean                    showDialogConfirmUpdate = false;
  private boolean                    showAlertSelectRow      = false;
  private boolean                    showViewDetail          = false;
  
  /**
   * </p> Instancia un nuevo group person m bean. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (17/06/2015)
   */
  public GroupPersonMBean () {
    selectedObject = new GroupTDTO();
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    userPortalDTO = getUserDTO();
    if ( userPortalDTO != null ) {
      listRegister( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  /**
   * </p> Load. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (17/06/2015)
   */
  public void load ( ActionEvent event ) {
    try {
      if ( pickListGroupPersons != null ) {
        addObjectSession( pickListGroupPersons, "pickListGroupPersons" );
        this.showConfirmAdd = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> List register. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (17/06/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void listRegister ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findGroupByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listRegister = ( List< GroupTDTO > ) response.getObjectResponse();
      } else {
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegister = new ArrayList< GroupTDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#remove()
   */
  @Override
  public String remove () {
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#update()
   */
  @Override
  public String update () {
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#add()
   */
  @SuppressWarnings ( "unchecked" )
  @Override
  public String add () {
    ResponseDTO responseDTO = null;
    String code = "0";
    List< PersonDTO > listPersonGroup = null;
    List< String > listExists = new ArrayList< String >();
    List< String > listExistsAux = new ArrayList< String >();
    List< String > listNew = new ArrayList< String >();
    List< PersonGroupDTO > listPersonGroupDTO = null;
    HashMap< String, String > mapFullName = new HashMap< String, String >();
    try {
      selectedObject = ( GroupTDTO ) getObjectSession( "selectedObject" );
      this.showConfirmAdd = false;
      listPersonGroup = ( List< PersonDTO > ) getObjectSession( "listPersonGroup" );
      if ( listPersonGroup != null && listPersonGroup.size() > 0 ) {
        for ( PersonDTO personDTO: listPersonGroup ) {
          listExists.add( String.valueOf( personDTO.getId() ) );
        }
      }
      ConstantWEB.WEB_LOG.info( "Lista de personas anteriores del grupo :: " + listExists.toString() );
      
      pickListGroupPersons = ( DualListModel< PersonDTO > ) getObjectSession( "pickListGroupPersons" );
      if ( pickListGroupPersons != null ) {
        List< PersonDTO > listTarget = pickListGroupPersons.getTarget();
        if ( listTarget != null && listTarget.size() > 0 ) {
          for ( PersonDTO personDTO: listTarget ) {
            listNew.add( String.valueOf( personDTO.getId() ) );
            mapFullName.put( String.valueOf( personDTO.getId() ), personDTO.getNames() + " " + personDTO.getLastNames() );
          }
        }
      }
      ConstantWEB.WEB_LOG.info( "Lista de personas actuales del grupo :: " + listNew.toString() );
      
      listExistsAux.addAll( listExists );
      listExists.removeAll( listNew );
      
      ConstantWEB.WEB_LOG.info( "Lista de personas a eliminar del grupo " + listExists.toString() );
      
      if ( listExists.size() > 0 ) {
        listPersonGroupDTO = ( List< PersonGroupDTO > ) getObjectSession( "listPersonGroupDTO" );
        if ( listPersonGroupDTO != null && listPersonGroupDTO.size() > 0 ) {
          for ( String string: listExists ) {
            for ( PersonGroupDTO personGroupDTO: listPersonGroupDTO ) {
              if ( string.equals( String.valueOf( personGroupDTO.getPerson().getId() ) ) ) {
                responseDTO = generalDelegate.remove( personGroupDTO );
                if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                  ConstantWEB.WEB_LOG.info( "Se elimino correctamente la persona " + string + ", del grupo " + selectedObject.getId() );
                } else {
                  ConstantWEB.WEB_LOG.error( "Error eliminando la persona " + string + ", del grupo " + selectedObject.getId() );
                  //Obetener codigo de error de oracle
                  if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
                    code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ),
                                                               responseDTO.getMessage().lastIndexOf( ":" ) );
                  }
                  String fullName = personGroupDTO.getPerson().getNames() + " " + personGroupDTO.getPerson().getLastNames();
                  addMessageWarning( null,
                                     ConstantWEB.MESSAGE_WARNING,
                                     code + ":"
                                         + MessageFormat.format( ConstantWEB.MESSAGE_ERROR_DELETE_GROUP_PERSON, fullName, selectedObject.getId() ) );
                }
                break;
              }
            }
          }
        }
      }
      
      listNew.removeAll( listExistsAux );
      ConstantWEB.WEB_LOG.info( "Lista de personas a insertar al grupo " + listNew.toString() );
      
      if ( listNew.size() > 0 ) {
        for ( String string: listNew ) {
          PersonGroupDTO personGroupDTO = new PersonGroupDTO();
          personGroupDTO.setCreationDate( currentDate );
          personGroupDTO.setCreationUser( getUserInSession() );
          
          personGroupDTO.setGroupT( new GroupTDTO() );
          personGroupDTO.getGroupT().setId( selectedObject.getId() );
          personGroupDTO.setPerson( new PersonDTO() );
          personGroupDTO.getPerson().setId( Integer.parseInt( string ) );
          
          responseDTO = generalDelegate.create( personGroupDTO );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            ConstantWEB.WEB_LOG.info( "Se inserto correctamente la persona " + string + ", en el grupo " + selectedObject.getId() );
          } else {
            ConstantWEB.WEB_LOG.error( "Error insertando la persona " + string + ", en el grupo " + selectedObject.getId() );
            //Obetener codigo de error de oracle
            if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
              code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ),
                                                         responseDTO.getMessage().lastIndexOf( ":" ) );
            }
            String fullName = mapFullName.get( string );
            addMessageWarning( null,
                               ConstantWEB.MESSAGE_WARNING,
                               code + ":" + MessageFormat.format( ConstantWEB.MESSAGE_ERROR_CREATE_GROUP_PERSON, fullName, selectedObject.getId() ) );
          }
          
        }
      }
      
      addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, null );
    }
    return null;
  }
  
  /**
   * </p> Validate selected record. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (17/06/2015)
   */
  public void validateSelectedRecord ( ActionEvent event ) {
    try {
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        this.showAlertSelectRow = false;
        this.showViewDetail = true;
        this.showDialogAdd = true;
      } else {
        this.showAlertSelectRow = true;
        this.showViewDetail = false;
        this.showDialogAdd = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void loadPickListGroupPersons ( int idClient, int idGroup ) {
    ResponseDTO response = null;
    List< PersonDTO > listPersonClient = null;
    List< PersonDTO > listPersonGroup = null;
    List< PersonGroupDTO > listPersonGroupDTO = null;
    
    try {
      response = this.generalDelegate.findPersonByGroup( idGroup );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        listPersonGroupDTO = ( List< PersonGroupDTO > ) response.getObjectResponse();
        if ( listPersonGroupDTO != null && listPersonGroupDTO.size() > 0 ) {
          listPersonGroup = new ArrayList< PersonDTO >();
          for ( PersonGroupDTO personGroupDTO: listPersonGroupDTO ) {
            listPersonGroup.add( personGroupDTO.getPerson() );
          }
        }
      } else {
        listPersonGroup = new ArrayList< PersonDTO >();
      }
      
      response = this.generalDelegate.findPersonByClient( idClient );
      if ( UtilCommon.validateResponseSuccessXMLOutput( response ) ) {
        listPersonClient = ( List< PersonDTO > ) response.getObjectResponse();
      } else {
        listPersonClient = new ArrayList< PersonDTO >();
      }
      
      if ( listPersonGroup != null && listPersonGroup.size() > 0 && listPersonClient != null && listPersonClient.size() > 0 ) {
        int i = 0;
        for ( PersonDTO personGDTO: listPersonGroup ) {
          i = 0;
          for ( PersonDTO personCDTO: listPersonClient ) {
            if ( personGDTO.getId() == personCDTO.getId() ) {
              listPersonClient.remove( i );
              break;
            }
            i++;
          }
        }
      }
      
      addObjectSession( listPersonGroup, "listPersonGroup" );
      addObjectSession( listPersonGroupDTO, "listPersonGroupDTO" );
      pickListGroupPersons = new DualListModel< PersonDTO >( listPersonClient, listPersonGroup );
      addObjectSession( pickListGroupPersons, "pickListGroupPersons" );
      
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Adds the group person. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (18/06/2015)
   */
  public void addGroupPerson () {
    try {
      validateSelectedRecord( null );
      if ( !this.showAlertSelectRow ) {
        loadPickListGroupPersons( selectedObject.getPerson().getClient().getId(), selectedObject.getId() );
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  public void viewGroupPerson () {
    ResponseDTO response = null;
    List< PersonGroupDTO > listPersonGroupDTO = null;
    try {
      validateSelectedRecord( null );
      if ( !this.showAlertSelectRow ) {
        response = this.generalDelegate.findPersonByGroup( selectedObject.getId() );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          listPersonGroupDTO = ( List< PersonGroupDTO > ) response.getObjectResponse();
        } else {
          listPersonGroupDTO = new ArrayList< PersonGroupDTO >();
        }
      }
      selectedObject.setPersonGroups( listPersonGroupDTO );
      addObjectSession( selectedObject, "selectedObject" );
      
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  public boolean isShowDialogAdd () {
    return showDialogAdd;
  }
  
  public void setShowDialogAdd ( boolean showDialogAdd ) {
    this.showDialogAdd = showDialogAdd;
  }
  
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  public boolean isShowAlertSelectRow () {
    return showAlertSelectRow;
  }
  
  public void setShowAlertSelectRow ( boolean showAlertSelectRow ) {
    this.showAlertSelectRow = showAlertSelectRow;
  }
  
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  public List< GroupTDTO > getListRegister () {
    return listRegister;
  }
  
  public void setListRegister ( List< GroupTDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public GroupTDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( GroupTDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  @SuppressWarnings ( "unchecked" )
  public DualListModel< PersonDTO > getPickListGroupPersons () {
    pickListGroupPersons = ( DualListModel< PersonDTO > ) getObjectSession( "pickListGroupPersons" );
    if ( pickListGroupPersons == null ) {
      pickListGroupPersons = new DualListModel< PersonDTO >( new ArrayList< PersonDTO >(), new ArrayList< PersonDTO >() );
    }
    return pickListGroupPersons;
  }
  
  public void setPickListGroupPersons ( DualListModel< PersonDTO > pickListGroupPersons ) {
    this.pickListGroupPersons = pickListGroupPersons;
  }
  
  public DualListModel< String > getPickListGroupPersons2 () {
    return pickListGroupPersons2;
  }
  
  public void setPickListGroupPersons2 ( DualListModel< String > pickListGroupPersons2 ) {
    this.pickListGroupPersons2 = pickListGroupPersons2;
  }
  
}
