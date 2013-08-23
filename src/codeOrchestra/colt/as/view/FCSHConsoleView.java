package codeOrchestra.colt.as.view;

import codeOrchestra.colt.core.logging.Logger;

/**
 * @author Alexander Eliseyev
 */
public class FCSHConsoleView {

    private static Logger logger = Logger.getLogger("fcsh");

    private static FCSHConsoleView sharedInstance;

    public synchronized static FCSHConsoleView get() {
        if (sharedInstance == null) {
            sharedInstance = new FCSHConsoleView();
        }
        return sharedInstance;
    }

    public synchronized void write(String s) {
        logger.compile(s);
    }

}
