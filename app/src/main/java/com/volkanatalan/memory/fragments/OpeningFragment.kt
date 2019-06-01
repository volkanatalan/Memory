package com.volkanatalan.memory.fragments


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import com.volkanatalan.memory.R
import com.volkanatalan.memory.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_opening.view.*


class OpeningFragment : Fragment() {
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_opening, container, false)
    val density = resources.displayMetrics.density
    val mLetterX = view.m_letter.x
    val mLetterToX = mLetterX - (80 * density)
  
  
  
    val mLetterAlphaAnimator = ObjectAnimator.ofFloat(view.m_letter, "alpha", 1f)
    mLetterAlphaAnimator.startDelay = 500
    mLetterAlphaAnimator.duration = 500
  
    val mLetterTranslateAnimator = ObjectAnimator.ofFloat(view.m_letter, "translationX", mLetterToX)
    mLetterTranslateAnimator.duration = 500
    mLetterTranslateAnimator.interpolator = AccelerateDecelerateInterpolator()
  
    val brainAlphaAnimator = ObjectAnimator.ofFloat(view.brain, "alpha", 0.3f)
    mLetterAlphaAnimator.duration = 500
  
    val textAlphaAnimator = ObjectAnimator.ofFloat(view.text, "alpha", 1f)
    textAlphaAnimator.duration = 500
  
    val brainScaleXAnimator = ObjectAnimator.ofFloat(view.brain, "scaleX", 10f)
    brainScaleXAnimator.startDelay = 500
    brainScaleXAnimator.duration = 500
  
    val brainScaleYAnimator = ObjectAnimator.ofFloat(view.brain, "scaleY", 10f)
    brainScaleYAnimator.duration = 500
    
    val animSet = AnimatorSet()
    animSet.play(mLetterTranslateAnimator).with(brainAlphaAnimator)
    animSet.play(mLetterTranslateAnimator).with(textAlphaAnimator)
    animSet.play(mLetterAlphaAnimator).before(mLetterTranslateAnimator)
    animSet.play(mLetterTranslateAnimator).before(brainScaleXAnimator)
    animSet.play(brainScaleXAnimator).with(brainScaleYAnimator)
    animSet.doOnEnd {
      val mainActivity = activity as MainActivity
      mainActivity.onBackPressed()
      mainActivity.hideKeyboard(false)
    }
    animSet.start()
    
    return view
  }
  
  
}
