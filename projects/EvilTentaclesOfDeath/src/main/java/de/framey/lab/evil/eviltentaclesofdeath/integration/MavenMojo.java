package de.framey.lab.evil.eviltentaclesofdeath.integration;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * For all Cultists out there bursting with mojo here's a maven plugin which can be used for post compilation. It actually calls
 * {@link PostCompiler#main(String[])} with maven property <code>project.build.outputDirectory</code> as parameter, thus doing the job.
 * <p>
 * It can easily being added to the build process like this:
 * <p>
 *
 * <pre>
 * <code>
 * &lt;build&gt;
 *     &lt;plugins&gt;
 *         ...
 *         &lt;plugin&gt;
 *             &lt;groupId&gt;de.framey.lab.evil&lt;/groupId&gt;
 *             &lt;artifactId&gt;cthulhu&lt;/artifactId&gt;
 *             &lt;version&gt;0.1.0-RELEASE&lt;/version&gt;
 *             &lt;executions&gt;
 *                 &lt;execution&gt;
 *                     &lt;id&gt;Tentacle&lt;/id&gt;
 *                     &lt;phase&gt;process-classes&lt;/phase&gt;
 *                     &lt;goals&gt;
 *                         &lt;goal&gt;summonCthulhu&lt;/goal&gt;
 *                     &lt;/goals&gt;
 *                 &lt;/execution&gt;
 *             &lt;/executions&gt;
 *         &lt;/plugin&gt;
 *     &lt;/plugins&gt;
 * &lt;/build&gt;
 * </code>
 * </pre>
 * </p>
 * </p>
 *
 * <p>
 * As a compile dependency you will need to add the Tentacle.jar but it is not needed at runtime.
 * <p>
 *
 * <pre>
 * <code>
 * &lt;dependency&gt;
 *     &lt;groupId&gt;de.framey.lab.evil&lt;/groupId&gt;
 *     &lt;artifactId&gt;cthulhu&lt;/artifactId&gt;
 *     &lt;version&gt;0.1.0-SNAPSHOT&lt;/version&gt;
 *     &lt;scope&gt;compile&lt;/scope&gt;
 * &lt;/dependency&gt;
 * </code>
 * </pre>
 * </p>
 * </p>
 *
 * @author Frank Meyfarth
 */
@Mojo(name = "summonCthulhu", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class MavenMojo extends AbstractMojo {

    /** The class directory to summon Cthulhu on. */
    @Parameter(defaultValue = "${project.build.outputDirectory}")
    private String outDir;

    /**
     * Simply calls {@link PostCompiler#main(String[])}.
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            PostCompiler.main(new String[] { outDir });
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to postcompile classes!", e);
        }
    }
}
