package com.example.template

import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import com.example.template.hook.BaseHook
import com.example.template.hook.ExampleHook

private const val PACKAGE_NAME_HOOKED = "android"
private const val TAG = "xposed-template"

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == PACKAGE_NAME_HOOKED) {
            // Init EzXHelper
            EzXHelperInit.initHandleLoadPackage(lpparam)
            EzXHelperInit.setLogTag(TAG)
            EzXHelperInit.setToastTag(TAG)
            // Init hooks
            initHooks(ExampleHook)
        }
    }

    private fun initHooks(vararg hook: BaseHook) {
        hook.forEach {
            try {
                if (it.isInit) return@forEach
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            } catch (thr: Throwable) {
                Log.e("Failed init hook: ${it.javaClass.simpleName}", thr)
            }
        }
    }
}