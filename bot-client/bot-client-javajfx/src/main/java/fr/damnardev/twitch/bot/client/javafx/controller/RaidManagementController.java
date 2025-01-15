package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableButtonTableCell;
import fr.damnardev.twitch.bot.client.javafx.wrapper.ObservableRaidConfigurationMessage;
import fr.damnardev.twitch.bot.model.DomainService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class RaidManagementController {

	private final ApplicationContext applicationContext;

	@FXML
	private TableView<ObservableRaidConfigurationMessage> tableViewMessage;

	@FXML
	private TableColumn<ObservableRaidConfigurationMessage, String> columnDeleted;

	@FXML
	private TableColumn<ObservableRaidConfigurationMessage, String> columnMessage;

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
		this.columnMessage.setCellValueFactory((cell) -> cell.getValue().messageProperty());
		this.columnDeleted.setCellFactory((x) -> new UnfocusableButtonTableCell<>(this::onButtonDelete));
	}

	private void onButtonDelete(ObservableRaidConfigurationMessage observableRaidConfigurationMessage) {

	}

	public void onEnterKeyPressed(ActionEvent actionEvent) {

	}

	public void onButtonAdd(ActionEvent actionEvent) {

	}

	public void onKeyPressed(KeyEvent keyEvent) {

	}

}
