package codeOrchestra.colt.as.cli;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;

/**
 * Created by slavara on 26.10.2015.
 */
public class ColtCLI implements Runnable {

    public static void main(String[] args) {
        ColtCLI cli = new ColtCLI();
        cli.err = System.err;
        cli.out = System.out;
        cli.in = new LineNumberReader(new InputStreamReader(System.in));
        cli.processArgs(args);
        cli.execute();
    }

    public ColtCLI() {
    }

    private PrintStream err;
    private PrintStream out;
    private LineNumberReader in;
    boolean keyboardReadRequest;

    private void processArgs(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            System.out.println("arg: " + arg);
        }
    }

    private void execute() {
        printStartMessage();
        keyboardReadRequest = true;
        new Thread(this).start();
    }

    private void printStartMessage() {
        out.println("COLT - Code Orchestra Livecoding Tool");
    }

    @Override
    public void run() {
        while (in != null) {
            out.println("run");
            try {
                String line = in.readLine();
                if (line != null) {
                    if(line.equals("q")) {
                        System.exit(0);
                    }
                }
                Thread.sleep(50);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}