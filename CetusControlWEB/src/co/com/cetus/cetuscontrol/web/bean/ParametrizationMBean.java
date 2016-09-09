package co.com.cetus.cetuscontrol.web.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.primefaces.event.SelectEvent;

import co.com.cetus.cetuscontrol.dto.ClientCetusDTO;
import co.com.cetus.cetuscontrol.dto.ExceptionWorkdayDTO;
import co.com.cetus.cetuscontrol.dto.JornadaListDTO;
import co.com.cetus.cetuscontrol.dto.ParameterGeneralDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.dto.WorkdayDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.cetuscontrol.web.util.EWeekDay;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class ParametrizationMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (4/03/2015)
 */
@ManagedBean
@RequestScoped
public class ParametrizationMBean extends GeneralManagedBean {
  
  private static final long           serialVersionUID            = 1L;
                                                                  
  private boolean                     disabledTabWorkday          = true;
  private boolean                     disabledTabExceptionWorkday = true;
  private boolean                     showConfirmSaveParameter    = false;
  private ParameterGeneralDTO         parameterGeneralDTO         = null;
  private UserPortalDTO               userPortalDTO               = null;
  private boolean                     showViewDetail              = false;
  private boolean                     showAlertSelectRow          = false;
  private WorkdayDTO                  selectedObject              = null;
  private boolean                     showConfirmMod              = false;
  private boolean                     showConfirmDelete           = false;
  private boolean                     showDialogConfirmUpdate     = false;
  private boolean                     showConfirmAdd              = false;
  private WorkdayDTO                  addObject                   = null;
  private List< WorkdayDTO >          listRegisterWorkday         = null;
  private List< SelectItem >          listWeekDay                 = null;
  private List< SelectItem >          listJornada                 = null;
  private JornadaListDTO              jornadaListDTO              = null;
  private boolean                     cloneWorkday                = false;
  private boolean                     disableCloneWorkday         = true;
  private boolean                     showCloneWorkday            = false;
  private List< String >              selectedCloneWorkday        = null;
  private List< SelectItem >          cloneListWeekDay            = null;
                                                                  
  private ExceptionWorkdayDTO         selectedObjectExcep         = null;
  private boolean                     showAlertSelectRowExcep     = false;
  private boolean                     showViewDetailExcep         = false;
  private boolean                     showConfirmModExcep         = false;
  private boolean                     showConfirmDeleteExcep      = false;
  private boolean                     showDialogConfirmUpdExcep   = false;
  private boolean                     showConfirmAddExcep         = false;
  private List< ExceptionWorkdayDTO > listRegisterException       = null;
  private ExceptionWorkdayDTO         addObjectExcep              = null;
  private List< SelectItem >          listJornadaExcep            = null;
  private boolean                     disableBtnAddException      = false;
  private boolean                     showAlertSelectDateBefore   = false;
  private int                         numberNotificatons          = 1;
  private List< SelectItem >          listNotification            = null;
  private String                      notificationMask            = "99";
  private int                         sizeNotificaton             = 5;
                                                                  
  public ParametrizationMBean () {
    parameterGeneralDTO = new ParameterGeneralDTO();
    selectedObject = new WorkdayDTO();
    addObject = new WorkdayDTO();
    jornadaListDTO = new JornadaListDTO();
    selectedObjectExcep = new ExceptionWorkdayDTO();
    addObjectExcep = new ExceptionWorkdayDTO();
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    userPortalDTO = getUserDTO();
    if ( userPortalDTO != null ) {
      loadParameterGeneral();
      loadNotifications();
      if ( !disabledTabWorkday ) {
        listRegisterWorkday( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
        loadWeekDay();
      }
      if ( !disabledTabExceptionWorkday ) {
        listExceptionWorkday( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
      }
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#remove()
   */
  @Override
  public String remove () {
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      selectedObject = ( WorkdayDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        
        ConstantWEB.WEB_LOG.info( "Se procede a eliminar la jornada :: " + selectedObject.toString() );
        responseDTO = generalDelegate.remove( selectedObject );
        ConstantWEB.WEB_LOG.info( "Respuesta despues de eliminar la jornada :: " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Jornada [" + selectedObject.getColDay() + "-" + selectedObject.getJornada()
                                    + "] eliminada exitosamente" );
                                    
          for ( WorkdayDTO workdayDTO: listRegisterWorkday ) {
            if ( workdayDTO.getColDay().equals( selectedObject.getColDay() )
                 && workdayDTO.getJornada() > selectedObject.getJornada() ) {
              workdayDTO.setModificationDate( currentDate );
              workdayDTO.setModificationUser( getUserInSession() );
              workdayDTO.setJornada( ( workdayDTO.getJornada() - 1 ) );
              
              ConstantWEB.WEB_LOG.info( "Se procede a actualizar la jornada :: " + workdayDTO.toString() );
              responseDTO = generalDelegate.edit( workdayDTO );
              
              ConstantWEB.WEB_LOG.info( "Respuesta despues de actualizar la jornada :: " + responseDTO.toString() );
              if ( !UtilCommon.validateResponseSuccess( responseDTO ) ) {
                ConstantWEB.WEB_LOG.error( "Error actualizando la jornada :: " + workdayDTO.toString() );
              }
            }
          }
          
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_DELETE );
          
          int time[] = calculateHoursWDaysM( listRegisterWorkday );
          updateParameterGeneral( parameterGeneralDTO, time );
        } else {
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_DELETE_DETAIL, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
          ConstantWEB.WEB_LOG.debug( "Error actualizando la jornada [" + selectedObject.getColDay() + "-" + selectedObject.getJornada()
                                     + "]" );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#update()
   */
  @Override
  public String update () {
    int jornada = 0;
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      selectedObject = ( WorkdayDTO ) getObjectSession( "selectedObject" );
      jornadaListDTO = ( JornadaListDTO ) getObjectSession( "jornadaListDTO" );
      if ( selectedObject != null && jornadaListDTO != null ) {
        
        selectedObject.setModificationDate( currentDate );
        selectedObject.setModificationUser( getUserInSession() );
        
        SimpleDateFormat sdf = new SimpleDateFormat( "HHmm" );
        jornada = selectedObject.getJornada();
        if ( jornada == 1 ) {
          selectedObject.setStartTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getStarDate1() ) ) ) );
          selectedObject.setEndTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getEndDate1() ) ) ) );
        } else if ( jornada == 2 ) {
          selectedObject.setStartTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getStarDate2() ) ) ) );
          selectedObject.setEndTime( ( Integer.parseInt( sdf.format( jornadaListDTO.getEndDate2() ) ) ) );
        } else if ( jornada == 3 ) {
          selectedObject.setStartTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getStarDate3() ) ) ) );
          selectedObject.setEndTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getEndDate3() ) ) ) );
        } else if ( jornada == 4 ) {
          selectedObject.setStartTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getStarDate4() ) ) ) );
          selectedObject.setEndTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getEndDate4() ) ) ) );
        }
        
        ConstantWEB.WEB_LOG.info( "Se procede a actualizar la jornada :: " + selectedObject.toString() );
        responseDTO = generalDelegate.edit( selectedObject );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Jornada [" + selectedObject.getColDay() + "-" + selectedObject.getJornada()
                                    + "] Actualizada exitosamente" );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
          
          int time[] = calculateHoursWDaysM( listRegisterWorkday );
          updateParameterGeneral( parameterGeneralDTO, time );
          
        } else {
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
          ConstantWEB.WEB_LOG.debug( "Error actualizando la jornada [" + selectedObject.getColDay() + "-" + selectedObject.getJornada()
                                     + "]" );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#add()
   */
  @SuppressWarnings ( "unchecked" )
  @Override
  public String add () {
    try {
      addObject = ( WorkdayDTO ) getObjectSession( "addObject" );
      jornadaListDTO = ( JornadaListDTO ) getObjectSession( "jornadaListDTO" );
      selectedCloneWorkday = ( List< String > ) getObjectSession( "selectedCloneWorkday" );
      
      if ( addObject != null && jornadaListDTO != null ) {
        addWorkday( addObject, jornadaListDTO );
        
        if ( selectedCloneWorkday != null ) {
          for ( String clone: selectedCloneWorkday ) {
            addObject.setId( 0 );
            addObject.setColDay( clone );
            addWorkday( addObject, jornadaListDTO );
          }
        }
        
        this.initElement();
        addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
        
        int time[] = calculateHoursWDaysM( listRegisterWorkday );
        updateParameterGeneral( parameterGeneralDTO, time );
        
      }
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Load parameter general. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (5/03/2015)
   */
  private void loadParameterGeneral () {
    try {
      parameterGeneralDTO = getParamGeneralClientCetus();
      if ( parameterGeneralDTO != null ) {
        disabledTabWorkday = false;
        if ( parameterGeneralDTO.getHourWeek() > 0 ) {
          disabledTabExceptionWorkday = false;
        } else {
          parameterGeneralDTO.setHourWeek( ( 0 ) );
        }
        
      } else {
        parameterGeneralDTO = new ParameterGeneralDTO();
        if ( parameterGeneralDTO.getDayMonth() >= 0 ) {
          parameterGeneralDTO.setDayMonth( ( 0 ) );
        }
        if ( parameterGeneralDTO.getHourWeek() >= 0 ) {
          parameterGeneralDTO.setHourWeek( ( 0 ) );
        }
        if ( parameterGeneralDTO.getTimeBeforeExpiration() >= 0 ) {
          parameterGeneralDTO.setTimeBeforeExpiration( ( 0 ) );
        }
        if ( parameterGeneralDTO.getTimeAfterExpiration() >= 0 ) {
          parameterGeneralDTO.setTimeAfterExpiration( ( 0 ) );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load save parameter. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (4/03/2015)
   */
  public void loadSaveParameter ( ActionEvent event ) {
    try {
      if ( parameterGeneralDTO != null ) {
        
          addObjectSession( parameterGeneralDTO, "parameterGeneralDTO" );
          showConfirmSaveParameter = true;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Save parameter. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (4/03/2015)
   */
  public String saveParameter () {
    ResponseDTO responseDTO = null;
    try {
      showConfirmSaveParameter = false;
      
      parameterGeneralDTO = ( ParameterGeneralDTO ) getObjectSession( "parameterGeneralDTO" );
      if ( parameterGeneralDTO != null ) {
        parameterGeneralDTO.setClientCetus( new ClientCetusDTO() );
        parameterGeneralDTO.getClientCetus().setId( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
        
        ConstantWEB.WEB_LOG.info( "Se procede a guardar los parametros generales para el cliente cetus" );
        if ( parameterGeneralDTO.getId() > 0 ) {
          parameterGeneralDTO.setModificationDate( currentDate );
          parameterGeneralDTO.setModificationUser( getUserInSession() );
          responseDTO = generalDelegate.edit( parameterGeneralDTO );
        } else {
          parameterGeneralDTO.setCreationDate( currentDate );
          parameterGeneralDTO.setCreationUser( getUserInSession() );
          responseDTO = generalDelegate.create( parameterGeneralDTO );
        }
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.debug( "Parametros guardados exitosamente." );
          disabledTabWorkday = false;
          addObjectSession( parameterGeneralDTO, ConstantWEB.DESC_PARAMETER_GENERAL_DTO );
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
        } else {
          //Obetener codigo de error de oracle
          String code = "0";
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
    return null;
  }
  
  /**
   * </p> Validate selected record. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (10/03/2015)
   */
  public void validateSelectedRecord ( ActionEvent event ) {
    try {
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        
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
  
  /**
   * </p> Load update. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (10/03/2015)
   */
  public void loadUpdate ( ActionEvent event ) {
    try {
      selectedObject = ( WorkdayDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null && jornadaListDTO != null ) {
        if ( !validateDatesSingle( jornadaListDTO, "Update" ) ) {
          if ( !validateDateSequence( jornadaListDTO, "Update" ) ) {
            this.showDialogConfirmUpdate = true;
          }
        }
        addObjectSession( jornadaListDTO, "jornadaListDTO" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (12/03/2015)
   */
  public void load ( ActionEvent event ) {
    try {
      if ( addObject != null && jornadaListDTO != null ) {
        addObjectSession( selectedCloneWorkday, "selectedCloneWorkday" );
        if ( jornadaListDTO.getNumberJornada() >= addObject.getJornada() ) {
          addMessageError( "jornadaAdd", ConstantWEB.ERROR_MIN_JORNADA, null );
        } else {
          if ( !validateDatesSingle( jornadaListDTO, "Add" ) ) {
            if ( !validateDateSequence( jornadaListDTO, "Add" ) ) {
              this.showConfirmAdd = true;
            }
          }
        }
        addObjectSession( addObject, "addObject" );
        addObjectSession( jornadaListDTO, "jornadaListDTO" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> List register workday. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (12/03/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void listRegisterWorkday ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findWorkDayByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Respuesta del servicio 
        this.listRegisterWorkday = ( List< WorkdayDTO > ) response.getObjectResponse();
        for ( WorkdayDTO workdayDTO: listRegisterWorkday ) {
          workdayDTO.setStartTimeStr( formatHourForView( workdayDTO.getStartTime() ) );
          workdayDTO.setEndTimeStr( formatHourForView( workdayDTO.getEndTime() ) );
        }
      } else {
        //si no encontro registro para listar 
        if ( UtilCommon.validateResponseNoResult( response ) ) {
          this.listRegisterWorkday = new ArrayList< WorkdayDTO >();
        }
      }
      addObjectSession( listRegisterWorkday, "listRegisterWorkday" );
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> Load week day. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (12/03/2015)
   */
  private void loadWeekDay () {
    listWeekDay = new ArrayList< SelectItem >();
    try {
      for ( EWeekDay eWeekDay: EWeekDay.values() ) {
        listWeekDay.add( new SelectItem( eWeekDay.getValue(), eWeekDay.getLabel() ) );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load jornada. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (12/03/2015)
   */
  private void loadJornada () {
    listJornada = new ArrayList< SelectItem >();
    try {
      for ( String jornada: ConstantWEB.LIST_JORNADAS ) {
        listJornada.add( new SelectItem( ( jornada ), jornada ) );
      }
      addObjectSession( listJornada, "listJornada" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Change day. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (13/03/2015)
   */
  @SuppressWarnings ( "unchecked" )
  public void changeDay () {
    List< WorkdayDTO > listWorkday = null;
    try {
      if ( addObject != null && addObject.getColDay() != null ) {
        
        disableCloneWorkday = true;
        cloneWorkday = false;
        showCloneWorkday = false;
        addObjectSession( disableCloneWorkday, "disableCloneWorkday" );
        addObjectSession( cloneWorkday, "cloneWorkday" );
        addObjectSession( showCloneWorkday, "showCloneWorkday" );
        
        loadJornada();
        listWorkday = ( ArrayList< WorkdayDTO > ) getObjectSession( "listRegisterWorkday" );
        jornadaListDTO = new JornadaListDTO();
        jornadaListDTO.loadJornadaListDTO( listWorkday, addObject.getColDay(), true, true );
        
        if ( jornadaListDTO.getNumberJornada() > 0 ) {
          addObject.setJornada( ( jornadaListDTO.getNumberJornada() ) );
        } else {
          addObject.setJornada( 0 );
        }
        
        addObjectSession( addObject, "addObject" );
        addObjectSession( jornadaListDTO, "jornadaListDTO" );
        
      } else {
        listJornada = new ArrayList< SelectItem >();
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Change jornada. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (13/03/2015)
   */
  public void changeJornada () {
    try {
      addObject = ( WorkdayDTO ) getObjectSession( "addObject" );
      jornadaListDTO = ( JornadaListDTO ) getObjectSession( "jornadaListDTO" );
      if ( addObject != null && addObject.getJornada() >= 0 && jornadaListDTO != null ) {
        disableCloneWorkday = false;
        cloneWorkday = false;
        showCloneWorkday = false;
        
        addObjectSession( disableCloneWorkday, "disableCloneWorkday" );
        addObjectSession( cloneWorkday, "cloneWorkday" );
        addObjectSession( showCloneWorkday, "showCloneWorkday" );
        
        if ( jornadaListDTO.getNumberJornada() > addObject.getJornada() ) {
          addMessageError( "jornadaAdd", ConstantWEB.ERROR_MIN_JORNADA, null );
        } else {
          int i = jornadaListDTO.getNumberJornada();
          jornadaListDTO.disableAll();
          while ( i < addObject.getJornada() ) {
            i++;
            if ( i == 1 ) {
              jornadaListDTO.setDisable1( false );
              jornadaListDTO.setRegisteredDB1( false );
            } else if ( i == 2 ) {
              jornadaListDTO.setDisable2( false );
              jornadaListDTO.setRegisteredDB2( false );
            } else if ( i == 3 ) {
              jornadaListDTO.setDisable3( false );
              jornadaListDTO.setRegisteredDB3( false );
            } else if ( i == 4 ) {
              jornadaListDTO.setDisable4( false );
              jornadaListDTO.setRegisteredDB4( false );
            }
          }
          addObjectSession( jornadaListDTO, "jornadaListDTO" );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Clear add work day. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (18/03/2015)
   */
  public void clearAddWorkDay ( ActionEvent event ) {
    try {
      
      addObject = new WorkdayDTO();
      jornadaListDTO = new JornadaListDTO();
      listJornada = new ArrayList< SelectItem >();
      disableCloneWorkday = true;
      cloneWorkday = false;
      showCloneWorkday = false;
      selectedCloneWorkday = new ArrayList< String >();
      
      addObjectSession( addObject, "addObject" );
      addObjectSession( jornadaListDTO, "jornadaListDTO" );
      addObjectSession( listJornada, "listJornada" );
      addObjectSession( disableCloneWorkday, "disableCloneWorkday" );
      addObjectSession( cloneWorkday, "cloneWorkday" );
      addObjectSession( showCloneWorkday, "showCloneWorkday" );
      addObjectSession( selectedCloneWorkday, "selectedCloneWorkday" );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Validate dates single. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param jornadaListDTO the jornada list dto
   * @param validateAcction the validate acction
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (17/03/2015)
   */
  private boolean validateDatesSingle ( JornadaListDTO jornadaListDTO, String validateAcction ) {
    boolean result = false;
    try {
      if ( jornadaListDTO != null ) {
        if ( jornadaListDTO.getStarDate1() != null && jornadaListDTO.getEndDate1() != null
             && compareTime( jornadaListDTO.getStarDate1(), jornadaListDTO.getEndDate1() ) == 1 ) {
          result = true;
          if ( validateAcction.equals( "Add" ) ) {
            addMessageError( "endTime1Add", ConstantWEB.ERROR_TIME_START_HIGHER_END, null );
          } else {
            addMessageError( "endTime1Update", ConstantWEB.ERROR_TIME_START_HIGHER_END, null );
          }
        }
        
        if ( jornadaListDTO.getStarDate2() != null && jornadaListDTO.getEndDate2() != null
             && compareTime( jornadaListDTO.getStarDate2(), jornadaListDTO.getEndDate2() ) == 1 ) {
          result = true;
          if ( validateAcction.equals( "Add" ) ) {
            addMessageError( "endTime2Add", ConstantWEB.ERROR_TIME_START_HIGHER_END, null );
          } else {
            addMessageError( "endTime2Update", ConstantWEB.ERROR_TIME_START_HIGHER_END, null );
          }
        }
        
        if ( jornadaListDTO.getStarDate3() != null && jornadaListDTO.getEndDate3() != null
             && compareTime( jornadaListDTO.getStarDate3(), jornadaListDTO.getEndDate3() ) == 1 ) {
          result = true;
          if ( validateAcction.equals( "Add" ) ) {
            addMessageError( "endTime3Add", ConstantWEB.ERROR_TIME_START_HIGHER_END, null );
          } else {
            addMessageError( "endTime3Update", ConstantWEB.ERROR_TIME_START_HIGHER_END, null );
          }
        }
        
        if ( jornadaListDTO.getStarDate4() != null && jornadaListDTO.getEndDate4() != null
             && compareTime( jornadaListDTO.getStarDate4(), jornadaListDTO.getEndDate4() ) == 1 ) {
          result = true;
          if ( validateAcction.equals( "Add" ) ) {
            addMessageError( "endTime4Add", ConstantWEB.ERROR_TIME_START_HIGHER_END, null );
          } else {
            addMessageError( "endTime4Update", ConstantWEB.ERROR_TIME_START_HIGHER_END, null );
          }
        }
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  /**
   * </p> Validate date sequence. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param jornadaListDTO the jornada list dto
   * @param validateAcction the validate acction
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (17/03/2015)
   */
  private boolean validateDateSequence ( JornadaListDTO jornadaListDTO, String validateAcction ) {
    boolean result = false;
    try {
      if ( jornadaListDTO != null ) {
        if ( jornadaListDTO.getEndDate1() != null && jornadaListDTO.getStarDate2() != null
             && compareTime( jornadaListDTO.getStarDate2(), jornadaListDTO.getEndDate1() ) == -1 ) {
          result = true;
          if ( validateAcction.equals( "Add" ) ) {
            addMessageError( "endTime2Add", ConstantWEB.ERROR_TIME_SEQUENCE, null );
          } else {
            addMessageError( "endTime2Update", ConstantWEB.ERROR_TIME_SEQUENCE, null );
          }
        }
        
        if ( jornadaListDTO.getEndDate2() != null && jornadaListDTO.getStarDate3() != null
             && compareTime( jornadaListDTO.getStarDate3(), jornadaListDTO.getEndDate2() ) == -1 ) {
          result = true;
          if ( validateAcction.equals( "Add" ) ) {
            addMessageError( "endTime3Add", ConstantWEB.ERROR_TIME_SEQUENCE, null );
          } else {
            addMessageError( "endTime3Update", ConstantWEB.ERROR_TIME_SEQUENCE, null );
          }
        }
        
        if ( jornadaListDTO.getEndDate3() != null && jornadaListDTO.getStarDate4() != null
             && compareTime( jornadaListDTO.getStarDate4(), jornadaListDTO.getEndDate3() ) == -1 ) {
          result = true;
          if ( validateAcction.equals( "Add" ) ) {
            addMessageError( "endTime4Add", ConstantWEB.ERROR_TIME_SEQUENCE, null );
          } else {
            addMessageError( "endTime4Update", ConstantWEB.ERROR_TIME_SEQUENCE, null );
          }
        }
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  /**
   * </p> Compare time. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param date1 the date1
   * @param date2 the date2
   * @return el int
   * @since CetusControlWEB (19/03/2015)
   */
  private int compareTime ( Date date1, Date date2 ) {
    int result = -2;
    try {
      if ( date1 != null && date2 != null ) {
        SimpleDateFormat sdf = new SimpleDateFormat( "HHmm" );
        Integer time1 = new Integer( sdf.format( date1 ) );
        Integer time2 = new Integer( sdf.format( date2 ) );
        
        if ( time1 == time2 ) {
          result = 0;
        } else if ( time1 < time2 ) {
          result = -1;
        } else if ( time1 > time2 ) {
          result = 1;
        }
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  /**
   * </p> Adds the workday. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param workdayDTO the workday dto
   * @since CetusControlWEB (18/03/2015)
   */
  private void addWorkday ( WorkdayDTO workdayDTO, JornadaListDTO jornadaListDTO ) {
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      
      if ( workdayDTO != null && jornadaListDTO != null ) {
        workdayDTO.setClientCetus( new ClientCetusDTO() );
        workdayDTO.getClientCetus().setId( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
        workdayDTO.setCreationDate( currentDate );
        workdayDTO.setCreationUser( getUserInSession() );
        
        SimpleDateFormat sdf = new SimpleDateFormat( "HHmm" );
        
        if ( jornadaListDTO.getStarDate1() != null && jornadaListDTO.getEndDate1() != null && !jornadaListDTO.isRegisteredDB1() ) {
          workdayDTO.setStartTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getStarDate1() ) ) ) );
          workdayDTO.setEndTime( ( Integer.parseInt( sdf.format( jornadaListDTO.getEndDate1() ) ) ) );
          workdayDTO.setJornada( ( 1 ) );
          
          ConstantWEB.WEB_LOG.info( "Se procede a insertar la jornada :: " + workdayDTO.toString() );
          
          responseDTO = generalDelegate.create( workdayDTO );
          
          ConstantWEB.WEB_LOG.info( "Respuesta de la creacion :: " + responseDTO.toString() );
          
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            ConstantWEB.WEB_LOG.info( "Jornada [" + workdayDTO.getColDay() + "-" + workdayDTO.getJornada() + "] Creada exitosamente" );
          } else {
            //Obetener codigo de error de oracle
            if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
              code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
            }
            addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
          }
        }
        
        if ( jornadaListDTO.getStarDate2() != null && jornadaListDTO.getEndDate2() != null && !jornadaListDTO.isRegisteredDB2() ) {
          workdayDTO.setId( 0 );
          workdayDTO.setStartTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getStarDate2() ) ) ) );
          workdayDTO.setEndTime( ( Integer.parseInt( sdf.format( jornadaListDTO.getEndDate2() ) ) ) );
          workdayDTO.setJornada( ( 2 ) );
          
          ConstantWEB.WEB_LOG.info( "Se procede a insertar la jornada :: " + workdayDTO.toString() );
          
          responseDTO = generalDelegate.create( workdayDTO );
          
          ConstantWEB.WEB_LOG.info( "Respuesta de la creacion :: " + responseDTO.toString() );
          
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            ConstantWEB.WEB_LOG.info( "Jornada [" + workdayDTO.getColDay() + "-" + workdayDTO.getJornada() + "] Creada exitosamente" );
          } else {
            //Obetener codigo de error de oracle
            if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
              code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
            }
            addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
          }
        }
        
        if ( jornadaListDTO.getStarDate3() != null && jornadaListDTO.getEndDate3() != null && !jornadaListDTO.isRegisteredDB3() ) {
          workdayDTO.setId( 0 );
          workdayDTO.setStartTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getStarDate3() ) ) ) );
          workdayDTO.setEndTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getEndDate3() ) ) ) );
          workdayDTO.setJornada( ( 3 ) );
          
          ConstantWEB.WEB_LOG.info( "Se procede a insertar la jornada :: " + workdayDTO.toString() );
          
          responseDTO = generalDelegate.create( workdayDTO );
          
          ConstantWEB.WEB_LOG.info( "Respuesta de la creacion :: " + responseDTO.toString() );
          
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            ConstantWEB.WEB_LOG.info( "Jornada [" + workdayDTO.getColDay() + "-" + workdayDTO.getJornada() + "] Creada exitosamente" );
          } else {
            //Obetener codigo de error de oracle
            if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
              code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
            }
            addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
          }
        }
        
        if ( jornadaListDTO.getStarDate4() != null && jornadaListDTO.getEndDate4() != null && !jornadaListDTO.isRegisteredDB4() ) {
          workdayDTO.setId( 0 );
          workdayDTO.setStartTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getStarDate4() ) ) ) );
          workdayDTO.setEndTime( Integer.parseInt( ( sdf.format( jornadaListDTO.getEndDate4() ) ) ) );
          workdayDTO.setJornada( ( 4 ) );
          
          ConstantWEB.WEB_LOG.info( "Se procede a insertar la jornada :: " + workdayDTO.toString() );
          
          responseDTO = generalDelegate.create( workdayDTO );
          
          ConstantWEB.WEB_LOG.info( "Respuesta de la creacion :: " + responseDTO.toString() );
          
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            ConstantWEB.WEB_LOG.info( "Jornada [" + workdayDTO.getColDay() + "-" + workdayDTO.getJornada() + "] Creada exitosamente" );
          } else {
            //Obetener codigo de error de oracle
            if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
              code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
            }
            addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
          }
        }
        
        ConstantWEB.WEB_LOG.info( "Se crearon todas las Jornadas para el dia [" + workdayDTO.getColDay() + "]" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Change clone workday. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (18/03/2015)
   */
  public void changeCloneWorkday () {
    cloneListWeekDay = new ArrayList< SelectItem >();
    selectedCloneWorkday = new ArrayList< String >();
    boolean exists = false;
    try {
      if ( cloneWorkday ) {
        showCloneWorkday = true;
        @SuppressWarnings ( "unchecked" )
        ArrayList< WorkdayDTO > listWorkday = ( ArrayList< WorkdayDTO > ) getObjectSession( "listRegisterWorkday" );
        for ( EWeekDay eWeekDay: EWeekDay.values() ) {
          addObject = ( WorkdayDTO ) getObjectSession( "addObject" );
          if ( addObject != null && addObject.getColDay() != null && addObject.getColDay().equals( eWeekDay.getValue() ) ) {
            cloneListWeekDay.add( new SelectItem( eWeekDay.getValue(), eWeekDay.getLabel(), null, true ) );
            selectedCloneWorkday.add( eWeekDay.getValue() );
          } else if ( listWorkday != null ) {
            exists = false;
            for ( WorkdayDTO workdayDTO: listWorkday ) {
              if ( workdayDTO.getColDay().equals( eWeekDay.getValue() ) ) {
                cloneListWeekDay.add( new SelectItem( eWeekDay.getValue(), eWeekDay.getLabel(), null, true ) );
                selectedCloneWorkday.add( eWeekDay.getValue() );
                exists = true;
                break;
              }
            }
            if ( !exists ) {
              cloneListWeekDay.add( new SelectItem( eWeekDay.getValue(), eWeekDay.getLabel() ) );
            }
          } else {
            cloneListWeekDay.add( new SelectItem( eWeekDay.getValue(), eWeekDay.getLabel() ) );
          }
        }
      } else {
        showCloneWorkday = false;
      }
      addObjectSession( showCloneWorkday, "showCloneWorkday" );
      addObjectSession( cloneWorkday, "cloneWorkday" );
      addObjectSession( cloneListWeekDay, "cloneListWeekDay" );
      addObjectSession( selectedCloneWorkday, "selectedCloneWorkday" );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Update workday. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (19/03/2015)
   */
  public void updateWorkday ( ActionEvent event ) {
    int jornada = 0;
    try {
      validateSelectedRecord( event );
      if ( selectedObject != null && showConfirmMod ) {
        jornadaListDTO = new JornadaListDTO();
        jornadaListDTO.loadJornadaListDTO( listRegisterWorkday, selectedObject.getColDay(), true, true );
        
        jornada = selectedObject.getJornada();
        if ( jornada == 1 ) {
          jornadaListDTO.setDisable1( false );
        } else if ( jornada == 2 ) {
          jornadaListDTO.setDisable2( false );
        } else if ( jornada == 3 ) {
          jornadaListDTO.setDisable3( false );
        } else if ( jornada == 4 ) {
          jornadaListDTO.setDisable4( false );
        }
        loadJornada();
        addObjectSession( jornadaListDTO, "jornadaListDTO" );
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Calculate hours w days m. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param listWorkday the list workday
   * @return el int[]
   * @since CetusControlWEB (20/03/2015)
   */
  private int[] calculateHoursWDaysM ( List< WorkdayDTO > listWorkday ) {
    int[] result = new int[2];
    int hoursWeek = 0;
    List< String > listDayWeek = new ArrayList< String >();
    try {
      if ( listWorkday != null ) {
        for ( WorkdayDTO workdayDTO: listWorkday ) {
          if ( !listDayWeek.contains( workdayDTO.getColDay() ) ) {
            listDayWeek.add( workdayDTO.getColDay() );
          }
          hoursWeek += ( workdayDTO.getEndTime() - workdayDTO.getStartTime() );
        }
        
        result[0] = ( hoursWeek / 100 );
        if ( listDayWeek.size() > 0 ) {
          result[1] = ( listDayWeek.size() * 4 ) + 2;
        } else {
          result[1] = 0;
        }
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    ConstantWEB.WEB_LOG.info( "hoursWeek=" + result[0] + ", daysWeek=" + result[0] );
    
    return result;
  }
  
  /**
   * </p> Update parameter general. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param parameterGeneralDTO the parameter general dto
   * @param time the time
   * @since CetusControlWEB (20/03/2015)
   */
  private void updateParameterGeneral ( ParameterGeneralDTO parameterGeneralDTO, int[] time ) {
    try {
      parameterGeneralDTO.setHourWeek( ( time[0] ) );
      parameterGeneralDTO.setDayMonth( ( time[1] ) );
      parameterGeneralDTO.setModificationDate( currentDate );
      parameterGeneralDTO.setModificationUser( getUserInSession() );
      
      ConstantWEB.WEB_LOG.info( "Se procede a actualizar los parametros generales :: " + parameterGeneralDTO.toString() );
      ResponseDTO responseDTO = generalDelegate.edit( parameterGeneralDTO );
      ConstantWEB.WEB_LOG.info( "Actualizar los parametros generales responseDTO :: " + responseDTO.toString() );
      
      if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
        ConstantWEB.WEB_LOG.debug( "Parametros actualizados exitosamente." );
        addObjectSession( parameterGeneralDTO, ConstantWEB.DESC_PARAMETER_GENERAL_DTO );
        disabledTabExceptionWorkday = false;
      } else {
        //Obetener codigo de error de oracle
        String code = "0";
        if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
          code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
        }
        addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Validate selected record excep. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (21/03/2015)
   */
  public void validateSelectedRecordExcepView ( ActionEvent event ) {
    try {
      if ( selectedObjectExcep != null ) {
        addObjectSession( selectedObjectExcep, "selectedObjectExcep" );
        this.showAlertSelectRowExcep = false;
        this.showViewDetailExcep = true;
      } else {
        this.showAlertSelectRowExcep = true;
        this.showViewDetailExcep = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Validate selected record excep. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (24/03/2015)
   */
  public void validateSelectedRecordExcepUpd ( ActionEvent event ) {
    SimpleDateFormat sdf = new SimpleDateFormat( ConstantWEB.PATTERN_DATE );
    String dayWeek = null;
    String existsExcep = null;
    List< String > list = new ArrayList< String >();
    try {
      Date lcurrentDate = sdf.parse( currentDateStr );
      
      if ( selectedObjectExcep != null ) {
        if ( selectedObjectExcep.getDateException().compareTo( lcurrentDate ) >= 0 ) {
          this.showConfirmModExcep = true;
          addObjectSession( selectedObjectExcep, "selectedObjectExcep" );
          
          Calendar calDateSelected = Calendar.getInstance();
          calDateSelected.setTime( selectedObjectExcep.getDateException() );
          dayWeek = EWeekDay.getValue( calDateSelected.get( Calendar.DAY_OF_WEEK ) );
          list.add( "0" );
          for ( WorkdayDTO workdayDTO: listRegisterWorkday ) {
            if ( workdayDTO.getColDay().equals( dayWeek ) ) {
              list.add( String.valueOf( workdayDTO.getJornada() ) );
            }
          }
          
          for ( ExceptionWorkdayDTO exceptionWorkdayDTO: listRegisterException ) {
            if ( exceptionWorkdayDTO.getDateException().compareTo( selectedObjectExcep.getDateException() ) == 0 ) {
              if ( existsExcep == null ) {
                existsExcep = exceptionWorkdayDTO.getJornada() + "";
              } else {
                existsExcep += "," + exceptionWorkdayDTO.getJornada();
              }
            }
          }
          
          if ( existsExcep != null ) {
            String arrExistsExcep[] = existsExcep.split( "," );
            if ( arrExistsExcep.length > 1 ) {
              list.remove( 0 );
              for ( String string: arrExistsExcep ) {
                if ( list.contains( string ) && !string.equals( selectedObjectExcep.getJornada() ) ) {
                  list.remove( list.indexOf( string ) );
                }
              }
            }
          }
          
          listJornadaExcep = new ArrayList< SelectItem >();
          for ( String string: list ) {
            if ( string.equals( "0" ) ) {
              listJornadaExcep.add( new SelectItem( ( string ), ConstantWEB.DESC_ALL_DAY ) );
            } else {
              listJornadaExcep.add( new SelectItem( ( string ), string ) );
            }
          }
          addObjectSession( listJornadaExcep, "listJornadaExcep" );
        } else {
          this.showAlertSelectDateBefore = true;
        }
      } else {
        this.showAlertSelectRowExcep = true;
        this.showConfirmModExcep = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Validate selected record excep del. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (24/03/2015)
   */
  public void validateSelectedRecordExcepDel ( ActionEvent event ) {
    SimpleDateFormat sdf = new SimpleDateFormat( ConstantWEB.PATTERN_DATE );
    try {
      Date lcurrentDate = sdf.parse( currentDateStr );
      
      if ( selectedObjectExcep != null ) {
        if ( selectedObjectExcep.getDateException().compareTo( lcurrentDate ) >= 0 ) {
          this.showConfirmDeleteExcep = true;
          addObjectSession( selectedObjectExcep, "selectedObjectExcep" );
        } else {
          this.showAlertSelectDateBefore = true;
        }
      } else {
        this.showAlertSelectRowExcep = true;
        this.showConfirmDeleteExcep = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> List exception workday. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (23/03/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void listExceptionWorkday ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findExcepWorkdayByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listRegisterException = ( List< ExceptionWorkdayDTO > ) response.getObjectResponse();
      } else {
        this.listRegisterException = new ArrayList< ExceptionWorkdayDTO >();
      }
      addObjectSession( listRegisterException, "listRegisterException" );
    } catch ( Exception e ) {
      addMessageError( null, e.getMessage(), "" );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    
  }
  
  /**
   * </p> Date exception select. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (23/03/2015)
   */
  public void dateExceptionSelect ( SelectEvent event ) {
    Date dateSelected = null;
    String dayWeek = null;
    int i = 0;
    try {
      if ( event != null && event.getObject() != null ) {
        dateSelected = ( Date ) event.getObject();
        Calendar calDateSelected = Calendar.getInstance();
        calDateSelected.setTime( dateSelected );
        dayWeek = EWeekDay.getValue( calDateSelected.get( Calendar.DAY_OF_WEEK ) );
        listJornadaExcep = new ArrayList< SelectItem >();
        listJornadaExcep.add( new SelectItem( ( 0 ), ConstantWEB.DESC_ALL_DAY ) );
        for ( WorkdayDTO workdayDTO: listRegisterWorkday ) {
          if ( workdayDTO.getColDay().equals( dayWeek ) ) {
            listJornadaExcep.add( new SelectItem( workdayDTO.getJornada(), String.valueOf( workdayDTO.getJornada() ) ) );
          }
        }
        
        for ( ExceptionWorkdayDTO exceptionWorkdayDTO: listRegisterException ) {
          if ( exceptionWorkdayDTO.getDateException().compareTo( dateSelected ) == 0 ) {
            i = 0;
            for ( SelectItem selectItem: listJornadaExcep ) {
              if ( exceptionWorkdayDTO.getJornada() == 0 ) {
                listJornadaExcep = new ArrayList< SelectItem >();
                addMessageError( "dateExcepAdd", ConstantWEB.ERROR_EXISTS_EXCEPTION_ALL_DAY, null );
                disableBtnAddException = true;
                break;
              } else if ( ( exceptionWorkdayDTO.getJornada() == ( int ) selectItem.getValue() ) ) {
                if ( listJornadaExcep.size() > 1 ) {
                  listJornadaExcep.remove( i );
                  listJornadaExcep.remove( 0 );
                } else {
                  listJornadaExcep.remove( 0 );
                }
                break;
              }
              i++;
            }
          }
        }
        
        if ( listJornadaExcep.size() == 0 ) {
          addMessageError( "dateExcepAdd", ConstantWEB.ERROR_EXISTS_EXCEPTION_ALL_JORNADA, null );
          disableBtnAddException = true;
        }
        
        addObjectSession( addObjectExcep, "addObjectExcep" );
        addObjectSession( listJornadaExcep, "listJornadaExcep" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load update excep. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (24/03/2015)
   */
  public void loadUpdateExcep ( ActionEvent event ) {
    try {
      if ( selectedObjectExcep != null ) {
        ExceptionWorkdayDTO selected = ( ExceptionWorkdayDTO ) getObjectSession( "selectedObjectExcep" );
        this.showDialogConfirmUpdExcep = true;
        selectedObjectExcep.setClientCetus( selected.getClientCetus() );
        selectedObjectExcep.setCreationDate( selected.getCreationDate() );
        selectedObjectExcep.setCreationUser( selected.getCreationUser() );
        selectedObjectExcep.setId( selected.getId() );
        selectedObjectExcep.setDateException( selected.getDateException() );
        
        addObjectSession( selectedObjectExcep, "selectedObjectExcep" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Update exception. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (24/03/2015)
   */
  public String updateException () {
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      
      selectedObjectExcep = ( ExceptionWorkdayDTO ) getObjectSession( "selectedObjectExcep" );
      
      if ( selectedObjectExcep != null ) {
        selectedObjectExcep.setModificationDate( currentDate );
        selectedObjectExcep.setModificationUser( getUserInSession() );
        
        ConstantWEB.WEB_LOG.info( "Se procede a actualizar la excepcion :: " + selectedObjectExcep.toString() );
        
        responseDTO = generalDelegate.edit( selectedObjectExcep );
        
        ConstantWEB.WEB_LOG.info( "Respuesta de la actualizar de la excepcion :: " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Excepcion laboral actualizada exitosamente" );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_UPDATE );
        } else {
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_UPDATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  public String removeException () {
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      selectedObjectExcep = ( ExceptionWorkdayDTO ) getObjectSession( "selectedObjectExcep" );
      
      if ( selectedObjectExcep != null ) {
        
        ConstantWEB.WEB_LOG.info( "Se procede a eliminar la excepcion :: " + selectedObjectExcep.toString() );
        
        responseDTO = generalDelegate.remove( selectedObjectExcep );
        
        ConstantWEB.WEB_LOG.info( "Respuesta de la eliminar de la excepcion :: " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Excepcion laboral eliminada exitosamente" );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MESSAGE_SUCCES_DELETE );
        } else {
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_DELETE_DETAIL, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  /**
   * </p> Clear add exception. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (23/03/2015)
   */
  public void clearAddException ( ActionEvent event ) {
    try {
      
      addObjectExcep = new ExceptionWorkdayDTO();
      listJornadaExcep = new ArrayList< SelectItem >();
      
      addObjectSession( addObjectExcep, "addObjectExcep" );
      addObjectSession( listJornadaExcep, "listJornadaExcep" );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load add exception. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (24/03/2015)
   */
  public void loadAddException ( ActionEvent event ) {
    try {
      if ( addObjectExcep != null ) {
        this.showConfirmAddExcep = true;
        addObjectSession( addObjectExcep, "addObjectExcep" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Adds the exception. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @return el string
   * @since CetusControlWEB (24/03/2015)
   */
  public String addException () {
    ResponseDTO responseDTO = null;
    String code = "0";
    try {
      addObjectExcep = ( ExceptionWorkdayDTO ) getObjectSession( "addObjectExcep" );
      
      if ( addObjectExcep != null ) {
        addObjectExcep.setClientCetus( new ClientCetusDTO() );
        addObjectExcep.getClientCetus().setId( getUserDTO().getPerson().getClient().getClientCetus().getId() );
        addObjectExcep.setCreationDate( currentDate );
        addObjectExcep.setCreationUser( getUserInSession() );
        
        ConstantWEB.WEB_LOG.info( "Se procede a insertar la excepcion :: " + addObjectExcep.toString() );
        
        responseDTO = generalDelegate.create( addObjectExcep );
        
        ConstantWEB.WEB_LOG.info( "Respuesta de la creacion de la excepcion :: " + responseDTO.toString() );
        
        if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
          ConstantWEB.WEB_LOG.info( "Excepcion laboral Creada exitosamente" );
          this.initElement();
          addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, ConstantWEB.MSG_DETAIL_SUCCESS );
        } else {
          //Obetener codigo de error de oracle
          if ( !responseDTO.getMessage().isEmpty() && responseDTO.getMessage().contains( "ORA" ) ) {
            code = responseDTO.getMessage().substring( responseDTO.getMessage().lastIndexOf( "ORA" ), responseDTO.getMessage().lastIndexOf( ":" ) );
          }
          addMessageError( null, ConstantWEB.MESSAGE_ERROR_CREATE, code + ":" + ConstantWEB.MSG_DETAIL_ERROR );
        }
      }
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  private void loadNotifications () {
    listNotification = new ArrayList< SelectItem >();
    try {
      for ( int i = 1; i <= 9; i++ ) {
        listNotification.add( new SelectItem( new Integer( i ), String.valueOf( i ) ) );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void changeNotification () {
    try {
      cleanObjectSession( "notificationMask" );
      cleanObjectSession( "sizeNotificaton" );
      cleanObjectSession( "numberNotificatons" );
      for ( int i = 0; i < numberNotificatons; i++ ) {
        if ( i == 0 ) {
          notificationMask = "99";
        } else {
          notificationMask += "-99";
          sizeNotificaton += 3;
        }
      }
      addObjectSession( numberNotificatons, "numberNotificatons" );
      addObjectSession( notificationMask, "notificationMask" );
      addObjectSession( sizeNotificaton, "sizeNotificaton" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Validate conf notification. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param context the context
   * @param comp the comp
   * @param value the value
   * @since CetusControlWEB (7/09/2016)
   */
  public void validateConfNotification ( FacesContext context, UIComponent comp, Object value ) {
    String percentages = ( String ) value;
    String[] arrPercentage = null;
    int value1 = 0;
    boolean success = true;
    try {
      arrPercentage = percentages.split( "-" );
      if ( arrPercentage != null && arrPercentage.length > 1 ) {
        forInitial: for ( int i = 0; i < ( arrPercentage.length - 1 ); i++ ) {
          value1 = Integer.parseInt( arrPercentage[i] );
          for ( int j = ( i + 1 ); j < arrPercentage.length; j++ ) {
            if ( value1 >= Integer.parseInt( arrPercentage[j] ) ) {
              success = false;
              break forInitial;
            }
          }
          
        }
        
        if ( !success ) {
          ( ( UIInput ) comp ).setValid( false );
          FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ConstantWEB.CONFIGURATION_INVALID, null );
          context.addMessage( comp.getClientId( context ), message );
        }
        
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public boolean isDisabledTabWorkday () {
    return disabledTabWorkday;
  }
  
  public void setDisabledTabWorkday ( boolean disabledTabWorkday ) {
    this.disabledTabWorkday = disabledTabWorkday;
  }
  
  public boolean isDisabledTabExceptionWorkday () {
    return disabledTabExceptionWorkday;
  }
  
  public void setDisabledTabExceptionWorkday ( boolean disabledTabExceptionWorkday ) {
    this.disabledTabExceptionWorkday = disabledTabExceptionWorkday;
  }
  
  public boolean isShowConfirmSaveParameter () {
    return showConfirmSaveParameter;
  }
  
  public void setShowConfirmSaveParameter ( boolean showConfirmSaveParameter ) {
    this.showConfirmSaveParameter = showConfirmSaveParameter;
  }
  
  public ParameterGeneralDTO getParameterGeneralDTO () {
    return parameterGeneralDTO;
  }
  
  public void setParameterGeneralDTO ( ParameterGeneralDTO parameterGeneralDTO ) {
    this.parameterGeneralDTO = parameterGeneralDTO;
  }
  
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  public boolean isShowAlertSelectRow () {
    return showAlertSelectRow;
  }
  
  public void setShowAlertSelectRow ( boolean showAlertSelectRow ) {
    this.showAlertSelectRow = showAlertSelectRow;
  }
  
  public WorkdayDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( WorkdayDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  public boolean isShowConfirmMod () {
    return showConfirmMod;
  }
  
  public void setShowConfirmMod ( boolean showConfirmMod ) {
    this.showConfirmMod = showConfirmMod;
  }
  
  public boolean isShowConfirmDelete () {
    return showConfirmDelete;
  }
  
  public void setShowConfirmDelete ( boolean showConfirmDelete ) {
    this.showConfirmDelete = showConfirmDelete;
  }
  
  public boolean isShowDialogConfirmUpdate () {
    return showDialogConfirmUpdate;
  }
  
  public void setShowDialogConfirmUpdate ( boolean showDialogConfirmUpdate ) {
    this.showDialogConfirmUpdate = showDialogConfirmUpdate;
  }
  
  public boolean isShowConfirmAdd () {
    return showConfirmAdd;
  }
  
  public void setShowConfirmAdd ( boolean showConfirmAdd ) {
    this.showConfirmAdd = showConfirmAdd;
  }
  
  public WorkdayDTO getAddObject () {
    try {
      addObject = ( WorkdayDTO ) getObjectSession( "addObject" );
      if ( addObject == null ) {
        addObject = new WorkdayDTO();
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return addObject;
  }
  
  public void setAddObject ( WorkdayDTO addObject ) {
    this.addObject = addObject;
  }
  
  public List< WorkdayDTO > getListRegisterWorkday () {
    return listRegisterWorkday;
  }
  
  public void setListRegisterWorkday ( List< WorkdayDTO > listRegisterWorkday ) {
    this.listRegisterWorkday = listRegisterWorkday;
  }
  
  public List< SelectItem > getListWeekDay () {
    return listWeekDay;
  }
  
  public void setListWeekDay ( List< SelectItem > listWeekDay ) {
    this.listWeekDay = listWeekDay;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListJornada () {
    try {
      listJornada = ( List< SelectItem > ) getObjectSession( "listJornada" );
      if ( listJornada == null ) {
        listJornada = new ArrayList< SelectItem >();
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return listJornada;
  }
  
  public void setListJornada ( List< SelectItem > listJornada ) {
    this.listJornada = listJornada;
  }
  
  public JornadaListDTO getJornadaListDTO () {
    try {
      jornadaListDTO = ( JornadaListDTO ) getObjectSession( "jornadaListDTO" );
      if ( jornadaListDTO == null ) {
        jornadaListDTO = new JornadaListDTO();
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return jornadaListDTO;
  }
  
  public void setJornadaListDTO ( JornadaListDTO jornadaListDTO ) {
    this.jornadaListDTO = jornadaListDTO;
  }
  
  public boolean isCloneWorkday () {
    if ( ( Boolean ) getObjectSession( "cloneWorkday" ) != null ) {
      cloneWorkday = ( Boolean ) getObjectSession( "cloneWorkday" );
    }
    return cloneWorkday;
  }
  
  public void setCloneWorkday ( boolean cloneWorkday ) {
    this.cloneWorkday = cloneWorkday;
  }
  
  public boolean isDisableCloneWorkday () {
    if ( ( Boolean ) getObjectSession( "disableCloneWorkday" ) != null ) {
      disableCloneWorkday = ( Boolean ) getObjectSession( "disableCloneWorkday" );
    }
    return disableCloneWorkday;
  }
  
  public void setDisableCloneWorkday ( boolean disableCloneWorkday ) {
    this.disableCloneWorkday = disableCloneWorkday;
  }
  
  public boolean isShowCloneWorkday () {
    if ( ( Boolean ) getObjectSession( "showCloneWorkday" ) != null ) {
      showCloneWorkday = ( Boolean ) getObjectSession( "showCloneWorkday" );
    }
    return showCloneWorkday;
  }
  
  public void setShowCloneWorkday ( boolean showCloneWorkday ) {
    this.showCloneWorkday = showCloneWorkday;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< String > getSelectedCloneWorkday () {
    selectedCloneWorkday = ( List< String > ) getObjectSession( "selectedCloneWorkday" );
    if ( selectedCloneWorkday == null ) {
      selectedCloneWorkday = new ArrayList< String >();
    }
    return selectedCloneWorkday;
  }
  
  public void setSelectedCloneWorkday ( List< String > selectedCloneWorkday ) {
    this.selectedCloneWorkday = selectedCloneWorkday;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getCloneListWeekDay () {
    return ( List< SelectItem > ) getObjectSession( "cloneListWeekDay" );
  }
  
  public void setCloneListWeekDay ( List< SelectItem > cloneListWeekDay ) {
    this.cloneListWeekDay = cloneListWeekDay;
  }
  
  public ExceptionWorkdayDTO getSelectedObjectExcep () {
    return selectedObjectExcep;
  }
  
  public void setSelectedObjectExcep ( ExceptionWorkdayDTO selectedObjectExcep ) {
    this.selectedObjectExcep = selectedObjectExcep;
  }
  
  public boolean isShowAlertSelectRowExcep () {
    return showAlertSelectRowExcep;
  }
  
  public void setShowAlertSelectRowExcep ( boolean showAlertSelectRowExcep ) {
    this.showAlertSelectRowExcep = showAlertSelectRowExcep;
  }
  
  public boolean isShowViewDetailExcep () {
    return showViewDetailExcep;
  }
  
  public void setShowViewDetailExcep ( boolean showViewDetailExcep ) {
    this.showViewDetailExcep = showViewDetailExcep;
  }
  
  public boolean isShowConfirmModExcep () {
    return showConfirmModExcep;
  }
  
  public void setShowConfirmModExcep ( boolean showConfirmModExcep ) {
    this.showConfirmModExcep = showConfirmModExcep;
  }
  
  public boolean isShowConfirmDeleteExcep () {
    return showConfirmDeleteExcep;
  }
  
  public void setShowConfirmDeleteExcep ( boolean showConfirmDeleteExcep ) {
    this.showConfirmDeleteExcep = showConfirmDeleteExcep;
  }
  
  public boolean isShowDialogConfirmUpdExcep () {
    return showDialogConfirmUpdExcep;
  }
  
  public void setShowDialogConfirmUpdExcep ( boolean showDialogConfirmUpdExcep ) {
    this.showDialogConfirmUpdExcep = showDialogConfirmUpdExcep;
  }
  
  public boolean isShowConfirmAddExcep () {
    return showConfirmAddExcep;
  }
  
  public void setShowConfirmAddExcep ( boolean showConfirmAddExcep ) {
    this.showConfirmAddExcep = showConfirmAddExcep;
  }
  
  public List< ExceptionWorkdayDTO > getListRegisterException () {
    return listRegisterException;
  }
  
  public void setListRegisterException ( List< ExceptionWorkdayDTO > listRegisterException ) {
    this.listRegisterException = listRegisterException;
  }
  
  public ExceptionWorkdayDTO getAddObjectExcep () {
    addObjectExcep = ( ExceptionWorkdayDTO ) getObjectSession( "addObjectExcep" );
    if ( addObjectExcep == null ) {
      addObjectExcep = new ExceptionWorkdayDTO();
    }
    return addObjectExcep;
  }
  
  public void setAddObjectExcep ( ExceptionWorkdayDTO addObjectExcep ) {
    this.addObjectExcep = addObjectExcep;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< SelectItem > getListJornadaExcep () {
    listJornadaExcep = ( List< SelectItem > ) getObjectSession( "listJornadaExcep" );
    if ( listJornadaExcep == null ) {
      listJornadaExcep = new ArrayList< SelectItem >();
    }
    return listJornadaExcep;
  }
  
  public void setListJornadaExcep ( List< SelectItem > listJornadaExcep ) {
    this.listJornadaExcep = listJornadaExcep;
  }
  
  public boolean isDisableBtnAddException () {
    return disableBtnAddException;
  }
  
  public void setDisableBtnAddException ( boolean disableBtnAddException ) {
    this.disableBtnAddException = disableBtnAddException;
  }
  
  public boolean isShowAlertSelectDateBefore () {
    return showAlertSelectDateBefore;
  }
  
  public void setShowAlertSelectDateBefore ( boolean showAlertSelectDateBefore ) {
    this.showAlertSelectDateBefore = showAlertSelectDateBefore;
  }
  
  public int getNumberNotificatons () {
    return getObjectSession( "numberNotificatons" ) != null ? ( int ) getObjectSession( "numberNotificatons" ) : numberNotificatons;
  }
  
  public void setNumberNotificatons ( int numberNotificatons ) {
    this.numberNotificatons = numberNotificatons;
  }
  
  public List< SelectItem > getListNotification () {
    return listNotification;
  }
  
  public void setListNotification ( List< SelectItem > listNotification ) {
    this.listNotification = listNotification;
  }
  
  public String getNotificationMask () {
    return getObjectSession( "notificationMask" ) != null ? ( String ) getObjectSession( "notificationMask" ) : notificationMask;
  }
  
  public void setNotificationMask ( String notificationMask ) {
    this.notificationMask = notificationMask;
  }
  
  public int getSizeNotificaton () {
    return getObjectSession( "sizeNotificaton" ) != null ? ( int ) getObjectSession( "sizeNotificaton" ) : sizeNotificaton;
  }
  
  public void setSizeNotificaton ( int sizeNotificaton ) {
    this.sizeNotificaton = sizeNotificaton;
  }
  
}
