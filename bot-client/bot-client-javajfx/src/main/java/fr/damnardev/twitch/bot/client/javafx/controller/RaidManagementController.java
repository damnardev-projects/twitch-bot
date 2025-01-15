package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableButtonTableCell;
import fr.damnardev.twitch.bot.model.DomainService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class RaidManagementController {

	private final ApplicationContext applicationContext;

	@FXML
	private TextField textFieldRaidMessage;

	@FXML
	private TableView<String> tableViewMessage;

	@FXML
	private TableColumn<String, String> columnDeleted;

	@FXML
	private TableColumn<String, String> columnMessage;

	@FXML
	private CheckBox wizebotShoutoutEnabled;

	@FXML
	private CheckBox twitchShoutoutEnabled;

	@FXML
	private CheckBox raidMessageEnabled;

	@FXML
	public void initialize() {
		setupTableView();
		setupColumn();
		setupCheckBox();
	}

	private void setupCheckBox() {
		this.wizebotShoutoutEnabled.selectedProperty().bindBidirectional(this.applicationContext.getRaidConfiguration().getWizebotShoutoutEnabled());
		this.twitchShoutoutEnabled.selectedProperty().bindBidirectional(this.applicationContext.getRaidConfiguration().getTwitchShoutoutEnabled());
		this.raidMessageEnabled.selectedProperty().bindBidirectional(this.applicationContext.getRaidConfiguration().getRaidMessageEnabled());
	}

	private void setupTableView() {
		this.tableViewMessage.setItems(this.applicationContext.getRaidConfiguration().getMessages());
	}

	private void setupColumn() {
		this.columnMessage.setCellValueFactory((cell) -> new SimpleStringProperty(cell.getValue()));
		this.columnDeleted.setCellFactory((x) -> new UnfocusableButtonTableCell<>(this::onButtonDelete));
	}

	public void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.DELETE) && this.tableViewMessage.isFocused()) {
			var selectedItem = this.tableViewMessage.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				onButtonDelete(selectedItem);
			}
		}
	}

	private void onButtonDelete(String value) {
		this.applicationContext.getRaidConfiguration().getMessages().remove(value);

	}

	public void onEnterKeyPressed() {
		addMessage();
	}

	public void onButtonAdd() {
		addMessage();
	}

	private void addMessage() {
		this.applicationContext.getRaidConfiguration().getMessages().add(this.textFieldRaidMessage.getText());
	}

}
