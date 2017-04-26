package nl.rug.search.patterngrime;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import spoon.Launcher;
import spoon.SpoonAPI;

import java.io.File;
import java.io.IOException;

/**
 * Created by feitosadaniel on 09/04/2017.
 */
public class CLI {

    @Parameter(names = { "-p", "--patterns" }, required = true, converter = FileConverter.class,
            description = "XML with pattern instances")
    private File patternFile;

    @Parameter(names = { "-s", "--source" }, required = true, converter = FileConverter.class,
            description = "Folder containing the source code")
    private File srcDir;

    @Parameter(names = { "-o", "--output" }, converter = FileConverter.class)
    private File output;

    @Parameter(names = "--help", help = true)
    private boolean help = false;

    public static void main(String args[]){
        CLI cli = new CLI();
        JCommander builder = JCommander.newBuilder()
                .addObject(cli)
                .build();
        builder.setProgramName("spoon-grime");
        builder.parse(args);

        if (cli.help) {
            builder.usage();
            return;
        }

        cli.configure();
        cli.run();
    }

    private void configure(){
        Assert.assertTrue(patternFile.isFile());
        try {
            GoFUtil.loadPatternInstances(patternFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Assert.assertTrue(srcDir.isDirectory());

        if(output != null){
            if(output.exists()) {
                Assert.assertTrue(output.isFile());
            }
        }else{
            output = new File(patternFile.getParent(),
                    String.format("%s.pttgrime.xml",FilenameUtils.getBaseName(patternFile.getName())));
        }
    }

    private void run(){
        final SpoonAPI spoon = new Launcher();
        spoon.getEnvironment().setNoClasspath(true);

        final String processorPackge = "nl.rug.search.patterngrime.spoon.processors";
        spoon.addProcessor(String.format("%s.CgNaProcessor", processorPackge));
        spoon.addProcessor(String.format("%s.CgNpmProcessor", processorPackge));
        spoon.addProcessor(String.format("%s.MgCaProcessor", processorPackge));
        spoon.addProcessor(String.format("%s.MgCeProcessor", processorPackge));
        spoon.addProcessor(String.format("%s.OgCaProcessor", processorPackge));
        spoon.addProcessor(String.format("%s.OgNpProcessor", processorPackge));

        spoon.addInputResource(srcDir.getAbsolutePath());
        spoon.run();

        try {
            GoFUtil.savePatternInstances(output.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
