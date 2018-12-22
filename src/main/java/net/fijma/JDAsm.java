package net.fijma;

import net.fijma.classfile.ClassFile;
import org.apache.commons.cli.*;

import java.io.FileInputStream;
import java.util.List;

public class JDAsm {

    private void run(List<String> args) throws Exception {
        for (String filename : args) {
            ClassFile res = ClassFile.read(new FileInputStream(filename));
            System.out.println("read:");
            System.out.println(res);
            System.out.println("Constant pool:");
            System.out.println(res.pool);
        }
    }

    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("jdasm", options);
    }

    public static void main( String[] args ) {

        Options options = new Options();
        options.addOption("h", false, "help");
        options.addOption("b", false, "just an option");
        // options.addOption(Option.builder("d").optionalArg(false).hasArg().argName("device").desc("serial port device").build());

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                usage(options);
                System.exit(0);
            }

            boolean b = cmd.hasOption("b");
            try {
                new JDAsm().run(cmd.getArgList());
            } catch (Exception e) {
                System.err.println("error: " + e.getMessage());
                System.exit(1);
            }

        } catch (ParseException e) {
            System.err.println(e.getMessage());
            usage(options);
            System.exit(1);
        }

    }
}
