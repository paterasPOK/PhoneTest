package com.example.phonetest

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import com.example.phonetest.R
import java.io.File
import java.util.jar.Manifest

class MainActivityMicrophone : AppCompatActivity(), View.OnClickListener {


    private val MICROPHONE_PERMISSION_CODE: Int = 200
    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_microphone)



        val btnStart = findViewById<Button>(R.id.btn_start)
        val btnStop = findViewById<Button>(R.id.btn_stop)
        val btnPlay = findViewById<Button>(R.id.btn_play)
        val btnDel = findViewById<Button>(R.id.btn_delete)

        btnStart.setOnClickListener(this)
        btnStop.setOnClickListener(this)
        btnPlay.setOnClickListener(this)
        btnDel.setOnClickListener(this)

        if (isMicrophonePresent()) {
            getMicrophonePermission()

        }



    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btn_start -> start()
            R.id.btn_stop -> stop()
            R.id.btn_play -> play()
            R.id.btn_delete -> deleteFile()


        }
    }

    private fun start() {
        val fileLocation = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder?.setOutputFile(fileLocation)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mediaRecorder?.setMaxDuration(5000)



        mediaRecorder?.prepare()
        mediaRecorder?.start()
        Toast.makeText(this,"Duration of Recording is 5 seconds",Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            if (mediaRecorder != null){
                mediaRecorder?.stop()
                mediaRecorder?.release()
                mediaRecorder = null
                Toast.makeText(this,"Recording stopped automatically",Toast.LENGTH_SHORT).show()
            }

        },5000)
    }

    private fun stop() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
    }

    private fun play() {
        val fileLocation = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        if (File(fileLocation).exists() && !flag){
            flag = true
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(fileLocation)
            mediaPlayer?.prepare()
            mediaPlayer?.start()

            Handler(Looper.getMainLooper()).postDelayed({
                stopPlayback()
            }, 5000)



        }
    }
    private fun stopPlayback(){
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        flag = false

    }
    private fun deleteFile() {
        val fileLocation = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        if (File(fileLocation).exists()){
            File(fileLocation).delete()
            Toast.makeText(this, "File Deleted", Toast.LENGTH_SHORT).show()
        }
    }


    private fun isMicrophonePresent(): Boolean {
        if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true
        }else {
            return false
        }
    }

    private fun getMicrophonePermission () {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(this, Array<String>(1) {android.Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE )
        }
    }
}