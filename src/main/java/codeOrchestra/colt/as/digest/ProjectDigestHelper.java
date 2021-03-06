package codeOrchestra.colt.as.digest;

import codeOrchestra.colt.as.flexsdk.FlexSDKLib;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.core.logging.Logger;
import codeOrchestra.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexander Eliseyev
 */
public class ProjectDigestHelper {

  private final static Logger LOG = Logger.getLogger(ProjectDigestHelper.class);
  
  private final AsProject project;

  public ProjectDigestHelper(AsProject project) {
    this.project = project;    
  }
  
  public List<EmbedDigest> getEmbedDigests() {
    List<EmbedDigest> result = new ArrayList<>();
    
    File embedDigestsFile = new File(AsProject.getDigestsDir(), "embedDigests.xml");
    if (!embedDigestsFile.exists()) {
      LOG.error("Embed digests report file expected at " + embedDigestsFile.getPath() + " doesn't exist");
      return result;
    }

      Document document;
      try {
          document = XMLUtils.fileToDOM(embedDigestsFile);
      } catch (Throwable t) {
          throw new RuntimeException("Can't parse digests file " + embedDigestsFile.getPath(), t);
      }
      Element rootElement = document.getDocumentElement();
    
    NodeList embedElements = rootElement.getElementsByTagName("embed");
    if (embedElements != null) {
      for (int i = 0; i < embedElements.getLength(); i++) {
        Element embedElement = (Element) embedElements.item(i);
        result.add(new EmbedDigest(embedElement.getAttribute("source"), embedElement.getAttribute("mimeType"), embedElement.getAttribute("fullPath")));
      }
    }
    
    return result;
  }
  
  public void build() throws DigestException {
    long timeStarted = System.currentTimeMillis();
    
    List<String> swcPaths = new ArrayList<>();
    for (FlexSDKLib flexLib : FlexSDKLib.values()) {
      String swcPath = flexLib.getPath();
      if (swcPath != null) {
        swcPaths.add(swcPath);
      }
    }

    swcPaths.addAll(project.getProjectPaths().getLibraryPaths().stream().collect(Collectors.toList()));
    
    SWCDigest swcDigest = new SWCDigest(swcPaths, AsProject.getDigestsDir().getPath());
    swcDigest.generate();
    
    LOG.info("Digests building took " + (System.currentTimeMillis() - timeStarted) + "ms");
  }
  
}