package com.thallo.stage.componets

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.thallo.stage.MainActivity

class CollectionAdapter(activity: MainActivity, private val fragmentlist:List<Fragment>) : FragmentStateAdapter(activity) {//fragment 也可以换为 activity
private val fid2 = 222L
    private val fid3 = 333L
    private val ids = arrayListOf(fid2,fid3)
    private val creatID= hashSetOf<Long>()
    override fun getItemCount(): Int {
        return fragmentlist.size
    }

    override fun createFragment(position: Int): Fragment {
        val id = ids[position]
        creatID.add(id)
        val fragment = fragmentlist[position]
        return fragment
    }//返回需要创建的fragment


}