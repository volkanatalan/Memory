package com.volkanatalan.memory.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.volkanatalan.memory.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), View.OnClickListener {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_about)
    link1.setOnClickListener(this)
    link2.setOnClickListener(this)
    link3.setOnClickListener(this)
    link4.setOnClickListener(this)
    link5.setOnClickListener(this)
  }
  
  
  override fun onClick(v: View?) {
    if (v == link2 ||
      v == link3 ||
      v == link4 ||
      v == link5 ||
      v == link6){
      
      val linkTextView = v as TextView
      var link = linkTextView.text as String
  
      if (!link.startsWith("http://") && !link.startsWith("https://"))
        link = "http://$link"
      
      startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }
    
    else if (v == link1){
      val emailTextView = v as TextView
      val email = emailTextView.text as String
      
      // Copy to clipboard
      val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      val clip = ClipData.newPlainText("email", email)
      clipboard.primaryClip = clip
      
      // Inform user that e-mail has been copied
      Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show()
    }
  }
}
