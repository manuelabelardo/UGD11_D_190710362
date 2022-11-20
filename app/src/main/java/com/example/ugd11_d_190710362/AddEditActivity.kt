package com.example.ugd11_d_190710362

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.example.ugd11_d_190710362.api.MobilApi
import com.example.ugd11_d_190710362.models.Mobil
import org.json.JSONObject
import java.lang.reflect.Method
import java.nio.charset.StandardCharsets

class AddEditActivity : AppCompatActivity() {

    private var etNama: EditText? = null
    private var etHarga: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        queue = Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etHarga = findViewById(R.id.et_harga)
        layoutLoading = findViewById(R.id.layout_loading)

        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id",-1)
        if (id == -1L) {
            tvTitle.setText("Tambah Mobil")
            btnSave.setOnClickListener { createMobil() }
        } else {
            tvTitle.setText("Edit Mobil")
            getMobilById(id)

            btnSave.setOnClickListener { updateMobil(id) }
        }
    }

    private fun getMobilById(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, MobilApi.GET_BY_ID_URL + 1d, Response.Listener { response ->
                val gson = Gson)
                val mobil = gson.fronJson(response,Mobil: :class. Java)

                etNama!!.setText (mobil.nama)
                etHarga!!.setText(mobil.harga)
                setExposedDropDownMenu()

                Toast.makeText(this@AddEditActivity, "Data berhas1l diambil!", Toast.LENGTH_SHORT).show()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String> ()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun createMahasiswa() {
        setLoading(true)

        val mobil = Mobil(
            etNama!!.text.toString(),
            etHarga!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, MobilApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                var mobil = gson.fromJson(response, Mobil::class.java)

                if(mobil != null)
                    Toast.makeText(this@AddEditActivity,"Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(Result_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT) , show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map‹String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(mobil)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        queue!!.add(stringRequest)
    }

    private fun updateMobil(id: Long) {
        setLoading(true)

        val mobil = Mobil(
            etNama!!.text.toString(),
            etHarga!!.text.toString(),
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, MobilApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var mobil = gson.fromJson(response, Mobil::class.java)

                if (mobil != null)
                    Toast.makeText(
                        this@AddEditActivity,
                        "Data berhasil diupdate",
                        Toast.LENGTH_SHORT
                    ).show()

                val , returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(mobil)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}