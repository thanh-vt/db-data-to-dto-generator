package vn.thanhvt;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.File;
import java.util.Arrays;

public class MainController {

    private static final String DEFAULT_FILE_LABEL = "No file selected";

    private final FileChooser fileChooser = new FileChooser();

    private final MainService mainService = new MainService();

    @FXML
    private Button browseFileBtn;

    @FXML
    private Button clearFileBtn;

    @FXML
    private Label fileLabel;

    @FXML
    private Tooltip fileLabelTooltip;

    @FXML
    private CheckBox isSpringCheckbox;

    @FXML
    private Button loadBtn;

    @FXML
    private Button unloadBtn;

    @FXML
    private ProgressIndicator loadingProgress;

    @FXML
    private TextField classNameInput;

    @FXML
    private ChoiceBox<JsonNamingStrategy> jsonNamingChoiceBox;

    @FXML
    private CheckBox ignoreNullCheckbox;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    private File selectedFile;

    @FXML
    public void initialize() {
        this.fileChooser.setTitle("Select jar file");
        this.fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        this.fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All file", "*.jar", "*.war"),
                new FileChooser.ExtensionFilter("Java Archive", "*.jar"),
                new FileChooser.ExtensionFilter("Web Archive", "*.war")
        );
        this.clearFile(null);
        this.jsonNamingChoiceBox.setConverter(new StringConverter<JsonNamingStrategy>() {

            @Override
            public String toString(JsonNamingStrategy object) {
                return object.getTitle();
            }

            @Override
            public JsonNamingStrategy fromString(String string) {
                return Arrays.stream(JsonNamingStrategy.values())
                        .filter(e -> e.getTitle().equals(string))
                        .findFirst()
                        .orElse(JsonNamingStrategy.CAMEL_CASE);
            }
        });
        ObservableList<JsonNamingStrategy> itemList = FXCollections.observableArrayList();
        itemList.addAll(JsonNamingStrategy.values());
        this.jsonNamingChoiceBox.setItems(itemList);
        this.jsonNamingChoiceBox.setValue(JsonNamingStrategy.SNAKE_CASE);
    }

    @FXML
    void clearFile(ActionEvent event) {
        this.fileLabel.setText(DEFAULT_FILE_LABEL);
        this.fileLabelTooltip.setText(DEFAULT_FILE_LABEL);
        this.selectedFile = null;
        this.loadBtn.setDisable(true);
        this.unloadBtn.setDisable(true);
    }

    @FXML
    public void openFileBrowser(ActionEvent event) {
        File selectedFile = fileChooser.showOpenDialog(App.mainStage);
        if (selectedFile != null) {
            this.fileLabel.setText(selectedFile.getAbsolutePath());
            this.fileLabelTooltip.setText(selectedFile.getAbsolutePath());
            System.out.printf("File loaded: %s%n", selectedFile.getAbsolutePath());
            this.selectedFile = selectedFile;
        }
        this.loadBtn.setDisable(selectedFile == null);
        this.unloadBtn.setDisable(true);
    }

    @FXML
    void hideTooltip(MouseEvent event) {
        this.fileLabelTooltip.hide();
    }

    @FXML
    void showTooltip(MouseEvent event) {
        this.fileLabelTooltip.show(App.mainStage);
    }

    @FXML
    void loadClasses(ActionEvent event) {
        Util.loading(this.loadingProgress, () -> {
            this.mainService.applyConfig(this.selectedFile, this.isSpringCheckbox.isSelected());
            this.setLoaded(true);
        }, (e) -> {
            Util.showError(e);
            this.setLoaded(false);
        });
    }

    @FXML
    void unloadClasses(ActionEvent event) {
        Util.loading(this.loadingProgress, () -> {
            this.mainService.clearConfig();
            this.setLoaded(false);
        }, (e) -> {
            Util.showError(e);
            this.setLoaded(false);
        });
    }

    void setLoaded(boolean isLoaded) {
        this.isSpringCheckbox.setDisable(isLoaded);
        this.loadBtn.setDisable(isLoaded);
        this.unloadBtn.setDisable(!isLoaded);
        this.browseFileBtn.setDisable(isLoaded);
        this.clearFileBtn.setDisable(isLoaded);
    }

    @FXML
    void generate(ActionEvent event) {
        try {
            String outputText = this.mainService.generate(this.classNameInput.getText(),
                    this.inputTextArea.getText(),
                    this.jsonNamingChoiceBox.getSelectionModel().getSelectedItem(),
                    this.ignoreNullCheckbox.isSelected());
            this.outputTextArea.setText(outputText);
        } catch (Exception e) {
            Util.showError(e);
        }
    }

}
