package etm.nmethodbug.hotswap;

import static org.hotswap.agent.annotation.LoadEvent.REDEFINE;

import java.lang.reflect.InvocationTargetException;

import org.hotswap.agent.annotation.Init;
import org.hotswap.agent.annotation.OnClassLoadEvent;
import org.hotswap.agent.annotation.Plugin;
import org.hotswap.agent.command.Scheduler;
import org.hotswap.agent.javassist.CannotCompileException;
import org.hotswap.agent.javassist.CtClass;
import org.hotswap.agent.javassist.NotFoundException;
import org.hotswap.agent.util.PluginManagerInvoker;

//Compare https://github.com/HotswapProjects/HotswapAgentExamples/blob/master/custom-plugin/src/main/java/org/hotswap/agent/example/plugin/ExamplePlugin.java
@Plugin(name = "HotswapDetector", testedVersions = "Your mom")
public class HotswapDetector {
	public static final String SERVICE = "etm.nmethodbug.hotswap.HotswapService";

	@OnClassLoadEvent(classNameRegexp = SERVICE)
	public static void transformTestEntityService(CtClass ctClass) throws NotFoundException, CannotCompileException {
        String src = PluginManagerInvoker.buildInitializePlugin(HotswapDetector.class);
        src += PluginManagerInvoker.buildCallPluginMethod(HotswapDetector.class, "registerService", "this", "java.lang.Object");
        ctClass.getDeclaredConstructor(new CtClass[0]).insertAfter(src);
        System.out.println("24_14:24:54 " + SERVICE + " has been enhanced.");
	}

    @Init
    ClassLoader appClassLoader;

    public void registerService(Object serviceHelper) {
        this.serviceHelper = serviceHelper;
        System.out.println("15:15:42: Plugin " + getClass() + " " + this.serviceHelper); 
    }

    // the service, please note that agentexamples cannot be typed here
    Object serviceHelper;
    
    // Scheduler service - use to run a command asynchronously and merge multiple similar commands to one execution
    // static  - Scheduler and other agent services are available even in static context (before the plugin is initialized)
    @Init
    Scheduler scheduler; 

    @OnClassLoadEvent(classNameRegexp = ".*", events = REDEFINE)
    public void reloadClass(String className) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		scheduler.scheduleCommand(new ReloadClassCommand(appClassLoader, className, serviceHelper));
	}

}
