package com.geekbrains.myweather_kotlin1.presentation.contacts

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.geekbrains.myweather_kotlin1.R
import com.geekbrains.myweather_kotlin1.databinding.ContactsFragmentBinding
import com.geekbrains.myweather_kotlin1.model.AppState
import com.geekbrains.myweather_kotlin1.model.showSnackBar
import com.geekbrains.myweather_kotlin1.presentation.history.HistoryFragment
import kotlinx.android.synthetic.main.contacts_fragment.*
import kotlinx.android.synthetic.main.history_fragment.*

class ContactsFragment : Fragment() {
    private var _binding: ContactsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactsViewModel by lazy {
        ViewModelProvider(this)[ContactsViewModel::class.java]
    }

    private val adapter: ContactsAdapter by lazy {
        ContactsAdapter(object : OnContactItemViewClickListener {
            override fun onContactItemViewClick(number: String) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    activity?.let { it ->
                        ActivityCompat.requestPermissions(
                            it, arrayOf(android.Manifest.permission.CALL_PHONE),
                            14
                        )
                    }
                } else {

// else block means user has already accepted.And make your phone call here.
                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel: ${number}")
                    startActivity(callIntent)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ContactsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactsFragmentRecyclerview.adapter = adapter
        viewModel.contactsLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        getContacts()
    }

    private fun getContacts() {
        context?.let {
            viewModel.getContacts(requireContext().contentResolver)
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.contactsFragmentRecyclerview.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                adapter.setData(appState.appData.contacts)
            }
            is AppState.Loading -> {
                binding.contactsFragmentRecyclerview.visibility = View.GONE
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.contactsFragmentRecyclerview.visibility = View.VISIBLE
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                binding.contactsFragmentRecyclerview.showSnackBar(
                    getString(R.string.loading_error),
                    getString(R.string.repeat_loading),
                    {   getContacts() })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface OnContactItemViewClickListener {
    fun onContactItemViewClick(number: String)
}
