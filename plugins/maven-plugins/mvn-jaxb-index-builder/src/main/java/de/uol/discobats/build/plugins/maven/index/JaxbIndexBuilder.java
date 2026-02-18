/*######################################################################################################
 # This file is part of the Distributed Component-Based Traffic Simulation (DisCo-BaTS) project.       #
 # Copyright (C) 2026 David Reiher <https://github.com/dvdrhr>                                         #
 #                                                                                                     #
 # This program is free software: you can redistribute it and/or modify it under the terms of the      #
 # GNU Lesser General Public License version 3 as published by the Free Software Foundation            #
 # This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;           #
 # without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           #
 # See the GNU Lesser General Public License version 3 for more details.                               #
 # You should have received a copy of the GNU Lesser General Public License along with this program.   #
 # If not, see <https://www.gnu.org/licenses/lgpl+gpl-3.0.txt/>.                                       #
 #                                                                                                     #
 # Module: mvn-jaxb-index-builder                                                                      #
 # File: JaxbIndexBuilder.java                                                                         #
 # Last Updated: 2026-02-17 21:58:03                                                                   #
 ######################################################################################################*/

package de.uol.discobats.build.plugins.maven.index;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.uol.discobats.util.log.LogLevel.ERROR;
import static de.uol.discobats.util.log.LogService.log;
import static java.lang.System.exit;

/**
 * maven plugin / MOJO to generate a jaxb.index file containing all model related classes
 * all classes of the current project are scanned and additionally all classes of all dependency jars
 * traversed classes will be filtered by their package path and only those containing "de.uol.discobats."
 * and "metamodel" or "model" will be included in the index file
 * jaxb.index will be written to <CURRENT_MODULE>/src/main/resources/de/uol/discobats/jaxb.index
 *
 * @version 1
 * @author David Reiher (https://github.com/dvdrhr)
 */
@Mojo(name = "build-jaxbindex", defaultPhase = LifecyclePhase.POST_CLEAN, requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class JaxbIndexBuilder extends AbstractMojo {

    @Parameter(property = "scope")
    String scope;

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    MavenProject project;

    @Parameter(property = "includes")
    List<String> includes;

    @Parameter(property = "excludes")
    List<String> excludes;

    /**
     * entry point method that gets called by maven
     */
    @Override
    public void execute() {
        buildJaxbIndex();
    }

    /**
     * main method
     * (1) get entries for current projects java files
     * (2) get entries for all current projects dependencies jar classes
     * (3) merge lists
     * (4) write collected entries to the target file
     */
    private void buildJaxbIndex() {
        ArrayList<String> ownModelClassPaths = getOwnModelClassPaths();
        ArrayList<String> dependencyModelClassPaths = getDependencyModelClassPaths();

        StringBuilder stringBuilder = new StringBuilder();
        Stream.concat(ownModelClassPaths.stream(), dependencyModelClassPaths.stream()).forEach(string -> stringBuilder.append(string).append("\n"));

        try {
            // create directory and file if necessary
            File target = getTargetFile();

            // write collected classpaths to the file
            Files.write(target.toPath(), stringBuilder.toString().getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
            log("WRITE TARGET: " + target.toPath());
            log("CONTENT: " + stringBuilder);

        } catch (IOException e) {
            log(ERROR, "while trying to write to target \n" + e);
            log(e);
            exitWithError();
        }
    }

    /**
     * return the string representation of the fully qualified file name that should be used for the jaxb index file
     */
    private File getTargetFile() throws IOException {
        String targetDir = getTargetDir();
        // create file if necessary
        File targetFile = new File(targetDir + File.separator + "jaxb.index");
        while (!targetFile.exists()) {
            boolean fileCreated = targetFile.createNewFile();
            if (!fileCreated || !targetFile.isFile()) {
                throw new IOException();
            }
        }
        return targetFile;
    }

    /**
     * returns the string representation of the directory path where jaxb.index should be stored
     */
    private String getTargetDir() throws IOException {
        // String projectBaseDir = project.getBasedir().toString();
        // File targetDir = new File(projectBaseDir + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "de/uol/discobats/build/plugins/mavenPlugins/mvnJaxbIndexBuilder/traffic" + File.separator + "model");
        String projectResourcesDir = project.getResources().getFirst().getDirectory() + "/de/uol/discobats";
        File targetDir = new File(projectResourcesDir);
        while (!targetDir.exists()) {
            boolean directoryCreated = targetDir.mkdirs();
            if (!directoryCreated || !targetDir.isDirectory()) {
                throw new IOException();
            }
        }
        return targetDir.getPath();
    }

    /**
     * returns an arraylist containing all discobats model/metamodel related class-entries
     * of all current projects dependency jars
     * de.uol.discobats.model.example.Example.class --> model.example.Example
     * interfaces and inner classes are excluded
     */
    private ArrayList<String> getDependencyModelClassPaths() {
        Set<Artifact> dependencyArtifacts = project.getArtifacts();
        return dependencyArtifacts.stream()
                                  .filter(artifact -> artifact.getGroupId().contains("discobats"))
                                  .map(Artifact::getFile)
                                  .map(JaxbIndexBuilder::getClassNamesFromJarFile)
                                  .flatMap(Collection::stream)
                                  .distinct()
                                  .filter(string -> string.contains("model.") || string.contains("metamodel"))
                                  .filter(string -> !string.contains("$"))
                                  .map(string -> {
                                      log("DEPENDENCY STRING: " + string);
                                      return string.substring(string.indexOf("de.uol.discobats.") + ("de.uol.discobats.".length()));
                                  })
                                  .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * returns an arraylist containing all discobats model/metamodel related class-entries of the current projects files
     * de.uol.discobats.model.example.Example.java --> model.example.Example
     * interfaces are excluded
     */
    private ArrayList<String> getOwnModelClassPaths() {
        String projectBaseDir = project.getBasedir().toString();
        ArrayList<String> allFiles = getSuitableFilesForPath(projectBaseDir);
        ArrayList<String> classPaths = new ArrayList<>();

        for (String string : allFiles) {
            log("STRING: " + string);
            String substring = string;

            final String javaSourcesDirectory = File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
            final String defaultModelPackagePath = "de" + File.separator + "uol" + File.separator + "discobats" + File.separator;
            final String javaFileExtension = ".java";

            // remove the usual java sources folder sub-path if present (/src/main/java/)
            if (substring.contains(javaSourcesDirectory)) {
                substring = substring.substring((substring.indexOf(javaSourcesDirectory) + (javaSourcesDirectory.length())));
            }

            // only process paths that are part of any "model" or "metamodel" package
            if (substring.contains("model") || substring.contains("metamodel")) {

                // remove the discobats default package prefix part of the path (de.uol.discobats)
                // remove the file extension of the path (.java)
                substring = substring.substring(
                    substring.indexOf(defaultModelPackagePath) + ((defaultModelPackagePath).length()),
                    substring.indexOf(javaFileExtension)
                ).replace(File.separator, ".");

                // skip in some cases
                final String finalSubstringCopy = substring;

                // skip inner classes
                if (finalSubstringCopy.contains("$")) {
                    log("SKIP: " + finalSubstringCopy + ", REASON: inner class");
                    continue;
                }

                // if includes config is present skip classes that aren't explicitly included
                if (!includes.isEmpty()
                    && includes.stream().noneMatch(include -> include.contains(finalSubstringCopy))) {
                    log("SKIP: " + finalSubstringCopy + ", REASON: not included");
                    continue;
                }

                // if exclude config is present skip classes that explicitly excluded
                if (!excludes.isEmpty() && excludes.stream().anyMatch(exclude -> exclude.contains(finalSubstringCopy))) {     // if present skip classes that arent explicitly included
                    log("SKIP: " + substring + ", REASON: not included");
                    continue;
                }

                // exclude interfaces
                try {
                    String readFile = new String(Files.readAllBytes(Paths.get(string)));
                    if (readFile.contains("public interface")) {
                        continue;
                    }
                } catch (IOException e) {
                    log(e, "failed to read the file at " + string + " during interface check");
                    exitWithError();
                }
                classPaths.add(substring);
            }
            log("SUBSTRING: " + substring);
        }
        return classPaths;
    }

    /**
     * iterates over the given Path List and adds all suitable Filepaths within it to the list that is returned
     */
    private ArrayList<String> getSuitableFilesForPath(final String path) {
        log("PATH: " + path);
        return getSuitableFilesForFolder(new File(path + File.separator + "src" + File.separator + "main" + File.separator + "java"));
    }

    /**
     * if a file is within a libraries model folder and ends with '.java', add to paths
     */
    private ArrayList<String> getSuitableFilesForFolder(final File folder) {
        ArrayList<String> paths = new ArrayList<>();
        if (folder.listFiles() == null) {
            return paths;
        }
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            log("FILE: " + fileEntry);
            String path = fileEntry.getAbsolutePath();
            if (fileEntry.isDirectory()) {
                paths.addAll(getSuitableFilesForFolder(fileEntry));
            } else if (((path.contains("model") || path.contains("metamodel")) && path.contains(".java") && !paths.contains(fileEntry.getAbsolutePath()))) {
                paths.add(fileEntry.getAbsolutePath());
            }
        }
        return paths;
    }

    /**
     * gets all simple classnames that are contained in the given jar file
     */
    private static Set<String> getClassNamesFromJarFile(File file) {
        Set<String> classNames = new HashSet<>();
        try (JarFile jarFile = new JarFile(file)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();

                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                                               .replace("/", ".")
                                               .replace(".class", "");
                    classNames.add(className);
                }
            }
        } catch (IOException e) {
            log(e);
            exitWithError();
        }
        return classNames;
    }

    /**
     * exit with a success indicating status code
     */
    private static void exitSuccessfully() {
        exit(0);
    }

    /**
     * exit with an abormal/failure indicating status code
     */
    private static void exitWithError() {
        exit(1);
    }

}