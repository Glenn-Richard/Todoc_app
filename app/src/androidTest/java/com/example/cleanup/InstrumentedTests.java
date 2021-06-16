package com.example.cleanup;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.cleanup.ui.MainActivity;
import com.example.cleanup.utils.RecyclerViewItemCountAssertion;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class InstrumentedTests extends RecyclerViewItemCountAssertion {


    final String TASK_1 = "task 1";
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    public InstrumentedTests(int expectedCount) {
        super(expectedCount);
    }

    @Before
    public void setup(){
        MainActivity mActivity = mActivityRule.getActivity();
        Assert.assertNotNull(mActivity);
    }

    @Test
    public void checkItemsAreDisplayed(){

        onView(allOf(isDisplayed(),withId(R.id.list_tasks)));
        onView(allOf(withId(R.id.list_tasks))).check(new RecyclerViewItemCountAssertion(0));
    }

    @Test
    public void checkItemIsAppended(){
        onView(withId(R.id.fab_add_task)).perform(click());
        onView(withId(R.id.txt_task_name)).perform(replaceText(TASK_1));
        onView(withText("AJOUTER")).perform(click());
        onView(allOf(withId(R.id.list_tasks))).check(new RecyclerViewItemCountAssertion(1));
    }

    @Test
    public void checkItemIsDeleted(){
        onView(withId(R.id.list_tasks)).check(new RecyclerViewItemCountAssertion(1));
        onView(withId(R.id.list_tasks)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click((ViewAction) withId(R.id.img_delete))));
        onView(allOf(withId(R.id.list_tasks))).check(new RecyclerViewItemCountAssertion(0));
    }
}
