package co.com.cetus.cetuscontrol.ejb.validator;

import co.com.cetus.common.exception.ValidatorException;
import co.com.cetus.common.util.UtilCommon;

public class CetusControlEJBValidator {
  
  public CetusControlEJBValidator () {
  }
  
  public static < T > void validateDTO ( T dto ) throws ValidatorException {
    if ( dto == null ) throw new ValidatorException( "El DTO es nulo.", "CetusControlEJBValidator" );
  }
  
  public static void componentIsNullOrEmpty ( String component ) throws ValidatorException {
    if ( UtilCommon.stringNullOrEmpty( component ) ) throw new ValidatorException( "El parametro component es nulo o vacio.",
                                                                                   "CetusControlEJBValidator" );
  }
  
  public static void listParameterIsNullOrEmpty ( String listParameter ) throws ValidatorException {
    if ( UtilCommon.stringNullOrEmpty( listParameter ) ) throw new ValidatorException( "El parametro listParameter es nulo o vacio.",
                                                                                       "CetusControlEJBValidator" );
  }
  
  public static void classDTOIsNullOrEmpty ( String classDTO ) throws ValidatorException {
    if ( UtilCommon.stringNullOrEmpty( classDTO ) ) throw new ValidatorException( "El parametro classDTO es nulo o vacio.",
                                                                                  "CetusControlEJBValidator" );
  }
  
  public static void fieldOrderIsNullOrEmpty ( String fieldOrder ) throws ValidatorException {
    if ( UtilCommon.stringNullOrEmpty( fieldOrder ) ) throw new ValidatorException( "El parametro fieldOrder es nulo o vacio.",
                                                                                    "CetusControlEJBValidator" );
  }
  
  public static void typeOrderIsNullOrEmpty ( String typeOrder ) throws ValidatorException {
    if ( UtilCommon.stringNullOrEmpty( typeOrder ) ) throw new ValidatorException( "El parametro typeOrder es nulo o vacio.",
                                                                                   "CetusControlEJBValidator" );
  }
  
  public static void idIsNull ( Object id ) throws ValidatorException {
    if ( id == null ) throw new ValidatorException( "El parametro id es nulo.", "CetusControlEJBValidator" );
  }
  
  public static void arrRangeIsNull ( int[] arrRange ) throws ValidatorException {
    if ( arrRange == null ) throw new ValidatorException( "El parametro arrRange es nulo.", "CetusControlEJBValidator" );
  }
  
}
