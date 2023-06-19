package com.example.final_b11009025.view

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.final_b11009025.R
import com.example.final_b11009025.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var calendarFragment: CalendarFragment
    private lateinit var todolistFragment: TodolistFragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    //viewBinding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //更改標題列
        getSupportActionBar()!!.title = "排程小助手"

        //建立fragmentManager
        fragmentManager = supportFragmentManager

        //透過BottomNavigationView切換Fragment
        binding.navigation.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId
            fragmentTransaction = fragmentManager.beginTransaction()

            when (id) {
                R.id.calendar -> {
                    calendarFragment = CalendarFragment()
                    fragmentTransaction.replace(R.id.fragmentContainerView, calendarFragment)
                    fragmentTransaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.todolist -> {
                    todolistFragment = TodolistFragment()
                    fragmentTransaction.replace(R.id.fragmentContainerView, todolistFragment)
                    fragmentTransaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }

    }


    override fun onStart() {
        super.onStart()
        // 畫面開始時檢查權限
        onClickRequestPermission()
    }

    private fun onClickRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED -> {
                // 同意
                Toast.makeText(this, "已取得通知權限", Toast.LENGTH_SHORT).show()
                val channel = NotificationChannel("Day15", "Day15", NotificationManager.IMPORTANCE_HIGH)
                val builder = Notification.Builder(this, "Day15")
                builder.setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("Day15")
                    .setContentText("Day15 Challenge")
                    .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                val notification : Notification = builder.build()
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
                manager.notify(0, notification)

            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) -> {
                //拒絕
                Toast.makeText(this, "未取得通知權限，可至設定開啟權限", Toast.LENGTH_SHORT).show()
            }
            else -> {
                // 第一次請求權限，直接詢問
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "已取得通知權限", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "未取得通知權限", Toast.LENGTH_SHORT).show()
        }
    }

}