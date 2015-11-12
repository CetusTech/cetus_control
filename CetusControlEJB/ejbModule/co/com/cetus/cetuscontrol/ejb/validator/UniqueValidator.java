package co.com.cetus.cetuscontrol.ejb.validator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.persistence.Entity;

import co.com.cetus.cetuscontrol.ejb.process.GeneralProcess;
import co.com.cetus.cetuscontrol.ejb.util.ConstantEJB;
import co.com.cetus.common.dto.AttributeDTO;
import co.com.cetus.common.exception.ProcessException;
import co.com.cetus.common.util.ConstantCommon;
import co.com.cetus.common.validation.UniqueConstraint;
import co.com.cetus.common.validation.UniqueConstraints;
import co.com.cetus.common.validation.exception.UniqueViolationException;

/**
 * The Class UniqueValidator.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlEJB (18/02/2015)
 */
public class UniqueValidator {
  
  /** The general process. */
  @EJB
  private GeneralProcess generalProcess;
  
  /**
   * </p> Instancia un nuevo unique validator. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param generalProcess the general process
   * @since CetusControlEJB (18/02/2015)
   */
  public UniqueValidator ( GeneralProcess generalProcess ) {
    this.generalProcess = generalProcess;
  }
  
  /**
   * </p> Validate. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param obj the obj
   * @throws UniqueViolationException the unique violation exception
   * @throws NoSuchMethodException the no such method exception
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   * @since CetusControlEJB (18/02/2015)
   */
  public void validate ( Object obj ) throws UniqueViolationException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    if ( isEntity( obj ) ) {
      UniqueConstraints uniqueConstraints = obj.getClass().getAnnotation( UniqueConstraints.class );
      if ( uniqueConstraints != null ) {
        UniqueConstraint[] constraints = uniqueConstraints.uniqueConstraints();
        if ( constraints != null && constraints.length > 0 ) {
          for ( UniqueConstraint constraint: constraints ) {
            validateConstraint( constraint, obj );
          }
        }
      }
    }
  }
  
  /**
   * </p> Checks if is entity. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param obj the obj
   * @return true, si el proceso fue exitoso
   * @since CetusControlEJB (18/02/2015)
   */
  private boolean isEntity ( Object obj ) {
    return obj != null && obj.getClass().getAnnotation( Entity.class ) != null;
  }
  
  /**
   * </p> Validate constraint. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param constraint the constraint
   * @param obj the obj
   * @throws UniqueViolationException the unique violation exception
   * @throws NoSuchMethodException the no such method exception
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   * @since CetusControlEJB (18/02/2015)
   */
  private void validateConstraint ( UniqueConstraint constraint, Object obj ) throws UniqueViolationException, NoSuchMethodException,
                                                                             IllegalAccessException,
                                                                             InvocationTargetException {
    String[] fields = constraint.fields();
    ArrayList< AttributeDTO > atributos = new ArrayList< AttributeDTO >();
    for ( String field: fields ) {
      AttributeDTO atributo = this.getValueForField( field, obj );
      if ( atributo != null ) {
        atributos.add( atributo );
      }
    }
    @SuppressWarnings ( "rawtypes" )
    List entities = null;
    try {
      entities = generalProcess.findAllFilter( obj.getClass(), atributos, ConstantCommon.CONJUNCION );
    } catch ( ProcessException e ) {
      ConstantEJB.CETUS_CONTROL_EJB_LOG.error( e.getMessage(), e );
    }
    if ( entities != null && entities.size() > 0 ) {
      UniqueViolationException uve = new UniqueViolationException( constraint.name(), constraint.errorMessage(), constraint.fields() );
      throw uve;
    }
  }
  
  /**
   * </p> Gets the value for field. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param fieldName the field name
   * @param obj the obj
   * @return el attribute dto
   * @throws NoSuchMethodException the no such method exception
   * @throws IllegalAccessException the illegal access exception
   * @throws InvocationTargetException the invocation target exception
   * @since CetusControlEJB (18/02/2015)
   */
  private AttributeDTO getValueForField ( String fieldName, Object obj ) throws NoSuchMethodException, IllegalAccessException,
                                                                        InvocationTargetException {
    AttributeDTO attribute = null;
    if ( fieldName != null ) {
      String get = "get" + Character.toUpperCase( fieldName.charAt( 0 ) )
                   + fieldName.substring( 1 );
      try {
        Method method = obj.getClass().getMethod( get );
        if ( method != null ) {
          Object value = method.invoke( obj );
          attribute = new AttributeDTO( fieldName, value );
        }
      } catch ( SecurityException e ) {
        throw e;
      } catch ( NoSuchMethodException e ) {
        throw e;
      } catch ( IllegalArgumentException e ) {
        throw e;
      } catch ( IllegalAccessException e ) {
        throw e;
      } catch ( InvocationTargetException e ) {
        throw e;
      }
    }
    return attribute;
  }
}
