package com.thallo.stage.broswer.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import androidx.compose.foundation.layout.R
import androidx.preference.PreferenceManager
import com.thallo.stage.componets.MyDialog
import com.thallo.stage.databinding.DiaEngineSelectBinding

class SearchDialog(context: Context) : MyDialog(context) {
    lateinit var binding:DiaEngineSelectBinding
    init {
        onCreate(context)
    }

    fun onCreate(context: Context?) {
        binding = DiaEngineSelectBinding.inflate(LayoutInflater.from(context))
        val prefs = context?.let { PreferenceManager.getDefaultSharedPreferences(it) }
        val isDiy = prefs?.getBoolean("switch_diy",false)


        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->

            when(findViewById<RadioButton>(checkedId)){
                binding.radioButton1 -> {prefs?.edit()?.putString("searchEngine",getContext().getString(com.thallo.stage.R.string.baidu))
                    ?.commit()
                    prefs?.edit()?.putBoolean("switch_diy",false)?.commit()

                }
                binding.radioButton2 -> {prefs?.edit()?.putString("searchEngine",getContext().getString(com.thallo.stage.R.string.google))
                    ?.commit()
                    prefs?.edit()?.putBoolean("switch_diy",false)?.commit()

                }
                binding.radioButton3 -> {prefs?.edit()?.putString("searchEngine",getContext().getString(com.thallo.stage.R.string.bing))
                    ?.commit()
                    prefs?.edit()?.putBoolean("switch_diy",false)?.commit()

                }
                binding.radioButton4 -> {prefs?.edit()?.putString("searchEngine",getContext().getString(com.thallo.stage.R.string.sogou))
                    ?.commit()
                    prefs?.edit()?.putBoolean("switch_diy",false)?.commit()

                }

            }
            dismiss()
        }

        if (prefs != null) {
            when(prefs.getString("searchEngine",getContext().getString(com.thallo.stage.R.string.baidu))){
                getContext().getString(com.thallo.stage.R.string.baidu) -> binding.radioButton1.isChecked=true
                getContext().getString(com.thallo.stage.R.string.google) -> binding.radioButton2.isChecked=true
                getContext().getString(com.thallo.stage.R.string.bing) -> binding.radioButton3.isChecked=true
                getContext().getString(com.thallo.stage.R.string.sogou) -> binding.radioButton4.isChecked=true

            }
        }
        if (isDiy == true){
            binding.radioButton5.visibility = View.VISIBLE
            binding.radioButton5.isChecked = true
        }
        else
            binding.radioButton5.visibility = View.GONE
        setView(binding.root)
    }


}