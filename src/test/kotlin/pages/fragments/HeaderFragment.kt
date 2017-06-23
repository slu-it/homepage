package pages.fragments

import info.novatec.testit.webtester.conditions.pagefragments.PresentAndVisible
import info.novatec.testit.webtester.kotlin.pagefragments.PageFragment
import info.novatec.testit.webtester.pagefragments.Div
import info.novatec.testit.webtester.pagefragments.Link
import info.novatec.testit.webtester.pagefragments.annotations.IdentifyUsing
import info.novatec.testit.webtester.pagefragments.annotations.PostConstructMustBe

interface HeaderFragment : PageFragment {

    @PostConstructMustBe(PresentAndVisible::class)
    @IdentifyUsing("#headline") fun headline(): Div

}