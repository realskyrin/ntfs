package cn.skyrin.common

import android.app.Activity
import cn.skyrin.common.Qualifiers.IO_DISPATCHER
import cn.skyrin.common.Qualifiers.MAIN_DISPATCHER
import cn.skyrin.common.permission.PermissionChecker
import cn.skyrin.common.permission.RealPermissionChecker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

object Qualifiers {
    const val MAIN_DISPATCHER = "main_dispatcher"
    const val IO_DISPATCHER = "io_dispatcher"
}

val commonModule = module {
    factory<CoroutineDispatcher>(named(MAIN_DISPATCHER)) { Dispatchers.Main }

    factory(named(IO_DISPATCHER)) { Dispatchers.IO }

    factory { RealPermissionChecker(get()) } bind PermissionChecker::class

    // factory { RealSdkProvider() } bind SdkProvider::class

//    factory { (activity: Activity) -> RealUrlLauncher(activity) } bind UrlLauncher::class
}