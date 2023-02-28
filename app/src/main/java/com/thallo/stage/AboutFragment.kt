package com.thallo.stage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thallo.stage.databinding.FragmentAboutBinding
import com.thallo.stage.session.createSession


class AboutFragment : Fragment() {
    lateinit var fragmentAboutBinding: FragmentAboutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentAboutBinding=FragmentAboutBinding.inflate(LayoutInflater.from(requireContext()))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAboutBinding.materialButton6.setOnClickListener { joinQQGroup("TbCzUUsxKdWQqmHqgqaTFJ110tq4FqCD") }
        fragmentAboutBinding.materialButton2.setOnClickListener { sendEmail() }
        fragmentAboutBinding.materialButton5.setOnClickListener {
            val intent = Intent(requireContext(),MainActivity::class.java)
            intent.data = Uri.parse("https://t.me/stage_browser_channel")
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return fragmentAboutBinding.root
    }

    /****************
     *
     * 发起添加群流程。群号：Stage浏览器chat&amp;play(612932857) 的 key 为： TbCzUUsxKdWQqmHqgqaTFJ110tq4FqCD
     * 调用 joinQQGroup(TbCzUUsxKdWQqmHqgqaTFJ110tq4FqCD) 即可发起手Q客户端申请加群 Stage浏览器chat&amp;play(612932857)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data =
            Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26jump_from%3Dwebapi%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            false
        }
    }
    fun sendEmail(){
        val i = Intent(Intent.ACTION_SEND)
        i.type = "message/rfc822"
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf("l694630313@Gmail.com"))
        startActivity(
            Intent.createChooser(
                i,
                "Select email application."
            )
        )
    }

}