package codeOrchestra.colt.as.run.indexhtml;

import codeOrchestra.colt.as.model.COLTAsProject;
import codeOrchestra.colt.as.util.ASPathUtils;
import codeOrchestra.util.FileUtils;
import codeOrchestra.util.templates.TemplateProcessor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Eliseyev
 */
public class IndexHTMLGenerator {

  private COLTAsProject project;

  public IndexHTMLGenerator(COLTAsProject project) {
    this.project = project;
  }

  public void generate() throws IOException {
    File targetSWFObjectFile = new File(project.getOutputDir(), "swfobject.web");
    FileUtils.copyFileChecked(new File(ASPathUtils.getTemplatesDir(), "swfobject.web"), targetSWFObjectFile, false);
    
    File targetIndexFile = new File(project.getOutputDir(), "index.html");
    FileUtils.copyFileChecked(new File(ASPathUtils.getTemplatesDir(), "index.html"), targetIndexFile, false);
    
    Map<String, String> replacements = new HashMap<String, String>();
    replacements.put("{SWF_NAME}", project.getProjectBuildSettings().getOutputFilename());
    new TemplateProcessor(targetIndexFile, replacements).process();
  }
  
}
