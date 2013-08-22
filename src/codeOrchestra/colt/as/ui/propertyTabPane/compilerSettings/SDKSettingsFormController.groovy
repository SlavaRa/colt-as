package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.flexsdk.FlexSDKNotPresentException
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.ui.components.CTBForm
import codeOrchestra.colt.as.ui.components.LTBForm
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class SDKSettingsFormController implements Initializable {

    @FXML LTBForm sdkPath
    @FXML CTBForm defConf
    @FXML CTBForm customConf

    public SDKModel model = ModelStorage.instance.project.projectBuildSettings.sdkModel

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customConf.extensionFilters.add(new FileChooser.ExtensionFilter("XML", "*.xml"))

        model.flexSDKPath().addListener({ ObservableValue<? extends String> observable, String oldValue, String newValue ->
            FlexSDKManager manager = FlexSDKManager.instance
            try {
                manager.checkIsValidFlexSDKPath(newValue)
                model.isValidFlexSDK = true
            } catch (FlexSDKNotPresentException ignored) {
                model.isValidFlexSDK = false
            }
        } as ChangeListener<String>)

        bindModel()
    }

    void bindModel() {
        sdkPath.textField.textProperty().bindBidirectional(model.flexSDKPath())
        defConf.checkBox.selectedProperty().bindBidirectional(model.useFlexConfig())
        customConf.checkBox.selectedProperty().bindBidirectional(model.useCustomConfig())
        customConf.textField.textProperty().bindBidirectional(model.customConfigPath())
    }
}
