package com.developer.ivan.easymadbus.presentation.favourites


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.developer.ivan.easymadbus.R
import com.developer.ivan.easymadbus.core.inflateFragment

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflateFragment(R.layout.fragment_favourite)


}
