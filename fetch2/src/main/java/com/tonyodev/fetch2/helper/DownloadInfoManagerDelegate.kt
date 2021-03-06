package com.tonyodev.fetch2.helper

import android.os.Handler
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2.Logger
import com.tonyodev.fetch2.Status
import com.tonyodev.fetch2.database.DownloadInfo
import com.tonyodev.fetch2.downloader.DownloadManager


open class DownloadInfoManagerDelegate(val downloadInfoUpdater: DownloadInfoUpdater,
                                       val uiHandler: Handler,
                                       val fetchListener: FetchListener,
                                       val logger: Logger) : DownloadManager.Delegate {

    override fun onStarted(download: Download, etaInMilliseconds: Long) {
        val downloadInfo = download as DownloadInfo
        downloadInfo.status = Status.DOWNLOADING
        try {
            downloadInfoUpdater.update(downloadInfo)
            uiHandler.post {
                fetchListener.onProgress(downloadInfo, etaInMilliseconds)
            }
        } catch (e: Exception) {
            logger.e("DownloadManagerDelegate", e)
        }
    }

    override fun onProgress(download: Download, etaInMilliSeconds: Long,
                            reportProgress: Boolean) {
        val downloadInfo = download as DownloadInfo
        downloadInfo.status = Status.DOWNLOADING
        try {
            downloadInfoUpdater.update(downloadInfo)
            if (reportProgress) {
                uiHandler.post {
                    fetchListener.onProgress(downloadInfo, etaInMilliSeconds)
                }
            }
        } catch (e: Exception) {
            logger.e("DownloadManagerDelegate", e)
        }
    }

    override fun onError(download: Download) {
        val downloadInfo = download as DownloadInfo
        downloadInfo.status = Status.FAILED
        try {
            downloadInfoUpdater.update(downloadInfo)
            uiHandler.post {
                fetchListener.onError(downloadInfo)
            }
        } catch (e: Exception) {
            logger.e("DownloadManagerDelegate", e)
        }
    }

    override fun onComplete(download: Download) {
        val downloadInfo = download as DownloadInfo
        downloadInfo.status = Status.COMPLETED
        try {
            downloadInfoUpdater.update(downloadInfo)
            uiHandler.post {
                fetchListener.onCompleted(downloadInfo)
            }
        } catch (e: Exception) {
            logger.e("DownloadManagerDelegate", e)
        }
    }

}