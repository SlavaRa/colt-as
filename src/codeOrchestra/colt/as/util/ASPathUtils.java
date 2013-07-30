package codeOrchestra.colt.as.util;

import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.util.PathUtils;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class ASPathUtils {

    public static String getFlexSDKPath() {
        File productDir = PathUtils.getApplicationBaseDir();
        return new File(productDir, "flex_sdk").getPath();
    }

    public static String getTemplatesDir() {
        File productDir = PathUtils.getApplicationBaseDir();
        return new File(productDir, "templates").getPath();
    }

    public static String getColtSWCPath() {
        File productDir = PathUtils.getApplicationBaseDir();
        return new File(new File(productDir, "lib"), "colt.swc").getPath();
    }

    public static String getIncrementalSWFPath(COLTAsProject project, int packageId) {
        return getIncrementalOutputDir(project) + File.separator + "package_" + packageId + ".swf";
    }

    public static String getIncrementalOutputDir(COLTAsProject project) {
        return project.getOutputDir().getPath() + File.separator + "livecoding";
    }

    public static String getSourceIncrementalSWCPath(COLTAsProject project) {
        return project.getOutputDir().getPath() + File.separator + project.getName() + "_liveCoding.swc";
    }

    public static String getTargetIncrementalSWCPath(COLTAsProject project, int packageId) {
        return getIncrementalOutputDir(project) + File.separator + "package_" + packageId + ".swc";
    }

}
