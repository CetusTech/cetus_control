package co.com.cetus.cetuscontrol.web.bean;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import co.com.cetus.cetuscontrol.dto.AreaDTO;
import co.com.cetus.cetuscontrol.dto.GroupTDTO;
import co.com.cetus.cetuscontrol.dto.PersonGroupDTO;
import co.com.cetus.cetuscontrol.dto.PriorityDTO;
import co.com.cetus.cetuscontrol.dto.StatusDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskTypeDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.cetuscontrol.web.util.ETemplateTaskMassive;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.DateUtility;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class TaskMassiveMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (24/06/2015)
 */
@ManagedBean
@RequestScoped
public class TaskMassiveMBean extends GeneralManagedBean {
  
  private static final long          serialVersionUID  = 1L;
  
  private UserPortalDTO              userPortalDTO     = null;
  
  private StreamedContent            fileTemplate      = null;
  private List< TaskTypeDTO >        listTaskType      = null;
  private List< AreaDTO >            listArea          = null;
  private List< PriorityDTO >        listPriority      = null;
  private List< StatusDTO >          listStatus        = null;
  private List< GroupTDTO >          listGroup         = null;
  private HashMap< String, Integer > mapPosCell        = null;
  private int                        contSuccess       = 0;
  private int                        contError         = 0;
  private StreamedContent            fileResultProcess = null;
  private BufferedWriter             writerFile        = null;
  private boolean                    showResultProcess = false;
  
  /**
   * </p> Instancia un nuevo task massive m bean. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (24/06/2015)
   */
  public TaskMassiveMBean () {
  }
  
  /* (non-Javadoc)
   * @see co.com.cetus.cetuscontrol.web.bean.GeneralManagedBean#initElement()
   */
  @Override
  @PostConstruct
  public void initElement () {
    userPortalDTO = getUserDTO();
    if ( userPortalDTO != null ) {
      //      listRegister( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
    } else {
      try {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      } catch ( Exception e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      }
    }
  }
  
  @Override
  public String remove () {
    return null;
  }
  
  @Override
  public String update () {
    return null;
  }
  
  @Override
  public String add () {
    return null;
  }
  
  /**
   * </p> Creates the template task massive. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (25/06/2015)
   */
  public void createTemplateTaskMassive () {
    int idClientCetus = userPortalDTO.getPerson().getClient().getClientCetus().getId();
    int idClient = userPortalDTO.getPerson().getClient().getId();
    ResponseDTO responseDTO = null;
    ByteArrayInputStream byteArrayInputStream = null;
    try {
      
      responseDTO = generalDelegate.getTemplateTaskMassive( idClientCetus, idClient );
      if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
        byteArrayInputStream = new ByteArrayInputStream( ( byte[] ) responseDTO.getObjectResponse() );
        fileTemplate = new DefaultStreamedContent( byteArrayInputStream, "application/xls", ConstantWEB.NAME_TEMPLATE_TASK_MASSIVE );
      } else {
        addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MESSAGE_ERROR_CREATE_TEMPLATE_TM );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
  }
  
  /**
   * </p> Load list task type. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (25/06/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void loadListTaskType ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findTaskType( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listTaskType = ( List< TaskTypeDTO > ) response.getObjectResponse();
      } else {
        this.listTaskType = null;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load list area. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (25/06/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void loadListArea ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findAreaByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listArea = ( List< AreaDTO > ) response.getObjectResponse();
      } else {
        this.listArea = null;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load list priority. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (25/06/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void loadListPriority ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findPriorityByClientCetus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listPriority = ( List< PriorityDTO > ) response.getObjectResponse();
      } else {
        this.listPriority = null;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load list status. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClientCetus the id client cetus
   * @since CetusControlWEB (25/06/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void loadListStatus ( int idClientCetus ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findStatus( idClientCetus );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listStatus = ( List< StatusDTO > ) response.getObjectResponse();
      } else {
        this.listStatus = null;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load list group. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idClient the id client
   * @since CetusControlWEB (25/06/2015)
   */
  @SuppressWarnings ( "unchecked" )
  private void loadListGroup ( int idClient ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findGroupByClient( idClient );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        this.listGroup = ( List< GroupTDTO > ) response.getObjectResponse();
      } else {
        this.listGroup = null;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Handle file upload. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (7/07/2015)
   */
  public void handleFileUpload ( FileUploadEvent event ) {
    HSSFWorkbook workbook = null;
    Row row = null;
    int idClientCetus = 0;
    int idClient = 0;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ByteArrayInputStream byteArrayInputStream = null;
    try {
      writerFile = new BufferedWriter( new OutputStreamWriter( byteArrayOutputStream ) );
      
      writeFile( "******************** INICIO DEL PROCESAMIENTO DEL ARCHIVO [ " + event.getFile().getFileName()
                 + " ] ********************" );
      
      workbook = new HSSFWorkbook( event.getFile().getInputstream() );
      HSSFSheet sheet = workbook.getSheetAt( 0 );
      Iterator< Row > rowIterator = sheet.iterator();
      rowIterator.next();//adelantar una fila
      
      writeFile( "Validar cabecera del archivo" );
      // Validar cabecera del archivo
      if ( validateTemplateHeader( rowIterator.next() ) ) {
        writeFile( "Cabecera valida" );
        
        idClientCetus = userPortalDTO.getPerson().getClient().getClientCetus().getId();
        idClient = userPortalDTO.getPerson().getClient().getId();
        
        loadListTaskType( idClientCetus );
        loadListArea( idClientCetus );
        loadListPriority( idClientCetus );
        loadListStatus( idClientCetus );
        loadListGroup( idClient );
        
        //Iterar entre filas
        while ( rowIterator.hasNext() ) {
          row = rowIterator.next();
          if ( rowProcess( row ) ) {
            contSuccess++;
          } else {
            contError++;
          }
        }
      } else {
        writeFile( "Cabecera invalida" );
      }
      
      addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, MessageFormat.format( ConstantWEB.MESSAGE_SUCCESS_UPLOAD_FILE, event.getFile().getFileName() ) );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
      writeFile( "Error interno. " + e.getMessage() );
    } finally {
      showResultProcess = true;
      addObjectSession( showResultProcess, "showResultProcess" );
      writeFile( "******************** FIN DEL PROCESAMIENTO DEL ARCHIVO [ " + event.getFile().getFileName()
                 + " ] ********************" );
      try {
        writerFile.close();
        byteArrayInputStream = new ByteArrayInputStream( byteArrayOutputStream.toByteArray() );
        addObjectSession( byteArrayInputStream, "byteArrayFileResultProcess" );
      } catch ( IOException e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
        writeFile( "Error interno IOException. " + e.getMessage() );
      }
    }
    
  }
  
  /**
   * </p> Validate template header. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param row the row
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (7/07/2015)
   */
  private boolean validateTemplateHeader ( Row row ) {
    boolean result = false;
    Cell cell = null;
    try {
      if ( row != null ) {
        Iterator< Cell > cellIterator = row.cellIterator();
        mapPosCell = new HashMap< String, Integer >();
        while ( cellIterator.hasNext() ) {
          cell = cellIterator.next();
          writeFile( "Evaluando la columna [" + cell.getStringCellValue() + "] de la cabecera de la plantilla" );
          if ( ETemplateTaskMassive.existsTemplateColumn( cell.getStringCellValue() ) ) {
            result = true;
            writeFile( "Columna [" + cell.getStringCellValue() + "]  correcta. Posicion " + cell.getColumnIndex() );
            mapPosCell.put( cell.getStringCellValue(), cell.getColumnIndex() );
          } else {
            result = false;
            writeFile( "Columna [" + cell.getStringCellValue() + "]  incorrecta." );
            break;
          }
        }
      }
    } catch ( Exception e ) {
      throw e;
    }
    return result;
  }
  
  /**
   * </p> Write file. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param text the text
   * @since CetusControlWEB (8/07/2015)
   */
  private void writeFile ( String text ) {
    try {
      writerFile.write( "\n[" + DateUtility.formatDatePattern( new Date(), ConstantWEB.PATTERN_DATE_WRITE_FILE ) + "] # " + text );
      ConstantWEB.WEB_LOG.info( text );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Row process. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param row the row
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (7/07/2015)
   */
  @SuppressWarnings ( "static-access" )
  private boolean rowProcess ( Row row ) {
    boolean result = false;
    boolean error = false;
    Cell cell = null;
    int id = -1;
    String description = null;
    TaskDTO taskDTO = null;
    String idResponsible = null;
    int idGroup = -1;
    String idGroupStr = null;
    boolean exists = false;
    ResponseDTO responseDTO = null;
    PersonGroupDTO personGroupDTO = null;
    String date = null;
    try {
      if ( row != null && mapPosCell != null ) {
        writeFile( "-------------- row " + row.getRowNum() + " --------------" );
        taskDTO = new TaskDTO();
        Iterator< Cell > cellIterator = row.cellIterator();
        while ( cellIterator.hasNext() ) {
          cell = cellIterator.next();
          
          // Obtener el valor id del tipo de tarea
          if ( mapPosCell.get( ETemplateTaskMassive.TASK_TYPE.getName() ).intValue() == cell.getColumnIndex() ) {
            description = cell.getStringCellValue();
            writeFile( "Obteniendo id del TASK_TYPE [" + description + "]" );
            id = getIdTaskType( description );
            writeFile( "Descripcion= " + description + ", Id= " + id );
            if ( id > 0 ) {
              taskDTO.setTaskType( new TaskTypeDTO() );
              taskDTO.getTaskType().setId( id );
              continue;
            } else {
              writeFile( "Tipo de tarea invalido, termina el procesamiento de esta tarea" );
              error = true;
              break;
            }
          }
          
          // Obtener el valor id del area
          if ( mapPosCell.get( ETemplateTaskMassive.AREA.getName() ).intValue() == cell.getColumnIndex() ) {
            description = cell.getStringCellValue();
            writeFile( "Obteniendo id del AREA [" + description + "]" );
            id = getIdArea( description );
            writeFile( "Descripcion= " + description + ", Id= " + id );
            if ( id > 0 ) {
              taskDTO.setArea( new AreaDTO() );
              taskDTO.getArea().setId( id );
              continue;
            } else {
              writeFile( "Area invalida, termina el procesamiento de esta tarea" );
              error = true;
              break;
            }
          }
          
          // Obtener el valor id de la prioridad
          if ( mapPosCell.get( ETemplateTaskMassive.PRIORITY.getName() ).intValue() == cell.getColumnIndex() ) {
            description = cell.getStringCellValue();
            writeFile( "Obteniendo id del PRIORITY [" + description + "]" );
            id = getIdPriority( description );
            writeFile( "Descripcion= " + description + ", Id= " + id );
            if ( id > 0 ) {
              taskDTO.setPriority( new PriorityDTO() );
              taskDTO.getPriority().setId( id );
              continue;
            } else {
              writeFile( "Prioridad invalida, termina el procesamiento de esta tarea" );
              error = true;
              break;
            }
          }
          
          // Obtener el valor id del estado
          if ( mapPosCell.get( ETemplateTaskMassive.STATUS.getName() ).intValue() == cell.getColumnIndex() ) {
            description = cell.getStringCellValue();
            writeFile( "Obteniendo id del STATUS [" + description + "]" );
            id = getIdStatus( description );
            writeFile( "Descripcion= " + description + ", Id= " + id );
            if ( id > 0 ) {
              taskDTO.setStatus( new StatusDTO() );
              taskDTO.getStatus().setId( id );
              continue;
            } else {
              writeFile( "Estado invalido, termina el procesamiento de esta tarea" );
              error = true;
              break;
            }
          }
          
          // Obtener el valor de la identificacion del responsable
          if ( mapPosCell.get( ETemplateTaskMassive.ID_RESPONSIBLE.getName() ).intValue() == cell.getColumnIndex() ) {
            if ( cell.getCellType() == cell.CELL_TYPE_NUMERIC ) {
              idResponsible = String.valueOf( ( new BigDecimal( cell.getNumericCellValue() ).longValue() ) );
            } else {
              idResponsible = cell.getStringCellValue();
            }
            writeFile( "Id del responsable [" + idResponsible + "]" );
            if ( UtilCommon.stringNullOrEmpty( idResponsible ) ) {
              writeFile( "Id del responsable nulo o vacio, termina el procesamiento de esta tarea" );
              error = true;
              break;
            } else {
              continue;
            }
          }
          
          // Obtener el valor de la identificacion del grupo
          if ( mapPosCell.get( ETemplateTaskMassive.ID_GROUP.getName() ).intValue() == cell.getColumnIndex() ) {
            if ( cell.getCellType() == cell.CELL_TYPE_NUMERIC ) {
              idGroupStr = String.valueOf( ( new BigDecimal( cell.getNumericCellValue() ).longValue() ) );
            } else {
              idGroupStr = cell.getStringCellValue();
            }
            writeFile( "Id del grupo [" + idGroupStr + "]" );
            if ( UtilCommon.stringNullOrEmpty( idGroupStr ) ) {
              writeFile( "Id del grupo nulo o vacio, termina el procesamiento de esta tarea" );
              error = true;
              break;
            } else {
              writeFile( "Validando la existencia del grupo para el cliente" );
              idGroup = Integer.valueOf( idGroupStr );
              for ( GroupTDTO groupTDTO: listGroup ) {
                if ( groupTDTO.getId() == idGroup ) {
                  exists = true;
                  break;
                }
              }
              if ( !exists ) {
                writeFile( "Id del grupo no existe en el cliente, termina el procesamiento de esta tarea" );
                error = true;
                break;
              } else {
                writeFile( "Se procede a validar relacion del id responsable con el grupo" );
                responseDTO = generalDelegate.findPersonGroup( idGroup, idResponsible );
                if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                  personGroupDTO = ( PersonGroupDTO ) responseDTO.getObjectResponse();
                  if ( personGroupDTO != null && personGroupDTO.getId() > 0 ) {
                    writeFile( "si existe relacion del id responsable con el grupo" );
                    taskDTO.setPersonGroup( new PersonGroupDTO() );
                    taskDTO.getPersonGroup().setId( personGroupDTO.getId() );
                    continue;
                  } else {
                    writeFile( "No se encontro relacion del id responsable con el grupo, termina el procesamiento de esta tarea" );
                    error = true;
                    break;
                  }
                } else if ( UtilCommon.validateResponseNoResult( responseDTO ) ) {
                  writeFile( "No se encontro resultado de la relacion del id responsable con el grupo, termina el procesamiento de esta tarea" );
                  error = true;
                  break;
                }
              }
            }
          }
          
          // Obtener el valor de la descripcion de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.DESCRIPTION.getName() ).intValue() == cell.getColumnIndex() ) {
            description = cell.getStringCellValue();
            writeFile( "Descripcion de la tarea [" + description + "]" );
            if ( UtilCommon.stringNullOrEmpty( description ) ) {
              writeFile( "Descripcion de la tarea nulo o vacio, termina el procesamiento de esta tarea" );
              error = true;
              break;
            } else {
              taskDTO.setDescription( description );
              continue;
            }
          }
          
          // Obtener el valor de la observacion de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.OBSERVATION.getName() ).intValue() == cell.getColumnIndex() ) {
            writeFile( "Observacion de la tarea [" + cell.getStringCellValue() + "]" );
            taskDTO.setObservation( cell.getStringCellValue() );
            continue;
          }
          
          // Obtener el valor del solicitante de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.REQUESTOR.getName() ).intValue() == cell.getColumnIndex() ) {
            writeFile( "Solicitante de la tarea [" + cell.getStringCellValue() + "]" );
            taskDTO.setRequestor( cell.getStringCellValue() );
            continue;
          }
          
          // Obtener el valor de la observacion del solicitante de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.OBSERVATION_REQUESTOR.getName() ).intValue() == cell.getColumnIndex() ) {
            writeFile( "Observacion del solicitante de la tarea [" + cell.getStringCellValue() + "]" );
            taskDTO.setObservationRequestor( cell.getStringCellValue() );
            continue;
          }
          
          // Obtener el valor del usuario funcional de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.USER_FUNCTIONAL.getName() ).intValue() == cell.getColumnIndex() ) {
            writeFile( "Usuario funcional de la tarea [" + cell.getStringCellValue() + "]" );
            taskDTO.setUserFunctional( cell.getStringCellValue() );
            continue;
          }
          
          // Obtener el valor de la fecha de asignacion de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.ASSING_DATE.getName() ).intValue() == cell.getColumnIndex() ) {
            if ( cell.getCellType() == cell.CELL_TYPE_NUMERIC ) {
              writeFile( "Fecha de asignacion tipo CELL_TYPE_NUMERIC" );
              try {
                Date assigDate = cell.getDateCellValue();
                if ( assigDate != null ) {
                  taskDTO.setAssingDate( assigDate );
                  continue;
                } else {
                  writeFile( "Fecha de asignacion de la tarea nulo o vacio, termina el procesamiento de esta tarea" );
                  error = true;
                  break;
                }
              } catch ( Exception e ) {
                writeFile( "Fecha de asignacion invalida, termina el procesamiento de esta tarea" );
                throw e;
              }
            } else if ( cell.getCellType() == cell.CELL_TYPE_STRING ) {
              writeFile( "Fecha de asignacion tipo CELL_TYPE_STRING" );
              date = cell.getStringCellValue();
              writeFile( "Fecha de asignacion de la tarea [" + date + "]" );
              if ( !UtilCommon.stringNullOrEmpty( date ) ) {
                try {
                  taskDTO.setAssingDate( DateUtility.parseDatePattern( date, ConstantWEB.PATTERN_DATE_TEMPLATE_XLS ) );
                  continue;
                } catch ( Exception e ) {
                  writeFile( "Fecha de asignacion invalida, termina el procesamiento de esta tarea" );
                  throw e;
                }
                
              }
            }
          }
          
          // Obtener el valor de la duracion de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.DURATION.getName() ).intValue() == cell.getColumnIndex() ) {
            if ( cell.getCellType() == cell.CELL_TYPE_NUMERIC ) {
              taskDTO.setVDuration( Integer.parseInt( String.valueOf( cell.getNumericCellValue() ) ) );
            } else {
              taskDTO.setVDuration( Integer.parseInt( cell.getStringCellValue() ) );
            }
            continue;
          }
          
          // Obtener el valor de la fecha fin de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.DATE_END.getName() ).intValue() == cell.getColumnIndex() ) {
            if ( cell.getCellType() == cell.CELL_TYPE_NUMERIC ) {
              writeFile( "Fecha fin tipo CELL_TYPE_NUMERIC" );
              try {
                Date assigDate = cell.getDateCellValue();
                if ( assigDate != null ) {
                  taskDTO.setDateEnd( assigDate );
                } else {
                  writeFile( "Fecha fin de la tarea nulo o vacio" );
                }
              } catch ( Exception e ) {
                writeFile( "Fecha fin invalida" );
              }
            } else if ( cell.getCellType() == cell.CELL_TYPE_STRING ) {
              writeFile( "Fecha fin tipo CELL_TYPE_STRING" );
              date = cell.getStringCellValue();
              writeFile( "Fecha fin de la tarea [" + date + "]" );
              if ( !UtilCommon.stringNullOrEmpty( date ) ) {
                try {
                  taskDTO.setDateEnd( DateUtility.parseDatePattern( date, ConstantWEB.PATTERN_DATE_TEMPLATE_XLS ) );
                } catch ( Exception e ) {
                  writeFile( "Fecha fin invalida" );
                }
              }
            }
            continue;
          }
          
          // Obtener el valor de la duracion de la tarea
          if ( mapPosCell.get( ETemplateTaskMassive.APPROVED.getName() ).intValue() == cell.getColumnIndex() ) {
            try {
              Object approved = null;
              if ( cell.getCellType() == cell.CELL_TYPE_NUMERIC ) {
                approved = cell.getNumericCellValue();
              } else {
                approved = cell.getStringCellValue();
              }
              writeFile( "Columna Aprobado de la tarea [" + approved.toString() + "]" );
              
              if ( approved != null && ( ( Integer ) approved == 0 || ( Integer ) approved == 1 ) ) {
                taskDTO.setApproved( ( Integer ) approved );
              } else {
                writeFile( "Columna Aprobado con datos invalidos" );
              }
            } catch ( Exception e ) {
              writeFile( "Columna Aprobado invalida" );
            }
            continue;
          }
        }
        
        if ( !error ) {
          responseDTO = generalDelegate.generateCodeTask();
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            taskDTO.setCode( ( String ) responseDTO.getObjectResponse() );
            taskDTO.setCreationUser( getUserInSession() );
            taskDTO.setCreationDate( currentDate );
            
            writeFile( "Se procede a crear la tarea en BD " );
            responseDTO = generalDelegate.create( taskDTO );
            writeFile( "Respuesta de la creacion de la tarea en BD ::>" + responseDTO.toString() );
            
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              writeFile( "Tarea creada exitosamente..." );
              result = true;
            } else {
              writeFile( "Error creando la tarea. " + responseDTO.getMessage() );
            }
          } else {
            writeFile( "Error obteniendo el codigo de la tarea, termina el procesamiento de esta tarea" );
          }
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      writeFile( "Error interno procesando la fila " + row.getRowNum() + ". " + e.getMessage() );
    }
    return result;
  }
  
  /**
   * </p> Gets the id task type. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param description the description
   * @return el long
   * @since CetusControlWEB (7/07/2015)
   */
  private int getIdTaskType ( String description ) {
    int id = -1;
    try {
      if ( description != null && listTaskType != null ) {
        for ( TaskTypeDTO taskTypeDTO: listTaskType ) {
          if ( taskTypeDTO.getDescription() != null && taskTypeDTO.getDescription().equals( description ) ) {
            id = taskTypeDTO.getId();
            break;
          }
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      writeFile( "Error obteniendo el id del tipo de tarea " + description );
    }
    return id;
  }
  
  /**
   * </p> Gets the id area. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param description the description
   * @return el long
   * @since CetusControlWEB (7/07/2015)
   */
  private int getIdArea ( String description ) {
    int id = -1;
    try {
      if ( description != null && listArea != null ) {
        for ( AreaDTO areaDTO: listArea ) {
          if ( areaDTO.getDescription() != null && areaDTO.getDescription().equals( description ) ) {
            id = areaDTO.getId();
            break;
          }
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      writeFile( "Error obteniendo el id del tipo de tarea " + description );
    }
    return id;
  }
  
  /**
   * </p> Gets the id priority. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param description the description
   * @return el long
   * @since CetusControlWEB (7/07/2015)
   */
  private int getIdPriority ( String description ) {
    int id = -1;
    try {
      if ( description != null && listArea != null ) {
        for ( PriorityDTO priorityDTO: listPriority ) {
          if ( priorityDTO.getDescription() != null && priorityDTO.getDescription().equals( description ) ) {
            id = priorityDTO.getId();
            break;
          }
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      writeFile( "Error obteniendo el id del tipo de tarea " + description );
    }
    return id;
  }
  
  /**
   * </p> Gets the id status. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param description the description
   * @return el long
   * @since CetusControlWEB (7/07/2015)
   */
  private int getIdStatus ( String description ) {
    int id = -1;
    try {
      if ( description != null && listStatus != null ) {
        for ( StatusDTO statusDTO: listStatus ) {
          if ( statusDTO.getDescription() != null && statusDTO.getDescription().equals( description ) ) {
            id = statusDTO.getId();
            break;
          }
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      writeFile( "Error obteniendo el id del tipo de tarea " + description );
    }
    return id;
  }
  
  /**
   * </p> Download result process. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (8/07/2015)
   */
  public void downloadResultProcess () {
    ByteArrayInputStream byteArrayFileResultProcess = null;
    try {
      byteArrayFileResultProcess = ( ByteArrayInputStream ) getObjectSession( "byteArrayFileResultProcess" );
      
      fileResultProcess = new DefaultStreamedContent( byteArrayFileResultProcess, "text/plain", ConstantWEB.NAME_FILE_RESULT_PROCESS + ".txt" );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
  }
  
  public StreamedContent getFileTemplate () {
    return fileTemplate;
  }
  
  public void setFileTemplate ( StreamedContent fileTemplate ) {
    this.fileTemplate = fileTemplate;
  }
  
  public int getContSuccess () {
    return contSuccess;
  }
  
  public void setContSuccess ( int contSuccess ) {
    this.contSuccess = contSuccess;
  }
  
  public int getContError () {
    return contError;
  }
  
  public void setContError ( int contError ) {
    this.contError = contError;
  }
  
  public StreamedContent getFileResultProcess () {
    return fileResultProcess;
  }
  
  public void setFileResultProcess ( StreamedContent fileResultProcess ) {
    this.fileResultProcess = fileResultProcess;
  }
  
  public boolean isShowResultProcess () {
    if ( ( Boolean ) getObjectSession( "showResultProcess" ) != null ) {
      showResultProcess = ( Boolean ) getObjectSession( "showResultProcess" );
    }
    return showResultProcess;
  }
  
  public void setShowResultProcess ( boolean showResultProcess ) {
    this.showResultProcess = showResultProcess;
  }
  
}
