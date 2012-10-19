package au.com.dius.resilience.test.ui.activity;

import au.com.dius.resilience.test.AbstractResilienceActivityTestCase;
import au.com.dius.resilience.ui.activity.ResilienceActivity;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class au.com.dius.resilience.ResilienceActivityTest \
 * au.com.dius.resilience.tests/android.test.InstrumentationTestRunner
 */
public class ResilienceActivityTest extends AbstractResilienceActivityTestCase<ResilienceActivity> {

  public ResilienceActivityTest() {
    super(ResilienceActivity.class);
  }

  @Override
  protected void beforeTest() {
  }

  @Override
  protected void afterTest() {
//    solo.finishOpenedActivities();
  }

//  public void test_MainActivityShouldHaveTabs() {
//
//    assertNotNull(solo.getView(R.id.tab_list_view));
//    assertNotNull(solo.getView(R.id.tab_map_view));
//
//  }
//
//  public void test_ListTabShouldBeSelectedByDefault() {
//    assertEquals("expected list view tab to be selected", R.id.tab_list_view, getCurrentlySelectedTabId());
//  }
//
//  public void test_ShouldBeAbleToSelectMapTab() {
//    solo.clickOnView(solo.getView(R.id.tab_map_view));
//    assertEquals("expected map view tab to be selected", R.id.tab_map_view, getCurrentlySelectedTabId());
//  }
//
//  public void test_ShouldBeAbleToSelectListTab() {
//    solo.clickOnView(solo.getView(R.id.tab_list_view));
//    assertEquals("expected map view tab to be selected", R.id.tab_list_view, getCurrentlySelectedTabId());
//  }
//
//  private int getCurrentlySelectedTabId() {
//    System.out.println("Current views" + solo.getCurrentViews());
//    System.out.println("Current buttons" + solo.getCurrentButtons());
//    System.out.println("Current image buttons" + solo.getCurrentImageButtons());
//
//    for (View view : solo.getCurrentViews()) {
//      System.out.println("view class is : " + view.getClass().getName() + "id is " + view.getId());
//      if (view instanceof TabHost) {
//        return ((TabHost)view).getCurrentTabView().getId();
//      }
//    }
//    return -1;
//  }
}


