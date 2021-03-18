
package com.fancylight.helpdesk

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
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

    // 이 액티비티에서 사용되는 프래그먼트들
    private var mHomeFragment: HomeFragment? = null
    private var mRequestFragment: RequestFragment? = null
    private var mNoticeFragment: NoticeFragment? = null
    private var mMemberInfoFragment: MemberInfoFragment? = null

    // 툴바 자주 사용되니까 클래스 전체의 변수로
    private var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // 툴 바를 액션 바로 설정한다
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)

        // 툴 바를 초기화한다
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24)
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

        mExpandableListView?.setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
            if (groupPosition == 0) {
                when (childPosition) {
                    0 -> {
                        showRequestFragment()
                        mDrawerLayout!!.closeDrawer(GravityCompat.START)
                    }
                }
            }
            true
        }

        // 우측 드로어에 리스너를 설정한다
        mDrawerEnd?.setNavigationItemSelectedListener(this)

        // 버튼 리스너를 설정한다
        val helpButton = findViewById<ImageButton>(R.id.btn_help)
        helpButton.setOnClickListener(this)

        // 사용될 Fragment 미리 생성
        mHomeFragment = HomeFragment()
        mRequestFragment = RequestFragment()
        mNoticeFragment = NoticeFragment()
        mMemberInfoFragment = MemberInfoFragment()

        // HomeFragment 에 리스너 추가하기
        // (HomeFragment 안의 버튼이 눌렸을 때 아래 메소드가 액티비티에서 호출됨)
        mHomeFragment?.setFragmentListener(object : HomeFragment.HomeFragmentListener {
            override fun seeNoticeButtonClicked() {
                // 홈 프래그먼트의 공지사항 보기 클릭되었을때 이 메소드가 호출됨
                // 공지사항 프래그먼트를 띄운다
                showNoticeFragment()
            }
        })

        // HomeFragment 를 화면에 추가
        showHomeFragment()

        // addOnBackStackChangedListener 는 백 스택이 바뀔 때 (= Back 버튼 눌러서 이전 프래그먼트로 갈때)
        // 실행할 코드를 지정할 수 있음. 이때는 툴바의 제목을 적절하게 바꿔줘야 함
        supportFragmentManager.addOnBackStackChangedListener { updateToolbarTitle() }

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
            supportFragmentManager.backStackEntryCount == 1 -> {
                // 앱 종료할지 물어보기
                AlertDialog.Builder(this)
                        .setTitle("종료?")
                        .setMessage("종료하시겠습니까?")
                        .setPositiveButton("네") { _: DialogInterface, _: Int -> finish() }
                        .setNegativeButton("아니오", null)
                        .show()
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

        when (item.itemId) {

            R.id.item_notice -> {
                showNoticeFragment()
                mDrawerLayout!!.closeDrawer(GravityCompat.END)
            }

            R.id.item_my_info -> {
                showMemInfoFragment()
                mDrawerLayout!!.closeDrawer(GravityCompat.END)
            }
        }

        return true
    }

    // NewActivity 시작하기

    private fun startNewActivity() {

        val intent = Intent(this, NewActivity::class.java)
        startActivity(intent)
    }

    // HomeFragment 띄우기

    private fun showHomeFragment() {

        supportFragmentManager.beginTransaction()
                .replace(R.id.frag_container, mHomeFragment!!)
                .addToBackStack(null)
                .commit()

        // 프래그먼트가 바뀌었으면 툴바 제목도 바꿔줘야 함
        updateToolbarTitle()
    }

    // NoticeFragment 띄우기

    private fun showNoticeFragment() {

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frag_container, mNoticeFragment!!)
                .addToBackStack(null)
                .commit()

        updateToolbarTitle()
    }

    // MemInfoFragment 띄우기

    private fun showMemInfoFragment() {

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frag_container, mMemberInfoFragment!!)
                .addToBackStack(null)
                .commit()

        updateToolbarTitle()
    }

    // RequestFragment 띄우기

    private fun showRequestFragment() {

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.frag_container, mRequestFragment!!)
                .addToBackStack(null)
                .commit()

        updateToolbarTitle()
    }

    // 현재 띄워지고 있는 프래그먼트에 따라 툴바 제목을 업데이트한다다

    private fun updateToolbarTitle() {

        // 현재 띄워지고 있는 프래그먼트를 조사한다
        // (findFragmentById : 인수로 지정된 레이아웃에 띄워져 있는 프래그먼트를 검색해서 리턴하는 메소드)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frag_container) ?: return

        // 현재 띄워지고 있는 프래그먼트에 따라 툴바 제목을 다르게 설정한다
        when (currentFragment) {
            mHomeFragment -> mToolbar?.title = ""
            mRequestFragment -> mToolbar?.title = "요청/접수"
            mMemberInfoFragment -> mToolbar?.title = "회원정보"
            mNoticeFragment -> mToolbar?.title = "공지사항"
        }
    }


}