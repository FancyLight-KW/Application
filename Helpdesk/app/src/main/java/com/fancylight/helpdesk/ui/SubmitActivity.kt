package com.fancylight.helpdesk.ui

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.`object`.SubmitObject
import com.fancylight.helpdesk.network.JsonData
import com.fancylight.helpdesk.network.UserApi
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class SubmitActivity : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val OPEN_GALLERY = 2
    lateinit var currentPhotoPath : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        SubmitObject.init()

        //기능들 여기다가 구현
        val rg1 : RadioGroup = findViewById(R.id.rg1)
        val spinner: Spinner = findViewById(R.id.spinner)
        val tmCheck : CheckBox = findViewById(R.id.tmCheck)
        val requestComplete: ImageButton = findViewById(R.id.btnDesiredDate)
        val attachment : ImageButton = findViewById(R.id.btnAttachment)
        val submitbtn : Button = findViewById(R.id.btn_submit)

        rg1.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.rb1 -> SubmitObject.target = "업무시스템"
                R.id.rb2 -> SubmitObject.target = "IT인프라"
                R.id.rb3 -> SubmitObject.target = "OA장비"
            }
        }

        //spinnerArray.xml에 있는 systemName 가져옴
        var sdata = resources.getStringArray(R.array.systemName)
        //어댑터 만들고 연결
        var adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, sdata)
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
                SubmitObject.systemCode = sdata[position]
            }
        }
        tmCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){ SubmitObject.tmApproval = "Y" }
            else{SubmitObject.tmApproval = "N"}
        }

        attachment.setOnClickListener(this)
        requestComplete.setOnClickListener(this)
        submitbtn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v?.id){
            //첨부파일 버튼 클릭시 카메라촬영이나 갤러리에서 사진 가져오는 기능 구현
            R.id.btnAttachment ->{
                val oItems = arrayOf<String>("  사진 촬영","  사진 가져오기")
                var oDialog = AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                oDialog.setTitle("촬영/가져오기")
                        .setItems(oItems, DialogInterface.OnClickListener { dialog, which ->
                            when(which){
                                0 -> {
                                    //Toast.makeText(applicationContext, "카메라 촬영", Toast.LENGTH_LONG).show()
                                    settingPermission()
                                    startCapture()
                                }
                                1 -> {
                                    //Toast.makeText(applicationContext,"갤러리 가져오기", Toast.LENGTH_LONG).show()
                                    settingPermission()
                                    openGallery()
                                }
                            }
                        }).show()
            }

            //요청완료일 버튼 클릭시 달력으로 날짜 선택하는 기능 구현
            R.id.btnDesiredDate -> {
                val textDesiredDate : TextView = findViewById(R.id.textDesiredDate)
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)
                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                        //희망완료일 기능 여기다 구현하면 됨
                        textDesiredDate.setText("${year}/ ${month + 1}/ ${dayOfMonth}")
                        SubmitObject.finishDate =SubmitObject.dateSet(year,month+1,dayOfMonth)
                    }
                }, year, month, date)
                dlg.show()
            }

            R.id.btn_submit -> {
                val title : EditText = findViewById(R.id.edit_title)
                val content : EditText = findViewById(R.id.edit_content)

                SubmitObject.title = title.text.toString()
                SubmitObject.content = content.text.toString()
                val strempty = SubmitObject.isEmpty()

                if(strempty == "완료"){
                    submitPost()
                }else{
                    //Toast.makeText(applicationContext,strempty,Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    fun settingPermission(){
        var permis = object  : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(applicationContext, "권한 허가", Toast.LENGTH_SHORT) .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(applicationContext, "권한 거부", Toast.LENGTH_SHORT) .show()
                ActivityCompat.finishAffinity(this@SubmitActivity) // 권한 거부시 앱 종료
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
    private fun createImageFile() : File{
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "Camera_",
                ".jpg",
                storageDir
        ).apply{
            currentPhotoPath = absolutePath
        }
    }

    fun startCapture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                }catch(ex:IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                            this, "com.fancylight.helpdesk.fileprovider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    private fun openGallery() {
        val intent : Intent = Intent(Intent.ACTION_PICK)

        intent.setType("image/*")
        startActivityForResult(intent, OPEN_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val textAttachment: TextView = findViewById(R.id.textAttachment)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                SubmitObject.path = currentPhotoPath
                textAttachment.text= File(SubmitObject.path).name

            } else if (requestCode == OPEN_GALLERY) {
                val dataUri = data?.data
                dataUri?.let {
                    SubmitObject.path = absolutelyPath(dataUri)
                }
                textAttachment.text= File(SubmitObject.path).name

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


    fun submitPost() {
        var stringJson = SubmitObject.convertJson()
        Log.d("SubmitJson",stringJson)

        if(SubmitObject.path==""){
            UserApi.service.dataNPost("Bearer "+ UserApi.ttt, stringJson).enqueue(object : retrofit2.Callback<JsonData> {
                override fun onResponse(call: retrofit2.Call<JsonData>, response: Response<JsonData>) {
                    if(response.isSuccessful){
                        //Toast.makeText(applicationContext,"성공", Toast.LENGTH_LONG).show()
                        androidx.appcompat.app.AlertDialog.Builder(this@SubmitActivity)
                            .setTitle("요청 완료")
                            .setMessage("관리자의 승인을 기다립니다!")
                            .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                            .show()
                    }
                    else{
                       // Toast.makeText(applicationContext,"실패" +response.code(), Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: retrofit2.Call<JsonData>, t: Throwable) {
                    //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                    Log.e("failure error", ""+t)
                }
            })
        } else{
            val file = File(SubmitObject.path)
            var fileName = file.name
            var requestImage : RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file)
            var fileBody : MultipartBody.Part = MultipartBody.Part.createFormData("imagefile",fileName,requestImage)

            UserApi.service.dataPost("Bearer "+ UserApi.ttt,fileBody, stringJson).enqueue(object : retrofit2.Callback<JsonData> {
                override fun onResponse(call: retrofit2.Call<JsonData>, response: Response<JsonData>) {
                    if(response.isSuccessful){
                       // Toast.makeText(applicationContext,"성공", Toast.LENGTH_LONG).show()
                        androidx.appcompat.app.AlertDialog.Builder(this@SubmitActivity)
                            .setTitle("요청 완료")
                            .setMessage("관리자의 승인을 기다립니다!")
                            .setPositiveButton("확인") { _: DialogInterface, _: Int -> startHomeActivity() }
                            .show()
                    }
                    else{
                        //Toast.makeText(applicationContext,"실패", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: retrofit2.Call<JsonData>, t: Throwable) {
                    //Toast.makeText(applicationContext,"실패실패", Toast.LENGTH_LONG).show()
                    Log.e("failure error", ""+t)
                }
            })
        }

    }

    private fun startHomeActivity() {

        val i = Intent(this, HomeActivity::class.java)
        i.flags =  Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)


    }



}
