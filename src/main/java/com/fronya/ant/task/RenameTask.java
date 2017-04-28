package com.fronya.ant.task;


import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Ant task for rename files
 */
public class RenameTask extends Task{
    private String destDir;
    private String jobId;
    private List<FileSet> filesets = new LinkedList<>();

    public void setDestDir(String destDir) {
        this.destDir = destDir;
    }

    public void setJobId(String jobId) {
            this.jobId = jobId;
    }

    public void addFileset(FileSet fileset){
        filesets.add(fileset);
    }

    public void execute() throws BuildException {
        for (FileSet currentFileSet: filesets) {
            DirectoryScanner ds = currentFileSet.getDirectoryScanner(getProject());
            String[] includedFiles = ds.getIncludedFiles();
            for (String currentFile: includedFiles) {
                if(!renameFile(currentFile)){
                    throw new BuildException();
                }
            }
        }
    }

    private boolean renameFile(String nameOldFile){
        StringBuilder fullNameFile = new StringBuilder(destDir);
        fullNameFile.append(File.separator).append(nameOldFile);
        File oldFile = new File(fullNameFile.toString());

        int indPoint = fullNameFile.lastIndexOf(".");
        fullNameFile.insert(indPoint, '-').insert(indPoint + 1, jobId);
        File newFile = new File(fullNameFile.toString());
        return oldFile.renameTo(newFile);
    }
}
