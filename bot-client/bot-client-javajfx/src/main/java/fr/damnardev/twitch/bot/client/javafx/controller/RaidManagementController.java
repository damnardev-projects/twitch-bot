package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableButtonTableCell;
import fr.damnardev.twitch.bot.client.javafx.wrapper.RaidConfigurationMessageWrapper;
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
	private TableView<RaidConfigurationMessageWrapper> tableViewMessage;

	@FXML
	private TableColumn<RaidConfigurationMessageWrapper, String> columnDeleted;

	@FXML
	private TableColumn<RaidConfigurationMessageWrapper, String> columnMessage;

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
		this.wizebotShoutoutEnabled.selectedProperty().bindBidirectional(this.applicationContext.getRaidConfiguration().wizebotShoutoutEnabledProperty());
		this.twitchShoutoutEnabled.selectedProperty().bindBidirectional(this.applicationContext.getRaidConfiguration().twitchShoutoutEnabledProperty());
		this.raidMessageEnabled.selectedProperty().bindBidirectional(this.applicationContext.getRaidConfiguration().raidMessageEnabledProperty());
	}

	private void setupTableView() {
		this.tableViewMessage.setItems(this.applicationContext.getRaidConfiguration().messages());
	}

	private void setupColumn() {
		this.columnMessage.setCellValueFactory((cell) -> cell.getValue().messageProperty());
		this.columnDeleted.setCellFactory((x) -> new UnfocusableButtonTableCell<>(this::onButtonDelete));
	}

	private void onButtonDelete(RaidConfigurationMessageWrapper message) {
	}

	public void onEnterKeyPressed(ActionEvent actionEvent) {

	}

	public void onButtonAdd(ActionEvent actionEvent) {

	}

	public void onKeyPressed(KeyEvent keyEvent) {

	}

}
