package com.satori.mods.suite;

import com.satori.mods.core.stats.*;

import java.lang.management.*;
import java.util.concurrent.*;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.*;

public class StatsJvmMod extends Mod {
  public static final Logger log = LoggerFactory.getLogger(StatsJvmMod.class);
  public static final Runtime runtime = Runtime.getRuntime();
  public static final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
  public static final RuntimeMXBean runBean = ManagementFactory.getRuntimeMXBean();
  public static final int coresNum = osBean.getAvailableProcessors();
  
  private long prevUpTime = Long.MIN_VALUE;
  private long prevCpuTime = Long.MIN_VALUE;
  
  public StatsJvmMod() throws Exception {
  }
  
  @Override
  public void onStats(StatsCycle cycle, IStatsCollector collector) {
    double memFree = runtime.freeMemory();
    double memCommitted = runtime.totalMemory();
    double memMax = runtime.maxMemory();
    collector.avg("mem.free", memFree);
    collector.avg("mem.committed", memCommitted);
    collector.avg("mem.allocated", memCommitted - memFree);
    collector.avg("mem.usage", (memCommitted - memFree) / memMax);
    
    //collector.avg("cpu.usage", osBean.getProcessCpuLoad() * coresNum);
    collectCpuUsage(collector);
  }
  
  private void collectCpuUsage(IStatsCollector collector) {
    long upTime = runBean.getUptime();
    long cpuTime = osBean.getProcessCpuTime();
    
    if (prevUpTime == Long.MIN_VALUE) {
      prevUpTime = upTime;
      prevCpuTime = cpuTime;
      return;
    }
    
    long elapsedTime = upTime - prevUpTime;
    if (elapsedTime <= 0) {
      return;
    }
    
    collector.avg(
      "cpu.usage",
      (cpuTime - prevCpuTime) / (TimeUnit.MILLISECONDS.toNanos(elapsedTime))
    );
    
    prevUpTime = upTime;
    prevCpuTime = cpuTime;
  }
  
}
