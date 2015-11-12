package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the parameter_general database table.
 * 
 */
public class ParameterGeneralDTO implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private int               id;
  
  private int               colNumber1;
  
  private int               colNumber2;
  
  private int               colNumber3;
  
  private int               colNumberdc1;
  
  private int               colNumberdc2;
  
  private int               colNumberdc3;
  
  private String            colVarchar1;
  
  private String            colVarchar2;
  
  private String            colVarchar3;
  
  private Date              creationDate;
  
  private String            creationUser;
  
  private int               dayMonth;
  
  private String            hourFormat;
  
  private int               hourWeek;
  
  private Date              modificationDate;
  
  private String            modificationUser;
  
  private int               timeAfterExpiration;
  
  private int               timeBeforeExpiration;
  
  private ClientCetusDTO    clientCetus;
  
  public ParameterGeneralDTO () {
  }
  
  public int getId () {
    return this.id;
  }
  
  public void setId ( int id ) {
    this.id = id;
  }
  
  public int getColNumber1 () {
    return this.colNumber1;
  }
  
  public void setColNumber1 ( int colNumber1 ) {
    this.colNumber1 = colNumber1;
  }
  
  public int getColNumber2 () {
    return this.colNumber2;
  }
  
  public void setColNumber2 ( int colNumber2 ) {
    this.colNumber2 = colNumber2;
  }
  
  public int getColNumber3 () {
    return this.colNumber3;
  }
  
  public void setColNumber3 ( int colNumber3 ) {
    this.colNumber3 = colNumber3;
  }
  
  public int getColNumberdc1 () {
    return this.colNumberdc1;
  }
  
  public void setColNumberdc1 ( int colNumberdc1 ) {
    this.colNumberdc1 = colNumberdc1;
  }
  
  public int getColNumberdc2 () {
    return this.colNumberdc2;
  }
  
  public void setColNumberdc2 ( int colNumberdc2 ) {
    this.colNumberdc2 = colNumberdc2;
  }
  
  public int getColNumberdc3 () {
    return this.colNumberdc3;
  }
  
  public void setColNumberdc3 ( int colNumberdc3 ) {
    this.colNumberdc3 = colNumberdc3;
  }
  
  public String getColVarchar1 () {
    return this.colVarchar1;
  }
  
  public void setColVarchar1 ( String colVarchar1 ) {
    this.colVarchar1 = colVarchar1;
  }
  
  public String getColVarchar2 () {
    return this.colVarchar2;
  }
  
  public void setColVarchar2 ( String colVarchar2 ) {
    this.colVarchar2 = colVarchar2;
  }
  
  public String getColVarchar3 () {
    return this.colVarchar3;
  }
  
  public void setColVarchar3 ( String colVarchar3 ) {
    this.colVarchar3 = colVarchar3;
  }
  
  public Date getCreationDate () {
    return this.creationDate;
  }
  
  public void setCreationDate ( Date creationDate ) {
    this.creationDate = creationDate;
  }
  
  public String getCreationUser () {
    return this.creationUser;
  }
  
  public void setCreationUser ( String creationUser ) {
    this.creationUser = creationUser;
  }
  
  public int getDayMonth () {
    return this.dayMonth;
  }
  
  public void setDayMonth ( int dayMonth ) {
    this.dayMonth = dayMonth;
  }
  
  public String getHourFormat () {
    return this.hourFormat;
  }
  
  public void setHourFormat ( String hourFormat ) {
    this.hourFormat = hourFormat;
  }
  
  public int getHourWeek () {
    return this.hourWeek;
  }
  
  public void setHourWeek ( int hourWeek ) {
    this.hourWeek = hourWeek;
  }
  
  public Date getModificationDate () {
    return this.modificationDate;
  }
  
  public void setModificationDate ( Date modificationDate ) {
    this.modificationDate = modificationDate;
  }
  
  public String getModificationUser () {
    return this.modificationUser;
  }
  
  public void setModificationUser ( String modificationUser ) {
    this.modificationUser = modificationUser;
  }
  
  public int getTimeAfterExpiration () {
    return this.timeAfterExpiration;
  }
  
  public void setTimeAfterExpiration ( int timeAfterExpiration ) {
    this.timeAfterExpiration = timeAfterExpiration;
  }
  
  public int getTimeBeforeExpiration () {
    return this.timeBeforeExpiration;
  }
  
  public void setTimeBeforeExpiration ( int timeBeforeExpiration ) {
    this.timeBeforeExpiration = timeBeforeExpiration;
  }
  
  public ClientCetusDTO getClientCetus () {
    return this.clientCetus;
  }
  
  public void setClientCetus ( ClientCetusDTO clientCetus ) {
    this.clientCetus = clientCetus;
  }
  
}