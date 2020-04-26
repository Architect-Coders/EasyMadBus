package com.developer.ivan.easymadbus

import com.developer.ivan.easymadbus.ui.bus.UIBusScreenTest
import com.developer.ivan.easymadbus.ui.favourite.UIFavouriteScreenTest
import com.developer.ivan.easymadbus.ui.favourite.detail.UIFavouriteDetailScreenTest
import org.junit.runner.RunWith
import org.junit.runners.Suite
import org.junit.runners.Suite.SuiteClasses

@RunWith(Suite::class)
@SuiteClasses(UIBusScreenTest::class, UIFavouriteScreenTest::class, UIFavouriteDetailScreenTest::class)
class UITestSuite

