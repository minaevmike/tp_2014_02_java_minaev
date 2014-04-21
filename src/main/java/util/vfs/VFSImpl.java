package util.vfs;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VFSImpl implements VFS {

    private String root;

    public VFSImpl(String root){
        this.root = root;
    }

    public boolean isDirectory(String path) {
        return new File(root + path).isDirectory();
    }

    public Iterator<String> getIterator(String startDir) {
        return new FileIterator(startDir);
    }

    private class FileIterator implements Iterator<String>{

        private Queue<File> files = new LinkedList<File>();

        public FileIterator(String path){
            files.add(new File(root + path));
        }

        public boolean hasNext() {
            return !files.isEmpty();
        }

        public String next() {
            File file = files.peek();
            if(file.isDirectory()){
                for(File subFile : file.listFiles()){
                    files.add(subFile);
                }
            }

            return files.poll().getAbsolutePath();
        }

        public void remove() {

        }

    }

    @Override
    public String getAbsolutePath(String file) {
        return new File(root + file).getAbsolutePath();
    }


    @Override
    public boolean isExist(String path) {
        return new File(path).exists();
    }


    @Override
    public byte[] getBytes(String file) {
        byte [] data;
        try{
            data = Files.readAllBytes(Paths.get(getAbsolutePath(file)));
        }
        catch (IOException e){
            e.printStackTrace();
            data = null;
        }
        return data;
    }


    @Override
    public String getUFT8Text(String file) {
        try {
            FileInputStream fs = new FileInputStream(getAbsolutePath(file));
            DataInputStream ds = new DataInputStream(fs);
            InputStreamReader sr = new InputStreamReader(ds, "UTF-8");
            BufferedReader br = new BufferedReader(sr);
            StringBuilder lines = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null)   {
                lines.append(line);
            }
            br.close();
            return lines.toString();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
