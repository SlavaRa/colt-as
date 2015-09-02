package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.model.monitor.ChangingMonitor
import codeOrchestra.colt.core.model.IModelElement
import codeOrchestra.colt.as.run.LiveMethods
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class LiveSettingsModel implements IModelElement{
    String liveType = LiveMethods.ANNOTATED.preferenceValue
    boolean startSessionPaused = false
    boolean makeGSLive = false
    int maxLoop = 10000

    LiveSettingsModel() {
        ChangingMonitor monitor = ChangingMonitor.instance
        monitor.addAll(
                liveType(),
                startSessionPaused(),
                makeGSLive(),
                maxLoop()
        )
    }

    @Override
    Closure buildXml(Project project) {
        return {
            'live-type'(liveType)
            'paused'(startSessionPaused)
            'make-gs-live'(makeGSLive)
            'max-loop'(maxLoop)
        }
    }

    @Override
    void buildModel(Object node) {
        liveType = node.'live-type'
        startSessionPaused = node.'paused' == "true"
        makeGSLive = node.'make-gs-live' == "true"
        maxLoop = "" + node.'max-loop' as Integer
    }
}
