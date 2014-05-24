package util.resources;

import util.sax.ReadXMLFileSax;

import java.util.HashMap;
import java.util.Map;


public class ResourceFactory {
    private static ResourceFactory resourceFactory = null;
    private Map<String, Resource> resourceMap = new HashMap<>();
    private ResourceFactory(){

    }
    public static ResourceFactory getInstance(){
        if(resourceFactory == null){
            resourceFactory = new ResourceFactory();
        }
        return resourceFactory;
    }

    public void add(String path){
        resourceMap.put(path, (Resource)ReadXMLFileSax.readXML(path));
    }

    public Resource get(String path){
        Resource resource = resourceMap.get(path);
        if(resource == null){
            resource = (Resource)ReadXMLFileSax.readXML(path);
            resourceMap.put(path, resource);
        }
        return resource;
    }
}
