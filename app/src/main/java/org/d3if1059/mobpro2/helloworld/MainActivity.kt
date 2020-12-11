package org.d3if1059.mobpro2.helloworld

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import org.d3if1059.mobpro2.helloworld.widget.CovidWidgetService
import org.d3if1059.mobpro2.helloworld.widget.PrefUtils
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //menampilkan chart
        with(chart) {
            setNoDataText(getString(R.string.belum_ada_data))
            description.text = ""
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            legend.setDrawInside(false)
        }

        val adapter = MainAdapter()
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )

        // Kode untuk menuliskan tanggal pada sumbu x
        val formatter = SimpleDateFormat("dd MMM", Locale("ID", "id"))
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val pos = value.toInt() - 1
                val isValidPosition = pos >= 0 && pos < adapter.itemCount
                return if (isValidPosition) formatter.format(adapter.getDate(pos)) else ""
            }
        }

        // Berfungsi agar ketika grafik di-klik oleh pengguna,
        // RecyclerView akan scroll menampilkan data yang sesuai
        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry?, highlight: Highlight) {
                recyclerView.scrollToPosition(highlight.x.toInt())
            }

            override fun onNothingSelected() {}
        })

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getData().observe(this, Observer {
            adapter.setData(it)
            PrefUtils.saveData(prefs, it.last())
            CovidWidgetService.startActionUpdateUI(this)
        })
        viewModel.getStatus().observe(this, Observer { updateProgress(it) })
        viewModel.getEntries().observe(this, Observer { updateChart(it) })
    }

    private fun updateProgress(status: ApiStatus) {
        when (status) {
            ApiStatus.LOADING -> {
                progressBar.visibility = View.VISIBLE
            }
            ApiStatus.SUCCESS -> {
                progressBar.visibility = View.GONE
            }
            ApiStatus.FAILED -> {
                progressBar.visibility = View.GONE
                networkError.visibility = View.VISIBLE
            }
        }
    }

    private fun updateChart(entries: List<Entry>) {
        val dataset = LineDataSet(
            entries,
            getString(R.string.jumlah_kasus_positif)
        )
        dataset.color = ContextCompat.getColor(this, R.color.colorPrimary)
        dataset.fillColor = dataset.color
        dataset.setDrawFilled(true)
        dataset.setDrawCircles(false)
        chart.data = LineData(dataset)
        chart.invalidate()
    }
}