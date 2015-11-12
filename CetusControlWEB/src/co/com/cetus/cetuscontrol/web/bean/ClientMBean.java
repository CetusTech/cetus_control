package co.com.cetus.cetuscontrol.web.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import co.com.cetus.cetuscontrol.dto.ClientDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.cetuscontrol.web.util.UtilWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class ClientMBean.
 *
 * @author Andres Herrera - Cetus Technology
 * @version CetusControlWEB (2/03/2015)
 */
@ManagedBean
@RequestScoped
public class ClientMBean extends GeneralManagedBean {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID        = -3255236201554716426L;
  
  /** The list register. */
  private List< ClientDTO > listRegister            = null;
  
  /** The add object. */
  private ClientDTO         addObject               = null;
  
  /** The selected object. */
  private ClientDTO         selectedObject          = null;
  
  /** The show confirm add. */
  private boolean           showConfirmAdd          = false;
  
  /** The show confirm mod. */
  private boolean           showConfirmMod          = false;
  
  /** The show dialog confirm update. */
  private boolean           showDialogConfirmUpdate = false;
  
  /** The show confirm delete. */
  private boolean           showConfirmDelete       = false;
  
  /** The show alert select row. */
  private boolean           showAlertSelectRow      = false;
  
  /** The show view detail. */
  private boolean           showViewDetail          = false;
  
  private String            codeObject              = null;
  
  /**
   * </p> Instancia un nuevo bank m bean. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (23/01/2015)
   */
  public ClientMBean () {
    addObject = new ClientDTO();
    selectedObject = new ClientDTO();
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    if ( getUserDTO() != null ) {
      listRegister();
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
   * @since CetusControlWEB (23/01/2015)
   */
  public void load ( ActionEvent event ) {
    try {
      if ( addObject != null ) {
        addObjectSession( addObject, "addObject" );
        this.showConfirmAdd = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load update. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (23/01/2015)
   */
  public void loadUpdate ( ActionEvent event ) {
    ClientDTO clientDTO = null;
    try {
      if ( selectedObject != null ) {
        clientDTO = ( ClientDTO ) getObjectSession( "selectedObject" );
        if ( clientDTO != null ) {
          selectedObject.setId( clientDTO.getId() );
          selectedObject.setClientCetus( clientDTO.getClientCetus() );
          selectedObject.setMaster( clientDTO.getMaster() );
          selectedObject.setCreationDate( clientDTO.getCreationDate() );
          selectedObject.setCreationUser( clientDTO.getCreationUser() );
        }
        addObjectSession( selectedObject, "selectedObject" );
        this.showDialogConfirmUpdate = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> List register. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (23/01/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void listRegister () {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findClientByClientCetus( getUserDTO().getPerson().getClient().getClientCetus().getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listRegister = ( List< ClientDTO > ) response.getObjectResponse();
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegister = new ArrayList< ClientDTO >();
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
    ResponseDTO response = null;
    try {
      this.showConfirmDelete = false;
      selectedObject = ( ClientDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        if ( selectedObject.getMaster() != 1 ) {
          ConstantWEB.WEB_LOG.debug( "########## REGISTRO A ELIMINAR  " + selectedObject.getName() );
          response = generalDelegate.remove( selectedObject );
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            ConstantWEB.WEB_LOG.debug( "########## REGISTRO ELIMINADO  " + selectedObject.getName() );
            this.initElement();
            addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_DELETE );
          } else {
            ConstantWEB.WEB_LOG.debug( "########## ERROR ELIMINADO EL REGISTRO  " + selectedObject.getName() );
            addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_DELETE_DETAIL );
          }
        } else {
          ConstantWEB.WEB_LOG.debug( "########## ERROR ELIMINADO EL REGISTRO  " + selectedObject.getName() );
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_DELETE_MASTER );
        }
      } else {
        ConstantWEB.WEB_LOG.debug( "########## No ha seleccionado un registro a eliminar  " + selectedObject.getName() );
        addMessageError( null, ConstantWEB.MESSAGE_ERROR, "No ha seleccionado un registro a eliminar" );
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
      selectedObject = ( ClientDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        context = RequestContext.getCurrentInstance();
        selectedObject.setCreationUser( selectedObject.getCreationUser() );
        selectedObject.setCreationDate( selectedObject.getCreationDate() );
        selectedObject.setModificationUser( getUserInSession() );
        selectedObject.setModificationDate( currentDate );
        ConstantWEB.WEB_LOG.debug( "######### ACTUALIZAR EL SIGUIENTE REGISTRO " + selectedObject.getName() );
        response = generalDelegate.edit( selectedObject );
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          ConstantWEB.WEB_LOG.debug( "######### REGISTRO ACTUALIZADO CORRECTAMENTE " + selectedObject.getName() );
          this.initElement();
          lSuccessfull = true;
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
        } else {
          //Obetener codigo de error de oracle
          String code = "0";
          if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
            code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
            if ( code != null && !code.isEmpty() ) {
              code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
              addMessageError( null, ConstantWEB.MESSAGE_ERROR, code );
            }
          } else {
            addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_UPDATE );
          }
          ConstantWEB.WEB_LOG.debug( "######### ERROR ACTUALIZANDO EL REGISTRO" + selectedObject.getName() );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    context.addCallbackParam( "lSuccessfull", lSuccessfull );
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#add()
   */
  @Override
  public String add () {
    boolean lSuccessfull = false;
    RequestContext context = null;
    ResponseDTO response = null;
    String codeClient = null;
    try {
      context = RequestContext.getCurrentInstance();
      this.showConfirmAdd = false;
      addObject = ( ClientDTO ) getObjectSession( "addObject" );
      if ( addObject != null ) {
        if ( getUserDTO() != null ) {
          addObject.setClientCetus( getUserDTO().getPerson().getClient().getClientCetus() );
        }
        response = generalDelegate.generateCodeClient();
        if ( UtilCommon.validateResponseSuccess( response ) ) {
          codeClient = ( String ) response.getObjectResponse();
          ConstantWEB.WEB_LOG.info( "########## CODIGO CLIENTE GENERADO :: " + codeClient );
          if ( codeClient != null ) {
            addObject.setCode( codeClient );
          }
          addObject.setCreationUser( getUserInSession() );
          addObject.setCreationDate( currentDate );
          //Los Clientes creados por la aplicacion no son Master
          addObject.setMaster( BigDecimal.ZERO.intValue() );
          ConstantWEB.WEB_LOG.debug( "######### REGISTRAR NUEVO CLIENTE " + addObject.getCode() );
          
          response = generalDelegate.create( addObject );
          if ( UtilCommon.validateResponseSuccess( response ) ) {
            ConstantWEB.WEB_LOG.debug( "######### CLIENTE CREADO EXITOSAMENTE " + addObject.getCode() );
            this.initElement();
            lSuccessfull = true;
            addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
          } else {
            //Obetener codigo de error de oracle
            String code = "0";
            if ( !response.getMessage().isEmpty() && response.getMessage().contains( "ORA" ) ) {
              code = response.getMessage().substring( response.getMessage().lastIndexOf( "ORA" ), response.getMessage().lastIndexOf( ":" ) );
              if ( code != null && !code.isEmpty() ) {
                code = UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, code );
                addMessageError( null, ConstantWEB.MESSAGE_ERROR, code );
              }
            } else {
              addMessageError( null, ConstantWEB.MESSAGE_ERROR, code + ":" + response.getMessage() );
            }
          }
        } else {
          addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR + " No Es posible generar el Código del Cliente." );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, null );
    }
    context.addCallbackParam( "lSuccessfull", lSuccessfull );
    return null;
  }
  
  /**
   * </p> Validate selected record. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (26/01/2015)
   */
  public void validateSelectedRecord ( ActionEvent event ) {
    try {
      if ( selectedObject != null ) {
        codeObject = selectedObject.getCode();
        addObjectSession( codeObject, "codeObject" );
        //addObjectSession( selectedObject, "selectedObject" );
        this.showAlertSelectRow = false;
        this.showViewDetail = true;
        this.showConfirmMod = true;
        this.showConfirmDelete = true;
      } else {
        this.showAlertSelectRow = true;
        this.showViewDetail = false;
        this.showConfirmMod = false;
        this.showConfirmDelete = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void onRowSelect ( SelectEvent event ) {
    try {
      selectedObject = ( ( ClientDTO ) event.getObject() );
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  /**
   * </p> Gets the list register. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el list
   * @since CetusControlWEB (2/03/2015)
   */
  public List< ClientDTO > getListRegister () {
    return listRegister;
  }
  
  /**
   * </p> Sets the list register. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param listRegister the list register
   * @since CetusControlWEB (2/03/2015)
   */
  public void setListRegister ( List< ClientDTO > listRegister ) {
    this.listRegister = listRegister;
  }
  
  /**
   * </p> Gets the add object. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el client dto
   * @since CetusControlWEB (2/03/2015)
   */
  public ClientDTO getAddObject () {
    return addObject;
  }
  
  /**
   * </p> Sets the add object. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param addObject the adds the object
   * @since CetusControlWEB (2/03/2015)
   */
  public void setAddObject ( ClientDTO addObject ) {
    this.addObject = addObject;
  }
  
  /**
   * </p> Gets the selected object. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return el client dto
   * @since CetusControlWEB (2/03/2015)
   */
  public ClientDTO getSelectedObject () {
    return selectedObject;
  }
  
  /**
   * </p> Sets the selected object. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param selectedObject the selected object
   * @since CetusControlWEB (2/03/2015)
   */
  public void setSelectedObject ( ClientDTO selectedObject ) {
    this.showAlertSelectRow = true;
    this.selectedObject = selectedObject;
  }
  
  /**
   * </p> Checks if is show confirm add. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/03/2015)
   */
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  /**
   * </p> Sets the show confirm add. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showConfirmAdd the show confirm add
   * @since CetusControlWEB (2/03/2015)
   */
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  /**
   * </p> Sets the show confirm mod. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showConfirmMod the show confirm mod
   * @since CetusControlWEB (2/03/2015)
   */
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  /**
   * </p> Checks if is show confirm mod. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/03/2015)
   */
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  /**
   * </p> Checks if is show alert select row. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/03/2015)
   */
  public boolean isShowAlertSelectRow () {
    return showAlertSelectRow;
  }
  
  /**
   * </p> Sets the show alert select row. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showAlertSelectRow the show alert select row
   * @since CetusControlWEB (2/03/2015)
   */
  public void setShowAlertSelectRow ( boolean showAlertSelectRow ) {
    this.showAlertSelectRow = showAlertSelectRow;
  }
  
  /**
   * </p> Checks if is show view detail. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/03/2015)
   */
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  /**
   * </p> Sets the show view detail. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showViewDetail the show view detail
   * @since CetusControlWEB (2/03/2015)
   */
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  /**
   * </p> Sets the show dialog confirm update. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showDialogConfirmUpdate the show dialog confirm update
   * @since CetusControlWEB (2/03/2015)
   */
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  /**
   * </p> Checks if is show dialog confirm update. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/03/2015)
   */
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  /**
   * </p> Checks if is show confirm delete. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/03/2015)
   */
  public boolean isShowConfirmDelete () {
    return showConfirmDelete;
  }
  
  /**
   * </p> Sets the show confirm delete. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param showConfirmDelete the show confirm delete
   * @since CetusControlWEB (2/03/2015)
   */
  public void setShowConfirmDelete ( boolean showConfirmDelete ) {
    this.showConfirmDelete = showConfirmDelete;
  }
  
  public String getCodeObject () {
    codeObject = ( String ) ( getObjectSession( "codeObject" ) != null ? getObjectSession( "codeObject" ) : null );
    return codeObject;
  }
  
  public void setCodeObject ( String codeObject ) {
    this.codeObject = codeObject;
  }
  
}
