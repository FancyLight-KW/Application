package com.fancylight.helpdesk

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class SubmitActivity : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val OPEN_GALLERY =2
    lateinit var currentPhotoPath : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        //기능들 여기다가 구현
        val spinner: Spinner = findViewById(R.id.spinner)
        val requestComplete: ImageButton = findViewById(R.id.btnRequestComplete)
        val attachment : ImageButton = findViewById(R.id.btnAttachment)
        val sumbitbtn : Button = findViewById(R.id.btn_submit)


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
                //스피너 동작 여기다 구현하면 됨
            }
        }

        attachment.setOnClickListener(this)
        requestComplete.setOnClickListener(this)
        sumbitbtn.setOnClickListener(this)

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
                                    Toast.makeText(applicationContext, "카메라 촬영", Toast.LENGTH_LONG).show()
                                    settingPermission()
                                    startCapture()
                                }
                                1 -> {
                                    Toast.makeText(applicationContext,"갤러리 가져오기", Toast.LENGTH_LONG).show()
                                    settingPermission()
                                    openGallery()
                                }
                            }
                        }).show()
            }

            //요청완료일 버튼 클릭시 달력으로 날짜 선택하는 기능 구현
            R.id.btnRequestComplete -> {
                val textRequestComplete : TextView = findViewById(R.id.textRequestComplete)
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)
                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                        //요청완료일 기능 여기다 구현하면 됨
                        textRequestComplete.setText("${year}/ ${month + 1}/ ${dayOfMonth}")
                    }
                }, year, month, date)
                dlg.show()
            }

            R.id.btn_submit -> {

            }
        }
    }

    fun settingPermission(){
        var permis = object  : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(applicationContext, "권한 허가", Toast.LENGTH_SHORT)
                        .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(applicationContext, "권한 거부", Toast.LENGTH_SHORT)
                        .show()
                ActivityCompat.finishAffinity(this@SubmitActivity) // 권한 거부시 앱 종료
            }
        }

    }

    @Throws(IOException::class)
    private fun createImageFile() : File{
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_",
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

        val textAttachment : TextView = findViewById(R.id.textAttachment)

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE) {
                val file = File(currentPhotoPath)

                if (Build.VERSION.SDK_INT < 28) {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Uri.fromFile(file))
                    //img_picture.setImageBitmap(bitmap)
                } else {
                    val decode = ImageDecoder.createSource(this.contentResolver, Uri.fromFile(file))
                    val bitmap = ImageDecoder.decodeBitmap(decode)
                    //img_picture.setImageBitmap(bitmap)
                }
            } else if(resultCode == Activity.RESULT_OK){
                if(requestCode == OPEN_GALLERY){
                    val dataUri = data?.data

                    dataUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, dataUri)

                            //binding.image1.setImageBitmap(bitmap)
                        } else {
                            val decode = ImageDecoder.createSource(this.contentResolver, dataUri)
                            val bitmap = ImageDecoder.decodeBitmap(decode)
                        }
                    }
                }


            }else{
                Toast.makeText(applicationContext,"오류",Toast.LENGTH_LONG).show()
            }
        }
    }

}
