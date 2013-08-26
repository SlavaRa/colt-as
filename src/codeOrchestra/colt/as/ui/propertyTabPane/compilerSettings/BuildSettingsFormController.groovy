package codeOrchestra.colt.as.ui.propertyTabPane.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.core.ui.components.inputForms.CBForm
import codeOrchestra.colt.core.ui.components.inputForms.CTBForm
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.Initializable

/**
 * @author Dima Kruk
 */
class BuildSettingsFormController implements Initializable {

//    @FXML LTBForm mainClass
    @FXML LTBForm fileName
    @FXML LTBForm outPath

    @FXML CBForm player

    @FXML CTBForm rsl
    @FXML CTBForm locale
    @FXML CTBForm exclude
    @FXML CTBForm interrupt

    BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel
    SDKModel sdkModel = ModelStorage.instance.project.projectBuildSettings.sdkModel

    @Override
    void initialize(URL url, ResourceBundle resourceBundle) {
//        mainClass.extensionFilters.addAll(new FileChooser.ExtensionFilter("AS", "*.as"), new FileChooser.ExtensionFilter("MXML", "*.mxml"))

        player.errorLabel.visible = false

        bindModel()
    }

    void bindModel() {
//        mainClass.textField.textProperty().bindBidirectional(model.mainClass())
        fileName.textField.textProperty().bindBidirectional(model.outputFileName())
        outPath.textField.textProperty().bindBidirectional(model.outputPath())

        player.choiceBox.valueProperty().bindBidirectional(model.targetPlayerVersion())
        model.targetPlayerVersion().addListener({ ObservableValue<? extends String> observableValue, String t, String t1 ->
            //model.targetPlayerVersion can't be empty
            if (t1?.isEmpty()) {
                model.targetPlayerVersion = t
            }   
        } as ChangeListener)
        player.choiceBox.valueProperty().addListener({ ObservableValue<? extends String> observableValue, String t, String t1 ->
            if(t1 != null && !t1?.isEmpty()) {
                if(!player.choiceBox.items.contains(t1)) {
                    player.choiceBox.items.add(t1)
                    error(true)
                } else {
                    if(player.errorLabel.visible) {
                        player.choiceBox.items.remove(t)
                    }
                    error(false)
                }
            } else {
                error(false)
            }

        } as ChangeListener)

        sdkModel.isValidFlexSDK().addListener({ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue->
            String modelValue = model.targetPlayerVersion
            player.choiceBox.items.clear()
            if (newValue) {
                FlexSDKManager manager = FlexSDKManager.instance
                List<String> versions = manager.getAvailablePlayerVersions(new File(sdkModel.flexSDKPath))
                player.choiceBox.items.addAll(versions)
                if(modelValue != null && !modelValue.isEmpty()) {
                    if (versions.contains(modelValue)) {
                        model.targetPlayerVersion = modelValue
                        error(false)
                    } else {
                        player.choiceBox.items.add(modelValue)
                        model.targetPlayerVersion = modelValue
                        error(true)
                    }
                } else {
                    model.targetPlayerVersion = versions.first()
                    error(false)
                }
            } else {
                player.choiceBox.items.add(modelValue)
                model.targetPlayerVersion = modelValue
                error(true)
            }
        } as ChangeListener<Boolean>)

        rsl.checkBox.selectedProperty().bindBidirectional(model.rsl())

        locale.checkBox.selectedProperty().bindBidirectional(model.nonDefaultLocale())
        locale.textField.textProperty().bindBidirectional(model.localeSettings())

        exclude.checkBox.selectedProperty().bindBidirectional(model.excludeDeadCode())

        interrupt.checkBox.selectedProperty().bindBidirectional(model.interrupt())
        interrupt.textField.textProperty().bindBidirectional(model.interruptValue())
    }

    private void error(boolean b) {
        player.errorLabel.visible = b
        player.errorLabel.text = sdkModel.isValidFlexSDK ? "Incorrect player version specified" : "Incorrect Flex SDK path specified"
    }
}
