package codeOrchestra.colt.as.model.beans.air

import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class AirDesktopLauncherModel implements IModelElement {
    String adlOptions = ""

    AirDesktopLauncherModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(adlOptions())
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'adl-options'(adlOptions)
        }
    }

    @Override
    void buildModel(Object node) {
        adlOptions = node.'adl-options'
    }
}
