package com.maxrzhe.presentation.navigation

import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.maxrzhe.presentation.ui.base.CoreFragment

fun FragmentActivity.listenToRouter(router: Router) {
    router.activityNavigationEvent.observe(this, { navEvent ->
        val consume = navEvent?.consume()
        consume?.invoke(this)
    })
}

fun CoreFragment.listenToRouter(router: Router) {
    router.fragmentNavigationEvent.observe(viewLifecycleOwner, { navEvent ->
        val consume: (NavController.() -> Any)? = navEvent?.consume()
        consume?.invoke(findNavController())
    })
}