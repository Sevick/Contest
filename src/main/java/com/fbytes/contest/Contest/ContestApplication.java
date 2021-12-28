package com.fbytes.contest.Contest;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.TestProcessor.TestProcessor;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
public class ContestApplication implements CommandLineRunner, ExitCodeGenerator {
    @Autowired
    private ILogger logger;
    @Autowired
    Environment env;
    @Autowired
    private TestProcessor testProcessor;

    private int exitCode;
    static private Map<String, String> commandlineProperties = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        // parse command line
        Options options = new Options();
        options.addOption("help", false, "show help");
        options.addOption("inputfile", true, "specify file to use as input");
        options.addOption("outputfile", true, "specify file to use as output");
        options.addOption("logfile", true, "specify file to use as log");
        CommandLineParser parser = new GnuParser();     // new DefaultParser(); is not working 25.12.2021 heh...
        CommandLine commandLine = null;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Unable to parse command line:");
            e.printStackTrace();
            System.exit(-1);
        }

        // set default values

        // apply parameters
        if (commandLine.hasOption("help")) {
            printHelp();
            return;
        }
        if (commandLine.hasOption("inputfile")) {
            try {
                InputStream inStream = new FileInputStream(ResourceUtils.getFile(commandLine.getOptionValue("inputfile")));
                System.setIn(inStream);
            } catch (FileNotFoundException e) {
                System.out.println("Unable to read from input file: " + commandLine.getOptionValue("inputfile") + "   " + e.getMessage());
                System.exit(-1);
            }
        }
        if (commandLine.hasOption("outputfile")) {
            try {
                PrintStream outStream = new PrintStream(new FileOutputStream(commandLine.getOptionValue("outputfile")));
                System.setOut(outStream);
            } catch (Exception e) {
                System.out.println("Unable to redirect output to: " + commandLine.getOptionValue("outputfile") + "   " + e.getMessage());
                System.exit(-1);
            }
        }
        if (commandLine.hasOption("logfile")) {
            commandlineProperties.put("contest.testresultwriterfile.logfile", commandLine.getOptionValue("logfile"));
        }

        // Run container adding command line parameters to environment
        System.exit(SpringApplication.exit(new SpringApplicationBuilder()
                .sources(ContestApplication.class)
                .initializers(context -> context
                        .getEnvironment()
                        .getPropertySources()
                        .addFirst(new CommandLinePropertySource(commandlineProperties))
                ).run(args)));
    }

    static private void printHelp() {
        String helpFilePath = "classpath:help.txt";
        try (InputStream in = new FileInputStream(ResourceUtils.getFile(helpFilePath))) {
            String helpText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(helpText);
        } catch (IOException e) {
            System.out.println("Unable to read help file: " + helpFilePath + "  " + e.getMessage());
        }
    }

    @Override
    public void run(String... args) {
        logger.log(ILogger.Severity.info, "ContestApplication started");
        // run tests, check for "test" profile that must be used in all unit tests
        if (!Arrays.asList(env.getActiveProfiles()).contains("test")) {
            try {
                testProcessor.runTests(System.in);
            } catch (Exception e) {
                exitCode = -2;
            }
        }
    }


    @Override
    public int getExitCode() {
        return exitCode;
    }


    static class CommandLinePropertySource extends PropertySource<String> {
        Map<String, String> propertiesMap;

        CommandLinePropertySource(Map<String, String> propertiesMap) {
            super("commandline");
            this.propertiesMap = propertiesMap;
        }

        @Override
        public Object getProperty(String name) {
            return propertiesMap.get(name);
        }
    }
}
