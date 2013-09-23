package codeOrchestra.colt.as.model

import codeOrchestra.colt.as.compiler.fcsh.FSCHCompilerKind
import codeOrchestra.colt.as.flexsdk.FlexSDKManager
import codeOrchestra.colt.core.ColtProjectManager
import codeOrchestra.colt.core.model.Project
import codeOrchestra.colt.core.storage.ProjectStorageManager
import codeOrchestra.util.StringUtils

/**
 * @author Dima Kruk
 */
class AsProject extends Project {

    private final AsProjectPaths projectPaths = new AsProjectPaths()
    private final AsProjectBuildSettings buildSettings = new AsProjectBuildSettings()
    private final AsProjectLiveSettings liveSettings = new AsProjectLiveSettings()

    @Override
    AsProjectPaths getProjectPaths() {
        return projectPaths
    }

    @Override
    AsProjectLiveSettings getProjectLiveSettings() {
        return liveSettings
    }

    @Override
    AsProjectBuildSettings getProjectBuildSettings() {
        return buildSettings
    }

    @Override
    String getProjectType() {
        return "AS"
    }

    Closure buildXml() {
        return {
            paths(projectPaths.buildXml(this))
            build(buildSettings.buildXml(this))
            live(liveSettings.buildXml(this))
        }
    }

    void buildModel(Object node) {
        super.buildModel(node)

        projectPaths.buildModel(node.paths)
        buildSettings.buildModel(node.build)
        liveSettings.buildModel(node.live)
    }

    static AsProject getCurrentProject() {
        return (AsProject) ColtProjectManager.instance.currentProject
    }

    File getOrCreateIncrementalSourcesDir() {
        File incrementalSourcesDir = new File(ProjectStorageManager.getOrCreateProjectStorageDir(), "incremental")
        if (!incrementalSourcesDir.exists()) {
            incrementalSourcesDir.mkdir()
        }
        return incrementalSourcesDir
    }

    File getLinkReportFile() {
        return new File(getOutputDir(), "link-report.xml")
    }

    File getOutputDir() {
        String outputPath = buildSettings.getOutputPath()
        if (StringUtils.isEmpty(outputPath)) {
            return getDefaultOutputDir()
        }
        return new File(outputPath);
    }

    File getDefaultOutputDir() {
        return new File(ProjectStorageManager.getOrCreateProjectStorageDir(), "colt_output")
    }

    File getDigestsDir() {
        return new File(ProjectStorageManager.getOrCreateProjectStorageDir(), "digests")
    }

    File getIncrementalOutputDir() {
        new File(ProjectStorageManager.getOrCreateProjectStorageDir(), "livecoding")
    }

    void initPaths() {
        File outputDir = getOutputDir();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File outputIncrementalDir = getIncrementalOutputDir();
        if (!outputIncrementalDir.exists()) {
            outputIncrementalDir.mkdirs();
        }

        File digestsDir = getDigestsDir();
        if (!digestsDir.exists()) {
            digestsDir.mkdirs();
        }
    }

    String getFlexConfigPath(FSCHCompilerKind compilerKind) {
        return new File(ProjectStorageManager.getOrCreateProjectStorageDir(), getName() + "_" + compilerKind.getCommandName() + "_flex_config.xml").getPath()
    }

    void initDefaultValues() {
        buildSettings.flexSDKPath = codeOrchestra.colt.as.util.ASPathUtils.flexSDKPath
        buildSettings.setUseDefaultSDKConfiguration(true);
        FlexSDKManager manager = FlexSDKManager.instance
        List<String> versions = manager.getAvailablePlayerVersions(new File(buildSettings.flexSDKPath))
        buildSettings.buildModel.targetPlayerVersion = versions.first()
        buildSettings.sdkModel.isValidFlexSDK = true

        liveSettings.launcherType = codeOrchestra.colt.as.run.LauncherType.DEFAULT;
        liveSettings.liveMethods = codeOrchestra.colt.as.run.LiveMethods.ANNOTATED;

        buildSettings.runTargetModel.iosAirModel.descriptorModel.outputPath = new File(path).parent + File.separator + "template"
        buildSettings.runTargetModel.iosAirModel.descriptorModel.outputFileName = name + "-app.xml"
        buildSettings.runTargetModel.iosAirModel.descriptorModel.name = name
        buildSettings.runTargetModel.iosAirModel.descriptorModel.id = "com." + name

        buildSettings.runTargetModel.androidAirModel.descriptorModel.outputPath = new File(path).parent + File.separator + "template"
        buildSettings.runTargetModel.androidAirModel.descriptorModel.outputFileName = name + "-app.xml"
        buildSettings.runTargetModel.androidAirModel.descriptorModel.name = name
        buildSettings.runTargetModel.androidAirModel.descriptorModel.id = "com." + name

    }
}
