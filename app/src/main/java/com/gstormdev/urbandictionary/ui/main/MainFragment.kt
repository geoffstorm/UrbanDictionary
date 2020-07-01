package com.gstormdev.urbandictionary.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.gstormdev.urbandictionary.R
import com.gstormdev.urbandictionary.api.Status
import com.gstormdev.urbandictionary.databinding.MainFragmentBinding
import com.gstormdev.urbandictionary.di.Injectable
import com.gstormdev.urbandictionary.ui.RecyclerViewSpacing
import com.gstormdev.urbandictionary.util.hideKeyboard
import javax.inject.Inject

@Injectable
class MainFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    private val definitionAdapter = DefinitionAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = definitionAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(RecyclerViewSpacing(context.resources.getDimension(R.dimen.recycler_child_spacing).toInt(), 1))
        }

        binding.btnSearch.setOnClickListener { handleSearch(it) }
        binding.edSearch.setOnEditorActionListener { textView, actionId, _ ->
            return@setOnEditorActionListener when(actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    handleSearch(textView)
                    true
                }
                else -> false
            }
        }

        viewModel.definitions.observe(viewLifecycleOwner, Observer {
            Log.e("MainFragment", "Resource status is ${it.status.name}")
            binding.progress.visibility = if (it.status == Status.LOADING) View.VISIBLE else View.GONE

            it.data?.let { list ->
                definitionAdapter.setData(list)
            }

            it.message?.let { msg ->
                // This is mostly for demonstration purposes.
                // The message is non-null when an API error occurs, and the message
                // is generally not user-friendly.
                // If the desire is to let users know when an error has occurred, this
                // needs to be reevaluated
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
            }
        })

        viewModel.searchTermError.observe(viewLifecycleOwner, Observer {
            it?.let { Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show() }
        })

        viewModel.emptyText.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                binding.tvEmpty.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
                binding.tvEmpty.text = it
            }
        })
    }

    private fun handleSearch(view: View) {
        view.hideKeyboard()
        viewModel.retrieveDefinitions(binding.edSearch.text.toString())
    }
}