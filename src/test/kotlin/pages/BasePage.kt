package pages

import info.novatec.testit.webtester.kotlin.pages.Page
import info.novatec.testit.webtester.pagefragments.Div
import info.novatec.testit.webtester.pagefragments.annotations.IdentifyUsing
import pages.fragments.FooterFragment
import pages.fragments.HeaderFragment

interface BasePage : Page {

    @IdentifyUsing("#headerFragment") fun header(): HeaderFragment

    @IdentifyUsing("#footerFragment") fun footer(): FooterFragment

    fun headerIsDisplayed(): Boolean {
        val header = header()
        return header.isPresent && header.isVisible
    }

    fun footerIsDisplayed(): Boolean {
        val footer = footer()
        return footer.isPresent && footer.isVisible
    }

}