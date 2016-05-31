package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class CommentTaskDTO.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlDTO (29/05/2016)
 */
public class CommentTaskDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  private int               id;
  private String            subject;
  private String            commentT;
  private Date              creationDate;
  private String            emitter;
  private TaskDTO           task;
                            
  private String            receiver;
                            
  public CommentTaskDTO () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public String getCommentT () {
    return this.commentT;
  }
  
  public void setCommentT ( String commentT ) {
    this.commentT = commentT;
  }
  
  public Date getCreationDate () {
    return this.creationDate;
  }
  
  public void setCreationDate ( Date creationDate ) {
    this.creationDate = creationDate;
  }
  
  public String getEmitter () {
    return this.emitter;
  }
  
  public void setEmitter ( String emitter ) {
    this.emitter = emitter;
  }
  
  public TaskDTO getTask () {
    return task;
  }
  
  public void setTask ( TaskDTO task ) {
    this.task = task;
  }
  
  public String getReceiver () {
    return this.receiver;
  }
  
  public void setReceiver ( String receiver ) {
    this.receiver = receiver;
  }
  
  public String getSubject () {
    return subject;
  }
  
  public void setSubject ( String subject ) {
    this.subject = subject;
  }
  
}