package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The Class JornadaListDTO.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlDTO (12/03/2015)
 */
public class JornadaListDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private Date              starDate1;
  private Date              endDate1;
  private Date              starDate2;
  private Date              endDate2;
  private Date              starDate3;
  private Date              endDate3;
  private Date              starDate4;
  private Date              endDate4;
  private boolean           registeredDB1;
  private boolean           disable1         = true;
  private boolean           registeredDB2;
  private boolean           disable2         = true;
  private boolean           registeredDB3;
  private boolean           disable3         = true;
  private boolean           registeredDB4;
  private boolean           disable4         = true;
  private int               numberJornada;
  
  public JornadaListDTO () {
  }
  
  public void loadJornadaListDTO ( List< WorkdayDTO > listWorkday, String day, boolean consultDB, boolean disabled ) {
    if ( listWorkday != null ) {
      numberJornada = 0;
      for ( WorkdayDTO workdayDTO: listWorkday ) {
        if ( day.equals( workdayDTO.getColDay() ) ) {
          if ( workdayDTO.getJornada() == 1 ) {
            this.starDate1 = convertHourToDate( workdayDTO.getStartTime() );
            this.endDate1 = convertHourToDate( workdayDTO.getEndTime() );
            this.registeredDB1 = consultDB;
            this.disable1 = disabled;
          } else if ( workdayDTO.getJornada() == 2 ) {
            this.starDate2 = convertHourToDate( workdayDTO.getStartTime() );
            this.endDate2 = convertHourToDate( workdayDTO.getEndTime() );
            this.registeredDB2 = consultDB;
            this.disable2 = disabled;
          } else if ( workdayDTO.getJornada() == 3 ) {
            this.starDate3 = convertHourToDate( workdayDTO.getStartTime() );
            this.endDate3 = convertHourToDate( workdayDTO.getEndTime() );
            this.registeredDB3 = consultDB;
            this.disable3 = disabled;
          } else if ( workdayDTO.getJornada() == 4 ) {
            this.starDate4 = convertHourToDate( workdayDTO.getStartTime() );
            this.endDate4 = convertHourToDate( workdayDTO.getEndTime() );
            this.registeredDB4 = consultDB;
            this.disable4 = disabled;
          }
          numberJornada++;
        }
      }
    }
  }
  
  /**
   * </p> Convert hour to date. </p>
   *
   * @author Jose David Salcedo M. - Cetus Technology
   * @param hour the hour
   * @return el date
   * @since CetusControlDTO (13/03/2015)
   */
  private Date convertHourToDate ( int hour ) {
    Calendar cal = Calendar.getInstance();
    try {
      cal.set( 0, 0, 0, ( hour / 100 ), ( hour % 100 ) );
    } catch ( Exception e ) {
      e.printStackTrace();
    }
    return cal.getTime();
  }
  
  public void disableAll () {
    disable1 = true;
    disable2 = true;
    disable3 = true;
    disable4 = true;
  }
  
  public Date getStarDate1 () {
    return starDate1;
  }
  
  public void setStarDate1 ( Date starDate1 ) {
    this.starDate1 = starDate1;
  }
  
  public Date getEndDate1 () {
    return endDate1;
  }
  
  public void setEndDate1 ( Date endDate1 ) {
    this.endDate1 = endDate1;
  }
  
  public Date getStarDate2 () {
    return starDate2;
  }
  
  public void setStarDate2 ( Date starDate2 ) {
    this.starDate2 = starDate2;
  }
  
  public Date getEndDate2 () {
    return endDate2;
  }
  
  public void setEndDate2 ( Date endDate2 ) {
    this.endDate2 = endDate2;
  }
  
  public Date getStarDate3 () {
    return starDate3;
  }
  
  public void setStarDate3 ( Date starDate3 ) {
    this.starDate3 = starDate3;
  }
  
  public Date getEndDate3 () {
    return endDate3;
  }
  
  public void setEndDate3 ( Date endDate3 ) {
    this.endDate3 = endDate3;
  }
  
  public Date getStarDate4 () {
    return starDate4;
  }
  
  public void setStarDate4 ( Date starDate4 ) {
    this.starDate4 = starDate4;
  }
  
  public Date getEndDate4 () {
    return endDate4;
  }
  
  public void setEndDate4 ( Date endDate4 ) {
    this.endDate4 = endDate4;
  }
  
  public boolean isRegisteredDB1 () {
    return registeredDB1;
  }
  
  public void setRegisteredDB1 ( boolean registeredDB1 ) {
    this.registeredDB1 = registeredDB1;
  }
  
  public boolean isDisable1 () {
    return disable1;
  }
  
  public void setDisable1 ( boolean disable1 ) {
    this.disable1 = disable1;
  }
  
  public boolean isRegisteredDB2 () {
    return registeredDB2;
  }
  
  public void setRegisteredDB2 ( boolean registeredDB2 ) {
    this.registeredDB2 = registeredDB2;
  }
  
  public boolean isDisable2 () {
    return disable2;
  }
  
  public void setDisable2 ( boolean disable2 ) {
    this.disable2 = disable2;
  }
  
  public boolean isRegisteredDB3 () {
    return registeredDB3;
  }
  
  public void setRegisteredDB3 ( boolean registeredDB3 ) {
    this.registeredDB3 = registeredDB3;
  }
  
  public boolean isDisable3 () {
    return disable3;
  }
  
  public void setDisable3 ( boolean disable3 ) {
    this.disable3 = disable3;
  }
  
  public boolean isRegisteredDB4 () {
    return registeredDB4;
  }
  
  public void setRegisteredDB4 ( boolean registeredDB4 ) {
    this.registeredDB4 = registeredDB4;
  }
  
  public boolean isDisable4 () {
    return disable4;
  }
  
  public void setDisable4 ( boolean disable4 ) {
    this.disable4 = disable4;
  }
  
  public int getNumberJornada () {
    return numberJornada;
  }
  
  public void setNumberJornada ( int numberJornada ) {
    this.numberJornada = numberJornada;
  }
  
  @Override
  public String toString () {
    return "JornadaListDTO [starDate1=" + starDate1 + ", endDate1=" + endDate1 + ", starDate2=" + starDate2 + ", endDate2=" + endDate2
           + ", starDate3=" + starDate3 + ", endDate3=" + endDate3 + ", starDate4=" + starDate4 + ", endDate4=" + endDate4 + ", registeredDB1="
           + registeredDB1 + ", disable1=" + disable1 + ", registeredDB2=" + registeredDB2 + ", disable2=" + disable2 + ", registeredDB3="
           + registeredDB3 + ", disable3=" + disable3 + ", registeredDB4=" + registeredDB4 + ", disable4=" + disable4 + ", numberJornada="
           + numberJornada + "]";
  }
  
}
