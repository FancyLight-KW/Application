package com.fancylight.helpdesk.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.`object`.ReviseObject
import com.fancylight.helpdesk.`object`.SubmitObject
import com.fancylight.helpdesk.model.Inquiry
import com.fancylight.helpdesk.network.UserApi
import com.fancylight.helpdesk.network.sResultMessage
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

class ReviseActivity : AppCompatActivity() , View.OnClickListener{
    private val REQUEST_IMAGE_CAPTURE = 1
    private val OPEN_GALLERY = 2
    lateinit var currentPhotoPath: String
    private lateinit var inquiry: Inquiry



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revise)

        inquiry = intent.getSerializableExtra("inquiry") as Inquiry
        ReviseObject.init(inquiry)

        val edittitle : EditText = findViewById(R.id.edit_title)
        val editcontent : EditText = findViewById(R.id.edit_content)
        val rb1 : RadioButton = findViewById(R.id.rb1)
        val rb2 : RadioButton = findViewById(R.id.rb2)
        val rb3 : RadioButton = findViewById(R.id.rb3)
        val desiretext : TextView = findViewById(R.id.textDesiredDate)
        val imagetext : TextView = findViewById(R.id.textAttachment)

        val rg1: RadioGroup = findViewById(R.id.rg1)
        val spinner: Spinner = findViewById(R.id.spinner)
        val tmCheck: CheckBox = findViewById(R.id.tmCheck)
        val requestComplete: ImageButton = findViewById(R.id.btnDesiredDate)
        val attachment: ImageButton = findViewById(R.id.btnAttachment)
        val submitbtn: Button = findViewById(R.id.btn_submit)

        when(inquiry.TARGET_CODE){
            "업무시스템" -> rb1.isChecked = true
            "IT인프라"-> rb2.isChecked= true
            "OA장비" ->rb3.isChecked= true
        }
        if(inquiry.TM_APPROVAL_REQ_YN=='Y'){
            tmCheck.isChecked= true
        }
        edittitle.text = Editable.Factory.getInstance().newEditable(inquiry.TITLE)
        editcontent.text = Editable.Factory.getInstance().newEditable(inquiry.CONTENT)
        desiretext.text = inquiry.REQ_FINISH_DATE

        if (inquiry.REQ_IMG_PATH != "") {
            imagetext.text = inquiry.REQ_IMG_PATH.substring(53)
        } else {
            imagetext.text = "첨부파일 없음"
        }

        rg1.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb1 -> ReviseObject.TARGET_CODE = "업무시스템"
                R.id.rb2 -> ReviseObject.TARGET_CODE = "IT인프라"
                R.id.rb3 -> ReviseObject.TARGET_CODE = "OA장비"
            }
        }

        //spinnerArray.xml에 있는 systemName 가져옴
        var sdata = resources.getStringArray(R.array.systemName)
        //어댑터 만들고 연결
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sdata)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                ReviseObject.SYSTEM_GROUP_CODE = sdata[position]
            }
        }

        tmCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                ReviseObject.TM_APPROVAL_REQ_YN = 'Y'
            } else {
                ReviseObject.TM_APPROVAL_REQ_YN = 'N'
            }
        }

        attachment.setOnClickListener(this)
        requestComplete.setOnClickListener(this)
        submitbtn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {
            //첨부파일 버튼 클릭시 카메라촬영이나 갤러리에서 사진 가져오는 기능 구현
            R.id.btnAttachment -> {
                val oItems = arrayOf<String>("  사진 촬영", "  사진 가져오기")
                var oDialog = AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                oDialog.setTitle("촬영/가져오기")
                        .setItems(oItems, DialogInterface.OnClickListener { dialog, which ->
                            when (which) {
                                0 -> {
                                    Toast.makeText(applicationContext, "카메라 촬영", Toast.LENGTH_LONG).show()
                                    settingPermission()
                                    startCapture()
                                }
                                1 -> {
                                    Toast.makeText(applicationContext, "갤러리 가져오기", Toast.LENGTH_LONG).show()
                                    settingPermission()
                                    openGallery()
                                }
                            }
                        }).show()
            }

            //요청완료일 버튼 클릭시 달력으로 날짜 선택하는 기능 구현
            R.id.btnDesiredDate -> {
                val textDesiredDate: TextView = findViewById(R.id.textDesiredDate)
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)
                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                        //희망완료일 기능 여기다 구현하면 됨
                        textDesiredDate.setText("${year}/ ${month + 1}/ ${dayOfMonth}")
                        ReviseObject.REQ_FINISH_DATE = SubmitObject.dateSet(year, month + 1, dayOfMonth)
                    }
                }, year, month, date)
                dlg.show()
            }

            R.id.btn_submit -> {
                val title: EditText = findViewById(R.id.edit_title)
                val content: EditText = findViewById(R.id.edit_content)

                ReviseObject.TITLE = title.text.toString()
                ReviseObject.CONTENT = content.text.toString()
                revisePost()
                AlertDialog.Builder(this)
                        .setTitle("수정")
                        .setMessage("변경된 내용으로 접수되었습니다!")
                        .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                        .show()


            }
        }
    }

    fun settingPermission() {
        var permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(applicationContext, "권한 허가", Toast.LENGTH_SHORT)
                        .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(applicationContext, "권한 거부", Toast.LENGTH_SHORT)
                        .show()
                ActivityCompat.finishAffinity(this@ReviseActivity) // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(this)
                .setPermissionListener(permis)
                .setRationaleMessage("카메라 사진 권한 필요")
                .setDeniedMessage("카메라 권한 요청 거부")
                .setPermissions(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA)
                .check()

    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "Camera_",
                ".jpg",
                storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            this, "com.fancylight.helpdesk.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun openGallery() {
        val intent: Intent = Intent(Intent.ACTION_PICK)

        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val textAttachment: TextView = findViewById(R.id.textAttachment)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                ReviseObject.REQ_IMG_PATH = currentPhotoPath
                textAttachment.text= File(ReviseObject.REQ_IMG_PATH).name


            } else if (requestCode == OPEN_GALLERY) {
                val dataUri = data?.data
                dataUri?.let {
                    ReviseObject.REQ_IMG_PATH = absolutelyPath(dataUri)
                }

                textAttachment.text= File(ReviseObject.REQ_IMG_PATH).name
            } else {
                //Toast.makeText(applicationContext, "오류", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun absolutelyPath(path: Uri): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor = contentResolver.query(path, proj, null, null, null)!!
        var index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c.moveToFirst()

        var result = c.getString(index)

        return result
    }


    fun revisePost() {
        var stringJson =""

        if(ReviseObject.REQ_IMG_PATH !="" && ReviseObject.REQ_IMG_PATH == inquiry.REQ_IMG_PATH){
            if(inquiry.MOD_USER_ID==""){
                stringJson =ReviseObject.convertJson(1)
            }
            else{
                stringJson =ReviseObject.convertJson(3)
            }
            Log.d("ReviseJson",stringJson)

            UserApi.service.ReviseNPut("Bearer " + UserApi.ttt,inquiry.REQ_SEQ, stringJson).enqueue(object : retrofit2.Callback<sResultMessage> {
                override fun onResponse(call: retrofit2.Call<sResultMessage>, response: Response<sResultMessage>) {
                    if (response.isSuccessful) {
                        //Toast.makeText(applicationContext, "성공", Toast.LENGTH_LONG).show()
                        AlertDialog.Builder(this@ReviseActivity)
                            .setTitle("수정")
                            .setMessage("변경된 내용으로 접수되었습니다!")
                            .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                            .show()

                    } else {
                        //Toast.makeText(applicationContext, "실패" +response.code() , Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: retrofit2.Call<sResultMessage>, t: Throwable) {
                    //Toast.makeText(applicationContext, "실패실패", Toast.LENGTH_LONG).show()
                    Log.e("failure error", "" + t)
                }
            })

        }else{
            if(inquiry.MOD_USER_ID==""){
                stringJson = ReviseObject.convertJson(0)
            }
            else{
                stringJson =ReviseObject.convertJson(2)
            }
            Log.d("ReviseJson",stringJson)

            if(ReviseObject.REQ_IMG_PATH == "" && ReviseObject.REQ_IMG_PATH == inquiry.REQ_IMG_PATH){
                UserApi.service.ReviseNPut("Bearer " + UserApi.ttt,inquiry.REQ_SEQ, stringJson).enqueue(object : retrofit2.Callback<sResultMessage> {
                    override fun onResponse(call: retrofit2.Call<sResultMessage>, response: Response<sResultMessage>) {
                        if (response.isSuccessful) {
                            //Toast.makeText(applicationContext, "성공", Toast.LENGTH_LONG).show()
                            AlertDialog.Builder(this@ReviseActivity)
                                .setTitle("수정")
                                .setMessage("변경된 내용으로 접수되었습니다!")
                                .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                                .show()
                        } else {
                            //Toast.makeText(applicationContext, "실패" +response.code() , Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: retrofit2.Call<sResultMessage>, t: Throwable) {
                        //Toast.makeText(applicationContext, "실패실패", Toast.LENGTH_LONG).show()
                        Log.e("failure error", "" + t)
                    }
                })

            } else{
                val file = File(ReviseObject.REQ_IMG_PATH)
                var fileName = file.name
                var requestImage: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                var fileBody: MultipartBody.Part = MultipartBody.Part.createFormData("imagefile", fileName, requestImage)


                UserApi.service.RevisePut("Bearer " + UserApi.ttt,ReviseObject.REQ_SEQ, fileBody, stringJson).enqueue(object : retrofit2.Callback<sResultMessage> {
                    override fun onResponse(call: retrofit2.Call<sResultMessage>, response: Response<sResultMessage>) {
                        if (response.isSuccessful) {
                            //Toast.makeText(applicationContext, "성공", Toast.LENGTH_LONG).show()
                            finish()
                        } else {
                            //Toast.makeText(applicationContext, "실패" +response.code() , Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<sResultMessage>, t: Throwable) {
                        //Toast.makeText(applicationContext, "실패실패", Toast.LENGTH_LONG).show()
                        Log.e("failure error", "" + t)
                    }
                })
            }
        }
    }

    private fun startHomeActivity() {

        val i = Intent(this, HomeActivity::class.java)
        i.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)

    }
}