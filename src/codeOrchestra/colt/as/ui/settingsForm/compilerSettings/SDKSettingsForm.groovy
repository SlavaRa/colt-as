package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.as.flexsdk.FlexSDKNotPresentException
import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.RunTargetModel
import codeOrchestra.colt.as.run.Target
import codeOrchestra.colt.as.model.beans.SDKModel
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated

import codeOrchestra.colt.core.ui.components.inputForms.base.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxActionInput
import codeOrchestra.colt.core.ui.components.inputForms.CheckBoxInput
import codeOrchestra.colt.core.ui.components.inputForms.LabeledActionInput
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.Parent
import javafx.stage.FileChooser

/**
 * @author Dima Kruk
 */
class SDKSettingsForm extends FormGroup implements IFormValidated{

    private LabeledActionInput sdkPath
    private CheckBoxInput defConf
    private CheckBoxActionInput customConf

    private SDKModel model = ModelStorage.instance.project.projectBuildSettings.sdkModel
    private RunTargetModel targetModel = ModelStorage.instance.project.projectBuildSettings.runTargetModel

    SDKSettingsForm() {
        sdkPath = new LabeledActionInput(title: "Flex SDK Path:", browseType: BrowseType.DIRECTORY)
        defConf = new CheckBoxInput(title: "Use default SDK compiler configuration file")
        customConf = new CheckBoxActionInput(title: "Use custom compiler configuration file", shortPathForProject: ModelStorage.instance.project)

        children.addAll(sdkPath, defConf, customConf)

        init()
    }

    private void validateSDK(String path) {
        FlexSDKManager manager = FlexSDKManager.instance
        try {
            manager.checkIsValidFlexSDKPath(path)
            if (targetModel.runTarget == Target.AIR_ANDROID || targetModel.runTarget == Target.AIR_IOS || targetModel.runTarget == Target.AIR_DESKTOP) {
                model.isValidFlexSDK = manager.isAirPresent(path)
            } else {
                model.isValidFlexSDK = true
            }
        } catch (FlexSDKNotPresentException ignored) {
            model.isValidFlexSDK = false
        }
    }

    public void init() {

        customConf.extensionFilters.add(new FileChooser.ExtensionFilter("XML", "*.xml"))

        model.flexSDKPath().addListener({ ObservableValue<? extends String> observable, String oldValue, String newValue ->
            validateSDK(newValue)
        } as ChangeListener<String>)

        targetModel.target().addListener({ javafx.beans.Observable observable ->
            validateSDK(model.flexSDKPath)
        } as InvalidationListener)

        model.isValidFlexSDK().addListener({ javafx.beans.Observable observable ->
            validated()
        } as InvalidationListener)

        bindModel()
    }

    void bindModel() {
        sdkPath.text().bindBidirectional(model.flexSDKPath())
        defConf.selected().bindBidirectional(model.useFlexConfig())
        customConf.selected().bindBidirectional(model.useCustomConfig())
        customConf.text().bindBidirectional(model.customConfigPath())
    }

    @Override
    Parent validated() {
        Parent result = customConf.validateValue() ? customConf : null

        if (model.isValidFlexSDK) {
            sdkPath.error = false
        } else {
            sdkPath.error = true
            result = sdkPath
        }
        return result
    }
}
