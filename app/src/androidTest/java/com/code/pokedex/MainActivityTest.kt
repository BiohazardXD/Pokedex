package com.code.pokedex

import android.view.Gravity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.code.pokedex.presentation.ui.MainActivity
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        val instance: OkHttpClient = OkHttpClient.Builder().build()
        //val resource : IdlingResource = OkHttp3IdlingResource.create("okhttp", instance)
        //IdlingRegistry.getInstance().register(resource)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testPokedexFragmentsNavigation() {

        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open());

        onView(withId(R.id.nav_home)).check(matches((isDisplayed())));
        onView(withId(R.id.nav_favorite)).check(matches((isDisplayed())));

        onView(withId(R.id.nav_home)).perform(click());

        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open());

        onView(withId(R.id.nav_favorite)).perform(click());

        onView(withId(R.id.drawer_layout))
            .check(matches(DrawerMatchers.isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open());
    }

}