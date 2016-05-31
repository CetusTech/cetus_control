package co.com.cetus.cetuscontrol.web.bean;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import co.com.cetus.cetuscontrol.dto.AreaDTO;
import co.com.cetus.cetuscontrol.dto.AttachDTO;
import co.com.cetus.cetuscontrol.dto.PriorityDTO;
import co.com.cetus.cetuscontrol.dto.StatusDTO;
import co.com.cetus.cetuscontrol.dto.TaskDTO;
import co.com.cetus.cetuscontrol.dto.TaskHistoryDTO;
import co.com.cetus.cetuscontrol.dto.TaskTypeDTO;
import co.com.cetus.cetuscontrol.dto.UserPortalDTO;
import co.com.cetus.cetuscontrol.web.bean.ManualTaskMBean.ColumnModel;
import co.com.cetus.cetuscontrol.web.util.ConstantWEB;
import co.com.cetus.common.dto.ResponseDTO;
import co.com.cetus.common.util.DateUtility;
import co.com.cetus.common.util.UtilCommon;

/**
 * The Class SearchTaskMBean.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (14/05/2016)
 */
@ManagedBean
@RequestScoped
public class SearchTaskMBean extends GeneralManagedBean {
  
  /** The Constant serialVersionUID. */
  private static final long         serialVersionUID           = -3255236201554716426L;
                                                               
  private List< TaskDTO >           listTask                   = null;
  private UserPortalDTO             userPortalDTO              = null;
  private TaskDTO                   selectedObject             = null;
  private boolean                   showViewDetail             = false;
  private boolean                   showMenu                   = false;
  private String                    destination                = ConstantWEB.PATH_FILE_TASK;
  private String                    separador                  = System.getProperty( "file.separator" );
  private String                    idUsuario                  = null;
  private int                       indexTab                   = 0;
  private List< AttachDTO >         listAttachFiles            = null;
  private List< TaskDTO >           listTaskHystory            = null;
  private HashMap< String, String > mapColumnsHeaderHistory    = null;
  private List< String >            listColumnLabelTaskHistory = null;
  private String[]                  columnSelectedHistory      = null;
  private AttachDTO                 attachDTOSelected          = null;
  private List< ColumnModel >       columnsHistory             = null;
  private DefaultStreamedContent    download                   = null;
  ArrayList< UploadedFile >         listFilesTask              = null;
  private UploadedFile              fileSelected               = null;
  private boolean                   showDialogConfirmSave      = false;
  private StreamedContent           fileTemplate               = null;
                                                               
  public SearchTaskMBean () {
  
  }
  
  @SuppressWarnings ( "unchecked" )
  @Override
  @PostConstruct
  public void initElement () {
    ResponseDTO responseDTO = null;
    try {
      userPortalDTO = getUserDTO();
      if ( userPortalDTO != null ) {
        idUsuario = String.valueOf( userPortalDTO.getPerson().getClient().getClientCetus().getId() );
        listTask = ( List< TaskDTO > ) getObjectSession( "listTask" );
        if ( listTask == null || listTask.size() == 0 ) {
          responseDTO = generalDelegate.findTaskByFilter( userPortalDTO.getPerson().getClient().getId(),
                                                          ( String ) getObjectSession( ConstantWEB.DESC_FILTER_SEARCH ),
                                                          ( String ) getObjectSession( ConstantWEB.DESC_INPUT_SEARCH ) );
                                                          
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            listTask = ( List< TaskDTO > ) responseDTO.getObjectResponse();
          } else {
            listTask = new ArrayList< >();
          }
          addObjectSession( listTask, "listTask" );
        }
      } else {
        getResponse().sendRedirect( getRequest().getContextPath() + ConstantWEB.URL_PAGE_USER_NOVALID );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @Override
  public String remove () {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String update () {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String add () {
    // TODO Auto-generated method stub
    return null;
  }
  
  /**
   * </p> On row select task. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (14/05/2016)
   */
  public void onRowSelectTask ( SelectEvent event ) {
    try {
      selectedObject = ( ( TaskDTO ) event.getObject() );
      if ( selectedObject != null ) {
        if ( ( selectedObject.getStatus().getId() == ConstantWEB.STATUS_COMPLETED_VAL )
             || selectedObject.getStatus().getId() == ConstantWEB.STATUS_CANCELED_VAL ) {
          showMenu = true;
        }
        showViewDetail = true;
        
        //validar si tiene archivos adjuntos, consultarlos y traerlos
        if ( selectedObject.getId() > 0 ) {
          loadFiles( selectedObject.getId() );
        }
        findTaskHistory( selectedObject.getId() );
        addObjectSession( selectedObject, "selectedObject" );
      } else {
        cleanObjectSession( "selectedObject" );
        cleanObjectSession( "listTaskHystory" );
        cleanObjectSession( "listAttachFiles" );
      }
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  /**
   * </p> Load files. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idTask the id task
   * @since CetusControlWEB (29/05/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void loadFiles ( int idTask ) {
    ResponseDTO response = null;
    try {
      response = generalDelegate.findAttachmentFilesByTaskId( selectedObject.getId() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        //Tiene archivos la tarea 
        listAttachFiles = ( ( List< AttachDTO > ) response.getObjectResponse() );
      } else {
        listAttachFiles = new ArrayList< AttachDTO >();
      }
      addObjectSession( listAttachFiles, "listAttachFiles" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Find task history. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param idTask the id task
   * @since CetusControlWEB (12/04/2016)
   */
  @SuppressWarnings ( "unchecked" )
  private void findTaskHistory ( int idTask ) {
    List< TaskHistoryDTO > list = new ArrayList< TaskHistoryDTO >();
    ResponseDTO responseDTO = null;
    TaskDTO taskDTO = null;
    try {
      listTaskHystory = new ArrayList< TaskDTO >();
      loadColumnTaskHystory();
      responseDTO = generalDelegate.findTaskHistory( idTask );
      
      if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
        list = ( List< TaskHistoryDTO > ) responseDTO.getObjectResponse();
        for ( TaskHistoryDTO taskHistoryDTO: list ) {
          taskDTO = ( TaskDTO ) UtilCommon.fromGson( TaskDTO.class, taskHistoryDTO.getTaskObject() );
          listTaskHystory.add( taskDTO );
        }
      }
      
      addObjectSession( listTaskHystory, "listTaskHystory" );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Load column task hystory. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (8/04/2016)
   */
  private void loadColumnTaskHystory () {
    try {
      mapColumnsHeaderHistory = new HashMap< String, String >();
      String[] colLabel = ConstantWEB.COLUMN_LABEL_TASK_HISTORY;
      String[] column = ConstantWEB.COLUMN_TASK_HISTORY;
      for ( int i = 0; i < colLabel.length; i++ ) {
        mapColumnsHeaderHistory.put( colLabel[i], column[i] );
      }
      
      listColumnLabelTaskHistory = Arrays.asList( colLabel );
      columnSelectedHistory = ConstantWEB.COLUMN_SELECTED_TASK_HISTORY;
      
      addObjectSession( listColumnLabelTaskHistory, "listColumnLabelTaskHistory" );
      addObjectSession( mapColumnsHeaderHistory, "mapColumnsHeaderHistory" );
      addObjectSession( columnSelectedHistory, "columnSelectedHistory" );
      
      createDynamicColumnsHistory();
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  /**
   * </p> Close dialog view. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (24/05/2016)
   */
  public void closeDialogView () {
    selectedObject = new TaskDTO();
    selectedObject.setArea( new AreaDTO() );
    selectedObject.setPriority( new PriorityDTO() );
    selectedObject.setStatus( new StatusDTO() );
    selectedObject.setTaskType( new TaskTypeDTO() );
    removerFileUploads();
  }
  
  /**
   * </p> Remover file uploads. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @since CetusControlWEB (2/02/2016)
   */
  private void removerFileUploads () {
    cleanObjectSession( "listFilesTask" );
    if ( destination != null && idUsuario != null ) {
      
      File archivos = new File( destination + separador + idUsuario );
      if ( archivos != null ) {
        File[] lista = archivos.listFiles();
        if ( archivos.listFiles() != null && archivos.listFiles().length > 0 ) {
          for ( File file: lista ) {
            if ( !file.isDirectory() ) {
              file.delete();
            }
          }
        }
      }
    }
    
  }
  
  @SuppressWarnings ( "unchecked" )
  public void removeAttachInTask () {
    ResponseDTO responseDTO = null;
    attachDTOSelected = ( AttachDTO ) getObjectSession( "attachDTOSelected" );
    if ( attachDTOSelected != null ) {
      //Obtener PATH para abrir el archivo
      indexTab = 1;
      File archivos = new File( attachDTOSelected.getPath() + separador + attachDTOSelected.getFileName() );
      if ( archivos != null ) {
        if ( archivos.isFile() ) {
          if ( archivos.delete() ) {
            responseDTO = generalDelegate.remove( attachDTOSelected );
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
              responseDTO = generalDelegate.findAttachmentFilesByTaskId( selectedObject.getId() );
              if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                //Tiene archivos la tarea 
                listAttachFiles = ( ( List< AttachDTO > ) responseDTO.getObjectResponse() );
              } else {
                listAttachFiles = new ArrayList< AttachDTO >();
              }
              addObjectSession( listAttachFiles, "listAttachFiles" );
            }
          } else {
            indexTab = 1;
            addMessageWarning( null, MessageFormat.format( ConstantWEB.MESSAGE_ERROR_DELETE, "Archivo" ), null );
          }
        } else {
          responseDTO = generalDelegate.remove( attachDTOSelected );
          if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
            selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
            responseDTO = generalDelegate.findAttachmentFilesByTaskId( selectedObject.getId() );
            if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
              //Tiene archivos la tarea 
              listAttachFiles = ( ( List< AttachDTO > ) responseDTO.getObjectResponse() );
            } else {
              listAttachFiles = new ArrayList< AttachDTO >();
            }
            addObjectSession( listAttachFiles, "listAttachFiles" );
          }
        }
      }
      
    } else {
      indexTab = 1;
      addMessageWarning( "msgDel", MessageFormat.format( ConstantWEB.MESSAGE_ERROR_DELETE, "Archivo" ), null );
    }
    addObjectSession( indexTab, "indexTab" );
    selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
    cleanObjectSession( "attachDTOSelected" );
  }
  
  /**
   * </p> Creates the dynamic columns history. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @since CetusControlWEB (8/04/2016)
   */
  @SuppressWarnings ( "unchecked" )
  public void createDynamicColumnsHistory () {
    try {
      columnsHistory = new ArrayList< ColumnModel >();
      mapColumnsHeaderHistory = ( HashMap< String, String > ) getObjectSession( "mapColumnsHeaderHistory" );
      for ( String columnKey: columnSelectedHistory ) {
        if ( mapColumnsHeaderHistory.containsKey( columnKey ) ) {
          columnsHistory.add( new ColumnModel( columnKey, mapColumnsHeaderHistory.get( columnKey ) ) );
        }
      }
      addObjectSession( columnsHistory, "columnsHistory" );
      
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  public void onRowSelectAttachment ( SelectEvent event ) {
    try {
      attachDTOSelected = ( ( AttachDTO ) event.getObject() );
      addObjectSession( attachDTOSelected, "attachDTOSelected" );
      indexTab = 1;
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      addObjectSession( indexTab, "indexTab" );
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  public void prepDownload ( AttachDTO in ) throws Exception {
    File file = new File( in.getPath() + separador + in.getFileName() );
    InputStream input = new FileInputStream( file );
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    setDownload( new DefaultStreamedContent( input, externalContext.getMimeType( file.getName() ), file.getName() ) );
  }
  
  /**
   * </p> Metodo llamado al cargar un archivo asociado a la tarea. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param event the event
   * @since CetusControlWEB (2/02/2016)
   */
  public void handleFileUpload ( FileUploadEvent event ) {
    boolean flag = false;
    try {
      if ( event != null ) {
        if ( getObjectSession( "listFilesTask" ) == null ) {
          listFilesTask = new ArrayList< >();
          listFilesTask.add( event.getFile() );
          //copiar archivo a la ruta del usuario como archivo temporal
          copyFileTmp( event.getFile().getFileName(), event.getFile().getInputstream() );
          addObjectSession( listFilesTask, "listFilesTask" );
          //addMessageInfo( null, ConstantWEB.MESSAGE_SUCCES, event.getFile().getFileName() );
        } else {
          
          for ( UploadedFile file: listFilesTask ) {
            if ( file.getFileName() != null ) {
              if ( file.getFileName().equals( event.getFile().getFileName() ) ) {
                addMessageError( "msgDel", ConstantWEB.MESSAGE_ERROR, "El archivo ya se encuentra agregado" );
                flag = true;
                break;
              }
            }
          }
          if ( !flag ) {
            listFilesTask.add( event.getFile() );
            //copiar archivo a la ruta del usuario como archivo temporal
            copyFileTmp( event.getFile().getFileName(), event.getFile().getInputstream() );
            addObjectSession( listFilesTask, "listFilesTask" );
          }
          
        }
        
      }
    } catch ( IOException e ) {
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getCause().getMessage() );
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  public void removeFileSelected ( UploadedFile file ) {
    if ( file != null ) {
      listFilesTask = ( ArrayList< UploadedFile > ) getObjectSession( "listFilesTask" );
      if ( listFilesTask != null ) {
        listFilesTask.remove( file );
        addObjectSession( listFilesTask, "listFilesTask" );
      }
    }
  }
  
  /**
   * </p> Copy file. </p>
   *
   * @author Andres Herrera - Cetus Technology
   * @param pFileName the file name
   * @param in the in
   * @throws IOException 
   * @since CetusControlWEB (2/02/2016)
   */
  public void copyFileTmp ( String pFileName, InputStream in ) throws IOException {
    File userUploads = null;
    int j = 0;
    File tmp = null;
    OutputStream out = null;
    int read = 0;
    byte[] bytes = new byte[1024];
    try {
      userUploads = new File( destination, String.valueOf( idUsuario ) );
      userUploads.mkdir();//crear el directorio del usuario que esta logueado
      if ( pFileName != null ) {
        j = pFileName.lastIndexOf( "." );
        //Crear un archivo temporal que sera borrado cuando se termine la operacion
        //Se le agrega un numeral para diferenciar de los numeros que generar java temporalmente asociados al archivo
        tmp = File.createTempFile( pFileName.substring( 0, j ) + "#", "." + pFileName.substring( j, pFileName.length() ), userUploads );
        tmp.deleteOnExit();//Indicar que el archivo sera borrado
        out = new FileOutputStream( tmp );
        while ( ( read = in.read( bytes ) ) != -1 ) {
          out.write( bytes, 0, read );
        }
        in.close();
        out.flush();
        out.close();
        System.out.println( "New file created!" );
      }
    } catch ( IOException e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      throw e;
    } finally {
      try {
        in.close();
        out.flush();
        out.close();
      } catch ( IOException e ) {
        ConstantWEB.WEB_LOG.error( e.getMessage(), e );
        throw e;
      }
      
    }
  }
  
  /**
   * </p> Gets the value column. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param value the value
   * @return el string
   * @since CetusControlWEB (9/04/2016)
   */
  public String getValueColumn ( Object value ) {
    String result = "";
    try {
      if ( value instanceof Date ) {
        result = DateUtility.formatDatePattern( ( Date ) value, getPatterDate() );
      } else {
        result = String.valueOf( value != null ? value : "" );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return result;
  }
  
  public void loadSave ( ActionEvent event ) {
    try {
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        addObjectSession( selectedObject, "selectedObject" );
        this.showDialogConfirmSave = true;
      } else {
        this.showDialogConfirmSave = false;
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
  }
  
  @SuppressWarnings ( "unchecked" )
  public String save () {
    File directoryTmp = null;
    File[] contents = null;
    String path;
    File userUploads = null;
    AttachDTO attachDTO = null;
    ResponseDTO responseDTO = null;
    try {
      this.showDialogConfirmSave = false;
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      if ( selectedObject != null ) {
        //Tiene archivos cargados en memoria
        listFilesTask = ( ArrayList< UploadedFile > ) getObjectSession( "listFilesTask" );
        if ( listFilesTask != null && listFilesTask.size() > 0 ) {
          //Tiene archivos para agregar! 
          ConstantWEB.WEB_LOG.debug( "ARCHIVOS PARA CARGAR " + listFilesTask.size() );
          path = destination + separador + idUsuario;
          directoryTmp = new File( path );
          for ( UploadedFile att: listFilesTask ) {
            if ( directoryTmp != null ) {
              contents = directoryTmp.listFiles();
              interno: for ( File file: contents ) {
                if ( !file.isDirectory() && file.getName().substring( 0, file.getName().lastIndexOf( "#" ) )
                                                .equals( att.getFileName().substring( 0, att.getFileName().lastIndexOf( "." ) ) ) ) {
                  attachDTO = new AttachDTO();
                  attachDTO.setTask( selectedObject );
                  attachDTO.setFileName( att.getFileName() );
                  userUploads = new File( path, String.valueOf( attachDTO.getTask().getCode() ) );
                  attachDTO.setPath( userUploads.getPath() );
                  attachDTO.setCreationDate( currentDate );
                  attachDTO.setCreationUser( getUserInSession() );
                  userUploads.mkdir();
                  //Crear el registro del archivo adjunto a la tarea 
                  responseDTO = generalDelegate.create( attachDTO );
                  if ( UtilCommon.validateResponseSuccess( responseDTO ) ) {
                    //Renombrar el archivo y mover a la carpeta asociada al ID de la tarea
                    if ( renameAndMoveFileTmp( att.getFileName(), file.getName(), userUploads.getPath() + separador, path ) ) {
                      //Archivo se movio correctamente
                      ConstantWEB.WEB_LOG.debug( "ARCHIVO MOVIDO CORRECTAMENTE " + att.getFileName() );
                    } else {
                      //Archivo presento problemas al moverlo, se debe validar porque debio ser borrado por ser temporal
                      ConstantWEB.WEB_LOG.debug( "ERROR MOVIENDO EL ARCHIVO " + att.getFileName() );
                    }
                  }
                  break interno;
                }
              }
            }
          }
          loadFiles(selectedObject.getId());
          cleanObjectSession( "listFilesTask" );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_DETAIL_ERROR );
    }
    return null;
  }
  
  /**
   *   
   * @author Andres Herrera - Cetus Technology
   * @param pFileNameReal Nombre real del archivo cargado
   * @param pFileNameTmp Nombre del archivo con extension de temporales
   * @param pTargetDir Directorio destino donde se almacenara el archivo
   * @param pPathTmp Directorio temporal donde estan los archivos cargados 
   * @return true, si el proceso fue exitoso
   * @since CetusControlWEB (2/02/2016)
   */
  public boolean renameAndMoveFileTmp ( String pFileNameReal, String pFileNameTmp, String pTargetDir, String pPathTmp ) {
    File filesTmp = null;//Archivo Temporal existente en la ruta especificada
    File dir = null;
    String newName = null;
    try {
      if ( pPathTmp != null && pFileNameTmp != null ) {
        filesTmp = new File( pPathTmp + separador, pFileNameTmp );
        dir = new File( pTargetDir );
        int j = pFileNameReal.lastIndexOf( "." );
        if ( pFileNameReal != null && !pFileNameReal.isEmpty() ) {
          newName = pFileNameReal.substring( 0, j ) + pFileNameReal.substring( j, pFileNameReal.length() );
          return filesTmp.renameTo( new File( dir, newName ) );
        }
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
    }
    return false;
  }
  
  public void generateReportViewTask () {
    ResponseDTO response = null;
    ByteArrayInputStream byteArrayInputStream = null;
    try {
      selectedObject = ( TaskDTO ) getObjectSession( "selectedObject" );
      response = generalDelegate.generateReportViewTask( selectedObject.getId(), getPatterDate() );
      if ( UtilCommon.validateResponseSuccess( response ) ) {
        byteArrayInputStream = new ByteArrayInputStream( ( byte[] ) response.getObjectResponse() );
        fileTemplate = new DefaultStreamedContent( byteArrayInputStream, "application/pdf", ConstantWEB.NAME_FILE_PDG_REPORT );
      } else {
        addMessageError( null, ConstantWEB.MESSAGE_ERROR, ConstantWEB.MSG_ERROR_GEN_REPORT_TASK );
      }
    } catch ( Exception e ) {
      ConstantWEB.WEB_LOG.error( e.getMessage(), e );
      addMessageError( null, ConstantWEB.MESSAGE_ERROR, e.getMessage() );
    }
  }
  
  public List< TaskDTO > getListTask () {
    return listTask;
  }
  
  public void setListTask ( List< TaskDTO > listTask ) {
    this.listTask = listTask;
  }
  
  public TaskDTO getSelectedObject () {
    return selectedObject;
  }
  
  public void setSelectedObject ( TaskDTO selectedObject ) {
    this.selectedObject = selectedObject;
  }
  
  public boolean isShowViewDetail () {
    return showViewDetail;
  }
  
  public void setShowViewDetail ( boolean showViewDetail ) {
    this.showViewDetail = showViewDetail;
  }
  
  public boolean isShowMenu () {
    return showMenu;
  }
  
  public void setShowMenu ( boolean showMenu ) {
    this.showMenu = showMenu;
  }
  
  public int getIndexTab () {
    return indexTab;
  }
  
  public void setIndexTab ( int indexTab ) {
    this.indexTab = indexTab;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< AttachDTO > getListAttachFiles () {
    listAttachFiles = ( List< AttachDTO > ) ( getObjectSession( "listAttachFiles" ) != null ? getObjectSession( "listAttachFiles" ) : null );
    return listAttachFiles;
  }
  
  public void setListAttachFiles ( List< AttachDTO > listAttachFiles ) {
    this.listAttachFiles = listAttachFiles;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< TaskDTO > getListTaskHystory () {
    listTaskHystory = ( List< TaskDTO > ) getObjectSession( "listTaskHystory" );
    if ( listTaskHystory == null ) {
      listTaskHystory = new ArrayList< TaskDTO >();
    }
    return listTaskHystory;
  }
  
  public void setListTaskHystory ( List< TaskDTO > listTaskHystory ) {
    this.listTaskHystory = listTaskHystory;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< String > getListColumnLabelTaskHistory () {
    listColumnLabelTaskHistory = ( List< String > ) getObjectSession( "listColumnLabelTaskHistory" );
    if ( listColumnLabelTaskHistory == null ) {
      listColumnLabelTaskHistory = new ArrayList< String >();
    }
    return listColumnLabelTaskHistory;
  }
  
  public void setListColumnLabelTaskHistory ( List< String > listColumnLabelTaskHistory ) {
    this.listColumnLabelTaskHistory = listColumnLabelTaskHistory;
  }
  
  public String[] getColumnSelectedHistory () {
    columnSelectedHistory = ( String[] ) getObjectSession( "columnSelectedHistory" );
    if ( columnSelectedHistory == null ) {
      columnSelectedHistory = new String[1];
    }
    return columnSelectedHistory;
  }
  
  public void setColumnSelectedHistory ( String[] columnSelectedHistory ) {
    this.columnSelectedHistory = columnSelectedHistory;
  }
  
  public AttachDTO getAttachDTOSelected () {
    return attachDTOSelected;
  }
  
  public void setAttachDTOSelected ( AttachDTO attachDTOSelected ) {
    this.attachDTOSelected = attachDTOSelected;
  }
  
  @SuppressWarnings ( "unchecked" )
  public List< ColumnModel > getColumnsHistory () {
    columnsHistory = ( List< ColumnModel > ) getObjectSession( "columnsHistory" );
    if ( columnsHistory == null ) {
      columnsHistory = new ArrayList< ColumnModel >();
    }
    return columnsHistory;
  }
  
  public void setColumnsHistory ( List< ColumnModel > columnsHistory ) {
    this.columnsHistory = columnsHistory;
  }
  
  public void setDownload ( DefaultStreamedContent download ) {
    this.download = download;
  }
  
  public DefaultStreamedContent getDownload () throws Exception {
    return download;
  }
  
  @SuppressWarnings ( "unchecked" )
  public ArrayList< UploadedFile > getListFilesTask () {
    listFilesTask = ( ArrayList< UploadedFile > ) ( getObjectSession( "listFilesTask" ) != null ? getObjectSession( "listFilesTask" ) : null );
    return listFilesTask;
  }
  
  public void setListFilesTask ( ArrayList< UploadedFile > listFilesTask ) {
    this.listFilesTask = listFilesTask;
  }
  
  public UploadedFile getFileSelected () {
    return fileSelected;
  }
  
  @SuppressWarnings ( "unchecked" )
  public void setFileSelected ( UploadedFile fileSelected ) {
    if ( fileSelected != null ) {
      listFilesTask = ( ArrayList< UploadedFile > ) getObjectSession( "listFilesTask" );
      if ( listFilesTask != null ) {
        listFilesTask.remove( fileSelected );
        addObjectSession( listFilesTask, "listFilesTask" );
      }
    }
  }
  
  public boolean isShowDialogConfirmSave () {
    return showDialogConfirmSave;
  }
  
  public void setShowDialogConfirmSave ( boolean showDialogConfirmSave ) {
    this.showDialogConfirmSave = showDialogConfirmSave;
  }
  
  public StreamedContent getFileTemplate () {
    return fileTemplate;
  }
  
  public void setFileTemplate ( StreamedContent fileTemplate ) {
    this.fileTemplate = fileTemplate;
  }
}
