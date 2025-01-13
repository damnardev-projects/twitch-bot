package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.javafx.control.ChannelWrapperTableRow;
import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableButtonTableCell;
import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableCheckBoxTableCell;
import fr.damnardev.twitch.bot.client.javafx.wrapper.ChannelWrapper;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import fr.damnardev.twitch.bot.model.Channel;
import fr.damnardev.twitch.bot.model.DomainService;
import fr.damnardev.twitch.bot.model.event.ChannelCreatedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelDeletedEvent;
import fr.damnardev.twitch.bot.model.event.ChannelFetchedAllEvent;
import fr.damnardev.twitch.bot.model.event.ChannelUpdatedEvent;
import fr.damnardev.twitch.bot.model.form.CreateChannelForm;
import fr.damnardev.twitch.bot.model.form.DeleteChannelForm;
import fr.damnardev.twitch.bot.model.form.UpdateChannelForm;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class ChannelManagementController {

	private final ChannelRepository channelRepository;

	@FXML
	private TableColumn<ChannelWrapper, String> columnName;

	@FXML
	private TableColumn<ChannelWrapper, Number> columnId;

	@FXML
	private TableColumn<ChannelWrapper, Boolean> columnEnabled;

	@FXML
	private TableColumn<ChannelWrapper, Boolean> columnOnline;

	@FXML
	private TableColumn<ChannelWrapper, String> columnDeleted;

	@FXML
	private TableView<ChannelWrapper> tableView;

	@FXML
	private TextField textFieldChannelName;

	@FXML
	private void initialize() {
		setupTableView();
		setupColumn();
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

	public void onEnterKeyPressed() {
		addChannel();
	}

	public void onButtonAdd() {
		addChannel();
	}

	private void addChannel() {
		var channelName = this.textFieldChannelName.getText();
		var form = CreateChannelForm.builder().name(channelName).build();
		this.channelRepository.create(form);
	}

	private void onButtonDelete(ChannelWrapper channel) {
		var form = DeleteChannelForm.builder().id(channel.idProperty().getValue()).name(channel.nameProperty().getValue()).build();
		this.channelRepository.delete(form);
	}

	public void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.DELETE)) {
			var selectedItem = this.tableView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				var form = DeleteChannelForm.builder().id(selectedItem.idProperty().getValue()).name(selectedItem.nameProperty().getValue()).build();
				this.channelRepository.delete(form);
			}
		}
		if (keyEvent.getCode().equals(KeyCode.E)) {
			var selectedItem = this.tableView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				selectedItem.enabledProperty().set(!selectedItem.enabledProperty().get());
			}
		}
	}

	public void handleChannelFetchedAllEvent(ChannelFetchedAllEvent event) {
		if (this.tableView == null) {
			return;
		}
		var wrappers = event.value().stream().map(this::buildWrapper).toList();
		this.tableView.getItems().clear();
		this.tableView.getItems().addAll(wrappers);
		sort();
	}

	private ChannelWrapper buildWrapper(Channel channel) {
		var channelWrapper = new ChannelWrapper(channel);
		channelWrapper.enabledProperty().addListener((observable, oldValue, newValue) -> {
			var form = UpdateChannelForm.builder().id(channel.id()).name(channel.name()).enabled(newValue).build();
			this.channelRepository.update(form);
		});
		return channelWrapper;
	}

	private void sort() {
		this.tableView.sort();
		this.tableView.refresh();
	}

	public void handlerChannelCreatedEvent(ChannelCreatedEvent event) {
		var wrapper = buildWrapper(event.value());
		this.tableView.getItems().add(wrapper);
		sort();
	}

	public void handleChannelUpdatedEvent(ChannelUpdatedEvent event) {
		var channel = event.value();
		var wrapper = this.tableView.getItems().stream().filter((item) -> item.idProperty().getValue().equals(channel.id())).findFirst().orElseThrow();
		wrapper.enabledProperty().set(channel.enabled());
		wrapper.onlineProperty().set(channel.online());
	}

	public void handleChannelDeletedEvent(ChannelDeletedEvent event) {
		var channel = event.value();
		var wrapper = this.tableView.getItems().stream().filter((item) -> item.idProperty().getValue().equals(channel.id())).findFirst().orElseThrow();
		this.tableView.getItems().remove(wrapper);
		sort();
	}

}
