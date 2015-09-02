package codeOrchestra.colt.as.air

import codeOrchestra.colt.as.model.AsProject
import codeOrchestra.util.PathUtils
import codeOrchestra.util.SystemInfo
import codeOrchestra.util.templates.TemplateCopyUtil
import codeOrchestra.colt.as.model.beans.air.IOSAirModel

/**
 * @author Dima Kruk
 */
class AirIosIpaBuildScriptGenerator extends AirBuildScriptGenerator {

    AirIosIpaBuildScriptGenerator(AsProject project) {
        super(project)
    }

    @Override
    protected String getScriptFileName() {
        return SystemInfo.isWindows ? "airIosIpaBuild.bat" : "airIosIpaBuild.sh"
    }

    @Override
    String generate(Object airModel, List<File> packagedFiles) throws IOException {
        IOSAirModel model = airModel as IOSAirModel
        File targetScriptFile = getScriptPath(project)
        File templateFile = new File(PathUtils.getTemplatesDir(), getScriptFileName())

        Map<String, String> replacements = new HashMap<>()
        replacements.put("{AIR_SDK}", project.projectBuildSettings.flexSDKPath)
        replacements.put("{APPNAME}", appName)
        replacements.put("{PROJECT_DIR}", project.getBaseDir().getAbsolutePath())

        String outputDirPath = project.getOutputDir().getAbsolutePath()
        if (SystemInfo.isWindows && !outputDirPath.endsWith(File.separator)) {
            outputDirPath += File.separator
        }
        replacements.put("{OUTPUT_DIR}", outputDirPath)

        File outputDir = project.getOutputDir()
        replacements.put("{IPA_FILE}", appName + ".ipa")
        replacements.put("{DESCRIPTOR_FILE}", appName + "-app.xml")

        replacements.put("{provisioning-profile}", model.provisionPath)
        replacements.put("{keystore}", model.keystorePath)
        replacements.put("{storepass}", model.storePass)

        String packaged = ""

        for (File currFile : packagedFiles) {
            String relative = new File(outputDir.getAbsolutePath()).toURI()
                    .relativize(new File(currFile.getAbsolutePath()).toURI()).getPath()
            packaged += "\"" + relative + "\" "
        }

        replacements.put("{PACKAGED_FILES}", packaged)

        TemplateCopyUtil.copy(templateFile, targetScriptFile, replacements)
        targetScriptFile.setExecutable(true)

        Map<String, String> descReplacements = new HashMap<>()
        descReplacements.put("{APPNAME}", appName)

        descReplacements.put("{OUTPUT_FILE}", project.getProjectBuildSettings().outputFilename)

        return targetScriptFile.getPath()
    }
}
