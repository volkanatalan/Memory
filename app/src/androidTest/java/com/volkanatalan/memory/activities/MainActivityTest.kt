package com.volkanatalan.memory.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.volkanatalan.memory.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
  
  @Rule
  @JvmField
  var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
  
  @Test
  fun mainActivityTest() {
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
    
    
    
    // Click on add memory button
    val actionMainActivityAdd = onView(
      allOf(
        withId(R.id.action_mainactivity_add), withContentDescription("Add memory"),
        childAtPosition(
          childAtPosition(
            withId(R.id.toolbar),
            1
          ),
          0
        ),
        isDisplayed()
      )
    )
    actionMainActivityAdd.perform(click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
    
    
    
    // Write memory title
    val titleEditText = onView(
      allOf(
        withId(R.id.titleEditText),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            0
          ),
          1
        )
      )
    )
    titleEditText.perform(scrollTo(), replaceText("Demo Title"), closeSoftKeyboard())
    
    
    
    // Write memory text
    val textEditText = onView(
      allOf(
        withId(R.id.textEditText),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            0
          ),
          2
        )
      )
    )
    textEditText.perform(scrollTo(), replaceText("Demo text"), closeSoftKeyboard())
    
    
    
    // Write memory tag
    val tagEditText = onView(
      allOf(
        withId(R.id.tagEditText),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            1
          ),
          0
        )
      )
    )
    tagEditText.perform(scrollTo(), replaceText("demo"), closeSoftKeyboard())
    
    
    
    // Click on add tag button
    val addTagImageView = onView(
      allOf(
        withId(R.id.addTagImageView),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            1
          ),
          1
        )
      )
    )
    addTagImageView.perform(scrollTo(), click())
    
    
    
    // Write another tag
    tagEditText.perform(scrollTo(), replaceText("demo tag"), closeSoftKeyboard())
    
    
    
    // Add an image to memory
    val addImageButton = onView(
      allOf(
        withId(R.id.addImageButton),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            2
          ),
          1
        )
      )
    )
    addImageButton.perform(scrollTo(), click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
    
    
    
    // Add a document to memory
    val addDocumentButton = onView(
      allOf(
        withId(R.id.addDocumentButton),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            3
          ),
          1
        )
      )
    )
    addDocumentButton.perform(scrollTo(), click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
    
    
    
    // Write link title
    val linkTitleEditText = onView(
      allOf(
        withId(R.id.linkTitleEditText),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    linkTitleEditText.perform(scrollTo(), replaceText("Link"), closeSoftKeyboard())
    
    
    
    // Write link address
    val linkAddressEditText = onView(
      allOf(
        withId(R.id.linkAddressEditText),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            0
          ),
          1
        ),
        isDisplayed()
      )
    )
    linkAddressEditText.perform(replaceText("google.com"), closeSoftKeyboard())
    
    
    
    // Click on add link button
    val addLinkImageView = onView(
      allOf(
        withId(R.id.addLinkImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.linkBase),
            1
          ),
          1
        )
      )
    )
    addLinkImageView.perform(scrollTo(), click())
    
    
    // Write another link. Only address this time.
    linkAddressEditText.perform(replaceText("google.com"), closeSoftKeyboard())
    
    
    
    // Click on add memory button
    val actionAddMemoryActivityDone = onView(
      allOf(
        withId(R.id.action_addmemoryactivity_done), withText("Done"),
        childAtPosition(
          childAtPosition(
            withId(R.id.toolbar),
            1
          ),
          0
        ),
        isDisplayed()
      )
    )
    actionAddMemoryActivityDone.perform(click())
    
    
    
    // Press back to go back to main activity
    pressBack()
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
    
    
    
    // Click search button to get all saved memories
    val searchImageView = onView(
      allOf(
        withId(R.id.search_image_view),
        childAtPosition(
          childAtPosition(
            withId(R.id.searchContainer),
            0
          ),
          0
        ),
        isDisplayed()
      )
    )
    searchImageView.perform(click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(1000)
    
    
    
    // Click on the first item of list view
    val listView = onData(anything())
      .inAdapterView(
        allOf(
          withId(R.id.list_view),
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            1
          )
        )
      )
      .atPosition(0)
    listView.perform(click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
    
    
    
    // Click on the first image of the memory to open it
    val imageContainer = onView(
      allOf(
        childAtPosition(
          allOf(
            withId(R.id.imageContainer),
            childAtPosition(
              withClassName(`is`("android.widget.LinearLayout")),
              2
            )
          ),
          0
        ),
        isDisplayed()
      )
    )
    imageContainer.perform(scrollTo(), click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
    
    
    
    // Click on the first link to open it
    val linkContainer = onView(
      allOf(
        childAtPosition(
          allOf(
            withId(R.id.linkContainer),
            childAtPosition(
              withId(R.id.linkBase),
              1
            )
          ),
          0
        ),
        isDisplayed()
      )
    )
    linkContainer.perform(scrollTo(), click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
    
    
    
    // Click on the first document to open it
    val document = onView(
      allOf(
        childAtPosition(
          allOf(
            withId(R.id.documentContainer),
            childAtPosition(
              withId(R.id.documentBase),
              1
            )
          ),
          0
        ),
        isDisplayed()
      )
    )
    document.perform(scrollTo(), click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
    
    
    
    // Click on the first tag to search it
    val tag = onView(
      allOf(
        withId(R.id.root),
        childAtPosition(
          childAtPosition(
            withId(R.id.tagContainer),
            1
          ),
          0
        ),
        isDisplayed()
      )
    )
    tag.perform(scrollTo(), click())
  
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
  
    
    
    //Click on edit memory button to edit the details of the memory
    val editButtonImageView = onView(
      allOf(
        withId(R.id.editButtonImageView),
        childAtPosition(
          allOf(
            withId(R.id.buttonContainer),
            childAtPosition(
              withClassName(`is`("android.widget.LinearLayout")),
              7
            )
          ),
          0
        ),
        isDisplayed()
      )
    )
    editButtonImageView.perform(click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
    
    
    // Change the memory's title
    titleEditText.perform(scrollTo(), replaceText("Demo Title Edited"), closeSoftKeyboard())
    
    
    
    // Delete the first tag
    val deleteTagImageView = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.tagContainer),
            0
          ),
          1
        ),
        isDisplayed()
      )
    )
    deleteTagImageView.perform(scrollTo(), click())
    
    
    
    // Add a new tag
    tagEditText.perform(replaceText("demo"))
    addTagImageView.perform(click())
  
    
    
    // Delete an image
    val deleteImageImageView = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.imageContainer),
            0
          ),
          2
        ),
        isDisplayed()
      )
    )
    deleteImageImageView.perform(scrollTo(), click())
    
    
    // Add an image
    addImageButton.perform(scrollTo(), click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
  
    
    
    // Delete a document
    val deleteDocImageView = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.documentContainer),
            0
          ),
          2
        ),
        isDisplayed()
      )
    )
    deleteDocImageView.perform(scrollTo(), click())
    
    
    
    // Add a document
    addDocumentButton.perform(scrollTo(), click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
  
    
    
    // Delete a link
    val deleteLinkImageView = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.linkContainer),
            0
          ),
          1
        ),
        isDisplayed()
      )
    )
    deleteLinkImageView.perform(scrollTo(), click())
    
    
    // Add a link
    linkTitleEditText.perform(scrollTo(), replaceText("Link2"), closeSoftKeyboard())
    linkAddressEditText.perform(scrollTo(), replaceText("google.com"), closeSoftKeyboard())
    
    
    
    // Click on add memory button
    actionAddMemoryActivityDone.perform(click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
    
    
    
    // Delete the memory
    val deleteButtonImageView = onView(
      allOf(
        withId(R.id.deleteButtonImageView),
        childAtPosition(
          allOf(
            withId(R.id.buttonContainer),
            childAtPosition(
              withClassName(`is`("android.widget.LinearLayout")),
              7
            )
          ),
          1
        ),
        isDisplayed()
      )
    )
    deleteButtonImageView.perform(scrollTo(), click())
    
    
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
    
    
    
    // Confirm the deletion
    val deleteButtonTextView = onView(
      allOf(
        withId(R.id.deleteButtonTextView), withText("Delete"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            1
          ),
          0
        ),
        isDisplayed()
      )
    )
    deleteButtonTextView.perform(click())
  }
  
  
  
  
  
  @Test
  fun searchBarTest(){
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
  
    
    
    // Search a memory
    val searchEditText = onView(
      allOf(
        withId(R.id.searchEditText),
        childAtPosition(
          childAtPosition(
            withId(R.id.searchContainer),
            0
          ),
          1
        ),
        isDisplayed()
      )
    )
    searchEditText.perform(replaceText("demo"), closeSoftKeyboard())
  
  
  
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(1000)
    
    
    
    // Click on clean search button
    val cleanSearchImageView = onView(
      allOf(
        withId(R.id.cleanSearch),
        childAtPosition(
          childAtPosition(
            withId(R.id.searchContainer),
            0
          ),
          2
        )
      )
    )
    cleanSearchImageView.perform(click())
  
    
    
    // If search edit text is not empty assert exception
    searchEditText.check(ViewAssertions.matches(withText("")))
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
