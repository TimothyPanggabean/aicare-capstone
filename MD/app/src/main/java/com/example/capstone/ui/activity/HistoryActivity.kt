package com.example.capstone.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.capstone.data.local.UserSession
import com.example.capstone.data.remote.pojo.ListHistoryItem
import com.example.capstone.databinding.ActivityHistoryBinding
import com.example.capstone.ui.adapter.ListHistoryAdapter
import com.example.capstone.ui.viewmodel.HistoryViewModel
import com.example.capstone.ui.viewmodel.factory.ViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "History"
        val pref = UserSession.getInstance(dataStore)

        historyViewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[HistoryViewModel::class.java]

        historyViewModel.userToken.observe(this) { token ->
            historyViewModel.getUserHistory(token)
        }
        historyViewModel.listHistory.observe(this) {
            showHistoryRecyclerList(it)
        }
        historyViewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun showHistoryRecyclerList(history: List<ListHistoryItem>) {
        val histories = ArrayList<ListHistoryItem>()
        histories.addAll(history)

        val listFollowersAdapter = ListHistoryAdapter(histories)

        binding.rvHistory.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 1)
            adapter = listFollowersAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

}