package com.mindbodyonline.nrwithchunkedresponses

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.newrelic.agent.android.NewRelic
import com.newrelic.agent.android.logging.AgentLog
import com.mindbodyonline.nrwithchunkedresponses.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NewRelic.withApplicationToken(BuildConfig.NEW_RELIC_APPLICATION_TOKEN)
            .withLogLevel(AgentLog.AUDIT)
            .start(this)

        val binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        bindButtons(binding)
    }

    private fun bindButtons(binding: ActivityMainBinding) {
        binding.mockServerButton.setOnClickListener {
            viewModel.mockCall()
            Toast.makeText(this, R.string.mock_request_sent, Toast.LENGTH_SHORT).show()
        }

        binding.realServerButton.setOnClickListener {
            viewModel.realCall()
            Toast.makeText(this, R.string.real_request_sent, Toast.LENGTH_SHORT).show()
        }
    }
}