package com.example.task_master;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class TestEspresso {
//    @Rule
//    public ActivityScenarioRule<MainActivity> activityScenarioRule =
//            new ActivityScenarioRule<>(MainActivity.class);

        @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testGoToSetting(){
        onView(withId(R.id.button4)).perform(click());
        onView(withId(R.id.textView9)).check(matches(withText("Settings")));
          }
    @Test
    public void testShowName() {
        onView(withId(R.id.button4)).perform(click());
        onView(withId(R.id.editTextTextPersonName3)).perform(typeText("Alaa"), closeSoftKeyboard());
        onView(withId(R.id.button7)).perform(click());
        onView(withId(R.id.textView6)).check(matches(withText("Alaa's Tasks")));
    }
    @Test
    public void testShowDetails(){

        onView(withId(R.id.IdList)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        onView(withId(R.id.textView7)).check(matches(withText("Math")));
        onView(withId(R.id.textView8)).check(matches(withText("ex 1")));
    }
    @Test
    public void testAddTask(){
        onView(withId(R.id.button)).perform(click());
        onView(withId(R.id.inputTitle)).perform(typeText("Math"), closeSoftKeyboard());
        onView(withId(R.id.inputBody)).perform(typeText("ex 1"), closeSoftKeyboard());
        onView(withId(R.id.button2)).perform(click());
        onView(withId(R.id.IdList)).perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        onView(withId(R.id.textView7)).check(matches(withText("Math")));
        onView(withId(R.id.textView8)).check(matches(withText("ex 1")));
    }

}
