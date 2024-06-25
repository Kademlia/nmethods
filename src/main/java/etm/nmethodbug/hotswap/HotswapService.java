package etm.nmethodbug.hotswap;

import java.util.HashSet;
import java.util.Set;

//Compare https://github.com/HotswapProjects/HotswapAgentExamples/blob/master/custom-plugin/src/main/java/org/hotswap/agent/example/plugin/ExamplePlugin.java
public class HotswapService {

    Set<String> reloadedClasses = new HashSet<String>(); 

    int loadedClasses;

    String examplePluginResourceText;

    public String getExamplePluginResourceText() {
        return examplePluginResourceText;
    }

    public void setExamplePluginResourceText(String examplePluginResourceText) {
        this.examplePluginResourceText = examplePluginResourceText;
    }

    public int getLoadedClasses() {
        return loadedClasses;
    }

    public void setLoadedClasses(int loadedClasses) {
        this.loadedClasses = loadedClasses;
    }

    public void setLoadedClasses(Integer loadedClasses) {
        this.loadedClasses = loadedClasses;
    }

    public void addReloadedClass(String className) {
        reloadedClasses.add(className);
    }

    public Set<String> getReloadedClasses() {
        return reloadedClasses;
    }
}
