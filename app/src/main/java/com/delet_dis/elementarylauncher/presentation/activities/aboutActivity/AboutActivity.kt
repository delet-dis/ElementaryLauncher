package com.delet_dis.elementarylauncher.presentation.activities.aboutActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.ActivityAboutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAboutBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val safe = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(safe.left, safe.top, safe.right, safe.bottom)
            insets
        }

        initBackButtonOnClickListener()

        initSupportButtonOnClickListener()
    }

    private fun initSupportButtonOnClickListener() = with(binding) {
        privacyPolicyButton.setOnClickListener {
            runCatching {
                startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(getString(R.string.privacyPolicyLink)))
                )
            }
        }
    }

    private fun initBackButtonOnClickListener() = with(binding) {
        backButton.setOnClickListener {
            finish()
        }
    }
}
