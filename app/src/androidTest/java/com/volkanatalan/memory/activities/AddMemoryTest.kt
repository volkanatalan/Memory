package com.volkanatalan.memory.activities


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
class AddMemoryTest {
  
  @Rule
  @JvmField
  var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
  
  @Test
  fun addMemoryTest() {
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
  
    val actionMainActivityAdd = onView(
      allOf(
        withId(R.id.action_mainactivity_add), withContentDescription("Add memory"),
        childAtPosition(
          childAtPosition(
            withId(R.id.toolbar),
            1
          ),
          0
        )
      )
    )
    actionMainActivityAdd.perform(click())
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
  
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
    titleEditText.perform(scrollTo(), replaceText("Demo"), closeSoftKeyboard())
  
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
    textEditText.perform(scrollTo(), replaceText("Text"), closeSoftKeyboard())
  
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
  
  
    tagEditText.perform(scrollTo(), replaceText("demo"), closeSoftKeyboard())
  
  
    addTagImageView.perform(scrollTo(), click())
  
  
    tagEditText.perform(scrollTo(), replaceText("demo2"), closeSoftKeyboard())
  
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
  
    val imageContainer = onView(
      allOf(
        withId(R.id.imageContainer)
      )
    )
    imageContainer.perform(scrollTo())
  
    val deleteImageImageView = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.imageContainer),
            1
          ),
          2
        )
      )
    )
    deleteImageImageView.perform(click())
  
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
  
    val documentContainer = onView(
      allOf(
        withId(R.id.documentContainer)
      )
    )
    documentContainer.perform(scrollTo())
  
    val deleteDocumentImageView = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.documentContainer),
            0
          ),
          2
        )
      )
    )
    deleteDocumentImageView.perform(click())
  
  
    addDocumentButton.perform(scrollTo(), click())
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
  
  
    addImageButton.perform(scrollTo(), click())
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
  
    val linkTitleEditText = onView(
      allOf(
        withId(R.id.linkTitleEditText),
        childAtPosition(
          childAtPosition(
            childAtPosition(
              withId(R.id.linkBase),
              1
            ),
            0
          ),
          0
        )
      )
    )
    linkTitleEditText.perform(scrollTo(), replaceText("Google"), closeSoftKeyboard())
  
    val linkAddressEditText = onView(
      allOf(
        withId(R.id.linkAddressEditText),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            0
          ),
          1
        )
      )
    )
    linkAddressEditText.perform(scrollTo(), replaceText("google.com"), closeSoftKeyboard())
  
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
  
  
    linkAddressEditText.perform(replaceText("google.com"), closeSoftKeyboard())
  
    val actionAddMemoryActivityDone = onView(
      allOf(
        withId(R.id.action_addmemoryactivity_done), withText("Done"),
        childAtPosition(
          childAtPosition(
            withId(R.id.toolbar),
            1
          ),
          0
        )
      )
    )
    actionAddMemoryActivityDone.perform(click(), closeSoftKeyboard())
  
    Thread.sleep(1000)
  }
  
  
  
  
  @Test
  fun interactMemory(){
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
    
    val searchButton = onView(
      allOf(
        withId(R.id.search_image_view),
        childAtPosition(
          childAtPosition(
            withId(R.id.search_container),
            0
          ),
          0
        )
      )
    )
    searchButton.perform(click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(1000)
    
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
    Thread.sleep(1000)
    
    val memory = withParent(
      childAtPosition(
        withId(R.id.search_results_container),
        0
      ))
    
    val image = onView(
      allOf(
        withId(R.id.memory_image_view),
        childAtPosition(
          childAtPosition(
            allOf(
              withId(R.id.imageContainer),
              withParent(memory)),
          0
          ),
          0
        ))
    )
    image.perform(scrollTo(), click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
    
    val link = onView(
      allOf(
        childAtPosition(
          allOf(
            withId(R.id.linkContainer),
            childAtPosition(
              allOf(
                withId(R.id.linkBase),
                withParent(memory)
              ),
              1
            )
          ),
          0
        )
      )
    )
    link.perform(scrollTo(), click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
    
    val document = onView(
      allOf(
        childAtPosition(
          allOf(
            withId(R.id.documentContainer),
            childAtPosition(
              allOf(
                withId(R.id.documentBase), withParent(memory)
              ),
              1
            )
          ),
          1
        )
      )
    )
    document.perform(scrollTo(), click())
    
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
    
    val tag = onView(
      allOf(
        withId(R.id.root),
        childAtPosition(
          childAtPosition(
            allOf(
            withId(R.id.tagContainer),
              withParent(memory)
            ),
            2
          ),
          0
        )
      )
    )
    tag.perform(scrollTo(), click())
  }
  
  
  
  
  @Test
  fun editAndDeleteMemory(){
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(3000)
  
    val searchButton = onView(
      allOf(
        withId(R.id.search_image_view),
        childAtPosition(
          childAtPosition(
            withId(R.id.search_container),
            0
          ),
          0
        )
      )
    )
    searchButton.perform(click())
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(1000)
  
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
    Thread.sleep(1000)
  
    val memory = withParent(
      childAtPosition(
        withId(R.id.search_results_container),
        0
      ))
  
  
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
  
  
    val editButtonImageView = onView(
      allOf(
        withId(R.id.editButtonImageView),
        childAtPosition(
          allOf(
            withId(R.id.buttonContainer),
            withParent(memory)
          ),
          0
        )
      )
    )
    editButtonImageView.perform(scrollTo(), click())
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(1000)
  
  
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
    titleEditText.perform(scrollTo(), click(), replaceText("Demo2"),closeSoftKeyboard())
  
  
  
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
    textEditText.perform(scrollTo(), replaceText("Text2"), closeSoftKeyboard())
  
  
  
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
    tagEditText.perform(scrollTo(), replaceText("demo3"), closeSoftKeyboard())
  
    val deleteTag = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.tagContainer),
            1
          ),
          1
        )
      )
    )
    deleteTag.perform(scrollTo(), click())
  
    val deleteImageImageView2 = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.imageContainer),
            0
          ),
          2
        )
      )
    )
    deleteImageImageView2.perform(scrollTo(), click())
  
  
  
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
  
    val deleteDocumentImageView2 = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.documentContainer),
            0
          ),
          2
        )
      )
    )
    deleteDocumentImageView2.perform(scrollTo(), click())
  
  
  
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
  
    val deleteLinkImageView = onView(
      allOf(
        withId(R.id.deleteImageView),
        childAtPosition(
          childAtPosition(
            withId(R.id.linkContainer),
            0
          ),
          1
        )
      )
    )
    deleteLinkImageView.perform(scrollTo(), click())
  
  
  
    val linkAddressEditText = onView(
      allOf(
        withId(R.id.linkAddressEditText),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            0
          ),
          1
        )
      )
    )
    linkAddressEditText.perform(scrollTo(), replaceText("facebook.com"), closeSoftKeyboard())
  
  
  
    val actionAddMemoryActivityDone = onView(
      allOf(
        withId(R.id.action_addmemoryactivity_done), withText("Done"),
        childAtPosition(
          childAtPosition(
            withId(R.id.toolbar),
            1
          ),
          0
        )
      )
    )
    actionAddMemoryActivityDone.perform(click())
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(5000)
  
    val clearSearchButton = onView(
      allOf(
        withId(R.id.clear_search),
        childAtPosition(
          childAtPosition(
            withId(R.id.search_container),
            0
          ),
          2
        )
      )
    )
    clearSearchButton.perform(click())
  
  
  
    val searchEditText = onView(
      allOf(
        withId(R.id.search_edit_text),
        childAtPosition(
          childAtPosition(
            withId(R.id.search_container),
            0
          ),
          1
        )
      )
    )
    searchEditText.perform(replaceText("de"), closeSoftKeyboard())
  
    Thread.sleep(2000)
  
    val deleteButtonImageView = onView(
      allOf(
        withId(R.id.deleteButtonImageView),
        childAtPosition(
          allOf(
            withId(R.id.buttonContainer),
            withParent(memory)
          ),
          1
        )
      )
    )
    deleteButtonImageView.perform(scrollTo())
  
    Thread.sleep(1000)
  
    deleteButtonImageView.perform(click())
  
    // Added a sleep statement to match the app's execution delay.
    // The recommended way to handle such scenarios is to use Espresso idling resources:
    // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
    Thread.sleep(2000)
  
    val deleteConfirmationButton = onView(
      allOf(
        withId(R.id.deleteButtonTextView), withText("Delete"),
        childAtPosition(
          childAtPosition(
            withClassName(`is`("android.widget.LinearLayout")),
            1
          ),
          0
        )
      )
    )
    deleteConfirmationButton.perform(click())
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
