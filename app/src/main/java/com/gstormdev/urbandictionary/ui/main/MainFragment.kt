package com.gstormdev.urbandictionary.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.gstormdev.urbandictionary.databinding.MainFragmentBinding
import com.gstormdev.urbandictionary.di.Injectable
import javax.inject.Inject

@Injectable
class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.definitions.observe(viewLifecycleOwner, Observer {
            Log.e("MainFragment", "Resource status is ${it.status.name}")
        })

        viewModel.searchTermError.observe(viewLifecycleOwner, Observer {
            it?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show() }
        })

        viewModel.retrieveDefinitions("wat")  // testing
    }

}