package co.com.cetus.cetuscontrol.web.delegate;

import java.util.List;

import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.ejb.bean.CetusControlBeanRemote;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.UtilCommon;

public class GeneralDelegate {
  
  private CetusControlBeanRemote beanRemote;
                                 
  private static GeneralDelegate generalDelegate = null;
                                                 
  public static GeneralDelegate getInstance () {
    //    ConstantWEB.WEB_LOG.debug( "######### OBTENIENDO INSTANCIA PARA EL GENERALDELEGATE" );
    if ( generalDelegate == null ) {
      synchronized ( GeneralDelegate.class ) {
        if ( generalDelegate == null ) {
          generalDelegate = new GeneralDelegate();
          ConstantWEB.WEB_LOG.debug( "######### CREADA LA NUEVA INSTANCIA DEL DELEGATE" );
        }
      }
    }
    return generalDelegate;
  }
  
  private GeneralDelegate () {
    initEjb();
  }
  
  private void initEjb () {
    try {
      beanRemote = ( CetusControlBeanRemote ) UtilCommon.getLookupSecurity( ConstantWEB.CONTEXT_PROPERTIE,
                                                                            ConstantWEB.JNDI_PROPERTIE,
                                                                            ConstantWEB.USER_PROPERTIE,
                                                                            ConstantWEB.PASSWORD_PROPERTIE );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Buscar el usuario para dar permisos a la aplicacion origen </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param userCetus the user cetus
   * @return el response dto
   * @since CetusControlWEB (11/12/2014)
   */
  public ResponseDTO searchUserLogged ( String userCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.searchUserLogged( userCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  /**
   * </p> Metodo para consultar registros mediante un campo y tipo de ordenamiento. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param classDTO the class dto
   * @param fieldOrder the field order
   * @param typeOrder the type order
   * @return el response dto
   * @since CetusControlWEB (23/01/2015)
   */
  public ResponseDTO findAll ( String classDTO, String fieldOrder, String typeOrder ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findAll( classDTO, fieldOrder, typeOrder );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  /**
   * </p> Metodo para crear un registro en base de datos mediante el Objeto enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param dto the dto
   * @return el response dto
   * @since CetusControlWEB (23/01/2015)
   */
  public ResponseDTO create ( Object dto ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.create( dto );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  /**
   * </p> Metodo para eliminar un registro en base de datos mediante el Objeto enviado. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param dto the dto
   * @return el response dto
   * @since CetusControlWEB (23/01/2015)
   */
  public ResponseDTO remove ( Object dto ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.remove( dto );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  /**
   * </p> Metodo para actualizar la informacion de un registro en base de datos mediante el objeto enviado </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param dto the dto
   * @return el response dto
   * @since CetusControlWEB (26/01/2015)
   */
  public ResponseDTO edit ( Object dto ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.edit( dto );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findClientByClientCetus ( int pIdClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findClientByClientCetus( pIdClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findPersonByClientCetus ( int pIdClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findPersonByClientCetus( pIdClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findRolByApplication ( int idApp ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findRolByApplication( idApp );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findStatus ( int idclientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findStatus( idclientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskType ( int idclientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskType( idclientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  /**
   * </p> Find parameter general by client cetus. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @return el response dto
   * @since CetusControlWEB (5/03/2015)
   */
  public ResponseDTO findParameterGeneralByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findParameterGeneralByClientCetus( idClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findWorkDayByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findWorkDayByClientCetus( idClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO createUserCetus ( UserPortalDTO pUserPortalDTO, List< String > selectedOptionsRol ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.createUserCetus( pUserPortalDTO, selectedOptionsRol );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO deleteUserCetus ( UserPortalDTO request ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.deleteUserCetus( request );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO generateCodeClient () {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.generateCodeClient();
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findExcepWorkdayByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findExcepWorkdayByClientCetus( idClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findEmailsFromPerson ( int idclientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findEmailsFromPerson( idclientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findAreaByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findAreaByClientCetus( idClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO generateCodePerson () {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.generateCodePerson();
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findUserByPerson ( int idPerson ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findUserByPerson( idPerson );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findRolByLogin ( String request ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findRolByLogin( request );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO updateUserCetus ( UserPortalDTO pUserPortalDTO, List< String > selectedOptionsRol ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.updateUserCetus( pUserPortalDTO, selectedOptionsRol );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findGroupByClientCetus ( int pIdClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findGroupByClientCetus( pIdClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findPersonByClient ( int pIdClient ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findPersonByClient( pIdClient );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findAllGroupTypeByClientCetus ( int pIdClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findAllGroupTypeByClientCetus( pIdClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public String getValueParameter ( String nameParameter ) {
    try {
      return beanRemote.getValueParameter( nameParameter );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return null;
  }
  
  public ResponseDTO findGroupByClient ( int pIdClient ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findGroupByClient( pIdClient );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findGroupByPerson ( int pIdPerson ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findGroupByPerson( pIdPerson );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByClient ( int pIdClient ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskByClient( pIdClient );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findGroupType ( int idclientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findGroupType( idclientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findPersonByGroup ( int idGroup ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findPersonByGroup( idGroup );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findPriorityByClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findPriorityByClientCetus( idClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findPersonGroupByPerson ( int pIdPerson ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findPersonGroupByPerson( pIdPerson );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByPersonGroup ( int pIdGroup, int pIdPerson ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskByPersonGroup( pIdGroup, pIdPerson );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO generateCodeTask () {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.generateCodeTask();
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO getTemplateTaskMassive ( int idClientCetus, int idClient ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.getTemplateTaskMassive( idClientCetus, idClient );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findPersonGroup ( int idGroup, String personIdentity ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findPersonGroup( idGroup, personIdentity );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByPerson ( int pIdPerson ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskByPerson( pIdPerson );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByPersonPriority ( int pIdPerson, String descripPriority ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskByPersonPriority( pIdPerson, descripPriority );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByPersonCompleted ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskByPersonCompleted( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByPersonNexOvercome ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskByPersonNexOvercome( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByPersonRun ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskByPersonRun( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findTaskByPersonExpired ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findTaskByPersonExpired( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public double getPercentageCurrent ( int idPerson ) {
    Double response = null;
    try {
      response = beanRemote.getPercentageCurrent( idPerson );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return response;
  }
  
  public ResponseDTO findProgressTaskByPerson ( int pIdPerson, int pidClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findProgressTaskByPerson( pIdPerson, pidClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO generateLengthTask ( int idTask ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.generateLengthTask( idTask );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findAttachmentFilesByTaskId ( long pIdTask ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findAttachmentFilesByTaskId( pIdTask );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findNotificationGenClientCetus ( int idClientCetus ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findNotificationGenClientCetus( idClientCetus );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findNotificationByGroup ( int idGroup ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findNotificationByGroup( idGroup );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
  
  public ResponseDTO findNotificationByGroupGen ( int idGroup, int idGeneral ) {
    ResponseDTO responseDTO = null;
    try {
      responseDTO = beanRemote.findNotificationByGroupGen( idGroup, idGeneral );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return responseDTO;
  }
}
