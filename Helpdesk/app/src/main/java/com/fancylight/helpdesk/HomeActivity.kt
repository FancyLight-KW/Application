
package com.fancylight.helpdesk

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.fancylight.helpdesk.adapter.NavExpandableListAdapter
import com.fancylight.helpdesk.model.NavListItem

class HomeActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    // 드로어 레이아웃
    private var mDrawerLayout: DrawerLayout? = null

    // 양쪽 드로어
    private var mDrawerStart: View? = null
    private var mDrawerEnd: NavigationView? = null

    // 왼쪽 드로어의 리스트뷰
    private var mExpandableListView: ExpandableListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 툴 바를 액션 바로 설정한다
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // 툴 바를 초기화한다
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 드로어를 초기화한다
        mDrawerLayout = findViewById(R.id.drawer_layout)

        mDrawerStart = findViewById(R.id.nav_view_start)
        mDrawerEnd = findViewById(R.id.nav_view_end)

        // 좌측 드로어의 리스트 뷰를 초기화한다
        mExpandableListView = findViewById(R.id.nav_list_start)
        val itemList = arrayOf(
            NavListItem("요청/접수", arrayListOf("요청/접수", "요청/접수(처리자)", "처리이력정보", "처리이력정보(관리자)", "나의 결재함")),
            NavListItem("장애관리", arrayListOf("장애관리", "하위 탭 2", "하위 탭 3")),
            NavListItem("변경관리", arrayListOf("변경관리", "나의 결재함(변경)", "하위 탭 3", "하위 탭 4")),
            NavListItem("통계정보", arrayListOf("서비스요청 적기접수율", "서비스요청 적기처리율", "서비스 만족도", "하위 탭 4")),
            NavListItem("게시판", arrayListOf("공지사항", "IT 정책", "질의응답", "FAQ", "자료실")),
        )
        mExpandableListView?.setAdapter(NavExpandableListAdapter(itemList))

        // 우측 드로어에 리스너를 설정한다
        mDrawerEnd?.setNavigationItemSelectedListener(this)

        // 버튼 리스너를 설정한다
        val helpButton = findViewById<ImageButton>(R.id.btn_help)
        helpButton.setOnClickListener(this)
    }

    // 옵션 메뉴 생성

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    // 옵션 아이템 선택 처리

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            // 왼쪽 드로어 오픈
            android.R.id.home -> {
                if (mDrawerLayout!!.isDrawerOpen(mDrawerStart!!)) {
                    mDrawerLayout!!.closeDrawer(mDrawerStart!!)
                } else {
                    mDrawerLayout!!.openDrawer(mDrawerStart!!)
                }
            }
            // 오른쪽 드로어 오픈
            R.id.item_my -> {
                if (mDrawerLayout!!.isDrawerOpen(mDrawerEnd!!)) {
                    mDrawerLayout!!.closeDrawer(mDrawerEnd!!)
                } else {
                    mDrawerLayout!!.openDrawer(mDrawerEnd!!)
                }
            }
        }

        return false
    }

    // Back 버튼 입력 시, 드로어 닫기

    override fun onBackPressed() {

        when {
            // 열린 드로어 닫기
            mDrawerLayout!!.isDrawerOpen(GravityCompat.START) -> {
                mDrawerLayout!!.closeDrawer(GravityCompat.START)
            }
            mDrawerLayout!!.isDrawerOpen(GravityCompat.END) -> {
                mDrawerLayout!!.closeDrawer(GravityCompat.END)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    // 버튼 클릭 처리

    override fun onClick(v: View?) {

        if (v?.id == R.id.btn_help) {

            // Help 버튼 클릭 : NewActivity 시작
            startNewActivity()
        }
    }

    // 네비게이션 아이템 선택 처리

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        return true
    }

    // NewActivity 시작하기

    private fun startNewActivity() {

        val intent = Intent(this, NewActivity::class.java)
        startActivity(intent)
    }

}