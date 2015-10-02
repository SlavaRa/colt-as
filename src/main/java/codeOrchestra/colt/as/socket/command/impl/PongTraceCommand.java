package codeOrchestra.colt.as.socket.command.impl;

import codeOrchestra.colt.as.socket.command.TraceCommand;
import codeOrchestra.colt.core.logging.model.LoggerMessage;
import codeOrchestra.colt.core.socket.ClientSocketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public final class PongTraceCommand implements TraceCommand {

  private static PongTraceCommand instance;
  public synchronized static PongTraceCommand getInstance() {
    if (instance == null) {
      instance = new PongTraceCommand();
    }
    return instance;
  }

  private static final String PONG_COMMAND = "pong";

  private List<PongListener> pongListeners = new ArrayList<>();

  private PongTraceCommand() {
  }

  @Override
  public boolean isApplicable(LoggerMessage message) {
    return PONG_COMMAND.equals(message.getCommand());
  }

  @Override
  public void execute(LoggerMessage message, ClientSocketHandler clientSocketHandler) {
    synchronized (this) {
      pongListeners.forEach(PongListener::pong);
    }
  }

  public synchronized void addPongListener(PongListener listener) {
    pongListeners.add(listener);
  }

  public synchronized void removePongListener(PongListener listener) {
    pongListeners.remove(listener);
  }

  public interface PongListener {
    void pong();
  }

}
