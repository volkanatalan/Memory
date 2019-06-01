package com.volkanatalan.memory.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volkanatalan.memory.R
import com.volkanatalan.memory.classes.Memory
import com.volkanatalan.memory.databases.MemoryDatabase
import kotlinx.android.synthetic.main.fragment_delete_confirmation.view.*

/**
 * DeleteConfirmationFragment is used to ask confirmation for deleting memory from database irreversibly.
 */
class DeleteConfirmationFragment : Fragment() {
  
  lateinit var mMemory: Memory
  
  
  
  companion object {
    
    fun newInstance(memory: Memory): DeleteConfirmationFragment {
      val fragment = DeleteConfirmationFragment()
      fragment.mMemory = memory
      return fragment
    }
  }
  
  
  
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val view: View = inflater.inflate(R.layout.fragment_delete_confirmation, container, false)
    val deleteButton = view.deleteButtonTextView
    val cancelButton = view.cancelTextView
    
    cancelButton.setOnClickListener {
      activity?.onBackPressed()
    }
    
    deleteButton.setOnClickListener {
      activity?.onBackPressed()
      onDeleteClickListener.onClick(mMemory)
    }
    
    return view
  }
  
  
  
  
  lateinit var onDeleteClickListener: OnDeleteClickListener
  
  interface OnDeleteClickListener{
    fun onClick(memory: Memory)
  }
}
