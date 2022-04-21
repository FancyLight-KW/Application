package com.fancylight.helpdesk.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.adapter.MentionAdapter
import com.fancylight.helpdesk.adapter.MentionAdapter.OnQuestionClickListener
import com.fancylight.helpdesk.model.Mention
import com.fancylight.helpdesk.network.ChatbotReturn
import com.fancylight.helpdesk.network.UserApi
import retrofit2.Response
import java.util.*

class ChatBotFragment : Fragment(), OnQuestionClickListener {

    private var mMentionList: MutableList<Mention>? = null
    private var mMentionAdapter: MentionAdapter? = null
    private var mMentionRecycler: RecyclerView? = null
    private var mQuestionEdit: EditText? = null

    // 대화명
    private var mMyName = "나"
    private var mCounterName = "챗봇"

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_chat_bot, container, false)

        // Build mention recycler view
        buildMentionRecycler(root)

        // Insert greeting mentions
        insertGreetingMentions()
        mQuestionEdit = root.findViewById(R.id.edit_question)

        val questionButton: Button = root.findViewById(R.id.btn_question)
        questionButton.setOnClickListener {
            onQuestionClick()
        }

        return root
    }

    // Build mention recycler view
    private fun buildMentionRecycler(root: View) {

        mMentionRecycler = root.findViewById(R.id.recycler_mention)
        mMentionRecycler?.setHasFixedSize(true)
        mMentionRecycler?.layoutManager = LinearLayoutManager(context)
        mMentionList = ArrayList()
        mMentionAdapter = MentionAdapter(mMentionList!!, mMyName)
        mMentionRecycler?.adapter = mMentionAdapter

        // Set question button click listener
        mMentionAdapter!!.setOnQuestionClickListener(this)
    }

    // Insert greeting mentions
    private fun insertGreetingMentions() {
        mMentionList!!.add(Mention(mMyName, "안녕하세요"))
        mMentionAdapter!!.notifyItemInserted(0)
        mMentionRecycler!!.postDelayed({
            mMentionList!!.add(Mention(mCounterName, "안녕하세요. 질문이 있으신가요?"))
            mMentionAdapter!!.notifyItemInserted(1)
        }, 1000)
    }

    // Process question button click
    override fun onQuestionClick() {
        val strQuestion = mQuestionEdit?.text.toString().trim { it <= ' ' }
        if (strQuestion.isEmpty()) {
            Toast.makeText(context, "질문을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        mQuestionEdit?.setText("")
        mMentionList?.add(Mention(mMyName, strQuestion))
        mMentionAdapter?.notifyItemInserted(mMentionList!!.size - 1)
        mMentionRecycler?.scrollToPosition(mMentionList!!.size)

        // Hide Keyboard
        if (context != null) {
            val inputMethodManager =
                    context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
        }

        // TODO: strQuestion 을 입력값으로 strAnswer 를 유도해서 채팅 진행하면 될듯?
        UserApi.service.ChatbotPost("Bearer "+ UserApi.ttt,strQuestion).enqueue(object : retrofit2.Callback<ChatbotReturn> {
            override fun onResponse(call: retrofit2.Call<ChatbotReturn>, response: Response<ChatbotReturn>) {
                if(response.isSuccessful){
                    var answer = response.body()!!.fulfillmentText

                    mMentionRecycler?.postDelayed({
                        mMentionList?.add(Mention(mCounterName, answer+" "))
                        mMentionAdapter?.notifyItemInserted(mMentionList!!.size - 1)
                        mMentionRecycler?.scrollToPosition(mMentionList!!.size)
                    }, 1000)
                    //Toast.makeText(activity,"성공2" +answer, Toast.LENGTH_LONG).show()

                }
                else{
                    //Toast.makeText(activity,"실패", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: retrofit2.Call<ChatbotReturn>, t: Throwable) {
                //Toast.makeText(activity,"실패실패", Toast.LENGTH_LONG).show()
                Log.e("failure error", ""+t)
            }
        })


    }
}