package property.maven.plugin.core;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import property.maven.plugin.api.Accessible;
import property.maven.plugin.api.Mutable;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * "Generate" goal of plugin.
 */
@Mojo(name = "generate")
public class PropertyMavenPlugin extends AbstractMojo {

    /**
     * Logger.
     */
    private Log log;

    /**
     * Class pool.
     */
    private ClassPool classPool;

    /**
     * Constructor.
     */
    public PropertyMavenPlugin() {
        log = getLog();
        classPool = ClassPool.getDefault();
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Property maven plugin start working...");
            }
            List<String> classDirectories = getClassDirectories();
            for (String classDirectory : classDirectories) {
                classDirectoryProcessing(classDirectory);
            }
            if (log.isDebugEnabled()) {
                log.debug("Property maven plugin finish working");
            }
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("There was next exception " + e);
            }
        }
    }

    /**
     * Class directory processing.
     * @param classDirectory class directory
     * @throws Exception exception
     */
    private void classDirectoryProcessing(String classDirectory) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Processing " + classDirectory);
        }
        classPool.appendClassPath(classDirectory);
        Collection<File> files = FileUtils.listFiles(new File(classDirectory), new String[]{"class"}, true);
        for (File file : files) {
            classFileProcessing(file, classDirectory);
        }
    }

    /**
     * Class file processing.
     * @param file file
     * @param classDirectory class directory
     * @throws Exception exception
     */
    private void classFileProcessing(File file, String classDirectory) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("File " + file.getAbsolutePath() + " processing");
        }
        String className = file.getAbsolutePath()
                               .replace( classDirectory + File.separator, "")
                               .replace(File.separator, ".")
                               .replace(".class", "");
        CtClass clazz = classPool.get(className);
        CtField[] fields = clazz.getDeclaredFields();

        for (CtField field : fields) {
            if (field.getAnnotation(Accessible.class) != null) {
                clazz.addMethod(generateGetter(clazz, field));
            }
            if (field.getAnnotation(Mutable.class) != null) {
                clazz.addMethod(generateSetter(clazz, field));
            }
        }
        clazz.writeFile(classDirectory);
        if (log.isDebugEnabled()) {
            log.debug("File " + file.getAbsolutePath() + " processing is finished");
        }
    }

    /**
     * Returns class directories.
     * @return class directories
     */
    private List<String> getClassDirectories() throws Exception {
        MavenProject project = (MavenProject) getPluginContext().get("project");
        List<String> sourceDirectories = project.getCompileClasspathElements();
        if (log.isDebugEnabled()) {
            log.debug("Class directories are " + sourceDirectories);
        }
        return sourceDirectories;
    }

    /**
     * Generates getter.
     * @param clazz class
     * @param field field
     * @return getter
     */
    public CtMethod generateGetter(CtClass clazz, CtField field) throws Exception {
        String property = field.getName();
        String getterName = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
        CtMethod getter = new CtMethod(field.getType(), getterName, null, clazz);
        getter.setBody("return " + property + ";");
        return getter;
    }

    /**
     * Generates setter.
     * @param clazz class
     * @param field field
     * @return setter
     */
    public CtMethod generateSetter(CtClass clazz, CtField field) throws Exception {
        String property = field.getName();
        String setterName = "set" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
        CtMethod setter = new CtMethod(CtClass.voidType, setterName, new CtClass[]{field.getType()}, clazz);
        setter.setBody("this." + property + "=$1;");
        return setter;
    }
}