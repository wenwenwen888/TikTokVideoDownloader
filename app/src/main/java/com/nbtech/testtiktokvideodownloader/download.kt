package com.nbtech.testtiktokvideodownloader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbtech.testtiktokvideodownloader.receiver.Receiver
import com.nbtech.testtiktokvideodownloader.services.ClipboardMonitor
import com.nbtech.testtiktokvideodownloader.tasks.downloadVideo
import com.nbtech.testtiktokvideodownloader.utils.Constants.STARTFOREGROUND_ACTION
import com.nbtech.testtiktokvideodownloader.utils.Constants.STOPFOREGROUND_ACTION
import com.nbtech.testtiktokvideodownloader.utils.iUtils
import kotlinx.android.synthetic.main.fragment_download.*
import kotlinx.android.synthetic.main.fragment_download.view.*

class download : Fragment() {
    private var NotifyID = 1001

    private var csRunning = false

    lateinit var prefEditor: SharedPreferences.Editor
    lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_download, container, false)

        pref = context!!.getSharedPreferences("tikVideoDownloader", 0) // 0 - for private mode
        prefEditor = pref.edit()
        csRunning = pref.getBoolean("csRunning", false)

        createNotificationChannel(
            requireActivity(),
            NotificationManagerCompat.IMPORTANCE_LOW,
            true,
            getString(R.string.app_name),
            "Tiktok Auto Download"
        )

        view.btnDownload.setOnClickListener { view ->
            //Log.d("clicked","true")
            // view.btnDownload.visibility=View.GONE
            //  pbFetchingVideo.visibility=View.VISIBLE
            val url = etURL.text.toString()
            DownloadVideo(url)
        }

        view.ivLink.setOnClickListener(fun(view: View) {
            val clipBoardManager =
                context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val primaryClipData = clipBoardManager.primaryClip
            val clip = primaryClipData.getItemAt(0).text.toString()

            etURL.text = Editable.Factory.getInstance().newEditable(clip)
            DownloadVideo(clip)
        })
        if (csRunning) {
            view.chkAutoDownload.isChecked = true
            startClipboardMonitor()
        } else {
            view.chkAutoDownload.isChecked = false
            stopClipboardMonitor()
        }
        view.chkAutoDownload.setOnClickListener { view ->
            val checked = view.chkAutoDownload.isChecked

            if (checked) {
                Log.e("loged", "testing checked!")
                startClipboardMonitor()
            } else {
                Log.e("loged", "testing unchecked!")
                stopClipboardMonitor()
                // setNofication(false);
            }

        }
        return view
    }

    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean,
        name: String,
        description: String
    ) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // 3
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.e("loged", "Notificaion Channel Created!")
        }
    }

    private fun setNofication(b: Boolean) {

//        if (b) {
//            val channelId = "${context!!.packageName}-${context!!.getString(R.string.app_name)}"
//            val notificationBuilder = NotificationCompat.Builder(context!!, channelId).apply {
//                setSmallIcon(R.drawable.notification_template_icon_bg) // 3
//                // setStyle(NotificationCompat.)
//                setLargeIcon(
//                    BitmapFactory.decodeResource(
//                        context!!.resources,
//                        R.drawable.notification_template_icon_bg
//                    )
//                )
//                setContentTitle("Auto Download Service") // 4
//                setContentText("Copy the link of video to start download") // 5
//                setOngoing(true)
//                priority = NotificationCompat.PRIORITY_LOW // 7
//                setSound(null)
//                setOnlyAlertOnce(true)
//                setAutoCancel(false)
//                addAction(
//                    R.drawable.navigation_empty_icon,
//                    "Stop",
//                    makePendingIntent("quit_action")
//                )
//
//                val intent = Intent(requireActivity(), MainActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//                val pendingIntent = PendingIntent.getActivity(requireActivity(), 0, intent, 0)
//
//                setContentIntent(pendingIntent)
//            }
//            with(NotificationManagerCompat.from(requireActivity())) {
//                // notificationId is a unique int for each notification that you must define
//                notify(NotifyID, notificationBuilder.build())
//
//                Log.e("loged", "testing notification notify!")
//
//
//            }
//
//
//        } else {
//            NotificationManagerCompat.from(requireActivity()).cancel(NotifyID)
//        }
    }

    fun startClipboardMonitor() {
        prefEditor.putBoolean("csRunning", true)
        prefEditor.commit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val service = requireActivity().startForegroundService(
                Intent(
                    requireContext(),
                    ClipboardMonitor::class.java
                ).setAction(STARTFOREGROUND_ACTION)
            )
        } else {
            val service = requireActivity().startService(
                Intent(
                    requireContext(),
                    ClipboardMonitor::class.java
                )
            )
        }

    }

    fun stopClipboardMonitor() {
        prefEditor.putBoolean("csRunning", false)
        prefEditor.commit()

        requireActivity().stopService(
            Intent(
                requireContext(),
                ClipboardMonitor::class.java
            ).setAction(STOPFOREGROUND_ACTION)
        )
    }

    fun makePendingIntent(name: String): PendingIntent {
        val intent = Intent(requireActivity(), Receiver::class.java)
        intent.action = name
        return PendingIntent.getBroadcast(requireActivity(), 0, intent, 0)
    }

    fun DownloadVideo(url: String) {

        if (url == "" && iUtils.checkURL(url)) {
            iUtils.ShowToast(context!!, "Please Enter a valid URI")
        } else {
            downloadVideo.Start(context!!, url, false)

        }
    }
}
