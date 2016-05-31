package co.com.cetus.cetuscontrol.jpa.entity;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the comment_task database table.
 * 
 */
@Entity
@Table ( name = "COMMENT_TASK" )
@NamedQueries ( {
                  @NamedQuery ( name = "CommentTask.findAll", query = "SELECT c FROM CommentTask c" ),
                  @NamedQuery ( name = "CommentTask.findCommentByIdTask", query = "SELECT c FROM CommentTask c WHERE c.task.id = :idTask ORDER BY c.creationDate DESC" )
})
public class CommentTask implements Serializable {
  private static final long serialVersionUID = 1L;
                                             
  @Id
  @GeneratedValue ( strategy = GenerationType.IDENTITY )
  private int               id;
                            
  @Column ( name = "SUBJECT" )
  private String            subject;
                            
  @Column ( name = "COMMENT_T" )
  private String            commentT;
                            
  @Temporal ( TemporalType.TIMESTAMP )
  @Column ( name = "CREATION_DATE" )
  private Date              creationDate;
                            
  private String            emitter;
                            
  @ManyToOne
  @JoinColumn ( name = "ID_TASK" )
  private Task              task;
                            
  private String            receiver;
                            
  public CommentTask () {
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
  
  public Task getTask () {
    return task;
  }
  
  public void setTask ( Task task ) {
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