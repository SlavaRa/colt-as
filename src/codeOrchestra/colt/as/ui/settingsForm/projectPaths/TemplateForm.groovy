package codeOrchestra.colt.as.ui.settingsForm.projectPaths

import codeOrchestra.colt.as.model.AsProjectPaths
import codeOrchestra.colt.as.ui.settingsForm.IFormValidated

import codeOrchestra.colt.core.ui.components.inputForms.BrowseType
import codeOrchestra.colt.core.ui.components.inputForms.FormType
import codeOrchestra.colt.core.ui.components.inputForms.LTBForm
import codeOrchestra.colt.core.ui.components.inputForms.group.FormGroup
import codeOrchestra.colt.core.ui.components.inputFormsNew.LActionFrom
import codeOrchestra.colt.core.ui.components.inputFormsNew.group.FormGroupNew
import javafx.scene.Parent

/**
 * @author Dima Kruk
 */
class TemplateForm extends FormGroupNew implements IFormValidated {
    private LActionFrom template

    private AsProjectPaths model = codeOrchestra.colt.as.model.ModelStorage.instance.project.projectPaths

    TemplateForm() {
        template = new LActionFrom(title: "HTML Template:", browseType: BrowseType.DIRECTORY)

        children.add(template)

        init()
    }

    void init() {
        template.activateValidation(true)
        template.text().bindBidirectional(model.htmlTemplatePath())
    }

    @Override
    Parent validated() {
        return template.validateValue() ? template : null
    }
}
