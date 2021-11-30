package com.delet_dis.elementarylauncher.presentation.activities.aboutActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.ActivityAboutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initBackButtonOnClickListener()

        initSupportButtonOnClickListener()
    }

    private fun initSupportButtonOnClickListener() = with(binding) {
        supportButton.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse(getString(R.string.supportLink)))
            )
        }
    }

    private fun initBackButtonOnClickListener() = with(binding) {
        backButton.setOnClickListener {
            finish()
        }
    }
}