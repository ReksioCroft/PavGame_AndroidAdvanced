package ro.tav.pavgame.presentation.view;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.tav.pavgame.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith( AndroidJUnit4.class )
public class GameWin4 {

    @Rule
    public ActivityTestRule < LoginActivity > mActivityTestRule = new ActivityTestRule <>( LoginActivity.class );

    @Test
    public void gameWin4() {
        ViewInteraction appCompatEditText = onView(
                allOf( withId( R.id.email ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                1 ) ) );
        appCompatEditText.perform( scrollTo(), replaceText( "tavi.imaginatie@yahoo.com" ), closeSoftKeyboard() );

        ViewInteraction appCompatEditText2 = onView(
                allOf( withId( R.id.password ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                2 ) ) );
        appCompatEditText2.perform( scrollTo(), replaceText( "tav713" ), closeSoftKeyboard() );

        ViewInteraction appCompatButton = onView(
                allOf( withId( R.id.logInButton ), withText( "Intrati" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                3 ) ) );
        appCompatButton.perform( scrollTo(), click() );

        ViewInteraction appCompatImageButton = onView(
                allOf( withContentDescription( "Deschide navigation drawer" ),
                        childAtPosition(
                                allOf( withId( R.id.toolbar ),
                                        childAtPosition(
                                                withClassName( is( "com.google.android.material.appbar.AppBarLayout" ) ),
                                                0 ) ),
                                1 ),
                        isDisplayed() ) );
        appCompatImageButton.perform( click() );

        ViewInteraction navigationMenuItemView = onView(
                allOf( withId( R.id.nav_game ),
                        childAtPosition(
                                allOf( withId( R.id.design_navigation_view ),
                                        childAtPosition(
                                                withId( R.id.nav_view ),
                                                0 ) ),
                                2 ),
                        isDisplayed() ) );
        navigationMenuItemView.perform( click() );

        ViewInteraction appCompatEditText3 = onView(
                allOf( withId( R.id.pavGameInputText ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ) ) );
        appCompatEditText3.perform( scrollTo(), replaceText( "2" ), closeSoftKeyboard() );

        ViewInteraction appCompatButton2 = onView(
                allOf( withId( R.id.startGameButton ), withText( "START GAME" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                1 ) ) );
        appCompatButton2.perform( scrollTo(), click() );

        ViewInteraction button = onView(
                childAtPosition(
                        allOf( withId( R.id.pavGameBoard ),
                                childAtPosition(
                                        withClassName( is( "androidx.constraintlayout.widget.ConstraintLayout" ) ),
                                        3 ) ),
                        6 ) );
        button.perform( scrollTo(), click() );

        ViewInteraction appCompatEditText4 = onView(
                allOf( withId( R.id.pavGameInputText ), withText( "2" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ) ) );
        appCompatEditText4.perform( scrollTo(), replaceText( "3" ) );

        ViewInteraction appCompatEditText5 = onView(
                allOf( withId( R.id.pavGameInputText ), withText( "3" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ),
                        isDisplayed() ) );
        appCompatEditText5.perform( closeSoftKeyboard() );

        ViewInteraction button2 = onView(
                childAtPosition(
                        allOf( withId( R.id.pavGameBoard ),
                                childAtPosition(
                                        withClassName( is( "androidx.constraintlayout.widget.ConstraintLayout" ) ),
                                        3 ) ),
                        9 ) );
        button2.perform( scrollTo(), click() );

        ViewInteraction button3 = onView(
                childAtPosition(
                        allOf( withId( R.id.pavGameBoard ),
                                childAtPosition(
                                        withClassName( is( "androidx.constraintlayout.widget.ConstraintLayout" ) ),
                                        3 ) ),
                        12 ) );
        button3.perform( scrollTo(), click() );

        ViewInteraction appCompatEditText6 = onView(
                allOf( withId( R.id.pavGameInputText ), withText( "3" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ) ) );
        appCompatEditText6.perform( scrollTo(), replaceText( "1" ) );

        ViewInteraction appCompatEditText7 = onView(
                allOf( withId( R.id.pavGameInputText ), withText( "1" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ),
                        isDisplayed() ) );
        appCompatEditText7.perform( closeSoftKeyboard() );

        ViewInteraction button4 = onView(
                childAtPosition(
                        allOf( withId( R.id.pavGameBoard ),
                                childAtPosition(
                                        withClassName( is( "androidx.constraintlayout.widget.ConstraintLayout" ) ),
                                        3 ) ),
                        0 ) );
        button4.perform( scrollTo(), click() );

        ViewInteraction appCompatEditText8 = onView(
                allOf( withId( R.id.pavGameInputText ), withText( "1" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ) ) );
        appCompatEditText8.perform( scrollTo(), replaceText( "2" ) );

        ViewInteraction appCompatEditText9 = onView(
                allOf( withId( R.id.pavGameInputText ), withText( "2" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ),
                        isDisplayed() ) );
        appCompatEditText9.perform( closeSoftKeyboard() );

        ViewInteraction button5 = onView(
                childAtPosition(
                        allOf( withId( R.id.pavGameBoard ),
                                childAtPosition(
                                        withClassName( is( "androidx.constraintlayout.widget.ConstraintLayout" ) ),
                                        3 ) ),
                        3 ) );
        button5.perform( scrollTo(), click() );

        ViewInteraction appCompatEditText10 = onView(
                allOf( withId( R.id.pavGameInputText ), withText( "2" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ) ) );
        appCompatEditText10.perform( scrollTo(), replaceText( "4" ) );

        ViewInteraction appCompatEditText11 = onView(
                allOf( withId( R.id.pavGameInputText ), withText( "4" ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                0 ),
                        isDisplayed() ) );
        appCompatEditText11.perform( closeSoftKeyboard() );

        ViewInteraction button6 = onView(
                childAtPosition(
                        allOf( withId( R.id.pavGameBoard ),
                                childAtPosition(
                                        withClassName( is( "androidx.constraintlayout.widget.ConstraintLayout" ) ),
                                        3 ) ),
                        15 ) );
        button6.perform( scrollTo(), click() );
    }

    private static Matcher < View > childAtPosition(
            final Matcher < View > parentMatcher, final int position ) {

        return new TypeSafeMatcher < View >() {
            @Override
            public void describeTo( Description description ) {
                description.appendText( "Child at position " + position + " in parent " );
                parentMatcher.describeTo( description );
            }

            @Override
            public boolean matchesSafely( View view ) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches( parent )
                        && view.equals( ( ( ViewGroup ) parent ).getChildAt( position ) );
            }
        };
    }
}
