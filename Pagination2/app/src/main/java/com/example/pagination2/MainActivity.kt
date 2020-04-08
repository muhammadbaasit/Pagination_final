package com.example.pagination2

import PaginationAdapter
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagination2.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var binding: ActivityMainBinding?=null
    var padapter: PaginationAdapter? = null
    var rcPViewModel:PViewModel?=null
    var isLoading = false
    var isLastPage = false
    val TOTAL_PAGES = 10
    var CURRENT_PAGES = 1
    var mlist:MutableList<Model>?= arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding!!.lifecycleOwner

        rcPViewModel= ViewModelProvider(this@MainActivity).get(PViewModel::class.java)

//---------------------------------------------------------------------------------------------

        main_progress.visibility = View.GONE
        padapter= PaginationAdapter(this@MainActivity)
        var layoutManager= LinearLayoutManager(this@MainActivity)
        binding!!.rv.layoutManager= layoutManager
        binding!!.rv.adapter=padapter
        binding!!.rv!!.addOnScrollListener(object : PaginationScrollListener(layoutManager!!) {

            override fun loadMoreItems() {

                isLoading = true
                CURRENT_PAGES = CURRENT_PAGES +1

                Handler().postDelayed({

                        loadNextPage()
                        mlist?.clear()

                }, 1500)
            }
            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }
            override fun isLastPage(): Boolean {
                return isLastPage
            }
            override fun isLoading(): Boolean {
                return isLoading
            }
        })

        Handler().postDelayed({
            loadFirstPage()
            mlist?.clear()

        }, 1500)
    }

    open fun loadFirstPage() {

        rcPViewModel?.apply {

            getApiData(CURRENT_PAGES, TOTAL_PAGES)

            alMutableLiveData.observe(this@MainActivity, androidx.lifecycle.Observer { list ->
                mlist = list
            })
        }

        main_progress.visibility = View.GONE

        padapter?.addAll(mlist!!)

        if (CURRENT_PAGES <= TOTAL_PAGES)
            padapter?.addLoadingFooter()
        else isLastPage = true
    }

    open fun loadNextPage() {

        rcPViewModel?.apply {

             getApiData(CURRENT_PAGES,TOTAL_PAGES)
                alMutableLiveData.observe(this@MainActivity,androidx.lifecycle.Observer {list ->
                 mlist = list
            })
        }

        padapter?.removeLoadingFooter()

        isLoading = false

            padapter?.addAll(mlist!!)

        if (CURRENT_PAGES !=null && mlist?.size != 0){
            padapter?.addLoadingFooter()
        }else{
             isLastPage = true
             isLoading = false
        }
    }
}
