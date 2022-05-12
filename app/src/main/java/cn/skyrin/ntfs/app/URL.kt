package cn.skyrin.ntfs.app

import cn.skyrin.ntfs.BuildConfig

object URL {
    val markets_pkg_name = listOf(
        // 华为应用商店
        "com.huawei.appmarket",
        // 腾讯应用宝
        "com.tencent.android.qqdownloader",
        // 小米应用商店
        "com.xiaomi.market",
        // OPPO应用商店
        "com.oppo.market",
        //OPPO应用商店（调试OPPO m80发现应用商店）
        "com.heytap.market",
        // VIVO应用商店
        "com.bbk.appstore",
        // 魅族应用市场
        "com.meizu.mstore",
        // 三星应用商店
        "com.sec.android.app.samsungapps",
        // 联想应用商店
        "com.lenovo.leos.appstore",
        // 中兴应用商店
        "zte.com.market",
        // 安智应用商店
        "com.hiapk.marketpho",
        // 360手机助手
        "com.qihoo.appstore",
        // 百度手机助手
        "com.baidu.appsearch",
        // Google Play Store
        "com.android.vending",
        // 豌豆荚
        "com.wandoujia.phoenix2",
        // 91手机助手
        "com.dragon.android.pandaspace",
        // PP手机助手
        "com.pp.assistant",
        // 搜狗应用市场
        "com.sogou.androidtool",
        // 应用汇
        "com.yingyonghui.market",
        // 机锋应用市场
        "com.mappn.gfan",
        // 安卓市场
        "com.hiapk.marketpho",
        // GO商店
        "cn.goapk.market",
        // 酷派应用商店
        "com.yulong.android.coolmart",
        // 酷市场
        "com.coolapk.market",
        // 金立软件商店
        "com.gionee.aora.market"
    )

    const val COOL_APK_HOME_PAGE = "coolmarket://u/2461365"
    const val GITHUB_PAGE = "https://github.com/realskyrin"
    const val WEIBO_URI = "sinaweibo://userinfo?uid=5617577738"
    const val MY_APP_ID = BuildConfig.APPLICATION_ID
    const val NTFH_APP_ID = "cn.skyrin.ntfh"

    const val MARKET_APP_SCHEME = "market://details?id=$MY_APP_ID"
    const val MARKET_NTFH_APP_SCHEME = "market://details?id=$NTFH_APP_ID"
    val MARKET_ID = "com.coolapk.market"
    const val NTFH_APP_URL = "https://www.coolapk.com/apk/287887"

    private const val BASE_URL = "https://skyrin-update.oss-cn-beijing.aliyuncs.com/ntfh/"

    var BASE_URL_PREVIEW = "$BASE_URL${Market.Preview}/release/"
    const val SKYRIN_BLOG_URL = "https://blog.skyrin.cn/"
    const val NTFH_SOURCE_PATH = "https://blog.skyrin.cn/ntfh/"
    var sponsor =
        "${SKYRIN_BLOG_URL}sponsor/"
    var ruleManual =
        "${SKYRIN_BLOG_URL}ntfh-rule-manual/"
    var usefulRules = "${SKYRIN_BLOG_URL}ntfh-useful-rules/"
    var updateLog = "${NTFH_SOURCE_PATH}update-log.html"
    var wechatGroupQRCode = "${NTFH_SOURCE_PATH}ntfh-wechat-group.html"
}


enum class Market(val market: String) {
    Coolapp("coolapp"),
    Xiaomi("xiaomi"),
    Tencent("tencent"),
    Preview("preview"),
    Google("google"),
    Huawei("huawei"),
    Ntfh("ntfh"),
    Debug("debug");

    override fun toString(): String {
        return market
    }
}