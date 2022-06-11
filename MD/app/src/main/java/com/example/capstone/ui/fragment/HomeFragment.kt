package com.example.capstone.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.capstone.R
import com.example.capstone.ui.activity.HistoryActivity
import com.example.capstone.ui.activity.MapsActivity
import com.example.capstone.ui.activity.ProfileActivity
import com.example.capstone.ui.activity.TestModelActivity

class HomeFragment : Fragment() {
    private lateinit var mapsCardView: CardView
    private lateinit var historyCardView: CardView
    private lateinit var cameraCardView: CardView
    private lateinit var profileCardView: CardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapsCardView = view.findViewById(R.id.card_view_maps)
        mapsCardView.setOnClickListener {
            val i = Intent(activity, MapsActivity::class.java)
            startActivity(i)
        }

        historyCardView = view.findViewById(R.id.card_view_history)
        historyCardView.setOnClickListener {
            val i = Intent(activity, HistoryActivity::class.java)
            startActivity(i)
        }

        cameraCardView = view.findViewById(R.id.card_view_camera)
        cameraCardView.setOnClickListener {
            val i = Intent(activity, TestModelActivity::class.java)
            startActivity(i)
        }

        profileCardView = view.findViewById(R.id.card_view_profile)
        profileCardView.setOnClickListener {
            val i = Intent(activity, ProfileActivity::class.java)
            startActivity(i)
        }
    }
}