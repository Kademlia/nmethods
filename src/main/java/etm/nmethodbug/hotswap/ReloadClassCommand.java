package etm.nmethodbug.hotswap;

import java.lang.reflect.Method;

import org.hotswap.agent.command.MergeableCommand;
import org.hotswap.agent.logging.AgentLogger;

// Compare https://github.com/HotswapProjects/HotswapAgentExamples/blob/master/custom-plugin/src/main/java/org/hotswap/agent/example/plugin/ExamplePlugin.java
public class ReloadClassCommand extends MergeableCommand {
    ClassLoader appClassLoader;
    String className;
    Object serviceHelper;
    private static long lastRefresh = 0;

    public ReloadClassCommand(ClassLoader appClassLoader, String className, Object serviceHelper) {
        this.appClassLoader = appClassLoader;
        this.className = className;
        this.serviceHelper = serviceHelper;
    }

    public void executeCommand() {
        try {
            Method setExamplePluginResourceText = appClassLoader.loadClass(ReloadClassService.class.getName()) 
                    .getDeclaredMethod("classReloaded", String.class, Object.class);
            setExamplePluginResourceText.invoke(null, className, serviceHelper);
      
    		if(className.contains("$") ) {
//    			System.out.println("24_14:07:12 " + "==== ignoring REDEFINITION ==== " + className);
    			return;
    		}
    		if(lastRefresh > System.currentTimeMillis() ) {
    			System.out.println("24_14:07:26 " + "==== ignoring REDEFINITION based on time ==== " + className); 
    			return;
    		}
    		lastRefresh = System.currentTimeMillis() + 300; 
    		
    		System.out.println("24_14:07:36 " + "==== CLASS REDEFINITION DETECTED ==== " + className + " " + lastRefresh + " " + System.currentTimeMillis());

    		// This fixes a java jdk (?) bug of the code cache not being cleaned fast enough
    		// It allows the UseSerialGC to clean the code cache, does NOT WORK with UseG1GC
    		long start = System.currentTimeMillis();
    		System.gc();   
    		long end = System.currentTimeMillis();  
    		System.out.println("24_14:07:53 " + "==== GC TIME ==== " + (end-start) + "ms");

        } catch (Exception e) {
            System.out.println("24_14:37:11 " + "==== Error invoking {}.reload() ==== " + e);
        }
    }

}
