package co.com.cetus.cetuscontrol.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



/**
 * The Class NotificationGeneralDTO.
 *
 * @author Jose David Salcedo M. - Cetus Technology
 * @version CetusControlDTO (16/02/2016)
 */
public class NotificationGeneralDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private Date creationDate;
	private String description;
  private ClientCetusDTO clientCetus;
	private String mandatory;
	private String name;
	private String tableColumn;
	private String tableName;
	private String templateName;
	private List<NotificationSettingDTO> notificationSettings;

	public NotificationGeneralDTO() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ClientCetusDTO getClientCetus() {
    return this.clientCetus;
  }

  public void setClientCetus(ClientCetusDTO clientCetus) {
    this.clientCetus = clientCetus;
  }

	public String getMandatory() {
		return this.mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTableColumn() {
		return this.tableColumn;
	}

	public void setTableColumn(String tableColumn) {
		this.tableColumn = tableColumn;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<NotificationSettingDTO> getNotificationSettings() {
		return this.notificationSettings;
	}

	public void setNotificationSettings(List<NotificationSettingDTO> notificationSettings) {
		this.notificationSettings = notificationSettings;
	}

	public NotificationSettingDTO addNotificationSetting(NotificationSettingDTO notificationSetting) {
		getNotificationSettings().add(notificationSetting);
		notificationSetting.setNotificationGeneral(this);

		return notificationSetting;
	}

	public NotificationSettingDTO removeNotificationSetting(NotificationSettingDTO notificationSetting) {
		getNotificationSettings().remove(notificationSetting);
		notificationSetting.setNotificationGeneral(null);

		return notificationSetting;
	}

}