package com.fbytes.contest.Contest;

import com.fbytes.contest.Contest.Logger.ILogger;
import com.fbytes.contest.Contest.TestProcessor.TestProcessor;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

import java.io.*;

@SpringBootApplication
public class ContestApplication implements CommandLineRunner, ExitCodeGenerator {
    @Autowired
    private ILogger logger;
    @Autowired
    private TestProcessor testProcessor;

    private int exitCode;

    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(ContestApplication.class, args)));
    }


    @Override
    public void run(String... args) {
        logger.log(ILogger.Severity.info, "ContestApplication started");

        // parse command line
        Options options = new Options();
        options.addOption("help", false, "show help");
        options.addOption("inputfile", true, "specify file to use as input");
        options.addOption("outputfile", true, "specify file to use as output");
        CommandLineParser parser = new GnuParser();     // new DefaultParser(); is not working 25.12.2021 heh...
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Unable to parse command line:");
            e.printStackTrace();
            exitCode = -1;
            return;
        }

        // set default values

        // apply parameters
        if (commandLine.hasOption("help")) {
            printHelp();
            exitCode = 0;
            return;
        }
        if (commandLine.hasOption("inputfile")) {
            logger.log(ILogger.Severity.debug,"Parameter: inputfile, value="+commandLine.getOptionValue("inputfile"));
            try {
                InputStream inStream =  new FileInputStream(ResourceUtils.getFile(commandLine.getOptionValue("inputfile")));
                System.setIn(inStream);
            } catch (FileNotFoundException e) {
                logger.logException("Unable to read from input file: "+commandLine.getOptionValue("inputfile")+"   "+e.getMessage(),e);
                exitCode = -1;
            }
        }
        if (commandLine.hasOption("outputfile")) {
            logger.log(ILogger.Severity.debug,"Parameter: outputfile, value="+commandLine.getOptionValue("outputfile"));
            try{
                PrintStream outStream =  new PrintStream(new FileOutputStream(commandLine.getOptionValue("outputfile")));
                System.setOut(outStream);
            } catch (Exception e) {
                logger.logException("Unable to redirect output to: "+commandLine.getOptionValue("outputfile")+"   "+e.getMessage(),e);
                exitCode = -1;
            }
        }

        // run tests
        try {
            testProcessor.runTests(System.in);
        }
        catch (Exception e){
            exitCode = -2;
            return;
        }
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    private void printHelp() {
        String helpFilePath = "classpath:help.txt";
        try (InputStream in = new FileInputStream(ResourceUtils.getFile(helpFilePath))) {
            String data = readFromInputStream(in);
            System.out.println(data);
        } catch (IOException e) {
            logger.logException("Unable to read help file: " + helpFilePath, e);
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
