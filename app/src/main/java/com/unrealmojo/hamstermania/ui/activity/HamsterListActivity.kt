package com.unrealmojo.hamstermania.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.unrealmojo.hamstermania.*
import com.unrealmojo.hamstermania.data.ui.Hamster
import com.unrealmojo.hamstermania.data.ui.HamsterSuggestion
import com.unrealmojo.hamstermania.floatingsearchviewrxbinding.RxFloatingSearchView
import com.unrealmojo.hamstermania.model.HamsterModel
import com.unrealmojo.hamstermania.ui.adapter.HamsterAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_hamster_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HamsterListActivity : AppCompatActivity() {
    @Inject
    lateinit var hamsterModel: HamsterModel

    private lateinit var mAdapter: HamsterAdapter

    private var mMainContentDisposable: Disposable? = null
    private var mSearchViewDisposable: Disposable? = null
    private var mSuggestionDisposable: Disposable? = null

    init {
        App.component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hamster_list)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mAdapter = HamsterAdapter(this, { hamster, holder ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(this, holder.image, "hamsterImage")
                HamsterDetailActivity.startActivity(this@HamsterListActivity, hamster, options)
            } else {
                HamsterDetailActivity.startActivity(this@HamsterListActivity, hamster, null)
            }
        }, { hamster, _ ->
            shareHamster(this@HamsterListActivity, hamster)
        })

        hamstersList.adapter = mAdapter
        hamstersList.layoutManager = LinearLayoutManager(this)

        mSearchViewDisposable = RxFloatingSearchView.queryChanges(search, 0)
                .skipInitialValue()
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { query ->
                    mSuggestionDisposable?.dispose()
                    if (query.isNullOrBlank() || !search.isSearchBarFocused) {
                        search.clearSuggestions()
                        return@subscribe
                    }
                    search.showProgress()
                    mSuggestionDisposable = hamsterModel.getHamsters(query.toString(), {
                        search.swapSuggestions(it.take(4).map { HamsterSuggestion(it) })
                    }, {
                        search.swapSuggestions(it.take(4).map { HamsterSuggestion(it) })
                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnDispose { search.hideProgress() }
                            .subscribe { search.hideProgress() }
                }

        search.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSearchAction(currentQuery: String) {
                loadHamsters(currentQuery)
            }

            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {
                val hamsterSuggestion = searchSuggestion as HamsterSuggestion
                val hamster = Hamster(hamsterSuggestion)
                HamsterDetailActivity.startActivity(this@HamsterListActivity, hamster, null)
            }
        })

        search.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
            override fun onFocusCleared() {
                mSuggestionDisposable?.dispose()
            }

            override fun onFocus() {
            }
        })

        search.setOnMenuItemClickListener {
            if (it.itemId == R.id.btnAbout) {
                startActivity(Intent(this@HamsterListActivity, AboutActivity::class.java))
            }
        }

        refresh.setOnRefreshListener { loadHamsters(search.query) }

        loadHamsters(search.query)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainContentDisposable?.dispose()
        mSearchViewDisposable?.dispose()
        mSuggestionDisposable?.dispose()
    }

    override fun onBackPressed() {
        when {
            search.query.isNotEmpty() -> {
                search.setSearchText("")
                loadHamsters(null)
            }
            search.isSearchBarFocused -> search.setSearchFocused(false)
            else -> super.onBackPressed()
        }
    }

    private fun loadHamsters(searchQuery: String?) {
        showProgress()
        mMainContentDisposable?.dispose()
        mMainContentDisposable = hamsterModel.getHamsters(searchQuery, { hamstersFromNetwork ->
            if (hamstersFromNetwork.isNotEmpty()) {
                showContent(hamstersFromNetwork)
            } else {
                showMessage(R.string.msg_no_hamsters)
            }
        }, { hamstersFromCache ->
            Snackbar.make(root, R.string.msg_hamsters_loaded_from_cache, Snackbar.LENGTH_LONG).show()
            if (hamstersFromCache.isNotEmpty()) {
                showContent(hamstersFromCache)
            } else {
                showMessage(R.string.msg_no_hamsters)
            }
        })
                .subscribe()
    }

    private fun showContent(hamsters: List<Hamster>) {
        mAdapter.data = hamsters
        message.visibility = View.GONE
        refresh.isRefreshing = false
        hamstersList.visibility = View.VISIBLE
    }

    private fun showProgress() {
        refresh.isRefreshing = true
    }

    private fun showMessage(msg: Int) {
        message.setText(msg)
        message.visibility = View.VISIBLE
        refresh.isRefreshing = false
        hamstersList.visibility = View.INVISIBLE
    }
}
