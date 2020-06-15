package com.maku.calendate.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maku.calendate.R
import com.maku.calendate.data.adapters.RemindersAdapters
import com.maku.calendate.data.db.entities.Reminder
import com.maku.calendate.data.db.interfaces.ReminderInterface
import com.maku.calendate.databinding.ListFragmentBinding
import timber.log.Timber

class ListFragment : Fragment(), PostBottomDialogFragment.ItemClickListener, ReminderInterface {

    private lateinit var mFragmentListBinding: ListFragmentBinding

    private var callback: ReminderInterface? = null

    private lateinit var mListViewModel: ListViewModel

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mFragmentListBinding = DataBindingUtil.inflate(
            inflater, R.layout.list_fragment, container, false)

        val adapter = RemindersAdapters(requireContext())
        mFragmentListBinding.recyclerView?.adapter = adapter
        mFragmentListBinding.recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        mListViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        mListViewModel.allWords.observe(requireActivity(), Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })

        mFragmentListBinding.button.setOnClickListener {

            showBottomSheet()

        }

        return mFragmentListBinding.root
    }


    fun showBottomSheet() {
        val addPhotoBottomDialogFragment: PostBottomDialogFragment = PostBottomDialogFragment.newInstance(callback)
        activity?.getSupportFragmentManager()?.let {
            addPhotoBottomDialogFragment.show(
                it,
                PostBottomDialogFragment.TAG
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onItemClick(item: String?) {
        Timber.d("desc " + item)
    }

    override fun getData(description: String?) {
        TODO("Not yet implemented")
    }

    override fun delete(reminder: Reminder) {
        mListViewModel.delete(reminder)
    }

}