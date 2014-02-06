package codeOrchestra.colt.as;

import codeOrchestra.colt.as.compiler.fcsh.FCSHManager;
import codeOrchestra.colt.as.controller.ColtAsController;
import codeOrchestra.colt.as.facade.AsColtFacade;
import codeOrchestra.colt.as.logging.transport.LoggerServerSocketThread;
import codeOrchestra.colt.as.model.AsProject;
import codeOrchestra.colt.as.model.ModelStorage;
import codeOrchestra.colt.as.model.util.ProjectImporter;
import codeOrchestra.colt.as.rpc.impl.ColtAsRemoteServiceImpl;
import codeOrchestra.colt.as.run.ASLiveLauncher;
import codeOrchestra.colt.as.session.sourcetracking.ASSourceFileFactory;
import codeOrchestra.colt.as.ui.ASApplicationGUI;
import codeOrchestra.colt.as.util.ASPathUtils;
import codeOrchestra.colt.core.AbstractLiveCodingLanguageHandler;
import codeOrchestra.colt.core.ColtProjectManager;
import codeOrchestra.colt.core.LiveCodingManager;
import codeOrchestra.colt.core.ServiceProvider;
import codeOrchestra.colt.core.controller.ColtController;
import codeOrchestra.colt.core.facade.ColtFacade;
import codeOrchestra.colt.core.gradle.GradleTaskManager;
import codeOrchestra.colt.core.http.CodeOrchestraResourcesHttpServer;
import codeOrchestra.colt.core.launch.LiveLauncher;
import codeOrchestra.colt.core.logging.LoggerService;
import codeOrchestra.colt.core.model.Project;
import codeOrchestra.colt.core.model.listener.ProjectAdapter;
import codeOrchestra.colt.core.rpc.ColtRemoteService;
import codeOrchestra.colt.core.session.SocketWriterAdapter;
import codeOrchestra.colt.core.session.sourcetracking.SourceFileFactory;
import codeOrchestra.colt.core.ui.components.FxThreadProgressIndicatorWrapper;
import codeOrchestra.colt.core.ui.components.IProgressIndicator;
import codeOrchestra.colt.core.ui.components.ProgressIndicatorController;
import codeOrchestra.colt.core.ui.components.sessionIndicator.SessionIndicatorController;
import codeOrchestra.util.PathUtils;
import codeOrchestra.util.StringUtils;
import groovy.util.slurpersupport.GPathResult;
import javafx.scene.Node;

import java.io.File;

/**
 * @author Alexander Eliseyev
 */
public class ASLiveCodingLanguageHandler extends AbstractLiveCodingLanguageHandler<AsProject> {

    private final ProjectAdapter projectListener = new ProjectAdapter() {
        @Override
        public void onProjectLoaded(Project project) {
            CodeOrchestraResourcesHttpServer.getInstance().addAlias(project.getOutputDir(), "/colt");
        }
    };
    private LoggerServerSocketThread loggerServerSocketThread = new LoggerServerSocketThread();

    private LoggerService loggerService;

    private ASApplicationGUI applicationGUI;

    @Override
    public String getId() {
        return "AS";
    }

    @Override
    public String getName() {
        return "ActionScript";
    }

    @Override
    public AsProject parseProject(GPathResult gPathResult, String projectPath) {
        AsProject project = ModelStorage.getInstance().getProject();
        project.setPath(projectPath);

        project.buildModel(gPathResult);

        // Default settings
        if (StringUtils.isEmpty(project.getProjectBuildSettings().sdkModel.getFlexSDKPath())) {
            project.getProjectBuildSettings().sdkModel.setFlexSDKPath(ASPathUtils.getFlexSDKPath());
        }

        // Prepare dirs
        project.initPaths();

        return project;
    }

    @Override
    public AsProject createProject(String pName, File pFile, boolean load) {
        AsProject project = load ? ModelStorage.getInstance().getProject() : new AsProject();

        project.setName(pName);
        project.setPath(pFile.getPath());
        project.initDefaultValues();

        // Prepare dirs
        project.initPaths();

        return project;
    }

    @Override
    public AsProject importProject(File file) {
        AsProject project = ProjectImporter.importProject(file);

        // Prepare dirs
        project.initPaths();

        return project;
    }

    @Override
    public AsProject getCurrentProject() {
        return ModelStorage.getInstance().getProject();
    }

    @Override
    public void initHandler() {
        loggerServerSocketThread.openSocket();

        CodeOrchestraResourcesHttpServer.getInstance().addAlias(PathUtils.getApplicationBaseDir(), "/");
        ServiceProvider.get(LiveCodingManager.class).addListener(SessionIndicatorController.getInstance());
        ColtProjectManager.getInstance().addProjectListener(projectListener);
    }

    @Override
    public void disposeHandler() {
        loggerServerSocketThread.closeSocket();
        FCSHManager.instance().destroyProcess();

        ServiceProvider.get(LiveCodingManager.class).removeListener(SessionIndicatorController.getInstance());
        ColtProjectManager.getInstance().removeProjectListener(projectListener);
    }

    @Override
    public int getDemoModeMaxUpdatesCount() {
        return 3;
    }

    @Override
    public LoggerService getLoggerService() {
        return loggerService;
    }

    public void setLoggerService(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    @Override
    public Node getPane() throws Exception {
        if (applicationGUI == null) {
            applicationGUI = new ASApplicationGUI();
        }
        return applicationGUI;
    }

    @Override
    public IProgressIndicator getProgressIndicator() {
        return new FxThreadProgressIndicatorWrapper(ProgressIndicatorController.getInstance());
    }

    @Override
    public ColtController<AsProject> createColtController() {
        return new ColtAsController();
    }

    @Override
    public ColtRemoteService<AsProject> createRPCService() {
        return new ColtAsRemoteServiceImpl();
    }

    @Override
    public LiveLauncher<AsProject> createLauncher() {
        return new ASLiveLauncher();
    }

    @Override
    public LiveCodingManager<AsProject, SocketWriterAdapter> createLiveCodingManager() {
        return new ASLiveCodingManager();
    }

    @Override
    public SourceFileFactory createSourceFileFactory() {
        return new ASSourceFileFactory();
    }

    @Override
    public ColtFacade createColtFacade() {
        return new AsColtFacade(applicationGUI);
    }

    @Override
    public GradleTaskManager<AsProject> createGradleTaskManager() {
        throw new UnsupportedOperationException();
    }
}
