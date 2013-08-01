package codeOrchestra.colt.as.model

import codeOrchestra.colt.as.compiler.fcsh.FSCHCompilerKind
import codeOrchestra.colt.core.COLTProjectManager
import codeOrchestra.colt.core.model.COLTProject
import codeOrchestra.groovyfx.FXBindable
import groovy.transform.Canonical

/**
 * @author Dima Kruk
 */
@Canonical
@FXBindable
class COLTAsProject extends COLTProject {

    private final COLTAsProjectPaths projectPaths = new COLTAsProjectPaths()
    private final COLTAsProjectBuildSettings buildSettings = new COLTAsProjectBuildSettings()
    private final COLTAsProjectLiveSettings liveSettings = new COLTAsProjectLiveSettings()

    @Override
    COLTAsProjectPaths getProjectPaths() {
        return projectPaths
    }

    @Override
    COLTAsProjectLiveSettings getProjectLiveSettings() {
        return liveSettings
    }

    @Override
    COLTAsProjectBuildSettings getProjectBuildSettings() {
        return buildSettings
    }

    @Override
    String getProjectType() {
        return "AS";
    }

    @Override
    Closure buildXml() {
        return {
            paths(projectPaths.buildXml())
            build(buildSettings.buildXml())
            live(liveSettings.buildXml())
        }
    }

    @Override
    void buildModel(Object node) {
        super.buildModel(node)

        projectPaths.buildModel(node.paths)
        buildSettings.buildModel(node.build)
        liveSettings.buildModel(node.live)
    }

    static COLTAsProject getCurrentProject() {
        return (COLTAsProject) COLTProjectManager.instance.currentProject;
    }

    public File getOrCreateIncrementalSourcesDir() {
        File incrementalSourcesDir = new File(getBaseDir(), "incremental");
        if (!incrementalSourcesDir.exists()) {
            incrementalSourcesDir.mkdir();
        }
        return incrementalSourcesDir;
    }

    public File getLinkReportFile() {
        return new File(getOutputDir(), "link-report.xml");
    }

    public File getOutputDir() {
        String outputPath = compilerSettings.getOutputPath();
        if (StringUtils.isEmpty(outputPath)) {
            return getDefaultOutputDir();
        }
        return new File(outputPath);
    }

    public File getDefaultOutputDir() {
        return new File(getBaseDir(), "colt_output");
    }

    public File getDigestsDir() {
        return new File(getBaseDir(), "digests");
    }

    public String getFlexConfigPath(FSCHCompilerKind compilerKind) {
        return new File(getBaseDir(), getName() + "_" + compilerKind.getCommandName() + "_flex_config.xml").getPath();
    }

}
