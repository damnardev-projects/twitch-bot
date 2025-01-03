package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.DomainService;
import fr.damnardev.twitch.bot.client.javafx.control.ChannelWrapperTableRow;
import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableButtonTableCell;
import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableCheckBoxTableCell;
import fr.damnardev.twitch.bot.client.javafx.wrapper.ChannelWrapper;
import fr.damnardev.twitch.bot.client.model.Channel;
import fr.damnardev.twitch.bot.client.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.client.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.client.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.client.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.client.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.client.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.client.model.form.UpdateChannelForm;
import fr.damnardev.twitch.bot.client.port.primary.ChannelService;
import fr.damnardev.twitch.bot.client.port.secondary.channel.CreateChannelRepository;
import fr.damnardev.twitch.bot.client.port.secondary.channel.DeleteChannelRepository;
import fr.damnardev.twitch.bot.client.port.secondary.channel.FetchAllChannelService;
import fr.damnardev.twitch.bot.client.port.secondary.channel.UpdateChannelService;
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
public class ChannelManagementController implements ChannelService {

	private final StatusController statusController;

	private final CreateChannelRepository createChannelRepository;

	private final FetchAllChannelService fetchAllChannelService;

	private final UpdateChannelService updateChannelService;

	private final DeleteChannelRepository deleteChannelRepository;

	@FXML
	public TableColumn<ChannelWrapper, String> columnName;

	@FXML
	public TableColumn<ChannelWrapper, Number> columnId;

	@FXML
	public TableColumn<ChannelWrapper, Boolean> columnEnabled;

	@FXML
	public TableColumn<ChannelWrapper, Boolean> columnOnline;

	@FXML
	public TableColumn<ChannelWrapper, String> columnDeleted;

	@FXML
	public TableView<ChannelWrapper> tableView;

	@FXML
	private TextField textFieldChannelName;

	@FXML
	public void initialize() {
		setupTableView();
		setupColumn();
		refresh();
	}

	private void setupTableView() {
		this.tableView.getSortOrder().add(this.columnId);
		this.tableView.setRowFactory((x) -> new ChannelWrapperTableRow());
	}

	private void setupColumn() {
		this.columnId.setCellValueFactory((cell) -> cell.getValue().idProperty());
		this.columnName.setCellValueFactory((cell) -> cell.getValue().nameProperty());
		this.columnEnabled.setCellValueFactory((cell) -> cell.getValue().enabledProperty());
		this.columnEnabled.setCellFactory((x) -> new UnfocusableCheckBoxTableCell<>());
		this.columnOnline.setCellValueFactory((cell) -> cell.getValue().onlineProperty());
		this.columnOnline.setCellFactory((x) -> new UnfocusableCheckBoxTableCell<>());
		this.columnDeleted.setCellFactory((x) -> new UnfocusableButtonTableCell<>(this::onButtonDelete));
	}

	private void refresh() {
		this.fetchAllChannelService.fetchAll();
	}

	private void sort() {
		this.tableView.sort();
		this.tableView.refresh();
	}

	private ChannelWrapper buildWrapper(Channel channel) {
		var channelWrapper = new ChannelWrapper(channel);
		channelWrapper.enabledProperty().addListener((observable, oldValue, newValue) -> {
			var form = UpdateChannelForm.builder().id(channel.id()).name(channel.name()).enabled(newValue).build();
			this.updateChannelService.update(form);
		});
		return channelWrapper;
	}

	public void onEnterKeyPressed() {
		this.onButtonAdd();
	}

	public void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.DELETE)) {
			var selectedItem = this.tableView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				log.info("Try to delete channel: {}", selectedItem);
				var form = DeleteChannelForm.builder().id(selectedItem.idProperty().getValue()).name(selectedItem.nameProperty().getValue()).build();
				this.deleteChannelRepository.delete(form);
			}
		}
		else if (keyEvent.getCode().equals(KeyCode.E)) {
			var selectedItem = this.tableView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				selectedItem.enabledProperty().set(!selectedItem.enabledProperty().get());
			}
		}
	}

	public void onButtonAdd() {
		var channelName = this.textFieldChannelName.getText();
		if (channelName.isBlank()) {
			this.statusController.setLabelText("Channel channelName is empty", true);
			return;
		}
		log.info("Try to add channel: {}", channelName);
		var form = CreateChannelForm.builder().name(channelName).build();
		this.createChannelRepository.create(form);
	}

	private void onButtonDelete(ChannelWrapper channel) {
		log.info("Try to delete channel: {}", channel);
		var form = DeleteChannelForm.builder().id(channel.idProperty().getValue()).name(channel.nameProperty().getValue()).build();
		this.deleteChannelRepository.delete(form);
	}

	public void onChannelFindEvent(ChannelFetchedAllEvent event) {
		log.info("Channels found [size]: {}", event.value().size());
		var wrappers = event.value().stream().map(this::buildWrapper).toList();
		this.tableView.getItems().clear();
		this.tableView.getItems().addAll(wrappers);
		sort();
	}

	public void onChannelCreatedEvent(ChannelCreatedEvent event) {
		log.info("Channel created: {}", event.value());
		this.tableView.getItems().add(buildWrapper(event.value()));
		sort();
		this.statusController.setLabelText("Channel created: " + event.value(), false);
	}

	public void onChannelUpdatedEvent(ChannelUpdatedEvent event) {
		log.info("Channel updated: {}", event.getValue());
		var channel = event.getValue();
		var wrapper = this.tableView.getItems().stream().filter((item) -> item.idProperty().getValue().equals(channel.id())).findFirst().orElseThrow();
		wrapper.enabledProperty().set(channel.enabled());
		wrapper.onlineProperty().set(channel.online());
		this.statusController.setLabelText("Channel updated: " + event.getValue(), false);
	}

	public void onChannelDeletedEvent(ChannelDeletedEvent event) {
		log.info("Channel deleted: {}", event.getValue());
		this.tableView.getItems().removeIf((item) -> item.idProperty().getValue().equals(event.getValue().id()));
		sort();
		this.statusController.setLabelText("Channel deleted: " + event.getValue(), false);
	}

	@Override
	public void fetchAll(ChannelFetchedAllEvent event) {
		onChannelFindEvent(event);
	}

	@Override
	public void create(ChannelCreatedEvent event) {
		onChannelCreatedEvent(event);
	}

}
