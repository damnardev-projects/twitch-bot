package fr.damnardev.twitch.bot.client.javafx.controller;

import fr.damnardev.twitch.bot.client.javafx.control.ChannelWrapperTableRow;
import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableButtonTableCell;
import fr.damnardev.twitch.bot.client.javafx.control.UnfocusableCheckBoxTableCell;
import fr.damnardev.twitch.bot.client.javafx.wrapper.ObservableChannel;
import fr.damnardev.twitch.bot.client.port.secondary.ChannelRepository;
import fr.damnardev.twitch.bot.model.DomainService;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.RequiredArgsConstructor;

@DomainService
@RequiredArgsConstructor
public class DashboardController {

	private final ChannelRepository channelRepository;

	private final ApplicationContext applicationContext;

	@FXML
	private TableColumn<ObservableChannel, String> columnName;

	@FXML
	private TableColumn<ObservableChannel, Number> columnId;

	@FXML
	private TableColumn<ObservableChannel, Boolean> columnEnabled;

	@FXML
	private TableColumn<ObservableChannel, Boolean> columnOnline;

	@FXML
	private TableColumn<ObservableChannel, String> columnDeleted;

	@FXML
	private TableView<ObservableChannel> tableView;

	@FXML
	private TextField textFieldChannelName;

	@FXML
	private void initialize() {
		setupTableView();
		setupColumn();
	}

	private void setupTableView() {
		this.tableView.setItems(this.applicationContext.getChannels());
		this.tableView.getSortOrder().add(this.columnId);
		this.tableView.setRowFactory((x) -> new ChannelWrapperTableRow());
		this.tableView.getItems().addListener((ListChangeListener<ObservableChannel>) (change) -> {
			if (change.next() && change.wasAdded()) {
				sort();
			}
		});
	}

	private void setupColumn() {
		this.columnId.setCellValueFactory((cell) -> cell.getValue().getId());
		this.columnName.setCellValueFactory((cell) -> cell.getValue().getName());
		this.columnEnabled.setCellValueFactory((cell) -> cell.getValue().getEnabled());
		this.columnEnabled.setCellFactory((x) -> new UnfocusableCheckBoxTableCell<>());
		this.columnOnline.setCellValueFactory((cell) -> cell.getValue().getOnline());
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
//		var form = CreateChannelForm.builder().name(channelName).build();
//		this.channelRepository.create(form);
	}

	private void onButtonDelete(ObservableChannel channel) {
		this.channelRepository.delete(channel.getId().get());
	}

	public void onKeyPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode().equals(KeyCode.DELETE)) {
			var selectedItem = this.tableView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				this.channelRepository.delete(selectedItem.getId().get());
			}
		}
		if (keyEvent.getCode().equals(KeyCode.E)) {
			var selectedItem = this.tableView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				selectedItem.getEnabled().set(!selectedItem.getEnabled().get());
			}
		}
	}

	private void sort() {
		this.tableView.sort();
		this.tableView.refresh();
	}

}
