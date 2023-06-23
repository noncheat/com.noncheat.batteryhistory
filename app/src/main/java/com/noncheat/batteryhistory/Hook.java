package com.noncheat.batteryhistory;

import static de.robv.android.xposed.XposedBridge.log;

import android.content.Context;
import android.os.Build;

import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    private String classNameTerraceHelper = "com.android.settings.fuelgauge.batteryusage.DataProcessor";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        XposedHelpers.findAndHookMethod(
                classNameTerraceHelper,
                loadPackageParam.classLoader,
                "getHistoryMapWithExpectedTimestamps",
                Context.class,
                Map.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        Object[] args = param.args;
                        final Map<Long, Map> batteryHistoryMap = (Map<Long, Map>) args[1];
                        batteryHistoryMap.keySet().removeIf(key -> key > System.currentTimeMillis());
                        param.args[1] = batteryHistoryMap;
                    }
                }
        );
    }
}
