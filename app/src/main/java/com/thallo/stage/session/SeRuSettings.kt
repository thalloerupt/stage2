package com.thallo.stage.session

import org.mozilla.geckoview.GeckoRuntimeSettings
import org.mozilla.geckoview.GeckoSessionSettings

class SeRuSettings {
    var geckoSessionSettings:GeckoSessionSettings
    var geckoRuntimeSettings:GeckoRuntimeSettings
    var desktopMode:Boolean

    constructor(
        geckoSessionSettings: GeckoSessionSettings,
        geckoRuntimeSettings: GeckoRuntimeSettings,
        desktopMode: Boolean
    ) {
        this.geckoSessionSettings = geckoSessionSettings
        this.geckoRuntimeSettings = geckoRuntimeSettings
        this.desktopMode = desktopMode
        if (desktopMode){
            geckoSessionSettings.userAgentOverride="Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36"
            geckoSessionSettings.viewportMode=GeckoSessionSettings.VIEWPORT_MODE_MOBILE
        }
        else {
            geckoSessionSettings.userAgentMode = GeckoSessionSettings.USER_AGENT_MODE_MOBILE

        }
        geckoRuntimeSettings.automaticFontSizeAdjustment=true
        geckoRuntimeSettings.aboutConfigEnabled=true
        geckoRuntimeSettings.webFontsEnabled=true
    }
}