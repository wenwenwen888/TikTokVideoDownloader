package com.nbtech.testtiktokvideodownloader.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.TextUtils

/**
 * Created by won on 2019-06-17.
 */
object ClipboardUtils {

    /**
     * 复制指定文本到剪切板
     *
     * @param text 要复制到剪切板的文本
     */
    fun copy(context: Context, text: String) {
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("", text)
        clipboardManager.primaryClip = clipData
        iUtils.ShowToast(context, "Copy complete!")
    }

    /**
     * 获取剪切板内容
     * @return
     */
    fun paste(context: Context): String {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        try {
            if (manager.hasPrimaryClip() && manager.primaryClip.itemCount > 0) {
                val addedText = manager.primaryClip.getItemAt(0).text
                val addedTextString = addedText.toString()
                if (!TextUtils.isEmpty(addedTextString)) {
                    return addedTextString
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

}