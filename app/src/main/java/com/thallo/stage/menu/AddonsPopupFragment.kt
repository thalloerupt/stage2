package com.thallo.stage.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thallo.stage.R
import com.thallo.stage.componets.MenuAddonsAdapater
import com.thallo.stage.databinding.FragmentAddonsBinding
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddonsPopupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddonsPopupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentAddonsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddonsBinding.inflate(LayoutInflater.from(context))
        var adapter= TabMenuAddonsAdapater()
        binding.recyclerView3.layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        binding.recyclerView3.adapter=adapter
        context?.let { it ->
            GeckoRuntime.getDefault(it).webExtensionController.list().accept {
                if(it.isNullOrEmpty())
                    binding.tabAddonsView.visibility=View.GONE
                else
                    binding.tabAddonsView.visibility=View.VISIBLE
                adapter.submitList(it)
            }
        }
        adapter.select= object : TabMenuAddonsAdapater.Select {
            override fun onSelect(session: GeckoSession) {
                context?.let {
                    GeckoRuntime.getDefault(it) }?.let {
                    session.open(it)
                    binding.tabAddonsView.setSession(session)
                }
            }

        }
        return binding.root
    }


}