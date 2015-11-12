package co.com.cetus.cetuscontrol.web.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import co.com.cetus.cetuscontrol.dto.ClientDTO;
import co.com.cetus.cetuscontrol.dto.GroupTDTO;
import co.com.cetus.cetuscontrol.dto.GroupTypeDTO;
import co.com.cetus.cetuscontrol.dto.PersonDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.cetuscontrol.web.util.Parameter;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

@ManagedBean
@RequestScoped
public class GroupAdminMBean extends GeneralManagedBean {
  
  /**
   * 
   */
  private static final long    serialVersionUID        = 1L;
  
  /** The list register. */
  private List< GroupTDTO >    listRegister            = null;
  
  /** The add object. */
  private GroupTDTO            addObject               = null;
  
  /** The selected object. */
  private GroupTDTO            selectedObject          = null;
  
  /** The show confirm add. */
  private boolean              showConfirmAdd          = false;
  
  /** The show confirm mod. */
  private boolean              showConfirmMod          = false;
  
  /** The show dialog confirm update. */
  private boolean              showDialogConfirmUpdate = false;
  
  /** The show confirm delete. */
  private boolean              showConfirmDelete       = false;
  
  /** The show alert select row. */
  private boolean              showAlertSelectRow      = false;
  
  /** The show view detail. */
  private boolean              showViewDetail          = false;
  
  private List< PersonDTO >    listPerson              = null;
  private List< ClientDTO >    listClient              = null;
  private long                 idClient;
  private int                 idPerson;
  private int                 idClientUpdate;
  private int                 idPersonUpdate;
  private ClientDTO            clientDTO               = null;
  private PersonDTO            personDTO               = null;
  private boolean              statusDateEnd           = true;
  private List< SelectItem >   listClientItem          = null;
  private List< SelectItem >   listPersonItem          = null;
  private List< GroupTypeDTO > listGroupType           = null;
  private List< SelectItem >   listGroupTypeItem       = null;
  private List< SelectItem >   status                  = null;
  
  public GroupAdminMBean () {
    addObject = new GroupTDTO();
    selectedObject = new GroupTDTO();
    selectedObject.setGroupType( new GroupTypeDTO() );
    selectedObject.setPerson( new PersonDTO() );
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    ConstantWEB.WEB_LOG.debug( "########## valor parametro " + generalDelegate.getValueParameter( Parameter.NOT_CREATE_ELEMENT ) );
    if ( getUserDTO() != null ) {
      listRegister();
      loadClient();
      loadStatus();
      loadGroupType();
      loadPersonByClient();
      addObject = new GroupTDTO();
      addObject.setGroupType( new GroupTypeDTO() );
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void loadGroupType () {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findAllGroupTypeByClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listGroupType = ( List< GroupTypeDTO > ) response.getObjectResponse();
        if ( listGroupType != null && !listGroupType.isEmpty() ) {
          this.listGroupTypeItem = new ArrayList< SelectItem >();
          for ( GroupTypeDTO objDTO: listGroupType ) {
            this.listGroupTypeItem.add( new SelectItem( objDTO.getId(), objDTO.getDescription() ) );
          }
        }
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listGroupType = new ArrayList< GroupTypeDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  private void loadStatus () {
    try {
      status = new ArrayList< SelectItem >();
      //status.add( new SelectItem( ConstantWEB.STATUS_GROUP_NEW, ConstantWEB.STATUS_GROUP_NEW ) );
      status.add( new SelectItem( ConstantWEB.STATUS_GROUP_CREATED, ConstantWEB.STATUS_GROUP_CREATED_ES ) );
      status.add( new SelectItem( ConstantWEB.STATUS_GROUP_IN_PROGRESS, ConstantWEB.STATUS_GROUP_IN_PROGRESS_ES ) );
      status.add( new SelectItem( ConstantWEB.STATUS_GROUP_CLOSED, ConstantWEB.STATUS_GROUP_CLOSED_ES ) );
      
    } catch ( Exception e ) {
      
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  private void listRegister () {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findGroupByClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listRegister = ( List< GroupTDTO > ) response.getObjectResponse();
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegister = new ArrayList< GroupTDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public void loadClient () {
    ResponseDTO response = null;
    try {
      response = this.generalDelegate.findClientByClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listClient = ( List< ClientDTO > ) response.getObjectResponse();
        this.listClientItem = new ArrayList< SelectItem >();
        if ( listClient != null && !listClient.isEmpty() ) {
          for ( ClientDTO clientDTO: listClient ) {
            listClientItem.add( new SelectItem( clientDTO.getId(), clientDTO.getName() ) );
          }
        }
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listClient = new ArrayList< ClientDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  public void loadPersonByClient () {
    ResponseDTO response = null;
    try {
      cleanObjectSession( "listPersonItem" );
      response = this.generalDelegate.findPersonByClient( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listPerson = ( List< PersonDTO > ) response.getObjectResponse();
        this.listPersonItem = new ArrayList< SelectItem >();
        if ( listPerson != null && !listPerson.isEmpty() ) {
          for ( PersonDTO personDTO: listPerson ) {
            this.listPersonItem.add( new SelectItem( personDTO.getId(), personDTO.getNames() + " " + personDTO.getLastNames() ) );
          }
        }
        addObjectSession( listPersonItem, "listPersonItem" );
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listPerson = new ArrayList< PersonDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  public void loadPersonByClientUpdate () {
    ResponseDTO response = null;
    try {
      cleanObjectSession( "listPersonItem" );
      response = this.generalDelegate.findPersonByClient( idClientUpdate );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listPerson = ( List< PersonDTO > ) response.getObjectResponse();
        this.listPersonItem = new ArrayList< SelectItem >();
        if ( listPerson != null && !listPerson.isEmpty() ) {
          for ( PersonDTO personDTO: listPerson ) {
            this.listPersonItem.add( new SelectItem( personDTO.getId(), personDTO.getNames() + personDTO.getLastNames() ) );
          }
        }
        addObjectSession( listPersonItem, "listPersonItem" );
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listPerson = new ArrayList< PersonDTO >();
        }
      }
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void load ( ActionEvent event ) {
    try {
      if ( addObject != null && idPerson > 0 ) {
        addObject.setPerson( new PersonDTO() );
        addObject.getPerson().setId( idPerson );
        addObjectSession( addObject, "addObject" );
        this.showConfirmAdd = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#remove()
   */
  @Override
  public String remove () {
    ResponseDTO response = null;
    try {
      this.showConfirmDelete = false;
      selectedObject = ( GroupTDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        ConstantWEB.WEB_LOG.debug( "########## REGISTRO A ELIMINAR  " + selectedObject.getDescription() );
        response = generalDelegate.remove( selectedObject );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          ConstantWEB.WEB_LOG.debug( "########## REGISTRO ELIMINADO  " + selectedObject.getDescription() );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_DELETE );
        } else {
          ConstantWEB.WEB_LOG.debug( "########## ERROR ELIMINADO EL REGISTRO  " + selectedObject.getDescription() );
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_DELETE_DETAIL );
        }
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#update()
   */
  @Override
  public String update () {
    boolean lSuccessfull = false;
    RequestContext context = null;
    ResponseDTO response = null;
    try {
      this.showConfirmMod = false;
      selectedObject = ( GroupTDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        context = RequestContext.getCurrentInstance();
        selectedObject.setCreationUser( getUserInSession() );
        selectedObject.setModificationDate( currentDate );
        selectedObject.setDescription( selectedObject.getDescription().toUpperCase() );
        //selectedObject.setColor( selectedObject.getColor().toUpperCase() );
        ConstantWEB.WEB_LOG.debug( "######### ACTUALIZAR EL SIGUIENTE REGISTRO " + selectedObject.getDescription() );
        response = generalDelegate.edit( selectedObject );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          ConstantWEB.WEB_LOG.debug( "######### REGISTRO ACTUALIZADO CORRECTAMENTE " + selectedObject.getDescription() );
          this.initElement();
          lSuccessfull = true;
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
        } else {
          //Obetener codigo de error de oracle
          getMessageError( response );
          ConstantWEB.WEB_LOG.debug( "######### ERROR ACTUALIZANDO EL REGISTRO" );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    context.addCallbackParam( "lSuccessfull", lSuccessfull );
    return null;
  }
  
  public void loadUpdate ( ActionEvent event ) {
    GroupTDTO GroupTDTO = null;
    try {
      if ( selectedObject != null && idPersonUpdate > 0 ) {
        GroupTDTO = ( GroupTDTO ) getObjectSession( "selectedObject" );
        if ( GroupTDTO != null ) {
          selectedObject.setId( GroupTDTO.getId() );
          selectedObject.setPerson( new PersonDTO() );
          selectedObject.getPerson().setId( idPersonUpdate );
          // selectedObject.setClientCetus( GroupTDTO.getClientCetus() );
          selectedObject.setCreationDate( GroupTDTO.getCreationDate() );
          selectedObject.setCreationUser( GroupTDTO.getCreationUser() );
          selectedObject.setModificationUser( getUserInSession() );
        }
        addObjectSession( selectedObject, "selectedObject" );
        this.showDialogConfirmUpdate = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#add()
   */
  @Override
  public String add () {
    boolean lSuccessfull = false;
    RequestContext context = null;
    ResponseDTO response = null;
    try {
      this.showConfirmAdd = false;
      addObject = ( GroupTDTO ) getObjectSession( "addObject" );
      if ( addObject != null ) {
        if ( getUserDTO() != null ) {
          // addObject.setClientCetus( getUserDTO().getPerson().getClient().getClientCetus() );
        }
        context = RequestContext.getCurrentInstance();
        addObject.setCreationUser( getUserInSession() );
        addObject.setDescription( addObject.getDescription().toUpperCase() );
        // addObject.setColor( addObject.getColor().toUpperCase() );
        addObject.setCreationDate( new Date() );
        response = generalDelegate.create( addObject );
        
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          ConstantWEB.WEB_LOG.debug( "########## GROUP OK " );
          this.initElement();
          lSuccessfull = true;
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
        } else {
          //Obetener codigo de error de oracle
          getMessageError( response );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, null );
    }
    context.addCallbackParam( "lSuccessfull", lSuccessfull );
    return null;
  }
  
  @SuppressWarnings ( "unchecked" )
  public void validateSelectedRecord ( ActionEvent event ) {
    ResponseDTO response = null;
    try {
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        addObjectSession( selectedObject.getInitDate(), "initDate" );
        if ( selectedObject.getPerson() != null && selectedObject.getPerson().getClient() != null ) {
          idClientUpdate = selectedObject.getPerson().getClient().getId();
          if ( idClientUpdate > 0 ) {
            response = this.generalDelegate.findPersonByClient( idClientUpdate );
            if ( UtilCommon.validateResponseSuccess( response ) ) {
              listPerson = ( List< PersonDTO > ) response.getObjectResponse();
              if ( listPerson != null ) {
                listPersonItem = new ArrayList< SelectItem >();
                for ( PersonDTO objDTO: listPerson ) {
                  listPersonItem.add( new SelectItem( objDTO.getId(), objDTO.getNames() + " " + objDTO.getLastNames() ) );
                }
                addObjectSession( listPersonItem, "listPersonItem" );
              }
            }
          }
          idPersonUpdate = selectedObject.getPerson().getId();
        }
        this.showAlertSelectRow = false;
        this.showViewDetail = true;
        this.showConfirmMod = true;
        this.showConfirmDelete = true;
      } else {
        selectedObject = new GroupTDTO();
        selectedObject.setGroupType( new GroupTypeDTO() );
        this.showAlertSelectRow = true;
        this.showViewDetail = false;
        this.showConfirmMod = false;
        this.showConfirmDelete = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void handleDateSelectInit ( SelectEvent event ) {
    Date date = ( Date ) event.getObject();
    if ( date != null ) {
      statusDateEnd = false;
      addObjectSession( statusDateEnd, "statusDateEnd" );
      addObjectSession( date, "initDate" );
    } else {
      statusDateEnd = true;
      addObjectSession( date, "initDate" );
    }
    
  }
  
  public void handleDateSelect ( SelectEvent event ) {
    Date date = ( Date ) event.getObject();
    Date initDate = ( Date ) getObjectSession( "initDate" );
    if ( date != null ) {
      if ( initDate != null && initDate.after( date ) ) {
        addMessageWarning( null, ConstantWEB.MESSAGE_ERROR, "La Fecha Final debe ser mayor a la fecha Inicial" );
      }
    }
    
  }
  
  public List< GroupTDTO > getListRegister () {
    return listRegister;
  }
  
  public void setListRegister ( List< GroupTDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  public GroupTDTO getAddObject () {
    return addObject;
  }
  
  public void setAddObject ( GroupTDTO addObject ) {
    this.addObject = addObject;
  }
  
  public GroupTDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( GroupTDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  public boolean isShowConfirmDelete () {
    return showConfirmDelete;
  }
  
  public void setShowConfirmDelete ( boolean showConfirmDelete ) {
    this.showConfirmDelete = showConfirmDelete;
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
  
  public long getIdClient () {
    return idClient;
  }
  
  public void setIdClient ( long idClient ) {
    this.idClient = idClient;
  }
  
  public ClientDTO getClientDTO () {
    return clientDTO;
  }
  
  public void setClientDTO ( ClientDTO clientDTO ) {
    this.clientDTO = clientDTO;
  }
  
  public PersonDTO getPersonDTO () {
    return personDTO;
  }
  
  public void setPersonDTO ( PersonDTO personDTO ) {
    this.personDTO = personDTO;
  }
  
  public List< SelectItem > getListClientItem () {
    return listClientItem;
  }
  
  public void setListClientItem ( List< SelectItem > listClientItem ) {
    this.listClientItem = listClientItem;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListPersonItem () {
    listPersonItem = ( List< SelectItem > ) ( getObjectSession( "listPersonItem" ) != null ? getObjectSession( "listPersonItem" ) : null );
    return listPersonItem;
  }
  
  public void setListPersonItem ( List< SelectItem > listPersonItem ) {
    this.listPersonItem = listPersonItem;
  }
  
  public long getIdPerson () {
    return idPerson;
  }
  
  public void setIdPerson ( int idPerson ) {
    this.idPerson = idPerson;
  }
  
  public List< SelectItem > getStatus () {
    return status;
  }
  
  public void setStatus ( List< SelectItem > status ) {
    this.status = status;
  }
  
  public List< SelectItem > getListGroupTypeItem () {
    return listGroupTypeItem;
  }
  
  public void setListGroupTypeItem ( List< SelectItem > listGroupTypeItem ) {
    this.listGroupTypeItem = listGroupTypeItem;
  }
  
  public boolean isStatusDateEnd () {
    statusDateEnd = ( Boolean ) ( getObjectSession( "statusDateEnd" ) != null ? getObjectSession( "statusDateEnd" ) : true );
    return statusDateEnd;
  }
  
  public void setStatusDateEnd ( boolean statusDateEnd ) {
    this.statusDateEnd = statusDateEnd;
  }
  
  public long getIdClientUpdate () {
    return idClientUpdate;
  }
  
  public void setIdClientUpdate ( int idClientUpdate ) {
    this.idClientUpdate = idClientUpdate;
  }
  
  public long getIdPersonUpdate () {
    return idPersonUpdate;
  }
  
  public void setIdPersonUpdate ( int idPersonUpdate ) {
    this.idPersonUpdate = idPersonUpdate;
  }
  
}
