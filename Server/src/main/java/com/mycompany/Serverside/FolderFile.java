package com.mycompany.Serverside;

public class FolderFile {
    private String filename;
    private String check;

    FolderFile(String file, String c){
        this.filename = file;
        this.check = c;
    }

    /**
     * @return String return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename the filename to set
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return String return the check
     */
    public String getCheck() {
        return check;
    }

    /**
     * @param check the check to set
     */
    public void setCheck(String check) {
        this.check = check;
    }

}
