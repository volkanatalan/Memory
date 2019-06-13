package com.volkanatalan.memory.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.volkanatalan.memory.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AboutActivityTest {
  
  @Rule
  @JvmField
  var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
  
  @Test
  fun aboutActivityTest() {
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(7000)
    
    val overflowMenuButton = onView(
      allOf(
        withContentDescription("More options"),
        childAtPosition(
          childAtPosition(
            withId(R.id.toolbar),
            1
          ),
          1
        ),
        isDisplayed()
      )
    )
    overflowMenuButton.perform(click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(250)
    
    val appCompatTextView = onView(
      allOf(
        withId(R.id.title), withText("About"),
        childAtPosition(
          allOf(
            withId(R.id.content),
            childAtPosition(
              withClassName(`is`("androidx.appcompat.view.menu.ListMenuItemView")),
              1
            )
          ),
          0
        ),
        isDisplayed()
      )
    )
    appCompatTextView.perform(click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(7000)
    
    val appCompatTextView2 = onView(
      allOf(
        withId(R.id.link1), withText("volkanatalan35@gmail.com"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.ScrollView")),
            0
          ),
          2
        )
      )
    )
    appCompatTextView2.perform(scrollTo(), click())
    
    val appCompatTextView3 = onView(
      allOf(
        withId(R.id.link2), withText("github.com/volkanatalan"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.ScrollView")),
            0
          ),
          3
        )
      )
    )
    appCompatTextView3.perform(scrollTo(), click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(7000)
    
    val appCompatTextView4 = onView(
      allOf(
        withId(R.id.link3), withText("linkedin.com/in/volkanatalan"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.ScrollView")),
            0
          ),
          4
        )
      )
    )
    appCompatTextView4.perform(scrollTo(), click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(7000)
    
    val appCompatTextView5 = onView(
      allOf(
        withId(R.id.link4), withText("flaticon.com/packs/files-8"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.ScrollView")),
            0
          ),
          7
        )
      )
    )
    appCompatTextView5.perform(scrollTo(), click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(7000)
    
    val appCompatTextView6 = onView(
      allOf(
        withId(R.id.link5), withText("freepik.com/free-vector/elegant-white-background-with-shiny-lines_4077186.htm"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.ScrollView")),
            0
          ),
          9
        )
      )
    )
    appCompatTextView6.perform(scrollTo(), click())
  }
  
  private fun childAtPosition(
    parentMatcher: Matcher<View>, position: Int
  ): Matcher<View> {
    
    return object : TypeSafeMatcher<View>() {
      override fun describeTo(description: Description) {
        description.appendText("Child at position $position in parent ")
        parentMatcher.describeTo(description)
      }
      
      public override fun matchesSafely(view: View): Boolean {
        val parent = view.parent
        return parent is ViewGroup && parentMatcher.matches(parent)
          && view == parent.getChildAt(position)
      }
    }
  }
}
