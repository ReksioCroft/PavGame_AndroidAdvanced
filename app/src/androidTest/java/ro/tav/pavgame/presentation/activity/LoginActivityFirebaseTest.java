package ro.tav.pavgame.presentation.activity;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ro.tav.pavgame.R;

@LargeTest
@RunWith( AndroidJUnit4.class )
public class LoginActivityFirebaseTest {

    @Rule
    public ActivityScenarioRule< LoginActivity > mActivityScenarioRule =
            new ActivityScenarioRule<>( LoginActivity.class );

    @Test
    public void loginActivityFirebaseTest() {
        ViewInteraction editText = onView(
                allOf( withId( R.id.email ), withText( "Email" ),
                        withParent( withParent( IsInstanceOf.< View > instanceOf( android.widget.ScrollView.class ) ) ),
                        isDisplayed() ) );
        editText.check( matches( isDisplayed() ) );

        ViewInteraction editText2 = onView(
                allOf( withId( R.id.password ), withText( "Parola" ),
                        withParent( withParent( IsInstanceOf.< View > instanceOf( android.widget.ScrollView.class ) ) ),
                        isDisplayed() ) );
        editText2.check( matches( isDisplayed() ) );

        ViewInteraction button = onView(
                allOf( withId( R.id.logInButton ), withText( "INTRATI" ),
                        withParent( withParent( IsInstanceOf.< View > instanceOf( android.widget.ScrollView.class ) ) ),
                        isDisplayed() ) );
        button.check( matches( isDisplayed() ) );

        ViewInteraction appCompatEditText = onView(
                allOf( withId( R.id.email ),
                        childAtPosition(
                                childAtPosition(
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                1 ) ) );
        appCompatEditText.perform( scrollTo(), replaceText( "octavian.staicu@s.unibuc.ro" ), closeSoftKeyboard() );

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
    }

    private static Matcher< View > childAtPosition(
            final Matcher< View > parentMatcher, final int position ) {

        return new TypeSafeMatcher< View >() {
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
