package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.DomainService;
import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableButtonTableCell;
import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableCheckBoxTableCell;
import fr.damnardev.twitch.bot.client.javafx.wrapper.RaidConfigurationMessageWrapper;
import fr.damnardev.twitch.bot.client.javafx.wrapper.RaidConfigurationWrapper;
import fr.damnardev.twitch.bot.client.model.RaidConfiguration;
import fr.damnardev.twitch.bot.client.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.client.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.client.model.event.RaidConfigurationFetchedAllEvent;
import fr.damnardev.twitch.bot.client.model.event.RaidConfigurationFetchedEvent;
import fr.damnardev.twitch.bot.client.model.event.RaidConfigurationUpdatedEvent;
import fr.damnardev.twitch.bot.client.model.form.CreateRaidConfigurationMessageForm;
import fr.damnardev.twitch.bot.client.model.form.DeleteRaidConfigurationMessageForm;
import fr.damnardev.twitch.bot.client.model.form.UpdateRaidConfigurationForm;
import fr.damnardev.twitch.bot.client.port.primary.RaidConfigurationService;
import fr.damnardev.twitch.bot.client.port.secondary.raid.CreateRaidConfigurationMessageRepository;
import fr.damnardev.twitch.bot.client.port.secondary.raid.DeleteRaidConfigurationMessageRepository;
import fr.damnardev.twitch.bot.client.port.secondary.raid.FetchAllRaidConfigurationRepository;
import fr.damnardev.twitch.bot.client.port.secondary.raid.FetchRaidConfigurationRepository;
import fr.damnardev.twitch.bot.client.port.secondary.raid.UpdateRaidConfigurationRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@DomainService
@RequiredArgsConstructor
@Slf4j
public class RaidConfigurationController implements RaidConfigurationService {

	private final FetchAllRaidConfigurationRepository fetchAllRaidConfigurationRepository;

	private final FetchRaidConfigurationRepository fetchRaidConfigurationRepository;

	private final StatusController statusController;

	private final CreateRaidConfigurationMessageRepository createRaidConfigurationMessageRepository;

	private final DeleteRaidConfigurationMessageRepository deleteRaidConfigurationMessageRepository;

	private final UpdateRaidConfigurationRepository updateRaidConfigurationRepository;

	@FXML
	public TableView<RaidConfigurationWrapper> tableViewRaidConfiguration;

	@FXML
	public TableColumn<RaidConfigurationWrapper, Number> columnId;

	@FXML
	public TableColumn<RaidConfigurationWrapper, String> columnName;

	@FXML
	public TableColumn<RaidConfigurationWrapper, Boolean> columnTwitchShoutoutEnabled;

	@FXML
	public TableColumn<RaidConfigurationWrapper, Boolean> columnWizebotShoutoutEnabled;

	@FXML
	public TableColumn<RaidConfigurationWrapper, Boolean> columnRaidMessageEnabled;

	@FXML
	public TableColumn<RaidConfigurationMessageWrapper, String> columnMessage;

	@FXML
	public TableView<RaidConfigurationMessageWrapper> tableViewMessage;

	@FXML
	public TableColumn<RaidConfigurationMessageWrapper, String> columnDeleted;

	@FXML
	public TextField textFieldMessage;

	@FXML
	public void initialize() {
		setupTableView();
		setupColumn();
		refresh();
	}

	private void setupTableView() {
		this.tableViewRaidConfiguration.getSortOrder().add(this.columnId);
		this.tableViewRaidConfiguration.sort();
		this.tableViewRaidConfiguration.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				this.tableViewMessage.getItems().clear();
				this.tableViewMessage.getItems().addAll(newValue.getMessages());
			}
		});
	}

	private void setupColumn() {
		this.columnId.setCellValueFactory((cell) -> cell.getValue().idProperty());
		this.columnName.setCellValueFactory((cell) -> cell.getValue().nameProperty());
		this.columnTwitchShoutoutEnabled.setCellValueFactory((cell) -> cell.getValue().twitchShoutoutEnabledProperty());
		this.columnTwitchShoutoutEnabled.setCellFactory((x) -> new UnfocusableCheckBoxTableCell<>());
		this.columnWizebotShoutoutEnabled.setCellValueFactory((cell) -> cell.getValue().wizebotShoutoutEnabledProperty());
		this.columnWizebotShoutoutEnabled.setCellFactory((x) -> new UnfocusableCheckBoxTableCell<>());
		this.columnRaidMessageEnabled.setCellValueFactory((cell) -> cell.getValue().raidMessageEnabledProperty());
		this.columnRaidMessageEnabled.setCellFactory((x) -> new UnfocusableCheckBoxTableCell<>());
		this.columnMessage.setCellValueFactory((cell) -> cell.getValue().messageProperty());
		this.columnDeleted.setCellFactory((x) -> new UnfocusableButtonTableCell<>(this::onButtonDelete));
	}

	private void refresh() {
		this.fetchAllRaidConfigurationRepository.fetchAll();
	}

	private void sort() {
		this.tableViewRaidConfiguration.sort();
		this.tableViewRaidConfiguration.refresh();
		this.tableViewMessage.sort();
		this.tableViewMessage.refresh();
	}

	private RaidConfigurationWrapper buildWrapper(RaidConfiguration raidConfiguration) {
		var raidConfigurationWrapper = new RaidConfigurationWrapper(raidConfiguration);
		raidConfigurationWrapper.wizebotShoutoutEnabledProperty().addListener((observable, oldValue, newValue) -> {
			var form = UpdateRaidConfigurationForm.builder().channelId(raidConfiguration.channelId()).channelName(raidConfiguration.channelName()).wizebotShoutoutEnabled(newValue).build();
			this.updateRaidConfigurationRepository.update(form);
		});
		raidConfigurationWrapper.twitchShoutoutEnabledProperty().addListener((observable, oldValue, newValue) -> {
			var form = UpdateRaidConfigurationForm.builder().channelId(raidConfiguration.channelId()).channelName(raidConfiguration.channelName()).twitchShoutoutEnabled(newValue).build();
			this.updateRaidConfigurationRepository.update(form);
		});
		raidConfigurationWrapper.raidMessageEnabledProperty().addListener((observable, oldValue, newValue) -> {
			var form = UpdateRaidConfigurationForm.builder().channelId(raidConfiguration.channelId()).channelName(raidConfiguration.channelName()).raidMessageEnabled(newValue).build();
			this.updateRaidConfigurationRepository.update(form);
		});
		return raidConfigurationWrapper;
	}

	public void onEnterKeyPressed(ActionEvent actionEvent) {
		onButtonAdd();
	}

	public void onButtonAdd() {
		var message = this.textFieldMessage.getText();
		if (message.isBlank()) {
			this.statusController.setLabelText("Channel channelName is empty", true);
			return;
		}
		log.info("Try to add message: {}", message);
		var raidConfiguration = this.tableViewRaidConfiguration.getFocusModel().getFocusedItem();
		var channelId = raidConfiguration.idProperty().get();
		var channelName = raidConfiguration.nameProperty().get();
		var form = CreateRaidConfigurationMessageForm.builder().channelId(channelId).channelName(channelName).message(message).build();
		this.createRaidConfigurationMessageRepository.create(form);
	}

	public void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.DELETE) && this.tableViewMessage.isFocused()) {
			var selectedItem = this.tableViewMessage.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				onButtonDelete(selectedItem);
			}
		}
		if (this.tableViewRaidConfiguration.isFocused()) {
			var selectedItem = this.tableViewRaidConfiguration.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				if (keyEvent.getCode().equals(KeyCode.E)) {
					selectedItem.wizebotShoutoutEnabledProperty().set(!selectedItem.wizebotShoutoutEnabledProperty().get());
				}
				if (keyEvent.getCode().equals(KeyCode.T)) {
					selectedItem.twitchShoutoutEnabledProperty().set(!selectedItem.twitchShoutoutEnabledProperty().get());
				}
				if (keyEvent.getCode().equals(KeyCode.R)) {
					selectedItem.raidMessageEnabledProperty().set(!selectedItem.raidMessageEnabledProperty().get());
				}
			}
		}
	}

	private void onButtonDelete(RaidConfigurationMessageWrapper message) {
		log.info("Try to delete message: {}", message);
		var form = DeleteRaidConfigurationMessageForm.builder().channelId(message.idProperty().get()).channelName(message.nameProperty().get()).message(message.messageProperty().get()).build();
		this.deleteRaidConfigurationMessageRepository.delete(form);
	}

	public void onRaidConfigurationFindAllEvent(RaidConfigurationFetchedAllEvent event) {
		log.info("Configurations found [size]: {}", event.value().size());
		var wrappers = event.value().stream().map(this::buildWrapper).toList();
		this.tableViewRaidConfiguration.getItems().clear();
		this.tableViewRaidConfiguration.getItems().addAll(wrappers);
		sort();
	}

	public void onRaidConfigurationFindEvent(RaidConfigurationFetchedEvent event) {
		log.info("Configuration found: {}", event);
		var wrapper = buildWrapper(event.getValue());
		this.tableViewRaidConfiguration.getItems().add(wrapper);
		sort();
	}

	public void onRaidConfigurationUpdatedEvent(RaidConfigurationUpdatedEvent event) {
		log.info("Configuration updated: {}", event);
		var raidConfiguration = getRaidConfiguration(event);
		updateRaidConfigurationMessage(event, raidConfiguration);
	}

	private RaidConfigurationWrapper getRaidConfiguration(RaidConfigurationUpdatedEvent event) {
		return this.tableViewRaidConfiguration.getItems().stream().filter((wrapper) -> wrapper.idProperty().get() == event.getValue().channelId()).findFirst().orElseThrow();
	}

	private void updateRaidConfigurationMessage(RaidConfigurationUpdatedEvent event, RaidConfigurationWrapper configuration) {
		configuration.getMessages().clear();
		event.getValue().messages().forEach((message) -> {
			RaidConfigurationMessageWrapper e = new RaidConfigurationMessageWrapper(configuration.idProperty().get(), configuration.nameProperty().get(), message);
			configuration.getMessages().add(e);
		});
		this.tableViewMessage.getItems().clear();
		this.tableViewMessage.getItems().addAll(configuration.getMessages());
		this.statusController.setLabelText("Raid configuration users " + event.getValue().channelName(), false);
	}

	public void onChannelCreatedEvent(ChannelCreatedEvent event) {
		this.fetchRaidConfigurationRepository.fetch(event.value().name());
	}

	public void onChannelDeletedEvent(ChannelDeletedEvent event) {
		this.tableViewRaidConfiguration.getItems().removeIf((w) -> w.idProperty().getValue().equals(event.getValue().id()));
	}

	@Override
	public void fetchAll(RaidConfigurationFetchedAllEvent event) {
		onRaidConfigurationFindAllEvent(event);
	}

}
