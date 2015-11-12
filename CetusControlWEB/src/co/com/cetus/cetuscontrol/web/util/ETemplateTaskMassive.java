package co.com.cetus.cetuscontrol.web.util;

/**
 * The Enum ETemplateTaskMassive.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlWEB (25/06/2015)
 */
public enum ETemplateTaskMassive {
  
  TASK_TYPE ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_TASK_TYPE" ) ),
  AREA ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_AREA" ) ),
  PRIORITY ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_PRIORITY" ) ),
  STATUS ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_STATUS" ) ),
  ID_GROUP ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_ID_GROUP" ) ),
  ID_RESPONSIBLE ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_ID_RESPONSIBLE" ) ),
  DESCRIPTION ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_DESCRIPTION" ) ),
  OBSERVATION ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_OBSERVATION" ) ),
  REQUESTOR ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_REQUESTOR" ) ),
  OBSERVATION_REQUESTOR ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_OBSERVATION_REQUESTOR" ) ),
  USER_FUNCTIONAL ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_USER_FUNCTIONAL" ) ),
  ASSING_DATE ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_ASSING_DATE" ) ),
  DURATION ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_DURATION" ) ),
  DATE_END ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_DATE_END" ) ),
  APPROVED ( UtilWEB.getProperty( ConstantWEB.NAME_BUNDLE_WEB, "NAME_COLUMN_APPROVED" ) );
  
  private String name;
  
  ETemplateTaskMassive ( String name ) {
    this.name = name;
  }
  
  public static boolean existsTemplateColumn ( String name ) {
    try {
      for ( ETemplateTaskMassive enumObj: ETemplateTaskMassive.values() ) {
        if ( enumObj.getName().equals( name ) ) {
          return true;
        }
      }
      return false;
    } catch ( Exception e ) {
      throw e;
    }
  }
  
  public String getName () {
    return name;
  }
  
  public void setName ( String name ) {
    this.name = name;
  }
  
}
