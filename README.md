# Execution time measurement starter

## Description

This starter is allowing to measure method execution time and print readings in tree-like view.

## Usage
1. Add starter as dependency
1. Configuration via `application.yml`:
   * add `exec-time-tracker.enabled=true` property to enable starter
   * add `exec-time-tracker.print-on-exit=true` property to print measured execution statistic via Slf4j
   * add `exec-time-tracker.print-type` property with one of `csv`, `table` or `both` values to select convenient type of statistic output 
1. Mark piece of code to measure execution time
   1. For public methods you can use `@TrackExecTime` annotation. By default, trace point is named using `<ClassName>::<methodName>` pattern, 
        but you can define your own trace name.   
   1. For non-public methods or specific pieces of code you can get suitable Tracker instance using `ExecTimeTrackerFactory` and wrap the code with `measure()` method.
1. If `exec-time-tracker.print-on-exit` property is enabled then make sure that first Tracker in callstack is invoked via `@TrackExecTime` annotation. 
   Otherwise, execution time statistic collection will work incorrectly.
1. You can access statistics by autowiring `ExecTimeTrackerService` bean and invoking `getStackAsAsciiTable()` or `getStackAsCsv()` methods. 

