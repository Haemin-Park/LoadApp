package com.example.loadapp

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.loadapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)

        val fileName = intent.getStringExtra("fileName")
        val fileStatus = intent.getBooleanExtra("downloadStatus", false)

        binding.contentDetail.tvFileName.text =
            String.format(resources.getString(R.string.file_name_format), fileName)
        var status = ""
        if (fileStatus) {
            status = "Success"
        } else {
            status = "Fail"
            binding.contentDetail.tvFileStatus.setTextColor(Color.RED)
        }
        binding.contentDetail.tvFileStatus.text =
            String.format(resources.getString(R.string.file_status_format), status)

        binding.contentDetail.btnOk.setOnClickListener {
            finish()
        }
    }

}
