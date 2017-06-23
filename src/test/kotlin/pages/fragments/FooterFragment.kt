package pages.fragments

import info.novatec.testit.webtester.conditions.pagefragments.PresentAndVisible
import info.novatec.testit.webtester.kotlin.pagefragments.PageFragment
import info.novatec.testit.webtester.pagefragments.Link
import info.novatec.testit.webtester.pagefragments.annotations.IdentifyUsing
import info.novatec.testit.webtester.pagefragments.annotations.PostConstructMustBe

interface FooterFragment : PageFragment {

    @PostConstructMustBe(PresentAndVisible::class)
    @IdentifyUsing("#socialMediaBar") fun socialMediaBar(): SocialMediaBar


    interface SocialMediaBar : PageFragment {

        @PostConstructMustBe(PresentAndVisible::class)
        @IdentifyUsing("#twitterLink") fun twitter(): Link

        @PostConstructMustBe(PresentAndVisible::class)
        @IdentifyUsing("#githubLink") fun github(): Link

        @PostConstructMustBe(PresentAndVisible::class)
        @IdentifyUsing("#xingLink") fun xing(): Link

    }

}