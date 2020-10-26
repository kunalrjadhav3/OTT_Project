package com.example.diagnal_assignment.views

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.diagnal_assignment.MainApp
import com.example.diagnal_assignment.R
import com.example.diagnal_assignment.dagger.components.DaggerActivityComponent
import com.example.diagnal_assignment.dagger.modules.ActivityModule
import com.example.diagnal_assignment.model.Content
import com.example.diagnal_assignment.utils.view.MaterialSearchView
import com.example.diagnal_assignment.viewmodel.MainActivityVM
import com.example.diagnal_assignment.views.adapters.PostersGridAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    internal val viewModel: MainActivityVM by lazy {
        ViewModelProviders.of(this).get(MainActivityVM::class.java)
    }
    private var adapter: PostersGridAdapter? = null
    private var mLastPage = false
    private var isKeyboardOpen = false
    private var isSearchViewOpen=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDependency()
        initView()
        initObservers()
        setListeners()
        registerStringObservabletoObserver(SearhViewObservable())
        viewModel.setNetworkRepository(apiInterface = MainApp.appContext.apiInterface)
        viewModel.fetchPageData()

    }

    private fun initDependency() {
        activityComponent =
            DaggerActivityComponent.builder().activityModule(ActivityModule()).build()
        activityComponent!!.inject(this)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        search_view.setMenuItem(menu.findItem(R.id.action_search))
        return true
    }

    private fun initView() {
        setSupportActionBar(toolBar)
        supportActionBar?.setTitle(R.string.category_name)
        search_view.setBackIcon(resources.getDrawable(R.drawable.ic_baseline_arrow_back_24))
        search_view.setCloseIcon(resources.getDrawable(R.drawable.ic_baseline_close_24))
        search_view.setCursorDrawable(R.drawable.custom_cursor)
        setGridColumns(resources.configuration.orientation) // When app is launched in Landscape/Potrait mode
        viewUtil.detectKeyboardChanges(container,this@MainActivity){
            isKeyboardOpen=it
        }
    }

    private fun setGridColumns(orientation: Int){
        gv_posters.numColumns = if (orientation == Configuration.ORIENTATION_LANDSCAPE) 7 else 3
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        search_view.closeSearch()
        setGridColumns(newConfig.orientation) // When app changes orientation
    }


    private fun setListeners() {
        gv_posters.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (!isSearchViewOpen && !isKeyboardOpen && !mLastPage && firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0)
                    viewModel.fetchPageData(true)
            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {}
        })

        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {
                isSearchViewOpen=true
            }

            override fun onSearchViewClosed() {
                isSearchViewOpen=false
                if (adapter == null) {
                    adapter = PostersGridAdapter(this@MainActivity, viewModel.originalList)
                    gv_posters.adapter = adapter
                } else {
                    adapter!!.posterList.clear()
                    updateGridViewAdapter(viewModel.originalList)
                }
            }
        })
    }


    private fun initObservers() {
        viewModel.loadingstatusLiveData.observe(this, Observer {
            viewUtil.disableUserTouch(this,it,progressBar)
        })

        viewModel.successData.observe(this, Observer {
            if (adapter == null) {
                adapter = PostersGridAdapter(this, it as ArrayList<Content>)
                gv_posters.adapter = adapter
            } else if (it == emptyList<Content>())
                mLastPage = true // Real World case, when API response returns a Page with No response
             else
                updateGridViewAdapter(it)
        }
        )
        viewModel.errorData.observe(this, Observer
        {
            mLastPage = it.contains(resources.getString(R.string.fileNotFoundException))
            if(mLastPage)
                viewUtil.displaySnackbar(container,resources.getString(R.string.noDataError))
        })
    }

    private fun updateGridViewAdapter(updatedList: List<Content>) {
        adapter!!.posterList.addAll(updatedList)
        adapter!!.notifyDataSetChanged()

    }

    private fun registerStringObservabletoObserver(stringObservable: Observable<String>) {
        viewModel.resgisterSearchStringSubscription(stringObservable).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribe(getObserver())
    }

    /*
    Update gv_posters with user search Results returned from MainActivityVM.getModifiedObservable()
    */
    private fun getObserver(): io.reactivex.Observer<List<Content>> {
        return object : io.reactivex.Observer<List<Content>> {
            override fun onComplete() {}
            override fun onError(e: Throwable) {}
            override fun onNext(searchResultList: List<Content>) {
                adapter?.let {
                    it.posterList.clear()
                    updateGridViewAdapter(searchResultList)
                }
            }

            override fun onSubscribe(d: Disposable) {}
        }

    }

/*
* emit user search input as Observable
*/
    private fun SearhViewObservable(): Observable<String> {
        return Observable.create { e ->
            search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String): Boolean {
                    e.onNext(text)
                    return false
                }
                override fun onQueryTextChange(text: String): Boolean {
                    e.onNext(text)
                    return false
                }
            })
        }

    }



    override fun onBackPressed() {
        if (search_view.isSearchOpen())
            search_view.closeSearch()
         else
            super.onBackPressed()

    }

}