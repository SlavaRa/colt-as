package codeOrchestra.colt.as.ui.settingsForm.compilerSettings

import codeOrchestra.colt.as.model.ModelStorage
import codeOrchestra.colt.as.model.beans.BuildModel
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import codeOrchestra.colt.core.ui.components.inputFormsNew.LInputForm

/**
 * @author Dima Kruk
 */
class CompilerSettingsForm extends FormGroup{
    private LInputForm options

    private BuildModel model = ModelStorage.instance.project.projectBuildSettings.buildModel

    CompilerSettingsForm() {
        options = new LInputForm(title: "Additional compiler options:")

        children.add(options)

        init()
    }

    void init() {
        bindModel()
    }

    void bindModel() {
        options.text().bindBidirectional(model.compilerOptions())
    }
}
