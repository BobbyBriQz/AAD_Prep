package com.bobby.aad_prep.ui

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bobby.aad_prep.api.state.Status
import com.bobby.aad_prep.databinding.ActivityGithubBinding
import com.bobby.aad_prep.ui.adapter.RepositoryAdapter
import com.bobby.aad_prep.vm.GithubViewModel
import com.google.android.material.snackbar.Snackbar

class GithubActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityGithubBinding
    private lateinit var mAdapter: RepositoryAdapter
    private val githubViewModel : GithubViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         mBinding = ActivityGithubBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initUI()
        githubViewModel.repoSearchResultLiveData.observe(this){
            when(it.status){
                Status.LOADING -> {
                    mBinding.loadingBar.visibility = View.VISIBLE
                }

                Status.SUCCESS -> {
                    mBinding.loadingBar.visibility = View.GONE
                    mAdapter.setList(it.data!!)
                }

                Status.ERROR -> {
                    mBinding.loadingBar.visibility = View.GONE
                    Snackbar.make(mBinding.root, it.message.toString(), Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry"){
                                searchRepo()
                            }
                            .show()
                }
                else ->{}
            }
        }
    }

    private fun initUI() {
        mBinding.searchBtn.setOnClickListener {
            hideKeyboard()
            searchRepo()
        }

        mAdapter = RepositoryAdapter()
        mBinding.repoRV.apply {
            layoutManager = LinearLayoutManager(this@GithubActivity)
            this.adapter = mAdapter
        }
    }

    private fun searchRepo() {
        val query = mBinding.searchET.text.toString()
        githubViewModel.searchRepo(query = query, page = 1, itemsPerPage = 20)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}