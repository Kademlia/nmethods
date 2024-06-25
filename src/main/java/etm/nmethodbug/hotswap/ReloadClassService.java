package etm.nmethodbug.hotswap;

//Compare https://github.com/HotswapProjects/HotswapAgentExamples/blob/master/custom-plugin/src/main/java/org/hotswap/agent/example/plugin/ExamplePlugin.java
public class ReloadClassService {
    public static void classReloaded(String className, Object serviceHelper) {
        HotswapService helloWorldService = (HotswapService) serviceHelper;
        helloWorldService.addReloadedClass(className);
    }
}
