package com.unrealmojo.hamstermania.model

import android.content.Context
import android.os.Build
import com.unrealmojo.hamstermania.data.ui.Hamster
import com.unrealmojo.hamstermania.core.HamsterStorage
import com.unrealmojo.hamstermania.core.ServerApi
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class HamsterModel(
        private val mContext: Context,
        private val mServerApi: ServerApi,
        private val mHamsterStorage: HamsterStorage
) {
    private val mDbOperationExecutor = Executors.newSingleThreadExecutor()
    private val mDbOperationScheduler = Schedulers.from(mDbOperationExecutor)

    private val appVersion = mContext.packageManager
            .getPackageInfo(mContext.packageName, 0)
            .versionName

    fun getHamsters(
            searchQuery: String?,
            onLoadedFromNetwork: (List<Hamster>) -> Unit,
            onLoadedFromCache: (List<Hamster>) -> Unit
    ): Completable =
            mServerApi.getHamsters(Build.VERSION.CODENAME, appVersion, Build.MODEL)
                    .subscribeOn(Schedulers.io())
                    .map { it.map { Hamster(it, mContext) } }
                    .map { it.sortedByDescending { it.description.length } }
                    .map { it.filter { matchQuery(it, searchQuery) } }
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(onLoadedFromNetwork)
                    .observeOn(mDbOperationScheduler)
                    .doOnSuccess { mHamsterStorage.updateHamsters(it) }
                    .onErrorResumeNext {
                        Single.fromCallable { mHamsterStorage.getHamsters() }
                                .subscribeOn(mDbOperationScheduler)
                                .map { it.sortedByDescending { it.description.length } }
                                .map { it.filter { matchQuery(it, searchQuery) } }
                                .onErrorReturnItem(ArrayList())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSuccess(onLoadedFromCache)
                    }
                    .ignoreElement()
                    .onErrorComplete()

    private fun matchQuery(hamster: Hamster, query: String?): Boolean {
        if (query == null || query.isEmpty()) {
            return true
        }

        val words = query.split(Regex(" +"))
        words.forEach {
            if (hamster.title.contains(it, true)
                    || hamster.description.contains(it, true)) {
                return true
            }
        }
        return false
    }
}