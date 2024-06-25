# G1GC CodeCache Cleanup Issue

This repository demonstrates an issue where G1GC does not correctly clean up the JVM CodeCache, even in emergency situations.

## Initial Example
The first test was performed on `jbrsdk-21.0.3-windows-x64-b446.1`.

## Reproduction Steps

1. **Add Hotswap Agent Core**
   - Ensure a working version of `hotswap-agent-core` is added and fix the Maven import. Use version `1.4.2` to avoid warning messages present in `1.4.1`.

2. **Run the Script**
   - Execute `debugG1Failing.bat` or `debugSerialWorking.bat` on Windows, or similar `.sh` scripts on Unix.

3. **Connect to the Process**
   - Connect to the running process on `127.0.0.1:31337`. If running remotely, forward the local port to the remote machine using your SSH connection with `-L 31337:localhost:31337`.

4. **Profiling Tool**
   - Connect a profiling tool, such as YourKit, to the process.

5. **Trigger the Issue**
   - Start adding spaces to `Main.java` and saving it repeatedly (5-15 times) to crash the code cache on G1GC.

## Workaround

By using `-XX:+UseSerialGC` in combination with calling `System.gc()` every time a code change is detected, it is possible to prevent the CodeCache from crashing. Note that this workaround does NOT work with G1GC.

## Example error
```plaintext
[2024-06-25T03:07:40.146+0200][71.258s][info][codecache] Code cache is full - disabling compilation
[2024-06-25T03:07:40.147+0200][71.258s][warning][codecache] CodeCache is full. Compiler has been disabled.
[2024-06-25T03:07:40.147+0200][71.258s][warning][codecache] Try increasing the code cache size using -XX:ReservedCodeCacheSize=
OpenJDK 64-Bit Server VM warning: CodeCache is full. Compiler has been disabled.
OpenJDK 64-Bit Server VM warning: Try increasing the code cache size using -XX:ReservedCodeCacheSize=
CodeCache: size=12288Kb used=11597Kb max_used=11935Kb free=690Kb
 bounds [0x000001c3b9470000, 0x000001c3ba070000, 0x000001c3ba070000]
 total_blobs=4495 nmethods=3967 adapters=429
 compilation: disabled (not enough contiguous free space left)
              stopped_count=1, restarted_count=0
 full_count=1
OpenJDK 64-Bit Server VM warning: Initialization of C1 CompilerThread4 thread failed (no space to run compilers)
```
