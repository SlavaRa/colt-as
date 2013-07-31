package codeOrchestra.colt.as.model.beans

import codeOrchestra.colt.as.model.IModelElement
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * Created with IntelliJ IDEA.
 * User: dimakruk
 * Date: 7/25/13
 * Time: 9:07 AM
 * To change this template use File | Settings | File Templates.
 */
@Canonical
@FXBindable
class BuildModel implements IModelElement {
    String mainClass
    String outputFileName
    String outputPath
    String targetPlayerVersion
    boolean rsl

    boolean nonDefaultLocale
    String localeSettings

    boolean excludeDeadCode

    boolean interrupt
    String interruptValue = "30"

    String compilerOptions

    @Override
    Closure buildXml() {
        return {
            'main-class'(mainClass)
            'output-name'(outputFileName)
            'output-path'(outputPath)
            'player-version'(targetPlayerVersion)
            'is-rsl'(rsl)

            'custom-locale'(nonDefaultLocale)
            'locale'(localeSettings)

            'is-exclude'(excludeDeadCode)

            'is-interrupt'(interrupt)
            'interrupt-value'(interruptValue)

            'compiler-options'(compilerOptions)
        }
    }

    @Override
    void buildModel(Object node) {
        mainClass = node.'main-class'
        outputFileName = node.'output-name'
        outputPath = node.'output-path'
        targetPlayerVersion = node.'player-version'
        rsl = node.'is-rsl' == "true"

        nonDefaultLocale = node.'custom-locale' == "true"
        localeSettings = node.'locale'

        excludeDeadCode = node.'is-exclude' == "true"

        interrupt = node.'is-interrupt' == "true"
        interruptValue = node.'interrupt-value'

        compilerOptions = node.'compiler-options'
    }
}
