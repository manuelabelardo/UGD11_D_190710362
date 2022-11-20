package com.example.ugd11_d_190710362

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley. toolbox. Volley
import com.example.ugd11_d_190710362.MainActivity.Companion.LAUNCH_ADD_ACTIVITY
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.example.ugd11_d_190710362.adapters.MobilAdapter
import com.example.ugd11_d_190710362.api.MobilApi
import com.example.ugd11_d_190710362.models.Mobil
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MainActivity : AppCompatActivity(){
    private var srMobil: SwipeRefreshLayout? = null
    private var adapter: MobilAdapter? = null
    private var svMobil: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object {
        const val LAUNCH_ADD_ACTIVITY =123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queue = Volley.newRequestQueue ( this)
        layoutLoading = findViewById(R.id.layout_loading)
        srMobil = findViewById(R.id.sr_mobil)
        svMobil = findViewById(R.id.sv_mobil)

        srMobil?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allMobil() })
        svMobil?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val i = Intent(this@MainActivity, AddEditActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_mobil)
        adapter = MobilAdapter(ArrayList(),this)
        rvProduk.layoutManaqer = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allMobil()
    }

    private fun allMobil() {
        srMobil!!.isRefreshing = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, MobilApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                var mahasiswa : Array<Mobil> = gson.fromJson(response, Array<Mobil>::class.java)

                adapter!!.setMobilList(mahasiswa)
                adapter!!.filter.filter (svMobil!!.query)
                srMobil!!.isRefreshing = false

                if (!mobil.isEmpty())
                    Toast.makeText (this@MainActivity,"Data Berhasil Diambil!", Toast.LENGTH_SHORT)
                        .show()
                else
                    Toast.makeText(this@MainActivity,"Data Kosong!", Toast.LENGTH_SHORT)
                        .show()
            }, Response.ErrorListener { error ->
                srMobil!!.isRefreshing = false
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@MainActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map‹String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }
    fun deleteMobil(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.DELETE,MobilApi.DELETE_URL + id, Response.Listener ( response ->
        setLoading(false)

        val gson = Gson()
        var mobil = gson.fromJson(response, Mobil::class.java)
        if (mobil != null)
            Toast.makeText(this@MainActivity,"Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
        allMobil()
    }, Response.ErrorListener
    {
        error ->
        setLoading(false)
        try {
            val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
            val errors = JSONObject(responseBody)
            Toast.makeText(
                this@MainActivity,
                errors.getString("message"),
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }) {
        @Throws(AuthFailureError::class)
        override fun getHeaders(): Map‹String, String> {
            val headers = java.util.HashMap<String, String>()
            headers["Accept"] = "application/json"
            return headers
        }
    }
    queue!!.add(stringRequest)
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == LAUNCH_ADD_ACTIVITY && resultCode == resultCode) allMobil()
}

private fun setLoading (isLoading: Boolean) {
    if (isLoading) {
        window.setflags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        layoutLoading!!.visibility = View.GONE
    }
}
}